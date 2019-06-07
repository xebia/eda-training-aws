package com.xebia.eda.messaging.messages;

public class ShipmentConfirmation {

    private Long orderId;

    public ShipmentConfirmation() {
    }

    public ShipmentConfirmation(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    @Override
    public String toString() {
        return "ShipmentConfirmation{" +
                "orderId=" + orderId +
                '}';
    }
}
