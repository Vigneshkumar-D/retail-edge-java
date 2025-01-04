package com.retailedge.repository.store;

import com.retailedge.entity.store.AccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDetailsRepository extends JpaRepository<AccountDetails,Integer>, JpaSpecificationExecutor<AccountDetails> {
}

