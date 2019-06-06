package com.xebia.eda.controller;

import com.xebia.common.DomainSamples;
import com.xebia.common.domain.Order;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.xebia.common.SQSSNSConfig.HELLO_QUEUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles({"default", "test"})
@Ignore
public class EdaSQSTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    NotificationMessagingTemplate notificationMessagingTemplate;

    @Autowired
    QueueMessagingTemplate queueMessagingTemplate;

    @Test
    public void putGetFromSQS() throws Exception {
        Order order = DomainSamples.createInitialOrder(5);
        queueMessagingTemplate.convertAndSend(HELLO_QUEUE, order);

        Order receive = queueMessagingTemplate.receiveAndConvert(HELLO_QUEUE, Order.class);
        assertEquals(order, receive);


    }


}
