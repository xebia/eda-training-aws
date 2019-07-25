package com.xebia.eda.configuration;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.retry.PredefinedRetryPolicies;
import com.amazonaws.retry.RetryPolicy;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsyncClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisAsyncClient;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseAsyncClient;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseAsyncClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import static com.amazonaws.SDKGlobalConfiguration.AWS_CBOR_DISABLE_SYSTEM_PROPERTY;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

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
    public AmazonSNSAsync amazonSNS(AWSCredentialsProvider awsCredentialsProvider) {
        return AmazonSNSAsyncClientBuilder.standard()
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
    @Primary
    public AmazonKinesis amazonKinesis(AWSCredentialsProvider awsCredentialsProvider) {
        System.setProperty(AWS_CBOR_DISABLE_SYSTEM_PROPERTY, "true"); // Disable CBOR
        return AmazonKinesisAsyncClient.asyncBuilder()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4568", "eu-west-1"))
                .withCredentials(awsCredentialsProvider)
                .build();
    }

    @Bean
    @Primary
    public AmazonDynamoDBAsync amazonDynamoDB(AWSCredentialsProvider awsCredentialsProvider) {
        return AmazonDynamoDBAsyncClient.asyncBuilder()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4569", "eu-west-1"))
                .withCredentials(awsCredentialsProvider)
                .build();
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new Jackson2ObjectMapperBuilder().build()
                .configure(WRITE_DATES_AS_TIMESTAMPS, false)
                .registerModule(new JavaTimeModule());
    }

    @Bean
    @Primary
    public AmazonKinesisFirehoseAsyncClient kinesisFirehoseClient(AWSCredentialsProvider awsCredentialsProvider) {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setMaxErrorRetry(FirehoseConstants.DEFAULT_MAX_RETRY_COUNT);
        clientConfiguration
                .setRetryPolicy(new RetryPolicy(PredefinedRetryPolicies.DEFAULT_RETRY_CONDITION,
                        PredefinedRetryPolicies.DEFAULT_BACKOFF_STRATEGY, FirehoseConstants.DEFAULT_MAX_RETRY_COUNT, true));
        clientConfiguration.setUserAgentPrefix(FirehoseConstants.USER_AGENT_STRING);

        return (AmazonKinesisFirehoseAsyncClient) AmazonKinesisFirehoseAsyncClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4573", "eu-west-1"))
                .withCredentials(awsCredentialsProvider)
                .withClientConfiguration(clientConfiguration).build();
    }

    @Bean
    @Primary
    public AmazonS3 s3ClientBuilder(AWSCredentialsProvider awsCredentialsProvider) {
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4572", "eu-west-1"))
                .withCredentials(awsCredentialsProvider)
                .build();
    }

}

class FirehoseConstants {
    public static final String USER_AGENT_STRING = "kinesis-logback-appender/1.3.0";
    public static final int DEFAULT_MAX_RETRY_COUNT = 3;

    public FirehoseConstants() {
    }

}

