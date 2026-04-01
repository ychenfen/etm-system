package com.etm.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.etm.modules.system.dto.UserQueryDTO;
import com.etm.modules.system.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    IPage<SysUser> selectUserPage(IPage<SysUser> page, @Param("query") UserQueryDTO query);
}
