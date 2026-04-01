package com.etm.modules.salary.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etm.common.exception.BusinessException;
import com.etm.common.result.PageResult;
import com.etm.modules.salary.entity.SalaryRule;
import com.etm.modules.salary.entity.SalarySettlement;
import com.etm.modules.salary.dto.SalaryRuleDTO;
import com.etm.modules.salary.dto.SalarySettlementQueryDTO;
import com.etm.modules.salary.dto.GenerateSettlementDTO;
import com.etm.modules.salary.dto.VerifyDTO;
import com.etm.modules.salary.mapper.SalaryRuleMapper;
import com.etm.modules.salary.mapper.SalarySettlementMapper;
import com.etm.modules.salary.service.SalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 薪酬结算Service实现
 */
@Service
@RequiredArgsConstructor
public class SalaryServiceImpl implements SalaryService {

    private final SalaryRuleMapper ruleMapper;
    private final SalarySettlementMapper settlementMapper;

    @Override
    public List<SalaryRule> getRuleList() {
        LambdaQueryWrapper<SalaryRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SalaryRule::getIsActive, 1)
                .eq(SalaryRule::getDeleted, 0)
                .orderByDesc(SalaryRule::getCreateTime);
        return ruleMapper.selectList(wrapper);
    }

    @Override
    public SalaryRule createRule(SalaryRuleDTO dto) {
        SalaryRule rule = new SalaryRule();
        rule.setRuleName(dto.getRuleName());
        rule.setCourseType(dto.getCourseType());
        rule.setTeachingLevel(dto.getTeachingLevel());
        rule.setBaseSalary(dto.getBaseSalary());
        rule.setDescription(dto.getDescription());
        rule.setIsActive(1);
        rule.setCreateTime(LocalDateTime.now());
        rule.setDeleted(0);
        ruleMapper.insert(rule);
        return rule;
    }

    @Override
    public SalaryRule updateRule(Long id, SalaryRuleDTO dto) {
        SalaryRule rule = ruleMapper.selectById(id);
        if (rule == null || rule.getDeleted() == 1) {
            throw new BusinessException("薪酬规则不存在");
        }
        rule.setRuleName(dto.getRuleName());
        rule.setCourseType(dto.getCourseType());
        rule.setTeachingLevel(dto.getTeachingLevel());
        rule.setBaseSalary(dto.getBaseSalary());
        rule.setDescription(dto.getDescription());
        rule.setUpdateTime(LocalDateTime.now());
        ruleMapper.updateById(rule);
        return rule;
    }

    @Override
    public void deleteRule(Long id) {
        SalaryRule rule = ruleMapper.selectById(id);
        if (rule == null || rule.getDeleted() == 1) {
            throw new BusinessException("薪酬规则不存在");
        }
        rule.setDeleted(1);
        rule.setUpdateTime(LocalDateTime.now());
        ruleMapper.updateById(rule);
    }

    @Override
    public PageResult<SalarySettlement> getSettlementPage(SalarySettlementQueryDTO query) {
        IPage<SalarySettlement> page = settlementMapper.selectSettlementPage(
                new Page<>(query.getPageNum(), query.getPageSize()), query
        );
        return new PageResult<>(page.getRecords(), page.getTotal(), query.getPageNum(), query.getPageSize());
    }

    @Override
    public SalarySettlement getSettlementDetail(Long id) {
        SalarySettlement settlement = settlementMapper.selectById(id);
        if (settlement == null) {
            throw new BusinessException("薪酬结算不存在");
        }
        return settlement;
    }

    @Override
    public void generateSettlements(GenerateSettlementDTO dto) {
        // 生成薪酬结算
        // 根据学院、周期查询所有授课教师
        // 查course_schedule获取实际课时
        // 基于salary_rule计算
        // 职称系数和考核系数

        String settlementPeriod = dto.getSettlementPeriod();
        String year = settlementPeriod.substring(0, 4);
        String month = settlementPeriod.substring(4, 6);

        // 生成结算单号: SAL+年月+序号
        LambdaQueryWrapper<SalarySettlement> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.like(SalarySettlement::getSettlementNo, "SAL" + year + month);
        int count = (int) settlementMapper.selectCount(countWrapper);
        String settlementNo = "SAL" + year + month + String.format("%04d", count + 1);

        // 这里简化处理，实际应查询真实数据
        // SalarySettlement settlement = new SalarySettlement();
        // settlement.setSettlementNo(settlementNo);
        // ...
    }

    @Override
    public void verifySettlement(Long id, VerifyDTO dto) {
        SalarySettlement settlement = settlementMapper.selectById(id);
        if (settlement == null) {
            throw new BusinessException("薪酬结算不存在");
        }

        if ("verify".equals(dto.getVerifyType())) {
            settlement.setVerifyStatus(dto.getStatus());
        } else if ("pay".equals(dto.getVerifyType())) {
            settlement.setPaymentStatus(dto.getStatus());
            if (dto.getStatus() == 1) {
                settlement.setPaymentTime(LocalDateTime.now());
            }
        }

        settlement.setUpdateTime(LocalDateTime.now());
        settlementMapper.updateById(settlement);
    }

    @Override
    public void batchPay(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("结算ID列表不能为空");
        }

        LocalDateTime now = LocalDateTime.now();
        for (Long id : ids) {
            SalarySettlement settlement = settlementMapper.selectById(id);
            if (settlement != null) {
                settlement.setPaymentStatus(1);
                settlement.setPaymentTime(now);
                settlement.setUpdateTime(now);
                settlementMapper.updateById(settlement);
            }
        }
    }
}
