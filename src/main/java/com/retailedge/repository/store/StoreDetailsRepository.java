package com.retailedge.repository.store;

import com.retailedge.entity.service.PaidService;
import com.retailedge.entity.store.StoreDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreDetailsRepository extends JpaRepository<StoreDetails,Integer>, JpaSpecificationExecutor<StoreDetails> {
}

