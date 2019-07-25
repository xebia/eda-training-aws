package com.xebia.eda.domain;

import com.xebia.common.domain.Customer;
import com.xebia.common.domain.Order;
import com.xebia.common.domain.ShippingAddress;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class OrderPlaced {

    private Long orderId;
    private Long customerId;
    private Instant created;
    private ShipmentAddress shipmentAddress;
    private List<InventoryItem> items;

    public OrderPlaced() {
    }

    public OrderPlaced(Long orderId, Long customerId, Instant created, ShipmentAddress shipmentAddress, List<InventoryItem> items) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.created = created;
        this.shipmentAddress = shipmentAddress;
        this.items = items;
    }

    public static OrderPlaced asOrderPlacedEvent(Customer customer, Order order) {
        ShippingAddress shippingAddress = order.getShippingAddress();
        return new OrderPlaced(
                order.getId(),
                order.getCustomerId(),
                order.getCreated(),
                new ShipmentAddress(
                        customer.getName(),
                        shippingAddress.getStreet(),
                        shippingAddress.getNumber(),
                        shippingAddress.getZipCode(),
                        shippingAddress.getCity(),
                        shippingAddress.getCountry()
                ),
                order.getLines().stream()
                        .map(line -> new InventoryItem(line.getProductId(), line.getItemCount()))
                        .collect(toList())
        );
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public ShipmentAddress getShipmentAddress() {
        return shipmentAddress;
    }

    public void setShipmentAddress(ShipmentAddress shipmentAddress) {
        this.shipmentAddress = shipmentAddress;
    }

    public List<InventoryItem> getItems() {
        return items;
    }

    public void setItems(List<InventoryItem> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderPlaced that = (OrderPlaced) o;
        return Objects.equals(orderId, that.orderId) &&
                customerId.equals(that.customerId) &&
                created.equals(that.created) &&
                shipmentAddress.equals(that.shipmentAddress) &&
                items.equals(that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, customerId, created, shipmentAddress, items);
    }

    @Override
    public String toString() {
        return "OrderPlaced{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", created=" + created +
                ", shipmentAddress=" + shipmentAddress +
                ", items=" + items +
                '}';
    }

    public static class InventoryItem {

        private Integer productId;
        private Integer itemCount;

        public InventoryItem() {
        }

        public InventoryItem(Integer productId, Integer itemCount) {
            this.productId = productId;
            this.itemCount = itemCount;
        }

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public Integer getItemCount() {
            return itemCount;
        }

        public void setItemCount(Integer itemCount) {
            this.itemCount = itemCount;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            InventoryItem that = (InventoryItem) o;
            return productId.equals(that.productId) &&
                    itemCount.equals(that.itemCount);
        }

        @Override
        public int hashCode() {
            return Objects.hash(productId, itemCount);
        }

        @Override
        public String toString() {
            return "ShipmentItem{" +
                    "productId=" + productId +
                    ", itemCount=" + itemCount +
                    '}';
        }
    }

    public static class ShipmentAddress {
        private String name;
        private String street;
        private String number;
        private String zipCode;
        private String city;
        private String country;

        public ShipmentAddress() {
        }

        public ShipmentAddress(String name, String street, String number, String zipCode, String city, String country) {
            this.name = name;
            this.street = street;
            this.number = number;
            this.zipCode = zipCode;
            this.city = city;
            this.country = country;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ShipmentAddress that = (ShipmentAddress) o;
            return name.equals(that.name) &&
                    street.equals(that.street) &&
                    number.equals(that.number) &&
                    zipCode.equals(that.zipCode) &&
                    city.equals(that.city) &&
                    country.equals(that.country);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, street, number, zipCode, city, country);
        }

        @Override
        public String toString() {
            return "ShipmentAddress{" +
                    "name='" + name + '\'' +
                    ", street='" + street + '\'' +
                    ", number='" + number + '\'' +
                    ", zipCode='" + zipCode + '\'' +
                    ", city='" + city + '\'' +
                    ", country='" + country + '\'' +
                    '}';
        }
    }
}
