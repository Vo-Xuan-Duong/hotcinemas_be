package com.example.hotcinemas_be.services;

import com.example.hotcinemas_be.dtos.requests.RegisterRequest;
import com.example.hotcinemas_be.dtos.requests.UpdatePasswordRequest;
import com.example.hotcinemas_be.dtos.requests.UserRequest;
import com.example.hotcinemas_be.dtos.responses.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    public UserResponse createUser(UserRequest userRequest) ;
    public UserResponse updateUser(Long userId ,UserRequest userRequest) ;
    public UserResponse getUserById(Long id) ;
    public Page<UserResponse> getAllUsers(Pageable pageable) ;
    public void deleteUser(Long id) ;
    public UserResponse getUserByEmail(String email) ;
    public UserResponse getUserByUserName(String userName) ;
    public UserResponse registerUser(RegisterRequest registerRequest) ;
    public UserResponse updateUserAvatar(Long id, String avatarUrl) ;
    public UserResponse updateUserPassword(Long id, UpdatePasswordRequest updatePasswordRequest) ;
    public UserResponse updateInfoUser(Long id, UserRequest userRequest) ;
    public UserResponse addRoleToUser(Long id, String roleName) ;
    public UserResponse removeRoleFromUser(Long id, String roleName) ;
    public boolean activateUser(Long id) ;
    public boolean deactivateUser(Long id) ;
    public Page<UserResponse> searchUsers(String keyword, Pageable pageable) ;
    public Page<UserResponse> getUsersByRole(String roleName, Pageable pageable) ;
}
