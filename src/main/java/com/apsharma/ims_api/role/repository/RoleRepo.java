package com.apsharma.ims_api.role.repository;

import com.apsharma.ims_api.role.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByRoleName(String roleName);
}
