package com.etm.modules.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.etm.modules.course.dto.EvaluationCreateDTO;
import com.etm.modules.course.entity.StudentEvaluation;
import com.etm.modules.course.mapper.StudentEvaluationMapper;
import com.etm.modules.course.service.StudentEvaluationService;
import com.etm.common.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生评教Service实现
 */
@Service
@RequiredArgsConstructor
public class StudentEvaluationServiceImpl extends ServiceImpl<StudentEvaluationMapper, StudentEvaluation> implements StudentEvaluationService {

    @Override
    public void submitEvaluation(EvaluationCreateDTO dto) {
        StudentEvaluation evaluation = new StudentEvaluation();
        evaluation.setScheduleId(dto.getScheduleId());
        evaluation.setStudentId(SecurityUtil.getCurrentUserId());
        evaluation.setClarityScore(dto.getClarityScore());
        evaluation.setInteractionScore(dto.getInteractionScore());
        evaluation.setHomeworkScore(dto.getHomeworkScore());
        evaluation.setAttitudeScore(dto.getAttitudeScore());
        evaluation.setOverallScore(dto.getOverallScore());
        evaluation.setComments(dto.getComments());
        evaluation.setIsAnonymous(dto.getIsAnonymous() != null ? dto.getIsAnonymous() : 0);
        evaluation.setCreateTime(LocalDateTime.now());
        this.save(evaluation);
    }

    @Override
    public Map<String, Object> getEvaluationStatistics(Long scheduleId) {
        LambdaQueryWrapper<StudentEvaluation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudentEvaluation::getScheduleId, scheduleId);
        List<StudentEvaluation> evaluations = this.list(wrapper);

        Map<String, Object> statistics = new HashMap<>();

        if (evaluations.isEmpty()) {
            statistics.put("clarityAvg", 0.0);
            statistics.put("interactionAvg", 0.0);
            statistics.put("homeworkAvg", 0.0);
            statistics.put("attitudeAvg", 0.0);
            statistics.put("overallAvg", 0.0);
            statistics.put("count", 0);
            return statistics;
        }

        double claritySum = 0, interactionSum = 0, homeworkSum = 0, attitudeSum = 0, overallSum = 0;
        for (StudentEvaluation e : evaluations) {
            claritySum += e.getClarityScore() != null ? e.getClarityScore() : 0;
            interactionSum += e.getInteractionScore() != null ? e.getInteractionScore() : 0;
            homeworkSum += e.getHomeworkScore() != null ? e.getHomeworkScore() : 0;
            attitudeSum += e.getAttitudeScore() != null ? e.getAttitudeScore() : 0;
            overallSum += e.getOverallScore() != null ? e.getOverallScore() : 0;
        }

        int count = evaluations.size();
        statistics.put("clarityAvg", claritySum / count);
        statistics.put("interactionAvg", interactionSum / count);
        statistics.put("homeworkAvg", homeworkSum / count);
        statistics.put("attitudeAvg", attitudeSum / count);
        statistics.put("overallAvg", overallSum / count);
        statistics.put("count", count);

        return statistics;
    }
}
