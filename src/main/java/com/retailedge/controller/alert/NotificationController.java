package com.retailedge.controller.alert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alerts")
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotification(String message) {
        try {
            messagingTemplate.convertAndSend("/topic/notifications", message);
            logger.info("Notification sent successfully: {}", message);
        } catch (Exception e) {
            logger.error("Error sending notification: {}", e.getMessage());
        }
    }

    @PostMapping("/notify")
    public ResponseEntity notify(@RequestBody String message) {
        sendNotification(message);
        return ResponseEntity.ok("Notification sent successfully!");
    }
}
