package com.xebia.common.service;

import com.xebia.common.domain.Customer;
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
    private final CustomerRepository customerRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    //========== CUSTOMERS ===============//
    public Optional<Customer> getCustomer(Long id) {
        return customerRepository.findById(id);
    }

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Customer customer, Long id) {
        Assert.isTrue(customer.getId() == null || customer.getId() == id, "Conflicting customer id");
        return customerRepository.save(customer);

    }



        //========== ORDERS ===============//
    public Optional<Order> getOrder(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public Order saveOrder(Order order) {
        Customer customer = order.getCustomer().getId() == null ? customerRepository.save(order.getCustomer()) : order.getCustomer();
        return orderRepository.save(order.withCustomer(customer));
    }

    public Order updateOrder(Order order, Long id) {
        Assert.isTrue((order.getId() == null) || (order.getId() == id), "Conflicting order id");
        return orderRepository.findById(id).map(saved -> orderRepository.save(order.withCustomer(saved.getCustomer())))
                .orElseThrow(() -> new IllegalArgumentException(String.format("Order with id %s not found", id)));
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
