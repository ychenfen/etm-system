package com.etm.modules.contract.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etm.common.exception.BusinessException;
import com.etm.common.result.PageResult;
import com.etm.modules.contract.dto.ContractCreateDTO;
import com.etm.modules.contract.dto.ContractQueryDTO;
import com.etm.modules.contract.dto.RenewalCreateDTO;
import com.etm.modules.contract.dto.RenewalQueryDTO;
import com.etm.modules.contract.dto.TerminationCreateDTO;
import com.etm.modules.contract.entity.EmploymentContract;
import com.etm.modules.contract.entity.RenewalApplication;
import com.etm.modules.contract.entity.TerminationRecord;
import com.etm.modules.contract.mapper.EmploymentContractMapper;
import com.etm.modules.contract.mapper.RenewalApplicationMapper;
import com.etm.modules.contract.mapper.TerminationRecordMapper;
import com.etm.modules.contract.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl extends ServiceImpl<EmploymentContractMapper, EmploymentContract> implements ContractService {

    private final EmploymentContractMapper contractMapper;
    private final RenewalApplicationMapper renewalMapper;
    private final TerminationRecordMapper terminationMapper;

    @Override
    public PageResult<EmploymentContract> getContractPage(ContractQueryDTO query) {
        Page<EmploymentContract> page = new Page<>(query.getPageNum(), query.getPageSize());
        Page<EmploymentContract> result = contractMapper.selectContractPage(
                page,
                query.getTeacherId(),
                query.getCollegeId(),
                query.getSignStatus()
        );
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getSize(), result.getCurrent());
    }

    @Override
    public EmploymentContract getContractDetail(Long id) {
        EmploymentContract contract = contractMapper.selectById(id);
        if (contract == null || contract.getDeleted() == 1) {
            throw new BusinessException("合同不存在");
        }
        return contract;
    }

    @Override
    @Transactional
    public EmploymentContract createContract(ContractCreateDTO dto) {
        String contractNo = generateContractNo();

        EmploymentContract contract = EmploymentContract.builder()
                .teacherId(dto.getTeacherId())
                .teacherName(dto.getTeacherName())
                .collegeId(dto.getCollegeId())
                .collegeName(dto.getCollegeName())
                .employStartDate(dto.getEmployStartDate())
                .employEndDate(dto.getEmployEndDate())
                .salary(dto.getSalary())
                .contractNo(contractNo)
                .signStatus(0)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .deleted(0)
                .build();

        contractMapper.insert(contract);
        return contract;
    }

    @Override
    @Transactional
    public void signContract(Long id) {
        EmploymentContract contract = getContractDetail(id);
        contract.setSignStatus(1);
        contract.setSignTime(LocalDateTime.now());
        contract.setUpdateTime(LocalDateTime.now());
        contractMapper.updateById(contract);
    }

    @Override
    public PageResult<RenewalApplication> getRenewalPage(RenewalQueryDTO query) {
        Page<RenewalApplication> page = new Page<>(query.getPageNum(), query.getPageSize());
        Page<RenewalApplication> result = renewalMapper.selectRenewalPage(
                page,
                query.getTeacherId(),
                query.getCollegeId(),
                query.getApprovalStatus()
        );
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getSize(), result.getCurrent());
    }

    @Override
    @Transactional
    public RenewalApplication createRenewal(RenewalCreateDTO dto) {
        EmploymentContract contract = getContractDetail(dto.getContractId());

        RenewalApplication renewal = RenewalApplication.builder()
                .teacherId(dto.getTeacherId())
                .contractId(dto.getContractId())
                .collegeId(dto.getCollegeId())
                .renewalStartDate(dto.getRenewalStartDate())
                .renewalEndDate(dto.getRenewalEndDate())
                .renewalReason(dto.getRenewalReason())
                .coursePlan(dto.getCoursePlan())
                .proposedSalary(dto.getProposedSalary())
                .approvalStatus(0)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        renewalMapper.insert(renewal);
        return renewal;
    }

    @Override
    @Transactional
    public void approveRenewal(Long id, Integer status, String comment) {
        RenewalApplication renewal = renewalMapper.selectById(id);
        if (renewal == null) {
            throw new BusinessException("续聘申请不存在");
        }

        renewal.setApprovalStatus(status);
        renewal.setApprovalComment(comment);
        renewal.setApprovalTime(LocalDateTime.now());
        renewal.setUpdateTime(LocalDateTime.now());

        if (status == 1) {
            EmploymentContract oldContract = contractMapper.selectById(renewal.getContractId());

            EmploymentContract newContract = EmploymentContract.builder()
                    .teacherId(renewal.getTeacherId())
                    .teacherName(oldContract.getTeacherName())
                    .collegeId(renewal.getCollegeId())
                    .collegeName(oldContract.getCollegeName())
                    .employStartDate(renewal.getRenewalStartDate())
                    .employEndDate(renewal.getRenewalEndDate())
                    .salary(renewal.getProposedSalary())
                    .contractNo(generateContractNo())
                    .signStatus(0)
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .deleted(0)
                    .build();

            contractMapper.insert(newContract);
            renewal.setNewContractId(newContract.getId());
        }

        renewalMapper.updateById(renewal);
    }

    @Override
    public PageResult<TerminationRecord> getTerminationPage(Long collegeId, Integer pageNum, Integer pageSize) {
        Page<TerminationRecord> page = new Page<>(pageNum, pageSize);
        Page<TerminationRecord> result = terminationMapper.selectTerminationPage(page, collegeId);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getSize(), result.getCurrent());
    }

    @Override
    @Transactional
    public TerminationRecord createTermination(TerminationCreateDTO dto) {
        TerminationRecord termination = TerminationRecord.builder()
                .teacherId(dto.getTeacherId())
                .teacherName(dto.getTeacherName())
                .contractId(dto.getContractId())
                .terminationType(dto.getTerminationType())
                .terminationReason(dto.getTerminationReason())
                .terminationDate(dto.getTerminationDate())
                .handoverStatus(0)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        terminationMapper.insert(termination);
        return termination;
    }

    private String generateContractNo() {
        YearMonth now = YearMonth.now();
        String prefix = "CON" + now.format(DateTimeFormatter.ofPattern("yyyyMM"));
        long count = contractMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<EmploymentContract>()
                        .like("contract_no", prefix)
        );
        return prefix + String.format("%04d", count + 1);
    }
}
