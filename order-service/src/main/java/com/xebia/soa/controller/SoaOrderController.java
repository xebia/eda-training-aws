package com.xebia.soa.controller;

import com.xebia.common.order.Order;
import com.xebia.common.order.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/order-api/v1")
public class SoaOrderController {

    private OrderRepository repository;

    @Autowired
    public SoaOrderController(OrderRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/orders/{id}")
    @ResponseBody
    public Optional<Order> getOrder(Long id) {
        return repository.findById(id);
    }


    @GetMapping("/orders")
    @ResponseBody
    public List<Order> getOrders() {
        return repository.findAll();
    }

    @PostMapping("/orders")
    @ResponseBody
    public Order saveOrder(@Valid @RequestBody Order order) {
        return repository.save(order);
    }

    @PutMapping("/orders/{id}")
    @ResponseBody
    public Order updateOrder(@Valid @RequestBody Order order, Long id) {
        Assert.isTrue(order.getId() == null || order.getId() == id, "Conflicting order id");
        return repository.save(order);
    }

    @PostConstruct
    public void done() {
        System.out.println("============> Initialized " + repository);
    }

}
