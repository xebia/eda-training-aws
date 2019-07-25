package com.xebia.eda.controller;

import com.xebia.common.domain.Order;
import com.xebia.common.service.OrderService;
import com.xebia.eda.domain.AuditEvent;
import com.xebia.eda.domain.OrderShipped;
import com.xebia.eda.service.CustomerViewService;
import com.xebia.eda.service.FirehoseAuditLogger;
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
import static com.xebia.eda.configuration.Sqs.ORDER_PLACED_QUEUE;
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
    private final CustomerViewService customerViewService;
    private final QueueMessagingTemplate queue;
    private final FirehoseAuditLogger auditLogger;

    @Autowired
    public EdaOrderController(OrderService orderService,
                              CustomerViewService customerViewService,
                              QueueMessagingTemplate queue, FirehoseAuditLogger auditLogger) {
        this.orderService = orderService;
        this.customerViewService = customerViewService;
        this.queue = queue;
        this.auditLogger = auditLogger;
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

        return customerViewService.getCustomer(order.getCustomerId())
                .map(customer -> asOrderPlacedEvent(customer, saved))
                .flatMap(event -> {
                    String msg = "EDA: Placing OrderPlaced event on queue: " + event;
                    LOGGER.info(msg);
                        queue.convertAndSend(ORDER_PLACED_QUEUE, event);
                        auditLogger.put(AuditEvent.of(msg));
                    return Optional.of(saved);
                })
                .map(result -> accepted().body(result))
                .orElseThrow(() -> new IllegalArgumentException(format("EDA: Cannot find customer with ID [%s]", order.getCustomerId())));
    }

    @SqsListener(value = ORDER_SHIPPED_EVENT_QUEUE, deletionPolicy = ON_SUCCESS)
    public void handle(@NotificationMessage OrderShipped event) {
        String msg = "EDA: Received order shipped event: " + event;
        LOGGER.info(msg);
        auditLogger.put(AuditEvent.of(msg));
        orderService.getOrder(event.getOrderId())
                .map(o -> orderService.updateOrder(o.withStatus(SHIPPED), event.getOrderId()))
                .orElseThrow(() -> new IllegalArgumentException(format("EDA: Order with id [%s] not found", event.getOrderId())));
    }

}
