package com.xebia.eda.replication;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface CustomerProducer {
    String STREAM = "customerReplication";

    @Output(STREAM)
    MessageChannel customersOut();
}
