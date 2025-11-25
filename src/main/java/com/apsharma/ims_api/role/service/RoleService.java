package com.apsharma.ims_api.role.service;


import com.apsharma.ims_api.role.model.Role;
import com.apsharma.ims_api.role.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepo roleRepo;

    public Role saveRole(Role role) {
        return roleRepo.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }

    public Role getRoleByName(String roleName) {
        return roleRepo.findByRoleName(roleName);
    }
}

