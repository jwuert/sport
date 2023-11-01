package org.wuerthner.sport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SimpleDate extends Date {
    public final static String DATE_FORMAT = "yyyy-MM-dd";
    private static final long serialVersionUID = 1L;

    /**
     * Creates a SimpleDate as of now
     */
    public SimpleDate() {
        truncateTime(System.currentTimeMillis());
    }

    /**
     * Creates a SimpleDate as of now plus the specified offset in days
     *
     * @param offset
     */
    public SimpleDate(long offset) {
        truncateTime(System.currentTimeMillis() + offset * 24 * 3600 * 1000);
    }

    /**
     * Creates a SimpleDate from a given Date
     *
     * @param date
     */
    public SimpleDate(Date date) {
        truncateTime(date.getTime());
    }

    /**
     * Creates a SimpleDate from a given String with the format: dd MMM yyyy
     *
     * @param stringValue
     * @throws ParseException
     */
    public SimpleDate(String stringValue) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            truncateTime(sdf.parse(stringValue).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void truncateTime(long millis) {
        millis = millis - (millis % (24 * 3600000));
        this.setTime(millis);
    }

    @Override
    public String toString() {
        return new SimpleDateFormat(DATE_FORMAT).format(this);
    }
}