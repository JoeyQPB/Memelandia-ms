package com.joey.usermemelandia.domain;

import com.joey.usermemelandia.records.UserDTO;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Table
public class User {

    @PrimaryKey
    private String id;

    @Indexed
    private String email;

    private String name;

    @CreatedDate
    private Timestamp created_at;

    @LastModifiedDate
    private Timestamp updated_at;

    public User() {
        this.id = UUID.randomUUID().toString();
        this.created_at = new Timestamp(System.currentTimeMillis());
    }

    public User(String email, String name) {
        this();
        this.email = email;
        this.name = name;
    }

    public User(UserDTO userDTO) {
        this();
        this.email = userDTO.email();
        this.name = userDTO.name();
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void markUpdated() {
        this.updated_at = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
