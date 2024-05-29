package com.joey.updateservice.publisher;

import com.joey.updateservice.dtos.CategoryServiceRequestDTO;
import com.joey.updateservice.dtos.UserServiceRequestDTO;
import com.joey.updateservice.service.KafkaServiceToCategory;
import com.joey.updateservice.service.KafkaServiceToUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka_service/update")
public class PublisherKafkaController {

    private final Logger LOGGER = LoggerFactory.getLogger(PublisherKafkaController.class);
    private final KafkaServiceToUser kafkaServiceToUser;
    private final KafkaServiceToCategory kafkaServiceToCategory;

    @Autowired
    public PublisherKafkaController (KafkaServiceToUser kafkaServiceToUser,
                                     KafkaServiceToCategory kafkaServiceToCategory) {
        this.kafkaServiceToUser = kafkaServiceToUser;
        this.kafkaServiceToCategory = kafkaServiceToCategory;
    }

    @PostMapping("/user")
    public Boolean updateUserPublisher (@RequestBody UserServiceRequestDTO requestDTO) {
        try {
            LOGGER.info("[:] /update_user was called with: " + requestDTO);
            this.kafkaServiceToUser.updateMemeForUserService(requestDTO);
            return true;
        } catch (Exception e) {
            LOGGER.error("[ERROR] /update_user error: " + e.getMessage() + " - - - Request: " + requestDTO);
            return false;
        }
    }

    @PostMapping("/category")
    public Boolean updateCategoryPublisher (@RequestBody CategoryServiceRequestDTO requestDTO) {
        try {
            LOGGER.info("[:] /update_category was called with: " + requestDTO);
            this.kafkaServiceToCategory.updateForCategoryService(requestDTO);
            return true;
        } catch (Exception e) {
            LOGGER.error("[ERROR] /update_category error: " + e.getMessage() + " - - - Request: " + requestDTO);
            return false;
        }
    }
}
