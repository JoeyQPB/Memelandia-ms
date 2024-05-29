package com.joey.usermemelandia.utils;

import com.joey.usermemelandia.records.UserServiceRequestUpdateDTO;

public class UserUtils {

    public static Boolean userServiceRequestUpdateDTOHasAnyFieldNullOrEmpty(UserServiceRequestUpdateDTO dto) {
        if (dto == null) return true;
        if (dto.newName() == null || dto.newName().isEmpty()) return true;
        if (dto.oldName() == null || dto.oldName().isEmpty()) return true;

        return false;
    }
}
