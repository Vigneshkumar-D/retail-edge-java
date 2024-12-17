package com.retailedge.controller.authentication;

import com.retailedge.model.ResponseModel;
import com.retailedge.model.user.TokenModel;
import com.retailedge.model.user.TokenResponseModel;
import com.retailedge.service.authentication.AuthenticationService;
import com.retailedge.utils.user.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<ResponseModel<?>> login(@RequestBody TokenModel user) {
            return  authenticationService.verify(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseModel<?>> logout(@RequestHeader("Authorization") String authorization) {
        return authenticationService.logout(authorization);
    }

    @PostMapping("/forget-password")
    public ResponseEntity<ResponseModel<?>> passwordReset(@RequestBody Map<String, String>  email) {
        return authenticationService.sendResetPasswordLink(email.get("email"));
    }

    @PostMapping("/confirm-reset")
    public ResponseEntity<ResponseModel<?>> confirmPasswordReset(@RequestHeader("token") String token, @RequestBody Map<String, String> newPassword) {
        return authenticationService.resetPassword(token, newPassword.get("newPassword"));
    }
}