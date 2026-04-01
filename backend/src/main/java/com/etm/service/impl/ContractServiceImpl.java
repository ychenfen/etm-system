package com.etm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etm.entity.Approval;
import com.etm.entity.Contract;
import com.etm.entity.Department;
import com.etm.entity.Teacher;
import com.etm.mapper.ApprovalMapper;
import com.etm.mapper.ContractMapper;
import com.etm.mapper.DepartmentMapper;
import com.etm.mapper.TeacherMapper;
import com.etm.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements ContractService {

    @Autowired
    private ApprovalMapper approvalMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    @Transactional
    public Contract generateFromApproval(Long approvalId) {
        Approval approval = approvalMapper.selectById(approvalId);
        if (approval == null) throw new RuntimeException("审批记录不存在");

        // 防止重复生成
        Long existing = this.count(new LambdaQueryWrapper<Contract>()
                .eq(Contract::getApprovalId, approvalId));
        if (existing > 0) return this.getOne(new LambdaQueryWrapper<Contract>()
                .eq(Contract::getApprovalId, approvalId));

        Contract contract = new Contract();
        contract.setContractNo("CTR" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        contract.setApprovalId(approvalId);
        contract.setTeacherId(approval.getTeacherId());
        contract.setDepartmentId(approval.getDepartmentId());
        contract.setStartDate(approval.getStartDate());
        contract.setEndDate(approval.getEndDate());
        contract.setSalaryStandard(approval.getProposedSalary());
        contract.setContractStatus("UNSIGNED");
        this.save(contract);
        return contract;
    }

    @Override
    @Transactional
    public void teacherSign(Long id) {
        Contract contract = this.getById(id);
        if (contract == null) throw new RuntimeException("合同不存在");
        if (!"UNSIGNED".equals(contract.getContractStatus())) throw new RuntimeException("合同当前状态不允许签署");
        contract.setTeacherSignTime(LocalDateTime.now());
        // 教师签后等学校盖章，状态暂保持 UNSIGNED
        this.updateById(contract);
    }

    @Override
    @Transactional
    public void schoolSign(Long id) {
        Contract contract = this.getById(id);
        if (contract == null) throw new RuntimeException("合同不存在");
        if (contract.getTeacherSignTime() == null) throw new RuntimeException("教师尚未签署，无法盖章");
        contract.setSchoolSignTime(LocalDateTime.now());
        contract.setContractStatus("SIGNED");
        this.updateById(contract);
    }

    @Override
    public Page<Contract> pageList(int current, int size, Long teacherId, String status) {
        LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<>();
        if (teacherId != null) wrapper.eq(Contract::getTeacherId, teacherId);
        if (StringUtils.hasText(status)) wrapper.eq(Contract::getContractStatus, status);
        wrapper.orderByDesc(Contract::getCreateTime);
        Page<Contract> page = this.page(new Page<>(current, size), wrapper);
        page.getRecords().forEach(c -> {
            Teacher t = teacherMapper.selectById(c.getTeacherId());
            if (t != null) c.setTeacherName(t.getName());
            Department d = departmentMapper.selectById(c.getDepartmentId());
            if (d != null) c.setDepartmentName(d.getName());
        });
        return page;
    }
}
