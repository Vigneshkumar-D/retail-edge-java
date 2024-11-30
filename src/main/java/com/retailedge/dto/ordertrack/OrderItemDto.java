package com.retailedge.dto.ordertrack;


import com.retailedge.entity.ordertrack.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItemDto {

    private Integer id;

    private String productName;

    private String brand;

    private String variant;

    private Integer quantity;

    private Double price;

}
