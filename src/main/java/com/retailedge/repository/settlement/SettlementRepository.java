package com.retailedge.repository.settlement;

import com.retailedge.entity.settlement.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface SettlementRepository extends JpaRepository<Settlement,Integer>, JpaSpecificationExecutor<Settlement> {
}
