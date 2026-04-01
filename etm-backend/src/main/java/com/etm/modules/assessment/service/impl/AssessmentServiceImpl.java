package com.etm.modules.assessment.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.etm.common.exception.BusinessException;
import com.etm.common.result.PageResult;
import com.etm.modules.assessment.entity.*;
import com.etm.modules.assessment.mapper.*;
import com.etm.modules.assessment.service.AssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 考核评估Service实现
 */
@Service
@RequiredArgsConstructor
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentConfigMapper configMapper;
    private final AssessmentPeriodMapper periodMapper;
    private final AssessmentRecordMapper recordMapper;
    private final AssessmentDetailMapper detailMapper;

    @Override
    public List<AssessmentConfig> getConfigList() {
        LambdaQueryWrapper<AssessmentConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AssessmentConfig::getIsActive, 1);
        return configMapper.selectList(wrapper);
    }

    @Override
    public void updateConfig(Long id, Integer weight, Integer isActive) {
        AssessmentConfig config = configMapper.selectById(id);
        if (config == null) {
            throw new BusinessException("考核配置不存在");
        }
        config.setWeight(weight);
        config.setIsActive(isActive);
        config.setUpdateTime(LocalDateTime.now());
        configMapper.updateById(config);
    }

    @Override
    public PageResult<AssessmentRecord> getRecordPage(Long teacherId, Long periodId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<AssessmentRecord> wrapper = new LambdaQueryWrapper<>();
        if (teacherId != null) {
            wrapper.eq(AssessmentRecord::getTeacherId, teacherId);
        }
        if (periodId != null) {
            wrapper.eq(AssessmentRecord::getPeriodId, periodId);
        }
        wrapper.orderByDesc(AssessmentRecord::getCreateTime);

        IPage<AssessmentRecord> page = recordMapper.selectPage(
                new Page<>(pageNum, pageSize), wrapper
        );
        return new PageResult<>(page.getRecords(), page.getTotal(), pageNum, pageSize);
    }

    @Override
    public AssessmentRecord getRecordDetail(Long id) {
        AssessmentRecord record = recordMapper.selectById(id);
        if (record == null) {
            throw new BusinessException("考核记录不存在");
        }
        // 获取记录的详细数据
        // 这里可以加载关联的AssessmentDetail列表
        return record;
    }

    @Override
    public void generateRecords(Long periodId, List<Long> teacherIds) {
        AssessmentPeriod period = periodMapper.selectById(periodId);
        if (period == null) {
            throw new BusinessException("考核周期不存在");
        }

        List<AssessmentConfig> configs = getConfigList();

        for (Long teacherId : teacherIds) {
            // 检查是否已存在该教师的考核记录
            LambdaQueryWrapper<AssessmentRecord> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AssessmentRecord::getTeacherId, teacherId)
                    .eq(AssessmentRecord::getPeriodId, periodId);
            AssessmentRecord existRecord = recordMapper.selectOne(queryWrapper);

            if (existRecord != null) {
                continue; // 跳过已存在的记录
            }

            AssessmentRecord record = new AssessmentRecord();
            record.setTeacherId(teacherId);
            record.setPeriodId(periodId);

            // 从supervision和evaluation表汇总数据计算
            // 这里使用默认分数作为示例，实际应从相关表查询
            record.setTeachingQualityScore(new BigDecimal("80"));
            record.setAttendanceScore(new BigDecimal("90"));
            record.setContributionScore(new BigDecimal("85"));

            // 计算总分
            BigDecimal totalScore = BigDecimal.ZERO;
            if (configs != null && !configs.isEmpty()) {
                for (AssessmentConfig config : configs) {
                    BigDecimal score = getScoreByDimension(teacherId, periodId, config.getDimensionCode());
                    totalScore = totalScore.add(score.multiply(new BigDecimal(config.getWeight()))
                            .divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                }
            } else {
                totalScore = record.getTeachingQualityScore()
                        .add(record.getAttendanceScore())
                        .add(record.getContributionScore())
                        .divide(new BigDecimal(3), 2, BigDecimal.ROUND_HALF_UP);
            }

            record.setTotalScore(totalScore);

            // 根据总分判断等级
            if (totalScore.compareTo(new BigDecimal("90")) >= 0) {
                record.setGrade("优秀");
            } else if (totalScore.compareTo(new BigDecimal("70")) >= 0) {
                record.setGrade("合格");
            } else {
                record.setGrade("不合格");
            }

            record.setCreateTime(LocalDateTime.now());
            recordMapper.insert(record);
        }
    }

    @Override
    public List<AssessmentPeriod> getPeriodList() {
        LambdaQueryWrapper<AssessmentPeriod> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(AssessmentPeriod::getCreateTime);
        return periodMapper.selectList(wrapper);
    }

    @Override
    public AssessmentPeriod createPeriod(String periodName, Integer periodType, LocalDate startDate, LocalDate endDate) {
        AssessmentPeriod period = new AssessmentPeriod();
        period.setPeriodName(periodName);
        period.setPeriodType(periodType);
        period.setStartDate(startDate);
        period.setEndDate(endDate);
        period.setStatus(0); // 未开始
        period.setCreateTime(LocalDateTime.now());
        periodMapper.insert(period);
        return period;
    }

    /**
     * 根据维度代码获取分数（示例实现）
     */
    private BigDecimal getScoreByDimension(Long teacherId, Long periodId, String dimensionCode) {
        // 实际应从supervision或evaluation表查询相关分数
        // 这里简化处理，返回默认值
        return new BigDecimal("80");
    }
}
