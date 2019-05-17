package com.xebia.common.domain;

import org.springframework.data.annotation.Immutable;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "inventoryitems")
@Immutable
public class InventoryItem {


    public InventoryItem(int productId, int itemCount) {
        this.productId = productId;
        this.itemCount = itemCount;
    }

    private InventoryItem() {

    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Basic
    @Column(name = "product_id")
    private int productId;
    @Basic
    @Column(name = "item_count")
    private int itemCount;

    public Long getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public int getItemCount() {
        return itemCount;
    }

    public InventoryItem withId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryItem orderLine = (InventoryItem) o;
        return id == orderLine.id &&
                productId == orderLine.productId &&
                itemCount == orderLine.itemCount ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, itemCount);
    }
}
