package com.etm.modules.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.etm.common.result.PageResult;
import com.etm.modules.course.dto.SupervisionCreateDTO;
import com.etm.modules.course.entity.TeachingSupervision;

/**
 * 教学督导Service接口
 */
public interface TeachingSupervisionService extends IService<TeachingSupervision> {

    /**
     * 分页查询教学督导记录
     */
    PageResult<TeachingSupervision> getSupervisionPage(Long scheduleId, Integer pageNum, Integer pageSize);

    /**
     * 创建教学督导记录，supervisorId从SecurityUtil获取
     */
    TeachingSupervision createSupervision(SupervisionCreateDTO dto);
}
