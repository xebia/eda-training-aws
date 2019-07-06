package com.xebia.eda.controller;

import com.xebia.common.domain.Shipment;
import com.xebia.common.service.ExternalOrderService;
import com.xebia.common.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

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
     * Consume messages from the 'orderCreated' SQS queue.
     * The method below is a copy from the logic of the @see SOAInventoryController.
     * For this exercise to succeed, replace the REST endpoint 'shipments' as follows:
     * - Change the method to consume 'OrderPlaced' events from a SQS queue (no return type)
     * - remove the REST endpoint annotations (@PostMapping/@ResponseBody)
     * - add an @SqsListener annotation with configuration to listen to the 'orderCreated' SQS queue.
     * - Hint: use the 'OrderPlaced.asShipment()' method to convert the OrderPlaced event to a Shipment domain object.
     */
    @PostMapping("/shipments")
    @ResponseBody
    public Shipment ship(@Valid @RequestBody Shipment shipment) {
        Instant shipmentDate = Instant.now().plusSeconds(15);
        scheduleFakeShipmentNotification(shipment, shipmentDate);
        return inventoryService.saveShipment(shipment.withShipmentDate(shipmentDate));
    }

    private void scheduleFakeShipmentNotification(Shipment shipment, Instant shipmentDate) {
        scheduler.schedule(() -> {
            LOGGER.info("EDA: call order system to notify that order with id=[{}] is shipped at [{}]", shipment.getOrderId(), shipmentDate);
            externalOrderService.notifyOrderShipped(shipment.getOrderId());
        }, shipmentDate);

        LOGGER.info("EDA: Scheduled order with id=[{}] to be shipped at {}", shipment.getOrderId(), shipmentDate);
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
