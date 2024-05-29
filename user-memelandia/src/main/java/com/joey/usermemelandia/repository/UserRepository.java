package com.joey.usermemelandia.repository;

import com.joey.usermemelandia.domain.User;
import com.joey.usermemelandia.exceptions.EntityNotFoundException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findByEmail(String email) throws EntityNotFoundException;
}

