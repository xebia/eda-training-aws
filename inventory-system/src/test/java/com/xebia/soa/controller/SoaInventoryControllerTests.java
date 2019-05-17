package com.xebia.soa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.common.domain.Shipment;
import com.xebia.common.domain.InventoryItem;
import com.xebia.soa.SoaInventoryApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static com.xebia.common.DomainSamples.createShipment;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = {SoaInventoryApplication.class})
@ComponentScan(basePackages = { "com.xebia.common.*", "com.xebia.soa.*" })
@ActiveProfiles({"default", "test"})
public class SoaInventoryControllerTests {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void shouldCreateShipment() throws Exception {
        Shipment shipment = createShipment(3);
        MvcResult response = mockMvc.perform(post("/inventory-api/v1/shipments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(shipment))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Shipment inserted = mapper.readValue(response.getResponse().getContentAsString(), Shipment.class);
        assertEquals(inserted.getItems().size(), 3);
    }

}
