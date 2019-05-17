package com.xebia.common.service;

import com.xebia.common.domain.Shipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Optional<Shipment> getShipment(Long id) {
        return inventoryRepository.findById(id);
    }

    public List<Shipment> getShipments() {
        return inventoryRepository.findAll();
    }

    public Shipment saveShipment(Shipment shipment) {
        return inventoryRepository.save(shipment);
    }

    public Shipment updateShipment(Shipment shipment, Long id) {
        Assert.isTrue((shipment.getId() == null) || (shipment.getId() == id), "Conflicting shipment id");
        return inventoryRepository.save(shipment);
    }


}
