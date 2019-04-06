package com.xebia.common.order;

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
    public Order(OrderState status, LocalDateTime created) {
        this.status = status;
        this.created = created;
    }

    public Order(OrderState status) {
        this.status = status;
        this.created = LocalDateTime.now();
    }

    private Order() {

    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderState status;
    @Basic
    @Column(name = "created")
    private LocalDateTime created;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private final List<OrderLine> lines = new ArrayList<OrderLine>();

    public Long getId() {
        return id;
    }

    public OrderState getStatus() {
        return status;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public List<OrderLine> getLines() {
        return lines;
    }

    public Order add(OrderLine orderLine) {
        lines.add(orderLine);
        return this;
    }

    public Order remove(Long orderLineId) {
        lines.removeIf(l -> l.getId() == orderLineId);
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order that = (Order) o;
        return id == that.id &&
                Objects.equals(status, that.status) &&
                Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, created);
    }
}
