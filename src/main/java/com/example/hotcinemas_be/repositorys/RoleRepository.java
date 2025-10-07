package com.example.hotcinemas_be.repositorys;

import com.example.hotcinemas_be.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(String name);
    Optional<Role> findByCode(String code);
}
