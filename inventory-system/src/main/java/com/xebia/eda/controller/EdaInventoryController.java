package com.xebia.eda.controller;

import com.xebia.common.domain.Shipment;
import com.xebia.common.service.InventoryService;
import com.xebia.eda.messaging.Sqs;
import com.xebia.eda.messaging.messages.ShipmentConfirmation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.xebia.eda.messaging.Sqs.ORDER_SHIPPED_QUEUE;
import static com.xebia.eda.messaging.Sqs.SHIP_ORDER_QUEUE;
import static org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy.ON_SUCCESS;

@Controller
public class EdaInventoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EdaInventoryController.class);

    private InventoryService inventoryService;
    private Sqs sqs;
    private TaskScheduler scheduler;

    @Autowired
    public EdaInventoryController(InventoryService inventoryService, Sqs sqs, TaskScheduler scheduler) {
        this.inventoryService = inventoryService;
        this.sqs = sqs;
        this.scheduler = scheduler;
    }

    @SqsListener(value = SHIP_ORDER_QUEUE, deletionPolicy = ON_SUCCESS)
    public void handle(Shipment shipment) {
        Instant shipmentDate = Instant.now().plusSeconds(15);
        scheduler.schedule(() -> sqs.sendMessage(ORDER_SHIPPED_QUEUE, new ShipmentConfirmation(shipment.getOrderId())), shipmentDate);

        LOGGER.info("Scheduled order with id=[{}] to be shipped at {}", shipment.getOrderId(), shipmentDate);

        inventoryService.saveShipment(shipment.withShipmentDate(shipmentDate));
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
