package com.xebia.eda.replication;

import com.xebia.common.domain.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(CustomerProcessor.class)
public class CustomerReplicator {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerReplicator.class);

    private final CustomerProcessor customerProcessor;

    @Autowired
    public CustomerReplicator(CustomerProcessor customerProcessor) {
        this.customerProcessor = customerProcessor;
    }

    public void replicateCustomer(Customer customer) {
        LOGGER.info("Replicating customer: {}", customer);
        customerProcessor.customersOut().send(new GenericMessage<>(customer));
    }
}
