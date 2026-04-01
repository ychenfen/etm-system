package com.etm.modules.assessment.service;

import com.etm.common.result.PageResult;
import com.etm.modules.assessment.entity.AssessmentConfig;
import com.etm.modules.assessment.entity.AssessmentPeriod;
import com.etm.modules.assessment.entity.AssessmentRecord;

import java.time.LocalDate;
import java.util.List;

/**
 * 考核评估Service接口
 */
public interface AssessmentService {

    /**
     * 获取考核配置列表
     */
    List<AssessmentConfig> getConfigList();

    /**
     * 更新考核配置
     */
    void updateConfig(Long id, Integer weight, Integer isActive);

    /**
     * 分页查询考核记录
     */
    PageResult<AssessmentRecord> getRecordPage(Long teacherId, Long periodId, Integer pageNum, Integer pageSize);

    /**
     * 获取考核记录详情，包含details
     */
    AssessmentRecord getRecordDetail(Long id);

    /**
     * 生成考核记录，从supervision和evaluation表汇总数据计算
     */
    void generateRecords(Long periodId, List<Long> teacherIds);

    /**
     * 获取考核周期列表
     */
    List<AssessmentPeriod> getPeriodList();

    /**
     * 创建考核周期
     */
    AssessmentPeriod createPeriod(String periodName, Integer periodType, LocalDate startDate, LocalDate endDate);
}
