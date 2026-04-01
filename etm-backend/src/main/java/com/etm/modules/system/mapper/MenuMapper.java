package com.etm.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.etm.modules.system.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;

/**
 * @deprecated Use SysMenuMapper instead
 */
@Mapper
public interface MenuMapper extends BaseMapper<SysMenu> {
}
