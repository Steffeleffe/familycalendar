package com.steffeleffe.familycalendar;

import com.steffeleffe.familycalendar.calendar.CalendarEvent;
import com.steffeleffe.familycalendar.calendar.CalendarService;
import com.steffeleffe.familycalendar.calendar.Day;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.*;
import java.util.stream.Collectors;

@Path("/calendar")
public class CalendarResource {

    @Inject
    Template page;

    @Inject
    CalendarService calendarService;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get() {
        List<CalendarEvent> events = calendarService.getEvents();
        return page.data(
                "days", Day.getFiveDays(),
                "events", events,
                "timeSlots", getTimeSlots(events));
    }

    private List<String> getTimeSlots(List<CalendarEvent> events) {
        Set<String> timeSlotSet = events.stream()
                .map(CalendarEventTemplateExtensions::timeSlot)
                .collect(Collectors.toUnmodifiableSet());
        ArrayList<String> list = new ArrayList<>(timeSlotSet);
        list.sort(null); // natural ordering for String
        return list;
    }

    @GET
    @Path("refresh")
    public void refresh() {
        calendarService.importEvents();
    }

    @GET
    @Path("events")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CalendarEvent> events() {
        return calendarService.getEvents();
    }

}
