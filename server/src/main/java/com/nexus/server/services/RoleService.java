package com.nexus.server.services;

import com.nexus.server.entities.Role;
import com.nexus.server.repositories.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final IRoleRepository roleRepository;

    @Autowired
    public RoleService(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Get all roles
     * @return List of all roles
     */
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * Get role by id
     * @param id Role id
     */
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    /**
     * Create role
     * @param role Role
     * @return Role
     */
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    /**
     * Update role
     * @param id Role id
     * @param roleDetails Role details
     * @return Role
     */
    public Optional<Role> updateRole(Long id, Role roleDetails) {
        return roleRepository.findById(id)
                .map(role -> {
                    role.setName(roleDetails.getName());
                    return roleRepository.save(role);
                });
    }

    /**
     * Delete role
     * @param id Role id
     */
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
