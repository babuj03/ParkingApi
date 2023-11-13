package com.parking.api.unit.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parking.api.controller.ParkingController;
import com.parking.api.model.*;
import com.parking.api.service.ParkingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ParkingControllerTest {

    @Mock
    private ParkingService parkingService;

    @InjectMocks
    private ParkingController parkingController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getParkingLots() {
        List<ParkingLot> parkingLots = new ArrayList<>();
        when(parkingService.getParkingLots()).thenReturn(parkingLots);
        List<ParkingLot> result = parkingController.getParkingLots();
        assertEquals(parkingLots, result);
    }

    @Test
    void getParkingLot_Exists() {
        int lotId = 1;
        ParkingLot parkingLot = new ParkingLot(lotId, "sample","Sample location",100);
        when(parkingService.getParkingLot(lotId)).thenReturn(parkingLot);
        ResponseEntity<?> response = parkingController.getParkingLot(lotId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(parkingLot, response.getBody());
    }

    @Test
    void getParkingLot_NotExists() {
        int lotId = 1;
        when(parkingService.getParkingLot(lotId)).thenReturn(null);
        ResponseEntity<?> response = parkingController.getParkingLot(lotId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Parking lot not found", response.getBody());
    }

    @Test
    void getParkingSpots() {
        List<ParkingSpot> parkingSpots = new ArrayList<>();
        when(parkingService.getParkingSpots()).thenReturn(parkingSpots);
        List<ParkingSpot> result = parkingController.getParkingSpots();
        assertEquals(parkingSpots, result);
    }

    @Test
    void getParkingSpot_Exists() {
        int spotId = 1;
        ParkingSpot parkingSpot = new ParkingSpot(spotId, 1, "1A","available");
        when(parkingService.getParkingSpot(spotId)).thenReturn(parkingSpot);
        ResponseEntity<?> response = parkingController.getParkingSpot(spotId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(parkingSpot, response.getBody());
    }

    @Test
    void getParkingSpot_NotExists() {
        int spotId = 1;
        when(parkingService.getParkingSpot(spotId)).thenReturn(null);
        ResponseEntity<?> response = parkingController.getParkingSpot(spotId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Parking spot not found", response.getBody());
    }

    @Test
    void reserveParkingSpot_Success() throws Exception {
        ReservationRequest reservationRequest = new ReservationRequest(1);
        String expectedResult = "Spot reserved successfully. Reservation ID: 1";
        when(parkingService.reserveParkingSpot(reservationRequest)).thenReturn(expectedResult);
        String requestBody = objectMapper.writeValueAsString(reservationRequest);
        ResponseEntity<?> response = parkingController.reserveParkingSpot(reservationRequest);
        assertTrue(Objects.requireNonNull(response.getBody()).toString().contains(expectedResult));
        verify(parkingService, times(1)).reserveParkingSpot(reservationRequest);
    }

    @Test
    void checkInBySpotIdOrReservationID() throws Exception {
        CheckInRequest checkInRequest = new CheckInRequest(1L, 1);
        String expectedResult ="Check-in successful.";
        when(parkingService.checkIn(checkInRequest)).thenReturn(expectedResult);
        String requestBody = objectMapper.writeValueAsString(checkInRequest);
        ResponseEntity<?> response = parkingController.checkInBySpotIdOrReservationID(checkInRequest);
       // assertEquals(expectedResult, response.getBody());
        assertTrue(Objects.requireNonNull(response.getBody()).toString().contains(expectedResult));
        verify(parkingService, times(1)).checkIn(checkInRequest);
    }

    @Test
    void checkOut() throws Exception {
        CheckOutRequest checkOutRequest = new CheckOutRequest(1L, 1);
        String expectedResult = "Check-out successful.";
        
        when(parkingService.checkOut(checkOutRequest)).thenReturn(expectedResult);
        String requestBody = objectMapper.writeValueAsString(checkOutRequest);
        ResponseEntity<?> response = parkingController.checkOut(checkOutRequest);
        assertEquals(expectedResult, response.getBody());
        verify(parkingService, times(1)).checkOut(checkOutRequest);
    }
}
