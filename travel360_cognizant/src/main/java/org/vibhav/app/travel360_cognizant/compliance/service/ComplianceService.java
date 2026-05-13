package org.vibhav.app.travel360_cognizant.compliance.service;



import org.vibhav.app.travel360_cognizant.compliance.entity.ComplianceReport;
import java.util.List;

public interface ComplianceService {

    ComplianceReport createReport(ComplianceReport report);

    List<ComplianceReport> getAllReports();

    ComplianceReport getReportById(Long id);

    void deleteReport(Long id);
}

