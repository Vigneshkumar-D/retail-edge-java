package com.retailedge.dto.user;

import com.retailedge.entity.user.Role;
import com.retailedge.entity.user.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
    private Integer roleId;
    private String password;
    private Boolean active;
    private MultipartFile profileImage;
}
