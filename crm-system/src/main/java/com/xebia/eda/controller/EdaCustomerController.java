package com.xebia.eda.controller;

import com.xebia.common.domain.Customer;
import com.xebia.common.service.CustomerService;
import com.xebia.common.service.NotificationService;
import com.xebia.eda.domain.OrderShipped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationMessage;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static com.xebia.eda.configuration.Sqs.ORDER_SHIPPED_NOTIFICATION_QUEUE;
import static org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy.ON_SUCCESS;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping(value = "/customer-api/v2")
public class EdaCustomerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EdaCustomerController.class);

    private final CustomerService customerService;
    private final NotificationService notificationService;

    @Autowired
    public EdaCustomerController(CustomerService customerService, NotificationService notificationService) {
        this.customerService = customerService;
        this.notificationService = notificationService;
    }

    @CrossOrigin
    @GetMapping("/customers/{id}")
    @ResponseBody
    public Optional<Customer> getCustomer(@PathVariable("id") Long id) {
        return customerService.getCustomer(id);
    }

    @CrossOrigin
    @GetMapping("/customers")
    @ResponseBody
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @CrossOrigin
    @PostMapping("/customers")
    @ResponseBody
    public Customer saveCustomer(@Valid @RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }

    @CrossOrigin
    @PutMapping("/customers/{id}")
    @ResponseBody
    public Customer updateCustomer(@Valid @RequestBody Customer customer, @PathVariable("id") Long id) {
        return customerService.updateCustomer(customer, id);
    }

    @DeleteMapping("/customers/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteCustomer(@PathVariable("id") Long id) {
        customerService.deleteCustomer(id);
    }

    @SqsListener(value = ORDER_SHIPPED_NOTIFICATION_QUEUE, deletionPolicy = ON_SUCCESS)
    public void handle(@NotificationMessage OrderShipped event) {
        LOGGER.info("EDA: Received order shipped event: {}", event);
        customerService.getCustomer(event.getCustomerId())
                .ifPresent(customer -> notificationService.notifyCustomer(customer, event.getOrderId()));
    }
}
