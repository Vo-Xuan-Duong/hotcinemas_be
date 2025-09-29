package com.example.hotcinemas_be.services.ServiceImpls;

import com.example.hotcinemas_be.dtos.requests.RegisterRequest;
import com.example.hotcinemas_be.dtos.requests.UpdatePasswordRequest;
import com.example.hotcinemas_be.dtos.requests.UserRequest;
import com.example.hotcinemas_be.dtos.requests.UserUpdateRequest;
import com.example.hotcinemas_be.dtos.responses.UserResponse;
import com.example.hotcinemas_be.exceptions.ErrorCode;
import com.example.hotcinemas_be.exceptions.ErrorException;
import com.example.hotcinemas_be.mappers.UserMapper;
import com.example.hotcinemas_be.models.Role;
import com.example.hotcinemas_be.models.User;
import com.example.hotcinemas_be.repositorys.RoleRepository;
import com.example.hotcinemas_be.repositorys.UserRepository;
import com.example.hotcinemas_be.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public  UserServiceImpl(UserRepository userRepository,
                            RoleRepository roleRepository,
                            UserMapper userMapper,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setAvatarUrl(userRequest.getAvatarUrl());
        user.setFullName(userRequest.getFullName());
        user.setIsActive(true);
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("User").orElseThrow(() -> new ErrorException("Role not found when add role to create user", ErrorCode.ERROR_MODEL_NOT_FOUND)));
        user.setRoles(roles);
        return userMapper.mapToResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateUser(Long userId, UserRequest userRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ErrorException("User not found when update user", ErrorCode.ERROR_MODEL_NOT_FOUND));

        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setAvatarUrl(userRequest.getAvatarUrl());
        user.setFullName(userRequest.getFullName());

        return userMapper.mapToResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::mapToResponse)
                .orElseThrow(() -> new ErrorException("User not found with id: " + id, ErrorCode.ERROR_MODEL_NOT_FOUND));
    }

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(userMapper::mapToResponse);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ErrorException("User not found when delete user", ErrorCode.ERROR_MODEL_NOT_FOUND));
        userRepository.delete(user);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::mapToResponse)
                .orElseThrow(() -> new ErrorException("User not found with email: " + email, ErrorCode.ERROR_MODEL_NOT_FOUND));
    }

    @Override
    public UserResponse getUserByUserName(String userName) {
        return userRepository.findByUsername(userName)
                .map(userMapper::mapToResponse)
                .orElseThrow(() -> new ErrorException("User not found with username: " + userName, ErrorCode.ERROR_MODEL_NOT_FOUND));
    }

    @Override
    public UserResponse registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ErrorException("Email already exists", ErrorCode.ERROR_MODEL_ALREADY_EXISTS);
        }
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new ErrorException("Username already exists", ErrorCode.ERROR_MODEL_ALREADY_EXISTS);
        }

        if(!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new ErrorException("Password and Confirm Password no match", ErrorCode.CONFIRM_PASSWORD_AND_PASSWORD_NOT_MATCH);
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setFullName(registerRequest.getFullName());
        user.setIsActive(false); // User is not active by default
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("User").orElseThrow(() -> new ErrorException("Role not found when add role to create user", ErrorCode.ERROR_MODEL_NOT_FOUND)));
        user.setRoles(roles);
        return userMapper.mapToResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateUserAvatar(Long id, String avatarUrl) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ErrorException("User not found when update avatar", ErrorCode.ERROR_MODEL_NOT_FOUND));
        user.setAvatarUrl(avatarUrl);
        return userMapper.mapToResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateUserPassword(Long id, UpdatePasswordRequest updatePasswordRequest) {
        if(!updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getConfirmNewPassword())) {
            throw new ErrorException("New Password and Confirm Password do not match", ErrorCode.CONFIRM_PASSWORD_AND_PASSWORD_NOT_MATCH);
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ErrorException("User not found when update password", ErrorCode.ERROR_MODEL_NOT_FOUND));
        if (!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new ErrorException("Old password does not match", ErrorCode.PASSWORD_NOT_MATCH);
        }
        user.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        return userMapper.mapToResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getUserInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ErrorException("User not found when get user info", ErrorCode.ERROR_MODEL_NOT_FOUND));

        return userMapper.mapToResponse(user);
    }

    @Override
    public Boolean changePassword(String newPassword) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ErrorException("User not found when change password", ErrorCode.ERROR_MODEL_NOT_FOUND));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    @Override
    public UserResponse updateInfoUser(UserUpdateRequest userUpdateRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ErrorException("User not found when update info", ErrorCode.ERROR_MODEL_NOT_FOUND));

        user.setEmail(userUpdateRequest.getEmail());
        user.setPhoneNumber(userUpdateRequest.getPhoneNumber());
        user.setFullName(userUpdateRequest.getFullName());

        return userMapper.mapToResponse(userRepository.save(user));
    }

    @Override
    public UserResponse addRolesToUser(Long id, List<String> roleName) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ErrorException("User not found when add roles to user", ErrorCode.ERROR_MODEL_NOT_FOUND));
        Set<Role> rolesToAdd = new HashSet<>();
        for (String name : roleName) {
            Role role = roleRepository.findByName(name)
                    .orElseThrow(() -> new ErrorException("Role not found with name: " + name, ErrorCode.ERROR_MODEL_NOT_FOUND));
            if (user.getRoles().contains(role)) {
                throw new ErrorException("User already has this role: " + name, ErrorCode.ERROR_INVALID_REQUEST);
            }
            rolesToAdd.add(role);
        }
        user.getRoles().addAll(rolesToAdd);
        return userMapper.mapToResponse(userRepository.save(user));
    }

    @Override
    public UserResponse removeRolesFromUser(Long id, List<String> roleName) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ErrorException("User not found when remove roles from user", ErrorCode.ERROR_MODEL_NOT_FOUND));
        Set<Role> rolesToRemove = new HashSet<>();
        for (String name : roleName) {
            Role role = roleRepository.findByName(name)
                    .orElseThrow(() -> new ErrorException("Role not found with name: " + name, ErrorCode.ERROR_MODEL_NOT_FOUND));
            if (!user.getRoles().contains(role)) {
                throw new ErrorException("User does not have this role: " + name, ErrorCode.ERROR_INVALID_REQUEST);
            }
            rolesToRemove.add(role);
        }
        user.getRoles().removeAll(rolesToRemove);
        return userMapper.mapToResponse(userRepository.save(user));
    }

    @Override
    public boolean activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ErrorException("User not found when activate user", ErrorCode.ERROR_MODEL_NOT_FOUND));
        if (user.getIsActive()) {
            throw new ErrorException("User is already active", ErrorCode.ERROR_INVALID_REQUEST);
        }
        user.setIsActive(true);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ErrorException("User not found when deactivate user", ErrorCode.ERROR_MODEL_NOT_FOUND));
        if (!user.getIsActive()) {
            throw new ErrorException("User is already inactive", ErrorCode.ERROR_INVALID_REQUEST);
        }
        user.setIsActive(false);
        userRepository.save(user);
        return true;
    }

    @Override
    public Page<UserResponse> searchUsers(String keyword, Pageable pageable) {
        return null;
    }

    @Override
    public Page<UserResponse> getUsersByRole(String roleName, Pageable pageable) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ErrorException("Role not found with name: " + roleName, ErrorCode.ERROR_MODEL_NOT_FOUND));
        Page<User> userPage = userRepository.findByRolesContaining(role, pageable);
        return userPage.map(userMapper::mapToResponse);
    }
}
