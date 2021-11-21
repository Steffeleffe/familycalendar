package com.steffeleffe.familycalendar.calendar;

import com.steffeleffe.familycalendar.calendar.configuration.CalendarConfiguration;
import com.steffeleffe.familycalendar.calendar.googleimporter.GoogleCalendarImporter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class CalendarListService {

    @Inject
    GoogleCalendarImporter importer;

    List<FamilyCalendar> familyCalendars;

    @Transactional
    public List<FamilyCalendar> getCalendars() {
        if (familyCalendars == null) {
            familyCalendars = importer.getCalendars();
            for (FamilyCalendar familyCalendar : familyCalendars) {
                Optional<CalendarConfiguration> byCalendarId = CalendarConfiguration.findByCalendarId(familyCalendar.id());
                if (byCalendarId.isEmpty()) {
                    CalendarConfiguration calendarConfiguration = new CalendarConfiguration();
                    calendarConfiguration.calendarId = familyCalendar.id();
                    calendarConfiguration.active = true;
                    calendarConfiguration.persist();
                }
            }
        }
        return familyCalendars;
    }

    public List<String> getCalendarIds() {
        return getCalendars().stream()
                .map(FamilyCalendar::id)
                .collect(Collectors.toUnmodifiableList());
    }

    public boolean isCalendarActive(String calendarId) {
        return CalendarConfiguration.isActive(calendarId);
    }

}
