package com.retailedge.service.kafka;

import com.retailedge.entity.inventory.Product;
import com.retailedge.kafka.KafkaProducerService;
import com.retailedge.repository.inventory.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private ProductRepository productRepository;

    public void sendNotification(String topic, String notification) {

        System.out.println("Notification sent to topic " + topic + ": " + notification);
        List<Product> productList = productRepository.findAll();
        if(productList.size()==0){
            kafkaProducerService.sendLowStockAlert(notification);
            System.out.println("Notification sent to topic 123" + topic + ": " + notification);
        }

    }
}
