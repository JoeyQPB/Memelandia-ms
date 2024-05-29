package com.joey.categorymemelandia.utils;

import com.joey.categorymemelandia.records.CategoryServiceRequestUpdateDTO;

public class CategoryUtils {

    public static Boolean categoryServiceRequestUpdateDTOHasAnyFieldNullOrEmpty (CategoryServiceRequestUpdateDTO dto) {
        return dto == null ||
                dto.newCategoryName() == null || dto.newCategoryName().isEmpty() ||
                dto.oldCategoryName() == null || dto.oldCategoryName().isEmpty();
    }
}
