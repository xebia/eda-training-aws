package com.xebia.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ExternalOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalOrderService.class);
    private final RestTemplate restTemplate;
    private final String orderSystemUri;

    @Autowired
    public ExternalOrderService(RestTemplate restTemplate, @Value("${order.system.uri}") String orderSystemUri) {
        this.restTemplate = restTemplate;
        this.orderSystemUri = orderSystemUri;
    }

    /**
     * <h3>Exercise 5b</h3>
     * Task: Call new API Gateway endpoint with PATCH request
     * The method below calls the order system directly, but it should call the endpoint shown to you when deploying the adapter.
     * For this exercise to succeed, replace the REST endpoint 'shipments' as follows:
     * - Change line 38 to call your own URL. You don't need the `orderSystemUri` any more.
     */
    public void notifyOrderShipped(Long orderId) {
        Map body = new HashMap<String, String>();
        body.put("id", orderId);
        body.put("status", "SHIPPED");
        try {
            restTemplate.patchForObject(orderSystemUri + "/order-api/v1/orders/" + orderId, body, Map.class, new HashMap());
            LOGGER.info("SOA: Notify Order service that order with id=[{}] is shipped", orderId);
        } catch (Exception ex) {
            LOGGER.error("SOA: Could not notify Order service of shipment due to=[{}]", ex.getMessage(), ex);
        }

    }
}
