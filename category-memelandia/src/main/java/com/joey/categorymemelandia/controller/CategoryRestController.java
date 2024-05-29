package com.joey.categorymemelandia.controller;

import com.joey.categorymemelandia.domain.Category;
import com.joey.categorymemelandia.exceptions.CategoryNotFoundException;
import com.joey.categorymemelandia.records.CategoryDTO;
import com.joey.categorymemelandia.records.RestResponse;
import com.joey.categorymemelandia.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
public class CategoryRestController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody CategoryDTO categoryDTO) {
        RestResponse<Category> responseService = this.categoryService.create(categoryDTO);
        return ResponseEntity.status(responseService.httpStatus()).body(responseService.body());
    }

    @GetMapping
    public ResponseEntity<Iterable<Category>> findAll() {
        RestResponse<Iterable<Category>> responseService = this.categoryService.findAll();
        return ResponseEntity.status(responseService.httpStatus()).body(responseService.body());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> findById(@PathVariable String id) throws CategoryNotFoundException {
        RestResponse<Category> responseService = this.categoryService.findById(id);
        return ResponseEntity.status(responseService.httpStatus()).body(responseService.body());
    }

    @GetMapping("/qe={name}")
    public ResponseEntity<Category> findByName(@PathVariable String name) throws CategoryNotFoundException {
        RestResponse<Category> responseService = this.categoryService.findByName(name);
        return ResponseEntity.status(responseService.httpStatus()).body(responseService.body());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> create(@PathVariable String id, @RequestBody CategoryDTO categoryDTO) throws CategoryNotFoundException {
        RestResponse<Category> responseService = this.categoryService.update(id, categoryDTO);
        return ResponseEntity.status(responseService.httpStatus()).body(responseService.body());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        RestResponse<Boolean> responseService = this.categoryService.delete(id);
        return ResponseEntity.status(responseService.httpStatus()).body(responseService.body());
    }

}
