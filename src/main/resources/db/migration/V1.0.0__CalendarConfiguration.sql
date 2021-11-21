CREATE SEQUENCE HIBERNATE_SEQUENCE START WITH 1;

CREATE CACHED TABLE CALENDARCONFIGURATION(
    ID BIGINT NOT NULL UNIQUE,
    ACTIVE BOOLEAN NOT NULL,
    CALENDARID VARCHAR(255),
    DEFAULTPARTICIPANT VARCHAR(255),
    DEFAULTPICTURE VARCHAR(255)
);

INSERT INTO CalendarConfiguration(id, calendarId, active, defaultParticipant, defaultPicture)
VALUES (HIBERNATE_SEQUENCE.NEXTVAL, 'steffeleffe@gmail.com', true, 'STEFFEN', null);

INSERT INTO CalendarConfiguration(id, calendarId, active, defaultParticipant, defaultPicture)
VALUES (1002, '66aglhcacpcpupnhh9fian0a1g@group.calendar.google.com', true, 'RIKKE', 'https://cdn-icons-png.flaticon.com/512/1165/1165602.png');

INSERT INTO CalendarConfiguration(id, calendarId, active, defaultParticipant, defaultPicture)
VALUES (1003, '8irm2mumdjrheha0sf3tnf4h7ve1fsm3@import.calendar.google.com', true, 'STEFFEN', 'https://cdn2.iconfinder.com/data/icons/transportation-vol-1-17/512/formula_1_car-512.png');