package com.parking.api.repository;

import com.parking.api.model.ParkingLot;
import com.parking.api.model.ParkingSpot;
import com.parking.api.model.Reservation;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Repository
public class ParkingRepository {

    private final Map<Integer, ParkingLot> parkingLots = new ConcurrentHashMap<>();
    private final Map<Integer, ParkingSpot> parkingSpots = new ConcurrentHashMap<>();
    private final Map<Long, Reservation> reservations = new ConcurrentHashMap<>();
    private long reservationIdCounter = 1;
  

    public ParkingRepository() {
        initializeSampleData();
    }

    public List<ParkingLot> getParkingLots() {
        return new ArrayList<>(parkingLots.values());
    }

    public ParkingLot getParkingLot(int id) {
        return parkingLots.get(id);
    }

    public List<ParkingSpot> getParkingSpots() {
        return new ArrayList<>(parkingSpots.values());
    }

    public ParkingSpot getParkingSpot(int id) {
        return parkingSpots.get(id);
    }

    public Reservation getReservation(Long id) {
        return reservations.get(id);
    }

    public void saveOrUpdateReservation(Reservation reservation) {
        reservations.put(reservation.getId(), reservation);
    }
    public void saveOrUpdateParkingSpot(ParkingSpot parkingSpot) {
        parkingSpots.put(parkingSpot.getId(), parkingSpot);
    }


    public void checkInReservation(long reservationId) {
        Reservation reservation = reservations.get(reservationId);
      
        if (reservation != null && !reservation.isCheckedIn()) {
            reservation.setCheckedIn(true);
            ParkingSpot spot = getParkingSpot(reservation.getSpotId());
            if (spot != null) {
                spot.setStatus("occupied");
            }
        }else {
            throw new RuntimeException("Reservation Id "+reservationId+" is not available");
        }

    }

    // Direct check-in without reservation
    public void checkInSpot(int spotId) {
        ParkingSpot spot = getParkingSpot(spotId);
      
        if (spot != null && spot.getStatus().equals("available")) {
            spot.setStatus("occupied");
        }else{
            throw new RuntimeException("SpotId "+spotId+" is not available");
        }
    }

    public long getNextReservationId() {
        return reservationIdCounter++;
    }

    private void initializeSampleData() {
        parkingLots.put(1, new ParkingLot(1, "The Gate Parking", "Glasgow", 100));
         for(int i=1; i<=100; i++) {
            parkingSpots.put(i, new ParkingSpot(i, 1, "A"+i, "available"));
        }
    }

}
