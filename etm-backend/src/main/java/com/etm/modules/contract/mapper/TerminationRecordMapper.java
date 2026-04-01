package com.etm.modules.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etm.modules.contract.entity.TerminationRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TerminationRecordMapper extends BaseMapper<TerminationRecord> {

    @Select("SELECT * FROM termination_record WHERE college_id = #{collegeId} ORDER BY create_time DESC")
    IPage<TerminationRecord> selectTerminationPage(Page<TerminationRecord> page, Long collegeId);
}
