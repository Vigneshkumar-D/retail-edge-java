package com.retailedge.repository.store;

import com.retailedge.entity.store.GSTDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GSTDetailsRepository extends JpaRepository<GSTDetails,Integer>, JpaSpecificationExecutor<GSTDetails> {
}

