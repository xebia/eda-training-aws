package com.xebia.eda.controller;

import com.xebia.common.domain.Customer;
import com.xebia.common.domain.Order;
import com.xebia.common.service.OrderService;
import com.xebia.eda.messaging.Sqs;
import com.xebia.eda.messaging.messages.ShipmentConfirmation;
import com.xebia.eda.messaging.messages.Shipment;
import com.xebia.eda.service.CustomerViewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static com.xebia.common.domain.OrderState.INITIATED;
import static com.xebia.common.domain.OrderState.SHIPPED;
import static com.xebia.eda.messaging.Sqs.ORDER_SHIPPED_QUEUE;
import static com.xebia.eda.messaging.Sqs.SHIP_ORDER_QUEUE;
import static com.xebia.eda.messaging.messages.Shipment.shipmentFor;
import static org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy.ON_SUCCESS;
import static org.springframework.http.ResponseEntity.accepted;

@RestController
@RequestMapping(value = "/order-api/v2")
public class EdaOrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EdaOrderController.class);

    private final OrderService orderService;
    private final CustomerViewService customerViewService;
    private final Sqs sqs;

    @Autowired
    public EdaOrderController(OrderService orderService,
                              CustomerViewService customerViewService,
                              Sqs sqs) {
        this.orderService = orderService;
        this.customerViewService = customerViewService;
        this.sqs = sqs;
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

    @PostMapping("/orders")
    @ResponseBody
    public ResponseEntity<Order> saveOrder(@Valid @RequestBody Order order) {
        Order saved = orderService.saveOrder(order.withStatus(INITIATED).withCreatedNow());

        // Send shipment request
        Customer customer = new Customer("TODO", null, null, false, false, null);
        Shipment shipment = shipmentFor(customer, saved);
        LOGGER.info("Putting shipment on queue: {}", shipment);
        sqs.sendMessage(SHIP_ORDER_QUEUE, shipment);

        // TODO: use SNS to notify customer that order was placed

        return accepted().body(saved);
    }

    @SqsListener(value = ORDER_SHIPPED_QUEUE, deletionPolicy = ON_SUCCESS)
    public void handle(ShipmentConfirmation message) {
        LOGGER.info("Received shipment confirmation: {}", message);
        orderService.getOrder(message.getOrderId())
                .map(o -> orderService.updateOrder(o.withStatus(SHIPPED), message.getOrderId()))
                .orElseThrow(() -> new IllegalArgumentException(String.format("Order with id %s not found", message.getOrderId())));
    }

}
