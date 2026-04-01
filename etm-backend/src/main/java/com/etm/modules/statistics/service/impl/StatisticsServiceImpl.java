package com.etm.modules.statistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.etm.modules.assessment.mapper.RecordMapper;
import com.etm.modules.college.mapper.BaseCollegeMybatisMapper;
import com.etm.modules.course.mapper.ScheduleMapper;
import com.etm.modules.salary.mapper.SettlementMapper;
import com.etm.modules.statistics.service.StatisticsService;
import com.etm.modules.teacher.mapper.TeacherMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final TeacherMapper teacherInfoMapper;
    private final BaseCollegeMybatisMapper baseCollegeMapper;
    private final ScheduleMapper courseScheduleMapper;
    private final SettlementMapper salarySettlementMapper;
    private final RecordMapper assessmentRecordMapper;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Map<String, Object> getOverview() {
        Map<String, Object> result = new HashMap<>();

        long teacherCount = teacherInfoMapper.selectCount(new QueryWrapper<>());
        long activeTeacherCount = teacherInfoMapper.selectCount(
                new QueryWrapper<>().eq("teacher_status", 1)
        );
        long collegeCount = baseCollegeMapper.selectCount(new QueryWrapper<>());
        long courseCount = courseScheduleMapper.selectCount(new QueryWrapper<>());

        result.put("teacherCount", teacherCount);
        result.put("activeTeacherCount", activeTeacherCount);
        result.put("collegeCount", collegeCount);
        result.put("courseCount", courseCount);

        YearMonth now = YearMonth.now();
        LocalDate startOfMonth = now.atDay(1);
        LocalDate endOfMonth = now.atEndOfMonth();

        long newTeacherCount = teacherInfoMapper.selectCount(
                new QueryWrapper<>()
                        .ge("create_time", startOfMonth)
                        .le("create_time", endOfMonth)
        );

        long terminationCount = 0;
        String terminationSql = "SELECT COUNT(*) FROM termination_record WHERE termination_date >= ? AND termination_date <= ?";
        Integer termCount = jdbcTemplate.queryForObject(terminationSql, Integer.class, startOfMonth, endOfMonth);
        terminationCount = termCount != null ? termCount : 0;

        java.math.BigDecimal totalSalary = java.math.BigDecimal.ZERO;
        String salaryQuery = "SELECT COALESCE(SUM(total_amount), 0) FROM salary_settlement WHERE settlement_period LIKE ?";
        Object salaryResult = jdbcTemplate.queryForObject(salaryQuery, java.math.BigDecimal.class, now.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM")) + "%");
        if (salaryResult instanceof java.math.BigDecimal) {
            totalSalary = (java.math.BigDecimal) salaryResult;
        }

        Map<String, Object> monthlyStats = new HashMap<>();
        monthlyStats.put("newTeacherCount", newTeacherCount);
        monthlyStats.put("terminationCount", terminationCount);
        monthlyStats.put("totalSalary", totalSalary);

        result.put("monthlyStats", monthlyStats);
        return result;
    }

    @Override
    public Map<String, Object> getTeacherStatistics(String dimension) {
        Map<String, Object> result = new HashMap<>();
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();

        if ("college".equals(dimension)) {
            String sql = "SELECT college_name, COUNT(*) FROM teacher_info WHERE teacher_status = 1 GROUP BY college_id, college_name";
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
            for (Map<String, Object> row : rows) {
                labels.add((String) row.get("college_name"));
                data.add(((Number) row.get("COUNT(*)")).longValue());
            }
        } else if ("title".equals(dimension)) {
            String sql = "SELECT teacher_title, COUNT(*) FROM teacher_info WHERE teacher_status = 1 GROUP BY teacher_title";
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
            for (Map<String, Object> row : rows) {
                labels.add((String) row.get("teacher_title"));
                data.add(((Number) row.get("COUNT(*)")).longValue());
            }
        } else if ("status".equals(dimension)) {
            String sql = "SELECT teacher_status, COUNT(*) FROM teacher_info GROUP BY teacher_status";
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
            for (Map<String, Object> row : rows) {
                int status = ((Number) row.get("teacher_status")).intValue();
                String statusName = getStatusName(status);
                labels.add(statusName);
                data.add(((Number) row.get("COUNT(*)")).longValue());
            }
        }

        result.put("labels", labels);
        result.put("data", data);
        return result;
    }

    @Override
    public Map<String, Object> getSalaryStatistics(Integer year, Long collegeId) {
        Map<String, Object> result = new HashMap<>();
        List<String> months = new ArrayList<>();
        List<java.math.BigDecimal> amounts = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            months.add(i + "月");
            String monthStr = String.format("%04d-%02d", year, i);
            String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM salary_settlement WHERE settlement_period LIKE ?";
            java.math.BigDecimal amount = jdbcTemplate.queryForObject(sql, java.math.BigDecimal.class, monthStr + "%");
            amounts.add(amount != null ? amount : java.math.BigDecimal.ZERO);
        }

        result.put("months", months);
        result.put("amounts", amounts);
        return result;
    }

    @Override
    public Map<String, Object> getAssessmentStatistics() {
        Map<String, Object> result = new HashMap<>();
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();

        String sql = "SELECT grade, COUNT(*) FROM assessment_record GROUP BY grade";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        long total = 0;
        for (Map<String, Object> row : rows) {
            total += ((Number) row.get("COUNT(*)")).longValue();
        }

        for (Map<String, Object> row : rows) {
            String grade = (String) row.get("grade");
            long count = ((Number) row.get("COUNT(*)")).longValue();
            labels.add(grade + " (" + (total > 0 ? count * 100 / total : 0) + "%)");
            data.add(count);
        }

        result.put("labels", labels);
        result.put("data", data);
        return result;
    }

    @Override
    public void exportReport(String type, String startDate, String endDate, HttpServletResponse response) {
        try {
            response.setContentType("text/csv; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + type + "_report.csv\"");

            OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8);

            if ("teacher".equals(type)) {
                writer.write("教师ID,教师名称,学院,职称,状态,创建时间\n");
                String sql = "SELECT id, teacher_name, college_name, teacher_title, teacher_status, create_time FROM teacher_info WHERE create_time >= ? AND create_time <= ?";
                List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, startDate, endDate);
                for (Map<String, Object> row : rows) {
                    writer.write(row.get("id") + "," + row.get("teacher_name") + "," + row.get("college_name") + "," +
                            row.get("teacher_title") + "," + row.get("teacher_status") + "," + row.get("create_time") + "\n");
                }
            } else if ("salary".equals(type)) {
                writer.write("教师ID,教师名称,结算周期,学院,基础薪酬,职称津贴,考核奖金,扣款金额,合计金额,支付状态\n");
                String sql = "SELECT teacher_id, teacher_name, settlement_period, college_id, base_amount, title_allowance, assess_bonus, deduction_amount, total_amount, payment_status FROM salary_settlement WHERE create_time >= ? AND create_time <= ?";
                List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, startDate, endDate);
                for (Map<String, Object> row : rows) {
                    writer.write(row.get("teacher_id") + "," + row.get("teacher_name") + "," + row.get("settlement_period") + "," +
                            row.get("college_id") + "," + row.get("base_amount") + "," + row.get("title_allowance") + "," +
                            row.get("assess_bonus") + "," + row.get("deduction_amount") + "," + row.get("total_amount") + "," +
                            row.get("payment_status") + "\n");
                }
            } else if ("assessment".equals(type)) {
                writer.write("教师ID,考核周期,教学质量得分,出勤得分,贡献得分,总得分,等级\n");
                String sql = "SELECT teacher_id, period_id, teaching_quality_score, attendance_score, contribution_score, total_score, grade FROM assessment_record WHERE create_time >= ? AND create_time <= ?";
                List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, startDate, endDate);
                for (Map<String, Object> row : rows) {
                    writer.write(row.get("teacher_id") + "," + row.get("period_id") + "," + row.get("teaching_quality_score") + "," +
                            row.get("attendance_score") + "," + row.get("contribution_score") + "," + row.get("total_score") + "," +
                            row.get("grade") + "\n");
                }
            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("导出报表失败: " + e.getMessage());
        }
    }

    private String getStatusName(int status) {
        return switch (status) {
            case 0 -> "待入职";
            case 1 -> "在职";
            case 2 -> "离职";
            case 3 -> "已解聘";
            default -> "未知";
        };
    }
}
