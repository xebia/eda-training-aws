package com.xebia.soa.controller;

import com.xebia.common.domain.Customer;
import com.xebia.common.domain.Order;
import com.xebia.common.domain.OrderState;
import com.xebia.soa.service.ExternalCustomerService;
import com.xebia.soa.service.ExternalInventoryService;
import com.xebia.common.service.NotificationService;
import com.xebia.common.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/order-api/v1")
public class SoaOrderController {

    private final OrderService orderService;
    private final ExternalInventoryService externalInventoryService;
    private final ExternalCustomerService externalCustomerService;
    private final NotificationService notificationService;

    @Autowired
    public SoaOrderController(OrderService orderService, ExternalInventoryService externalInventoryService, ExternalCustomerService externalCustomerService, NotificationService notificationService) {
        this.orderService = orderService;
        this.externalInventoryService = externalInventoryService;
        this.externalCustomerService = externalCustomerService;
        this.notificationService = notificationService;
    }

    @GetMapping("/orders/{id}")
    @ResponseBody
    public Optional<Order> getOrder(Long id) {
        return orderService.getOrder(id);
    }

    @GetMapping("/orders")
    @ResponseBody
    public List<Order> getOrders() {
        return orderService.getOrders();
    }

    /**
     * Problematic service. Order initiation is dependent on:
     * - CRM system
     * - Inventory system
     */
    @PostMapping("/orders")
    @ResponseBody
    public Order saveOrder(@Valid @RequestBody Order order) {
        Order saved = orderService.saveOrder(order.withStatus(OrderState.INITIATED).withCreatedNow());
        Customer customer = externalCustomerService.getCustomer(order.getCustomerId());
        externalInventoryService.initiateShipment(customer, saved);
        notificationService.notifyCustomer(customer, saved);
        return saved;
    }

    /**
     * Problematic service since it depends on:
     * - CRM system
     */
    @PatchMapping("/orders/{id}")
    @ResponseBody
    public Order patchOrder(@Valid @RequestBody Order order, @PathVariable("id") Long id) {
        Optional<Order> saved = orderService.getOrder(id);
        return saved.map(o -> {
            Order patched = orderService.updateOrder(o.withStatus(order.getStatus()), id);
            Customer customer = externalCustomerService.getCustomer(o.getCustomerId());
            notificationService.notifyCustomer(customer, patched);
            return patched;
        }).orElseThrow(() -> new IllegalArgumentException(String.format("Order with id %s not found", id)));
    }

}
