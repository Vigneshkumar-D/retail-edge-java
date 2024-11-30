//package com.retailedge.kafka;
//
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//public class KafkaProducerService {
//
//    private static String TOPICS = "low-stock-alert, order-alert, credit-reminder, emi-alert";
//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    public void sendMessage(String message) {
//        kafkaTemplate.send(TOPICS, message);
//        System.out.println("Message sent to Kafka topic: " + message);
//    }
//}





package com.retailedge.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendLowStockAlert(String message) {
        sendMessage("low-stock-alert", message);
    }

    public void sendOrderAlert(String message) {
        sendMessage("order-alert", message);
    }

    public void sendCreditReminder(String message) {
        sendMessage("credit-reminder", message);
    }

    public void sendEmiAlert(String message) {
        sendMessage("emi-alert", message);
    }

    private void sendMessage(String topic, String message) {
        try {
            kafkaTemplate.send(topic, message).get(); // Wait for the send to complete
            System.out.println("Message sent to Kafka topic '" + topic + "': " + message);
        } catch (Exception e) {
            System.err.println("Failed to send message to topic '" + topic + "': " + e.getMessage());
            e.printStackTrace();
        }
    }
}
