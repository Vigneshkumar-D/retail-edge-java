package com.retailedge.entity.inventory;

import com.retailedge.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "stock_report_history")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StockReportHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Instant timestamp;

    private Boolean shortage;

    @ElementCollection
    @CollectionTable(name = "shortage_products", joinColumns = @JoinColumn(name = "stock_report_history_id"))
    private List<ShortageProducts> shortProducts;

    private String remark;
}
