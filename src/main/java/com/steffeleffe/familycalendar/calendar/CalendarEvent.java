package com.steffeleffe.familycalendar.calendar;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;

public class CalendarEvent {
    public final String id;
    public final String summary;
    public final String imageUrl;
    public final Instant startTime;
    public final Instant endTime;
    public final Set<Participant> participants;

    private CalendarEvent(String id,
                          String summary,
                          String imageUrl,
                          Instant startTime,
                          Instant endTime,
                          Set<Participant> participants) {
        this.id = id;
        this.summary = summary;
        this.imageUrl = imageUrl;
        this.startTime = startTime;
        this.endTime = endTime;
        this.participants = Collections.unmodifiableSet(participants);
    }

    public static class Builder {
        private String id;
        private String summary;
        private String imageUrl;
        private Instant startTime;
        private Instant endTime;
        private Set<Participant> participants = Collections.emptySet();

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setSummary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder setStartTime(Instant startTime) {
            this.startTime = startTime;
            return this;
        }

        public void setEndTime(Instant endTime) {
            this.endTime = endTime;
        }

        public Builder setParticipants(Set<Participant> participants) {
            this.participants = participants;
            return this;
        }

        public CalendarEvent build() {
            return new CalendarEvent(id, summary, imageUrl, startTime, endTime, participants);
        }


    }

}
