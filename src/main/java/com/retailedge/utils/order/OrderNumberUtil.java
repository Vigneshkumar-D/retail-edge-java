package com.retailedge.utils.order;

import com.retailedge.repository.ordertrack.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
public class OrderNumberUtil {

    @Autowired
    private OrderRepository orderRepository;

    public String generateOrderNumber() {
        Integer orderCounter = orderRepository.findAll().size()+1;
        YearMonth yearMonth = YearMonth.now();
        String year = String.valueOf(yearMonth.getYear()).substring(2); // Last two digits of the current year
        return "2K" + year + "-ORD" + String.format("%04d", orderCounter);
    }

}
