package com.parking.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public  class CheckOutRequest {
    private long reservationId;
    private int spotId;
}