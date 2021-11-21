package com.steffeleffe.familycalendar.calendar;

import java.util.Objects;

public final class FamilyCalendar {
    private final String id;
    private final String description;

    public FamilyCalendar(
            String id,
            String description) {
        this.id = id;
        this.description = description;
    }

    public String id() {
        return id;
    }

    public String description() {
        return description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        FamilyCalendar that = (FamilyCalendar) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description);
    }

    @Override
    public String toString() {
        return "FamilyCalendar[" +
                "id=" + id + ", " +
                "description=" + description + ']';
    }

}