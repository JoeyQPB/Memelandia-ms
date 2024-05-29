package com.joey.usermemelandia.service;

import com.joey.usermemelandia.config.ApiGateway_Discovery;
import com.joey.usermemelandia.records.RestResponse;
import com.joey.usermemelandia.records.UserDTO;
import com.joey.usermemelandia.domain.User;
import com.joey.usermemelandia.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApiGateway_Discovery apiGatewayDiscovery;

    @Autowired
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create a new user")
    void createUserTest_HappyPath() {
        UserDTO userDTO = new UserDTO("Jhon Doe", "jhondoe@email.com");
        User user = new User(userDTO);

        when(this.userRepository.save(any())).thenReturn(user);

        RestResponse<User> restResponse = this.userService.createUser(userDTO);

        HttpStatus userCreated_Status = restResponse.httpStatus();
        User userCreated = restResponse.body();

        verify(this.userRepository, times(1)).save(any());

        assertEquals(user, userCreated);
        assertEquals(HttpStatus.CREATED, userCreated_Status);
        assertNotNull(userCreated.getCreated_at());
        assertNotNull(userCreated.getId());

        Timestamp now = new Timestamp(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        calendar.add(Calendar.MINUTE, 1);
        Timestamp futureTimestamp = new Timestamp(calendar.getTimeInMillis());
        calendar.add(Calendar.MINUTE, -2);
        Timestamp pastTimestamp = new Timestamp(calendar.getTimeInMillis());

        assertTrue(userCreated.getCreated_at().before(futureTimestamp));
        assertTrue(userCreated.getCreated_at().after(pastTimestamp));
    }

    @Test
    @DisplayName("Should Throw MissingFieldsException")
    void createUserTest_ThrowMissingFieldsException_WhenNameOrEmailIsNull() {

        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            this.userService.createUser(new UserDTO("Jhon Doe", ""));
        });
        assertEquals("Error: Fill the no null able fields", thrown.getMessage());

        thrown = Assertions.assertThrows(Exception.class, () -> {
            this.userService.createUser(new UserDTO("", "jhondoe@email.com"));
        });
        assertEquals("Error: Fill the no null able fields", thrown.getMessage());

        thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            this.userService.createUser(null);
        });
    }

    @Test
    @DisplayName("Should return all users on the data base")
    void findAllTest_HappyPath() {
        // with no el
        List<User> userArrayList = new ArrayList<>();

        when(this.userRepository.findAll()).thenReturn(userArrayList);

        RestResponse<Iterable<User>> restResponse = this.userService.findAll();

        HttpStatus userList_Status = restResponse.httpStatus();
        ArrayList<User> userList = (ArrayList<User>) restResponse.body();

        verify(this.userRepository, times(1)).findAll();

        assertEquals(userArrayList.toString(), userList.toString());
        assertEquals(HttpStatus.OK, userList_Status);
        assertEquals(userArrayList.size(), userList.size());

        // with 1 el
        UserDTO userDTO = new UserDTO("Jhon Doe", "jhondoe@email.com");
        User user = new User(userDTO);
        userArrayList.add(user);

        when(this.userRepository.findAll()).thenReturn(userArrayList);

        restResponse = this.userService.findAll();

        userList_Status = restResponse.httpStatus();
        userList = (ArrayList<User>) restResponse.body();

        verify(this.userRepository, times(2)).findAll();

        assertEquals(userArrayList.toString(), userList.toString());
        assertEquals(HttpStatus.OK, userList_Status);
        assertEquals(userArrayList.size(), userList.size());

        // with 2 el
        UserDTO userDTO_2 = new UserDTO("Jhon Doe 2", "jhondoe2@email.com");
        User user_2 = new User(userDTO_2);
        userArrayList.add(user_2);

        when(this.userRepository.findAll()).thenReturn(userArrayList);

        restResponse = this.userService.findAll();

        userList_Status = restResponse.httpStatus();
        userList = (ArrayList<User>) restResponse.body();

        verify(this.userRepository, times(3)).findAll();

        assertEquals(userArrayList.toString(), userList.toString());
        assertEquals(HttpStatus.OK, userList_Status);
        assertEquals(userArrayList.size(), userList.size());
    }

    @Test
    @DisplayName("Should return an user by id")
    void findByIdTest_HappyPath() {
        UserDTO userDTO = new UserDTO("Jhon Doe", "jhondoe@email.com");
        User user = new User(userDTO);

        when(this.userRepository.findById(any())).thenReturn(Optional.of(user));

        RestResponse<User> restResponse = this.userService.findById(any());

        HttpStatus userFound_Status = restResponse.httpStatus();
        User userFound = restResponse.body();

        verify(this.userRepository, times(1)).findById(any());

        assertEquals(user, userFound);
        assertEquals(HttpStatus.OK, userFound_Status);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException When Id DoNotMatchOrNull")
    void findByIdTest_throwEntityNotFoundException_WhenIdDoNotMatchOrNull() {
        Exception thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            when(this.userRepository.findById("abc")).thenReturn(null);
            this.userService.findById("abc");
        });

        thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            this.userService.findById(null);
        });
    }

    @Test
    @DisplayName("Should return an user by email")
    void findByEmailTest_HappyPath() {
        UserDTO userDTO = new UserDTO("Jhon Doe", "jhondoe@email.com");
        User user = new User(userDTO);

        when(this.userRepository.findByEmail("jhondoe@email.com")).thenReturn(Optional.of(user));

        RestResponse<User> restResponse = this.userService.findByEmail("jhondoe@email.com");

        HttpStatus userFound_Status = restResponse.httpStatus();
        User userFound = restResponse.body();

        verify(this.userRepository, times(1)).findByEmail("jhondoe@email.com");

        assertEquals(user, userFound);
        assertEquals(HttpStatus.OK, userFound_Status);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException When Email DoNotMatchOrNull")
    void findByEmailTest_throwEntityNotFoundException_WhenEmailDoNotMatchOrNull() {
        Exception thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            when(this.userRepository.findByEmail("abc")).thenReturn(null);
            this.userService.findByEmail("abc");
        });

        thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            this.userService.findByEmail(null);
        });
    }

    @Test
    @DisplayName("Should update and return an user")
    void updateTest_HappyPath() {
        UserDTO userDTO = new UserDTO("Jhon Doe", "jhondoe@email.com");
        User user = new User(userDTO);

        when(this.userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        user.markUpdated();
        user.setName("abc");
        when(this.userRepository.save(any())).thenReturn(user);
        when(this.apiGatewayDiscovery.updateUserPublisher(any())).thenReturn(true);

        RestResponse<User> restResponse = this.userService.update(user.getId(), new UserDTO("abc", ""));

        HttpStatus userUpdated_Status = restResponse.httpStatus();
        User userUpdated = restResponse.body();

        verify(this.userRepository, times(1)).save(any());

        assertEquals(user, userUpdated);
        assertEquals(userUpdated.getName(), "abc");
        assertEquals(HttpStatus.OK, userUpdated_Status);

        assertNotNull(userUpdated.getUpdated_at());
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        calendar.add(Calendar.MINUTE, 1);
        Timestamp futureTimestamp = new Timestamp(calendar.getTimeInMillis());
        calendar.add(Calendar.MINUTE, -2);
        Timestamp pastTimestamp = new Timestamp(calendar.getTimeInMillis());

        assertTrue(userUpdated.getUpdated_at().before(futureTimestamp));
        assertTrue(userUpdated.getUpdated_at().after(pastTimestamp));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException WhenIdDoNotMatchOrNull")
    void updateTest_throwEntityNotFoundException_WhenIdDoNotMatchOrNull() {
        Exception thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            when(this.userRepository.findById(any())).thenReturn(null);
            this.userService.update("abc", new UserDTO("abc", "abc"));
        });
    }

    @Test
    @DisplayName("Should Throw MissingFieldsException WhenDoNotHaveAnyFieldsToUpdate")
    void updateTest_ThrowMissingFieldsException_WhenDoNotHaveAnyFieldsToUpdate() {
        Exception thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            UserDTO userDTO = new UserDTO("Jhon Doe", "jhondoe@email.com");
            User user = new User(userDTO);
            when(this.userRepository.findById("abc")).thenReturn(Optional.of(user));
            this.userService.update("abc", new UserDTO("", ""));
        });
    }

    @Test
    @DisplayName("Should delete an user and return true")
    void deleteTest_HappyPath() {
        User user = new User(new UserDTO("abc", "abc"));
        when(this.userRepository.findById("abc")).thenReturn(Optional.of(user));

        RestResponse<Boolean> restResponse = this.userService.delete("abc");

        HttpStatus userFound_Status = restResponse.httpStatus();
        Boolean deleted = restResponse.body();

        verify(this.userRepository, times(1)).delete(user);

        assertEquals(true, deleted);
        assertEquals(HttpStatus.OK, userFound_Status);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException WhenIdDoNotMatchOrNull")
    void deleteTest_throwEntityNotFoundException_WhenIdDoNotMatchOrNull() {
        Exception thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            when(this.userRepository.findById("abc")).then(null);
            this.userService.delete("abc");
        });
    }
}