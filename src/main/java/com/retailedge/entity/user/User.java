package com.retailedge.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;

@Table(name = "user_detail")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "Password may not be blank")
    @JsonIgnore
    private String password; // Store hashed password

    @Column(nullable = false, unique = true)
    @Email(message = "Email should be valid")
    private String email;

    @Column(nullable = false, unique = true)
    private String mobileNumber;

    @ManyToOne
    @JoinColumn(name = "roleId", referencedColumnName = "id")
    private Role role;

    @Column(nullable = false)
    private boolean active; // To manage session activity

    private Instant lastLogin;

    public User(int i, String user, Role role, String password, boolean active, String email, String mobileNumber) {
        this.id = i;
        this.username = user;
        this.role= role;
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.active = active;
        this.email = email;
    }
}
