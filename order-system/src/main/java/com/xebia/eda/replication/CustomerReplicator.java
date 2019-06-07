package com.xebia.eda.replication;

import com.xebia.common.domain.Customer;
import com.xebia.eda.messaging.Kinesis.StreamConsumer;
import org.springframework.stereotype.Component;

import static com.xebia.eda.messaging.Kinesis.CUSTOMER_UPDATES_STREAM;

@Component
public class CustomerReplicator {

    @StreamConsumer(CUSTOMER_UPDATES_STREAM)
    public void consume(Customer replicatedCustomer) {
        System.out.println(replicatedCustomer);
    }
}
