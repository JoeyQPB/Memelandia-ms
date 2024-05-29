package com.joey.memememelandia.service;

import com.joey.memememelandia.Utils.MemeUtils;
import com.joey.memememelandia.config.API_Gateway_Discovery;
import com.joey.memememelandia.domain.CategoryDTO;
import com.joey.memememelandia.domain.Meme;
import com.joey.memememelandia.domain.UserDTO;
import com.joey.memememelandia.exceptions.CategoryNotFoundException;
import com.joey.memememelandia.exceptions.MemeNotFoundException;
import com.joey.memememelandia.exceptions.MissingFieldsException;
import com.joey.memememelandia.exceptions.UserNotFoundException;
import com.joey.memememelandia.records.MemeDtoRequest;
import com.joey.memememelandia.records.OnlyMemeDTO;
import com.joey.memememelandia.records.RestResponse;
import com.joey.memememelandia.repository.MemeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class MemeService {

    private final Logger LOGGER = LoggerFactory.getLogger(MemeService.class);
    private final MemeRepository memeRepository;
    private final API_Gateway_Discovery apiGatewayDiscovery;

    @Autowired
    public MemeService(MemeRepository memeRepository, API_Gateway_Discovery apiGatewayDiscovery) {
        this.memeRepository = memeRepository;
        this.apiGatewayDiscovery = apiGatewayDiscovery;
    }

    public RestResponse<Meme> create(MemeDtoRequest memeDtoRequest) {
        if (memeDtoRequest == null) throw new NullPointerException("The variable(s) can´t be null!");

        if (MemeUtils.isThereAnyMemeDTOFieldEmpty(memeDtoRequest)) {
            throw new MissingFieldsException("Missing field not null able!");
        }

        UserDTO userDTO = this.apiGatewayDiscovery.getUserByEmailFromUserService(memeDtoRequest.creatorEmail());
        if (userDTO == null || userDTO.name() == null || userDTO.name().isEmpty()) {
            throw new UserNotFoundException("User not found for email: " + memeDtoRequest.creatorEmail());
        }

        CategoryDTO categoryDTO = this.apiGatewayDiscovery.getCategoryByNameFromCategoryService(memeDtoRequest.category());
        if (categoryDTO == null || categoryDTO.name() == null || categoryDTO.name().isEmpty()) {
            throw new CategoryNotFoundException("Category not found for name: " + memeDtoRequest.category());
        }

        Meme memeApp = new Meme(memeDtoRequest, userDTO.name());
        Meme memeCreated = this.memeRepository.save(memeApp);

        LOGGER.info("Created new Meme ({}): name: {}, at: {}",
                memeCreated.getId(), memeCreated.getName(), memeCreated.getCreated_at());

        return new RestResponse<>(HttpStatus.CREATED, memeCreated);
    }

    public RestResponse<Meme> create(String userId, String categoryId, OnlyMemeDTO onlyMemeDTO) {
        if (onlyMemeDTO == null || userId == null || categoryId == null) {
            throw new NullPointerException("The variable(s) can´t be null!");
        }

        if (MemeUtils.isThereAnyOnlyMemeDTOFieldEmpty(onlyMemeDTO)) {
            throw new MissingFieldsException("Missing field not null able!");
        }

        UserDTO userDTO = this.apiGatewayDiscovery.getUserByIdFromUserService(userId);
        if (userDTO == null || userDTO.name() == null || userDTO.name().isEmpty()) {
            throw new UserNotFoundException("User not found for id: " + userId);
        }

        CategoryDTO categoryDTO = this.apiGatewayDiscovery.getCategoryByIdFromCategoryService(categoryId);
        if (categoryDTO == null || categoryDTO.name() == null || categoryDTO.name().isEmpty()) {
            throw new CategoryNotFoundException("Category not found for id: " + categoryId);
        }

        Meme memeApp = new Meme(onlyMemeDTO, categoryDTO.name(), userDTO.name());
        Meme memeCreated = this.memeRepository.save(memeApp);

        LOGGER.info("Created new Meme ({}): name: {}, at: {}",
                memeCreated.getId(), memeCreated.getName(), memeCreated.getCreated_at());

        return new RestResponse<>(HttpStatus.CREATED, memeCreated);
    }

    public RestResponse<Iterable<Meme>> findAll() {
        Iterable<Meme> memeIterable = this.memeRepository.findAll();
        LOGGER.info("Found all memes");
        return new RestResponse<>(HttpStatus.OK, memeIterable);
    }

    public RestResponse<Meme> findById(String id) {
        if (id == null) throw new NullPointerException("The variable(s) can´t be null!");

        Meme meme = this.memeRepository.findById(id)
                .orElseThrow(() -> new MemeNotFoundException("Meme not found for id: " + id));

        LOGGER.info("Found Meme id: {}", meme.getId());
        return new RestResponse<>(HttpStatus.OK, meme);
    }

    public RestResponse<Meme> findByName(String name) {
        if (name == null) throw new NullPointerException("The variable(s) can´t be null!");

        Meme meme = this.memeRepository.findByName(name)
                .orElseThrow(() -> new MemeNotFoundException("Meme not found for name: " + name));

        LOGGER.info("Found Meme, name: {}", meme.getName());
        return new RestResponse<>(HttpStatus.OK, meme);
    }

    public RestResponse<Iterable<Meme>> findAllByCategory(String category) {
        Iterable<Meme> memeIterable = this.memeRepository.findAllByCategory(category);
        LOGGER.info("Found all memes BY CATEGORY!");
        return new RestResponse<>(HttpStatus.OK, memeIterable);
    }

    public RestResponse<Iterable<Meme>> findAllByCreatedBy(String createdBy) {
        Iterable<Meme> memeIterable = this.memeRepository.findAllByCreatedBy(createdBy);
        LOGGER.info("Found all memes BY CREATOR!");
        return new RestResponse<>(HttpStatus.OK, memeIterable);
    }

    public RestResponse<Meme> update(String id, OnlyMemeDTO onlyMemeDTO) {
        if (id == null || onlyMemeDTO == null) {
            throw new NullPointerException("The variable(s) can´t be null!");
        }

        if (MemeUtils.isAllOnlyMemeDTOFieldEmpty(onlyMemeDTO)) {
            throw new MissingFieldsException("Missing field to update!");
        }

        Meme memeFromDb = this.memeRepository.findById(id)
                .orElseThrow(() -> new MemeNotFoundException("Meme not found for id: " + id));

        MemeUtils.updateMeme(memeFromDb, onlyMemeDTO);

        Meme memeUpdated = this.memeRepository.save(memeFromDb);
        LOGGER.info("Updated Meme ({}): name: {}, meme Url: {}",
                memeUpdated.getId(), memeUpdated.getName(), memeUpdated.getMemeUrl());

        return new RestResponse<>(HttpStatus.OK, memeUpdated);
    }

    public RestResponse<Meme> updateCreator(String id, String creatorName) {
        if (id == null || creatorName == null) {
            throw new NullPointerException("The variable(s) can´t be null!");
        }

        if (creatorName.isEmpty()) {
            throw new MissingFieldsException("Missing field to update!");
        }

        Meme memeFromDb = this.memeRepository.findById(id)
                .orElseThrow(() -> new MemeNotFoundException("Meme not found for id: " + id));
        memeFromDb.setCreatedBy(creatorName);
        Meme memeUpdated = this.memeRepository.save(memeFromDb);

        LOGGER.info("Updated Meme ({}): name: {}", memeUpdated.getId(), memeUpdated.getName());

        return new RestResponse<>(HttpStatus.OK, memeUpdated);
    };

    public RestResponse<Meme> updateCategory(String id, String categoryName) {
        if (id == null || categoryName == null) {
            throw new NullPointerException("The variable(s) can´t be null!");
        }

        if (categoryName.isEmpty()) {
            throw new MissingFieldsException("Missing field to update!");
        }

        Meme memeFromDb = this.memeRepository.findById(id)
                .orElseThrow(() -> new MemeNotFoundException("Meme not found for id: " + id));
        memeFromDb.setCategory(categoryName);
        Meme memeUpdated = this.memeRepository.save(memeFromDb);

        LOGGER.info("Updated Meme ({}): category: {}", memeUpdated.getId(), memeUpdated.getCategory());

        return new RestResponse<>(HttpStatus.OK, memeUpdated);
    };

    public RestResponse<Boolean> delete(String id) {
        if (id == null) throw new NullPointerException("The variable can´t be null!");

        Meme meme = this.memeRepository.findById(id)
                .orElseThrow(() -> new MemeNotFoundException("Meme not found for id: " + id));

        this.memeRepository.delete(meme);
        return new RestResponse<>(HttpStatus.OK, true);
    }

    public RestResponse<Meme> memeOfTheDay() {
        List<Meme> memeList = (List<Meme>) this.memeRepository.findAll();
        Meme meme = memeList.get(new Random().nextInt(memeList.size()));
        LOGGER.info("Meme of the Day: {}", meme);
        return new RestResponse<>(HttpStatus.OK, meme);
    }

}
