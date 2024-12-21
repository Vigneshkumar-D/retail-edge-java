package com.retailedge.repository.supplier;

import com.retailedge.entity.suppiler.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails,Integer>, JpaSpecificationExecutor<PaymentDetails> {
    @Query(value = "SELECT p FROM PaymentDetails p WHERE p.supplier.id = :supplierId ORDER BY p.paymentDate DESC LIMIT 3")
    List<PaymentDetails> findTop3BySupplierIdOrderByPaymentDateDesc(@Param("supplierId") Integer supplierId);

    @Query("SELECT p FROM PaymentDetails p WHERE p.supplier.id = :supplierId AND p.paymentStatus = 'COMPLETED' ORDER BY p.paymentDate DESC")
    List<PaymentDetails> findTopBySupplierIdAndPaymentStatusOrderByPaymentDateDesc(@Param("supplierId") Integer supplierId);

}
