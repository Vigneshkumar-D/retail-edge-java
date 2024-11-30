package com.retailedge.entity.ordertrack;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.retailedge.entity.customer.Customer;
import com.retailedge.entity.user.User;
import com.retailedge.enums.ordertrack.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;

    @CreatedBy
    @ManyToOne
    private User user;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status; // Enum for order status (e.g., PENDING, SHIPPED, DELIVERED)

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "advance_amount")
    private Double advanceAmount;

    @Column(name = "balance_amount")
    private Double balanceAmount;

    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @Column(name = "created_at", nullable = false)
//    @CreatedDate
    private Instant createdDate;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Instant updatedDate;

    @Column(name = "expected_delivery_at")
    private Instant expectedDeliveryDate;

}
