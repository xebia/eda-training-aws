package com.xebia;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xebia.common.domain.Address;
import com.xebia.common.domain.Customer;
import com.xebia.common.service.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@EntityScan
@EnableJpaRepositories
public class CRMApplication {

    public static void main(String[] args) {
        SpringApplication.run(CRMApplication.class, args);
    }

}

@Configuration
@Profile("!prod")
class LocalstackConfig {
    public AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(new AnonymousAWSCredentials());

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

@Component
class DataLoader implements ApplicationRunner {

    public static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);

    private CustomerRepository customerRepository;
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public DataLoader(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void run(ApplicationArguments args) throws Exception{
        List<Customer> customers = Arrays.asList(
                new Customer("John Doe", "abc@efg.nl", "0612345678", true, true, new Address("Sesamstraat", "5b", "3456AB", "Amsterdam", "Netherlands")),
                new Customer("Dave Burk", "dave@burk.nl", "0612345679", true, false, new Address("Beathovenlaan", "4", "4532AB", "Rotterdam", "Netherlands")),
                new Customer("Jane Swam", "jane@hij.nl", "0612345680", false, true, new Address("Sunset Avenue", "4", "4455EF", "Utrecht", "Netherlands")));
        java.util.List<Customer> loaded = customerRepository.saveAll(customers);
        LOGGER.info("Loaded " + customers.size() + " initial Customers:\n" + loaded.stream().map(i -> i.toString() + "\n").collect(Collectors.toList()));

        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        LOGGER.info("Loaded " + customers.size() + " initial Customers as json :\n" + mapper.writeValueAsString(customers));
    }
}
