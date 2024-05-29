package com.joey.memememelandia.domain;

import com.joey.memememelandia.records.MemeDTO;
import com.joey.memememelandia.records.MemeDtoRequest;
import com.joey.memememelandia.records.OnlyMemeDTO;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Table
public class Meme {

    @PrimaryKey
    private String id;

    @Indexed
    private String name;

    private String memeUrl;

    private String category;

    private String createdBy;

    @CreatedDate
    private Timestamp created_at;

    @LastModifiedDate
    private Timestamp updated_at;

    public Meme() {
        this.id = UUID.randomUUID().toString();
        this.created_at = new Timestamp(System.currentTimeMillis());
    }

    public Meme(MemeDTO memeDTO) {
        this();
        this.name = memeDTO.name();
        this.memeUrl = memeDTO.memeUrl();
        this.category = memeDTO.category();
        this.createdBy = memeDTO.createdBy();
    }

    public Meme(OnlyMemeDTO onlyMemeDTO, String category, String createdBy) {
        this();
        this.name = onlyMemeDTO.name();
        this.memeUrl = onlyMemeDTO.memeUrl();
        this.category = category;
        this.createdBy = createdBy;
    }

    public Meme(String name, String memeUrl, String category, String createdBy) {
        this();
        this.name = name;
        this.memeUrl = memeUrl;
        this.category = category;
        this.createdBy = createdBy;
    }

    public Meme(MemeDtoRequest memeDtoRequest, String createdBy) {
        this();
        this.name = memeDtoRequest.name();
        this.memeUrl = memeDtoRequest.memeUrl();
        this.category = memeDtoRequest.category();
        this.createdBy = createdBy;
    }

    public void markUpdated() {
        this.updated_at = new Timestamp(System.currentTimeMillis());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemeUrl() {
        return memeUrl;
    }

    public void setMemeUrl(String memeUrl) {
        this.memeUrl = memeUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String created_by) {
        this.createdBy = created_by;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meme meme = (Meme) o;
        return Objects.equals(id, meme.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Meme{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", memeUrl='" + memeUrl + '\'' +
                ", category='" + category + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
