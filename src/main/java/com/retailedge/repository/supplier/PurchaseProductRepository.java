package com.retailedge.repository.supplier;

import com.retailedge.entity.suppiler.PurchaseProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface PurchaseProductRepository  extends JpaRepository<PurchaseProduct,Integer>, JpaSpecificationExecutor<PurchaseProduct> {
}
