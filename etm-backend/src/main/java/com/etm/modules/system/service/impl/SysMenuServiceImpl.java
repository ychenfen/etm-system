package com.etm.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etm.modules.system.entity.SysMenuEntity;
import com.etm.modules.system.mapper.MenuMapper;
import com.etm.modules.system.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<MenuMapper, SysMenuEntity> implements SysMenuService {

    private final MenuMapper menuMapper;

    @Override
    public List<SysMenuEntity> getMenuTree() {
        QueryWrapper<SysMenuEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort_order");
        List<SysMenuEntity> allMenus = menuMapper.selectList(wrapper);
        return buildMenuTree(allMenus, 0L);
    }

    @Override
    public List<SysMenuEntity> getUserMenuTree(Long userId) {
        QueryWrapper<SysMenuEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort_order");
        List<SysMenuEntity> allMenus = menuMapper.selectList(wrapper);
        return buildMenuTree(allMenus, 0L);
    }

    private List<SysMenuEntity> buildMenuTree(List<SysMenuEntity> allMenus, Long parentId) {
        List<SysMenuEntity> result = new ArrayList<>();

        List<SysMenuEntity> childMenus = allMenus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .collect(Collectors.toList());

        for (SysMenuEntity menu : childMenus) {
            menu.setChildren(buildMenuTree(allMenus, menu.getId()));
            result.add(menu);
        }

        return result;
    }
}
