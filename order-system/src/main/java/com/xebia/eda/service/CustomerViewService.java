package com.xebia.eda.service;

import com.xebia.common.domain.Customer;
import com.xebia.soa.service.ExternalCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerViewService {

    private final CustomerViewRepository customerViewRepository;
    private final ExternalCustomerService externalCustomerService;

    @Autowired
    public CustomerViewService(CustomerViewRepository customerViewRepository, ExternalCustomerService externalCustomerService) {
        this.customerViewRepository = customerViewRepository;
        this.externalCustomerService = externalCustomerService;
    }

    /**
     * <h3>Exercise 3b-2</h3>
     * Task: Fetch Customers from the local database (which were replicated via Kinesis - see @CustomerReplicationReader) instead of remotely from the crm-system.
     * The method below uses the @see ExternalCustomerService to call the crm-system for fetching a Customer.
     * For this exercise to succeed, replace the call to the crm-system as follows:
     * - Use the injected @see CustomerViewRepository to get the (replicated) customer from the local database using the method: 'getOne(...)'
     */
    public Customer getCustomer(Long id) {
        //TODO: replace this line
        return externalCustomerService.getCustomer(id);
    }

}
