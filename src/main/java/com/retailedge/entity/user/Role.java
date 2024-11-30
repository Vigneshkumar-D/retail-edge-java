package com.retailedge.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "user_role")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(unique = true)
    @NotBlank(message = "Role name may be blank")
    private String roleName;

    @Column(columnDefinition = "boolean default true")
    private boolean active;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Instant createdOn;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Instant updatedOn;

    public Role(Integer id, String roleName){
        this.id= id;
        this.roleName=roleName;
        this.active=true;
    }
    @SneakyThrows
    @PreRemove
    private void preRemove(){
        List<String> roles = List.of("Super User","Manager","Guest");
        if(roles.contains(this.roleName)){
            throw new RuntimeException("Predefined Role : \""+this.roleName+"\" can't be deleted");
        }
    }
}
