package com.xebia.eda.configuration;

import com.amazonaws.services.sns.AmazonSNS;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;

@Configuration
public class Sns {

    @Bean
    public NotificationMessagingTemplate notificationMessagingTemplate(AmazonSNS amazonSns, MessageConverter converter) {
        return new NotificationMessagingTemplate(amazonSns, (ResourceIdResolver) null, converter);
    }
}
