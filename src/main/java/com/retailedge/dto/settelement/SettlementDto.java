package com.retailedge.dto.settelement;

import com.retailedge.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SettlementDto {

    private Instant settlementDate;

    private User user;

    private Double sales;

    private Double service;

    private Double ec;

    private Double expenses;

    private Double previousDayCash;

    private Double totalCash;

    private Double netCash;

    private Double shortage;

}
