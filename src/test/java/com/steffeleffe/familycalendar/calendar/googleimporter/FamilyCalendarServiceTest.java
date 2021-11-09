package com.steffeleffe.familycalendar.calendar.googleimporter;

import com.steffeleffe.familycalendar.calendar.CalendarService;
import com.steffeleffe.familycalendar.calendar.Participant;
import com.steffeleffe.familycalendar.calendar.googleimporter.GoogleCalendarImporter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class FamilyCalendarServiceTest {

    GoogleCalendarImporter importer = new GoogleCalendarImporter();

    @Test
    void getParticipants() {
        String description = "Blablabla\nHvem: Ada, Ebbe\nMore text to ignore";
        String calendarId = "1234";

        Set<Participant> participants = importer.getParticipants(description, calendarId);

        Assertions.assertThat(participants).containsExactlyInAnyOrder(Participant.EBBE, Participant.ADA);
    }

    @Test
    void getParticipants_NoParticipantsInDescription() {
        String description = "Blablabla\nMore text to ignore";
        String calendarId = "1234";

        Set<Participant> participants = importer.getParticipants(description, calendarId);

        Assertions.assertThat(participants).isEmpty();
    }

    @Test
    void getParticipants_NoDescription() {
        String calendarId = "1234";

        Set<Participant> participants = importer.getParticipants(null, calendarId);

        Assertions.assertThat(participants).isEmpty();
    }

    @Test
    void getParticipants_AllKeyword() {
        String description = "Blablabla\nHvem: alle\nMore text to ignore";
        String calendarId = "1234";

        Set<Participant> participants = importer.getParticipants(description, calendarId);

        Assertions.assertThat(participants).containsExactlyInAnyOrder(Participant.EBBE, Participant.ADA, Participant.RIKKE, Participant.MARIE, Participant.STEFFEN);
    }

    @Test
    void getParticipants_RikkeWorkCalendar() {
        String calendarId = "66aglhcacpcpupnhh9fian0a1g@group.calendar.google.com";

        Set<Participant> participants = importer.getParticipants(null, calendarId);

        Assertions.assertThat(participants).containsExactlyInAnyOrder(Participant.RIKKE);
    }

    @Test
    void getImageUrl() {
        String description = "Blablabla\nBillede: http://example.com/picture.svg\nMore text to ignore";
        String calendarId = "1234";

        URI imageUrl = importer.getImageUrl(description, calendarId);

        assertThat(imageUrl).isEqualTo(URI.create("http://example.com/picture.svg"));
    }

    @Test
    void getImageUrl_RikkeWorkCalendar() {
        String calendarId = "66aglhcacpcpupnhh9fian0a1g@group.calendar.google.com";

        URI imageUrl = importer.getImageUrl(null, calendarId);

        assertThat(imageUrl).isEqualTo(URI.create("https://www.flaticon.com/svg/static/icons/svg/3209/3209008.svg"));
    }

    @Test
    void getImageUrlAsAnchorText() {
        String description = "Billede:<a href=\"https://www.flaticon.com/free-icon/shower_1752086\" id=\"ow437\" __is_owner=\"true\">https://image.flaticon.com/icons/png/512/1752/1752086.png</a><br>Hvem: Ada, Ebbe, Marie";
        String calendarId = "1234";

        URI imageUrl = importer.getImageUrl(description, calendarId);

        assertThat(imageUrl).isEqualTo(URI.create("https://image.flaticon.com/icons/png/512/1752/1752086.png"));
    }

    @Test
    void getImageUrl_NoUrlInDescription() {
        String description = "Blablabla\nMore text to ignore";
        String calendarId = "1234";

        URI imageUrl = importer.getImageUrl(description, calendarId);

        assertThat(imageUrl).isNull();
    }

    @Test
    void getImageUrl_NoDescription() {
        String calendarId = "1234";

        URI imageUrl = importer.getImageUrl(null, calendarId);

        assertThat(imageUrl).isNull();
    }
}