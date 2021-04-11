package com.github.steffeleffe.calendar;

import java.util.*;

public class Day {
    public int index;
    public String name;

    private Day(int index, String name) {
        this.index = index;
        this.name = name;
    }

    private static Locale locale = new Locale("da", "DK", "DK");

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
