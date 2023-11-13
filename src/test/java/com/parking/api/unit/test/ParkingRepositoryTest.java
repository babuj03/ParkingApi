package com.parking.api.unit.test;

import com.parking.api.model.ParkingLot;
import com.parking.api.model.ParkingSpot;
import com.parking.api.model.Reservation;
import com.parking.api.repository.ParkingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class ParkingRepositoryTest {

    @InjectMocks
    private ParkingRepository parkingRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getParkingLots() {
        List<ParkingLot> result = parkingRepository.getParkingLots();
        assertEquals(1, result.size());
        assertEquals("The Gate Parking", result.get(0).getName());
    }

    @Test
    void getParkingLot_Exists() {
        ParkingLot result = parkingRepository.getParkingLot(1);
        assertEquals("The Gate Parking", result.getName());
    }

    @Test
    void getParkingLot_NotExists() {
        ParkingLot result = parkingRepository.getParkingLot(2);
        assertNull(result);
    }

    @Test
    void getParkingSpots() {
        List<ParkingSpot> result = parkingRepository.getParkingSpots();
        assertEquals(5, result.size());
        assertEquals("A1", result.get(0).getSpotNumber());
    }

    @Test
    void getParkingSpot_Exists() {
        ParkingSpot result = parkingRepository.getParkingSpot(1);
        assertEquals("A1", result.getSpotNumber());
    }

    @Test
    void getParkingSpot_NotExists() {
        ParkingSpot result = parkingRepository.getParkingSpot(6);
        assertNull(result);
    }

    @Test
    void getReservation_Exists() {
        Reservation reservation = new Reservation(1L, 1, false);
        parkingRepository.saveOrUpdateReservation(reservation);
        Reservation result = parkingRepository.getReservation(1L);
        assertEquals(reservation, result);
    }

    @Test
    void getReservation_NotExists() {
        Reservation result = parkingRepository.getReservation(2L);
        assertNull(result);
    }

    @Test
    void saveOrUpdateReservation() {
        Reservation reservation = new Reservation(1L, 1, false);
        parkingRepository.saveOrUpdateReservation(reservation);
        Reservation result = parkingRepository.getReservation(1L);
        assertEquals(reservation, result);
    }

    @Test
    void saveOrUpdateParkingSpot() {
        ParkingSpot parkingSpot = new ParkingSpot(6, 1, "A6", "available");
        parkingRepository.saveOrUpdateParkingSpot(parkingSpot);
        ParkingSpot result = parkingRepository.getParkingSpot(6);
        assertEquals(parkingSpot, result);
    }

    @Test
    void checkInReservation() {
        Reservation reservation = new Reservation(1L, 1, false);
        parkingRepository.saveOrUpdateReservation(reservation);
        parkingRepository.checkInReservation(1L);
        ParkingSpot spot = parkingRepository.getParkingSpot(1);
        assertEquals("occupied", spot.getStatus());
    }

    @Test
    void checkInSpot() {
        ParkingSpot parkingSpot = new ParkingSpot(6, 1, "A6", "available");
        parkingRepository.saveOrUpdateParkingSpot(parkingSpot);
        parkingRepository.checkInSpot(6);
        ParkingSpot result = parkingRepository.getParkingSpot(6);
        assertEquals("occupied", result.getStatus());
    }

    @Test
    void getNextReservationId() {
        long result1 = parkingRepository.getNextReservationId();
        long result2 = parkingRepository.getNextReservationId();
        assertEquals(1, result1);
        assertEquals(2, result2);
    }
}
