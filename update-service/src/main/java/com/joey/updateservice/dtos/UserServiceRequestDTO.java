package com.joey.updateservice.dtos;

import com.fasterxml.jackson.databind.ser.std.SerializableSerializer;

public class UserServiceRequestDTO extends SerializableSerializer {
    private String oldName;
    private String newName;

    public UserServiceRequestDTO (String oldName, String newName) {
        this.newName = newName;
        this.oldName = oldName;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
