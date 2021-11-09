package com.steffeleffe.familycalendar.calendar;

import java.util.List;

public interface CalendarImporter {
    public List<FamilyCalendar> getCalendars();

    public List<FamilyEvent> getEvents(String calendarId);

    public void initialize() throws CalendarImportException;
}
