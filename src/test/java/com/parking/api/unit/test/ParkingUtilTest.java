package com.parking.api.unit.test;

import com.parking.api.util.ParkingUtil;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParkingUtilTest {

    @Test
    public void testRoundUpToNearestHour() {
        LocalDateTime checkInTime = LocalDateTime.of(2023, 1, 1, 10, 30);
        LocalDateTime checkOutTime = LocalDateTime.of(2023, 1, 1, 13, 45);

        long roundedHours = ParkingUtil.roundUpToNearestHour(checkInTime, checkOutTime);

        assertEquals(4, roundedHours);
    }

    @Test
    public void testRound_10_Minutes() {
        LocalDateTime checkInTime = LocalDateTime.of(2023, 1, 1, 10, 30);
        LocalDateTime checkOutTime = LocalDateTime.of(2023, 1, 1, 10, 40);

        long roundedHours = ParkingUtil.roundUpToNearestHour(checkInTime, checkOutTime);

        assertEquals(1, roundedHours);
    }

    @Test
    public void testRound_45_Minutes() {
        LocalDateTime checkInTime = LocalDateTime.of(2023, 1, 1, 10, 10);
        LocalDateTime checkOutTime = LocalDateTime.of(2023, 1, 1, 10, 45);

        long roundedHours = ParkingUtil.roundUpToNearestHour(checkInTime, checkOutTime);

        assertEquals(1, roundedHours);
    }

    @Test
    public void testRound_1_Hour_0_Minutes() {
        LocalDateTime checkInTime = LocalDateTime.of(2023, 1, 1, 10, 10);
        LocalDateTime checkOutTime = LocalDateTime.of(2023, 1, 1, 10, 10);

        long roundedHours = ParkingUtil.roundUpToNearestHour(checkInTime, checkOutTime);

        assertEquals(0, roundedHours);
    }

}

