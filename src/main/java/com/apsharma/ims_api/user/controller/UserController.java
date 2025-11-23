package com.apsharma.ims_api.user.controller;

import com.apsharma.ims_api.user.model.User;
import com.apsharma.ims_api.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping(value="/users")
    public ResponseEntity<List<User>> getAllUsers() throws SQLException{
        List<User> users = service.getAllUser();
        return ResponseEntity.status(HttpStatus.CREATED).body(users);

    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getUserById(@PathVariable int id) throws SQLException {
//        User user = service.getUser(id);
//        if (user != null) {
//            return ResponseEntity.status(HttpStatus.OK).body(user);
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }
//    }
//
//    @PutMapping
//    public ResponseEntity<User> updateUser(@RequestBody User user) throws SQLException {
//        return ResponseEntity.status(HttpStatus.OK).body(service.updateUser(user));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<User> deleteUser(@PathVariable int id) throws SQLException {
//        User user = service.deleteUser(id);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(user);
//    }
}