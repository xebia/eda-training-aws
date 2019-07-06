package com.xebia.eda.controller;

import com.xebia.common.domain.Customer;
import com.xebia.common.domain.Order;
import com.xebia.common.service.OrderService;
import com.xebia.eda.domain.OrderShipped;
import com.xebia.soa.service.ExternalCustomerService;
import com.xebia.soa.service.ExternalInventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationMessage;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static com.xebia.common.domain.OrderState.INITIATED;
import static com.xebia.common.domain.OrderState.SHIPPED;
import static com.xebia.eda.configuration.Sqs.ORDER_CREATED_QUEUE;
import static com.xebia.eda.configuration.Sqs.ORDER_SHIPPED_EVENT_QUEUE;
import static com.xebia.eda.domain.OrderPlaced.asOrderPlacedEvent;
import static java.lang.String.format;
import static org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy.ON_SUCCESS;
import static org.springframework.http.ResponseEntity.accepted;

@RestController
@RequestMapping(value = "/order-api/v2")
public class EdaOrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EdaOrderController.class);

    private final OrderService orderService;
    private final ExternalCustomerService customerService;
    private final ExternalInventoryService externalInventoryService;
    private final QueueMessagingTemplate queue;

    @Autowired
    public EdaOrderController(OrderService orderService,
                              ExternalCustomerService customerService,
                              ExternalInventoryService externalInventoryService, QueueMessagingTemplate queue) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.externalInventoryService = externalInventoryService;
        this.queue = queue;
    }

    @GetMapping("/orders/{id}")
    @ResponseBody
    public Optional<Order> getOrder(Long id) {
        return orderService.getOrder(id);
    }

    @GetMapping("/orders")
    @ResponseBody
    public List<Order> getOrders() {
        return orderService.getOrders();
    }

    /**
     * <h3>Exercise 1a</h3>
     * Task: Decouple the inventory-system from the order-system by means of the
     * SQS queue 'orderCreated'.
     * The method below is a copy from the logic of the @see SOAOrderController.
     * For this exercise to succeed, replace the SOA call 'ExternalInventoryService.initiateShipment(...)' with an EDA counterpart:
     * - Transform the Order to an OrderPlaced event (see OrderPlaced.asOrderPlacedEvent(...))
     * - put the OrderPlaced event to the pre-configured 'orderCreated' SQS queue using the QueueMessagingTemplate.convertAndSend(...) method.
     */
    @PostMapping("/orders")
    @ResponseBody
    public ResponseEntity<Order> saveOrder(@Valid @RequestBody Order order) {
        Order saved = orderService.saveOrder(order.withStatus(INITIATED).withCreatedNow());
        Customer customer = customerService.getCustomer(order.getCustomerId());
        //TODO: replace following line
        externalInventoryService.initiateShipment(customer, saved);
        return accepted().body(saved);
    }
}
