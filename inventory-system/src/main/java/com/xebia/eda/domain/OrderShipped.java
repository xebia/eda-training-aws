package com.xebia.eda.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Objects;

public class OrderShipped {

    private Long orderId;
    private Long customerId;
    private LocalDateTime shipmentDate;

    public OrderShipped() {
    }

    public OrderShipped(Long orderId, Long customerId, Instant shipmentDate) {
        this(orderId, customerId, LocalDateTime.ofInstant(shipmentDate, OffsetDateTime.now().getOffset()));
    }

    public OrderShipped(Long orderId, Long customerId, LocalDateTime shipmentDate) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.shipmentDate = shipmentDate;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }


    public LocalDateTime getShipmentDate() {
        return shipmentDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderShipped that = (OrderShipped) o;
        return Objects.equals(orderId, that.orderId) &&
                Objects.equals(customerId, that.customerId) &&
                Objects.equals(shipmentDate, that.shipmentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, customerId, shipmentDate);
    }

    @Override
    public String toString() {
        return "OrderShipped{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", shipmentDate=" + shipmentDate +
                '}';
    }
}
