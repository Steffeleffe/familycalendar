package com.steffeleffe.familycalendar;

import com.steffeleffe.familycalendar.calendar.FamilyEvent;
import io.quarkus.qute.TemplateExtension;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@TemplateExtension
public class CalendarEventTemplateExtensions {
    private static Random random = new Random();
    private static ZoneId ZONE = ZoneId.of("Europe/Copenhagen");
    private static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH.mm");
    private static DateTimeFormatter FORMATTER2 = DateTimeFormatter.ofPattern("HHmm");

    static int dayIndex(FamilyEvent event) {
        return (int)(ZonedDateTime.ofInstant(event.startTime(), ZONE).getLong(ChronoField.EPOCH_DAY)
                - ZonedDateTime.now(ZONE).getLong(ChronoField.EPOCH_DAY));
    }

    static String timeString(FamilyEvent event) {
        return event.startTime().atZone(ZONE).format(FORMATTER) + '-' + event.endTime().atZone(ZONE).format(FORMATTER);
    }

    static String timeSlot(FamilyEvent event) {
        return event.startTime().atZone(ZONE).format(FORMATTER2);
    }

    static boolean isBeforeNoon(FamilyEvent event) {
        return event.startTime().atZone(ZONE).getHour() < 12;
    }

    static boolean isAfterNoon(FamilyEvent event) {
        return event.startTime().atZone(ZONE).getHour() >= 12;
    }

    static List<FamilyEvent> sorted(List<FamilyEvent> events) {
        return events.stream().sorted(Comparator.comparing(FamilyEvent::startTime)).toList();
    }

    static String color(FamilyEvent event) {
        StringBuilder sb = new StringBuilder("#");
        for (int i=0; i<3; i++) {
            sb.append(random.nextInt(3) + 5);
        }
        return sb.toString();
    }

}
