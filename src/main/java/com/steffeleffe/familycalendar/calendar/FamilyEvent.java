package com.steffeleffe.familycalendar.calendar;

import java.net.URI;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

public final class FamilyEvent {
    public final String id;
    public final String summary;
    public final URI imageUrl;
    public final Instant startTime;
    public final Instant endTime;
    public final Set<Participant> participants;
    public final String calendarId;

    public FamilyEvent(
            String id,
            String summary,
            URI imageUrl,
            Instant startTime,
            Instant endTime,
            Set<Participant> participants,
            String calendarId
    ) {
        this.id = id;
        this.summary = summary;
        this.imageUrl = imageUrl;
        this.startTime = startTime;
        this.endTime = endTime;
        this.participants = participants;
        this.calendarId = calendarId;
    }

    public String id() {
        return id;
    }

    public String summary() {
        return summary;
    }

    public URI imageUrl() {
        return imageUrl;
    }

    public Instant startTime() {
        return startTime;
    }

    public Instant endTime() {
        return endTime;
    }

    public Set<Participant> participants() {
        return participants;
    }

    public String calendarId() {
        return calendarId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        FamilyEvent that = (FamilyEvent) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.summary, that.summary) &&
                Objects.equals(this.imageUrl, that.imageUrl) &&
                Objects.equals(this.startTime, that.startTime) &&
                Objects.equals(this.endTime, that.endTime) &&
                Objects.equals(this.participants, that.participants) &&
                Objects.equals(this.calendarId, that.calendarId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, summary, imageUrl, startTime, endTime, participants, calendarId);
    }

    @Override
    public String toString() {
        return "FamilyEvent[" +
                "id=" + id + ", " +
                "summary=" + summary + ", " +
                "imageUrl=" + imageUrl + ", " +
                "startTime=" + startTime + ", " +
                "endTime=" + endTime + ", " +
                "participants=" + participants + ", " +
                "calendarId=" + calendarId + ']';
    }

}

