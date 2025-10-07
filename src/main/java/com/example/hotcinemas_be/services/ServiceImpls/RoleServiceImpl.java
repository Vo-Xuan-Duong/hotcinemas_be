package com.example.hotcinemas_be.services.ServiceImpls;

import com.example.hotcinemas_be.dtos.role.requests.RoleRequest;
import com.example.hotcinemas_be.dtos.role.responses.RoleResponse;
import com.example.hotcinemas_be.mappers.RoleMapper;
import com.example.hotcinemas_be.models.Permission;
import com.example.hotcinemas_be.models.Role;
import com.example.hotcinemas_be.repositorys.PermissionRepository;
import com.example.hotcinemas_be.repositorys.RoleRepository;
import com.example.hotcinemas_be.services.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final PermissionRepository permissionRepository;

    public RoleServiceImpl(RoleRepository roleRepository,
            RoleMapper roleMapper,
            PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public RoleResponse createRole(RoleRequest roleRequest) {
        Role role = new Role();
        role.setName(roleRequest.getName());
        role.setDescription(roleRequest.getDescription());
        role.setIsActive(true); // Assuming new roles are active by default
        return roleMapper.mapToResponse(roleRepository.save(role));
    }

    @Override
    public RoleResponse updateRole(Long roleId, RoleRequest roleRequest) {
        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        existingRole.setName(roleRequest.getName());
        existingRole.setDescription(roleRequest.getDescription());
        // Assuming we don't change the active status on update
        return roleMapper.mapToResponse(roleRepository.save(existingRole));
    }

    @Override
    public RoleResponse getRoleById(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
        return roleMapper.mapToResponse(role);
    }

    @Override
    public void deleteRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
        roleRepository.delete(role);
    }

    @Override
    public RoleResponse getRoleByCode(String code) {
        Role role = roleRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Role not found with name: " + code));
        return roleMapper.mapToResponse(role);
    }

    @Override
    public Page<RoleResponse> getPageRoles(Pageable pageable) {
        Page<Role> roles = roleRepository.findAll(pageable);
        return roles.map(roleMapper::mapToResponse);
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::mapToResponse).toList();
    }

    @Override
    public void activateRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
        role.setIsActive(true);
        roleRepository.save(role);
    }

    @Override
    public void deactivateRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
        role.setIsActive(false);
        roleRepository.save(role);
    }

    @Override
    public RoleResponse addPermissionsToRole(Long roleId, List<Long> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        role.getPermissions().addAll(permissions);

        return roleMapper.mapToResponse(roleRepository.save(role));
    }

    @Override
    public RoleResponse removePermissionsFromRole(Long roleId, List<Long> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        permissions.forEach(role.getPermissions()::remove);

        return roleMapper.mapToResponse(roleRepository.save(role));
    }
}
