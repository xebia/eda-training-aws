package com.xebia.eda.configuration;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.model.ListStreamsResult;
import com.xebia.eda.replication.CustomerProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class Kinesis {

    @Autowired
    AmazonKinesis amazonKinesis;

    @PostConstruct
    public void createTopics() {
        if (!streamExists(CustomerProcessor.STREAM)) {
            amazonKinesis.createStream(CustomerProcessor.STREAM, 2);
        }
    }

    private boolean streamExists(String streamName) {
        ListStreamsResult listStreamsResult = amazonKinesis.listStreams();
        return listStreamsResult.getStreamNames().contains(streamName);
    }
}
