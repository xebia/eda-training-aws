package com.xebia.common;

import com.xebia.common.domain.InventoryItem;
import com.xebia.common.domain.Recipient;
import com.xebia.common.domain.Shipment;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DomainSamples {


    public static Shipment createShipment(int itemCount) {
        Recipient recipient = new Recipient("Johnny", "Holiday", "Sunsetroad", "3", "AD2345", "Delft", "Netherlands");
        List<InventoryItem> items = IntStream.rangeClosed(1, itemCount)
                .boxed()
                .map(i -> new InventoryItem(1000 + i, i))
                .collect(Collectors.toList());
        return new Shipment(12345l, recipient, items);
    }
}
