package com.xebia.eda.replication;

import com.xebia.common.domain.Customer;
import com.xebia.eda.service.CustomerViewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(CustomerProcessor.class)
public class CustomerReplicator {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerReplicator.class);

    private final CustomerViewService customerViewService;

    @Autowired
    public CustomerReplicator(CustomerViewService customerViewService) {
        this.customerViewService = customerViewService;
    }

    @StreamListener(CustomerProcessor.STREAM)
    public void processCustomer(Customer customer) {
        LOGGER.info("Received customer update: {}", customer);

        customerViewService.saveCustomer(customer);
    }
}
