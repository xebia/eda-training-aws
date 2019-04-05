package com.xebia.common.order;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "orderline", schema = "orders", catalog = "orders")
public class OrderLine {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Basic
    @Column(name = "product_id")
    private int productId;
    @Basic
    @Column(name = "product_name")
    private String productName;
    @Basic
    @Column(name = "item_count")
    private int itemCount;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false, updatable = false)
    private Order parent;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }


    public Order getParent() {
        return parent;
    }

    public void setParent(Order order) {
        parent = order;
        order.getLines().add(this);
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLine orderLine = (OrderLine) o;
        return id == orderLine.id &&
                productId == orderLine.productId &&
                itemCount == orderLine.itemCount &&
                Objects.equals(productName, orderLine.productName) &&
                Objects.equals(parent, orderLine.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, productName, itemCount, parent);
    }
}
