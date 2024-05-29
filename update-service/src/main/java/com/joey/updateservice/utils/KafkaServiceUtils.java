package com.joey.updateservice.utils;

import com.joey.updateservice.dtos.CategoryServiceRequestDTO;
import com.joey.updateservice.dtos.UserServiceRequestDTO;

public class KafkaServiceUtils {

    public static Boolean isThereAnyFieldNullOrEmptyToUser(UserServiceRequestDTO dto) {
        return dto == null ||
                dto.getNewName() == null || dto.getNewName().isEmpty() ||
                dto.getOldName() == null || dto.getOldName().isEmpty();
    }

    public static Boolean isThereAnyFieldNullOrEmptyToCategory(CategoryServiceRequestDTO dto) {
        return dto == null ||
                dto.getOldCategoryName() == null || dto.getOldCategoryName().isEmpty() ||
                dto.getNewCategoryName() == null || dto.getNewCategoryName().isEmpty();
    }
}
