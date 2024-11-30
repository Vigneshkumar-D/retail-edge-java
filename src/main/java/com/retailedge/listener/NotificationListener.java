//package com.retailedge.listener;
//
////import com.bizedge.common.handler.NotificationWebSocketHandler;
//import com.retailedge.handler.NotificationWebSocketHandler;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//
//@Service
//public class NotificationListener {
//
//    private final NotificationWebSocketHandler webSocketHandler;
//
//    public NotificationListener(NotificationWebSocketHandler webSocketHandler) {
//        this.webSocketHandler = webSocketHandler;
//    }
//
//    // Listener for Low Stock Alerts
//    @KafkaListener(topics = "low-stock-alert", groupId = "notification-group")
//    public void listenLowStockAlerts(String message) {
//        System.out.println("Received Low Stock Alert: " + message);
//        // Add your logic to handle low stock alerts, e.g., notify the inventory manager
//        sendNotification(message, "Low Stock Alert");
//    }
//
//    // Listener for Order Alerts
//    @KafkaListener(topics = "order-alert", groupId = "notification-group")
//    public void listenOrderAlerts(String message) {
//        System.out.println("Received Order Alert: " + message);
//        // Add your logic to handle order alerts, e.g., notify the sales team
//        sendNotification(message, "Order Alert");
//    }
//
//    // Listener for Credit Reminders
//    @KafkaListener(topics = "credit-reminder", groupId = "notification-group")
//    public void listenCreditReminders(String message) {
//        System.out.println("Received Credit Reminder: " + message);
//        // Add your logic to handle credit reminders, e.g., notify the customer
//        sendNotification(message, "Credit Reminder");
//    }
//
//    // Listener for EMI Alerts
//    @KafkaListener(topics = "emi-alert", groupId = "notification-group")
//    public void listenEmiAlerts(String message) {
//        System.out.println("Received EMI Alert: " + message);
//        // Add your logic to handle EMI alerts, e.g., notify the finance team
//        sendNotification(message, "EMI Alert");
//    }
//
//    // Method to handle notification sending logic
//    private void sendNotification(String message, String alertType) {
//        // Here, you can integrate with your notification service
//        // For example, sending an in-app notification, email, SMS, etc.
//        System.out.printf("Sending %s: %s%n", alertType, message);
//        // Implement your notification logic here (e.g., call NotificationController)
//    }
//
//
//}
