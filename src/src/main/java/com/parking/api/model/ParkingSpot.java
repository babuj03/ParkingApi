package com.parking.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSpot {
    private int id;
    private int lotId;
    private String spotNumber;
    private String status;  // "available", "reserved", occupied.
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
}