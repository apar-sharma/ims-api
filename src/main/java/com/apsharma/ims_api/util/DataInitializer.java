package com.apsharma.ims_api.util;

import com.apsharma.ims_api.role.model.Role;
import com.apsharma.ims_api.role.repository.RoleRepo;
import com.apsharma.ims_api.user.model.User;
import com.apsharma.ims_api.user.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if roles already exist
        if (roleRepo.count() == 0) {
            System.out.println("Initializing roles...");

            // Create roles
            Role roleUser = new Role();
            roleUser.setRoleName("ROLE_USER");
            roleRepo.save(roleUser);

            Role roleAdmin = new Role();
            roleAdmin.setRoleName("ROLE_ADMIN");
            roleRepo.save(roleAdmin);

            System.out.println("Roles created: ROLE_USER, ROLE_ADMIN");
        }

        // Check if users already exist
        if (userRepo.count() == 0) {
            System.out.println("Initializing test users...");

            Role roleUser = roleRepo.findByRoleName("ROLE_USER");
            Role roleAdmin = roleRepo.findByRoleName("ROLE_ADMIN");

            // Create admin user
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(bCryptPasswordEncoder.encode("admin123"));
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setEmail("admin@ims.com");
            admin.setPhoneNumber(9999999999L);
            admin.setRoles(List.of(roleAdmin));
            userRepo.save(admin);

            // Create regular user
            User user = new User();
            user.setUsername("john.doe");
            user.setPassword(bCryptPasswordEncoder.encode("password123"));
            user.setFirstName("John");
            user.setLastName("Doe");
            user.setEmail("john.doe@ims.com");
            user.setPhoneNumber(1234567890L);
            user.setRoles(List.of(roleUser));
            userRepo.save(user);

            // Create another regular user
            User user2 = new User();
            user2.setUsername("jane.smith");
            user2.setPassword(bCryptPasswordEncoder.encode("password456"));
            user2.setFirstName("Jane");
            user2.setLastName("Smith");
            user2.setEmail("jane.smith@ims.com");
            user2.setPhoneNumber(9876543210L);
            user2.setRoles(List.of(roleUser));
            userRepo.save(user2);

            System.out.println("Test users created:");
            System.out.println("  - admin (password: admin123) - ROLE_ADMIN");
            System.out.println("  - john.doe (password: password123) - ROLE_USER");
            System.out.println("  - jane.smith (password: password456) - ROLE_USER");
        }
    }
}

