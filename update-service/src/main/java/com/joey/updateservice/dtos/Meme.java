package com.joey.updateservice.dtos;

import java.sql.Timestamp;

public record Meme (String id, String name, String memeUrl, String category, String createdBy, Timestamp created_at, Timestamp updated_at) {
}
