package com.retailedge.repository.credit;


import com.retailedge.entity.credit.CreditReminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditReminderRepository extends JpaRepository<CreditReminder,Integer>, JpaSpecificationExecutor<CreditReminder> {

}
