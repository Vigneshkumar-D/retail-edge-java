package com.retailedge.repository.inventory;

import com.retailedge.entity.inventory.Product;
import com.retailedge.enums.inventory.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer>, JpaSpecificationExecutor<Product> {
    boolean existsByCategoryId(Integer categoryId);
    List<Product> findAllByStatus(ProductStatus productStatus);

//    List<Product> findProductsSoldBetween(LocalDateTime startDate, LocalDateTime endDate);
}
