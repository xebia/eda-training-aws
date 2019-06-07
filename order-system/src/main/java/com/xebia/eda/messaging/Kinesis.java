package com.xebia.eda.messaging;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.InvalidStateException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.ShutdownException;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;
import com.amazonaws.services.kinesis.clientlibrary.types.InitializationInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ProcessRecordsInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ShutdownInput;
import com.amazonaws.services.kinesis.model.ListStreamsResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
public class Kinesis {

    public static final String CUSTOMER_UPDATES_STREAM = "customer-updates";

    private final AmazonKinesis amazonKinesis;
    private final AmazonDynamoDB amazonDynamoDB;
    private final AmazonCloudWatch amazonCloudWatch;
    private final AWSCredentialsProvider awsCredentialsProvider;
    private final ApplicationContext applicationContext;
    private final ObjectMapper objectMapper;

    @Autowired
    public Kinesis(AmazonKinesis amazonKinesis,
                   AmazonDynamoDB amazonDynamoDB,
                   AmazonCloudWatch amazonCloudWatch,
                   AWSCredentialsProvider awsCredentialsProvider,
                   ApplicationContext applicationContext,
                   ObjectMapper objectMapper) {
        this.amazonKinesis = amazonKinesis;
        this.amazonDynamoDB = amazonDynamoDB;
        this.amazonCloudWatch = amazonCloudWatch;
        this.awsCredentialsProvider = awsCredentialsProvider;
        this.applicationContext = applicationContext;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void findAndStartConsumers() {
        Map<String, Object> streamConsumers = new HashMap<>();

        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            Object bean = applicationContext.getBean(beanName);
            Class<?> beanClass = bean.getClass();
            for (Method method : beanClass.getMethods()) {
                if (method.isAnnotationPresent(StreamConsumer.class)) {
                    StreamConsumer annotation = method.getAnnotation(StreamConsumer.class);
                    streamConsumers.put(annotation.value(), bean);
                }
            }
        }

        // Create topics
        streamConsumers.keySet().stream()
                .filter(streamName -> !streamExists(streamName))
                .forEach(streamName -> amazonKinesis.createStream(streamName, 2));

        // Create record processors and bind to methods
        streamConsumers.entrySet().stream()
                .map(entry -> {
                    KinesisClientLibConfiguration config = new KinesisClientLibConfiguration(
                            "order-system",
                            entry.getKey(),
                            awsCredentialsProvider,
                            format("order-system-%s", entry.getKey())
                    );

                    // Find the method to be invoked
                    final Method callback = resolveConsumingMethod(entry.getValue());

                    return new Worker.Builder()
                            .kinesisClient(amazonKinesis)
                            .dynamoDBClient(amazonDynamoDB)
                            .cloudWatchClient(amazonCloudWatch)
                            .recordProcessorFactory(() -> new InflectedMethodProcessor(entry.getValue(), callback, objectMapper))
                            .config(config)
                            .build();
                })
                .forEach(worker -> Executors.newSingleThreadExecutor().execute(worker));
    }

    private Method resolveConsumingMethod(Object obj) {
        Class<?> beanClass = obj.getClass();
        for (Method method : beanClass.getMethods()) {
            if (method.isAnnotationPresent(StreamConsumer.class)) {
                return method;
            }
        }
        return null;
    }

    private boolean streamExists(String streamName) {
        ListStreamsResult listStreamsResult = amazonKinesis.listStreams();
        return listStreamsResult.getStreamNames().contains(streamName);
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface StreamConsumer {
        String value() default "";
    }

    public static class InflectedMethodProcessor implements IRecordProcessor {

        private final Object target;
        private final Method callback;
        private final ObjectMapper objectMapper;

        public InflectedMethodProcessor(Object target, Method callback, ObjectMapper objectMapper) {
            this.target = target;
            this.callback = callback;
            this.objectMapper = objectMapper;
        }

        @Override
        public void initialize(InitializationInput initializationInput) {
        }

        @Override
        public void processRecords(ProcessRecordsInput processRecordsInput) {
            processRecordsInput.getRecords().stream()
                    .map(record -> {
                        try {
                            String payloadString = new String(record.getData().array(), UTF_8);
                            Class<?> payloadType = callback.getParameterTypes()[0];
                            return objectMapper.readValue(payloadString, payloadType);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .forEach(message -> {
                        try {
                            callback.invoke(target, message);
                        } catch (InvocationTargetException | IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    });

            try {
                processRecordsInput.getCheckpointer().checkpoint();
            } catch (ShutdownException | InvalidStateException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void shutdown(ShutdownInput shutdownInput) {
        }

        public static class Message<T> {
            private T payload;

            public Message(T payload) {
                this.payload = payload;
            }

            public T getPayload() {
                return payload;
            }

            public void setPayload(T payload) {
                this.payload = payload;
            }

            @Override
            public String toString() {
                return "Message{" +
                        "payload=" + payload +
                        '}';
            }
        }
    }
}
