package com.parking.api.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ParkingUtil {
    public static int FEE_PER_HOUR = 2;
    public static long roundUpToNearestHour(LocalDateTime checkInTime, LocalDateTime checkOutTime) {
        long hours = checkInTime.until(checkOutTime, ChronoUnit.HOURS);
        long remainder = checkInTime.until(checkOutTime, ChronoUnit.MINUTES) % 60;
        // If there is a remainder, round up to the next hour
        if (remainder > 0) {
            hours++;
        }
        return hours;
    }
}
