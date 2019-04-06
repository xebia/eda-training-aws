package com.xebia.common.domain;

import org.springframework.data.annotation.Immutable;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "orderlines")
@Immutable
public class OrderLine {

    public OrderLine(int productId, String productName, int itemCount) {
        this.productId = productId;
        this.productName = productName;
        this.itemCount = itemCount;
    }

    private OrderLine() {

    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Basic
    @Column(name = "product_id")
    private int productId;
    @Basic
    @Column(name = "product_name")
    private String productName;
    @Basic
    @Column(name = "item_count")
    private int itemCount;

    public Long getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getItemCount() {
        return itemCount;
    }

    public boolean sameContent(OrderLine orderLine) {
        return orderLine.getProductId() == productId && orderLine.getProductName().equals(productName) && orderLine.getItemCount() == itemCount;

    }

    public OrderLine withId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLine orderLine = (OrderLine) o;
        return id == orderLine.id &&
                productId == orderLine.productId &&
                itemCount == orderLine.itemCount &&
                Objects.equals(productName, orderLine.productName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, productName, itemCount);
    }
}
