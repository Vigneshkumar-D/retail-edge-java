package com.retailedge.dto.user;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoleDTO {
    private String roleName;
    private Boolean active;
}
