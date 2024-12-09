package com.retailedge.service.user;

import com.retailedge.dto.user.*;
import com.retailedge.entity.user.*;
import com.retailedge.repository.user.*;
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
//        user.setRoleId(userDTO.getRoleId());
        user.setActive(true);
//        user.setLastLogin(Instant.now());
        return userRepository.save(user);
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
                new User(2, "Admin", this.roleService.getSuperUser(), this.encodePassword("123456"), true, "vigneshkumar.d2797@gmail.com", "9876543220"),
                new User(3, "Anonymous User", this.roleService.getGuest(), this.encodePassword("123456"), true, "anonymous@example.com", "9876543230")
        );

        for (User user : users) {
            try {
                if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                    userRepository.save(user);
                } else {
                    System.out.println("User " + user.getUsername() + " already exists.");
                }
            } catch (DataIntegrityViolationException ex) {
                System.err.println("Data integrity violation while saving user " + user.getUsername() + ": " + ex.getMessage());
            }
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

}
