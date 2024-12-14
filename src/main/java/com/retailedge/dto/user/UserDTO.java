package com.retailedge.dto.user;

import com.retailedge.entity.user.User;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDTO {
    private String username;
    private String email;
    private String mobileNumber;
    private User role;
    private String password;
    private Boolean active;
    private String imageUrl;
}
