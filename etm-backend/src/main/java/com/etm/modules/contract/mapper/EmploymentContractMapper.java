package com.etm.modules.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etm.modules.contract.entity.EmploymentContract;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmploymentContractMapper extends BaseMapper<EmploymentContract> {

    @Select("SELECT * FROM employment_contract WHERE deleted = 0 AND " +
            "CASE WHEN #{teacherId} IS NOT NULL THEN teacher_id = #{teacherId} ELSE 1=1 END AND " +
            "CASE WHEN #{collegeId} IS NOT NULL THEN college_id = #{collegeId} ELSE 1=1 END AND " +
            "CASE WHEN #{signStatus} IS NOT NULL THEN sign_status = #{signStatus} ELSE 1=1 END " +
            "ORDER BY create_time DESC")
    IPage<EmploymentContract> selectContractPage(Page<EmploymentContract> page,
                                                  Long teacherId,
                                                  Long collegeId,
                                                  Integer signStatus);
}
