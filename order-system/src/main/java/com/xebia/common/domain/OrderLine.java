package com.xebia.common.domain;

import org.springframework.data.annotation.Immutable;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "orderlines")
@Immutable
public class OrderLine {

    public OrderLine(int productId, String productName, int itemCount, int priceCents) {
        this.productId = productId;
        this.productName = productName;
        this.itemCount = itemCount;
        this.priceCents = priceCents;
    }

    private OrderLine() {

    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "product_id")
    @NotNull
    private Integer productId;

    @Column(name = "product_name")
    @NotEmpty
    private String productName;

    @Column(name = "item_count")
    @NotNull
    private Integer itemCount;

    @Column(name = "price_cents")
    @NotNull
    private Integer priceCents;

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

    public int getPriceCents() {
        return priceCents;
    }



    public boolean sameContent(OrderLine orderLine) {
        return priceCents == orderLine.getPriceCents() && orderLine.getProductId() == productId && orderLine.getProductName().equals(productName) && orderLine.getItemCount() == itemCount;

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
        return productId == orderLine.productId &&
                itemCount == orderLine.itemCount &&
                priceCents == orderLine.priceCents &&
                Objects.equals(id, orderLine.id) &&
                Objects.equals(productName, orderLine.productName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, productName, itemCount, priceCents);
    }

    @Override
    public String toString() {
        return "OrderLine{" +
                "id=" + id +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", itemCount=" + itemCount +
                ", priceCents=" + priceCents +
                '}';
    }
}
