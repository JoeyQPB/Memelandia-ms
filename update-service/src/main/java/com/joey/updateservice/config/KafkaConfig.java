package com.joey.updateservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${kafka.application.topic.category}")
    private String categoryTopic;

    @Value("${kafka.application.topic.user}")
    private String userTopic;

    @Bean
    public NewTopic categoryTopic() {
        return TopicBuilder.name(categoryTopic).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic userTopic() {
        return TopicBuilder.name(userTopic).partitions(1).replicas(1).build();
    }
}
