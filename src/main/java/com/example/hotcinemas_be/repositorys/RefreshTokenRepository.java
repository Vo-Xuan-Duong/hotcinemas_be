package com.example.hotcinemas_be.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hotcinemas_be.models.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,String> {
}
