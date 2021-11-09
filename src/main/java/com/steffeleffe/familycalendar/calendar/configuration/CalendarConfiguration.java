package com.steffeleffe.familycalendar.calendar.configuration;

import com.steffeleffe.familycalendar.calendar.Participant;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.net.URI;
import java.util.Optional;

@Entity
public class CalendarConfiguration extends PanacheEntity {
    public String calendarId;
    public boolean active;
    public String defaultParticipant = null;
    public String defaultPicture = null;

    public static boolean isActive(String calendarId) {
        return count("calendarId = ?1 and active = false", calendarId) == 0;
    }

    public static Optional<CalendarConfiguration> findByCalendarId(String calendarId) {
        return find("calendarId", calendarId).firstResultOptional();
    }

}
