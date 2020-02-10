package org.wincc.report.service;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.wincc.report.model.Report;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService{

    private static final String REP_FOLDER = "/static/report/";
    private static final String REP_EXT = ".jrxml";
    private static final String REPORT = "report";

    @Autowired
    private NamedParameterJdbcTemplate template;

    @Override
    public List<Report> getRecordsByPeriod(LocalDateTime dateStart, LocalDateTime dateEnd) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("date_start", dateStart);
        parameters.addValue("date_end", dateEnd);
        List<Report> result = template.query(getQuery(), parameters, (resultSet, i) -> {
            Report report = new Report();
            report.setRouteRunId(resultSet.getLong("route_run_id"));
            report.setRouteId(resultSet.getInt("route_id"));
            report.setDateStart(Optional.ofNullable(resultSet.getTimestamp("date_time_start")).map(date ->date.toLocalDateTime()).orElse(null));
            report.setDateEnd(Optional.ofNullable(resultSet.getTimestamp("date_time_end")).map(date ->date.toLocalDateTime()).orElse(null));
            report.setTimeWork(resultSet.getInt("time_work"));
            report.setElNum(resultSet.getInt("el_num"));
            report.setName(resultSet.getString("name"));
            report.setHrs(resultSet.getInt("hrs"));
            report.setAvrLoad(resultSet.getInt("avg_load"));
            report.setPrf(resultSet.getInt("prf"));
            report.setAlarm(resultSet.getString("id_alarm"));
            report.setPal(resultSet.getInt("pal"));
            report.setTMax(resultSet.getInt("t_max"));
            report.setTBeltMax(resultSet.getInt("t_belt_max"));
            return report;
        });
        return result;
    }

    @Override
    public void generateReport(HttpServletResponse response, Map<String, Object> parameters, boolean print) {
        try(InputStream inputStream = this.getClass().getResourceAsStream(REP_FOLDER + REPORT + REP_EXT);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ServletOutputStream servletOutputStream = response.getOutputStream()) {
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, template.getJdbcTemplate().getDataSource().getConnection());
            response.setContentType("application/pdf");
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Content-disposition", "filename=" + REPORT + "_" + LocalDate.now().toString() + ".pdf");
            JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
            response.setContentLength(baos.size());
            baos.writeTo(servletOutputStream);
            if (print == true)
                JasperViewer.viewReport(jasperPrint, false);
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (SQLException e) {
            log.error(e.getMessage());
        } catch (JRException e) {
            log.error(e.getMessage());
        }
    }

    /**
     *
     * @return
     */
    private String getQuery() {
       return "select " +
               "r.route_run_id,\n " +
               "\tcase when r.el_num = 1 then r.route_id else null end as route_id, \n" +
               "\tcase when r.el_num = 1 then r.date_time_start else null end as date_time_start,\n" +
               "\tcase when r.el_num = 1 then r.date_time_end else null end as date_time_end,\n" +
               "\tcase when r.el_num = 1 then r.time_work else null end as time_work, \n" +
               "\tr.el_num,\n" +
               "\tr.name,\n" +
               "\tr.hrs,\n" +
               "\tr.avg_load,\n" +
               "\tr.prf,\n" +
               "\tr.id_alarm,\n" +
               "\tr.pal,\n" +
               "\tr.t_max,\n" +
               "\tr.t_belt_max " +
               "from view_report r \n" +
               "where CAST(r.date_time_start AS DATE) BETWEEN convert(date,:date_start) AND convert(date,:date_end) \n" +
               "order by r.date_time_start, r.el_num";
    }

}
