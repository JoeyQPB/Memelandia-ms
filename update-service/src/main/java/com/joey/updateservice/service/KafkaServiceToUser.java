package com.joey.updateservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joey.updateservice.exceptions.MissingFieldsException;
import com.joey.updateservice.dtos.UserServiceRequestDTO;
import com.joey.updateservice.utils.KafkaServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaServiceToUser {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${kafka.application.topic.user}")
    private String userTopic;

    private final Logger LOGGER = LoggerFactory.getLogger(KafkaServiceToCategory.class);

    public void updateMemeForUserService(UserServiceRequestDTO requestDTO) {
        try {
            LOGGER.info("============== UPDATE FOR USER SERVICE START ==============");
            LOGGER.info("[:] Request: " + requestDTO);

            if (KafkaServiceUtils.isThereAnyFieldNullOrEmptyToUser(requestDTO)) {
                throw new MissingFieldsException("The request is missing fields not null able!");
            }
            String content = objectMapper.writeValueAsString(requestDTO);
            kafkaTemplate.send(userTopic, content);

            LOGGER.info("============== UPDATE FOR USER SERVICE END ==============");
        } catch (Exception exception) {
            LOGGER.error("[ERROR] /update_user error: " + exception.getMessage() + " - - - Request: " + requestDTO);
        }
    }
}
