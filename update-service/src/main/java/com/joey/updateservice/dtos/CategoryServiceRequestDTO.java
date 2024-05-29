package com.joey.updateservice.dtos;

public class CategoryServiceRequestDTO {
    private String oldCategoryName;
    private String newCategoryName;

    public CategoryServiceRequestDTO (String oldCategoryName, String newCategoryName) {
        this.oldCategoryName = oldCategoryName;
        this.newCategoryName = newCategoryName;
    }

    public String getOldCategoryName() {
        return oldCategoryName;
    }

    public void setOldCategoryName(String oldCategoryName) {
        this.oldCategoryName = oldCategoryName;
    }

    public String getNewCategoryName() {
        return newCategoryName;
    }

    public void setNewCategoryName(String newCategoryName) {
        this.newCategoryName = newCategoryName;
    }
}
