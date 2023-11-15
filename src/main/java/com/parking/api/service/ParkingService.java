package com.parking.api.service;

import com.parking.api.model.*;
import com.parking.api.repository.ParkingRepository;
import com.parking.api.util.ParkingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class ParkingService {

    private final ParkingRepository parkingRepository;
    private final ReentrantLock lock = new ReentrantLock();

    private static final Logger logger = LoggerFactory.getLogger(ParkingService.class);

    public ParkingService(ParkingRepository parkingRepository) {
        this.parkingRepository = parkingRepository;
    }

    public List<ParkingLot> getParkingLots() {
        logger.info("Getting all parking lots");
        return parkingRepository.getParkingLots();
    }

    public ParkingLot getParkingLot(int id) {
        logger.info("Getting parking lot with ID: {}", id);
        return parkingRepository.getParkingLot(id);
    }

    public List<ParkingSpot> getParkingSpots() {
        logger.info("Getting all parking spots");
        return parkingRepository.getParkingSpots();
    }

    public ParkingSpot getParkingSpot(int id) {
        logger.info("Getting parking spot with ID: {}", id);
        return parkingRepository.getParkingSpot(id);
    }

    public String reserveParkingSpot(ReservationRequest request) {
        logger.info("Reserving parking spot with ID: {}", request.getSpotId());
        lock.lock();
        try {
            ParkingSpot spot = parkingRepository.getParkingSpot(request.getSpotId());
            if (canReserveSpot(spot)) {
                Reservation reservation = createReservation(spot);
                updateSpotAndReservation(spot, reservation);
                logger.info("Spot reserved successfully. Reservation ID: {}", reservation.getId());
                return "Spot reserved successfully. Reservation ID: " + reservation.getId();
            } else {
                logger.error("Unable to reserve the spot, Try again later.");
                throw new RuntimeException("Unable to reserve the spot, Try again later.");
            }
        } finally {
            lock.unlock();
        }
    }

    public String checkInBySpotId(int spotId) {
        logger.info("Checking in by spot ID: {}", spotId);
        lock.lock();
        try {
            ParkingSpot spot = parkingRepository.getParkingSpot(spotId);
            return validateAndUpdateSpotStatus(spot);
        } finally {
            lock.unlock();
        }
    }

    private String validateAndUpdateSpotStatus(ParkingSpot spot) {
        if (spot != null && spot.getStatus().equals("available")) {
            spot.setCheckInTime(LocalDateTime.now());
            spot.setStatus("occupied");
            parkingRepository.saveOrUpdateParkingSpot(spot);
            logger.info("Check-in successful.");
            return "Check-in successful.";
        } else {
            logger.error("Unable to check in, Try again later.");
            throw new RuntimeException("Unable to check in, Try again later.");
        }
    }

    public String checkInByReservationID(Long reservationId) {
        logger.info("Checking in by reservation ID: {}", reservationId);
        lock.lock();
        try {
            Reservation reservation = parkingRepository.getReservation(reservationId);
            ParkingSpot spot = parkingRepository.getParkingSpot(reservation.getSpotId());
            return validateAndUpdateSpotStatus(spot);
        } finally {
            lock.unlock();
        }
    }

    public String checkOut(CheckOutRequest request) {
        logger.info("Checking out by spot ID: {}", request.getSpotId());
        ParkingSpot spot = parkingRepository.getParkingSpot(request.getSpotId());
       // Reservation reservation = (request.getReservationId() > 0) ? parkingRepository.getReservation(request.getReservationId()) : null;
        if (spot != null && spot.getStatus().equals("occupied")) {
            spot.setStatus("available");
            spot.setCheckOutTime(LocalDateTime.now());
            parkingRepository.saveOrUpdateParkingSpot(spot);
            long hours = ParkingUtil.roundUpToNearestHour(spot.getCheckInTime(), spot.getCheckOutTime());
            String message = "You have to pay amount $" + hours * ParkingUtil.FEE_PER_HOUR;
            logger.info(message);
            return message;
        } else {
            logger.error("Unable to check out, Try again later.");
            throw new RuntimeException("Unable to check out, Try again later.");
        }
    }

    private boolean canReserveSpot(ParkingSpot spot) {
        return spot != null && spot.getStatus().equalsIgnoreCase("available");
    }

    private Reservation createReservation(ParkingSpot spot) {
        return new Reservation(parkingRepository.getNextReservationId(), spot.getId(), false);
    }

    private void updateSpotAndReservation(ParkingSpot spot, Reservation reservation) {
        spot.setStatus("reserved");
        parkingRepository.saveOrUpdateParkingSpot(spot);
        parkingRepository.saveOrUpdateReservation(reservation);
    }


    public List<ParkingSpot> getAvailableParkingSpots() {
        logger.info("Getting available parking spots");
        return parkingRepository.getParkingSpots().stream()
                .filter(park -> park.getStatus().equalsIgnoreCase("available"))
                .collect(Collectors.toList());
    }
}
