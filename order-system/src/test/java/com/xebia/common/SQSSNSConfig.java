package com.xebia.common;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.Subscription;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.xebia.eda.configuration.LocalStackConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.converter.MessageConverter;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Configuration
@Profile("test")
@Import(LocalStackConfig.class)
public class SQSSNSConfig {

    public static final String HELLO_QUEUE = "hello-queue";
    public static final String HELLO_NOTIFICATION_QUEUE = "hello-notify-queue";
    public static final String HELLO_TOPIC = "hello-topic";

    @Autowired
    AmazonSQSAsync amazonSQS;

    @Autowired
    AmazonSNS amazonSns;

    @Bean
    public NotificationMessagingTemplate notificationMessagingTemplate(AmazonSNS amazonSns, MessageConverter converter) {
        return new NotificationMessagingTemplate(amazonSns, (ResourceIdResolver) null, converter);
    }

    @PostConstruct
    public void initEnvironment() {

        //SQS only queue
        amazonSQS.createQueue(HELLO_QUEUE);

        //SQS queue linked to SNS topic
        CreateQueueResult helloNotificationQueue = amazonSQS.createQueue(HELLO_NOTIFICATION_QUEUE);
        String helloQueueArn = amazonSQS.getQueueAttributes(helloNotificationQueue.getQueueUrl(), Arrays.asList("QueueArn")).getAttributes().get("QueueArn");

        CreateTopicResult helloTopic = amazonSns.createTopic(new CreateTopicRequest().withName(HELLO_TOPIC));
        List<Subscription> existingSubscriptions = amazonSns.listSubscriptions().getSubscriptions();

        // link topic to queue unless subscription is already existing
        if (existingSubscriptions.stream().noneMatch(s -> s.getEndpoint().equals(helloQueueArn))) {
            amazonSns.subscribe(helloTopic.getTopicArn(), "sqs", helloQueueArn);
        }
    }
}

