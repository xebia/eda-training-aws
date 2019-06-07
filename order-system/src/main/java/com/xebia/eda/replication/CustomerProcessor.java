package com.xebia.eda.replication;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface CustomerProcessor {
    String STREAM = "customerReplication";

    @Input(STREAM)
    SubscribableChannel customersIn();
}
