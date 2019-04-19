package com.xebia.soa;

import com.xebia.common.service.ClaimRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = { "com.xebia.common.*", "com.xebia.soa.*" })
@EntityScan("com.xebia.common.*")
@EnableJpaRepositories(basePackageClasses = {ClaimRepository.class})

public class SoaInventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoaInventoryApplication.class, args);
    }

}

