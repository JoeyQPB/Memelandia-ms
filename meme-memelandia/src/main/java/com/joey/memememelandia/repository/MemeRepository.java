package com.joey.memememelandia.repository;

import com.joey.memememelandia.domain.Meme;
import com.joey.memememelandia.exceptions.MemeNotFoundException;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemeRepository extends CrudRepository<Meme, String> {
    Optional<Meme> findByName(String name) throws MemeNotFoundException;
    @Query(allowFiltering = true)
    Iterable<Meme> findAllByCategory(String category);
    @Query(allowFiltering = true)
    Iterable<Meme> findAllByCreatedBy(String createdBy);
}
