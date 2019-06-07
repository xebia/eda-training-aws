package com.xebia.eda.domain;

import java.util.Objects;

public class OrderShipped {

    private Long orderId;

    public OrderShipped() {
    }

    public OrderShipped(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderShipped that = (OrderShipped) o;
        return orderId.equals(that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }

    @Override
    public String toString() {
        return "OrderShipped{" +
                "orderId=" + orderId +
                '}';
    }
}
