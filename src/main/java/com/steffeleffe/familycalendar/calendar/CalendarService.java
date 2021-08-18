package com.steffeleffe.familycalendar.calendar;

import com.google.api.services.calendar.model.Event;
import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@ApplicationScoped
public class CalendarService {

    @Inject
    CalendarQuickstart importer;

    static List<String> googleCalendarIds = Arrays.asList(
            "primary",
            "3eq4uqnkhcgipgkdrrhs7ec6e4@group.calendar.google.com",
            "rikke.vangsted@gmail.com",
            "66aglhcacpcpupnhh9fian0a1g@group.calendar.google.com", //Rikkes work calendar
            "hdn3t11kjru1fs823pee8g9bso@group.calendar.google.com"
    );
    private List<CalendarEvent> calendarEvents;


    public synchronized List<CalendarEvent> getEvents() {
        if (calendarEvents == null) {
            importEvents();
        }
        return calendarEvents;
    }

    @Scheduled(every="30m")
    public void importEvents() {
        List<CalendarEvent> importedEvents = new ArrayList<>();
        for (String calendarId : googleCalendarIds) {
            List<Event> events = importer.fetchEvents(calendarId);
            List<CalendarEvent> eventsFromCalendar = events.stream()
                    .map(event -> CalendarService.toCalendarEvent(event, calendarId))
                    .collect(Collectors.toList());
            importedEvents.addAll(eventsFromCalendar);
        }
        this.calendarEvents = importedEvents;
    }

    static class EventHolder {
        final String description;
        final String calendarId;

        public EventHolder(String calendarId, String description) {
            this.calendarId = calendarId;
            this.description = description;
        }

    }

    private static CalendarEvent toCalendarEvent(Event event, String calendarId) {
        new EventHolder(event.getId(), event.getDescription());

        CalendarEvent.Builder builder = new CalendarEvent.Builder();

        builder.setId(event.getId());
        builder.setSummary(event.getSummary());
        builder.setStartTime(Instant.ofEpochMilli(event.getStart().getDateTime().getValue()));
        builder.setEndTime(Instant.ofEpochMilli(event.getEnd().getDateTime().getValue()));
        builder.setImageUrl(getImageUrl(event.getDescription(), calendarId));
        builder.setParticipants(getParticipants(event.getDescription(), calendarId));
        builder.setCalendarId(calendarId);

        return builder.build();
    }

    static Set<Participant> getParticipants(String eventDescription, String calendarId) {
        if (calendarId.equals("66aglhcacpcpupnhh9fian0a1g@group.calendar.google.com")) {
            return Collections.singleton(Participant.RIKKE);
        }
        if (eventDescription == null) {
            return Collections.emptySet();
        }

        Pattern p = Pattern.compile("[hH]vem:(.+)");
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
                .map(CalendarService::findParticipant)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toUnmodifiableSet());
    }

    private static Optional<Participant> findParticipant(String s) {
        return Arrays.stream(Participant.values())
                .filter(p -> p.id.equalsIgnoreCase(s))
                .findFirst();
    }

    static String getImageUrl(String eventDescription, String calendarId) {
        System.out.println(eventDescription);
        if (calendarId.equals("66aglhcacpcpupnhh9fian0a1g@group.calendar.google.com")) {
            return "https://www.flaticon.com/svg/static/icons/svg/3209/3209008.svg";
        }
        if (eventDescription == null) {
            return null;
        }

        Pattern p = Pattern.compile("[bB]illede:(\\s+?)?(<.+?>)?([a-z0-9_/\\-:.]+)");
        Matcher m = p.matcher(eventDescription);
        return m.find() ? m.group(3).trim() : null;
    }

}
