package com.retailedge.repository.service;

import com.retailedge.entity.service.WarrantyService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WarrantyServiceRepository extends JpaRepository<WarrantyService,Integer>, JpaSpecificationExecutor<WarrantyService> {
}