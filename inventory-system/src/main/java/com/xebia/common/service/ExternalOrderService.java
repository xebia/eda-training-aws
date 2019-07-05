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
