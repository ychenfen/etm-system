package com.etm.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class DashboardDto {
    private long teacherCount;
    private long courseCount;
    private long departmentCount;
    private long pendingHireCount;
    private long pendingSalaryCount;
    private List<Map<String, Object>> departmentTeacherStats;
    private List<Map<String, Object>> monthlyAttendanceStats;
    private List<Map<String, Object>> hireStatusStats;
}
