package com.xebia.soa;

import com.xebia.common.service.InventoryRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
@ComponentScan(basePackages = {"com.xebia.common.*", "com.xebia.soa.*"})
@EntityScan("com.xebia.common.*")
@EnableJpaRepositories(basePackageClasses = {InventoryRepository.class})
@Configuration
@EnableScheduling
public class SoaInventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoaInventoryApplication.class, args);
    }

    @Bean
    public TaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler
                = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.setThreadNamePrefix(
                "ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }
}

