package com.parking.api.unit.test;

import com.parking.api.controller.ParkingController;
import com.parking.api.model.*;
import com.parking.api.service.ParkingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ParkingControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(ParkingControllerTest.class);

    @Mock
    private ParkingService parkingService;

    @InjectMocks
    private ParkingController parkingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetParkingLots() {
        logger.info("Testing getParkingLots method");
        List<ParkingLot> parkingLots = new ArrayList<>();
        when(parkingService.getParkingLots()).thenReturn(parkingLots);

        List<ParkingLot> result = parkingController.getParkingLots();

        assertEquals(parkingLots, result);
        logger.info("Test successful");
    }

    @Test
    void testGetParkingLotNotFound() {
        logger.info("Testing getParkingLotNotFound method");
        int parkingLotId = 1;
        when(parkingService.getParkingLot(parkingLotId)).thenReturn(null);

        ResponseEntity<?> responseEntity = parkingController.getParkingLot(parkingLotId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Parking lot not found", responseEntity.getBody());
        logger.info("Test successful");
    }

    @Test
    void testGetParkingLotFound() {
        logger.info("Testing getParkingLotFound method");
        int parkingLotId = 1;
        ParkingLot parkingLot = new ParkingLot();
        when(parkingService.getParkingLot(parkingLotId)).thenReturn(parkingLot);

        ResponseEntity<?> responseEntity = parkingController.getParkingLot(parkingLotId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(parkingLot, responseEntity.getBody());
        logger.info("Test successful");
    }

    @Test
    void testGetParkingSpots() {
        logger.info("Testing getParkingSpots method");
        List<ParkingSpot> parkingSpots = new ArrayList<>();
        when(parkingService.getParkingSpots()).thenReturn(parkingSpots);

        List<ParkingSpot> result = parkingController.getParkingSpots();

        assertEquals(parkingSpots, result);
        logger.info("Test successful");
    }

    @Test
    void testGetParkingSpotNotFound() {
        logger.info("Testing getParkingSpotNotFound method");
        int parkingSpotId = 1;
        when(parkingService.getParkingSpot(parkingSpotId)).thenReturn(null);

        ResponseEntity<?> responseEntity = parkingController.getParkingSpot(parkingSpotId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Parking spot not found", responseEntity.getBody());
        logger.info("Test successful");
    }

    @Test
    void testGetParkingSpotFound() {
        logger.info("Testing getParkingSpotFound method");
        int parkingSpotId = 1;
        ParkingSpot parkingSpot = new ParkingSpot();
        when(parkingService.getParkingSpot(parkingSpotId)).thenReturn(parkingSpot);

        ResponseEntity<?> responseEntity = parkingController.getParkingSpot(parkingSpotId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(parkingSpot, responseEntity.getBody());
        logger.info("Test successful");
    }

    @Test
    void testGetAvailableParkingSpots() {
        logger.info("Testing getAvailableParkingSpots method");
        List<ParkingSpot> parkingSpots = new ArrayList<>();
        when(parkingService.getAvailableParkingSpots()).thenReturn(parkingSpots);

        List<ParkingSpot> result = parkingController.getAvailableParkingSpots();

        assertEquals(parkingSpots, result);
        logger.info("Test successful");
    }

    @Test
    void testReserveParkingSpot() {
        logger.info("Testing reserveParkingSpot method");
        ReservationRequest request = new ReservationRequest();
        when(parkingService.reserveParkingSpot(request)).thenReturn("Reservation successful");

        ResponseEntity<?> responseEntity = parkingController.reserveParkingSpot(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Reservation successful", responseEntity.getBody());
        logger.info("Test successful");
    }

    @Test
    void testCheckInBySpotId() {
        logger.info("Testing checkInBySpotId method");
        CheckInRequest request = new CheckInRequest();
        when(parkingService.checkInBySpotId(anyInt())).thenReturn("Check-in successful");

        ResponseEntity<?> responseEntity = parkingController.checkInBySpotId(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Check-in successful", responseEntity.getBody());
        logger.info("Test successful");
    }

    @Test
    void testCheckInByReservationID() {
        logger.info("Testing checkInByReservationID method");
        CheckInRequest request = new CheckInRequest();
        when(parkingService.checkInByReservationID(anyLong())).thenReturn("Check-in successful");

        ResponseEntity<?> responseEntity = parkingController.checkInByReservationId(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Check-in successful", responseEntity.getBody());
        logger.info("Test successful");
    }

    @Test
    void testCheckOut() {
        logger.info("Testing checkOut method");
        CheckOutRequest request = new CheckOutRequest();
        when(parkingService.checkOut(request)).thenReturn("Check-out successful");

        ResponseEntity<?> responseEntity = parkingController.checkOut(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Check-out successful", responseEntity.getBody());
        logger.info("Test successful");
    }

}
