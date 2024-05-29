package com.joey.categorymemelandia.repository;

import com.joey.categorymemelandia.domain.Category;
import com.joey.categorymemelandia.exceptions.CategoryNotFoundException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<Category, String> {
    Optional<Category> findByName(String name) throws CategoryNotFoundException;
}
