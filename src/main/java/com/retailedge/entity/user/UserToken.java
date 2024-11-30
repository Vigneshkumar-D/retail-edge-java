package com.retailedge.entity.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.time.Instant;

@Entity
@Table(name="user_token")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(columnDefinition="TEXT")
    private String token;

    private Integer userId;

    private Instant expireOn;

    private Integer amount;

    private String unit;

    @ReadOnlyProperty
    @OneToOne(orphanRemoval = false)
    @JoinColumn(name = "id", referencedColumnName = "id",insertable = false,updatable = false)
    private User user;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Instant createdOn;

    @Column(columnDefinition = "boolean default true")
    private boolean active;
}
