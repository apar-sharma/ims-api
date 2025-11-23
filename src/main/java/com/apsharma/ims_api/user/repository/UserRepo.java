package com.apsharma.ims_api.user.repository;

import com.apsharma.ims_api.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
