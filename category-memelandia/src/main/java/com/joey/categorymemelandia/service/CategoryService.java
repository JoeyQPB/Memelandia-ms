package com.joey.categorymemelandia.service;

import com.joey.categorymemelandia.config.ApiGateway_Discovery;
import com.joey.categorymemelandia.domain.Category;
import com.joey.categorymemelandia.exceptions.BadGatewayException;
import com.joey.categorymemelandia.exceptions.CategoryNotFoundException;
import com.joey.categorymemelandia.exceptions.MissingFieldsException;
import com.joey.categorymemelandia.exceptions.UnableToCallUpdateService;
import com.joey.categorymemelandia.records.CategoryDTO;
import com.joey.categorymemelandia.records.CategoryServiceRequestUpdateDTO;
import com.joey.categorymemelandia.records.RestResponse;
import com.joey.categorymemelandia.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;
    private final ApiGateway_Discovery apiGatewayDiscovery;

    @Autowired
    public CategoryService (CategoryRepository categoryRepository, ApiGateway_Discovery apiGatewayDiscovery) {
        this.categoryRepository = categoryRepository;
        this.apiGatewayDiscovery = apiGatewayDiscovery;
    }

    public RestResponse<Category> create(CategoryDTO categoryDTO) {
        if (categoryDTO.name().isEmpty() || categoryDTO.description().isEmpty()) {
            throw new MissingFieldsException("Error: Fill the not null able fields");
        }

        Category categoryApp = new Category(categoryDTO);
        Category createdCategory = this.categoryRepository.save(categoryApp);
        LOGGER.info("Created new Category id: {}, at: {}", createdCategory.getId(), createdCategory.getCreated_at());

        return new RestResponse<>(HttpStatus.CREATED, createdCategory);
    }

    public RestResponse<Iterable<Category>> findAll() {
        Iterable<Category> categoryIterable = this.categoryRepository.findAll();
        LOGGER.info("Found all categories");
        return new RestResponse<>(HttpStatus.OK, categoryIterable);
    }

    public RestResponse<Category> findById(String id) {
        Category category = this.categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found for id: " + id));

        LOGGER.info("Found Category id: {}", category.getId());
        return new RestResponse<>(HttpStatus.OK, category);
    }

    public RestResponse<Category> findByName(String name) {
        Category category = this.categoryRepository.findByName(name)
                .orElseThrow(() -> new CategoryNotFoundException("User not found for name: " + name));

        LOGGER.info("Found Category name: {}", category.getName());
        return new RestResponse<>(HttpStatus.OK, category);
    }

    public RestResponse<Category> update(String id, CategoryDTO categoryDTO) throws CategoryNotFoundException {
        Category optionalCategory = this.categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found for id: " + id));

        if (categoryDTO.name().isEmpty() && categoryDTO.description().isEmpty()) {
            throw new MissingFieldsException("Error: there is no field to update");
        }

        String oldCategoryName = optionalCategory.getName();
        if (!categoryDTO.name().isEmpty()) optionalCategory.setName(categoryDTO.name());
        if (!categoryDTO.description().isEmpty()) optionalCategory.setDescription(categoryDTO.description());
        optionalCategory.markUpdated();

        LOGGER.info("Updated Category name: {}, description: {}", optionalCategory.getName(), optionalCategory.getDescription());

        Category categoryUpdated =  this.categoryRepository.save(optionalCategory);

        if (!categoryDTO.name().isEmpty()) {
            CategoryServiceRequestUpdateDTO categoryUpdateDto = new CategoryServiceRequestUpdateDTO(oldCategoryName, categoryDTO.name());
            Boolean isUpdateServiceCalled = this.apiGatewayDiscovery.updateCategoryPublisher(categoryUpdateDto);

            if (isUpdateServiceCalled == null) {
                LOGGER.error("Bad Gateway");
                throw new BadGatewayException("Error with Gateway!");
            }

            if (!isUpdateServiceCalled) {
                LOGGER.error("Error while calling kafka update service with: " + categoryUpdateDto);
                throw new UnableToCallUpdateService("Error while calling kafka update service with: " + categoryUpdateDto);
            }

            LOGGER.info("Called update service to user with: " + categoryUpdateDto);
        }

        return new RestResponse<>(HttpStatus.OK, categoryUpdated);
    }

    public RestResponse<Boolean> delete(String id) {
        Category optionalCategory = this.categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("User not found for id: " + id));

        this.categoryRepository.delete(optionalCategory);
        LOGGER.info("Deleted Category id: {}", id);
        return new RestResponse<>(HttpStatus.OK, true);
    }

}
