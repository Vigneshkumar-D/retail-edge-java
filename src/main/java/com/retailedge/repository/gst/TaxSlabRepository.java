package com.retailedge.repository.gst;

import com.retailedge.entity.gst.TaxSlab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxSlabRepository extends JpaRepository<TaxSlab, Long>, JpaSpecificationExecutor<TaxSlab> {
    TaxSlab findByCategory_CategoryAndRegion(String category, String region);
    TaxSlab findByCategory_Id(Long id);
}
