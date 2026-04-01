package com.etm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.etm.entity.Evaluation;

public interface EvaluationService extends IService<Evaluation> {
    Page<Evaluation> pageList(int current, int size, Long teacherId, String semester);
}
