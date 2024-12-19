package com.retailedge.controller.user;

import com.retailedge.dto.user.UserDTO;
import com.retailedge.entity.user.Role;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.user.RoleRepository;
import com.retailedge.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseModel<?>> createUser(@ModelAttribute UserDTO userDTO) {
//        Optional<Role> role = roleRepository.findById(userDTO.getRole().getId());
//        userDTO.setRole(role.get());

        return userService.createUser(userDTO);
//        return userService.createUser(userDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel<?>> getUserById(@PathVariable("id") Integer userId) {
        return userService.getUserById(userId);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseModel<?>> updateUser(@PathVariable(name = "id") Integer userId, @ModelAttribute UserDTO userDTO) {
        return userService.updateUser(userId, userDTO);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseModel<?>> deleteUser(@PathVariable(name = "id") Integer userId) {
        return userService.deleteUser(userId);
    }

    @GetMapping
    public ResponseEntity<ResponseModel<?>> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("current-user")
    public ResponseEntity<ResponseModel<?>> getCurrentUser(){
        return userService.getCurrentUser();
    }
}
