package com.xebia.soa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.common.DomainSamples;
import com.xebia.common.domain.Customer;
import com.xebia.common.domain.Order;
import com.xebia.soa.service.ExternalCustomerService;
import com.xebia.soa.service.ExternalInventoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.*;
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
@ComponentScan(basePackages = { "com.xebia.common.*", "com.xebia.soa.*" })
@ActiveProfiles({"default", "test"})
public class SoaControllerTests {


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

@Configuration
@Profile("test")
class DummyServicesConfig {

    @Bean
    @Primary
    public ExternalCustomerService createDummyCustomerService() {
        return new ExternalCustomerService("dummy-uri") {
            @Override
            public Customer getCustomer(Long id) {
                return new Customer("Johny Holiday", "abc@efg.nl", "0612345678", true, true);
            }
        };
    }

    @Bean
    @Primary
    public ExternalInventoryService createDummyInventoryService() {
        return new ExternalInventoryService("dummy-uri") {
            @Override
            public void initiateShipment(Customer customer, Order order) {
                ExternalInventoryService.LOGGER.info("shipment initiated");
            }
        };
    }
}

