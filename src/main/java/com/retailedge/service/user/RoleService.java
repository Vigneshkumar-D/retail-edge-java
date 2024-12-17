package com.retailedge.service.user;

import com.retailedge.dto.user.RoleDTO;
import com.retailedge.entity.user.Role;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.user.RoleRepository;
import com.retailedge.utils.ExceptionHandler.ExceptionHandlerUtil;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ExceptionHandlerUtil exceptionHandlerUtil;

    public ResponseEntity<ResponseModel<?>> getAllRoles() {
        try{
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, roleRepository.findAll()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error retrieving role details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> createRole(Role role) {
        try{
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, roleRepository.save(role)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error adding role details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> getRoleById(Integer id) {
        try{
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, roleRepository.findById(id)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error retrieving role details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> updateRole(Integer roleId, RoleDTO roleDetails) {
        try{
            Role role = roleRepository.findById(roleId).orElse(null);
            if (role == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseModel<>(false, "Role details not found!", 500));
            }
            modelMapper.map(roleDetails, role);
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, roleRepository.save(role)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error updating role details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> deleteRole(Integer roleId) {

        try {
            if (!roleRepository.existsById(roleId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Role not found", 404));
            }
            roleRepository.deleteById(roleId);
            // Return 200 OK if the category is deleted successfully
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting role: " + e.getMessage(), 500));
        }
    }

    @PostConstruct
    public void addRoles(){
        List<Role> roles = Arrays.asList(
                new Role(1,"System", true, Instant.now(), Instant.now()),
                new Role(2,"Super User", true, Instant.now(), Instant.now()),
                new Role(3,"Manager", true, Instant.now(), Instant.now()),
                new Role(4,"Guest", true, Instant.now(), Instant.now())
        );
        try{
            this.roleRepository.saveAll(roles);
        }
        catch (DataIntegrityViolationException ex){
            System.out.println("Role already exists....");
        }

    }

    public Role getSystem(){
        Optional<Role> optionalRole = this.roleRepository.findByRoleName("System");
        return optionalRole.orElse(null);
    }
    public Role getSuperUser(){
        Optional<Role> optionalRole = this.roleRepository.findByRoleName("Super User");
        return optionalRole.orElse(null);
    }
    public Role getGuest(){
        Optional<Role> optionalRole = this.roleRepository.findByRoleName("Guest");
        return optionalRole.orElse(null);
    }
}
