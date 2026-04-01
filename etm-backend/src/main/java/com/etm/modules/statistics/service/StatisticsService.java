package com.etm.modules.statistics.service;

import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface StatisticsService {

    Map<String, Object> getOverview();

    Map<String, Object> getTeacherStatistics(String dimension);

    Map<String, Object> getSalaryStatistics(Integer year, Long collegeId);

    Map<String, Object> getAssessmentStatistics();

    void exportReport(String type, String startDate, String endDate, HttpServletResponse response);
}
