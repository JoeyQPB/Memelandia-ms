package com.joey.usermemelandia.controller;

import com.joey.usermemelandia.domain.User;
import com.joey.usermemelandia.records.RestResponse;
import com.joey.usermemelandia.records.UserDTO;
import com.joey.usermemelandia.records.RestErrorMessage;
import com.joey.usermemelandia.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDTO user) {
        RestResponse<User> responseService = this.userService.createUser(user);
        return ResponseEntity.status(responseService.httpStatus()).body(responseService.body());
    }

    @GetMapping
    public ResponseEntity<Iterable<User>> getAllUsers() {
        RestResponse<Iterable<User>> responseService = this.userService.findAll();
        return ResponseEntity.status(responseService.httpStatus()).body(responseService.body());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        RestResponse<User> responseService = this.userService.findById(id);
        return ResponseEntity.status(responseService.httpStatus()).body(responseService.body());
    }

    @GetMapping("/qe={email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        RestResponse<User> responseService = this.userService.findByEmail(email);
        return ResponseEntity.status(responseService.httpStatus()).body(responseService.body());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        RestResponse<User> responseService = this.userService.update(id, userDTO);
        return ResponseEntity.status(responseService.httpStatus()).body(responseService.body());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        RestResponse<Boolean> responseService = this.userService.delete(id);
        return ResponseEntity.status(responseService.httpStatus()).body(responseService.body());
    }
}
