package com.etm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.etm.dto.DashboardDto;
import com.etm.entity.*;
import com.etm.mapper.*;
import com.etm.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private SalaryMapper salaryMapper;
    @Autowired
    private AttendanceMapper attendanceMapper;
    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public DashboardDto getStatistics() {
        DashboardDto dto = new DashboardDto();

        dto.setTeacherCount(teacherMapper.selectCount(null));
        dto.setCourseCount(courseMapper.selectCount(null));
        dto.setDepartmentCount(departmentMapper.selectCount(null));

        LambdaQueryWrapper<Teacher> pendingWrapper = new LambdaQueryWrapper<>();
        pendingWrapper.eq(Teacher::getHireStatus, "PENDING");
        dto.setPendingHireCount(teacherMapper.selectCount(pendingWrapper));

        LambdaQueryWrapper<Salary> salaryWrapper = new LambdaQueryWrapper<>();
        salaryWrapper.eq(Salary::getStatus, "PENDING");
        dto.setPendingSalaryCount(salaryMapper.selectCount(salaryWrapper));

        // 各院系教师数量统计
        List<Map<String, Object>> deptStats = new ArrayList<>();
        List<Department> departments = departmentMapper.selectList(null);
        for (Department dept : departments) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", dept.getName());
            LambdaQueryWrapper<Teacher> tw = new LambdaQueryWrapper<>();
            tw.eq(Teacher::getDepartmentId, dept.getId());
            map.put("value", teacherMapper.selectCount(tw));
            deptStats.add(map);
        }
        dto.setDepartmentTeacherStats(deptStats);

        // 聘用状态统计
        List<Map<String, Object>> hireStats = new ArrayList<>();
        String[] statuses = {"PENDING", "APPROVED", "REJECTED", "EXPIRED"};
        String[] statusNames = {"待审核", "已聘用", "已拒绝", "已到期"};
        for (int i = 0; i < statuses.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", statusNames[i]);
            LambdaQueryWrapper<Teacher> tw = new LambdaQueryWrapper<>();
            tw.eq(Teacher::getHireStatus, statuses[i]);
            map.put("value", teacherMapper.selectCount(tw));
            hireStats.add(map);
        }
        dto.setHireStatusStats(hireStats);

        // 考勤统计
        dto.setAttendanceCount(attendanceMapper.selectCount(null));
        LambdaQueryWrapper<Attendance> normalWrapper = new LambdaQueryWrapper<>();
        normalWrapper.eq(Attendance::getStatus, "NORMAL");
        dto.setNormalAttendanceCount(attendanceMapper.selectCount(normalWrapper));

        // 月度薪酬统计
        List<Salary> allSalary = salaryMapper.selectList(null);
        Map<String, Double> salaryByMonth = allSalary.stream()
                .filter(s -> s.getMonth() != null && s.getTotalSalary() != null)
                .collect(Collectors.groupingBy(Salary::getMonth,
                        Collectors.summingDouble(s -> s.getTotalSalary().doubleValue())));
        List<Map<String, Object>> monthlySalary = new ArrayList<>();
        salaryByMonth.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", e.getKey());
                    map.put("value", e.getValue());
                    monthlySalary.add(map);
                });
        dto.setMonthlySalaryStats(monthlySalary);

        // 最新通知（前5条）
        LambdaQueryWrapper<Notice> noticeWrapper = new LambdaQueryWrapper<>();
        noticeWrapper.eq(Notice::getStatus, 1).orderByDesc(Notice::getCreateTime).last("LIMIT 5");
        List<Notice> notices = noticeMapper.selectList(noticeWrapper);
        List<Map<String, Object>> recentNotices = new ArrayList<>();
        for (Notice n : notices) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", n.getId());
            map.put("title", n.getTitle());
            map.put("type", n.getType());
            map.put("createTime", n.getCreateTime());
            recentNotices.add(map);
        }
        dto.setRecentNotices(recentNotices);

        // 合同即将到期教师（30天内）
        LocalDate now = LocalDate.now();
        LocalDate threshold = now.plusDays(30);
        LambdaQueryWrapper<Teacher> expiringWrapper = new LambdaQueryWrapper<>();
        expiringWrapper.eq(Teacher::getHireStatus, "APPROVED")
                .le(Teacher::getHireEndDate, threshold)
                .ge(Teacher::getHireEndDate, now);
        List<Teacher> expiringTeachers = teacherMapper.selectList(expiringWrapper);
        List<Map<String, Object>> expiringList = new ArrayList<>();
        for (Teacher t : expiringTeachers) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", t.getId());
            map.put("name", t.getName());
            map.put("hireEndDate", t.getHireEndDate());
            if (t.getDepartmentId() != null) {
                Department dept = departmentMapper.selectById(t.getDepartmentId());
                map.put("departmentName", dept != null ? dept.getName() : "");
            }
            expiringList.add(map);
        }
        dto.setExpiringTeachers(expiringList);

        return dto;
    }
}
