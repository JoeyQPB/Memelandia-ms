package com.joey.categorymemelandia.service;

import com.joey.categorymemelandia.config.ApiGateway_Discovery;
import com.joey.categorymemelandia.domain.Category;
import com.joey.categorymemelandia.exceptions.CategoryNotFoundException;
import com.joey.categorymemelandia.exceptions.MissingFieldsException;
import com.joey.categorymemelandia.records.CategoryDTO;
import com.joey.categorymemelandia.records.RestResponse;
import com.joey.categorymemelandia.repository.CategoryRepository;
import org.apache.catalina.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ApiGateway_Discovery apiGatewayDiscovery;

    @Autowired
    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_HappyPath() {
        CategoryDTO catDTO = new CategoryDTO("category", "description");
        Category category = new Category(catDTO);

        when(this.categoryRepository.save(any())).thenReturn(category);

        RestResponse<Category> serviceResponse = this.categoryService.create(catDTO);
        HttpStatus serviceResponse_HttpStatus = serviceResponse.httpStatus();
        Category serviceResponse_Category = serviceResponse.body();

        verify(this.categoryRepository, times(1)).save(any());

        assertEquals(category, serviceResponse_Category);
        assertEquals(HttpStatus.CREATED, serviceResponse_HttpStatus);
        assertNotNull(serviceResponse_Category.getCreated_at());
        assertNotNull(serviceResponse_Category.getId());

        Timestamp now = new Timestamp(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        calendar.add(Calendar.MINUTE, 1);
        Timestamp futureTimestamp = new Timestamp(calendar.getTimeInMillis());
        calendar.add(Calendar.MINUTE, -2);
        Timestamp pastTimestamp = new Timestamp(calendar.getTimeInMillis());

        assertTrue(serviceResponse_Category.getCreated_at().before(futureTimestamp));
        assertTrue(serviceResponse_Category.getCreated_at().after(pastTimestamp));
    }

    @Test
    @DisplayName("Should Throw MissingFieldsException")
    void createCategoryTest_ThrowMissingFieldsException_WhenNameOrDescriptionIsNull() {

        Exception thrown = Assertions.assertThrows(MissingFieldsException.class, () -> {
            this.categoryService.create(new CategoryDTO("name", ""));
        });
        assertEquals("Error: Fill the not null able fields", thrown.getMessage());

        thrown = Assertions.assertThrows(MissingFieldsException.class, () -> {
            this.categoryService.create(new CategoryDTO("", "category"));
        });

        thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            this.categoryService.create(null);
        });
    }

    @Test
    void findAll_HappyPath() {
        List<Category> categoryArrayList = new ArrayList<>();

        when(this.categoryRepository.findAll()).thenReturn(categoryArrayList);

        RestResponse<Iterable<Category>> restResponse = this.categoryService.findAll();

        HttpStatus serviceResponse_HttpStatus = restResponse.httpStatus();
        ArrayList<Category> serviceResponse_categoryList = (ArrayList<Category>) restResponse.body();

        verify(this.categoryRepository, times(1)).findAll();

        assertEquals(categoryArrayList.toString(), serviceResponse_categoryList.toString());
        assertEquals(HttpStatus.OK, serviceResponse_HttpStatus);
        assertEquals(categoryArrayList.size(), serviceResponse_categoryList.size());


        // with 1 el
        CategoryDTO catDTO = new CategoryDTO("category", "description");
        Category category = new Category(catDTO);
        categoryArrayList.add(category);

        restResponse = this.categoryService.findAll();

        serviceResponse_HttpStatus = restResponse.httpStatus();
        categoryArrayList = (ArrayList<Category>) restResponse.body();

        verify(this.categoryRepository, times(2)).findAll();

        assertEquals(categoryArrayList.toString(), serviceResponse_categoryList.toString());
        assertEquals(HttpStatus.OK, serviceResponse_HttpStatus);
        assertEquals(categoryArrayList.size(), serviceResponse_categoryList.size());

        // with 2 el
        CategoryDTO catDTO_2 = new CategoryDTO("category", "description");
        Category category_2 = new Category(catDTO_2);
        categoryArrayList.add(category_2);

        restResponse = this.categoryService.findAll();

        serviceResponse_HttpStatus = restResponse.httpStatus();
        categoryArrayList = (ArrayList<Category>) restResponse.body();

        verify(this.categoryRepository, times(3)).findAll();

        assertEquals(categoryArrayList.toString(), serviceResponse_categoryList.toString());
        assertEquals(HttpStatus.OK, serviceResponse_HttpStatus);
        assertEquals(categoryArrayList.size(), serviceResponse_categoryList.size());
    }

    @Test
    void findById_HappyPath() {
        CategoryDTO catDTO = new CategoryDTO("category", "description");
        Category category = new Category(catDTO);

        when(this.categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        RestResponse<Category> serviceResponse = this.categoryService.findById(category.getId());

        HttpStatus serviceResponse_HttpStatus = serviceResponse.httpStatus();
        Category serviceResponse_Category = serviceResponse.body();

        verify(this.categoryRepository, times(1)).findById(category.getId());

        assertEquals(category, serviceResponse_Category);
        assertEquals(HttpStatus.OK, serviceResponse_HttpStatus);
    }

    @Test
    @DisplayName("Should throw CategoryNotFoundException When Id DoNotMatchOrNull")
    void findByIdTest_throwCategoryNotFoundException_WhenIdDoNotMatchOrNull() {
        Exception thrown = Assertions.assertThrows(NullPointerException.class, () -> {
            when(this.categoryRepository.findById("abc")).thenReturn(null);
            this.categoryService.findById("abc");
        });

        Assertions.assertThrows(RuntimeException.class, () -> {
            this.categoryService.findById(null);
        });
    }

    @Test
    void findByName_HappyPath() {
        CategoryDTO catDTO = new CategoryDTO("category", "description");
        Category category = new Category(catDTO);

        when(this.categoryRepository.findByName("category")).thenReturn(Optional.of(category));

        RestResponse<Category> serviceResponse = this.categoryService.findByName("category");

        HttpStatus serviceResponse_HttpStatus = serviceResponse.httpStatus();
        Category serviceResponse_Category = serviceResponse.body();

        verify(this.categoryRepository, times(1)).findByName("category");

        assertEquals(category, serviceResponse_Category);
        assertEquals(HttpStatus.OK, serviceResponse_HttpStatus);
    }

    @Test
    @DisplayName("Should throw CategoryNotFoundException When name DoNotMatchOrNull")
    void findByNameTest_throwCategoryNotFoundException_WhenNameDoNotMatchOrNull() {
        Exception thrown = Assertions.assertThrows(NullPointerException.class, () -> {
            when(this.categoryRepository.findByName("abc")).thenReturn(null);
            this.categoryService.findByName("abc");
        });
    }

    @Test
    void update_HappyPath() {
        CategoryDTO catDTO = new CategoryDTO("category", "description");
        Category category = new Category(catDTO);

        when(this.categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(this.apiGatewayDiscovery.updateCategoryPublisher(any())).thenReturn(true);

        category.setName("category update");
        category.setDescription("description update");
        category.markUpdated();

        when(this.categoryRepository.save(category)).thenReturn(category);

        RestResponse<Category> restResponse = this.categoryService.update(category.getId(), new CategoryDTO("category update", "description update"));

        HttpStatus categoryUpdated_Status = restResponse.httpStatus();
        Category categoryUpdated = restResponse.body();

        assertNotNull(categoryUpdated.getUpdated_at());
        assertEquals(HttpStatus.OK, categoryUpdated_Status);
        assertEquals("category update", categoryUpdated.getName());
        assertEquals("description update", categoryUpdated.getDescription());
    }

    @Test
    @DisplayName("Should throw CategoryNotFoundException When Id DoNotMatchOrNull")
    void updateTest_throwCategoryNotFoundException_WhenIdDoNotMatchOrNull() {
        Exception thrown = Assertions.assertThrows(NullPointerException.class, () -> {
            when(this.categoryRepository.findById("abc")).thenReturn(null);
            this.categoryService.update("abc", any());
        });

        thrown = Assertions.assertThrows(MissingFieldsException.class, () -> {
            when(this.categoryRepository.findById("abc")).thenReturn(Optional.of(new Category("name", "category")));
            this.categoryService.update("abc", new CategoryDTO("", ""));
        });
        assertEquals("Error: there is no field to update", thrown.getMessage());

        Assertions.assertThrows(RuntimeException.class, () -> {
            this.categoryService.update(null, any());
        });
    }

    @Test
    void delete_HappyPath() {
        CategoryDTO catDTO = new CategoryDTO("category", "description");
        Category category = new Category(catDTO);

        when(this.categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        RestResponse<Boolean> serviceResponse = this.categoryService.delete(category.getId());

        HttpStatus serviceResponse_HttpStatus = serviceResponse.httpStatus();
        Boolean serviceResponse_Category = serviceResponse.body();

        verify(this.categoryRepository, times(1)).delete(category);

        assertTrue(serviceResponse_Category);
        assertEquals(HttpStatus.OK, serviceResponse_HttpStatus);
    }

    @Test
    @DisplayName("Should throw CategoryNotFoundException When Id DoNotMatchOrNull")
    void deleteTest_throwCategoryNotFoundException_WhenIdDoNotMatchOrNull() {
        Exception thrown = Assertions.assertThrows(NullPointerException.class, () -> {
            when(this.categoryRepository.findById("abc")).thenReturn(null);
            this.categoryService.delete("abc");
        });
    }
}