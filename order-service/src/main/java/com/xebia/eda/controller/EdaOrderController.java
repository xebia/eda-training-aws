package com.xebia.eda.controller;

import com.xebia.common.domain.Order;
import com.xebia.common.domain.OrderLine;
import com.xebia.common.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/order-api/v2")
//TODO: add hooks for async logic
public class EdaOrderController {

    private final OrderService orderService;

    @Autowired
    public EdaOrderController(OrderService orderService) {
        this.orderService = orderService;
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

    @PostMapping("/orders")
    @ResponseBody
    public Order saveOrder(@Valid @RequestBody Order order) {
        //TODO: claim inventory
        return orderService.saveOrder(order);
    }

    @PutMapping("/orders/{id}")
    @ResponseBody
    public Order updateOrder(@Valid @RequestBody Order order, Long id) {
        return orderService.updateOrder(order, id);
    }

    @GetMapping("/orders/{orderId}/lines/{lineId}")
    @ResponseBody
    public Optional<OrderLine> getOrderLine(Long orderId, Long lineId) {
        return orderService.getOrderLine(orderId, lineId);
    }

    @PostMapping("/orders/{orderId}/lines")
    @ResponseBody
    public OrderLine saveOrderLine(Long orderId, @Valid @RequestBody OrderLine orderline) {
        return orderService.saveOrderLine(orderId, orderline);
    }

    @PutMapping("/orders/{orderId}/lines/{lineId}")
    @ResponseBody
    public OrderLine updateOrderLine(Long orderId, Long lineId, @Valid @RequestBody OrderLine orderline) {
        return orderService.updateOrderLine(orderId, lineId, orderline);
    }


}
