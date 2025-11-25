package com.apsharma.ims_api.role.controller;

import com.apsharma.ims_api.role.model.Role;
import com.apsharma.ims_api.role.service.RoleService;
import com.apsharma.ims_api.util.ApiResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RoleController {

    @Autowired
    private RoleService repoService;

    @GetMapping(value="/roles")
    public ResponseEntity<Map<String, Object>> getAll() {
        List<Role> roles = repoService.getAllRoles();
        return ResponseEntity.ok(
                new ApiResponseBuilder()
                        .status(HttpStatus.OK)
                        .message("Roles retrieved successfully")
                        .data(roles)
                        .build()
        );
    }


    @PostMapping(value="/roles")
    public ResponseEntity<Map<String, Object>> save(@RequestBody Role role) {
        Role savedRole = repoService.saveRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponseBuilder()
                        .status(HttpStatus.CREATED)
                        .message("Role created successfully")
                        .data(savedRole)
                        .build()
        );
    }

}

