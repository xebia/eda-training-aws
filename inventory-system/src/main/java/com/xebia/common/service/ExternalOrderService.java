package com.xebia.common.service;

import com.xebia.common.domain.Shipment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ExternalOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalOrderService.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final String orderSystemUri;

    public ExternalOrderService(@Value("${order.system.uri}") String orderSystemUri) {
        this.orderSystemUri = orderSystemUri;
    }

    public void notifyOrderShipped(Long orderId) {
        Map body = new HashMap<String, String>();
        body.put("id", orderId);
        body.put("status", "SHIPPED");
        try {
            restTemplate.patchForObject(orderSystemUri + "/orders/" + orderId, body, Map.class, new HashMap());
        } catch (Exception ex) {
            LOGGER.error("Could not notify Order service of shipment due to=[{}]", ex.getMessage(), ex);
        }

    }
}
