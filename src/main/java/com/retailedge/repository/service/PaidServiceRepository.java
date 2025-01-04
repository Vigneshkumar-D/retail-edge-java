package com.retailedge.repository.service;

import com.retailedge.entity.service.PaidService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaidServiceRepository extends JpaRepository<PaidService,Integer>, JpaSpecificationExecutor<PaidService> {
    @Query(value = "SELECT * FROM paid_service_details " +
            "WHERE (service_date BETWEEN :startOfDay AND :endOfDay) " +
            "OR (service_completion_date BETWEEN :startOfDay AND :endOfDay)",
            nativeQuery = true)
    List<PaidService> findPaidServiceBetweenDates(@Param("startOfDay") Instant startOfDay, @Param("endOfDay") Instant endOfDay);

    @Query("SELECT s FROM PaidService s " +
            "WHERE CAST(s.serviceDate AS date) = CAST(:date AS date) " +
            "OR CAST(s.serviceCompletionDate AS date) = CAST(:date AS date)")
    List<PaidService> findServiceByExactDate(@Param("date") LocalDate date);



}
