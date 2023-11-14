package com.parking.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public  class Reservation {
    private long id;
    private int spotId;
    private boolean checkedIn;
}