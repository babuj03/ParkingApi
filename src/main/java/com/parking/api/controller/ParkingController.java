package com.parking.api.controller;

import com.parking.api.model.*;
import com.parking.api.service.ParkingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

   ParkingService parkingService;

   public ParkingController(ParkingService parkingService)
   {
       this.parkingService =parkingService;
   }

    @GetMapping("/lots")
    public List<ParkingLot> getParkingLots() {
        return new ArrayList<>(parkingService.getParkingLots());
    }

    @GetMapping("/lots/{id}")
    public ResponseEntity<?> getParkingLot(@PathVariable int id) {
        ParkingLot parkingLot = parkingService.getParkingLot(id);
        if (parkingLot != null) {
            return new ResponseEntity<>(parkingLot, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Parking lot not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/spots")
    public List<ParkingSpot> getParkingSpots() {
        return new ArrayList<>(parkingService.getParkingSpots());
    }

    @GetMapping("/spots/{id}")
    public ResponseEntity<?> getParkingSpot(@PathVariable int id) {
        ParkingSpot parkingSpot = parkingService.getParkingSpot(id);
        if (parkingSpot != null) {
            return new ResponseEntity<>(parkingSpot, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Parking spot not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/available/spots")
    public List<ParkingSpot> getAvailableParkingSpots() {
        return new ArrayList<>(parkingService.getAvailableParkingSpots());
    }

    @PostMapping("/reserve")
    public ResponseEntity<?> reserveParkingSpot(@RequestBody ReservationRequest request) {
        return new ResponseEntity<String>(parkingService.reserveParkingSpot(request),HttpStatus.OK);
    }

    @PostMapping("/check-in")
    public ResponseEntity<?> checkInBySpotIdOrReservationID(@RequestBody CheckInRequest request) {
       return new ResponseEntity<String>(parkingService.checkIn(request), HttpStatus.OK);
    }

    @PostMapping("/check-out")
    public ResponseEntity<?> checkOut(@RequestBody CheckOutRequest request) {
       return new ResponseEntity<String>(parkingService.checkOut(request),HttpStatus.OK);
    }
}
