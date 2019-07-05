package com.xebia.eda.domain;

import com.xebia.common.domain.InventoryItem;
import com.xebia.common.domain.ShipmentAddress;

import java.time.Instant;
import java.util.List;

public class OrderPlaced {

    private Long orderId;
    private Long customerId;
    private Instant created;
    private ShipmentAddress shipmentAddress;
    private List<InventoryItem> items;

    public OrderPlaced() {
    }

    public OrderPlaced(Long orderId, Long customerId, Instant created, ShipmentAddress shipmentAddress, List<InventoryItem> items) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.created = created;
        this.shipmentAddress = shipmentAddress;
        this.items = items;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public ShipmentAddress getShipmentAddress() {
        return shipmentAddress;
    }

    public void setShipmentAddress(ShipmentAddress shipmentAddress) {
        this.shipmentAddress = shipmentAddress;
    }

    public List<InventoryItem> getItems() {
        return items;
    }

    public void setItems(List<InventoryItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "OrderPlaced{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", created=" + created +
                ", shipmentAddress=" + shipmentAddress +
                ", items=" + items +
                '}';
    }
}
