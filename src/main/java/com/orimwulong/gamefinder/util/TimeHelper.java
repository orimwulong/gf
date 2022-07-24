package com.orimwulong.gamefinder.util;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;

public final class TimeHelper {

    private TimeHelper() {
        throw new java.lang.UnsupportedOperationException("TimeHelper is a utility class and cannot be instantiated");
    }

    public static String getDurationText(long mins) {
        Duration d = Duration.ofMinutes(mins);
        long days = d.toDaysPart();
        long hours = d.toHoursPart();
        long minutes = d.toMinutesPart();

        return String.format("%d day(s) %d hour(s) %d minute(s) (from %d minutes)", days, hours, minutes, mins);
    }

    public static String getNowAsPattern(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        return sdf.format(Date.from(Instant.now()));
    }

}
