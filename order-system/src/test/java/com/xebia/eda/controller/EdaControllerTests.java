package com.xebia.eda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.common.DomainSamples;
import com.xebia.common.domain.Customer;
import com.xebia.common.domain.Order;
import com.xebia.eda.service.CustomerViewService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static com.xebia.eda.configuration.Sqs.ORDER_CREATED_QUEUE;
import static com.xebia.eda.domain.OrderPlaced.asOrderPlacedEvent;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles({"default", "test"})
@Ignore
public class EdaControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    CustomerViewService customerViewService;

    @MockBean
    QueueMessagingTemplate queueMessagingTemplate;

    @Test
    public void shouldInsertOrder() throws Exception {
        Customer customer = DomainSamples.CUSTOMER_1;
        when(customerViewService.getCustomer(customer.getId())).thenReturn(Optional.of(customer));

        Order order = DomainSamples.createInitialOrder(5);
        MvcResult response = mockMvc.perform(post("/order-api/v2/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(order))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();

        Order inserted = mapper.readValue(response.getResponse().getContentAsString(), Order.class);
        assertEquals(order.getStatus(), inserted.getStatus());
        assertEquals(order.getLines().size(), inserted.getLines().size());

        verify(queueMessagingTemplate).convertAndSend(ORDER_CREATED_QUEUE, asOrderPlacedEvent(customer, inserted));
    }
}
