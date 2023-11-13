package com.parking.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpot {
    private int id;
    private int lotId;
    private String spotNumber;
    private String status;  // "available", "reserved", occupied.
}