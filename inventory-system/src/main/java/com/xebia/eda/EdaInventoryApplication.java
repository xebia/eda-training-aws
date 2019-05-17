package com.xebia.eda;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.xebia.common.service.InventoryRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = { "com.xebia.common.*", "com.xebia.eda.*" })
@EntityScan("com.xebia.common.*")
@EnableJpaRepositories(basePackageClasses = {InventoryRepository.class})
public class EdaInventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdaInventoryApplication.class, args);
    }

}

@Configuration
@Profile("!prod")
class LocalstackConfig {
    public AWSCredentialsProvider credentialsProvider  = new AWSStaticCredentialsProvider(new AnonymousAWSCredentials());

    @Bean
    public AmazonSNS amazonSNS() {
        return AmazonSNSClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4575", "us-east-1"))
                .withCredentials(credentialsProvider)
                .build();
    }

    @Bean
    public AmazonSQS amazonSQS() {
        return AmazonSQSAsyncClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4576", "us-east-1"))
                .withCredentials(credentialsProvider)
                .build();
    }
}
