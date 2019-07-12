package com.xebia.eda.replication;

import com.xebia.common.domain.Customer;
import com.xebia.eda.service.CustomerViewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(CustomerConsumer.class)
public class CustomerReplicationReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerReplicationReader.class);

    private final CustomerViewRepository customerViewRepository;

    @Autowired
    public CustomerReplicationReader(CustomerViewRepository customerViewRepository) {
        this.customerViewRepository = customerViewRepository;
    }

    /**
     * <h3>Exercise 3b-1</h3>
     * Task: Consume customer objects from the Kinesis Stream 'customerReplication'.
     * The basic wiring to consume messages on the mentioned Kinesis stream is already done for you (@see CustomerConsumer and this class).
     * For this exercise to succeed do the following:
     * - add an @StreamListener annotation with configuration to listen to the 'customerReplication' Kinesis Stream'
     * - save the consumed customer in the database using the 'customerViewRepository.save(...)' method
     */
    public void processCustomer(Customer customer) {
        //TODO: implement
    }
}
