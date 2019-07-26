package com.xebia.eda.replication;

import com.xebia.common.domain.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(CustomerProducer.class)
public class CustomerReplicatorWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerReplicatorWriter.class);

    private final CustomerProducer customerProducer;

    @Autowired
    public CustomerReplicatorWriter(CustomerProducer customerProducer) {
        this.customerProducer = customerProducer;
    }

    /**
     * <h3>Exercise 3a-1</h3>
     * Task: Put customer object on the Kinesis stream 'customerReplication'.
     * The basic wiring to put messages on the mentioned Kinesis stream is already done for you (@see CustomerProducer and this class).
     * For this exercise to succeed do the following:
     * - Use the injected CustomerProducer to put a customer on the 'customerReplication' stream
     */
    public void replicateCustomer(Customer customer) {
        LOGGER.info("Replicating customer: {}", customer);
        customerProducer.customersOut().send(new GenericMessage<>(customer));
    }
}
