package com.parking.api.unit.test;

import com.parking.api.model.*;
import com.parking.api.repository.ParkingRepository;
import com.parking.api.service.ParkingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParkingServiceTest {

    @Mock
    private ParkingRepository parkingRepository;

    @InjectMocks
    private ParkingService parkingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void reserveParkingSpot_Success() {
        // Arrange
        ReservationRequest reservationRequest = new ReservationRequest(1);
        ParkingSpot availableSpot = new ParkingSpot(1, 1, "2B","available");
        when(parkingRepository.getParkingSpot(reservationRequest.getSpotId())).thenReturn(availableSpot);
        when(parkingRepository.getNextReservationId()).thenReturn(1L);
        String response = parkingService.reserveParkingSpot(reservationRequest);
        assertEquals("Spot reserved successfully. Reservation ID: 1", response);
        assertEquals("reserved", availableSpot.getStatus());
        verify(parkingRepository, times(1)).saveOrUpdateParkingSpot(availableSpot);
        verify(parkingRepository, times(1)).saveOrUpdateReservation(any(Reservation.class));
    }

    @Test
    void reserveParkingSpot_Failure_UnavailableSpot() {
        // Arrange
        ReservationRequest reservationRequest = new ReservationRequest(1);
        ParkingSpot occupiedSpot = new ParkingSpot(1,1, "1D", "occupied");
        when(parkingRepository.getParkingSpot(reservationRequest.getSpotId())).thenReturn(occupiedSpot);
        //String response = parkingService.reserveParkingSpot(reservationRequest);
        assertThrows(RuntimeException.class, () -> parkingService.reserveParkingSpot(reservationRequest));
        verify(parkingRepository, never()).saveOrUpdateParkingSpot(any(ParkingSpot.class));
        verify(parkingRepository, never()).saveOrUpdateReservation(any(Reservation.class));
    }

    @Test
    void checkIn_Success_WithReservation() {
        // Arrange
        CheckInRequest checkInRequest = new CheckInRequest(1L, 1);
        Reservation reservation = new Reservation(1L, 1, false);
        ParkingSpot availableSpot = new ParkingSpot(1,1, "2C", "available");
        when(parkingRepository.getReservation(checkInRequest.getReservationId())).thenReturn(reservation);
        when(parkingRepository.getParkingSpot(checkInRequest.getSpotId())).thenReturn(availableSpot);
        String response = parkingService.checkIn(checkInRequest);
        assertEquals("Check-in successful.", response);
        assertEquals("occupied", availableSpot.getStatus());
        assertTrue(reservation.isCheckedIn());
        verify(parkingRepository, times(1)).saveOrUpdateParkingSpot(availableSpot);
        verify(parkingRepository, times(1)).saveOrUpdateReservation(reservation);
    }

    @Test
    void checkIn_Success_WithoutReservation() {
        // Arrange
        CheckInRequest checkInRequest = new CheckInRequest(0, 1);
        ParkingSpot availableSpot = new ParkingSpot(1,1, "2B", "available");
        when(parkingRepository.getParkingSpot(checkInRequest.getSpotId())).thenReturn(availableSpot);
        String response = parkingService.checkIn(checkInRequest);
        assertEquals("Check-in successful.", response);
        assertEquals("occupied", availableSpot.getStatus());
        verify(parkingRepository, times(1)).saveOrUpdateParkingSpot(availableSpot);
        verify(parkingRepository, never()).saveOrUpdateReservation(any(Reservation.class));
    }

    @Test
    void checkIn_Failure_InvalidReservation() {
        // Arrange
        CheckInRequest checkInRequest = new CheckInRequest(0, 0);
        Reservation invalidReservation = new Reservation(1L, 1, true);
        ParkingSpot availableSpot = new ParkingSpot(1,1, "1B", "reserved");
        when(parkingRepository.getReservation(checkInRequest.getReservationId())).thenReturn(invalidReservation);
        when(parkingRepository.getParkingSpot(checkInRequest.getSpotId())).thenReturn(availableSpot);
        assertThrows(RuntimeException.class, () -> parkingService.checkIn(checkInRequest));
    }

    @Test
    void checkIn_Failure_SpotNotAvailable() {
        // Arrange
        CheckInRequest checkInRequest = new CheckInRequest(0, 1);
        ParkingSpot occupiedSpot = new ParkingSpot(1, 1, "1A", "occupied");
        when(parkingRepository.getParkingSpot(checkInRequest.getSpotId())).thenReturn(occupiedSpot);
        assertThrows(RuntimeException.class, () -> parkingService.checkIn(checkInRequest));
    }
}
   
