package com.retailedge.dto.ordertrack;


import com.retailedge.dto.customer.CustomerDto;
import com.retailedge.entity.customer.Customer;
import com.retailedge.entity.ordertrack.OrderItem;
import com.retailedge.entity.user.User;
import com.retailedge.enums.ordertrack.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;

import java.time.Instant;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDto {

    private CustomerDto customer;

    private String orderNumber;

    private OrderStatus status;

    private Double advanceAmount;

    private User user;

    private Double balanceAmount;

    private Double totalAmount;

    private List<OrderItemDto> orderItems;

    private Instant createdDate;

    private Instant updatedDate;

    private Instant expectedDeliveryDate;

}
