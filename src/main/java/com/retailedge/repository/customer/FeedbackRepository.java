package com.retailedge.repository.customer;

import com.retailedge.entity.customer.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback,Integer>, JpaSpecificationExecutor<Feedback> {

//    @Query("SELECT f FROM Feedback f " +
//            "JOIN f.purchase p " +
//            "JOIN p.customer c " +
//            "WHERE c.id = :customerId")
//    List<Feedback> findByCustomerId(@Param("customerId") Integer customerId);
}
