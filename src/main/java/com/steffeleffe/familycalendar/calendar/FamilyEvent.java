package com.steffeleffe.familycalendar.calendar;

import java.net.URI;
import java.time.Instant;
import java.util.Set;

public record FamilyEvent(
        String id,
        String summary,
        URI imageUrl,
        Instant startTime,
        Instant endTime,
        Set<Participant> participants,
        String calendarId
) {
}

