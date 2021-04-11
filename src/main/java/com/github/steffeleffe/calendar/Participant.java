package com.github.steffeleffe.calendar;

public enum Participant {
    STEFFEN("Steffen", "S", "cornflowerblue"),
    MARIE("Marie", "M", "palevioletred"),
    ADA("Ada", "A", "hotpink"),
    RIKKE("Rikke", "R", "darkseagreen"),
    EBBE("Ebbe", "E", "sandybrown"),
    ;

    public final String id;
    public final String abbreviation;
    public final String color;

    Participant(String name, String abbreviation, String color) {
        this.id = name;
        this.abbreviation = abbreviation;
        this.color = color;
    }
}
