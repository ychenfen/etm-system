package com.etm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.etm.dto.DashboardDto;
import com.etm.entity.*;
import com.etm.mapper.*;
import com.etm.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

        return dto;
    }
}
