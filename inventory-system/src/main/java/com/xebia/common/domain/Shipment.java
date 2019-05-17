package com.xebia.common.domain;

import org.springframework.data.annotation.Immutable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

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
    private LocalDateTime shipmentDate;

    @Embedded
    private Recipient recipient;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private List<InventoryItem> items;

    public Shipment() {
    }

    public Shipment(@NotNull Long orderId, Recipient recipient, List<InventoryItem> items) {
        this.orderId = orderId;
        this.recipient = recipient;
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

    public Shipment withId(Long id) {
        this.id = id;
        return this;
    }

    public Shipment withShipmentDate(LocalDateTime shipmentDate) {
        this.shipmentDate = shipmentDate;
        return this;
    }


}