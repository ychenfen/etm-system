package com.etm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etm.entity.Approval;
import com.etm.entity.Department;
import com.etm.entity.Teacher;
import com.etm.mapper.ApprovalMapper;
import com.etm.mapper.DepartmentMapper;
import com.etm.mapper.TeacherMapper;
import com.etm.service.ApprovalService;
import com.etm.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ApprovalServiceImpl extends ServiceImpl<ApprovalMapper, Approval> implements ApprovalService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    @Lazy
    private ContractService contractService;

    @Override
    @Transactional
    public Approval submit(Approval approval) {
        // 生成审批编号
        String no = "APR" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        approval.setApprovalNo(no);
        approval.setCurrentNode("college_leader");
        approval.setApprovalStatus("PENDING");
        this.save(approval);
        return approval;
    }

    @Override
    @Transactional
    public void approve(Long id, String node, boolean pass, String remark, String operatorName) {
        Approval approval = this.getById(id);
        if (approval == null) throw new RuntimeException("审批记录不存在");
        if (!"PENDING".equals(approval.getApprovalStatus())) throw new RuntimeException("审批已结束，无法操作");
        if (!node.equals(approval.getCurrentNode())) throw new RuntimeException("当前不在 " + node + " 节点");

        String status = pass ? "APPROVED" : "REJECTED";
        LocalDateTime now = LocalDateTime.now();

        switch (node) {
            case "college_leader":
                approval.setCollegeStatus(status);
                approval.setCollegeRemark(remark);
                approval.setCollegeBy(operatorName);
                approval.setCollegeTime(now);
                if (pass) {
                    approval.setCurrentNode("hr_salary");
                } else {
                    approval.setApprovalStatus("REJECTED");
                }
                break;
            case "hr_salary":
                approval.setHrSalaryStatus(status);
                approval.setHrSalaryRemark(remark);
                approval.setHrSalaryBy(operatorName);
                approval.setHrSalaryTime(now);
                if (pass) {
                    approval.setCurrentNode("hr_director");
                } else {
                    approval.setApprovalStatus("REJECTED");
                }
                break;
            case "hr_director":
                approval.setHrDirectorStatus(status);
                approval.setHrDirectorRemark(remark);
                approval.setHrDirectorBy(operatorName);
                approval.setHrDirectorTime(now);
                if (pass) {
                    approval.setCurrentNode("finished");
                    approval.setApprovalStatus("APPROVED");
                    // 自动生成合同
                    contractService.generateFromApproval(id);
                } else {
                    approval.setApprovalStatus("REJECTED");
                }
                break;
            default:
                throw new RuntimeException("未知节点: " + node);
        }
        this.updateById(approval);
    }

    @Override
    @Transactional
    public void revoke(Long id) {
        Approval approval = this.getById(id);
        if (approval == null) throw new RuntimeException("审批记录不存在");
        if (!"PENDING".equals(approval.getApprovalStatus())) throw new RuntimeException("只有审批中的申请可以撤回");
        if (!"college_leader".equals(approval.getCurrentNode())) throw new RuntimeException("已进入下一审批节点，无法撤回");
        approval.setApprovalStatus("REVOKED");
        this.updateById(approval);
    }

    @Override
    public Page<Approval> pageList(int current, int size, Long teacherId, String status) {
        LambdaQueryWrapper<Approval> wrapper = new LambdaQueryWrapper<>();
        if (teacherId != null) wrapper.eq(Approval::getTeacherId, teacherId);
        if (StringUtils.hasText(status)) wrapper.eq(Approval::getApprovalStatus, status);
        wrapper.orderByDesc(Approval::getCreateTime);
        Page<Approval> page = this.page(new Page<>(current, size), wrapper);
        page.getRecords().forEach(a -> {
            Teacher t = teacherMapper.selectById(a.getTeacherId());
            if (t != null) a.setTeacherName(t.getName());
            Department d = departmentMapper.selectById(a.getDepartmentId());
            if (d != null) a.setDepartmentName(d.getName());
        });
        return page;
    }
}
