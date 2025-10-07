package com.example.hotcinemas_be.services;

import com.example.hotcinemas_be.dtos.user.requests.UserUpdateRequest;
import com.example.hotcinemas_be.dtos.user.responses.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.hotcinemas_be.dtos.auth.requests.RegisterRequest;
import com.example.hotcinemas_be.dtos.user.requests.UpdatePasswordRequest;
import com.example.hotcinemas_be.dtos.user.requests.UserRequest;

import java.util.List;

public interface UserService {
    public UserResponse createUser(UserRequest userRequest);

    public UserResponse updateUser(Long userId, UserRequest userRequest);

    public UserResponse getUserById(Long id);

    public Page<UserResponse> getPageUsers(Pageable pageable);

    public List<UserResponse> getAllUsers();

    public void deleteUser(Long id);

    public UserResponse getUserByEmail(String email);

    public UserResponse getUserByUserName(String userName);

    public UserResponse registerUser(RegisterRequest registerRequest);

    public UserResponse updateUserAvatar(Long id, String avatarUrl);

    public UserResponse updateUserPassword(Long id, UpdatePasswordRequest updatePasswordRequest);

    public UserResponse getUserInfo();

    public void changePassword(String newPassword);

    public UserResponse updateInfoUser(UserUpdateRequest userUpdateRequest);

    public UserResponse changeRole(Long id, String role);

    public boolean activateUser(Long id);

    public boolean deactivateUser(Long id);

    public Page<UserResponse> searchUsers(String keyword, Pageable pageable);

    public Page<UserResponse> getUsersByRole(String roleName, Pageable pageable);
}
