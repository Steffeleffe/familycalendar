package com.github.steffeleffe;

import com.github.steffeleffe.calendar.CalendarEvent;
import com.github.steffeleffe.calendar.CalendarService;
import com.github.steffeleffe.calendar.Day;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.*;
import java.util.stream.Collectors;

@Path("/calendar/index.html")
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

}
