package com.parking.api.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parking.api.model.ParkingSpot;
import com.parking.api.model.ReservationRequest;
import com.parking.api.repository.ParkingRepository;
import com.parking.api.service.ParkingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
class ParkingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private ParkingRepository parkingRepository;

    @InjectMocks
    private ParkingService parkingService;



    @Test
    public void testReserveParkingSpot_Success() throws Exception {
        ReservationRequest reservationRequest = new ReservationRequest(1);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/parking/reserve")
                        .content(objectMapper.writeValueAsString(reservationRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("Spot reserved successfully. Reservation ID: 1", content);
    }

    @Test
    public  void testReserveParkingSpot_Failure() throws Exception {
        ParkingSpot spot = new ParkingSpot(10001, 1, "A2", "occupied");
        parkingRepository.saveOrUpdateParkingSpot(spot);
        ReservationRequest request = new ReservationRequest(10001);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/parking/reserve")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("Unable to reserve the spot, Try again later"));
    }
}