package com.retailedge.dto.customer;


import com.retailedge.entity.customer.Purchase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FeedbackDto {

    private Purchase purchase;

    private String comments;

    private Double rating;
}
