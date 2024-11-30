package com.retailedge.repository.ordertrack;

import com.retailedge.entity.ordertrack.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface OrderItemRepository extends JpaRepository<OrderItem,Integer>, JpaSpecificationExecutor<OrderItem> {
}
