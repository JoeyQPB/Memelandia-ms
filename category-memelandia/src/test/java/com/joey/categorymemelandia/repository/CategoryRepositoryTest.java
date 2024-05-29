package com.joey.categorymemelandia.repository;

import com.joey.categorymemelandia.domain.Category;
import com.joey.categorymemelandia.records.CategoryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest;
import org.springframework.data.cassandra.CassandraInvalidQueryException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataCassandraTest
@ActiveProfiles("test")
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void clearDB() {
        Iterable<Category> userIterable = this.categoryRepository.findAll();
        userIterable.forEach(user -> this.categoryRepository.delete(user));
    }

    @Test
    @DisplayName("should return an Category found by name")
    public void findByName_HappyPath() {
        CategoryDTO categoryDTO = new CategoryDTO("Category name", "description");
        Category category = createCategoryMethod(categoryDTO);

        Optional<Category> categoryFromDb = this.categoryRepository.findByName("Category name");

        assertEquals(category, categoryFromDb.get());
    }

    @Test
    @DisplayName("should return null when the category name didn't match")
    public void findByName_FindingNoOne() {
        Optional<Category> userFromDB = this.categoryRepository.findByName("Not a Category name");
        assertEquals(Optional.empty(), userFromDB);
    }

    @Test
    @DisplayName("findByName should throw CassandraInvalidQueryException for null name")
    public void findByName_WhenNameIsNull_ThrowsIllegalArgumentException() {
        assertThrows(CassandraInvalidQueryException.class, () -> {
            categoryRepository.findByName(null);
        }, "findByName should throw IllegalArgumentException for null name");
    }

    private Category createCategoryMethod(CategoryDTO categoryDTO) {
        Category category = new Category(categoryDTO);
        return this.categoryRepository.save(category);
    }
}