package com.retailedge.repository.store;

import com.retailedge.entity.store.AccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AccountDetailsRepository extends JpaRepository<AccountDetails,Integer>, JpaSpecificationExecutor<AccountDetails> {
}

