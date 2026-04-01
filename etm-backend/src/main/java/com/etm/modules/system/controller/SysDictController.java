package com.etm.modules.system.controller;

import com.etm.common.result.Result;
import com.etm.modules.system.entity.SysDictData;
import com.etm.modules.system.entity.SysDictType;
import com.etm.modules.system.service.SysDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "系统字典管理")
@RestController
@RequestMapping("/system/dict")
@RequiredArgsConstructor
public class SysDictController {

    private final SysDictService dictService;

    // ========== DictType Methods ==========

    @Operation(summary = "获取字典类型列表")
    @GetMapping("/type/list")
    @PreAuthorize("hasRole('admin')")
    public Result<List<SysDictType>> getDictTypeList() {
        return Result.success(dictService.getDictTypeList());
    }

    @Operation(summary = "获取字典类型详情")
    @GetMapping("/type/{id}")
    @PreAuthorize("hasRole('admin')")
    public Result<SysDictType> getDictTypeById(@PathVariable Long id) {
        return Result.success(dictService.getDictTypeById(id));
    }

    @Operation(summary = "新增字典类型")
    @PostMapping("/type")
    @PreAuthorize("hasRole('admin')")
    public Result<SysDictType> createDictType(@Valid @RequestBody SysDictType dictType) {
        return Result.success(dictService.createDictType(dictType));
    }

    @Operation(summary = "修改字典类型")
    @PutMapping("/type/{id}")
    @PreAuthorize("hasRole('admin')")
    public Result<SysDictType> updateDictType(@PathVariable Long id, @Valid @RequestBody SysDictType dictType) {
        dictType.setId(id);
        return Result.success(dictService.updateDictType(dictType));
    }

    @Operation(summary = "删除字典类型")
    @DeleteMapping("/type/{id}")
    @PreAuthorize("hasRole('admin')")
    public Result<?> deleteDictType(@PathVariable Long id) {
        dictService.deleteDictType(id);
        return Result.success("删除成功");
    }

    // ========== DictData Methods ==========

    @Operation(summary = "根据类型获取字典数据列表")
    @GetMapping("/data/{dictType}")
    public Result<List<SysDictData>> getDictDataByType(@PathVariable String dictType) {
        return Result.success(dictService.getDictDataByType(dictType));
    }

    @Operation(summary = "获取字典数据详情")
    @GetMapping("/data/{id}")
    @PreAuthorize("hasRole('admin')")
    public Result<SysDictData> getDictDataById(@PathVariable Long id) {
        return Result.success(dictService.getDictDataById(id));
    }

    @Operation(summary = "新增字典数据")
    @PostMapping("/data")
    @PreAuthorize("hasRole('admin')")
    public Result<SysDictData> createDictData(@Valid @RequestBody SysDictData dictData) {
        return Result.success(dictService.createDictData(dictData));
    }

    @Operation(summary = "修改字典数据")
    @PutMapping("/data/{id}")
    @PreAuthorize("hasRole('admin')")
    public Result<SysDictData> updateDictData(@PathVariable Long id, @Valid @RequestBody SysDictData dictData) {
        dictData.setId(id);
        return Result.success(dictService.updateDictData(dictData));
    }

    @Operation(summary = "删除字典数据")
    @DeleteMapping("/data/{id}")
    @PreAuthorize("hasRole('admin')")
    public Result<?> deleteDictData(@PathVariable Long id) {
        dictService.deleteDictData(id);
        return Result.success("删除成功");
    }
}
