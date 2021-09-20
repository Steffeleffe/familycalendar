package com.steffeleffe.familycalendar;

import com.steffeleffe.familycalendar.calendar.CalendarEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

class CalendarEventTemplateExtensionsTest {

    @Test
    void dayIndexToday() {
        CalendarEvent today = getCalendarEvent(Instant.now(), null);

        int dayIndex = CalendarEventTemplateExtensions.dayIndex(today);

        assertThat(dayIndex).isEqualTo(0);
    }

    @Test
    void dayIndexInThreeDays() {
        CalendarEvent threeDaysFromNow = getCalendarEvent(Instant.now().plus(3, ChronoUnit.DAYS), null);

        int dayIndex = CalendarEventTemplateExtensions.dayIndex(threeDaysFromNow);

        assertThat(dayIndex).isEqualTo(3);
    }

    @Test
    void timeStringForEventSameDay() {
        Instant startTime = Instant.parse("2007-12-03T10:15:00.00Z");
        Instant endTime = Instant.parse("2007-12-03T10:45:00.00Z");
        CalendarEvent event = getCalendarEvent(startTime, endTime);

        String timeString = CalendarEventTemplateExtensions.timeString(event);

        assertThat(timeString).isEqualTo("11.15-11.45");
    }

    private CalendarEvent getCalendarEvent(Instant startTime, Instant endTime) {
        return new CalendarEvent(null, null, null, startTime, endTime, null, null);
    }

    @Test
    void timeSlotForEvent() {
        CalendarEvent event = getCalendarEvent(Instant.parse("2007-12-03T10:15:00.00Z"), null);

        String timeSlot = CalendarEventTemplateExtensions.timeSlot(event);

        assertThat(timeSlot).isEqualTo("1115");
    }
}