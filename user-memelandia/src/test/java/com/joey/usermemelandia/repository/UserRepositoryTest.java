package com.joey.usermemelandia.repository;

import com.joey.usermemelandia.domain.User;
import com.joey.usermemelandia.records.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest;
import org.springframework.data.cassandra.CassandraInvalidQueryException;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataCassandraTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void clearDB() {
        Iterable<User> userIterable = this.userRepository.findAll();
        userIterable.forEach(user -> this.userRepository.delete(user));
    }

    @Test
    @DisplayName("Should create a user")
    public void createUser_HappyPath() {
        UserDTO userDTO = new UserDTO("Jhon Doe", "JhonDoe@email.com");
        User userCreated = createUserMethod(userDTO);

        assertInstanceOf(User.class, userCreated);
        assertNotNull(userCreated.getId());
        assertEquals(userCreated.getEmail(), userDTO.email());
        assertEquals(userCreated.getName(), userDTO.name());

        Timestamp now = new Timestamp(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        calendar.add(Calendar.MINUTE, 1);
        Timestamp futureTimestamp = new Timestamp(calendar.getTimeInMillis());
        calendar.add(Calendar.MINUTE, -2);
        Timestamp pastTimestamp = new Timestamp(calendar.getTimeInMillis());

        assertTrue(userCreated.getCreated_at().before(futureTimestamp));
        assertTrue(userCreated.getCreated_at().after(pastTimestamp));
        assertNull(userCreated.getUpdated_at());
    }

    @Test
    @DisplayName("save should throw IllegalArgumentException for null user")
    public void createUser_Failed() {
        assertThrows(IllegalArgumentException.class, () -> {
            userRepository.save(null);
        }, "save should throw IllegalArgumentException for null user");
    }

    @Test
    @DisplayName("should return all users on table")
    public void findAll_HappyPath() {
        Iterable<User> users = userRepository.findAll();
        List<User> usersList = StreamSupport.stream(users.spliterator(), false).toList();

        assertTrue(usersList.size() == 0);

        UserDTO userDTO = new UserDTO("Jhon Doe", "JhonDoe@email.com");
        User userCreated = createUserMethod(userDTO);

        users = userRepository.findAll();
        usersList = StreamSupport.stream(users.spliterator(), false).toList();

        assertEquals(1, usersList.size());
        assertEquals(userCreated, usersList.get(0));

        UserDTO userDTO_2 = new UserDTO("Maria Doe", "MariaDoe@email.com");
        User userCreated_2 = createUserMethod(userDTO_2);

        users = userRepository.findAll();
        usersList = StreamSupport.stream(users.spliterator(), false).toList();

        assertEquals(2, usersList.size());
        if (usersList.get(0).getName().equals(userCreated.getName())) {
            assertEquals(userCreated_2, usersList.get(1));
            assertEquals(userCreated, usersList.get(0));
        } else {
            assertEquals(userCreated_2, usersList.get(0));
            assertEquals(userCreated, usersList.get(1));
        }
    }

    @Test
    @DisplayName("should return an user found by Id")
    public void findById_HappyPath() {
        UserDTO userDTO = new UserDTO("Jhon Doe", "JhonDoe@email.com");
        User userCreated = createUserMethod(userDTO);

        Optional<User> userFromDB = this.userRepository.findById(userCreated.getId());

        assertEquals(userCreated, userFromDB.orElseThrow());
    }

    @Test
    @DisplayName("should return null when the id didn't match")
    public void findById_FindingNoOne() {
        Optional<User> userFromDB = this.userRepository.findById("123");
        assertEquals(Optional.empty(), userFromDB);
    }

    @Test
    @DisplayName("findById should throw IllegalArgumentException for null id")
    public void findById_WhenIDIsNull_ThrowsIllegalArgumentException() {
        assertThrows(CassandraInvalidQueryException.class, () -> {
            userRepository.findById("");
        }, "findById should throw IllegalArgumentException for null id");
    }

    @Test
    @DisplayName("should return an user found by email")
    public void findByEmail_HappyPath() {
        UserDTO userDTO = new UserDTO("Jhon Doe", "JhonDoe@email.com");
        User userCreated = createUserMethod(userDTO);

        Optional<User> userFromDB = this.userRepository.findByEmail("JhonDoe@email.com");

        assertEquals(userCreated.getId(), userFromDB.get().getId());
    }

    @Test
    @DisplayName("should return null when the email didn't match")
    public void findByEmail_FindingNoOne() {
        Optional<User> userFromDB = this.userRepository.findByEmail("@email.com");
        assertEquals(Optional.empty(), userFromDB);
    }

    @Test
    @DisplayName("findByEmail should throw IllegalArgumentException for null email")
    public void findByEmail_WhenEmailIsNull_ThrowsIllegalArgumentException() {
        assertThrows(CassandraInvalidQueryException.class, () -> {
            userRepository.findByEmail(null);
        }, "findByEmail should throw IllegalArgumentException for null email");
    }

    @Test
    @DisplayName("update should throw IllegalArgumentException when don't have any field s to update")
    public void update_WhenNoFieldToUpdate_IllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            userRepository.save(null);
        }, "save should throw IllegalArgumentException for null user");
    }

    @Test
    @DisplayName("User should be deleted from the database")
    public void deleteUser_HappyPath() {
        User user = new User();
        user.setEmail("test@example.com");
        userRepository.save(user);

        userRepository.delete(user);

        Optional<User> deletedUser = userRepository.findByEmail("test@example.com");
        assertFalse(deletedUser.isPresent(), "User should be deleted from the database");
    }

    @Test
    @DisplayName("User should be deleted from the database by Id")
    public void deleteUser_ById_HappyPath() {
        User user = new User();
        user.setEmail("test@example.com");
        userRepository.save(user);

        Optional<User> userFromDB = userRepository.findByEmail("test@example.com");
        userRepository.deleteById(userFromDB.get().getId());

        Optional<User> deletedUser = userRepository.findByEmail("test@example.com");
        assertFalse(deletedUser.isPresent(), "User should be deleted from the database");
    }

    @Test
    @DisplayName("delete should throw IllegalArgumentException for null user")
    public void deleteUser_WhenUserIsNull_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            userRepository.delete(null);
        }, "delete should throw IllegalArgumentException for null user");
    }

    private User createUserMethod(UserDTO userDTO) {
        User user = new User(userDTO);
        return this.userRepository.save(user);
    }

    private void deleteUserMethod(String id) {
        this.userRepository.deleteById(id);
    }

}