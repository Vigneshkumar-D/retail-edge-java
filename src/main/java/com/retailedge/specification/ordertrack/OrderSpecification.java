package com.retailedge.specification.ordertrack;

import com.retailedge.entity.ordertrack.Order;
import com.retailedge.specification.SearchCriteria;
import com.retailedge.specification.generic.GenericSpecification;

public class OrderSpecification extends GenericSpecification<Order> {
    public OrderSpecification(SearchCriteria criteria) {
        super(criteria);
    }
}
