package com.example.hotcinemas_be.mappers;

import com.example.hotcinemas_be.dtos.responses.PermissionResponse;
import com.example.hotcinemas_be.models.Permission;
import org.springframework.stereotype.Service;

@Service
public class PermissionMapper {

    public PermissionResponse mapToResponse(Permission permission) {
        if (permission == null) {
            return null;
        }
        return PermissionResponse.builder()
                .permissionId(permission.getPermissionId())
                .permissionName(permission.getName())
                .description(permission.getDescription())
                .isActive(permission.getIsActive())
                .build();
    }
}
