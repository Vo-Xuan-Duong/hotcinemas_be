package com.example.hotcinemas_be.services;

import com.example.hotcinemas_be.dtos.role.requests.RoleRequest;
import com.example.hotcinemas_be.dtos.role.responses.RoleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {
    public RoleResponse createRole(RoleRequest roleRequest);
    public RoleResponse updateRole(Long roleId, RoleRequest roleRequest);
    public RoleResponse getRoleById(Long roleId);
    public void deleteRole(Long roleId);
    public RoleResponse getRoleByCode(String code);
    public Page<RoleResponse> getPageRoles(Pageable pageable);
    public List<RoleResponse> getAllRoles();
    public void activateRole(Long roleId);
    public void deactivateRole(Long roleId);
    public RoleResponse addPermissionsToRole(Long roleId, List<Long> permissionIds);
    public RoleResponse removePermissionsFromRole(Long roleId, List<Long> permissionIds);
}
