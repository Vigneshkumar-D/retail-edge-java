package com.retailedge.controller.user;

import com.retailedge.dto.user.RoleDTO;
import com.retailedge.entity.user.Role;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.user.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<ResponseModel<?>> createRole(@RequestBody Role role) {
        return roleService.createRole(role);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel<?>> getRoleById(@PathVariable(name = "id") Integer roleId) {
        return roleService.getRoleById(roleId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseModel<?>> updateRole(@PathVariable(name = "id") Integer roleId, @RequestBody RoleDTO roleDTO) {
        return roleService.updateRole(roleId, roleDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseModel<?>> deleteRole(@PathVariable(name = "id") Integer roleId) {
        return roleService.deleteRole(roleId);
    }

    @GetMapping
    public ResponseEntity<ResponseModel<?>> getAllRoles() {
        return roleService.getAllRoles();
    }
}
