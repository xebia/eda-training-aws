package com.xebia.eda.controller;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.Subscription;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.xebia.LocalstackConfig;
import com.xebia.common.DomainSamples;
import com.xebia.common.domain.Order;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationMessage;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.Acknowledgment;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.annotation.PostConstruct;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.xebia.common.SQSSNSConfig.HELLO_NOTIFICATION_QUEUE;
import static com.xebia.common.SQSSNSConfig.HELLO_QUEUE;
import static com.xebia.common.SQSSNSConfig.HELLO_TOPIC;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
