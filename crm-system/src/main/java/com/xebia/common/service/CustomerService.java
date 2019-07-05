package com.xebia.common.service;

import com.xebia.common.domain.Customer;
import com.xebia.eda.replication.CustomerReplicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerReplicator stream;


    @Autowired
    public CustomerService(CustomerRepository customerRepository, CustomerReplicator stream) {
        this.customerRepository = customerRepository;
        this.stream = stream;
    }

    public Optional<Customer> getCustomer(Long id) {
        return customerRepository.findById(id);
    }

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    public Customer saveCustomer(Customer customer ) {
        Customer saved = customerRepository.save(customer);
        stream.replicateCustomer(saved);
        return saved;
    }

    public List<Customer> saveCustomers(List<Customer> customers ) {
        return customers.stream().map(this::saveCustomer).collect(Collectors.toList());
    }


    public Customer updateCustomer(Customer customer, Long id) {
        Assert.isTrue(customer.getId() == null || customer.getId().equals(id), "Conflicting customer id");
        Customer saved = customerRepository.save(customer.withId(id));
        stream.replicateCustomer(saved);
        return saved;
    }
}
