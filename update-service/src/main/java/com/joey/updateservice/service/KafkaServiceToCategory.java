package com.joey.updateservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joey.updateservice.exceptions.MissingFieldsException;
import com.joey.updateservice.dtos.CategoryServiceRequestDTO;
import com.joey.updateservice.utils.KafkaServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaServiceToCategory {

    @Autowired
    private KafkaTemplate<String ,String> kafkaTemplate;

    @Value("${kafka.application.topic.category}")
    private String categoryTopic;

    @Autowired
    private ObjectMapper objectMapper;

    private final Logger LOGGER = LoggerFactory.getLogger(KafkaServiceToCategory.class);

    public void updateForCategoryService(CategoryServiceRequestDTO requestDTO) {
        try {
            LOGGER.info("============== UPDATE FOR CATEGORY SERVICE START ==============");
            LOGGER.info("[:] Request: " + requestDTO);

            if (KafkaServiceUtils.isThereAnyFieldNullOrEmptyToCategory(requestDTO)) {
                throw new MissingFieldsException("The request is missing fields not null able!");
            }

            String content = objectMapper.writeValueAsString(requestDTO);
            kafkaTemplate.send(categoryTopic, content);

            LOGGER.info("============== UPDATE FOR CATEGORY SERVICE END ==============");
        } catch (Exception exception) {
            LOGGER.error("[ERROR] /update_user error: " + exception.getMessage() + " - - - Request: " + requestDTO);
        }
    }
}
