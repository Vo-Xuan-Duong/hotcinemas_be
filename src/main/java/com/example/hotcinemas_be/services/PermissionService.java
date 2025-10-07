package com.example.hotcinemas_be.services;

import com.example.hotcinemas_be.dtos.permission.requests.PermissionRequest;
import com.example.hotcinemas_be.dtos.permission.responses.PermissionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


public interface PermissionService {
    public PermissionResponse createPermission(PermissionRequest permissionRequest);
    public PermissionResponse getPermissionById(Long id);
    public PermissionResponse updatePermission(Long id, PermissionRequest permissionRequest);
    public void deletePermission(Long id);
    public Page<PermissionResponse> getPermissions(Pageable pageable);
    public List<PermissionResponse> getAllPermissions();
    public void activatePermission(Long id);
    public void deactivatePermission(Long id);
}
