package com.example.hotcinemas_be.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse {
    private Long roleId;
    private String roleName;
    private String description;
    private Boolean isActive;
    private Set<PermissionResponse> permissions;
}
