package com.xebia.eda.domain;

import java.util.Objects;

public class OrderShipped {

    private Long orderId;
    private Long customerId;

    public OrderShipped() {
    }

    public OrderShipped(Long orderId, Long customerId) {
        this.orderId = orderId;
        this.customerId = customerId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderShipped that = (OrderShipped) o;
        return orderId.equals(that.orderId) &&
                customerId.equals(that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, customerId);
    }

    @Override
    public String toString() {
        return "OrderShipped{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                '}';
    }
}
