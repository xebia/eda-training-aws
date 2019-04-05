package com.xebia.soa;

import com.xebia.common.order.OrderRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = { "com.xebia.common.order", "com.xebia.soa.*" })
@EntityScan("com.xebia.common.order")
@EnableJpaRepositories(basePackageClasses = {OrderRepository.class})
public class SoaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoaApplication.class, args);
    }

}

