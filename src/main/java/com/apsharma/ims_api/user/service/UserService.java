package com.apsharma.ims_api.user.service;

import com.apsharma.ims_api.role.model.Role;
import com.apsharma.ims_api.user.model.User;
import com.apsharma.ims_api.role.repository.RoleRepo;
import com.apsharma.ims_api.user.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;



    public void registerUser(User user) {
        List<Role> roles = new ArrayList<>();
        for(Role role : user.getRoles()) {
            Role existingRole = roleRepo.findByRoleName(role.getRoleName());

            if(existingRole == null) {
                throw new RuntimeException("Role not found: " + role.getRoleName());
            }
            roles.add(existingRole);
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
        userRepo.save(user);
    }

    public void validateUser(User user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
    }

    public List<User> getAllUser() {
        return userRepo.findAll();
    }


//    public void addUser(User user) throws SQLException {
////        UserValidator.validateForCreate(user);
//        userRepo.save(user);
//    }
//
//    public List<User> getAllUser() throws SQLException{
//        return userRepo.findAll();
//    }
//
//    public User getUser(long id) throws SQLException{
//        return userRepo.findById(id).orElseThrow(() -> new UserNotFoundException(id));
//    }
//
//    public User updateUser(User updated) throws SQLException{
//        userRepo.findById(updated.getId()).orElseThrow(() -> new UserNotFoundException(updated.getId()));
//        User user = userRepo.save(updated);
//        return updated;
//    }
//
//
//    public User deleteUser(long id) {
//        User existing = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException(id));
//        userRepo.deleteById(id);
//        return existing;
//    }



}
