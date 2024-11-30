package com.retailedge.repository.supplier;

import com.retailedge.entity.suppiler.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier,Integer>, JpaSpecificationExecutor<Supplier> {
}
