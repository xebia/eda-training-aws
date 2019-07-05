package com.xebia.common.domain;

import org.springframework.data.annotation.Immutable;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.xebia.common.domain.OrderState.INITIATED;
import static java.time.Instant.now;

@Entity
@Table(name = "orders")
@Immutable
public class Order {
    public Order(Long customerId, OrderState status, ShippingAddress shippingAddress, Instant created) {
        this.customerId = customerId;
        this.status = status;
        this.created = created;
        this.shippingAddress = shippingAddress;
    }

    public Order(Long customerId, OrderState status, ShippingAddress shippingAddress) {
        this.customerId = customerId;
        this.status = status;
        this.created = now();
        this.shippingAddress = shippingAddress;
    }

    private Order() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Embedded
    @Valid
    private ShippingAddress shippingAddress;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderState status = INITIATED;

    @Column(name = "created")
    private Instant created;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    @Valid
    private final List<OrderLine> lines = new ArrayList<OrderLine>();

    public Long getId() { return id; }

    public OrderState getStatus() {
        return status;
    }

    public Instant getCreated() {
        return created;
    }

    public List<OrderLine> getLines() { return lines; }

    public Long getCustomerId() { return customerId; }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public Order add(OrderLine orderLine) {
        lines.add(orderLine);
        return this;
    }

    public Order remove(Long orderLineId) {
        lines.removeIf(l -> l.getId() == orderLineId);
        return this;
    }

    public Order withStatus(OrderState status) {
        this.status = status;
        return this;
    }

    public Order withCreatedNow() {
        this.created = now();
        return this;
    }



    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
                Objects.equals(customerId, order.customerId) &&
                Objects.equals(shippingAddress, order.shippingAddress) &&
                status == order.status &&
                Objects.equals(created, order.created) &&
                Objects.equals(lines, order.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, shippingAddress, status, created, lines);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", shippingAddress=" + shippingAddress +
                ", status=" + status +
                ", created=" + created +
                ", lines=" + lines +
                '}';
    }
}
