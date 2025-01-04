package com.retailedge.entity.settlement;

import com.retailedge.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "settlement")
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private User user;

    @Column(nullable = false)
    private Instant settlementDate;

    @Column(nullable = false)
    private Double sales;

    @Column(nullable = false)
    private Double service;

    @Column(nullable = false)
    private Double ec;

    @Column(nullable = false)
    private Double expenses;

    @Column(nullable = false)
    private Double previousDayCash;

    @Column(nullable = false)
    private Double totalCash;

    @Column(nullable = false)
    private Double netCash;

    @Column
    private Double shortage; // Can be null if there's no shortage

    public Double calculateFinalCash() {
        return netCash - (shortage != null ? shortage : 0.0);
    }

}
