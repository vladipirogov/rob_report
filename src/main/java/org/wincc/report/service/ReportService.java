package org.wincc.report.service;

import org.wincc.report.model.Report;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ReportService {

    List<Report> getRecordsByPeriod(LocalDateTime dateStart, LocalDateTime dateEnd);

    void generateReport(HttpServletResponse response, Map<String, Object> parameters, boolean print);
}
