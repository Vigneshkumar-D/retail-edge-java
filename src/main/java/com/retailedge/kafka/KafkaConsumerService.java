package com.retailedge.kafka;

import com.retailedge.controller.alert.NotificationController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @Autowired
    private NotificationController notificationController;

    @KafkaListener(topics = "low-stock-alert", groupId = "alert-group")
    public void consumeLowStockAlert(String message) {
        System.out.println("Received Low Stock Alert: " + message);
        sendNotification(message);
    }

    @KafkaListener(topics = "order-alert", groupId = "alert-group")
    public void consumeOrderAlert(String message) {
        System.out.println("Received Order Alert: " + message);
        sendNotification(message);
    }

    @KafkaListener(topics = "credit-reminder", groupId = "alert-group")
    public void consumeCreditReminder(String message) {
        System.out.println("Received Credit Reminder: " + message);
        sendNotification(message);
    }

    @KafkaListener(topics = "emi-alert", groupId = "alert-group")
    public void consumeEmiAlert(String message) {
        System.out.println("Received EMI Alert: " + message);
        sendNotification(message);
    }

    private void sendNotification(String message) {
        notificationController.sendNotification(message);
        System.out.println("Sending in-app notification: " + message);
    }

}
