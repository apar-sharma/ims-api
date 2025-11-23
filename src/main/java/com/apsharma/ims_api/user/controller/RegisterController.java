package com.apsharma.ims_api.user.controller;


import com.apsharma.ims_api.user.model.User;
import com.apsharma.ims_api.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;


@RestController
@RequestMapping(value="/auth")
public class RegisterController {

    @Autowired
    private UserService userService;

    @PostMapping(value  ="/register")
    public ResponseEntity<User> registerUser(@RequestBody @Valid User user) throws JsonProcessingException {
        userService.registerUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping(value="/login")
    public ResponseEntity<User> login(@RequestBody User user) throws JsonProcessingException, SQLException {
        userService.validateUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

}
