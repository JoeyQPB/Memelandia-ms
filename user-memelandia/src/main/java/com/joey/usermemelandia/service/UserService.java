package com.joey.usermemelandia.service;

import com.joey.usermemelandia.config.ApiGateway_Discovery;
import com.joey.usermemelandia.domain.User;
import com.joey.usermemelandia.exceptions.BadGatewayException;
import com.joey.usermemelandia.exceptions.MissingFieldsException;
import com.joey.usermemelandia.exceptions.UnableToCallUpdateService;
import com.joey.usermemelandia.records.RestResponse;
import com.joey.usermemelandia.records.UserDTO;
import com.joey.usermemelandia.exceptions.EntityNotFoundException;
import com.joey.usermemelandia.records.UserServiceRequestUpdateDTO;
import com.joey.usermemelandia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final ApiGateway_Discovery apiGatewayDiscovery;

    @Autowired
    public UserService(UserRepository userRepository, ApiGateway_Discovery apiGatewayDiscovery) {
        this.userRepository = userRepository;
        this.apiGatewayDiscovery = apiGatewayDiscovery;
    }

    public RestResponse<User> createUser(UserDTO userDTO) {
        if (userDTO.email().isEmpty() || userDTO.name().isEmpty()) {
            LOGGER.error("Error: Not null fields is null");
            throw new MissingFieldsException("Error: Fill the no null able fields");
        }

        User userApp = new User(userDTO);
        User createdUser = this.userRepository.save(userApp);
        LOGGER.info("Created new User id: {}, at: {}", createdUser.getId(), createdUser.getCreated_at());

        return new RestResponse<User>(HttpStatus.CREATED, createdUser);
    }

    public RestResponse<Iterable<User>> findAll() {
        Iterable<User> users = this.userRepository.findAll();
        LOGGER.info("Found all users");
        return new RestResponse<Iterable<User>>(HttpStatus.OK, users);
    }

    public RestResponse<User> findById(String id) {
        User user = this.userRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("User not found for id: " + id);
            return new EntityNotFoundException("User not found for id: " + id);
        });

        LOGGER.info("Found User id: {}", user.getId());

        return new RestResponse<User>(HttpStatus.OK, user);
    }

    public RestResponse<User> findByEmail(String email) {
        User user = this.userRepository.findByEmail(email).orElseThrow(() -> {
            LOGGER.error("User not found for email: " + email);
            return new EntityNotFoundException("User not found for email: " + email);
        });

        LOGGER.info("Found User email: {}", user.getEmail());

        return new RestResponse<User>(HttpStatus.OK, user);
    }

    public RestResponse<User> update(String id, UserDTO userDTO) {
        User optionalUser = this.userRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("User not found for id: " + id);
            return new EntityNotFoundException("User not found for id: " + id);
        });

        if (userDTO.email().isEmpty() && userDTO.name().isEmpty()) {
            LOGGER.error("Error: Missing fields to update");
            throw new MissingFieldsException("Error: Missing fields to update");
        }

        String oldName = optionalUser.getName();
        if (!userDTO.name().isEmpty()) optionalUser.setName(userDTO.name());
        if (!userDTO.email().isEmpty()) optionalUser.setEmail(userDTO.email());

        optionalUser.markUpdated();

        User userUpdated = userRepository.save(optionalUser);
        LOGGER.info("User updated: {}, name: {}, email: {}, at: {}", userUpdated.getId(), userUpdated.getName(), userUpdated.getEmail(), userUpdated.getUpdated_at());

        if (!userDTO.name().isEmpty()) {
            UserServiceRequestUpdateDTO updateServiceDTO = new UserServiceRequestUpdateDTO(oldName, userDTO.name());
            Boolean isUpdateServiceCalled = this.apiGatewayDiscovery.updateUserPublisher(updateServiceDTO);

            if (isUpdateServiceCalled == null) {
                LOGGER.error("Bad Gateway");
                throw new BadGatewayException("Error with Gateway!");
            }

            if (!isUpdateServiceCalled) {
                LOGGER.error("Error while calling kafka update service with: " + updateServiceDTO);
                throw new UnableToCallUpdateService("Error while calling kafka update service with: " + updateServiceDTO);
            }

            LOGGER.info("Called update service to user with: " + updateServiceDTO);
        }

        return new RestResponse<User>(HttpStatus.OK, userUpdated);
    }

    public RestResponse<Boolean> delete(String id) {
        User optionalUser = this.userRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("User not found for id: " + id);
            return new EntityNotFoundException("User not found for id: " + id);
        });

        userRepository.delete(optionalUser);
        LOGGER.info("Deleted User id: {}", id);

        return new RestResponse<Boolean>(HttpStatus.OK, true);
    }
}
