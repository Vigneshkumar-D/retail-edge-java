package com.retailedge.controller.authentication;

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
    public ResponseEntity<?> login(@RequestBody TokenModel user) {
        try {
            String token = authenticationService.verify(user);
            return ResponseEntity.ok(new TokenResponseModel(true, token, "Login Success")); // Return the token in a response model
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorization) {
        return authenticationService.logout(authorization);
    }

    @PostMapping("/forget-password")
    public ResponseEntity<String> passwordReset(@RequestBody Map<String, String>  email) {
        return authenticationService.sendResetPasswordLink(email.get("email"));

    }

    @PostMapping("/confirm-reset")
    public ResponseEntity<String> confirmPasswordReset(@RequestHeader("token") String token, @RequestBody Map<String, String> newPassword) {
        System.out.println("t "+ token);
        System.out.println("p "+newPassword.get("newPassword"));
        return authenticationService.resetPassword(token, newPassword.get("newPassword"));
//         ResponseEntity.ok("Password successfully reset");
    }
}