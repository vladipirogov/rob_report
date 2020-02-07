package org.wincc.report.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wincc.report.model.MainReportDto;
import org.wincc.report.model.Report;
import org.wincc.report.service.ReportService;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class MainController {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Autowired
    private ReportService reportService;

    @RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST })
    public String index(Model model) {
        LocalDateTime dateStart = LocalDate.now().atStartOfDay();
        LocalDateTime dateEnd = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        List<Report> reportList = reportService.getRecordsByPeriod(dateStart, dateEnd);
        model.addAttribute("reports", reportList);
        model.addAttribute("model", new MainReportDto());
        return "index";
    }

    @PostMapping(path = "/find_updates")
    @ResponseBody
    public List<Report> getUpdates(@RequestBody MainReportDto dto) {
        List<Report> result = null;
        try {
            LocalDateTime dateStart = getDateStart(dto.getDateInput());
            LocalDateTime dateEnd = getDateStart(dto.getEndDateInput());
            result = reportService.getRecordsByPeriod(dateStart, dateEnd);
        }
        catch (DateTimeParseException ex) {
            log.error(ex.getMessage());
        }
        return result;
    }

    @PostMapping(path = "/report/main", produces="application/pdf;charset=UTF-8")
    public void report(HttpServletResponse response, @ModelAttribute(value="model") MainReportDto model) {
        response.setContentType("text/html");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("date_time", model.getDateInput());
        parameters.put("end_date_time", model.isEndDate() ? model.getEndDateInput() : model.getDateInput());
        reportService.generateReport(response, parameters, model.isPrint());
    }

    private LocalDateTime getDateStart(String inputDateStart) throws DateTimeParseException {
        if (inputDateStart == null || inputDateStart.isEmpty())
            return LocalDate.now().atStartOfDay();
        return LocalDateTime.parse(inputDateStart, timeFormatter);
    }
}
