package com.etm.modules.recruitment.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.etm.common.exception.BusinessException;
import com.etm.common.result.PageResult;
import com.etm.modules.recruitment.dto.ApprovalActionDTO;
import com.etm.modules.recruitment.dto.ApprovalCreateDTO;
import com.etm.modules.recruitment.dto.ApprovalQueryDTO;
import com.etm.modules.recruitment.entity.EmploymentApproval;
import com.etm.modules.recruitment.mapper.EmploymentApprovalMapper;
import com.etm.modules.recruitment.service.EmploymentApprovalService;
import com.etm.common.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 聘用审批Service实现
 */
@Service
@RequiredArgsConstructor
public class EmploymentApprovalServiceImpl extends ServiceImpl<EmploymentApprovalMapper, EmploymentApproval> implements EmploymentApprovalService {

    @Override
    public PageResult<EmploymentApproval> getApprovalPage(ApprovalQueryDTO query) {
        LambdaQueryWrapper<EmploymentApproval> wrapper = new LambdaQueryWrapper<>();
        if (query.getCollegeId() != null) {
            wrapper.eq(EmploymentApproval::getCollegeId, query.getCollegeId());
        }
        if (query.getFinalStatus() != null) {
            wrapper.eq(EmploymentApproval::getFinalStatus, query.getFinalStatus());
        }
        wrapper.orderByDesc(EmploymentApproval::getCreateTime);

        IPage<EmploymentApproval> page = this.page(new Page<>(query.getPageNum(), query.getPageSize()), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal(), query.getPageNum(), query.getPageSize());
    }

    @Override
    public EmploymentApproval createApproval(ApprovalCreateDTO dto) {
        EmploymentApproval approval = new EmploymentApproval();
        approval.setApplicationId(dto.getApplicationId());
        approval.setTeacherId(dto.getTeacherId());
        approval.setCollegeId(dto.getCollegeId());
        approval.setProposedSalary(dto.getProposedSalary());
        approval.setEmployStartDate(dto.getEmployStartDate());
        approval.setEmployEndDate(dto.getEmployEndDate());
        approval.setApplyReason(dto.getApplyReason());
        approval.setCollegeLeaderStatus(0); // 待
        approval.setHrSalaryStatus(0); // 待
        approval.setHrDirectorStatus(0); // 待
        approval.setFinalStatus(0); // 进行中
        approval.setCreateTime(LocalDateTime.now());
        this.save(approval);
        return approval;
    }

    @Override
    public void doApprove(Long id, ApprovalActionDTO dto) {
        EmploymentApproval approval = this.getById(id);
        if (approval == null) {
            throw new BusinessException("聘用审批不存在");
        }
        LocalDateTime now = LocalDateTime.now();
        Long userId = SecurityUtil.getCurrentUserId();

        // 根据node更新对应字段
        switch (dto.getNode()) {
            case "collegeLeader":
                approval.setCollegeLeaderStatus(dto.getStatus());
                approval.setCollegeLeaderComment(dto.getRemark());
                approval.setCollegeLeaderTime(now);
                break;
            case "hrSalary":
                approval.setHrSalaryStatus(dto.getStatus());
                approval.setHrSalaryComment(dto.getRemark());
                approval.setHrSalaryTime(now);
                break;
            case "hrDirector":
                approval.setHrDirectorStatus(dto.getStatus());
                approval.setHrDirectorComment(dto.getRemark());
                approval.setHrDirectorTime(now);
                break;
            default:
                throw new BusinessException("无效的审批节点");
        }

        // 检查是否所有审批都通过或任意一个驳回
        if (approval.getCollegeLeaderStatus() == 2 || approval.getHrSalaryStatus() == 2 || approval.getHrDirectorStatus() == 2) {
            approval.setFinalStatus(2); // 驳回
        } else if (approval.getCollegeLeaderStatus() == 1 && approval.getHrSalaryStatus() == 1 && approval.getHrDirectorStatus() == 1) {
            approval.setFinalStatus(1); // 完成
        }

        approval.setUpdateTime(now);
        this.updateById(approval);
    }
}
