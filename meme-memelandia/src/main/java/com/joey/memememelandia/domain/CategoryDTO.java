package com.joey.memememelandia.domain;

import java.sql.Timestamp;

public record CategoryDTO (String id, String name, String description, Timestamp created_at, Timestamp updated_at) {
}
