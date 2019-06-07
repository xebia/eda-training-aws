package com.xebia.eda.controller;

import com.xebia.common.domain.Customer;
import com.xebia.common.service.CustomerService;
import com.xebia.eda.replication.CustomerReplicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/customer-api/v2")
public class EdaCustomerController {

    private final CustomerService customerService;
    private final CustomerReplicator stream;

    @Autowired
    public EdaCustomerController(CustomerService customerService, CustomerReplicator stream) {
        this.customerService = customerService;
        this.stream = stream;
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
        Customer saved = customerService.saveCustomer(customer);
        stream.replicateCustomer(saved);
        return saved;
    }

    @PutMapping("/customers/{id}")
    @ResponseBody
    public Customer updateCustomer(@Valid @RequestBody Customer customer, @PathVariable("id") Long id) {
        Customer saved = customerService.updateCustomer(customer, id);
        stream.replicateCustomer(saved);
        return saved;
    }
}
