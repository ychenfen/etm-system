package com.etm.modules.salary.service;

import com.etm.common.result.PageResult;
import com.etm.modules.salary.entity.SalaryRule;
import com.etm.modules.salary.entity.SalarySettlement;
import com.etm.modules.salary.dto.SalaryRuleDTO;
import com.etm.modules.salary.dto.SalarySettlementQueryDTO;
import com.etm.modules.salary.dto.GenerateSettlementDTO;
import com.etm.modules.salary.dto.VerifyDTO;

import java.util.List;

/**
 * 薪酬结算Service接口
 */
public interface SalaryService {

    /**
     * 获取薪酬规则列表
     */
    List<SalaryRule> getRuleList();

    /**
     * 创建薪酬规则
     */
    SalaryRule createRule(SalaryRuleDTO dto);

    /**
     * 更新薪酬规则
     */
    SalaryRule updateRule(Long id, SalaryRuleDTO dto);

    /**
     * 删除薪酬规则
     */
    void deleteRule(Long id);

    /**
     * 分页查询薪酬结算
     */
    PageResult<SalarySettlement> getSettlementPage(SalarySettlementQueryDTO query);

    /**
     * 获取薪酬结算详情
     */
    SalarySettlement getSettlementDetail(Long id);

    /**
     * 生成薪酬结算
     * 自动核算：查course_schedule获取实际课时，基于salary_rule计算
     * 职称系数从teacher_info.titleLevel(1=1.0, 2=1.1, 3=1.15, 4=1.2)
     * 考核系数从assessment_record(优秀=1.1, 合格=1.0, 不合格=0.8)
     * 生成settlementNo(SAL+年月+序号)
     */
    void generateSettlements(GenerateSettlementDTO dto);

    /**
     * 核批薪酬结算
     */
    void verifySettlement(Long id, VerifyDTO dto);

    /**
     * 批量发放
     */
    void batchPay(List<Long> ids);
}
