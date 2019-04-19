package com.xebia.common;

import com.xebia.common.domain.ProductClaim;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DomainSamples {


    public static List<ProductClaim> createProductClaims(int lineCount) {
        return IntStream.rangeClosed(1, lineCount)
                .boxed()
                .map(i -> new ProductClaim(1000 + i,  i))
                .collect(Collectors.toList());
    }
}
