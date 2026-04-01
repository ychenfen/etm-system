package com.etm.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.etm.modules.system.entity.SysMenuEntity;

import java.util.List;

public interface SysMenuService extends IService<SysMenuEntity> {

    List<SysMenuEntity> getMenuTree();

    List<SysMenuEntity> getUserMenuTree(Long userId);
}
