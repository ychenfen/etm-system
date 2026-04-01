package com.etm.modules.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etm.modules.contract.entity.RenewalApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RenewalApplicationMapper extends BaseMapper<RenewalApplication> {

    @Select("SELECT * FROM renewal_application WHERE " +
            "CASE WHEN #{teacherId} IS NOT NULL THEN teacher_id = #{teacherId} ELSE 1=1 END AND " +
            "CASE WHEN #{collegeId} IS NOT NULL THEN college_id = #{collegeId} ELSE 1=1 END AND " +
            "CASE WHEN #{approvalStatus} IS NOT NULL THEN approval_status = #{approvalStatus} ELSE 1=1 END " +
            "ORDER BY create_time DESC")
    IPage<RenewalApplication> selectRenewalPage(Page<RenewalApplication> page,
                                                 Long teacherId,
                                                 Long collegeId,
                                                 Integer approvalStatus);
}
