package com.steffeleffe.familycalendar.calendar;

import java.util.*;

public final class Day {
    private final int index;
    private final String name;

    Day(
            int index,
            String name
    ) {
        this.index = index;
        this.name = name;
    }

    public int index() {
        return index;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Day that = (Day) obj;
        return this.index == that.index &&
                Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, name);
    }

    @Override
    public String toString() {
        return "Day[" +
                "index=" + index + ", " +
                "name=" + name + ']';
    }

    private static final Locale locale = new Locale("da", "DK", "DK");

    public static List<Day> getFiveDays() {
        ArrayList<Day> days = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DAY_OF_YEAR, i);
            String dayDisplay = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale);
            days.add(new Day(i, dayDisplay));
        }
        return days;
    }
}

