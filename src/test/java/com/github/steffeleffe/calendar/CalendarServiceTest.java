package com.github.steffeleffe.calendar;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class CalendarServiceTest {

    @Test
    void getParticipants() {
        String description = "Blablabla\nHvem: Ada, Ebbe\nMore text to ignore";

        Set<Participant> participants = CalendarService.getParticipants(description);

        assertThat(participants).containsExactlyInAnyOrder(Participant.EBBE, Participant.ADA);
    }

    @Test
    void getParticipants_NoParticipantsInDescription() {
        String description = "Blablabla\nMore text to ignore";

        Set<Participant> participants = CalendarService.getParticipants(description);

        assertThat(participants).isEmpty();
    }

    @Test
    void getParticipants_NoDescription() {
        Set<Participant> participants = CalendarService.getParticipants(null);

        assertThat(participants).isEmpty();
    }

    @Test
    void getParticipants_AllKeyword() {
        String description = "Blablabla\nHvem: alle\nMore text to ignore";

        Set<Participant> participants = CalendarService.getParticipants(description);

        assertThat(participants).containsExactlyInAnyOrder(Participant.EBBE, Participant.ADA, Participant.RIKKE, Participant.MARIE, Participant.STEFFEN);
    }

    @Test
    @Disabled
    void getParticipants_RikkeWorkCalendar() {
        fail("Not implemented yet");
    }

    @Test
    void getImageUrl() {
        String description = "Blablabla\nBillede:http://example.com/picture.svg\nMore text to ignore";

        String imageUrl = CalendarService.getImageUrl(description);

        assertThat(imageUrl).isEqualTo("http://example.com/picture.svg");
    }

    @Test
    void getImageUrl_NoUrlInDescription() {
        String description = "Blablabla\nMore text to ignore";

        String imageUrl = CalendarService.getImageUrl(description);

        assertThat(imageUrl).isNull();
    }

    @Test
    void getImageUrl_NoDescription() {
        String imageUrl = CalendarService.getImageUrl(null);

        assertThat(imageUrl).isNull();
    }
}