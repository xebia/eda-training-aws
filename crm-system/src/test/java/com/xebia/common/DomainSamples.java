package com.xebia.common;

import com.xebia.common.domain.Address;
import com.xebia.common.domain.Customer;

public class DomainSamples {

    public static Customer CUSTOMER_JOE = new Customer("John Doe", "abc@efg.nl", "0612345678", true, true, new Address("Sesamstraat", "5b", "3456AB", "Amsterdam", "Netherlands"));
    public static Customer CUSTOMER_JANE = new Customer("Jane Smith", "dave@burk.nl", "0612345679", true, false, new Address("Beathovenlaan", "4", "4532AB", "Rotterdam", "Netherlands"));


}
