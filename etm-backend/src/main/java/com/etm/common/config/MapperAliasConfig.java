package com.etm.common.config;

import com.etm.modules.assessment.mapper.RecordMapper;
import com.etm.modules.college.mapper.BaseCollegeMybatisMapper;
import com.etm.modules.course.mapper.ScheduleMapper;
import com.etm.modules.salary.mapper.SettlementMapper;
import com.etm.modules.teacher.mapper.TeacherMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperAliasConfig {

    @Bean
    public TeacherMapper teacherInfoMapper(TeacherMapper mapper) {
        return mapper;
    }

    @Bean
    public BaseCollegeMybatisMapper baseCollegeMapper(BaseCollegeMybatisMapper mapper) {
        return mapper;
    }

    @Bean
    public ScheduleMapper courseScheduleMapper(ScheduleMapper mapper) {
        return mapper;
    }

    @Bean
    public SettlementMapper salarySettlementMapper(SettlementMapper mapper) {
        return mapper;
    }

    @Bean
    public RecordMapper assessmentRecordMapper(RecordMapper mapper) {
        return mapper;
    }
}
