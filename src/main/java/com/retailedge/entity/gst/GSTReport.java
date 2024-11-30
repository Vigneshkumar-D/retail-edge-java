package com.retailedge.entity.gst;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "gst_report")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GSTReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant startDate;

    private Instant endDate;

    private BigDecimal totalSGST;

    private BigDecimal totalCGST;

    private BigDecimal totalIGST;

    private BigDecimal totalTax;

}
