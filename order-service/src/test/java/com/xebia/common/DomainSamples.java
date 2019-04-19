package com.xebia.common;

import com.xebia.common.domain.Address;
import com.xebia.common.domain.Order;
import com.xebia.common.domain.OrderLine;
import com.xebia.common.domain.OrderState;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DomainSamples {

    public static Address ADDRESS_1 = new Address("Sesamstreet", "5b", "3456AB", "Amsterdam", "Netherlands");
    public static Address ADDRESS_2 = new Address("Beathovenlaan", "4", "4532AB", "Rotterdam", "Netherlands");


    public static Order createInitialOrder(int lineCount) {
        Order order = new Order(1234L, OrderState.INITIATED, ADDRESS_1);
        List<OrderLine> orderLines = IntStream.rangeClosed(1, lineCount)
                .boxed()
                .map(i -> new OrderLine(1000 + i, "Fancy Gadget #" + i, i))
                .collect(Collectors.toList());
        order.getLines().addAll(orderLines);
        return order;
    }
}
