package com.etm.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etm.common.exception.BusinessException;
import com.etm.modules.system.entity.SysDictData;
import com.etm.modules.system.entity.SysDictType;
import com.etm.modules.system.mapper.SysDictDataMapper;
import com.etm.modules.system.mapper.SysDictTypeMapper;
import com.etm.modules.system.service.SysDictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysDictServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements SysDictService {

    private final SysDictDataMapper sysDictDataMapper;

    // ========== DictType Methods ==========

    @Override
    public List<SysDictType> getDictTypeList() {
        return list(new LambdaQueryWrapper<SysDictType>()
                .eq(SysDictType::getStatus, 1)
                .orderByDesc(SysDictType::getCreateTime));
    }

    @Override
    public SysDictType getDictTypeById(Long id) {
        SysDictType dictType = getById(id);
        if (dictType == null) {
            throw BusinessException.of("字典类型不存在");
        }
        return dictType;
    }

    @Override
    @Transactional
    public SysDictType createDictType(SysDictType dictType) {
        long count = count(new LambdaQueryWrapper<SysDictType>()
                .eq(SysDictType::getDictType, dictType.getDictType()));
        if (count > 0) {
            throw BusinessException.of("字典类型已存在");
        }

        if (dictType.getStatus() == null) {
            dictType.setStatus(1);
        }
        save(dictType);
        return dictType;
    }

    @Override
    @Transactional
    public SysDictType updateDictType(SysDictType dictType) {
        SysDictType existType = getById(dictType.getId());
        if (existType == null) {
            throw BusinessException.of("字典类型不存在");
        }

        long count = count(new LambdaQueryWrapper<SysDictType>()
                .eq(SysDictType::getDictType, dictType.getDictType())
                .ne(SysDictType::getId, dictType.getId()));
        if (count > 0) {
            throw BusinessException.of("字典类型已存在");
        }

        updateById(dictType);
        return dictType;
    }

    @Override
    @Transactional
    public void deleteDictType(Long id) {
        SysDictType dictType = getById(id);
        if (dictType == null) {
            throw BusinessException.of("字典类型不存在");
        }

        // Delete related dict data
        sysDictDataMapper.delete(new LambdaQueryWrapper<SysDictData>()
                .eq(SysDictData::getDictType, dictType.getDictType()));

        removeById(id);
    }

    // ========== DictData Methods ==========

    @Override
    public List<SysDictData> getDictDataByType(String dictType) {
        if (!StringUtils.hasText(dictType)) {
            throw BusinessException.of("字典类型不能为空");
        }
        return sysDictDataMapper.selectList(new LambdaQueryWrapper<SysDictData>()
                .eq(SysDictData::getDictType, dictType)
                .eq(SysDictData::getStatus, 1)
                .orderByAsc(SysDictData::getSort)
                .orderByDesc(SysDictData::getCreateTime));
    }

    @Override
    public SysDictData getDictDataById(Long id) {
        SysDictData dictData = sysDictDataMapper.selectById(id);
        if (dictData == null) {
            throw BusinessException.of("字典数据不存在");
        }
        return dictData;
    }

    @Override
    @Transactional
    public SysDictData createDictData(SysDictData dictData) {
        // Verify dict type exists
        long typeCount = count(new LambdaQueryWrapper<SysDictType>()
                .eq(SysDictType::getDictType, dictData.getDictType()));
        if (typeCount == 0) {
            throw BusinessException.of("字典类型不存在");
        }

        if (dictData.getStatus() == null) {
            dictData.setStatus(1);
        }
        sysDictDataMapper.insert(dictData);
        return dictData;
    }

    @Override
    @Transactional
    public SysDictData updateDictData(SysDictData dictData) {
        SysDictData existData = sysDictDataMapper.selectById(dictData.getId());
        if (existData == null) {
            throw BusinessException.of("字典数据不存在");
        }

        sysDictDataMapper.updateById(dictData);
        return dictData;
    }

    @Override
    @Transactional
    public void deleteDictData(Long id) {
        SysDictData dictData = sysDictDataMapper.selectById(id);
        if (dictData == null) {
            throw BusinessException.of("字典数据不存在");
        }
        sysDictDataMapper.deleteById(id);
    }
}
