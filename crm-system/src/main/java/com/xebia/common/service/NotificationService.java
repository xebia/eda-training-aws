package com.xebia.common.service;

import com.xebia.common.domain.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    public void notifyCustomer(Customer customer, Long orderId) {
        pretendSending(customer, orderId);
    }


    private void pretendSending(Customer customer, Long orderId) {
        if (customer.isNotificationEmail()) {
            LOGGER.info(format("Sending email for [%s] to [%s]: Order with ID [%s] shipped!", customer.getName(), customer.getEmail(), orderId));
        }

        if (customer.isNotificationText()) {
            LOGGER.info(format("Sending SMS for [%s] to [%s]: Order with ID [%s] shipped!", customer.getName(), customer.getMobile(), orderId));
        }
    }
}


