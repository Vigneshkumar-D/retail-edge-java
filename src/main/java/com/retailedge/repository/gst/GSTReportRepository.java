package com.retailedge.repository.gst;

import com.retailedge.entity.gst.GSTReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GSTReportRepository extends JpaRepository<GSTReport, Long>, JpaSpecificationExecutor<GSTReport> {
}
