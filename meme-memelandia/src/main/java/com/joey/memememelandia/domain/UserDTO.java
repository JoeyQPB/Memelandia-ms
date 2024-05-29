package com.joey.memememelandia.domain;

import java.sql.Timestamp;

public record UserDTO (String id, String email, String name, Timestamp created_at, Timestamp updated_at) {
}
