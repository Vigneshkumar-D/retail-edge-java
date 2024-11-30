package com.retailedge.dto.gst;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GSTReportDto {

    private Instant startDate;

    private Instant endDate;

    private BigDecimal totalSGST;

    private BigDecimal totalCGST;

    private BigDecimal totalIGST;

    private BigDecimal totalTax;

}
