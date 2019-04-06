package com.xebia.common.service;

import com.xebia.common.domain.Customer;
import com.xebia.common.domain.Order;
import com.xebia.common.domain.OrderLine;
import com.xebia.common.domain.OrderState;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DomainSamples {

    public static Customer CUSTOMER_JOE = new Customer("Joe", "joe@gmail.com");
    public static Customer CUSTOMER_JANE = new Customer("Jane", "jane@hotmail.com");


    public static Order createInitialOrder(int lineCount) {
        Order order = new Order(CUSTOMER_JOE, OrderState.INITIATED);
        List<OrderLine> orderLines = IntStream.rangeClosed(1, lineCount)
                .boxed()
                .map(i -> new OrderLine(1000 + i, "Fancy Gadget #" + i, i))
                .collect(Collectors.toList());
        order.getLines().addAll(orderLines);
        return order;
    }
}
