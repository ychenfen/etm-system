package com.etm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.etm.entity.*;
import com.etm.mapper.CourseMapper;
import com.etm.mapper.DepartmentMapper;
import com.etm.mapper.TeacherMapper;
import com.etm.service.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/export")
public class ExportController {

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private SalaryService salaryService;
    @Autowired
    private EvaluationService evaluationService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private DepartmentMapper departmentMapper;

    private Map<Long, String> getTeacherNameMap() {
        Map<Long, String> map = new HashMap<>();
        List<Teacher> teachers = teacherMapper.selectList(null);
        for (Teacher t : teachers) {
            map.put(t.getId(), t.getName());
        }
        return map;
    }

    private Map<Long, String> getCourseNameMap() {
        Map<Long, String> map = new HashMap<>();
        List<Course> courses = courseMapper.selectList(null);
        for (Course c : courses) {
            map.put(c.getId(), c.getName());
        }
        return map;
    }

    private Map<Long, String> getDeptNameMap() {
        Map<Long, String> map = new HashMap<>();
        List<Department> depts = departmentMapper.selectList(null);
        for (Department d : depts) {
            map.put(d.getId(), d.getName());
        }
        return map;
    }

    @GetMapping("/teacher")
    public void exportTeacher(HttpServletResponse response) throws IOException {
        List<Teacher> list = teacherService.list(new LambdaQueryWrapper<Teacher>().eq(Teacher::getDeleted, 0));
        Map<Long, String> deptMap = getDeptNameMap();
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("教师信息");
        String[] headers = {"姓名", "性别", "电话", "邮箱", "学历", "学位", "职称", "工作单位", "专业特长", "所属院系", "聘用状态", "聘期起", "聘期止"};
        createHeaderRow(sheet, headers);
        int rowNum = 1;
        for (Teacher t : list) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(str(t.getName()));
            row.createCell(1).setCellValue(str(t.getGender()));
            row.createCell(2).setCellValue(str(t.getPhone()));
            row.createCell(3).setCellValue(str(t.getEmail()));
            row.createCell(4).setCellValue(str(t.getEducation()));
            row.createCell(5).setCellValue(str(t.getDegree()));
            row.createCell(6).setCellValue(str(t.getTitle()));
            row.createCell(7).setCellValue(str(t.getWorkUnit()));
            row.createCell(8).setCellValue(str(t.getSpeciality()));
            row.createCell(9).setCellValue(str(deptMap.get(t.getDepartmentId())));
            row.createCell(10).setCellValue(statusText(t.getHireStatus()));
            row.createCell(11).setCellValue(t.getHireStartDate() != null ? t.getHireStartDate().toString() : "");
            row.createCell(12).setCellValue(t.getHireEndDate() != null ? t.getHireEndDate().toString() : "");
        }
        autoSizeColumns(sheet, headers.length);
        writeResponse(response, wb, "教师信息");
    }

    @GetMapping("/course")
    public void exportCourse(HttpServletResponse response) throws IOException {
        List<Course> list = courseService.list(new LambdaQueryWrapper<Course>().eq(Course::getDeleted, 0));
        Map<Long, String> teacherMap = getTeacherNameMap();
        Map<Long, String> deptMap = getDeptNameMap();
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("课程信息");
        String[] headers = {"课程名称", "编码", "授课教师", "所属院系", "学期", "学分", "学时", "授课班级", "学生人数", "教室", "上课时间", "状态"};
        createHeaderRow(sheet, headers);
        int rowNum = 1;
        for (Course c : list) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(str(c.getName()));
            row.createCell(1).setCellValue(str(c.getCode()));
            row.createCell(2).setCellValue(str(teacherMap.get(c.getTeacherId())));
            row.createCell(3).setCellValue(str(deptMap.get(c.getDepartmentId())));
            row.createCell(4).setCellValue(str(c.getSemester()));
            row.createCell(5).setCellValue(c.getCredit() != null ? c.getCredit().doubleValue() : 0);
            row.createCell(6).setCellValue(c.getHours() != null ? c.getHours() : 0);
            row.createCell(7).setCellValue(str(c.getClassName()));
            row.createCell(8).setCellValue(c.getStudentCount() != null ? c.getStudentCount() : 0);
            row.createCell(9).setCellValue(str(c.getClassroom()));
            row.createCell(10).setCellValue(str(c.getSchedule()));
            row.createCell(11).setCellValue(courseStatusText(c.getStatus()));
        }
        autoSizeColumns(sheet, headers.length);
        writeResponse(response, wb, "课程信息");
    }

    @GetMapping("/attendance")
    public void exportAttendance(HttpServletResponse response) throws IOException {
        List<Attendance> list = attendanceService.list(new LambdaQueryWrapper<Attendance>().eq(Attendance::getDeleted, 0));
        Map<Long, String> teacherMap = getTeacherNameMap();
        Map<Long, String> courseMap = getCourseNameMap();
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("考勤记录");
        String[] headers = {"教师", "课程", "考勤日期", "签到时间", "签退时间", "实际课时", "状态", "备注"};
        createHeaderRow(sheet, headers);
        int rowNum = 1;
        for (Attendance a : list) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(str(teacherMap.get(a.getTeacherId())));
            row.createCell(1).setCellValue(str(courseMap.get(a.getCourseId())));
            row.createCell(2).setCellValue(a.getAttendanceDate() != null ? a.getAttendanceDate().toString() : "");
            row.createCell(3).setCellValue(a.getCheckInTime() != null ? a.getCheckInTime().toString() : "");
            row.createCell(4).setCellValue(a.getCheckOutTime() != null ? a.getCheckOutTime().toString() : "");
            row.createCell(5).setCellValue(a.getActualHours() != null ? a.getActualHours().doubleValue() : 0);
            row.createCell(6).setCellValue(attendStatusText(a.getStatus()));
            row.createCell(7).setCellValue(str(a.getRemark()));
        }
        autoSizeColumns(sheet, headers.length);
        writeResponse(response, wb, "考勤记录");
    }

    @GetMapping("/salary")
    public void exportSalary(HttpServletResponse response) throws IOException {
        List<Salary> list = salaryService.list(new LambdaQueryWrapper<Salary>().eq(Salary::getDeleted, 0));
        Map<Long, String> teacherMap = getTeacherNameMap();
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("薪酬记录");
        String[] headers = {"教师", "月份", "基本工资", "总课时", "课时费", "奖金", "扣款", "实发金额", "状态", "发放日期"};
        createHeaderRow(sheet, headers);
        int rowNum = 1;
        for (Salary s : list) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(str(teacherMap.get(s.getTeacherId())));
            row.createCell(1).setCellValue(str(s.getMonth()));
            row.createCell(2).setCellValue(s.getBaseSalary() != null ? s.getBaseSalary().doubleValue() : 0);
            row.createCell(3).setCellValue(s.getTotalHours() != null ? s.getTotalHours().doubleValue() : 0);
            row.createCell(4).setCellValue(s.getHourRate() != null ? s.getHourRate().doubleValue() : 0);
            row.createCell(5).setCellValue(s.getBonus() != null ? s.getBonus().doubleValue() : 0);
            row.createCell(6).setCellValue(s.getDeduction() != null ? s.getDeduction().doubleValue() : 0);
            row.createCell(7).setCellValue(s.getTotalSalary() != null ? s.getTotalSalary().doubleValue() : 0);
            row.createCell(8).setCellValue(salaryStatusText(s.getStatus()));
            row.createCell(9).setCellValue(s.getPayDate() != null ? s.getPayDate().toString() : "");
        }
        autoSizeColumns(sheet, headers.length);
        writeResponse(response, wb, "薪酬记录");
    }

    @GetMapping("/evaluation")
    public void exportEvaluation(HttpServletResponse response) throws IOException {
        List<Evaluation> list = evaluationService.list(new LambdaQueryWrapper<Evaluation>().eq(Evaluation::getDeleted, 0));
        Map<Long, String> teacherMap = getTeacherNameMap();
        Map<Long, String> courseMap = getCourseNameMap();
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("评价记录");
        String[] headers = {"教师", "课程", "学期", "教学评分", "考勤评分", "学生评分", "总评分", "评价人", "评价日期", "评语"};
        createHeaderRow(sheet, headers);
        int rowNum = 1;
        for (Evaluation e : list) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(str(teacherMap.get(e.getTeacherId())));
            row.createCell(1).setCellValue(str(courseMap.get(e.getCourseId())));
            row.createCell(2).setCellValue(str(e.getSemester()));
            row.createCell(3).setCellValue(e.getTeachingScore() != null ? e.getTeachingScore().doubleValue() : 0);
            row.createCell(4).setCellValue(e.getAttendanceScore() != null ? e.getAttendanceScore().doubleValue() : 0);
            row.createCell(5).setCellValue(e.getStudentScore() != null ? e.getStudentScore().doubleValue() : 0);
            row.createCell(6).setCellValue(e.getTotalScore() != null ? e.getTotalScore().doubleValue() : 0);
            row.createCell(7).setCellValue(str(e.getEvaluator()));
            row.createCell(8).setCellValue(e.getEvaluationDate() != null ? e.getEvaluationDate().toString() : "");
            row.createCell(9).setCellValue(str(e.getComment()));
        }
        autoSizeColumns(sheet, headers.length);
        writeResponse(response, wb, "评价记录");
    }

    private void createHeaderRow(Sheet sheet, String[] headers) {
        Row headerRow = sheet.createRow(0);
        CellStyle style = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
    }

    private void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void writeResponse(HttpServletResponse response, Workbook wb, String filename) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename + ".xlsx", "UTF-8"));
        wb.write(response.getOutputStream());
        wb.close();
    }

    private String str(String val) {
        return val != null ? val : "";
    }

    private String statusText(String status) {
        if (status == null) return "";
        switch (status) {
            case "PENDING": return "待审核";
            case "APPROVED": return "已聘用";
            case "REJECTED": return "已拒绝";
            case "EXPIRED": return "已到期";
            default: return status;
        }
    }

    private String attendStatusText(String status) {
        if (status == null) return "";
        switch (status) {
            case "NORMAL": return "正常";
            case "LATE": return "迟到";
            case "EARLY_LEAVE": return "早退";
            case "ABSENT": return "缺勤";
            case "LEAVE": return "请假";
            default: return status;
        }
    }

    private String courseStatusText(String status) {
        if (status == null) return "";
        switch (status) {
            case "ACTIVE": return "进行中";
            case "COMPLETED": return "已完成";
            case "CANCELLED": return "已取消";
            default: return status;
        }
    }

    private String salaryStatusText(String status) {
        if (status == null) return "";
        switch (status) {
            case "PENDING": return "待审核";
            case "APPROVED": return "已审核";
            case "PAID": return "已发放";
            default: return status;
        }
    }
}
