package com.xebia.eda.configuration;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.QueueMessageHandler;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;

import static java.util.Collections.singletonList;

@Configuration
public class Sqs {

    public static final String ORDER_PLACED_QUEUE = "orderPlaced";
    public static final String ORDER_SHIPPED_EVENT_QUEUE = "orderShippedEvent";

    @Bean(destroyMethod = "doStop")
    public SimpleMessageListenerContainer simpleMessageListenerContainer(SimpleMessageListenerContainerFactory factory,
                                                                         QueueMessageHandler queueMessageHandler) {
        SimpleMessageListenerContainer container = factory.createSimpleMessageListenerContainer();
        container.setMessageHandler(queueMessageHandler);
        return container;
    }

    @Bean
    @Primary
    public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(AmazonSQSAsync amazonSQS,
                                                                                       @Value("${sqs.wait.timeout.seconds:5}") Integer waitTimeoutSeconds) {
        SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
        factory.setAmazonSqs(amazonSQS);
        factory.setWaitTimeOut(waitTimeoutSeconds);
        factory.setMaxNumberOfMessages(1);
        return factory;
    }

    @Bean
    @Primary
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSqs, MessageConverter messageConverter) {
        return new QueueMessagingTemplate(amazonSqs, (ResourceIdResolver) null, messageConverter);
    }

    @Bean
    @Primary
    public MessageConverter messageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setObjectMapper(objectMapper);
        messageConverter.setSerializedPayloadClass(String.class);

        return messageConverter;
    }

    @Bean
    @Primary
    public QueueMessageHandler queueMessageHandler(AmazonSQSAsync amazonSQSAsync, MessageConverter messageConverter) {
        QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();
        factory.setAmazonSqs(amazonSQSAsync);
        factory.setMessageConverters(singletonList(messageConverter));

        return factory.createQueueMessageHandler();
    }
}
