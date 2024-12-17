package com.retailedge.controller.user;

import com.retailedge.dto.user.UserDTO;
import com.retailedge.entity.user.User;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.user.UserService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ResponseModel<?>> createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel<?>> getUserById(@PathVariable("id") Integer userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseModel<?>> updateUser(@PathVariable(name = "id") Integer userId, @RequestBody UserDTO userDTO) {
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
