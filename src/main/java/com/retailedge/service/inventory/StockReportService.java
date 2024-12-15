package com.retailedge.service.inventory;


import com.retailedge.entity.inventory.NonMobileProduct;
import com.retailedge.entity.inventory.Product;
import com.retailedge.entity.inventory.StockReport;
import com.retailedge.enums.inventory.ProductStatus;
import com.retailedge.repository.inventory.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockReportService {

    @Autowired
    private ProductRepository productRepository;
    
    public List<StockReport> list(){
        List<Product> productList = productRepository.findAllByStatus(ProductStatus.IN_STOCK);
        List<StockReport> stockReportList = new ArrayList<>();
        for(Product product: productList){
            StockReport stockReport= new StockReport();
            stockReport.setProductName(product.getProductName());
            stockReport.setQuantity(product.getStockLevel());

            if (product instanceof NonMobileProduct nonMobileProduct) {
                if (nonMobileProduct.getBarcode() != null) {
                    stockReport.setIMEIorBarcode(nonMobileProduct.getBarcode());
                }else {
                    stockReport.setIMEIorBarcode(product.getImeiNumber());
                }
            }
        }
        return  stockReportList;
    }
}
