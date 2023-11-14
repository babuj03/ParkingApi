package com.parking.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public  class ParkingLot {
    private int id;
    private String name;
    private String location;
    private int totalSpots;
}