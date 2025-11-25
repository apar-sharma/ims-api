package com.apsharma.ims_api.user.controller;

import com.apsharma.ims_api.user.dto.UserResponse;
import com.apsharma.ims_api.user.mapper.UserMapper;
import com.apsharma.ims_api.user.model.User;
import com.apsharma.ims_api.user.service.UserService;
import com.apsharma.ims_api.util.ApiResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private UserMapper userMapper;

    @GetMapping(value="/users")
    public ResponseEntity<Map<String, Object>> getAllUsers() throws SQLException{
        List<User> users = service.getAllUser();
        List<UserResponse> userResponses = users.stream()
                .map(userMapper::toResponse)
                .toList();

        return ResponseEntity.ok(
                new ApiResponseBuilder()
                        .status(HttpStatus.OK)
                        .message("Users retrieved successfully")
                        .data(userResponses)
                        .build()
        );
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