package com.xebia.eda.controller;

import com.xebia.common.domain.Order;
import com.xebia.common.service.OrderService;
import com.xebia.eda.service.CustomerViewService;
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
    private final CustomerViewService customerViewService;

    @Autowired
    public EdaOrderController(OrderService orderService, CustomerViewService customerViewService) {
        this.orderService = orderService;
        this.customerViewService = customerViewService;
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
        return orderService.saveOrder(order);
    }



}
