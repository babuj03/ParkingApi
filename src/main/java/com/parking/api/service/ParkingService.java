package com.parking.api.service;

import com.parking.api.model.CheckInRequest;
import com.parking.api.model.CheckOutRequest;
import com.parking.api.model.ParkingLot;
import com.parking.api.model.ParkingSpot;
import com.parking.api.model.Reservation;
import com.parking.api.model.ReservationRequest;
import com.parking.api.repository.ParkingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class ParkingService {

    private final ParkingRepository parkingRepository;
    ReentrantLock lock = new ReentrantLock();

    public ParkingService(ParkingRepository parkingRepository) {
        this.parkingRepository = parkingRepository;
    }

    public List<ParkingLot> getParkingLots() {
        return parkingRepository.getParkingLots();
    }

    public ParkingLot getParkingLot(int id) {
        return parkingRepository.getParkingLot(id);
    }

    public List<ParkingSpot> getParkingSpots() {
        return parkingRepository.getParkingSpots();
    }

    public ParkingSpot getParkingSpot(int id) {
        return parkingRepository.getParkingSpot(id);
    }

    //@Transactional
    public String reserveParkingSpot(ReservationRequest request) {
        lock.lock();
        ParkingSpot spot = parkingRepository.getParkingSpot(request.getSpotId());
        if (canReserveSpot(spot)) {
            Reservation reservation = createReservation(spot);
            updateSpotAndReservation(spot, reservation);
            lock.unlock();
            return "Spot reserved successfully. Reservation ID: " + reservation.getId();
        } else {
            lock.unlock();
            throw new RuntimeException("Unable to reserve the spot, Try again later.");
        }
    }

    //@Transactional
    public String checkIn(CheckInRequest request) {
        lock.lock();
        ParkingSpot spot = parkingRepository.getParkingSpot(request.getSpotId());
        Reservation reservation = (request.getReservationId() > 0) ? parkingRepository.getReservation(request.getReservationId()) : null;
        if (canCheckIn(reservation, spot)) {
            updateSpotAndReservationOnCheckIn(spot, reservation);
            lock.unlock();
            return"Check-in successful.";
        } else {
            lock.unlock();
            throw new RuntimeException("Unable to check in, Try again later.");
        }
    }

    public String checkOut(CheckOutRequest request) {
        ParkingSpot spot = parkingRepository.getParkingSpot(request.getSpotId());
        Reservation reservation = (request.getReservationId() > 0) ? parkingRepository.getReservation(request.getReservationId()) : null;
        if (canCheckOut(reservation, spot)) {
            updateSpotAndReservationOnCheckOut(spot, reservation);
            return "Check-out successful.";
        } else {
            throw new RuntimeException("Unable to check out, Try again later.");
        }
    }

    private boolean canReserveSpot(ParkingSpot spot) {
        return spot != null && spot.getStatus().equals("available");
    }

    private Reservation createReservation(ParkingSpot spot) {
        return new Reservation(parkingRepository.getNextReservationId(), spot.getId(), false);
    }

    private void updateSpotAndReservation(ParkingSpot spot, Reservation reservation) {
        spot.setStatus("reserved");
        parkingRepository.saveOrUpdateParkingSpot(spot);
        parkingRepository.saveOrUpdateReservation(reservation);
    }

    private boolean canCheckIn(Reservation reservation, ParkingSpot spot) {
        return (reservation != null && !reservation.isCheckedIn()) || (spot != null && spot.getStatus().equals("available"));
    }

    private void updateSpotAndReservationOnCheckIn(ParkingSpot spot, Reservation reservation) {
        if (reservation != null) {
            reservation.setCheckedIn(true);
            parkingRepository.saveOrUpdateReservation(reservation);
        }
        spot.setStatus("occupied");
        parkingRepository.saveOrUpdateParkingSpot(spot);
    }

    private boolean canCheckOut(Reservation reservation, ParkingSpot spot) {
        return (reservation != null && reservation.isCheckedIn()) || (spot != null && spot.getStatus().equals("occupied"));
    }

    private void updateSpotAndReservationOnCheckOut(ParkingSpot spot, Reservation reservation) {
        if (reservation != null) {
            reservation.setCheckedIn(false);
            parkingRepository.saveOrUpdateReservation(reservation);
        }
        spot.setStatus("available");
        parkingRepository.saveOrUpdateParkingSpot(spot);
    }

    public List<ParkingSpot> getAvailableParkingSpots() {
        return parkingRepository.getParkingSpots().stream().filter(park->park.getStatus().equalsIgnoreCase("available")).collect(Collectors.toList());
    }
}
