package com.example.hotcinemas_be.mappers;

import com.example.hotcinemas_be.dtos.permission.responses.PermissionResponse;
import com.example.hotcinemas_be.dtos.role.responses.RoleResponse;
import com.example.hotcinemas_be.models.Role;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class RoleMapper {
    private final PermissionMapper permissionMapper;

    public RoleMapper(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    public RoleResponse mapToResponse(Role role) {
        if (role == null) {
            return null;
        }
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .isActive(role.getIsActive())
                .permissions(role.getPermissions().stream().map(permissionMapper::mapToResponse).toList())
                .build();
    }
}
