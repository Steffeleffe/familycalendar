package com.github.steffeleffe;

import com.github.steffeleffe.calendar.CalendarEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

class CalendarEventTemplateExtensionsTest {

    @Test
    void dayIndexToday() {
        CalendarEvent.Builder builder = new CalendarEvent.Builder();
        builder.setStartTime(Instant.now());
        CalendarEvent today = builder.build();

        int dayIndex = CalendarEventTemplateExtensions.dayIndex(today);

        assertThat(dayIndex).isEqualTo(0);
    }

    @Test
    void dayIndexInThreeDays() {
        CalendarEvent.Builder builder = new CalendarEvent.Builder();
        builder.setStartTime(Instant.now().plus(3, ChronoUnit.DAYS));
        CalendarEvent threeDaysFromNow = builder.build();

        int dayIndex = CalendarEventTemplateExtensions.dayIndex(threeDaysFromNow);

        assertThat(dayIndex).isEqualTo(3);
    }

    @Test
    void timeStringForEventSameDay() {
        CalendarEvent.Builder builder = new CalendarEvent.Builder();
        builder.setStartTime(Instant.parse("2007-12-03T10:15:00.00Z"));
        builder.setEndTime(Instant.parse("2007-12-03T10:45:00.00Z"));
        CalendarEvent event = builder.build();

        String timeString = CalendarEventTemplateExtensions.timeString(event);

        assertThat(timeString).isEqualTo("11.15-11.45");
    }

    @Test
    void timeSlotForEvent() {
        CalendarEvent.Builder builder = new CalendarEvent.Builder();
        builder.setStartTime(Instant.parse("2007-12-03T10:15:00.00Z"));
        CalendarEvent event = builder.build();

        String timeSlot = CalendarEventTemplateExtensions.timeSlot(event);

        assertThat(timeSlot).isEqualTo("1115");
    }
}