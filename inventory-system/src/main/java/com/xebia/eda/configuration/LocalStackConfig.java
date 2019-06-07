package com.xebia.eda.configuration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsyncClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisAsyncClient;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import static com.amazonaws.SDKGlobalConfiguration.AWS_CBOR_DISABLE_SYSTEM_PROPERTY;

@Configuration
@Profile("!prod")
public class LocalStackConfig {
    @Bean
    @Primary
    public AWSCredentialsProvider credentialsProvider(@Value("${cloud.aws.credentials.access-key}") String accessKey,
                                                      @Value("${cloud.aws.credentials.secret-key}") String secretKey) {
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
    }

    @Bean
    @Primary
    public AmazonCloudWatch amazonCloudWatch(AWSCredentialsProvider awsCredentialsProvider) {
        return AmazonCloudWatchAsyncClient.builder()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4582", "eu-west-1"))
                .withCredentials(awsCredentialsProvider)
                .build();
    }


    @Bean
    @Primary
    public AmazonSNS amazonSNS(AWSCredentialsProvider awsCredentialsProvider) {
        return AmazonSNSClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4575", "eu-west-1"))
                .withCredentials(awsCredentialsProvider)
                .build();
    }

    @Bean
    @Primary
    public AmazonSQSAsync amazonSQSAsync(AWSCredentialsProvider awsCredentialsProvider) {
        return AmazonSQSAsyncClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4576", "eu-west-1"))
                .withCredentials(awsCredentialsProvider)
                .build();
    }

    @Bean(destroyMethod = "shutdown")
    public AmazonKinesis amazonKinesis(AWSCredentialsProvider awsCredentialsProvider) {
        System.setProperty(AWS_CBOR_DISABLE_SYSTEM_PROPERTY, "true"); // Disable CBOR
        return AmazonKinesisAsyncClient.builder()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4568", "eu-west-1"))
                .withCredentials(awsCredentialsProvider)
                .build();
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB(AWSCredentialsProvider awsCredentialsProvider) {
        return AmazonDynamoDBAsyncClient.builder()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4569", "eu-west-1"))
                .withCredentials(awsCredentialsProvider)
                .build();
    }
}