package com.retailedge.service.ordertrack;

import com.retailedge.dto.ordertrack.OrderDto;
import com.retailedge.dto.ordertrack.OrderItemDto;
import com.retailedge.entity.ordertrack.Order;
import com.retailedge.entity.ordertrack.OrderItem;
import com.retailedge.entity.user.User;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.ordertrack.OrderRepository;
import com.retailedge.repository.user.UserRepository;
import com.retailedge.service.customer.CustomerService;
import com.retailedge.specification.ordertrack.OrderSpecificationBuilder;
import com.retailedge.utils.order.OrderNumberUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderNumberUtil orderNumberUtil;

    @Autowired
    private UserRepository userRepository;


    public  List<Order> list(OrderSpecificationBuilder builder){
        return orderRepository.findAll(builder.build());
    }

    public Order add(OrderDto orderDto) {
        Order order = modelMapper.map(orderDto, Order.class);
        order.setCustomer(customerService.add(orderDto.getCustomer()));
        order.setOrderNumber(orderNumberUtil.generateOrderNumber());
        order.setCreatedDate(Instant.now());
        Optional<User> user = userRepository.findById(orderDto.getUser().getId());
        order.setUser(user.get());
        if (order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
                item.setOrder(order);
            }
        }
        return orderRepository.save(order);
    }
//
    @Transactional
    public Order update(Integer orderId, OrderDto orderDto) {
        // Fetch the existing order
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        // Map the non-collection properties from orderDto to order using ModelMapper
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setPropertyCondition(conditions -> conditions.getSource() != null);
        modelMapper.map(orderDto, order);

        // Set the user for the order
        Optional<User> user = userRepository.findById(orderDto.getUser().getId());
        order.setUser(user.orElseThrow(() -> new RuntimeException("User not found")));

        if (order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
                item.setOrder(order);
            }
        }

        // Update the updated date
        order.setUpdatedDate(Instant.now());

        return orderRepository.save(order);
    }

//    public List<OrderItem> updateOrderItem(List<OrderItemDto> orderItemDtos){
//
//            List<OrderItem> updatedItems = new ArrayList<>();
//
//            // Go through each item in the DTO
//            for (OrderItemDto itemDto : orderItemDtos) {
//                Optional<OrderItem> existingItem = order.getOrderItems().stream()
//                        .filter(item -> item.getId().equals(itemDto.getId())) // Find the matching item by ID
//                        .findFirst();
//
//                if (existingItem.isPresent()) {
//                    // If the item exists, update it
//                    OrderItem item = existingItem.get();
//                    item.setQuantity(itemDto.getQuantity());
//                    item.setPrice(itemDto.getPrice());
//                    updatedItems.add(item); // Add the updated item to the list
//                } else {
//                    // If it's a new item, create it and associate with the order
//                    OrderItem newItem = new OrderItem();
//                    newItem.setProductName(itemDto.getProductName());
//                    newItem.setQuantity(itemDto.getQuantity());
//                    newItem.setPrice(itemDto.getPrice());
//                    newItem.setOrder(order); // Make sure to set the order reference
//                    updatedItems.add(newItem); // Add the new item to the list
//                }
//            }
//
//            return updatedItems;
//    }

    public ResponseEntity<ResponseModel<?>> delete(Integer orderId) throws Exception {
        try {
            if (!orderRepository.existsById(orderId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Order not found", 404));
            }
            orderRepository.deleteById(orderId);
            // Return 200 OK if the category is deleted successfully
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting Order: " + e.getMessage(), 500));
        }
    }

}
