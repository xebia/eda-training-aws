package com.xebia.eda.controller;

import com.xebia.common.domain.Shipment;
import com.xebia.common.service.ExternalOrderService;
import com.xebia.common.service.InventoryService;
import com.xebia.eda.configuration.Sqs;
import com.xebia.eda.domain.OrderPlaced;
import com.xebia.eda.domain.OrderShipped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static com.xebia.eda.configuration.Sns.ORDER_SHIPPED_TOPIC;
import static com.xebia.eda.configuration.Sqs.ORDER_PLACED_QUEUE;
import static org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy.ON_SUCCESS;

@Controller
public class EdaInventoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EdaInventoryController.class);

    private InventoryService inventoryService;
    private NotificationMessagingTemplate topic;
    private ExternalOrderService externalOrderService;
    private TaskScheduler scheduler;

    @Autowired
    public EdaInventoryController(InventoryService inventoryService, NotificationMessagingTemplate topic, ExternalOrderService externalOrderService, TaskScheduler scheduler) {
        this.inventoryService = inventoryService;
        this.topic = topic;
        this.externalOrderService = externalOrderService;
        this.scheduler = scheduler;
    }

    /**
     * <h3>Exercise 1b</h3>
     * Task: Consume messages from the 'orderPlaced' SQS queue.
     * The method below is a copy from the logic of the @see SOAInventoryController and needs to be changed.
     * For this exercise to succeed, replace the REST endpoint 'shipments' as follows:
     * - Change the method to consume 'OrderPlaced' events from a SQS queue (no return type)
     * - remove the REST endpoint annotations (@PostMapping/@ResponseBody)
     * - add an @SqsListener annotation with configuration to listen to the 'orderPlaced' SQS queue.
     * - Hint: use the 'OrderPlaced.asShipment()' method to convert the OrderPlaced event to a Shipment domain object.
     */
    @SqsListener(value = ORDER_PLACED_QUEUE, deletionPolicy = ON_SUCCESS)
    public void handle(OrderPlaced event) {
        LOGGER.info("EDA: received order {}", event);
        Instant shipmentDate = Instant.now().plusSeconds(15);
        Shipment shipment = event.asShipment();
        scheduleFakeShipmentNotification(event, shipmentDate);
        inventoryService.saveShipment(shipment.withShipmentDate(shipmentDate));
    }


    /**
     * <h3>Exercise 2a</h3>
     * Task: Instead of calling the order-system directly when the order is shipped emit an 'OrderShipped' event on a SNS topic, which
     * then can be consumed by the other systems.
     * The implementation below is a copy from the logic of the @see SOAInventoryController and needs to be changed.
     * For this exercise to succeed, replace the 'externalOrderService.notifyOrderShipped(...)' call as follows:
     * - Change the method signature to accept an 'OrderPlaced' event previously consumed instead of a 'Shipment'
     * - create an instance of 'OrderShipped' using the data from 'OrderPlaced' and 'shipmentDate'
     * - put the OrderShipped event to the pre-configured 'orderShipped' SNS topic using the NotificationMessagingTemplate.sendNotification(...) method.
     */
    private void scheduleFakeShipmentNotification(OrderPlaced event, Instant shipmentDate) {
        scheduler.schedule(() -> {
            try {
                OrderShipped orderShippedEvent = new OrderShipped(event.getOrderId(), event.getCustomerId(), shipmentDate);
                topic.sendNotification(ORDER_SHIPPED_TOPIC, orderShippedEvent, "Order shipped!");
                LOGGER.info("EDA: Sent OrderShipped event {}",orderShippedEvent);
            } catch(Exception ex) {
                LOGGER.error("EDA: Failed to send OrderShipped Event due to {}", ex.getMessage(), ex);
            }

        }, shipmentDate);
    }

    @GetMapping("/shipments/{id}")
    @ResponseBody
    public Optional<Shipment> getShipment(@PathVariable("id") Long id) {
        return inventoryService.getShipment(id);
    }

    @GetMapping("/shipments")
    @ResponseBody
    public List<Shipment> getShipments() {
        return inventoryService.getShipments();
    }

}
