package com.etm.modules.college.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etm.common.exception.BusinessException;
import com.etm.common.result.PageResult;
import com.etm.modules.college.dto.CollegeDTO;
import com.etm.modules.college.dto.CollegeQueryDTO;
import com.etm.modules.college.entity.BaseCollege;
import com.etm.modules.college.mapper.BaseCollegeMapper;
import com.etm.modules.college.service.BaseCollegeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BaseCollegeServiceImpl extends ServiceImpl<BaseCollegeMapper, BaseCollege> implements BaseCollegeService {

    @Override
    public PageResult<BaseCollege> getCollegePage(CollegeQueryDTO query) {
        LambdaQueryWrapper<BaseCollege> wrapper = new LambdaQueryWrapper<BaseCollege>()
                .eq(BaseCollege::getDeleted, 0)
                .like(StringUtils.hasText(query.getCollegeName()), BaseCollege::getCollegeName, query.getCollegeName())
                .eq(query.getStatus() != null, BaseCollege::getStatus, query.getStatus())
                .orderByAsc(BaseCollege::getSort)
                .orderByDesc(BaseCollege::getCreateTime);
        IPage<BaseCollege> page = page(new Page<>(query.getPageNum(), query.getPageSize()), wrapper);
        return PageResult.of(page);
    }

    @Override
    public List<Map<String, Object>> getCollegeSelect() {
        return baseMapper.selectMaps(new LambdaQueryWrapper<BaseCollege>()
                .eq(BaseCollege::getDeleted, 0)
                .eq(BaseCollege::getStatus, 1)
                .select(BaseCollege::getId, BaseCollege::getCollegeName)
                .orderByAsc(BaseCollege::getSort));
    }

    @Override
    @Transactional
    public BaseCollege createCollege(CollegeDTO dto) {
        long count = count(new LambdaQueryWrapper<BaseCollege>()
                .eq(BaseCollege::getCollegeCode, dto.getCollegeCode())
                .eq(BaseCollege::getDeleted, 0));
        if (count > 0) {
            throw BusinessException.of("学院代码已存在");
        }

        BaseCollege college = new BaseCollege();
        college.setCollegeName(dto.getCollegeName());
        college.setCollegeCode(dto.getCollegeCode());
        college.setContactPerson(dto.getContactPerson());
        college.setContactPhone(dto.getContactPhone());
        college.setContactEmail(dto.getContactEmail());
        college.setDescription(dto.getDescription());
        college.setSort(dto.getSort() != null ? dto.getSort() : 0);
        college.setStatus(1);

        save(college);
        return college;
    }

    @Override
    @Transactional
    public BaseCollege updateCollege(Long id, CollegeDTO dto) {
        BaseCollege college = getById(id);
        if (college == null) {
            throw BusinessException.of("学院不存在");
        }

        long count = count(new LambdaQueryWrapper<BaseCollege>()
                .eq(BaseCollege::getCollegeCode, dto.getCollegeCode())
                .ne(BaseCollege::getId, id)
                .eq(BaseCollege::getDeleted, 0));
        if (count > 0) {
            throw BusinessException.of("学院代码已存在");
        }

        college.setCollegeName(dto.getCollegeName());
        college.setCollegeCode(dto.getCollegeCode());
        college.setContactPerson(dto.getContactPerson());
        college.setContactPhone(dto.getContactPhone());
        college.setContactEmail(dto.getContactEmail());
        college.setDescription(dto.getDescription());
        if (dto.getSort() != null) {
            college.setSort(dto.getSort());
        }

        updateById(college);
        return college;
    }

    @Override
    @Transactional
    public void deleteCollege(Long id) {
        BaseCollege college = getById(id);
        if (college == null) {
            throw BusinessException.of("学院不存在");
        }
        removeById(id);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        BaseCollege college = getById(id);
        if (college == null) {
            throw BusinessException.of("学院不存在");
        }
        college.setStatus(status);
        updateById(college);
    }
}
