package com.xebia.soa;

import com.xebia.common.service.CustomerRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = { "com.xebia.common.*", "com.xebia.soa.*" })
@EntityScan("com.xebia.common.*")
@EnableJpaRepositories(basePackageClasses = {CustomerRepository.class})
public class SoaCustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoaCustomerApplication.class, args);
    }

}

