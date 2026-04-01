package com.etm.modules.college.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.etm.common.result.PageResult;
import com.etm.modules.college.dto.CollegeDTO;
import com.etm.modules.college.dto.CollegeQueryDTO;
import com.etm.modules.college.entity.BaseCollege;

import java.util.List;
import java.util.Map;

public interface BaseCollegeService extends IService<BaseCollege> {

    PageResult<BaseCollege> getCollegePage(CollegeQueryDTO query);

    List<Map<String, Object>> getCollegeSelect();

    BaseCollege createCollege(CollegeDTO dto);

    BaseCollege updateCollege(Long id, CollegeDTO dto);

    void deleteCollege(Long id);

    void updateStatus(Long id, Integer status);
}
