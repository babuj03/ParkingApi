package com.parking.api.unit.test;

import com.parking.api.model.*;
import com.parking.api.repository.ParkingRepository;
import com.parking.api.service.ParkingService;
import com.parking.api.util.ParkingUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void reserveParkingSpot_SuccessfulReservation() {
        int spotId = 1;
        ReservationRequest reservationRequest = new ReservationRequest(spotId);
        ParkingSpot spot =  ParkingSpot.builder().id(1).lotId(1).spotNumber("A1").status("Available").build();
        when(parkingRepository.getParkingSpot(spotId)).thenReturn(spot);
        when(parkingRepository.getNextReservationId()).thenReturn(1L);
        String result = parkingService.reserveParkingSpot(reservationRequest);
        assertEquals("Spot reserved successfully. Reservation ID: 1", result);
        verify(parkingRepository, times(1)).getParkingSpot(spotId);
        verify(parkingRepository, times(1)).getNextReservationId();
        verify(parkingRepository, times(1)).saveOrUpdateParkingSpot(spot);
        verify(parkingRepository, times(1)).saveOrUpdateReservation(any(Reservation.class));
    }

    @Test
    void reserveParkingSpot_UnavailableSpot() {
        int spotId = 1;
        ReservationRequest reservationRequest = new ReservationRequest(spotId);
        ParkingSpot spot =  ParkingSpot.builder().id(1).lotId(1).spotNumber("A1").status("reserved").build();
        when(parkingRepository.getParkingSpot(spotId)).thenReturn(spot); 
        assertThrows(RuntimeException.class, () -> parkingService.reserveParkingSpot(reservationRequest));
        verify(parkingRepository, times(1)).getParkingSpot(spotId);
        verify(parkingRepository, never()).getNextReservationId();
        verify(parkingRepository, never()).saveOrUpdateParkingSpot(any(ParkingSpot.class));
        verify(parkingRepository, never()).saveOrUpdateReservation(any(Reservation.class));
    }

    @Test
    void checkIn_SuccessfulCheckIn() {
        int spotId = 1;
        //CheckInRequest checkInRequest = new CheckInRequest(spotId);
        ParkingSpot spot = ParkingSpot.builder().id(1).lotId(1).spotNumber("A1").status("available").build();
        when(parkingRepository.getParkingSpot(spotId)).thenReturn(spot);
        String result = parkingService.checkInBySpotId(spotId);
        assertEquals("Check-in successful.", result);
        verify(parkingRepository, times(1)).getParkingSpot(spotId);
        verify(parkingRepository, times(1)).saveOrUpdateParkingSpot(spot);
        verify(parkingRepository, never()).getReservation(anyLong());
        verify(parkingRepository, never()).saveOrUpdateReservation(any(Reservation.class));
    }

    @Test
    void checkIn_SuccessfulCheckInWithReservation() {
        int spotId = 2;
        long reservationId = 1;
        ParkingSpot spot = ParkingSpot.builder().id(2).lotId(12).spotNumber("A2").status("available").build();
        Reservation reservation = new Reservation(reservationId, spotId, false);
        when(parkingRepository.getParkingSpot(spotId)).thenReturn(spot);
        when(parkingRepository.getReservation(reservationId)).thenReturn(reservation);
        String result = parkingService.checkInByReservationID(reservationId);
        assertEquals("Check-in successful.", result);
        verify(parkingRepository, times(1)).getParkingSpot(spotId);
        verify(parkingRepository, times(1)).getReservation(reservationId);
        verify(parkingRepository, times(1)).saveOrUpdateParkingSpot(spot);
    }

    @Test
    void checkIn_UnsuccessfulCheckIn() {
        int spotId = 1;
        long reservationId = 1L;
        ParkingSpot spot = ParkingSpot.builder().id(1).lotId(1).spotNumber("A1").status("occupied").build();
        Reservation reservation = new Reservation(reservationId, spotId, true);
        when(parkingRepository.getParkingSpot(spotId)).thenReturn(spot);
        when(parkingRepository.getReservation(reservationId)).thenReturn(reservation); 
        assertThrows(RuntimeException.class, () -> parkingService.checkInBySpotId(spotId));
        verify(parkingRepository, times(1)).getParkingSpot(spotId);
        verify(parkingRepository, never()).saveOrUpdateParkingSpot(any(ParkingSpot.class));
        verify(parkingRepository, never()).saveOrUpdateReservation(any(Reservation.class));
    }

    @Test
    void checkOut_SuccessfulCheckOut() {
        int spotId = 1;
        long reservationId = 1L;
        CheckOutRequest checkOutRequest = CheckOutRequest.builder().reservationId(reservationId).spotId(spotId).build();
        ParkingSpot spot =  ParkingSpot.builder().id(1).lotId(1).spotNumber("A1").status("occupied").build();
        Reservation reservation = new Reservation(reservationId, spotId, true);
        LocalDateTime checkInTime = LocalDateTime.now().minusHours(2);
        LocalDateTime checkOutTime = LocalDateTime.now();
        spot.setCheckInTime(checkInTime);
        spot.setCheckOutTime(checkOutTime);
        when(parkingRepository.getParkingSpot(spotId)).thenReturn(spot);
        when(parkingRepository.getReservation(reservationId)).thenReturn(reservation);
        String result = parkingService.checkOut(checkOutRequest);
        assertEquals("You have to pay amount $4", result);
    }

    @Test
    void checkOut_UnsuccessfulCheckOut() {
        int spotId = 1;
        long reservationId = 1L;
        CheckOutRequest checkOutRequest =  CheckOutRequest.builder().reservationId(reservationId).spotId(spotId).build();
        ParkingSpot spot = ParkingSpot.builder().id(1).lotId(1).spotNumber("A1").status("occupied").build();
        Reservation reservation = new Reservation(reservationId, spotId, false);
        when(parkingRepository.getParkingSpot(spotId)).thenReturn(spot);
        when(parkingRepository.getReservation(reservationId)).thenReturn(reservation);
        assertThrows(RuntimeException.class, () -> parkingService.checkOut(checkOutRequest));
     }
}
