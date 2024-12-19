package com.retailedge.service.ordertrack;

import com.retailedge.dto.ordertrack.OrderItemDto;
import com.retailedge.entity.ordertrack.Order;
import com.retailedge.entity.ordertrack.OrderItem;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.ordertrack.OrderItemRepository;
import com.retailedge.repository.ordertrack.OrderRepository;
import com.retailedge.specification.ordertrack.OrderSpecificationBuilder;
import com.retailedge.utils.ExceptionHandler.ExceptionHandlerUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ExceptionHandlerUtil exceptionHandlerUtil;

    public ResponseEntity<ResponseModel<?>> list(){
        try{
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, orderItemRepository.findAll()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error retrieving order item details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> add(List<OrderItemDto> orderItemDto, Long orderId) {
        try{
            Optional<Order> optionalOrder = orderRepository.findById(Math.toIntExact(orderId));
            if(optionalOrder.isEmpty()){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseModel<>(false, "Order details not found",500));

            }
            Order order = optionalOrder.get();
            List<OrderItem> orderItems = orderItemDto.stream()
                    .map(dto -> {
                        OrderItem orderItem = modelMapper.map(dto, OrderItem.class);
//                    orderItem.setOrder(order); // Set the Order for each OrderItem
                        return orderItem;
                    })
                    .toList();
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, orderItemRepository.saveAll(orderItems)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error adding order details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }



    public ResponseEntity<ResponseModel<?>> update(Integer orderItemId, OrderItemDto orderItemDto){
        try{
            OrderItem orderItem = orderItemRepository.findById(orderItemId).orElse(null);
            if (orderItem == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseModel<>(false, "Order Item Details not found!", 500));
            }
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            modelMapper.getConfiguration().setPropertyCondition(conditions -> {
                return conditions.getSource() != null;
            });
            modelMapper.map(orderItemDto, orderItem);

            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, orderItemRepository.save(orderItem)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error updating order item details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
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
