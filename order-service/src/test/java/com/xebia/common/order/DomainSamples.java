package com.xebia.common.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DomainSamples {


    public static Order createInitialOrder(int lineCount) {
        Order order = new Order(OrderState.INITIATED);
        List<OrderLine> orderLines = IntStream.rangeClosed(1, lineCount)
                .boxed()
                .map(i -> new OrderLine(1000 + i, "Fancy Gadget #" + i, i))
                .collect(Collectors.toList());
        order.getLines().addAll(orderLines);
        return order;
    }
}
