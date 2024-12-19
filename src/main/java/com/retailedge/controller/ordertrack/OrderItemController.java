package com.retailedge.controller.ordertrack;

import com.retailedge.dto.ordertrack.OrderItemDto;
import com.retailedge.entity.ordertrack.OrderItem;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.ordertrack.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/order-item")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping
    public ResponseEntity<ResponseModel<?>> list(){
        return orderItemService.list();
    }

    @PostMapping
    public  ResponseEntity<ResponseModel<?>> add(@RequestBody List<OrderItemDto> orderItemDto, @PathVariable("order_id") Long orderId){
        return orderItemService.add(orderItemDto, orderId);
    }

    @PutMapping("/{orderItemId}")
    public ResponseEntity<ResponseModel<?>> update(@PathVariable("orderItemId") Integer orderItemId, @RequestBody OrderItemDto orderItemDto){
        return orderItemService.update(orderItemId, orderItemDto);
    }

    @DeleteMapping("/{orderItemId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable("orderItemId") Integer orderItemId) throws Exception {
       return orderItemService.delete(orderItemId);
    }

}
