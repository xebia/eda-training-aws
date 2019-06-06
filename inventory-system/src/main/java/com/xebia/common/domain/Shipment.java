package com.xebia.common.domain;

import org.springframework.data.annotation.Immutable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "shipments")
@Immutable
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "shipment_date")
    private Instant shipmentDate;

    @Embedded
    private ShipmentAddress shipmentAddress;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private List<InventoryItem> items;

    public Shipment() {
    }

    public Shipment(@NotNull Long orderId, ShipmentAddress shipmentAddress, List<InventoryItem> items) {
        this.orderId = orderId;
        this.shipmentAddress = shipmentAddress;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public List<InventoryItem> getItems() {
        return items;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Instant getShipmentDate() {
        return shipmentDate;
    }

    public ShipmentAddress getShipmentAddress() {
        return shipmentAddress;
    }

    public Shipment withId(Long id) {
        this.id = id;
        return this;
    }

    public Shipment withShipmentDate(Instant shipmentDate) {
        this.shipmentDate = shipmentDate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shipment shipment = (Shipment) o;
        return Objects.equals(id, shipment.id) &&
                Objects.equals(orderId, shipment.orderId) &&
                Objects.equals(shipmentDate, shipment.shipmentDate) &&
                Objects.equals(shipmentAddress, shipment.shipmentAddress) &&
                Objects.equals(items, shipment.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, shipmentDate, shipmentAddress, items);
    }

    @Override
    public String toString() {
        return "Shipment{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", shipmentDate=" + shipmentDate +
                ", shipmentAddress=" + shipmentAddress +
                ", items=" + items +
                '}';
    }
}