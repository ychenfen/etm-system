package com.etm.modules.salary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.etm.modules.salary.entity.SalarySettlement;
import com.etm.modules.salary.dto.SalarySettlementQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 薪酬结算Mapper
 */
@Mapper
public interface SalarySettlementMapper extends BaseMapper<SalarySettlement> {

    /**
     * 查询结算详细信息，联接course_schedule统计课时明细
     */
    @Select("SELECT cs.id, cs.courseName, cs.className, cs.totalHours FROM course_schedule cs " +
            "WHERE cs.teacherId = #{teacherId} AND cs.semester = #{semester} AND cs.deleted = 0")
    List<Map<String, Object>> selectSettlementDetail(@Param("teacherId") Long teacherId, @Param("semester") String semester);

    /**
     * 分页查询薪酬结算
     */
    IPage<SalarySettlement> selectSettlementPage(IPage page, @Param("query") SalarySettlementQueryDTO query);
}
