package com.joey.usermemelandia.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joey.usermemelandia.domain.User;
import com.joey.usermemelandia.records.RestResponse;
import com.joey.usermemelandia.records.UserDTO;
import com.joey.usermemelandia.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserRestController.class)
public class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create an user")
    void createUser_HappyPath() throws Exception {
        UserDTO userDTO = new UserDTO("John", "jhondoe@mail.com");
        User createdUser = new User(userDTO);
        RestResponse<User> responseService = new RestResponse<>(HttpStatus.CREATED, createdUser);
        when(userService.createUser(any(UserDTO.class))).thenReturn(responseService);

        ResultActions result = mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)));

        verify(this.userService, times(1)).createUser(any());

        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createdUser.getId()))
                .andExpect(jsonPath("$.email").value("jhondoe@mail.com"));
    }

    @Test
    @DisplayName("Should found all users")
    void getAllUsers_HappyPath() throws Exception {
        User user1 = new User(new UserDTO("John", "jhondoe@mail.com"));
        User user2 = new User(new UserDTO("Maria", "mariadoe@mail.com"));
        Iterable<User> users = List.of(user1, user2);
        RestResponse<Iterable<User>> responseService = new RestResponse<>(HttpStatus.OK, users);
        when(userService.findAll()).thenReturn(responseService);

        ResultActions result = mockMvc.perform(get("/api/user"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(user1.getId()))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[0].email").value("jhondoe@mail.com"))
                .andExpect(jsonPath("$[1].id").value(user2.getId()))
                .andExpect(jsonPath("$[1].name").value("Maria"))
                .andExpect(jsonPath("$[1].email").value("mariadoe@mail.com"));
    }

    @Test
    @DisplayName("Should found an users by id")
    void getUserById_HappyPath() throws Exception {
        User user = new User(new UserDTO("John", "jhondoe@mail.com"));
        RestResponse<User> responseService = new RestResponse<>(HttpStatus.OK, user);
        when(userService.findById(user.getId())).thenReturn(responseService);

        ResultActions result = mockMvc.perform(get("/api/user/" + user.getId()));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("jhondoe@mail.com"));
    }

    @Test
    @DisplayName("Should found an users by email")
    void getUserByEmail_HappyPath() throws Exception {
        User user = new User(new UserDTO("John", "jhondoe@mail.com"));
        RestResponse<User> responseService = new RestResponse<>(HttpStatus.OK, user);
        when(userService.findByEmail("jhondoe@mail.com")).thenReturn(responseService);

        ResultActions result = mockMvc.perform(get("/api/user/qe=jhondoe@mail.com"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("jhondoe@mail.com"));
    }

    @Test
    @DisplayName("Should update an users")
    void updateUser_HappyPath() throws Exception {
        UserDTO userDTO = new UserDTO("Updated John", "updatedjohn@example.com");
        User updatedUser = new User(new UserDTO("John", "jhondoe@mail.com"));
        updatedUser.setName(userDTO.name());
        updatedUser.setEmail(userDTO.email());
        RestResponse<User> responseService = new RestResponse<>(HttpStatus.OK, updatedUser);
        when(userService.update(updatedUser.getId(), userDTO)).thenReturn(responseService);

        ResultActions result = mockMvc.perform(put("/api/user/" + updatedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedUser.getId()))
                .andExpect(jsonPath("$.name").value("Updated John"))
                .andExpect(jsonPath("$.email").value("updatedjohn@example.com"));
    }

    @Test
    @DisplayName("Should delete an users by id")
    void delete_HappyPath() throws Exception {
        RestResponse<Boolean> responseService = new RestResponse<>(HttpStatus.OK, true);
        when(userService.delete("1")).thenReturn(responseService);

        ResultActions result = mockMvc.perform(delete("/api/user/1"));

        result.andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}