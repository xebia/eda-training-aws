package com.xebia.eda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.common.DomainSamples;
import com.xebia.common.domain.Customer;
import com.xebia.eda.EdaCustomerApplication;
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

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = {EdaCustomerApplication.class})
@ComponentScan(basePackages = { "com.xebia.common.*", "com.xebia.eda.*" })
@ActiveProfiles({"default", "test"})
public class EdaCustomerControllerTests {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void shouldInsertCustomer() throws Exception {
        Customer customer = DomainSamples.CUSTOMER_JOE;
        MvcResult response = mockMvc.perform(post("/customer-api/v2/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customer))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Customer inserted = mapper.readValue(response.getResponse().getContentAsString(), Customer.class);
        assertEquals(customer.getName(), inserted.getName());

    }

}
