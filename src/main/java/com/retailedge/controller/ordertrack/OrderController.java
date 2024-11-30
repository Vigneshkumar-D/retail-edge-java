package com.retailedge.controller.ordertrack;


import com.retailedge.dto.ordertrack.OrderDto;
import com.retailedge.entity.ordertrack.Order;
import com.retailedge.entity.service.PaidService;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.ordertrack.OrderService;
import com.retailedge.specification.ordertrack.OrderSpecificationBuilder;
import com.retailedge.specification.service.PaidServiceSpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Order> list(
            @RequestParam(required = false, name = "id")Integer id,
            @RequestParam(required = false, name = "customerName")String customerName,
            @RequestParam(required = false, name = "phoneNumber") String phoneNumber,
            @RequestParam(required = false, name = "createdDate")Instant createdDate,
            @RequestParam(required = false, name = "status") String status,
            @RequestParam(required = false, name = "orderNumber") String orderNumber){

        OrderSpecificationBuilder builder=new OrderSpecificationBuilder();
        if(id!=null)builder.with("id",":",id);
        if(customerName!=null)builder.with("customer.name",":",customerName);
        if(phoneNumber!=null)builder.with("customer.phoneNumber",":",phoneNumber);
        if(createdDate!=null)builder.with("createdDate",":",createdDate);
        if(status!=null)builder.with("status",":",status);
        if(orderNumber!=null)builder.with("orderNumber",":",orderNumber);

        return orderService.list(builder);
    }


    @PostMapping
    public  Order add(@RequestBody OrderDto orderDto){
        return orderService.add(orderDto);
    }

    @PutMapping("/{orderId}")
    public Order update(@PathVariable("orderId") Integer orderId, @RequestBody OrderDto orderDto){
        return orderService.update(orderId, orderDto);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable("orderId") Integer orderId) throws Exception {
      return orderService.delete(orderId);
    }

}
