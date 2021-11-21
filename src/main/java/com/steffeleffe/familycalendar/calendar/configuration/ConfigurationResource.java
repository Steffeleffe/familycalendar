package com.steffeleffe.familycalendar.calendar.configuration;


import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/configuration")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConfigurationResource {

    @GET
    public List<CalendarConfiguration> list() {
        return CalendarConfiguration.listAll();
    }

    @GET
    @Path("/{id}")
    public CalendarConfiguration get(@PathParam("id") Long id) {
        return CalendarConfiguration.findById(id);
    }

    @POST
    @Transactional
    public Response create(CalendarConfiguration person) {
        person.persist();
        return Response.created(URI.create("/persons/" + person.id)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public CalendarConfiguration update(@PathParam("id") Long id, CalendarConfiguration configuration) {
        CalendarConfiguration entity = CalendarConfiguration.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }

        // map all fields from the configuration parameter to the existing entity
        entity.active = configuration.active;
        entity.defaultPicture = configuration.defaultPicture;
        entity.defaultParticipant = configuration.defaultParticipant;
        entity.calendarId = configuration.calendarId;

        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        CalendarConfiguration entity = CalendarConfiguration.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }
        entity.delete();
    }

    @GET
    @Path("/search/{calendarId}")
    public CalendarConfiguration search(@PathParam("calendarId") String calendarId) {
        return CalendarConfiguration.findByCalendarId(calendarId).orElseThrow(NotFoundException::new);
    }

}