package com.etm.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.etm.modules.system.entity.SysDictData;
import com.etm.modules.system.entity.SysDictType;

import java.util.List;

public interface SysDictService extends IService<SysDictType> {

    // ========== DictType Methods ==========
    List<SysDictType> getDictTypeList();

    SysDictType getDictTypeById(Long id);

    SysDictType createDictType(SysDictType dictType);

    SysDictType updateDictType(SysDictType dictType);

    void deleteDictType(Long id);

    // ========== DictData Methods ==========
    List<SysDictData> getDictDataByType(String dictType);

    SysDictData getDictDataById(Long id);

    SysDictData createDictData(SysDictData dictData);

    SysDictData updateDictData(SysDictData dictData);

    void deleteDictData(Long id);
}
