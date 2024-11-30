package com.retailedge.repository.service;

import com.retailedge.entity.service.PaidService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PaidServiceRepository extends JpaRepository<PaidService,Integer>, JpaSpecificationExecutor<PaidService> {
}
