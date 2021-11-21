package com.steffeleffe.familycalendar.calendar;

import com.steffeleffe.familycalendar.calendar.googleimporter.GoogleCalendarImporter;
import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class CalendarService {

    @Inject
    GoogleCalendarImporter importer;

    @Inject
    CalendarListService calendarListService;

    private List<FamilyEvent> importedFamilyEvents;

    public List<FamilyEvent> getEvents() {
        if (importedFamilyEvents == null) {
            importEvents();
        }
        return importedFamilyEvents.stream()
                .filter(e -> calendarListService.isCalendarActive(e.calendarId()))
                .collect(Collectors.toUnmodifiableList());
    }

    @Scheduled(every="30m")
    public void importEvents() {
        List<FamilyEvent> importedEvents = new ArrayList<>();
        for (String calendarId : calendarListService.getCalendarIds()) {
            importedEvents.addAll(importer.getEvents(calendarId));
        }
        this.importedFamilyEvents = importedEvents;
    }

}
