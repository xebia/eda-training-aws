package com.xebia.common;

import com.xebia.common.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DomainSamples {

    public static ShippingAddress SHIPPING_ADDRESS_1 = new ShippingAddress("Sesamstreet", "5b", "3456AB", "Amsterdam", "Netherlands");
    public static ShippingAddress SHIPPING_ADDRESS_2 = new ShippingAddress("Beathovenlaan", "4", "4532AB", "Rotterdam", "Netherlands");

    public static Customer CUSTOMER_1 = new Customer("John Doe", "abc@efg.nl", "0612345678", true, true, new Address("Sesamstraat", "5b", "3456AB", "Amsterdam", "Netherlands"));
    public static Customer CUSTOMER_2 = new Customer("Dave Burk", "dave@burk.nl", "0612345679", true, false, new Address("Beathovenlaan", "4", "4532AB", "Rotterdam", "Netherlands"));


    public static Order createInitialOrder(int lineCount) {
        Order order = new Order(1234L, OrderState.INITIATED, SHIPPING_ADDRESS_1);
        List<OrderLine> orderLines = IntStream.rangeClosed(1, lineCount)
                .boxed()
                .map(i -> new OrderLine(1000 + i, "Fancy Gadget #" + i, i, i * 100))
                .collect(Collectors.toList());
        order.getLines().addAll(orderLines);
        return order;
    }
}
