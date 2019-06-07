package com.xebia.eda.controller;

import com.xebia.common.DomainSamples;
import com.xebia.common.domain.Order;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationMessage;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.Acknowledgment;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.xebia.common.SQSSNSConfig.HELLO_NOTIFICATION_QUEUE;
import static com.xebia.common.SQSSNSConfig.HELLO_TOPIC;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles({"default", "test"})
@Ignore
public class EdaSNSTests {

    @Component
    @TestConfiguration
    @EnableSqs
    public static class ManualDeletionPolicyTestListener {

        private final CountDownLatch countDownLatch = new CountDownLatch(1);

        @SqsListener(value = HELLO_NOTIFICATION_QUEUE, deletionPolicy = SqsMessageDeletionPolicy.NEVER)
        public void receive(@NotificationMessage Order message, Acknowledgment acknowledgment)
                throws ExecutionException, InterruptedException {
            System.out.println(acknowledgment.acknowledge().get());
            System.out.println("received " + message);
            this.countDownLatch.countDown();
        }

        public CountDownLatch getCountDownLatch() {
            return this.countDownLatch;
        }

    }

    @Autowired
    ManualDeletionPolicyTestListener manualDeletionPolicyTestListener;

    @Autowired
    NotificationMessagingTemplate notificationMessagingTemplate;

    @Autowired
    QueueMessagingTemplate queueMessagingTemplate;

    @Test
    public void putSNSGetFromSQS() throws Exception {
        Order order = DomainSamples.createInitialOrder(5);
        notificationMessagingTemplate.convertAndSend(HELLO_TOPIC, order);

        assertTrue(manualDeletionPolicyTestListener.getCountDownLatch().await(5, TimeUnit.SECONDS));
    }
}
