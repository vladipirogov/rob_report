package org.wincc.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.wincc.report.model.Report;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query(value = "select r from Report r where r.dateStart between :date_start and :date_end order by r.routeRunId, r.elNum")
    List<Report> getRecordsByPeriod(@Param("date_start") LocalDateTime dateStart, @Param("date_end")LocalDateTime dateEnd);
}
