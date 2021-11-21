package com.steffeleffe.familycalendar.calendar.googleimporter;

// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// [START calendar_quickstart]
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.steffeleffe.familycalendar.calendar.*;
import com.steffeleffe.familycalendar.calendar.configuration.CalendarConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.io.*;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@ApplicationScoped
public class GoogleCalendarImporter implements CalendarImporter {
    private static final String APPLICATION_NAME = "Family Calendar";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleCalendarImporter.class);

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private boolean isTokenMissing() {
        File tokenFile = new File(TOKENS_DIRECTORY_PATH + "/StoredCredential");
        return !tokenFile.exists();
    }

    public void initialize() throws CalendarImportException {
        try {
            com.google.api.client.http.javanet.NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            getCredentials(httpTransport);
        } catch (IOException | GeneralSecurityException e) {
            throw new CalendarImportException("Failed to initialize Google API credentials.", e);
        }
    }

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = GoogleCalendarImporter.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }


    public synchronized List<FamilyEvent> getEvents(String calendarId) {
        if (isTokenMissing()) {
            LOGGER.info("StoredCredential token does not exist. User need to call /login endpoint.");
            return Collections.emptyList();
        } else {
            LOGGER.info("StoredCredential exists. Fetching events...");
        }
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            Instant todayTrimmedToMidnight = Instant.now().truncatedTo(ChronoUnit.DAYS);
            Instant fiveDaysFromNowTrimmedToMidnight = todayTrimmedToMidnight.plus(5, ChronoUnit.DAYS);

            Events events = service.events().list(calendarId)
                    .setMaxResults(1000)
                    .setTimeMin(new DateTime(todayTrimmedToMidnight.toEpochMilli()))
                    .setTimeMax(new DateTime(fiveDaysFromNowTrimmedToMidnight.toEpochMilli()))
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();

            List<Event> items = events.getItems();
            LOGGER.info("operation=\"fetched events\", calendarId=\"{}\", eventsCount={}", calendarId, items.size());
            return items.stream()
                    .filter(e -> e.getStart().getDateTime() != null)
                    .filter(e -> e.getEnd().getDateTime() != null)
                    .map(e -> toCalendarEvent(e, calendarId)).collect(Collectors.toUnmodifiableList());
        } catch (IOException | GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    private FamilyEvent toCalendarEvent(Event event, String calendarId) {
        return new FamilyEvent(
                event.getId(),
                event.getSummary(),
                getImageUrl(event.getDescription(), calendarId),
                Instant.ofEpochMilli(event.getStart().getDateTime().getValue()),
                Instant.ofEpochMilli(event.getEnd().getDateTime().getValue()),
                getParticipants(event.getDescription(), calendarId),
                calendarId);
    }

    Set<Participant> getParticipants(String eventDescription, String calendarId) {
        Optional<CalendarConfiguration> byCalendarId = CalendarConfiguration.findByCalendarId(calendarId);
        if (byCalendarId.isPresent() && byCalendarId.get().defaultParticipant != null) {
            String defaultParticipant = byCalendarId.get().defaultParticipant;
            if (Arrays.stream(Participant.values()).anyMatch((t) -> t.name().equals(defaultParticipant))) {
                return Collections.singleton(Participant.valueOf(defaultParticipant));
            }
        }

        if (eventDescription == null) {
            return Collections.emptySet();
        }


        Pattern p = Pattern.compile("[hH]vem:([a-zA-Z, ]+)");
        Matcher m = p.matcher(eventDescription);
        if (!m.find()) {
            return Collections.emptySet();
        }
        String trimmedParticipantsString = m.group(1).trim();
        if (trimmedParticipantsString.equalsIgnoreCase("alle")) {
            return new HashSet<>(Arrays.asList(Participant.values()));
        }
        String[] split = trimmedParticipantsString.split(",");

        return Arrays.stream(split)
                .map(String::trim)
                .map(this::findParticipant)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toUnmodifiableSet());
    }

    private Optional<Participant> findParticipant(String s) {
        return Arrays.stream(Participant.values())
                .filter(p -> p.id.equalsIgnoreCase(s))
                .findFirst();
    }

    URI getImageUrl(String eventDescription, String calendarId) {

        Optional<CalendarConfiguration> byCalendarId = CalendarConfiguration.findByCalendarId(calendarId);
        if (byCalendarId.isPresent() && byCalendarId.get().defaultPicture != null) {
            return URI.create(byCalendarId.get().defaultPicture);
        }

        if (eventDescription == null) {
            return null;
        }

        Pattern p = Pattern.compile("[bB]illede:(\\s+?)?(<.+?>)?([a-z0-9_/\\-:.]+)");
        Matcher m = p.matcher(eventDescription);
        return m.find() ? URI.create(m.group(3).trim()) : null;
    }

    public synchronized List<FamilyCalendar> getCalendars() {
        if (isTokenMissing()) {
            LOGGER.info("StoredCredential token does not exist. User need to call /login endpoint.");
            return Collections.emptyList();
        } else {
            LOGGER.info("StoredCredential exists. Fetching calendars...");
        }
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            CalendarList calendars = service.calendarList().list().execute();
            return calendars.getItems()
                    .stream()
                    .map(this::map)
                    .collect(Collectors.toUnmodifiableList());
        } catch (IOException | GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    private FamilyCalendar map(CalendarListEntry entry) {
        return new FamilyCalendar(entry.getId(), entry.getDescription());
    }

}
