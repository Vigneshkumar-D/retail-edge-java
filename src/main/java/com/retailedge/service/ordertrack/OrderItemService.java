package com.retailedge.service.ordertrack;

import com.retailedge.dto.ordertrack.OrderItemDto;
import com.retailedge.entity.ordertrack.Order;
import com.retailedge.entity.ordertrack.OrderItem;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.ordertrack.OrderItemRepository;
import com.retailedge.repository.ordertrack.OrderRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<OrderItem> list(){
        return orderItemRepository.findAll();
    }

    public List<OrderItem> add(List<OrderItemDto> orderItemDto, Long orderId) {
        Order order = orderRepository.findById(Math.toIntExact(orderId))
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<OrderItem> orderItems = orderItemDto.stream()
                .map(dto -> {
                    OrderItem orderItem = modelMapper.map(dto, OrderItem.class);
//                    orderItem.setOrder(order); // Set the Order for each OrderItem
                    return orderItem;
                })
                .collect(Collectors.toList());

        return orderItemRepository.saveAll(orderItems);
    }



    public OrderItem update(Integer orderItemId, OrderItemDto orderItemDto){
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElse(null);
        if (orderItem == null) {
            throw new RuntimeException("Order Item Details not found with id: " + orderItemId);
        }
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setPropertyCondition(conditions -> {
            return conditions.getSource() != null;
        });
        modelMapper.map(orderItemDto, orderItem);

        return orderItemRepository.save(orderItem);
    }

    public ResponseEntity<ResponseModel<?>> delete(Integer orderItemId) throws Exception {
        try {
            if (!orderItemRepository.existsById(orderItemId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Order Item not found", 404));
            }
            orderItemRepository.deleteById(orderItemId);
            // Return 200 OK if the category is deleted successfully
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting Order Item: " + e.getMessage(), 500));
        }
    }

}
