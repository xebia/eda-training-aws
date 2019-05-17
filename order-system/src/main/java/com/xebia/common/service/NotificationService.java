package com.xebia.common.service;

import com.xebia.common.domain.Customer;
import com.xebia.common.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    public void notifyCustomer(Customer customer, Order order) {
        pretendSending(customer, order);
    }

    private String notificationMessage(Order order) {
        switch (order.getStatus()) {
            case SHIPPED: return "order with id " + order.getId() + " is shipped";
            case INITIATED: return "order with id " + order.getId() + " is received";
        }
        return null;
    }

    private void pretendSending(Customer customer, Order order) {
        String message = notificationMessage(order);
        if (customer.isNotificationEmail()) {
            LOGGER.info(String.format("Sent email for %s to %s that %s", customer.getName(), customer.getEmail(), message));
        }
        if (customer.isNotificationText()) {
            LOGGER.info(String.format("Sent SMS for %s to %s that %s", customer.getName(), customer.getMobile(), message));

        }

    }
}


