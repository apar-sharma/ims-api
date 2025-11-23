package com.apsharma.ims_api.role.controller;

import com.apsharma.ims_api.role.model.Role;
import com.apsharma.ims_api.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RoleController {

    @Autowired
    private RoleService repoService;

    @GetMapping(value="/roles")
    public ResponseEntity<List<Role>> getAll(@RequestBody Role role) {
        List<Role> roles = repoService.getAllRoles();
        return ResponseEntity.status(HttpStatus.CREATED).body(roles);
    }


    @PostMapping(value="/roles")
    public ResponseEntity<String> save(@RequestBody Role role) {
        repoService.saveRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body("created");
    }

}

