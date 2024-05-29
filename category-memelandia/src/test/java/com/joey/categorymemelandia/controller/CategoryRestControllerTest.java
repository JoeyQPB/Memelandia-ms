package com.joey.categorymemelandia.controller;

import com.joey.categorymemelandia.domain.Category;
import com.joey.categorymemelandia.records.CategoryDTO;
import com.joey.categorymemelandia.records.RestResponse;
import com.joey.categorymemelandia.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryRestController.class)
class CategoryRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create a category")
    void createTest() throws Exception {
        CategoryDTO catDTO = new CategoryDTO("category", "description");
        Category category = new Category(catDTO);

        RestResponse<Category> responseService = new RestResponse<>(HttpStatus.CREATED, category);

        when(this.categoryService.create(any())).thenReturn(responseService);

        ResultActions result = mockMvc.perform(post("/api/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(catDTO)));

        verify(this.categoryService, times(1)).create(any());

        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(category.getId()))
                .andExpect(jsonPath("$.name").value(category.getName()))
                .andExpect(jsonPath("$.description").value(category.getDescription()));
    }

    @Test
    void findAllTest() throws Exception {
        Category category = new Category(new CategoryDTO("category", "description"));
        Category category2 = new Category(new CategoryDTO("category 2", "description 2"));
        Iterable<Category> users = List.of(category, category2);

        RestResponse<Iterable<Category>> responseService = new RestResponse<>(HttpStatus.OK, users);

        when(this.categoryService.findAll()).thenReturn(responseService);

        ResultActions result = mockMvc.perform(get("/api/category"));

        verify(this.categoryService, times(1)).findAll();

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(category.getId()))
                .andExpect(jsonPath("$[0].name").value("category"))
                .andExpect(jsonPath("$[0].description").value("description"))
                .andExpect(jsonPath("$[1].id").value(category2.getId()))
                .andExpect(jsonPath("$[1].name").value("category 2"))
                .andExpect(jsonPath("$[1].description").value("description 2"));
    }

    @Test
    void findByIdTest() throws Exception {
        Category category = new Category(new CategoryDTO("category", "description"));

        RestResponse<Category> responseService = new RestResponse<>(HttpStatus.OK, category);

        when(this.categoryService.findById(any())).thenReturn(responseService);

        ResultActions result = mockMvc.perform(get("/api/category/" + category.getId()));

        verify(this.categoryService, times(1)).findById(category.getId());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(category.getId()))
                .andExpect(jsonPath("$.name").value("category"))
                .andExpect(jsonPath("$.description").value("description"));
    }

    @Test
    void findByNameTest() throws Exception {
        Category category = new Category(new CategoryDTO("category", "description"));

        RestResponse<Category> responseService = new RestResponse<>(HttpStatus.OK, category);

        when(this.categoryService.findByName(any())).thenReturn(responseService);

        ResultActions result = mockMvc.perform(get("/api/category/qe=category"));

        verify(this.categoryService, times(1)).findByName(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(category.getId()))
                .andExpect(jsonPath("$.name").value("category"))
                .andExpect(jsonPath("$.description").value("description"));
    }

    @Test
    void updateTest() throws Exception {
        CategoryDTO catDto = new CategoryDTO("category", "description");
        Category category = new Category(catDto);
        CategoryDTO categoryDTO_to_update = new CategoryDTO("up","date");

        category.setName("up");
        category.setDescription("date");
        RestResponse<Category> responseService = new RestResponse<>(HttpStatus.OK, category);

        when(this.categoryService.update(category.getId(), categoryDTO_to_update)).thenReturn(responseService);

        ResultActions result = mockMvc.perform(put("/api/category/" + category.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDTO_to_update)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(category.getId()))
                .andExpect(jsonPath("$.name").value("up"))
                .andExpect(jsonPath("$.description").value("date"));
    }

    @Test
    void deleteTest() throws Exception {
        RestResponse<Boolean> responseService = new RestResponse<>(HttpStatus.OK, true);

        when(this.categoryService.delete("1")).thenReturn(responseService);

        ResultActions result = mockMvc.perform(delete("/api/category/1"));

        verify(this.categoryService, times(1)).delete("1");

        result.andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}