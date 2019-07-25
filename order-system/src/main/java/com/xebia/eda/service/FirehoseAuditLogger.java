package com.xebia.eda.service;

/**
 * Copyright 2005-2016 Crown Equipment Corporation. All rights reserved.
 * See license distributed with this file.
 */

import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseAsyncClient;
import com.amazonaws.services.kinesisfirehose.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.eda.domain.AuditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.nio.ByteBuffer;

/**
 * Core implementation with minimal dependencies (no Spring, no java.annotations)
 * to be used with other ecosystems (like Scala)
 */


@Component
public class FirehoseAuditLogger {

    private static final String ENCODING = "UTF-8";
    private final ObjectMapper mapper = new ObjectMapper();
    private final String streamName;

    private boolean initializationFailed = false;
    private AmazonKinesisFirehoseAsyncClient client;

    private static final Logger log = LoggerFactory.getLogger(FirehoseAuditLogger.class);

    private AsyncHandler<PutRecordRequest, PutRecordResult> asyncHandler = new AsyncHandler<PutRecordRequest, PutRecordResult>(){
        @Override
        public void onError(Exception exception) {
            log.error("Failed to publish a logEvent entry to kinesis using appender: ", exception);
        }
        @Override
        public void onSuccess(PutRecordRequest request, PutRecordResult result) {
            log.info("Successfully put record {}", request);
            //no calls needed
        }
    };

    public FirehoseAuditLogger(AmazonKinesisFirehoseAsyncClient client,
                               @Value("${firehose.log.stream}") String streamName) {
        this.client = client;
        this.streamName = streamName;
    }

    @PostConstruct
    public void start() {
        if(streamName == null) {
            initializationFailed = true;
            log.error("Invalid configuration - streamName cannot be null");
            return;
        }

        if (!validateStreamName(client, streamName))
            setInitializationFailed(true);
    }

    @PreDestroy
    public void stop() {
        client.shutdown();
    }

    private boolean validateStreamName(AmazonKinesisFirehoseAsyncClient client, String streamName) {
        try {
            String streamStatus = client.describeDeliveryStream(new DescribeDeliveryStreamRequest()
                    .withDeliveryStreamName(streamName))
                    .getDeliveryStreamDescription()
                    .getDeliveryStreamStatus();
            if(!DeliveryStreamStatus.ACTIVE.name().equals(streamStatus)) {
                log.error("Stream {} is not ready (in active status)", streamName);
                return false;
            }
        }
        catch(ResourceNotFoundException rnfe) {
            log.error("Stream {} doesn't exist", streamName, rnfe);
            return false;
        }
        return true;
    }

    public void put(AuditEvent event) {
        try {
            String raw = mapper.writeValueAsString(event);
            put(raw);
        } catch (JsonProcessingException ex) {
            log.error("Failed to write to audit log event " + event, ex);
        }


    }

    public void put(String message) {
        if(initializationFailed) {
            return;
        }
        try {
            ByteBuffer data = ByteBuffer.wrap((message + "\n").getBytes(ENCODING));
            client.putRecordAsync(new PutRecordRequest().withDeliveryStreamName(streamName)
                    .withRecord(new Record().withData(data)), asyncHandler);
        }
        catch(Exception e) {
            log.error("Failed to schedule logEvent entry for publishing into Kinesis stream: " + streamName, e);
        }
    }

    private void setInitializationFailed(boolean initializationFailed) {
        this.initializationFailed = initializationFailed;
    }




}
