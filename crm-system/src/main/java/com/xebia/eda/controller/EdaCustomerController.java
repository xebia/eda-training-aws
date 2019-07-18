package com.xebia.eda.controller;

import com.xebia.common.domain.Customer;
import com.xebia.common.service.CustomerService;
import com.xebia.common.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/customers/{id}")
    @ResponseBody
    public Optional<Customer> getCustomer(@PathVariable("id") Long id) {
        return customerService.getCustomer(id);
    }

    @GetMapping("/customers")
    @ResponseBody
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @PostMapping("/customers")
    @ResponseBody
    public Customer saveCustomer(@Valid @RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }

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

    /**
     * <h3>Exercise 2c</h3>
     * Task: Consume messages from the 'orderShippedNotification' SQS queue, which were broadcasted by SNS.
     * The method below is a copy from the logic of the @see SOACustomerController and needs to be changed.
     * For this exercise to succeed, replace this REST endpoint as follows:
     * - Change the method to consume 'OrderShipped' events from a SQS queue
     * - remove the REST endpoint annotations (@PostMapping/@ResponseBody/@RequestBody/@PathVariable)
     * - add an @SqsListener annotation with configuration to listen to the 'orderShippedNotification' SQS queue and the correct 'SqsMessageDeletionPolicy'
     * - Hint: don't forget to annotate the 'OrderShipped' event with the @NotificationMessage annotation.
     */
    @PutMapping("/customers/{id}/notifications")
    public void notifyCustomer(@PathVariable("id") Long id, @RequestParam("orderId") Long orderId) {
        LOGGER.info("EDA: Notify customer that order is shipped");
        customerService.getCustomer(id)
                .ifPresent(customer -> notificationService.notifyCustomer(customer, orderId));
    }
}
