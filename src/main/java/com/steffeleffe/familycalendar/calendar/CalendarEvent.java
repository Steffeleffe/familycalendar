package com.steffeleffe.familycalendar.calendar;

import java.time.Instant;
import java.util.Set;

public record CalendarEvent(
        String id,
        String summary,
        String imageUrl,
        Instant startTime,
        Instant endTime,
        Set<Participant> participants,
        String calendarId
) {
}

