package com.xebia.soa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.common.order.DomainSamples;
import com.xebia.common.order.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles({"default", "test"})
public class SoaApplicationTests {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void shouldInsertOrder() throws Exception {
        Order order = DomainSamples.createInitialOrder(5);
        MvcResult response = mockMvc.perform(post("/order-api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(order))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Order inserted = mapper.readValue(response.getResponse().getContentAsString(), Order.class);
        assertEquals(order.getStatus(), inserted.getStatus());
        assertEquals(order.getLines().size(), inserted.getLines().size());
    }

}
