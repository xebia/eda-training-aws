package com.xebia.eda.domain;

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

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "OrderShipped{" +
                "orderId=" + orderId +
                '}';
    }
}
