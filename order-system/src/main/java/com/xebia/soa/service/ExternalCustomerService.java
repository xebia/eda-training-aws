package com.xebia.soa.service;

import com.xebia.common.domain.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate  ;

@Service
public class ExternalCustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalCustomerService.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final String crmSystemUri;

    @Autowired
    public ExternalCustomerService(@Value("${crm.system.uri}") String crmSystemUri) {
        this.crmSystemUri = crmSystemUri;
    }

    public Customer getCustomer(Long id) {
        try {
            ResponseEntity<Customer> responseEntity = restTemplate.getForEntity(crmSystemUri + "/customer-api/v1/customers/" + id, Customer.class);
            return responseEntity.getBody();
        } catch (Exception ex) {
            LOGGER.error("Could not ship Order due to=[{}]", ex.getMessage(), ex);
            throw new IllegalArgumentException(ex);
        }
    }
}

