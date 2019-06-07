package com.xebia.eda.controller;

import com.xebia.common.domain.Shipment;
import com.xebia.common.service.InventoryService;
import com.xebia.eda.domain.OrderCreated;
import com.xebia.eda.domain.OrderShipped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.xebia.eda.configuration.Sqs.ORDER_CREATED_QUEUE;
import static com.xebia.eda.configuration.Sqs.ORDER_SHIPPED_QUEUE;
import static org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy.ON_SUCCESS;

@Controller
public class EdaInventoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EdaInventoryController.class);

    private InventoryService inventoryService;
    private QueueMessagingTemplate queue;
    private TaskScheduler scheduler;

    @Autowired
    public EdaInventoryController(InventoryService inventoryService, QueueMessagingTemplate queue, TaskScheduler scheduler) {
        this.inventoryService = inventoryService;
        this.queue = queue;
        this.scheduler = scheduler;
    }

    @SqsListener(value = ORDER_CREATED_QUEUE, deletionPolicy = ON_SUCCESS)
    public void handle(OrderCreated event) {
        Instant shipmentDate = Instant.now().plusSeconds(15);
        scheduler.schedule(() -> queue.convertAndSend(ORDER_SHIPPED_QUEUE, new OrderShipped(event.getOrderId())), shipmentDate);
        LOGGER.info("Scheduled order with id=[{}] to be shipped at {}", event.getOrderId(), shipmentDate);

        inventoryService.saveShipment(asShipment(event).withShipmentDate(shipmentDate));
    }

    private Shipment asShipment(OrderCreated event) {
        return new Shipment(
                event.getOrderId(),
                event.getShipmentAddress(),
                event.getItems()
        );
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