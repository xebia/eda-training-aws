package com.xebia.soa.service;

import com.xebia.common.domain.ShippingAddress;
import com.xebia.common.domain.Customer;
import com.xebia.common.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExternalInventoryService {

    public static final Logger LOGGER = LoggerFactory.getLogger(ExternalInventoryService.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final String inventorySystemUri;

    @Autowired
    public ExternalInventoryService(@Value("${inventory.system.uri}") String inventorySystemUri) {
        this.inventorySystemUri = inventorySystemUri;
    }

    public void initiateShipment(Customer customer, Order order) {
        ShipmentDto body = convertToShipment(customer, order);
        try {
            restTemplate.postForEntity(inventorySystemUri + "/inventory-api/v1/shipments", body, ShipmentDto.class);
            LOGGER.info("SOA: Call inventory system to ship order: {}", order.getId());
        } catch (Exception ex) {
            LOGGER.error("SOA: Could not ship Order due to=[{}]", ex.getMessage(), ex);
        }

    }

    public static ShipmentDto convertToShipment(Customer customer, Order order) {
        ShippingAddress shippingAddress = order.getShippingAddress();
        ShipmentAddressDto shipmentAddressDto = new ShipmentAddressDto(customer.getName(), shippingAddress.getStreet(), shippingAddress.getNumber(), shippingAddress.getZipCode(), shippingAddress.getCity(), shippingAddress.getCountry());
        List<InventoryItemDto> items = order.getLines().stream().map(i -> new InventoryItemDto(i.getProductId(), i.getItemCount())).collect(Collectors.toList());
        return new ShipmentDto(order.getId(), shipmentAddressDto, items);
    }
}

class ShipmentDto {

    private Long id;
    private Long orderId;
    private ShipmentAddressDto shipmentAddress;
    private List<InventoryItemDto> items;

    public ShipmentDto() {

    }

    public ShipmentDto(@NotNull Long orderId, ShipmentAddressDto shipmentAddress, List<InventoryItemDto> items) {
        this.orderId = orderId;
        this.shipmentAddress = shipmentAddress;
        this.items = items;
    }

    public Long getOrderId() {
        return orderId;
    }

    public ShipmentAddressDto getShipmentAddress() {
        return shipmentAddress;
    }

    public List<InventoryItemDto> getItems() {
        return items;
    }

    public Long getId() {
        return id;
    }
}

class ShipmentAddressDto {

    private String name;
    private String street;
    private String number;
    private String zipCode;
    private String city;
    private String country;

    public ShipmentAddressDto() {

    }

    public ShipmentAddressDto(String name, String street, String number, String zipCode, String city, String country) {
        this.name = name;
        this.street = street;
        this.city = city;
        this.number = number;
        this.country = country;
        this.zipCode = zipCode;
    }


    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }
}

class InventoryItemDto {

    private int productId;
    private int itemCount;

    public InventoryItemDto() {

    }

    public InventoryItemDto(int productId, int itemCount) {
        this.productId = productId;
        this.itemCount = itemCount;
    }

    public int getProductId() {
        return productId;
    }

    public int getItemCount() {
        return itemCount;
    }
}