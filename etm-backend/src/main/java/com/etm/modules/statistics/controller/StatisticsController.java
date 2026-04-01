package com.etm.modules.statistics.controller;

import com.etm.common.result.Result;
import com.etm.modules.statistics.service.StatisticsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/overview")
    public Result<Map<String, Object>> getOverview() {
        return Result.ok(statisticsService.getOverview());
    }

    @GetMapping("/teacher")
    public Result<Map<String, Object>> getTeacherStatistics(@RequestParam(defaultValue = "college") String dimension) {
        return Result.ok(statisticsService.getTeacherStatistics(dimension));
    }

    @GetMapping("/salary")
    public Result<Map<String, Object>> getSalaryStatistics(
            @RequestParam Integer year,
            @RequestParam(required = false) Long collegeId) {
        return Result.ok(statisticsService.getSalaryStatistics(year, collegeId));
    }

    @GetMapping("/assessment")
    public Result<Map<String, Object>> getAssessmentStatistics() {
        return Result.ok(statisticsService.getAssessmentStatistics());
    }

    @GetMapping("/export")
    public void exportReport(
            @RequestParam String type,
            @RequestParam String startDate,
            @RequestParam String endDate,
            HttpServletResponse response) {
        statisticsService.exportReport(type, startDate, endDate, response);
    }
}
