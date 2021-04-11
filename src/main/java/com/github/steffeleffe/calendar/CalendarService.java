package com.github.steffeleffe.calendar;

import com.google.api.services.calendar.model.Event;

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
            "66aglhcacpcpupnhh9fian0a1g@group.calendar.google.com",
            "hdn3t11kjru1fs823pee8g9bso@group.calendar.google.com"
    );

    public List<CalendarEvent> getEvents() {
        return googleCalendarIds.stream()
                .map(id -> importer.fetchEvents(id))
                .flatMap(Collection::stream)
                .map(CalendarService::toCalendarEvent)
                .collect(Collectors.toList());

    }

    private static CalendarEvent toCalendarEvent(Event event) {
        CalendarEvent.Builder builder = new CalendarEvent.Builder();

        builder.setId(event.getId());
        builder.setSummary(event.getSummary());
        builder.setStartTime(Instant.ofEpochMilli(event.getStart().getDateTime().getValue()));
        builder.setEndTime(Instant.ofEpochMilli(event.getEnd().getDateTime().getValue()));
        builder.setImageUrl(getImageUrl(event.getDescription()));
        builder.setParticipants(getParticipants(event.getDescription()));

        return builder.build();
    }

    static Set<Participant> getParticipants(String eventDescription) {
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

    static String getImageUrl(String eventDescription) {
        if (eventDescription == null) {
            return null;
        }
        Pattern p = Pattern.compile("[bB]illede:(.+)");
        Matcher m = p.matcher(eventDescription);
        return m.find() ? m.group(1).trim() : null;
    }

}
