package com.steffeleffe.familycalendar;

import com.steffeleffe.familycalendar.calendar.*;
import com.steffeleffe.familycalendar.calendar.configuration.CalendarConfiguration;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/calendar")
public class CalendarResource {

    @Inject
    Template page;

    @Inject
    CalendarService calendarService;

    @Inject
    CalendarListService calendarListService;

    @Inject
    CalendarImporter importer;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get() {
        List<FamilyEvent> events = calendarService.getEvents();
        return page.data(
                "days", Day.getFiveDays(),
                "events", events,
                "timeSlots", getTimeSlots(events));
    }

    private List<String> getTimeSlots(List<FamilyEvent> events) {
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
    public List<FamilyEvent> events() {
        return calendarService.getEvents();
    }

    @GET
    @Path("calenders")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FamilyCalendar> calendars()
    {
        return calendarListService.getCalendars();
    }

    @GET
    @Path("login")
    public void login() throws CalendarImportException {
        importer.initialize();
        calendarService.importEvents();
    }

    @Transactional
    @POST
    @Path("disableCalendar/{calenderId}")
    public void inactivateCalendar(@PathParam("calenderId") String calendarId) {
        CalendarConfiguration configuration = CalendarConfiguration.findByCalendarId(calendarId)
                .orElseThrow(() -> new IllegalStateException("There is no calendar configuration)"));
        configuration.active = false;
        configuration.persist();
    }

    @GET
    @Path("configuration")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CalendarConfiguration> configuration() {
        return CalendarConfiguration.listAll();
    }

    @Transactional
    @POST
    @Path("setPictureUrl/{calenderId}/{pictureUrl}")
    public void setPictureUrl(@PathParam("calenderId") String calendarId, @PathParam("pictureUrl") String pictureUrl) {
        CalendarConfiguration configuration = CalendarConfiguration.findByCalendarId(calendarId)
                .orElseThrow(() -> new IllegalStateException("There is no calendar configuration)"));
        configuration.defaultPicture = pictureUrl;
        configuration.persist();
    }

}
