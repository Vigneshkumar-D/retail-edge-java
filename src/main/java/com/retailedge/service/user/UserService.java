package com.retailedge.service.user;

import com.retailedge.dto.user.*;
import com.retailedge.entity.user.*;
import com.retailedge.repository.user.*;
import com.retailedge.service.authentication.EmailService;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired(required = true)
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ModelMapper modelMapper; // Injecting ModelMapper

    // Create a new user
    public User updateUser(Integer userId, UserDTO userDTO) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }
        Role role = roleRepository.findById(userDTO.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setPropertyCondition(conditions -> {
            return conditions.getSource() != null;
        });
        modelMapper.map(userDTO, user);
        if(role!=null){
            user.setRole(role);
        }
        System.out.println("Role "+ role.getRoleName());


        return userRepository.save(user);
    }

    public User createUser(UserDTO userDTO) {
        // Find the role by ID
        Role role = roleRepository.findById(userDTO.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Create a new user and map fields
        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);
        user.setActive(true);
        User savedUser = userRepository.save(user);
        this.sentWelcomeMail(savedUser);
        return savedUser;
    }


    public User getUserById(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public boolean deleteUser(Integer userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    public String encodePassword(String password) {
        return this.passwordEncoder.encode(password);
    }

    public User getCurrentUser(String username){
        return userRepository.findByUsername(username).get();
    }

    @PostConstruct
    public void addUsers() {
       
        List<User> users = Arrays.asList(
                new User(1, "System User", this.roleService.getSystem(), this.encodePassword("123456"), true, "system@example.com", "9876543210"),
                new User(2, "Admin", this.roleService.getSuperUser(), this.encodePassword("Admin@123"), true, "vigneshkumar.d2797@gmail.com", "9876543220"),
                new User(3, "Anonymous User", this.roleService.getGuest(), this.encodePassword("123456"), true, "anonymous@example.com", "9876543230")
        );
        
        for (User user : users) {
        userRepository.findByUsername(user.getUsername())
                      .ifPresentOrElse(
                          existing -> System.out.println("User " + user.getUsername() + " already exists. "+ user.getPassword()),
                          () -> userRepository.save(user)
                      );
        }
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof String) {
                // Principal is the username; fetch User by username
                return userRepository.findByUsername((String) principal).get();
            } else if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
                // Principal is a UserDetails object; fetch User by username
                String username = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
                return userRepository.findByUsername(username).get();
            }
        }
        return null; // Return null if not authenticated or unable to retrieve user
    }

    public void sentWelcomeMail(User user){
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
                "<h3>Welcome, " + user.getUsername() + "!</h3>" +
                "<p>We are excited to have you join RetailEdge! Your account has been successfully created. Here are your login credentials:</p>" +
                "<p><strong>Username:</strong> " + user.getUsername() + "</p>" +
                "<p><strong>Password:</strong> " + user.getPassword() + "</p>" +
                "<p>Click the button below to log in and start exploring:</p>" +
                "<div style='text-align: center; margin: 20px 0;'>" +
                "<a href='https://retail-edge.netlify.app/login' style='background: #007bff; color: #fff; padding: 10px 20px; border-radius: 5px; font-size: 16px;'>Log In to Your Account</a>" +
                "</div>" +
                "<p>If you wish to set a new password, you can use the 'Forgot Password' option on the login page to reset it at any time.</p>" +
                "<p>If you have any questions or need assistance, feel free to reach out to our support team.</p>" +
                "<p>We hope you enjoy using RetailEdge!</p>" +
                "<p>Regards,<br>Team RetailEdge</p>" +
                "</div>" +
                "</body>" +
                "</html>";
        emailService.sendEmail(user.getEmail(), "Welcome to RetailEdge! Get Started Today", body );
    }

}
