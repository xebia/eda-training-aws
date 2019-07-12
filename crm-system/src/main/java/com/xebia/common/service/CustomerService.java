package com.xebia.common.service;

import com.xebia.common.domain.Customer;
import com.xebia.eda.replication.CustomerReplicatorWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerReplicatorWriter stream;


    @Autowired
    public CustomerService(CustomerRepository customerRepository, CustomerReplicatorWriter stream) {
        this.customerRepository = customerRepository;
        this.stream = stream;
    }

    public Optional<Customer> getCustomer(Long id) {
        return customerRepository.findById(id);
    }

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    /**
     * <h3>Exercise 3a-2</h3>
     * Task: replicate a Customer whenever it is saved.
     * For this exercise to succeed do the following:
     * - Use the injected @see CustomerReplicatorWriter to replicate a saved Customer
     */
    public Customer saveCustomer(Customer customer ) {
        Customer saved = customerRepository.save(customer);
        //TODO: replicate customer
        return saved;
    }

    /**
     * <h3>Exercise 3a-3</h3>
     * Task: replicate a Customer whenever it is updated.
     * For this exercise to succeed do the following:
     * - Use the injected @see CustomerReplicatorWriter to replicate a updated Customer
     */
    public Customer updateCustomer(Customer customer, Long id) {
        Assert.isTrue(customer.getId() == null || customer.getId().equals(id), "Conflicting customer id");
        Customer saved = customerRepository.save(customer.withId(id));
        //TODO: replicate customer
        return saved;
    }

    public List<Customer> saveCustomers(List<Customer> customers ) {
        return customers.stream().map(this::saveCustomer).collect(Collectors.toList());
    }


}
