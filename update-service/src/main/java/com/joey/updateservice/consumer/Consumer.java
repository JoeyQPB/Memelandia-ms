package com.joey.updateservice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joey.updateservice.config.ApiGateway_Discovery;
import com.joey.updateservice.publisher.PublisherKafkaController;
import com.joey.updateservice.dtos.CategoryServiceRequestDTO;
import com.joey.updateservice.dtos.Meme;
import com.joey.updateservice.dtos.UserServiceRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Consumer {
    private final Logger LOGGER = LoggerFactory.getLogger(PublisherKafkaController.class);
    private final ApiGateway_Discovery apiGatewayDiscovery;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public Consumer (ApiGateway_Discovery apiGatewayDiscovery) {
        this.apiGatewayDiscovery = apiGatewayDiscovery;
    }

    @RetryableTopic
    @KafkaListener(topics = "${kafka.application.topic.user}", groupId = "${kafka.application.groupId.name}")
    public void updateUserConsumer (String requestJsonString) throws JsonProcessingException {
        LOGGER.info("[:] UPDATING MEMES WITH: " + requestJsonString);
        UserServiceRequestDTO requestDTO = objectMapper.readValue(requestJsonString, UserServiceRequestDTO.class);

        List<Meme> memesUpdate = new ArrayList<>();

        Iterable<Meme> memeList = this.apiGatewayDiscovery.getAllMemesByCreator(requestDTO.getOldName());

        for (Meme meme : memeList) {
            Meme memeUpdated = this.apiGatewayDiscovery.updateMemeCreator(meme.id(), requestDTO.getNewName());
            memesUpdate.add(memeUpdated);
        }

        showUpdates(memesUpdate);
    }

    @RetryableTopic
    @KafkaListener(topics = "${kafka.application.topic.category}", groupId = "${kafka.application.groupId.name}")
    public void updateCategoryConsumer (String requestJsonString) throws JsonProcessingException {
        LOGGER.info("[:] UPDATING MEMES WITH: " + requestJsonString);
        CategoryServiceRequestDTO requestDTO = objectMapper.readValue(requestJsonString, CategoryServiceRequestDTO.class);
        List<Meme> memesUpdate = new ArrayList<>();

        Iterable<Meme> memeList = this.apiGatewayDiscovery.getAllMemesByCategory(requestDTO.getOldCategoryName());

        for (Meme meme : memeList) {
            Meme memeUpdated = this.apiGatewayDiscovery.updateMemeCategory(meme.id(), requestDTO.getNewCategoryName());
            memesUpdate.add(memeUpdated);
        }

        showUpdates(memesUpdate);
    }

    public void showUpdates(List<Meme> memeArrayList) {
        LOGGER.info("[:] UPDATES MEMES");
        for (Meme meme : memeArrayList) {
            LOGGER.info("[:] meme: {}", meme.toString());
        }
    }
}
