package com.xebia.eda.service;

import com.xebia.common.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerViewService {

    private final CustomerViewRepository customerViewRepository;

    @Autowired
    public CustomerViewService(CustomerViewRepository customerViewRepository) {
        this.customerViewRepository = customerViewRepository;
    }

    public Customer saveCustomer(Customer customer) {
        return customerViewRepository.save(customer);
    }

    public Optional<Customer> getCustomer(Long id) {
        return customerViewRepository.findById(id);
    }

    public List<Customer> getCustomers() {
        return customerViewRepository.findAll();
    }

}
