package com.steffeleffe.familycalendar.calendar;

import java.io.IOException;

public class CalendarImportException extends Exception {
    public CalendarImportException(String s, Throwable e) {
        super(s, e);
    }
}
