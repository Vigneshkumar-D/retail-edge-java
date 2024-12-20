package com.retailedge.service.ordertrack;

import com.retailedge.dto.ordertrack.OrderDto;
import com.retailedge.dto.ordertrack.OrderItemDto;
import com.retailedge.entity.customer.Customer;
import com.retailedge.entity.ordertrack.Order;
import com.retailedge.entity.ordertrack.OrderItem;
import com.retailedge.entity.user.User;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.customer.CustomerRepository;
import com.retailedge.repository.ordertrack.OrderItemRepository;
import com.retailedge.repository.ordertrack.OrderRepository;
import com.retailedge.repository.user.UserRepository;
import com.retailedge.service.customer.CustomerService;
import com.retailedge.specification.ordertrack.OrderSpecificationBuilder;
import com.retailedge.specification.service.WarrantyServiceSpecificationBuilder;
import com.retailedge.utils.ExceptionHandler.ExceptionHandlerUtil;
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
    private CustomerRepository customerRepository;

    @Autowired
    private OrderNumberUtil orderNumberUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExceptionHandlerUtil exceptionHandlerUtil;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public ResponseEntity<ResponseModel<?>> list(OrderSpecificationBuilder builder){
        try{
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, orderRepository.findAll(builder.build())));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error retrieving order details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> add(OrderDto orderDto) {
        try{
            Order order = modelMapper.map(orderDto, Order.class);
            Customer customer = customerRepository.findByPhoneNumber(orderDto.getCustomer().getPhoneNumber());
            if (customer == null) {
                customer = modelMapper.map(orderDto.getCustomer(), Customer.class);
                customer  = customerRepository.save(customer);
            }

            order.setCustomer(customer);
            order.setOrderNumber(orderNumberUtil.generateOrderNumber());
            order.setCreatedDate(Instant.now());
            Optional<User> user = userRepository.findById(orderDto.getUser().getId());
            order.setUser(user.get());
            if (order.getOrderItems() != null) {
                for (OrderItem item : order.getOrderItems()) {
                    item.setOrder(order);
                }
            }

            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, orderRepository.save(order)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error adding order details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }
//
//    @Transactional
//    public ResponseEntity<ResponseModel<?>> update(Integer orderId, OrderDto orderDto) {
//        try{
//            // Fetch the existing order
//            Optional<Order> optionalOrder = orderRepository.findById(orderId);
//            if(optionalOrder.isEmpty()){
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body(new ResponseModel<>(false, "Order details not found!", 500));
//            }
//            Order order = optionalOrder.get();
//            // Map the non-collection properties from orderDto to order using ModelMapper
//            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//            modelMapper.getConfiguration().setPropertyCondition(conditions -> conditions.getSource() != null);
//            modelMapper.map(orderDto, order);
//
//            // Set the user for the order
//            Optional<User> user = userRepository.findById(orderDto.getUser().getId());
//            order.setUser(user.orElseThrow(() -> new RuntimeException("User not found")));
//
////            if (order.getOrderItems() != null) {
////                for (OrderItem item : order.getOrderItems()) {
////                    item.setOrder(order);
////                }
////            }
//
//            // Update the updated date
//            order.setUpdatedDate(Instant.now());
//
//            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, orderRepository.save(order)));
//        }catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ResponseModel<>(false, "Error updating order details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
//        }
//    }

    @Transactional
    public ResponseEntity<ResponseModel<?>> update(Integer orderId, OrderDto orderDto) {
        try {
            // Fetch the existing order
            Optional<Order> optionalOrder = orderRepository.findById(orderId);
            if (optionalOrder.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseModel<>(false, "Order details not found!", 500));
            }
            Order order = optionalOrder.get();

            // Configure ModelMapper
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            modelMapper.getConfiguration().setPropertyCondition(conditions -> conditions.getSource() != null);
            modelMapper.typeMap(OrderDto.class, Order.class)
                    .addMappings(mapper -> mapper.skip(Order::setId));
            modelMapper.map(orderDto, order);

            // Set the user for the order
            Optional<User> user = userRepository.findById(orderDto.getUser().getId());
            order.setUser(user.orElseThrow(() -> new RuntimeException("User not found")));

            // Update order items
            if (orderDto.getOrderItems() != null) {
                List<OrderItem> updatedItems = orderDto.getOrderItems().stream()
                        .map(dto -> {
                            OrderItem item = dto.getId() != null
                                    ? orderItemRepository.findById(dto.getId()).orElse(new OrderItem())
                                    : new OrderItem();
                            modelMapper.map(dto, item);
                            item.setOrder(order); // Maintain bidirectional relationship
                            return item;
                        })
                        .collect(Collectors.toList());
                order.getOrderItems().clear();
                order.getOrderItems().addAll(updatedItems);
            }

            // Update the updated date
            order.setUpdatedDate(Instant.now());

            // Save and return
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, orderRepository.save(order)));
        } catch (Exception e) {
            e.printStackTrace(); // Log the stack trace
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error updating order details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
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
