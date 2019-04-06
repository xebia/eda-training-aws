package com.xebia.common.domain;

import org.springframework.data.annotation.Immutable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
@Immutable
public class Order {
    public Order(Customer customer, OrderState status, LocalDateTime created) {
        this.customer = customer;
        this.status = status;
        this.created = created;
    }

    public Order(Customer customer, OrderState status) {
        this.customer = customer;
        this.status = status;
        this.created = LocalDateTime.now();
    }

    private Order() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderState status;

    @Column(name = "created")
    private LocalDateTime created;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private final List<OrderLine> lines = new ArrayList<OrderLine>();

    public Long getId() { return id; }

    public OrderState getStatus() {
        return status;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public List<OrderLine> getLines() { return lines; }

    public Customer getCustomer() { return customer; }


    public Order add(OrderLine orderLine) {
        lines.add(orderLine);
        return this;
    }

    public Order remove(Long orderLineId) {
        lines.removeIf(l -> l.getId() == orderLineId);
        return this;
    }

    public Order withCustomer(Customer customer) {
        this.customer = customer;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
                Objects.equals(customer, order.customer) &&
                status == order.status &&
                Objects.equals(created, order.created) &&
                Objects.equals(lines, order.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, status, created, lines);
    }
}
