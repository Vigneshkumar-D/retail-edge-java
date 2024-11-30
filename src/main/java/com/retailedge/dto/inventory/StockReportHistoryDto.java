package com.retailedge.dto.inventory;

import com.retailedge.entity.inventory.ShortageProducts;
import com.retailedge.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StockReportHistoryDto {

    private User user;

    private Instant timestamp;

    private Boolean shortage;

    private List<ShortageProducts> shortProducts;

    private String remark;
}
