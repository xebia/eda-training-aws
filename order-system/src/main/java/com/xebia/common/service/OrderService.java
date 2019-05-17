package com.xebia.common.service;

import com.xebia.common.domain.Order;
import com.xebia.common.domain.OrderLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Optional<Order> getOrder(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order updateOrder(Order order, Long id) {
        Assert.isTrue((order.getId() == null) || (order.getId() == id), "Conflicting order id");
        return orderRepository.save(order);
    }

    public Order patchOrder(Order order, Long id) {
        Assert.isTrue((order.getId() == null) || (order.getId() == id), "Conflicting order id");
        return orderRepository.save(order);
    }


    //========== ORDERLINE ===============//

    public Optional<OrderLine> getOrderLine(Long orderId, Long lineId) {
        return orderRepository.findById(orderId).flatMap(order ->
                order.getLines().stream().filter(l -> l.getId() == lineId).findFirst());
    }

    public OrderLine saveOrderLine(Long orderId, OrderLine orderline) {
        return orderRepository.findById(orderId).map(order ->
                orderRepository.save(order.add(orderline))).
                flatMap(order -> order.getLines().stream().filter(l ->
                        l.sameContent(orderline)).max(Comparator.comparing(OrderLine::getId)))
                .orElseThrow(() -> new IllegalArgumentException(String.format("Order with id %s not found", orderId)));
    }

    public OrderLine updateOrderLine(Long orderId, Long lineId, OrderLine orderline) {
        Assert.isTrue(orderline.getId() == null || orderline.getId() == lineId, "Conflicting orderline id");

        return orderRepository.findById(orderId).map(order ->
                orderRepository.save(order.remove(lineId).add(orderline.withId(lineId))))
                .map(order -> orderline.withId(lineId))
                .orElseThrow(() -> new IllegalArgumentException(String.format("Order with id %s not found", orderId)));
    }
}
