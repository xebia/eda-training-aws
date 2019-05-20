package com.xebia.soa.controller;

import com.xebia.common.domain.Shipment;
import com.xebia.common.service.ExternalOrderService;
import com.xebia.common.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZoneId;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/inventory-api/v1")
public class SoaInventoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoaInventoryController.class);


    private InventoryService inventoryService;

    private TaskScheduler scheduler;

    private ExternalOrderService externalOrderService;

    @Autowired
    public SoaInventoryController(InventoryService inventoryService, TaskScheduler scheduler, ExternalOrderService externalOrderService) {
        this.inventoryService = inventoryService;
        this.scheduler = scheduler;
        this.externalOrderService = externalOrderService;
    }

    @PostMapping("/shipments")
    @ResponseBody
    public Shipment ship(@Valid @RequestBody Shipment shipment) {
        LocalDateTime shipmentDate = LocalDateTime.now().plusSeconds(15);
        Date shipped = Date.from(shipmentDate.atZone(ZoneId.systemDefault()).toInstant());
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                externalOrderService.notifyOrderShipped(shipment.getOrderId());
            }
        }, shipped);
        LOGGER.info("Scheduled order with id=[" + shipment.getOrderId() + "] to be shipped in {}", shipped);
        return inventoryService.saveShipment(shipment.withShipmentDate(shipmentDate));
    }

    @GetMapping("/shipments/{id}")
    @ResponseBody
    public Optional<Shipment> getShipment(@PathVariable("id")Long id) {
        return inventoryService.getShipment(id);
    }

    @GetMapping("/shipments")
    @ResponseBody
    public List<Shipment> getShipments() {
        return inventoryService.getShipments();
    }


}
