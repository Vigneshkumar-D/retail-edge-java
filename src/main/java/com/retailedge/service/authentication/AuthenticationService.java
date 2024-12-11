package com.retailedge.service.authentication;

import com.retailedge.entity.authentication.TokenBlacklist;
import com.retailedge.entity.password.PasswordManagement;
import com.retailedge.entity.user.User;
import com.retailedge.model.ResponseModel;
import com.retailedge.model.user.TokenModel;
import com.retailedge.repository.authentication.PasswordManagementRepository;
import com.retailedge.repository.authentication.TokenBlacklistRepository;
import com.retailedge.repository.user.UserRepository;
import com.retailedge.utils.user.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private JWTUtil jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordManagementRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository;

    @Autowired
    private JWTUtil jwtUtil;

    public String verify(TokenModel user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            Optional<User> user1 = userRepository.findByUsername(user.getUsername());
            user1.get().setLastLogin(Instant.now());
            userRepository.save(user1.get());
            return jwtService.generateToken(user.getUsername());
        } else {
            return "Authentication failed";
        }
    }

    public ResponseEntity<String> logout(String authorization) {
        String jwtToken = authorization.startsWith("Bearer ") ? authorization.substring(7) : authorization;
        Date expirationDate = jwtUtil.extractExpiration(jwtToken);
        LocalDateTime localDateTime = expirationDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        TokenBlacklist blacklistedToken = new TokenBlacklist(jwtToken, localDateTime);
        tokenBlacklistRepository.save(blacklistedToken);
        return ResponseEntity.ok("Logged out successfully");
    }


    public ResponseEntity<String> sendResetPasswordLink(String email) {
        System.out.println("email "+ email);
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
//            throw new RuntimeException("User with email not found");
             return   ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User with email not found");
        }


        User user = userOptional.get();

        String token = jwtUtil.generatePasswordResetToken(user.getUsername());
        PasswordManagement resetToken = new PasswordManagement();
        resetToken.setToken(token);
        resetToken.setExpiration(LocalDateTime.now().plusMinutes(10)); // Token valid for 1 hour
        resetToken.setUser(user);

        tokenRepository.save(resetToken);
        String rootUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "") + request.getContextPath();
        String body = "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #f9f9f9; color: #333; margin: 0; padding: 0; }" +
                ".container { max-width: 600px; margin: 20px auto; background: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }" +
                "h3 { color: #007bff; }" +
                "p { font-size: 14px; line-height: 1.5; }" +
                "a { color: #007bff; text-decoration: none; }" +
                "a:hover { text-decoration: underline; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<h3>Hello " + user.getUsername() + ",</h3>" +
                "<p>You recently requested to reset your password for your RetailEdge account. Click the button below to reset it:</p>" +
                "<div style='text-align: center; margin: 20px 0;'>" +
                "<a href=https://retail-edge.netlify.app/reset-password?token="+ token + "' style='background: #007bff; color: #fff; padding: 10px 20px; border-radius: 5px; font-size: 16px;'>Reset Your Password</a>" +
                "</div>" +
                "<p>If you did not request a password reset, please ignore this email or contact support if you have any concerns.</p>" +
                "<p>This link is valid for 10 minutes.</p>" +
                "<p>Regards,<br>Team RetailEdge</p>" +
                "</div>" +
                "</body>" +
                "</html>";

        emailService.sendEmail(user.getEmail(), "Password Reset", body );
        return ResponseEntity.ok("Password reset link sent to your email");
    }

    public ResponseEntity<String> resetPassword(String token, String newPassword) {
        PasswordManagement resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid or expired lonk"));

        if (resetToken.isExpired()) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Link Expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(resetToken); // Clean up token after successful password reset
       return ResponseEntity.status(HttpStatus.OK).body("Password successfully reset");
    }


//    public ResponseEntity<ResponseModel<?>> delete(Integer supplierId) throws Exception {
//
//        try {
//            if (!supplierRepository.existsById(supplierId)) {
//                // Return 404 Not Found
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body(new ResponseModel<>(false, "Supplier not found", 404));
//            }
//            supplierRepository.deleteById(supplierId);
//            // Return 200 OK if the category is deleted successfully
//            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
//        } catch (Exception e) {
//            // Return 500 Internal Server Error for any unexpected errors
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ResponseModel<>(false, "Error deleting supplier: " + e.getMessage(), 500));
//        }
//    }

}
