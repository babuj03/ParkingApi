package com.parking.api.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parking.api.model.*;
import com.parking.api.service.ParkingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class ParkingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ParkingService parkingService;

    @Test
    void testGetParkingLots() throws Exception {
        List<ParkingLot> parkingLots = new ArrayList<>();
        when(parkingService.getParkingLots()).thenReturn(parkingLots);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/parking/lots")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());

        verify(parkingService, times(1)).getParkingLots();
    }

    @Test
    void testGetParkingLotNotFound() throws Exception {
        int parkingLotId = 1;
        when(parkingService.getParkingLot(parkingLotId)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/parking/lots/{id}", parkingLotId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Parking lot not found"));

        verify(parkingService, times(1)).getParkingLot(parkingLotId);
    }

    @Test
    void testGetParkingLotFound() throws Exception {
        int parkingLotId = 1;
        ParkingLot parkingLot = new ParkingLot();
        when(parkingService.getParkingLot(parkingLotId)).thenReturn(parkingLot);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/parking/lots/{id}", parkingLotId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(parkingService, times(1)).getParkingLot(parkingLotId);
    }

    @Test
    void testGetParkingSpots() throws Exception {
        List<ParkingSpot> parkingSpots = new ArrayList<>();
        when(parkingService.getParkingSpots()).thenReturn(parkingSpots);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/parking/spots")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());

        verify(parkingService, times(1)).getParkingSpots();
    }

    @Test
    void testGetParkingSpotNotFound() throws Exception {
        int parkingSpotId = 1;
        when(parkingService.getParkingSpot(parkingSpotId)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/parking/spots/{id}", parkingSpotId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Parking spot not found"));

        verify(parkingService, times(1)).getParkingSpot(parkingSpotId);
    }

    @Test
    void testGetParkingSpotFound() throws Exception {
        int parkingSpotId = 1;
        ParkingSpot parkingSpot = new ParkingSpot();
        when(parkingService.getParkingSpot(parkingSpotId)).thenReturn(parkingSpot);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/parking/spots/{id}", parkingSpotId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(parkingService, times(1)).getParkingSpot(parkingSpotId);
    }

    @Test
    void testGetAvailableParkingSpots() throws Exception {
        List<ParkingSpot> parkingSpots = new ArrayList<>();
        when(parkingService.getAvailableParkingSpots()).thenReturn(parkingSpots);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/parking/available/spots")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());

        verify(parkingService, times(1)).getAvailableParkingSpots();
    }

    @Test
    void testReserveParkingSpot() throws Exception {
        ReservationRequest request = new ReservationRequest();
        when(parkingService.reserveParkingSpot(request)).thenReturn("Reservation successful");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/parking/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Reservation successful"));

        verify(parkingService, times(1)).reserveParkingSpot(request);
    }

    @Test
    void testCheckInBySpotId() throws Exception {
        CheckInRequest request = new CheckInRequest();
        when(parkingService.checkInBySpotId(anyInt())).thenReturn("Check-in successful");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/parking/check-in-by-spot-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Check-in successful"));

        verify(parkingService, times(1)).checkInBySpotId(anyInt());
    }

    @Test
    void testCheckInByReservationID() throws Exception {
        CheckInRequest request = new CheckInRequest();
        when(parkingService.checkInByReservationID(anyLong())).thenReturn("Check-in successful");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/parking/check-in-by-reservation-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Check-in successful"));

        verify(parkingService, times(1)).checkInByReservationID(anyLong());
    }

    @Test
    void testCheckOut() throws Exception {
        CheckOutRequest request = new CheckOutRequest();
        when(parkingService.checkOut(request)).thenReturn("Check-out successful");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/parking/check-out")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Check-out successful"));

        verify(parkingService, times(1)).checkOut(request);
    }
}
