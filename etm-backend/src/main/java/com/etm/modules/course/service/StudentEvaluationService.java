package com.etm.modules.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.etm.modules.course.dto.EvaluationCreateDTO;
import com.etm.modules.course.entity.StudentEvaluation;

import java.util.Map;

/**
 * 学生评教Service接口
 */
public interface StudentEvaluationService extends IService<StudentEvaluation> {

    /**
     * 提交评教
     */
    void submitEvaluation(EvaluationCreateDTO dto);

    /**
     * 获取课程的评教统计数据
     * 返回各项平均分
     */
    Map<String, Object> getEvaluationStatistics(Long scheduleId);
}
