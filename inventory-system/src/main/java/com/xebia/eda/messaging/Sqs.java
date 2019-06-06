package com.xebia.eda.messaging;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@Component
public class Sqs {
    public static final String SHIP_ORDER_QUEUE = "ship-order";
    public static final String ORDER_SHIPPED_QUEUE = "order-shipped";

    private final QueueMessagingTemplate queueMessagingTemplate;

    @Autowired
    public Sqs(QueueMessagingTemplate queueMessagingTemplate) {
        this.queueMessagingTemplate = queueMessagingTemplate;
    }

    public void sendMessage(String queue, Object payload) {
        queueMessagingTemplate.convertAndSend(queue, payload);
    }

    @Configuration
    public static class SqsConfiguration {

        @Autowired
        AmazonSQSAsync amazonSQS;

        @Bean
        public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSqs, MessageConverter messageConverter) {
            return new QueueMessagingTemplate(amazonSqs, (ResourceIdResolver) null, messageConverter);
        }

        @Bean
        public MessageConverter messageConverter(ObjectMapper objectMapper) {
            MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
            messageConverter.setObjectMapper(objectMapper);
            messageConverter.setSerializedPayloadClass(String.class);

            return messageConverter;
        }

        @Bean
        @Primary
        public ObjectMapper objectMapper() {
            return new Jackson2ObjectMapperBuilder().build()
                    .configure(WRITE_DATES_AS_TIMESTAMPS, false)
                    .registerModule(new JavaTimeModule());
        }

        @PostConstruct
        public void createQueues() {
            List<String> queues = new ArrayList<>();
            queues.add(SHIP_ORDER_QUEUE);
            queues.add(ORDER_SHIPPED_QUEUE);

            queues.forEach(amazonSQS::createQueue);
        }
    }
}
