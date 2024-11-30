package com.retailedge.repository.gst;

import com.retailedge.entity.gst.HSNCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HSNCodeRepository extends JpaRepository<HSNCode, Long>, JpaSpecificationExecutor<HSNCode> {
    HSNCode findByCode(String code);

    HSNCode findByTaxSlab_Category_Id(Long id);
}
