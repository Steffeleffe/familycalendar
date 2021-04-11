package com.github.steffeleffe;

import com.github.steffeleffe.calendar.CalendarEvent;
import io.quarkus.qute.TemplateExtension;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Random;

@TemplateExtension
public class CalendarEventTemplateExtensions {
    private static Random random = new Random();
    private static ZoneId ZONE = ZoneId.of("Europe/Copenhagen");
    private static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH.mm");
    private static DateTimeFormatter FORMATTER2 = DateTimeFormatter.ofPattern("HHmm");

    static int dayIndex(CalendarEvent event) {
        return (int)(ZonedDateTime.ofInstant(event.startTime, ZONE).getLong(ChronoField.EPOCH_DAY)
                - ZonedDateTime.now(ZONE).getLong(ChronoField.EPOCH_DAY));
    }

    static String timeString(CalendarEvent event) {
        return event.startTime.atZone(ZONE).format(FORMATTER) + '-' + event.endTime.atZone(ZONE).format(FORMATTER);
    }

    static String timeSlot(CalendarEvent event) {
        return event.startTime.atZone(ZONE).format(FORMATTER2);
    }

    static String color(CalendarEvent event) {
        StringBuilder sb = new StringBuilder("#");
        for (int i=0; i<3; i++) {
            sb.append(random.nextInt(3) + 5);
        }
        return sb.toString();
    }
}
