package com.nexus.server.controllers;

import com.nexus.server.entities.Role;
import com.nexus.server.services.ActivityService;
import com.nexus.server.services.RoleService;
import com.nexus.server.utils.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles") // http://localhost:8081/api/roles
public class RoleController {
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * Get all roles
     *
     * @return List of all roles
     * @route GET /api/roles
     */
    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    /**
     * Get role by id
     *
     * @param id Role id
     * @return Role
     * @route GET /api/roles/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role with id '" + id + "' not found")));
    }

    /**
     * Create role - Request body:
     * <pre>
     * {
     *      "name": "test role",
     * }
     * </pre>
     *
     * @return Role
     * @route POST /api/roles
     */
    @PostMapping
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) {
        return ResponseEntity.ok(roleService.createRole(role));
    }

    /**
     * Update role - Request body:
     * <pre>
     *     {
     *         "name": "role updated",
     *     }
     * </pre>
     *
     * @param id          Role id
     * @param roleDetails Role details
     * @return Role
     * @route PUT /api/roles/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @Valid @RequestBody Role roleDetails) {
        return ResponseEntity.ok(roleService.updateRole(id, roleDetails)
                .orElseThrow(() -> new ResourceNotFoundException("Role with id '" + id + "' not found")));
    }

    /**
     * Delete role
     *
     * @param id Role id
     * @return Response message
     * @route DELETE /api/roles/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteRole(@PathVariable Long id) {
        if (roleService.getRoleById(id).isEmpty()) {
            throw new ResourceNotFoundException("Role with id '" + id + "' not found");
        }

        roleService.deleteRole(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Role deleted successfully");
        return ResponseEntity.ok(response);
    }

}
