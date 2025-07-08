package com.example.hotcinemas_be.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionResponse {
    private Long permissionId;
    private String permissionName;
    private String description;
    private Boolean isActive;
}
