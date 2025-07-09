package com.example.hotcinemas_be.config;

import com.example.hotcinemas_be.models.Role;
import com.example.hotcinemas_be.models.User;
import com.example.hotcinemas_be.repositorys.RoleRepository;
import com.example.hotcinemas_be.repositorys.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class AppInitConfig {
    private final PasswordEncoder passwordEncoder;

    public AppInitConfig() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            Role adminRole;
            if(roleRepository.findByName("Admin").isEmpty()) {
                adminRole = new Role();
                adminRole.setName("Admin");
                adminRole.setDescription("Admin");
                roleRepository.save(adminRole);
                System.out.println("ROLE 'admin' has been created");
            } else {
                adminRole = roleRepository.findByName("Admin").get();
            }
            Role userRole;
            if(roleRepository.findByName("User").isEmpty()) {
                userRole = new Role();
                userRole.setName("User");
                userRole.setDescription("User");
                roleRepository.save(userRole);
                System.out.println("ROLE 'user' has been created");
            } else {
                userRole = roleRepository.findByName("User").get();
            }
            Role staffRole;
            if(roleRepository.findByName("Staff").isEmpty()) {
                staffRole = new Role();
                staffRole.setName("Staff");
                staffRole.setDescription("Staff");
                roleRepository.save(staffRole);
                System.out.println("ROLE 'staff' has been created");
            } else {
                staffRole = roleRepository.findByName("Staff").get();
            }
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            roles.add(userRole);
            roles.add(staffRole);

            if(userRepository.findByUsername("admin").isEmpty()) {
                User adminUser = new User();
                adminUser.setUsername("admin");
                adminUser.setPassword(passwordEncoder.encode("admin")); // This should be encoded in a real application
                adminUser.setEmail("admin@gmail.com");
                adminUser.setFullName("Admin User");
                adminUser.setPhoneNumber("1234567890");
                adminUser.setAvatarUrl("https://example.com/avatar.png");
                adminUser.setIsActive(true);
                adminUser.setRoles(roles);
                userRepository.save(adminUser);
                System.out.println("Admin user has been created with roles: " + roles);
            } else {
                System.out.println("Admin user already exists.");
            }




            System.out.println("Application initialization logic goes here.");
        };
    }
}
