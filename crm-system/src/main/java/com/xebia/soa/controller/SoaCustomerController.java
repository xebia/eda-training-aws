package com.xebia.soa.controller;

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
@RequestMapping(value = "/customer-api/v1")
public class SoaCustomerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoaCustomerController.class);

    private final CustomerService customerService;
    private final NotificationService notificationService;

    @Autowired
    public SoaCustomerController(CustomerService customerService, NotificationService notificationService) {
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

    @PutMapping("/customers/{id}/notifications")
    public void notifyCustomer(@PathVariable("id") Long id, @RequestParam("orderId") Long orderId) {
        LOGGER.info("SOA: Notify customer that order is shipped");
        customerService.getCustomer(id)
                .ifPresent(customer -> notificationService.notifyCustomer(customer, orderId));
    }
}
