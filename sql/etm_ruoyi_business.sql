-- =============================================
-- 高校外聘老师管理系统 - 业务表脚本
-- 适配 RuoYi-Vue 框架
-- 使用前请先执行 RuoYi 自带的 ry_2024xxxx.sql 和 quartz.sql
-- =============================================

-- 使用RuoYi的数据库
USE `ry-vue`;

-- =============================================
-- 一、基础信息管理模块
-- =============================================

-- 1.1 二级学院表
DROP TABLE IF EXISTS `etm_college`;
CREATE TABLE `etm_college` (
    `college_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '学院ID',
    `college_name` VARCHAR(100) NOT NULL COMMENT '学院名称',
    `college_code` VARCHAR(50) DEFAULT NULL COMMENT '学院编码',
    `contact_person` VARCHAR(50) DEFAULT NULL COMMENT '对接人姓名',
    `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '对接人电话',
    `contact_email` VARCHAR(100) DEFAULT NULL COMMENT '对接人邮箱',
    `description` TEXT COMMENT '学院简介',
    `order_num` INT DEFAULT 0 COMMENT '显示顺序',
    `status` CHAR(1) DEFAULT '0' COMMENT '状态(0正常 1停用)',
    `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志(0存在 2删除)',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`college_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='二级学院表';

-- 1.2 外聘老师基础信息表
DROP TABLE IF EXISTS `etm_teacher`;
CREATE TABLE `etm_teacher` (
    `teacher_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '外聘老师ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '关联系统用户ID',
    `teacher_no` VARCHAR(50) DEFAULT NULL COMMENT '外聘老师编号',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `gender` CHAR(1) DEFAULT '0' COMMENT '性别(0男 1女 2未知)',
    `birth_date` DATE DEFAULT NULL COMMENT '出生日期',
    `id_card` VARCHAR(18) DEFAULT NULL COMMENT '身份证号',
    `nationality` VARCHAR(50) DEFAULT '汉族' COMMENT '民族',
    `political_status` VARCHAR(50) DEFAULT NULL COMMENT '政治面貌',
    `education` VARCHAR(50) DEFAULT NULL COMMENT '最高学历',
    `degree` VARCHAR(50) DEFAULT NULL COMMENT '最高学位',
    `major` VARCHAR(100) DEFAULT NULL COMMENT '所学专业',
    `graduate_school` VARCHAR(100) DEFAULT NULL COMMENT '毕业院校',
    `graduate_date` DATE DEFAULT NULL COMMENT '毕业时间',
    `title` VARCHAR(50) DEFAULT NULL COMMENT '职称',
    `title_level` CHAR(1) DEFAULT NULL COMMENT '职称等级(1初级 2中级 3副高 4正高)',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号码',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '电子邮箱',
    `address` VARCHAR(255) DEFAULT NULL COMMENT '联系地址',
    `emergency_contact` VARCHAR(50) DEFAULT NULL COMMENT '紧急联系人',
    `emergency_phone` VARCHAR(20) DEFAULT NULL COMMENT '紧急联系人电话',
    `bank_name` VARCHAR(100) DEFAULT NULL COMMENT '开户银行',
    `bank_account` VARCHAR(50) DEFAULT NULL COMMENT '银行账号',
    `photo_url` VARCHAR(255) DEFAULT NULL COMMENT '照片地址',
    `resume_url` VARCHAR(255) DEFAULT NULL COMMENT '简历地址',
    `specialty` TEXT COMMENT '专业特长',
    `teacher_status` CHAR(1) DEFAULT '0' COMMENT '状态(0待聘用 1聘用中 2待续聘 3已解聘)',
    `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志(0存在 2删除)',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`teacher_id`),
    UNIQUE KEY `uk_id_card` (`id_card`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`teacher_status`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='外聘老师基础信息表';

-- 1.3 外聘老师工作经历表
DROP TABLE IF EXISTS `etm_teacher_work`;
CREATE TABLE `etm_teacher_work` (
    `work_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `teacher_id` BIGINT NOT NULL COMMENT '外聘老师ID',
    `company_name` VARCHAR(100) NOT NULL COMMENT '单位名称',
    `position` VARCHAR(100) DEFAULT NULL COMMENT '担任职务',
    `start_date` DATE DEFAULT NULL COMMENT '开始时间',
    `end_date` DATE DEFAULT NULL COMMENT '结束时间',
    `work_content` TEXT COMMENT '工作内容',
    `leave_reason` VARCHAR(255) DEFAULT NULL COMMENT '离职原因',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`work_id`),
    KEY `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='外聘老师工作经历表';

-- 1.4 外聘老师资质证书表
DROP TABLE IF EXISTS `etm_teacher_cert`;
CREATE TABLE `etm_teacher_cert` (
    `cert_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `teacher_id` BIGINT NOT NULL COMMENT '外聘老师ID',
    `cert_type` VARCHAR(50) NOT NULL COMMENT '证书类型',
    `cert_name` VARCHAR(100) NOT NULL COMMENT '证书名称',
    `cert_no` VARCHAR(100) DEFAULT NULL COMMENT '证书编号',
    `issue_org` VARCHAR(100) DEFAULT NULL COMMENT '发证机构',
    `issue_date` DATE DEFAULT NULL COMMENT '发证日期',
    `expire_date` DATE DEFAULT NULL COMMENT '有效期至',
    `cert_level` VARCHAR(50) DEFAULT NULL COMMENT '证书等级',
    `file_url` VARCHAR(255) DEFAULT NULL COMMENT '证书扫描件地址',
    `verify_status` CHAR(1) DEFAULT '0' COMMENT '审核状态(0待审核 1已通过 2已驳回)',
    `verify_remark` VARCHAR(255) DEFAULT NULL COMMENT '审核备注',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`cert_id`),
    KEY `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='外聘老师资质证书表';

-- 1.5 岗位需求表
DROP TABLE IF EXISTS `etm_position`;
CREATE TABLE `etm_position` (
    `position_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
    `college_id` BIGINT NOT NULL COMMENT '所属学院ID',
    `position_name` VARCHAR(100) NOT NULL COMMENT '岗位名称',
    `subject_category` VARCHAR(50) DEFAULT NULL COMMENT '学科门类',
    `major_direction` VARCHAR(100) DEFAULT NULL COMMENT '专业方向',
    `teaching_level` VARCHAR(50) DEFAULT NULL COMMENT '授课层次(本科/研究生)',
    `course_type` VARCHAR(50) DEFAULT NULL COMMENT '课程类型(理论课/实践课/讲座)',
    `education_req` VARCHAR(50) DEFAULT NULL COMMENT '学历要求',
    `title_req` VARCHAR(50) DEFAULT NULL COMMENT '职称要求',
    `experience_years` INT DEFAULT 0 COMMENT '工作年限要求',
    `skill_req` TEXT COMMENT '技能要求',
    `teaching_hours` INT DEFAULT NULL COMMENT '授课时长(学时/学期)',
    `salary_min` DECIMAL(10,2) DEFAULT NULL COMMENT '薪酬下限(元/课时)',
    `salary_max` DECIMAL(10,2) DEFAULT NULL COMMENT '薪酬上限(元/课时)',
    `required_count` INT DEFAULT 1 COMMENT '需求人数',
    `hired_count` INT DEFAULT 0 COMMENT '已聘人数',
    `description` TEXT COMMENT '岗位描述',
    `status` CHAR(1) DEFAULT '0' COMMENT '状态(0正常 1停用)',
    `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`position_id`),
    KEY `idx_college_id` (`college_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='岗位需求表';

-- =============================================
-- 二、招募与聘用管理模块
-- =============================================

-- 2.1 招募公告表
DROP TABLE IF EXISTS `etm_recruit_notice`;
CREATE TABLE `etm_recruit_notice` (
    `notice_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '公告ID',
    `title` VARCHAR(200) NOT NULL COMMENT '公告标题',
    `position_id` BIGINT DEFAULT NULL COMMENT '关联岗位ID',
    `college_id` BIGINT DEFAULT NULL COMMENT '发布学院ID',
    `content` TEXT COMMENT '公告内容',
    `requirements` TEXT COMMENT '报名要求',
    `materials` TEXT COMMENT '所需材料说明',
    `apply_start_time` DATETIME DEFAULT NULL COMMENT '报名开始时间',
    `apply_end_time` DATETIME DEFAULT NULL COMMENT '报名截止时间',
    `contact_person` VARCHAR(50) DEFAULT NULL COMMENT '联系人',
    `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `view_count` INT DEFAULT 0 COMMENT '浏览次数',
    `apply_count` INT DEFAULT 0 COMMENT '报名人数',
    `notice_status` CHAR(1) DEFAULT '0' COMMENT '状态(0草稿 1已发布 2已结束 3已取消)',
    `publish_time` DATETIME DEFAULT NULL COMMENT '发布时间',
    `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`notice_id`),
    KEY `idx_college_id` (`college_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='招募公告表';

-- 2.2 报名申请表
DROP TABLE IF EXISTS `etm_application`;
CREATE TABLE `etm_application` (
    `application_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '申请ID',
    `application_no` VARCHAR(50) DEFAULT NULL COMMENT '申请编号',
    `notice_id` BIGINT NOT NULL COMMENT '公告ID',
    `teacher_id` BIGINT NOT NULL COMMENT '外聘老师ID',
    `apply_time` DATETIME DEFAULT NULL COMMENT '报名时间',
    `self_intro` TEXT COMMENT '自我介绍',
    `match_score` DECIMAL(5,2) DEFAULT NULL COMMENT '系统匹配度评分',
    `first_status` CHAR(1) DEFAULT '0' COMMENT '首轮状态(0待筛选 1通过 2不通过)',
    `first_time` DATETIME DEFAULT NULL COMMENT '首轮筛选时间',
    `first_remark` VARCHAR(500) DEFAULT NULL COMMENT '首轮筛选备注',
    `second_status` CHAR(1) DEFAULT '0' COMMENT '二轮状态(0待审核 1通过 2不通过)',
    `second_user_id` BIGINT DEFAULT NULL COMMENT '二轮审核人ID',
    `second_time` DATETIME DEFAULT NULL COMMENT '二轮审核时间',
    `second_remark` VARCHAR(500) DEFAULT NULL COMMENT '二轮审核备注',
    `interview_time` DATETIME DEFAULT NULL COMMENT '面试时间',
    `interview_location` VARCHAR(200) DEFAULT NULL COMMENT '面试地点',
    `interview_score` DECIMAL(5,2) DEFAULT NULL COMMENT '面试总分',
    `interview_remark` TEXT COMMENT '面试评语',
    `final_status` CHAR(1) DEFAULT '0' COMMENT '最终状态(0进行中 1录用 2不录用 3放弃)',
    `final_time` DATETIME DEFAULT NULL COMMENT '最终确定时间',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`application_id`),
    UNIQUE KEY `uk_notice_teacher` (`notice_id`, `teacher_id`),
    KEY `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='报名申请表';

-- 2.3 面试评分表
DROP TABLE IF EXISTS `etm_interview_score`;
CREATE TABLE `etm_interview_score` (
    `score_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `application_id` BIGINT NOT NULL COMMENT '申请ID',
    `interviewer_id` BIGINT NOT NULL COMMENT '面试官ID',
    `interviewer_name` VARCHAR(50) DEFAULT NULL COMMENT '面试官姓名',
    `teaching_concept` DECIMAL(5,2) DEFAULT NULL COMMENT '教学理念(满分20)',
    `professional_ability` DECIMAL(5,2) DEFAULT NULL COMMENT '专业能力(满分30)',
    `expression_ability` DECIMAL(5,2) DEFAULT NULL COMMENT '表达能力(满分20)',
    `practical_exp` DECIMAL(5,2) DEFAULT NULL COMMENT '实践经验(满分20)',
    `overall_impression` DECIMAL(5,2) DEFAULT NULL COMMENT '综合印象(满分10)',
    `total_score` DECIMAL(5,2) DEFAULT NULL COMMENT '总分',
    `evaluation` TEXT COMMENT '综合评价',
    `is_recommend` CHAR(1) DEFAULT NULL COMMENT '是否推荐录用(0否 1是)',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`score_id`),
    KEY `idx_application_id` (`application_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='面试评分表';

-- 2.4 聘用审批表
DROP TABLE IF EXISTS `etm_employ_approval`;
CREATE TABLE `etm_employ_approval` (
    `approval_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '审批ID',
    `approval_no` VARCHAR(50) DEFAULT NULL COMMENT '审批编号',
    `application_id` BIGINT NOT NULL COMMENT '报名申请ID',
    `teacher_id` BIGINT NOT NULL COMMENT '外聘老师ID',
    `college_id` BIGINT NOT NULL COMMENT '学院ID',
    `position_id` BIGINT DEFAULT NULL COMMENT '岗位ID',
    `proposed_salary` DECIMAL(10,2) DEFAULT NULL COMMENT '拟定课时薪酬',
    `employ_start_date` DATE DEFAULT NULL COMMENT '拟聘开始日期',
    `employ_end_date` DATE DEFAULT NULL COMMENT '拟聘结束日期',
    `apply_reason` TEXT COMMENT '申请理由',
    `college_status` CHAR(1) DEFAULT '0' COMMENT '学院负责人审批(0待审批 1通过 2驳回)',
    `college_user_id` BIGINT DEFAULT NULL COMMENT '学院负责人ID',
    `college_time` DATETIME DEFAULT NULL COMMENT '审批时间',
    `college_remark` VARCHAR(500) DEFAULT NULL COMMENT '审批意见',
    `hr_salary_status` CHAR(1) DEFAULT '0' COMMENT '薪酬岗审批(0待审批 1通过 2驳回)',
    `hr_salary_user_id` BIGINT DEFAULT NULL COMMENT '薪酬岗ID',
    `hr_salary_time` DATETIME DEFAULT NULL COMMENT '审批时间',
    `hr_salary_remark` VARCHAR(500) DEFAULT NULL COMMENT '审批意见',
    `approved_salary` DECIMAL(10,2) DEFAULT NULL COMMENT '核定课时薪酬',
    `hr_director_status` CHAR(1) DEFAULT '0' COMMENT '处长审批(0待审批 1通过 2驳回)',
    `hr_director_user_id` BIGINT DEFAULT NULL COMMENT '处长ID',
    `hr_director_time` DATETIME DEFAULT NULL COMMENT '审批时间',
    `hr_director_remark` VARCHAR(500) DEFAULT NULL COMMENT '审批意见',
    `current_node` VARCHAR(50) DEFAULT 'college' COMMENT '当前审批节点',
    `approval_status` CHAR(1) DEFAULT '0' COMMENT '整体状态(0审批中 1已通过 2已驳回 3已撤回)',
    `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`approval_id`),
    KEY `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='聘用审批表';

-- 2.5 聘用合同表
DROP TABLE IF EXISTS `etm_contract`;
CREATE TABLE `etm_contract` (
    `contract_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '合同ID',
    `contract_no` VARCHAR(50) NOT NULL COMMENT '合同编号',
    `teacher_id` BIGINT NOT NULL COMMENT '外聘老师ID',
    `college_id` BIGINT NOT NULL COMMENT '学院ID',
    `approval_id` BIGINT DEFAULT NULL COMMENT '审批ID',
    `contract_type` CHAR(1) DEFAULT '1' COMMENT '合同类型(1首次聘用 2续聘)',
    `start_date` DATE NOT NULL COMMENT '合同开始日期',
    `end_date` DATE NOT NULL COMMENT '合同结束日期',
    `salary_standard` DECIMAL(10,2) DEFAULT NULL COMMENT '课时薪酬标准',
    `work_content` TEXT COMMENT '工作内容',
    `contract_terms` TEXT COMMENT '合同条款',
    `contract_file` VARCHAR(255) DEFAULT NULL COMMENT '合同文件地址',
    `sign_status` CHAR(1) DEFAULT '0' COMMENT '签署状态(0待签署 1学校已签 2双方已签)',
    `teacher_sign_time` DATETIME DEFAULT NULL COMMENT '老师签署时间',
    `school_sign_time` DATETIME DEFAULT NULL COMMENT '学校签署时间',
    `contract_status` CHAR(1) DEFAULT '1' COMMENT '合同状态(0已作废 1生效中 2已到期 3已终止)',
    `terminate_reason` VARCHAR(500) DEFAULT NULL COMMENT '终止原因',
    `terminate_time` DATETIME DEFAULT NULL COMMENT '终止时间',
    `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`contract_id`),
    UNIQUE KEY `uk_contract_no` (`contract_no`),
    KEY `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='聘用合同表';

-- =============================================
-- 三、授课管理模块
-- =============================================

-- 3.1 课程信息表
DROP TABLE IF EXISTS `etm_course`;
CREATE TABLE `etm_course` (
    `course_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '课程ID',
    `course_code` VARCHAR(50) DEFAULT NULL COMMENT '课程代码',
    `course_name` VARCHAR(100) NOT NULL COMMENT '课程名称',
    `college_id` BIGINT DEFAULT NULL COMMENT '开课学院ID',
    `course_type` VARCHAR(50) DEFAULT NULL COMMENT '课程类型',
    `teaching_level` VARCHAR(50) DEFAULT NULL COMMENT '授课层次',
    `credit` DECIMAL(3,1) DEFAULT NULL COMMENT '学分',
    `total_hours` INT DEFAULT NULL COMMENT '总学时',
    `theory_hours` INT DEFAULT NULL COMMENT '理论学时',
    `practice_hours` INT DEFAULT NULL COMMENT '实践学时',
    `description` TEXT COMMENT '课程简介',
    `status` CHAR(1) DEFAULT '0' COMMENT '状态(0正常 1停用)',
    `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='课程信息表';

-- 3.2 课程安排表
DROP TABLE IF EXISTS `etm_schedule`;
CREATE TABLE `etm_schedule` (
    `schedule_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '安排ID',
    `schedule_no` VARCHAR(50) DEFAULT NULL COMMENT '安排编号',
    `teacher_id` BIGINT NOT NULL COMMENT '外聘老师ID',
    `course_id` BIGINT DEFAULT NULL COMMENT '课程ID',
    `college_id` BIGINT NOT NULL COMMENT '学院ID',
    `semester` VARCHAR(50) DEFAULT NULL COMMENT '学期(如:2024-2025-1)',
    `course_name` VARCHAR(100) NOT NULL COMMENT '课程名称',
    `class_name` VARCHAR(100) DEFAULT NULL COMMENT '授课班级',
    `student_count` INT DEFAULT NULL COMMENT '学生人数',
    `week_start` INT DEFAULT NULL COMMENT '起始周',
    `week_end` INT DEFAULT NULL COMMENT '结束周',
    `day_of_week` TINYINT DEFAULT NULL COMMENT '星期几(1-7)',
    `period_start` TINYINT DEFAULT NULL COMMENT '开始节次',
    `period_end` TINYINT DEFAULT NULL COMMENT '结束节次',
    `location` VARCHAR(100) DEFAULT NULL COMMENT '授课地点',
    `total_hours` INT DEFAULT NULL COMMENT '总课时',
    `completed_hours` INT DEFAULT 0 COMMENT '已完成课时',
    `syllabus_url` VARCHAR(255) DEFAULT NULL COMMENT '教学大纲地址',
    `exam_method` VARCHAR(100) DEFAULT NULL COMMENT '考核方式',
    `schedule_status` CHAR(1) DEFAULT '1' COMMENT '状态(0已取消 1进行中 2已完成)',
    `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`schedule_id`),
    KEY `idx_teacher_id` (`teacher_id`),
    KEY `idx_college_id` (`college_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='课程安排表';

-- 3.3 授课监督记录表
DROP TABLE IF EXISTS `etm_supervision`;
CREATE TABLE `etm_supervision` (
    `supervision_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `schedule_id` BIGINT NOT NULL COMMENT '课程安排ID',
    `teacher_id` BIGINT NOT NULL COMMENT '外聘老师ID',
    `supervisor_id` BIGINT NOT NULL COMMENT '监督人员ID',
    `supervisor_name` VARCHAR(50) DEFAULT NULL COMMENT '监督人员姓名',
    `supervision_date` DATE NOT NULL COMMENT '监督日期',
    `supervision_time` VARCHAR(50) DEFAULT NULL COMMENT '监督时段',
    `is_on_time` CHAR(1) DEFAULT NULL COMMENT '是否按时上课(0否 1是)',
    `late_minutes` INT DEFAULT NULL COMMENT '迟到分钟数',
    `content_score` TINYINT DEFAULT NULL COMMENT '授课内容一致性(1-5分)',
    `method_score` TINYINT DEFAULT NULL COMMENT '教学方法合理性(1-5分)',
    `atmosphere_score` TINYINT DEFAULT NULL COMMENT '课堂氛围(1-5分)',
    `feedback_score` TINYINT DEFAULT NULL COMMENT '学生反馈(1-5分)',
    `total_score` DECIMAL(5,2) DEFAULT NULL COMMENT '总评分',
    `evidence_urls` TEXT COMMENT '佐证材料URL(JSON数组)',
    `problems` TEXT COMMENT '发现问题',
    `suggestions` TEXT COMMENT '改进建议',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`supervision_id`),
    KEY `idx_schedule_id` (`schedule_id`),
    KEY `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='授课监督记录表';

-- 3.4 学生评价表
DROP TABLE IF EXISTS `etm_evaluation`;
CREATE TABLE `etm_evaluation` (
    `evaluation_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评价ID',
    `schedule_id` BIGINT NOT NULL COMMENT '课程安排ID',
    `teacher_id` BIGINT NOT NULL COMMENT '外聘老师ID',
    `student_id` BIGINT DEFAULT NULL COMMENT '学生ID',
    `evaluation_date` DATE DEFAULT NULL COMMENT '评价日期',
    `clarity_score` TINYINT DEFAULT NULL COMMENT '知识点讲解清晰度(1-5)',
    `interaction_score` TINYINT DEFAULT NULL COMMENT '课堂互动性(1-5)',
    `homework_score` TINYINT DEFAULT NULL COMMENT '作业批改及时性(1-5)',
    `attitude_score` TINYINT DEFAULT NULL COMMENT '教学态度(1-5)',
    `overall_score` TINYINT DEFAULT NULL COMMENT '总体评分(1-5)',
    `comments` TEXT COMMENT '文字评价',
    `is_anonymous` CHAR(1) DEFAULT '1' COMMENT '是否匿名(0否 1是)',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`evaluation_id`),
    KEY `idx_schedule_id` (`schedule_id`),
    KEY `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='学生评价表';

-- 3.5 调课申请表
DROP TABLE IF EXISTS `etm_schedule_change`;
CREATE TABLE `etm_schedule_change` (
    `change_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '申请ID',
    `change_no` VARCHAR(50) DEFAULT NULL COMMENT '申请编号',
    `schedule_id` BIGINT NOT NULL COMMENT '课程安排ID',
    `teacher_id` BIGINT NOT NULL COMMENT '外聘老师ID',
    `change_type` CHAR(1) NOT NULL COMMENT '变更类型(1调课 2停课)',
    `reason` TEXT NOT NULL COMMENT '申请原因',
    `original_date` DATE DEFAULT NULL COMMENT '原上课日期',
    `original_period` VARCHAR(50) DEFAULT NULL COMMENT '原上课节次',
    `new_date` DATE DEFAULT NULL COMMENT '新上课日期',
    `new_period` VARCHAR(50) DEFAULT NULL COMMENT '新上课节次',
    `new_location` VARCHAR(100) DEFAULT NULL COMMENT '新上课地点',
    `alternative_plan` TEXT COMMENT '替代方案',
    `approval_status` CHAR(1) DEFAULT '0' COMMENT '审批状态(0待审批 1已通过 2已驳回)',
    `approver_id` BIGINT DEFAULT NULL COMMENT '审批人ID',
    `approver_name` VARCHAR(50) DEFAULT NULL COMMENT '审批人姓名',
    `approval_time` DATETIME DEFAULT NULL COMMENT '审批时间',
    `approval_remark` VARCHAR(500) DEFAULT NULL COMMENT '审批意见',
    `notify_status` CHAR(1) DEFAULT '0' COMMENT '通知状态(0未通知 1已通知)',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`change_id`),
    KEY `idx_schedule_id` (`schedule_id`),
    KEY `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='调课申请表';

-- =============================================
-- 四、考核评估模块
-- =============================================

-- 4.1 考核维度配置表
DROP TABLE IF EXISTS `etm_assess_config`;
CREATE TABLE `etm_assess_config` (
    `config_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `dimension_code` VARCHAR(50) NOT NULL COMMENT '维度编码',
    `dimension_name` VARCHAR(100) NOT NULL COMMENT '维度名称',
    `parent_code` VARCHAR(50) DEFAULT NULL COMMENT '父维度编码',
    `weight` DECIMAL(5,2) NOT NULL COMMENT '权重(%)',
    `max_score` DECIMAL(5,2) DEFAULT 100 COMMENT '满分',
    `score_source` VARCHAR(50) DEFAULT NULL COMMENT '评分来源(manual/auto)',
    `calc_rule` TEXT COMMENT '计算规则说明',
    `description` TEXT COMMENT '维度说明',
    `order_num` INT DEFAULT 0 COMMENT '排序',
    `status` CHAR(1) DEFAULT '0' COMMENT '是否启用(0启用 1停用)',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`config_id`),
    UNIQUE KEY `uk_dimension_code` (`dimension_code`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='考核维度配置表';

-- 4.2 考核周期表
DROP TABLE IF EXISTS `etm_assess_period`;
CREATE TABLE `etm_assess_period` (
    `period_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `period_name` VARCHAR(100) NOT NULL COMMENT '考核周期名称',
    `period_type` CHAR(1) DEFAULT '1' COMMENT '周期类型(1学期 2学年)',
    `start_date` DATE NOT NULL COMMENT '开始日期',
    `end_date` DATE NOT NULL COMMENT '结束日期',
    `assess_start_date` DATE DEFAULT NULL COMMENT '考核开始日期',
    `assess_end_date` DATE DEFAULT NULL COMMENT '考核截止日期',
    `status` CHAR(1) DEFAULT '0' COMMENT '状态(0未开始 1进行中 2已结束)',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`period_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='考核周期表';

-- 4.3 考核记录表
DROP TABLE IF EXISTS `etm_assess_record`;
CREATE TABLE `etm_assess_record` (
    `record_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '考核ID',
    `assess_no` VARCHAR(50) DEFAULT NULL COMMENT '考核编号',
    `teacher_id` BIGINT NOT NULL COMMENT '外聘老师ID',
    `period_id` BIGINT NOT NULL COMMENT '考核周期ID',
    `college_id` BIGINT DEFAULT NULL COMMENT '学院ID',
    `teaching_score` DECIMAL(5,2) DEFAULT NULL COMMENT '教学质量得分',
    `attendance_score` DECIMAL(5,2) DEFAULT NULL COMMENT '履职情况得分',
    `contribution_score` DECIMAL(5,2) DEFAULT NULL COMMENT '附加贡献得分',
    `total_score` DECIMAL(5,2) DEFAULT NULL COMMENT '考核总分',
    `grade` VARCHAR(20) DEFAULT NULL COMMENT '考核等级(优秀/合格/不合格)',
    `rank_num` INT DEFAULT NULL COMMENT '学院内排名',
    `strength` TEXT COMMENT '主要优点',
    `weakness` TEXT COMMENT '存在不足',
    `suggestion` TEXT COMMENT '改进建议',
    `report_url` VARCHAR(255) DEFAULT NULL COMMENT '考核报告地址',
    `assess_status` CHAR(1) DEFAULT '0' COMMENT '状态(0待考核 1考核中 2已完成)',
    `notify_status` CHAR(1) DEFAULT '0' COMMENT '结果通知状态',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`record_id`),
    UNIQUE KEY `uk_teacher_period` (`teacher_id`, `period_id`),
    KEY `idx_period_id` (`period_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='考核记录表';

-- 4.4 考核明细表
DROP TABLE IF EXISTS `etm_assess_detail`;
CREATE TABLE `etm_assess_detail` (
    `detail_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `record_id` BIGINT NOT NULL COMMENT '考核记录ID',
    `dimension_code` VARCHAR(50) NOT NULL COMMENT '维度编码',
    `dimension_name` VARCHAR(100) DEFAULT NULL COMMENT '维度名称',
    `raw_score` DECIMAL(5,2) DEFAULT NULL COMMENT '原始得分',
    `weight` DECIMAL(5,2) DEFAULT NULL COMMENT '权重',
    `weighted_score` DECIMAL(5,2) DEFAULT NULL COMMENT '加权得分',
    `score_detail` TEXT COMMENT '得分明细(JSON)',
    `deduction_reason` TEXT COMMENT '扣分原因',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`detail_id`),
    KEY `idx_record_id` (`record_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='考核明细表';

-- =============================================
-- 五、薪酬结算模块
-- =============================================

-- 5.1 薪酬规则配置表
DROP TABLE IF EXISTS `etm_salary_rule`;
CREATE TABLE `etm_salary_rule` (
    `rule_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `rule_name` VARCHAR(100) NOT NULL COMMENT '规则名称',
    `course_type` VARCHAR(50) DEFAULT NULL COMMENT '适用课程类型',
    `teaching_level` VARCHAR(50) DEFAULT NULL COMMENT '适用授课层次',
    `base_salary` DECIMAL(10,2) NOT NULL COMMENT '基础课时费(元)',
    `description` TEXT COMMENT '规则说明',
    `status` CHAR(1) DEFAULT '0' COMMENT '是否启用(0启用 1停用)',
    `effective_date` DATE DEFAULT NULL COMMENT '生效日期',
    `expire_date` DATE DEFAULT NULL COMMENT '失效日期',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`rule_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='薪酬规则配置表';

-- 5.2 职称系数配置表
DROP TABLE IF EXISTS `etm_title_coef`;
CREATE TABLE `etm_title_coef` (
    `coef_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `title_level` CHAR(1) NOT NULL COMMENT '职称等级(1初级 2中级 3副高 4正高)',
    `title_name` VARCHAR(50) NOT NULL COMMENT '职称名称',
    `coefficient` DECIMAL(4,2) NOT NULL COMMENT '系数',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`coef_id`),
    UNIQUE KEY `uk_title_level` (`title_level`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='职称系数配置表';

-- 5.3 考核系数配置表
DROP TABLE IF EXISTS `etm_assess_coef`;
CREATE TABLE `etm_assess_coef` (
    `coef_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `grade` VARCHAR(20) NOT NULL COMMENT '考核等级',
    `coefficient` DECIMAL(4,2) NOT NULL COMMENT '系数',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`coef_id`),
    UNIQUE KEY `uk_grade` (`grade`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='考核系数配置表';

-- 5.4 薪酬结算表
DROP TABLE IF EXISTS `etm_salary`;
CREATE TABLE `etm_salary` (
    `salary_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '结算ID',
    `salary_no` VARCHAR(50) NOT NULL COMMENT '结算编号',
    `teacher_id` BIGINT NOT NULL COMMENT '外聘老师ID',
    `college_id` BIGINT DEFAULT NULL COMMENT '学院ID',
    `settle_period` VARCHAR(50) NOT NULL COMMENT '结算周期(如:2024-03)',
    `period_type` CHAR(1) DEFAULT '1' COMMENT '周期类型(1月度 2学期)',
    `scheduled_hours` DECIMAL(10,2) DEFAULT 0 COMMENT '计划课时',
    `actual_hours` DECIMAL(10,2) DEFAULT 0 COMMENT '实际课时',
    `absent_hours` DECIMAL(10,2) DEFAULT 0 COMMENT '缺勤课时',
    `base_salary` DECIMAL(10,2) DEFAULT NULL COMMENT '基础课时费',
    `title_coef` DECIMAL(4,2) DEFAULT 1.00 COMMENT '职称系数',
    `assess_coef` DECIMAL(4,2) DEFAULT 1.00 COMMENT '考核系数',
    `base_amount` DECIMAL(10,2) DEFAULT 0 COMMENT '基础金额',
    `title_allowance` DECIMAL(10,2) DEFAULT 0 COMMENT '职称补贴',
    `assess_bonus` DECIMAL(10,2) DEFAULT 0 COMMENT '考核奖励',
    `other_bonus` DECIMAL(10,2) DEFAULT 0 COMMENT '其他奖励',
    `deduction_amount` DECIMAL(10,2) DEFAULT 0 COMMENT '扣款金额',
    `deduction_reason` TEXT COMMENT '扣款原因',
    `total_amount` DECIMAL(10,2) NOT NULL COMMENT '应发总额',
    `tax_amount` DECIMAL(10,2) DEFAULT 0 COMMENT '代扣税额',
    `actual_amount` DECIMAL(10,2) DEFAULT NULL COMMENT '实发金额',
    `verify_status` CHAR(1) DEFAULT '0' COMMENT '校验状态(0待校验 1学院已确认 2人事已确认)',
    `college_verifier` BIGINT DEFAULT NULL COMMENT '学院校验人',
    `college_verify_time` DATETIME DEFAULT NULL COMMENT '学院校验时间',
    `hr_verifier` BIGINT DEFAULT NULL COMMENT '人事校验人',
    `hr_verify_time` DATETIME DEFAULT NULL COMMENT '人事校验时间',
    `payment_status` CHAR(1) DEFAULT '0' COMMENT '发放状态(0待发放 1已推送财务 2已发放)',
    `payment_time` DATETIME DEFAULT NULL COMMENT '发放时间',
    `payment_voucher` VARCHAR(255) DEFAULT NULL COMMENT '发放凭证',
    `remark` TEXT COMMENT '备注',
    `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`salary_id`),
    UNIQUE KEY `uk_settlement` (`teacher_id`, `settle_period`),
    KEY `idx_settle_period` (`settle_period`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='薪酬结算表';

-- 5.5 薪酬结算明细表
DROP TABLE IF EXISTS `etm_salary_detail`;
CREATE TABLE `etm_salary_detail` (
    `detail_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `salary_id` BIGINT NOT NULL COMMENT '结算ID',
    `schedule_id` BIGINT DEFAULT NULL COMMENT '课程安排ID',
    `course_name` VARCHAR(100) DEFAULT NULL COMMENT '课程名称',
    `teaching_hours` DECIMAL(10,2) DEFAULT NULL COMMENT '授课课时',
    `unit_price` DECIMAL(10,2) DEFAULT NULL COMMENT '课时单价',
    `amount` DECIMAL(10,2) DEFAULT NULL COMMENT '金额',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`detail_id`),
    KEY `idx_salary_id` (`salary_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='薪酬结算明细表';

-- =============================================
-- 六、续聘与解聘管理模块
-- =============================================

-- 6.1 续聘申请表
DROP TABLE IF EXISTS `etm_renewal`;
CREATE TABLE `etm_renewal` (
    `renewal_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '申请ID',
    `renewal_no` VARCHAR(50) DEFAULT NULL COMMENT '申请编号',
    `teacher_id` BIGINT NOT NULL COMMENT '外聘老师ID',
    `college_id` BIGINT NOT NULL COMMENT '学院ID',
    `contract_id` BIGINT NOT NULL COMMENT '原合同ID',
    `renewal_start` DATE NOT NULL COMMENT '续聘开始日期',
    `renewal_end` DATE NOT NULL COMMENT '续聘结束日期',
    `renewal_reason` TEXT COMMENT '续聘理由',
    `course_plan` TEXT COMMENT '课程安排计划',
    `assess_report_url` VARCHAR(255) DEFAULT NULL COMMENT '历史考核报告',
    `proposed_salary` DECIMAL(10,2) DEFAULT NULL COMMENT '拟定薪酬',
    `approval_status` CHAR(1) DEFAULT '0' COMMENT '审批状态(0待审批 1已通过 2已驳回)',
    `approver_id` BIGINT DEFAULT NULL COMMENT '审批人ID',
    `approval_time` DATETIME DEFAULT NULL COMMENT '审批时间',
    `approval_remark` VARCHAR(500) DEFAULT NULL COMMENT '审批意见',
    `new_contract_id` BIGINT DEFAULT NULL COMMENT '新合同ID',
    `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`renewal_id`),
    KEY `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='续聘申请表';

-- 6.2 解聘记录表
DROP TABLE IF EXISTS `etm_termination`;
CREATE TABLE `etm_termination` (
    `termination_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `termination_no` VARCHAR(50) DEFAULT NULL COMMENT '记录编号',
    `teacher_id` BIGINT NOT NULL COMMENT '外聘老师ID',
    `contract_id` BIGINT NOT NULL COMMENT '合同ID',
    `college_id` BIGINT DEFAULT NULL COMMENT '学院ID',
    `termination_type` CHAR(1) NOT NULL COMMENT '解聘类型(1合同到期 2考核不合格 3主动离职 4其他)',
    `termination_reason` TEXT COMMENT '解聘原因',
    `apply_date` DATE DEFAULT NULL COMMENT '申请日期',
    `termination_date` DATE NOT NULL COMMENT '解聘日期',
    `notice_file` VARCHAR(255) DEFAULT NULL COMMENT '解聘通知书',
    `approval_status` CHAR(1) DEFAULT '0' COMMENT '审批状态(0待审批 1已通过 2已驳回)',
    `approver_id` BIGINT DEFAULT NULL COMMENT '审批人',
    `approval_time` DATETIME DEFAULT NULL COMMENT '审批时间',
    `approval_remark` VARCHAR(500) DEFAULT NULL COMMENT '审批意见',
    `handover_status` CHAR(1) DEFAULT '0' COMMENT '交接状态(0待交接 1交接中 2已完成)',
    `handover_content` TEXT COMMENT '交接内容',
    `handover_receiver` BIGINT DEFAULT NULL COMMENT '交接接收人',
    `handover_time` DATETIME DEFAULT NULL COMMENT '交接完成时间',
    `final_salary_id` BIGINT DEFAULT NULL COMMENT '最终薪酬结算ID',
    `settle_status` CHAR(1) DEFAULT '0' COMMENT '结算状态(0待结算 1已结算)',
    `del_flag` CHAR(1) DEFAULT '0' COMMENT '删除标志',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`termination_id`),
    KEY `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 COMMENT='解聘记录表';

-- =============================================
-- 七、初始化业务数据
-- =============================================

-- 初始化职称系数
INSERT INTO `etm_title_coef` (`title_level`, `title_name`, `coefficient`, `create_time`) VALUES
('1', '助教', 1.00, NOW()),
('2', '讲师', 1.10, NOW()),
('3', '副教授', 1.15, NOW()),
('4', '教授', 1.20, NOW());

-- 初始化考核系数
INSERT INTO `etm_assess_coef` (`grade`, `coefficient`, `create_time`) VALUES
('优秀', 1.10, NOW()),
('合格', 1.00, NOW()),
('不合格', 0.80, NOW());

-- 初始化考核维度配置
INSERT INTO `etm_assess_config` (`dimension_code`, `dimension_name`, `weight`, `score_source`, `description`, `order_num`, `status`, `create_time`) VALUES
('teaching_quality', '教学质量考核', 50.00, 'auto', '包含课堂监督评分、学生评价平均分、课程考核通过率', 1, '0', NOW()),
('attendance', '履职情况考核', 30.00, 'auto', '包含授课出勤率、调课停课次数、作业批改及时性', 2, '0', NOW()),
('contribution', '附加贡献考核', 20.00, 'manual', '包含课程建设参与、学生竞赛指导、教学改进建议', 3, '0', NOW());

-- 初始化薪酬规则
INSERT INTO `etm_salary_rule` (`rule_name`, `course_type`, `teaching_level`, `base_salary`, `description`, `status`, `create_time`) VALUES
('本科理论课标准', '理论课', '本科', 150.00, '本科理论课基础课时费150元', '0', NOW()),
('本科实践课标准', '实践课', '本科', 180.00, '本科实践课基础课时费180元', '0', NOW()),
('研究生理论课标准', '理论课', '研究生', 200.00, '研究生理论课基础课时费200元', '0', NOW()),
('研究生实践课标准', '实践课', '研究生', 250.00, '研究生实践课基础课时费250元', '0', NOW()),
('专题讲座标准', '讲座', NULL, 500.00, '专题讲座每次500元', '0', NOW());

-- 初始化测试学院
INSERT INTO `etm_college` (`college_name`, `college_code`, `contact_person`, `contact_phone`, `status`, `create_time`) VALUES
('计算机学院', 'CS', '李明', '13800000001', '0', NOW()),
('电子信息学院', 'EE', '王芳', '13800000002', '0', NOW()),
('机械工程学院', 'ME', '张强', '13800000003', '0', NOW()),
('经济管理学院', 'EM', '刘丽', '13800000004', '0', NOW());

-- =============================================
-- 八、菜单权限配置(插入到RuoYi的sys_menu表)
-- =============================================

-- 外聘老师管理系统一级菜单
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`) VALUES
('外聘管理', 0, 5, 'etm', NULL, 1, 0, 'M', '0', '0', '', 'peoples', 'admin', NOW());

SET @etm_menu_id = LAST_INSERT_ID();

-- 二级菜单 - 基础信息
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`) VALUES
('基础信息', @etm_menu_id, 1, 'base', NULL, 1, 0, 'M', '0', '0', '', 'documentation', 'admin', NOW());

SET @base_menu_id = LAST_INSERT_ID();

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`) VALUES
('学院管理', @base_menu_id, 1, 'college', 'etm/college/index', 1, 0, 'C', '0', '0', 'etm:college:list', 'tree', 'admin', NOW()),
('外聘老师', @base_menu_id, 2, 'teacher', 'etm/teacher/index', 1, 0, 'C', '0', '0', 'etm:teacher:list', 'user', 'admin', NOW()),
('岗位需求', @base_menu_id, 3, 'position', 'etm/position/index', 1, 0, 'C', '0', '0', 'etm:position:list', 'post', 'admin', NOW());

-- 二级菜单 - 招募管理
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`) VALUES
('招募管理', @etm_menu_id, 2, 'recruit', NULL, 1, 0, 'M', '0', '0', '', 'guide', 'admin', NOW());

SET @recruit_menu_id = LAST_INSERT_ID();

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`) VALUES
('招募公告', @recruit_menu_id, 1, 'notice', 'etm/recruit/notice/index', 1, 0, 'C', '0', '0', 'etm:notice:list', 'message', 'admin', NOW()),
('报名管理', @recruit_menu_id, 2, 'application', 'etm/recruit/application/index', 1, 0, 'C', '0', '0', 'etm:application:list', 'form', 'admin', NOW()),
('聘用审批', @recruit_menu_id, 3, 'approval', 'etm/recruit/approval/index', 1, 0, 'C', '0', '0', 'etm:approval:list', 'cascader', 'admin', NOW());

-- 二级菜单 - 授课管理
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`) VALUES
('授课管理', @etm_menu_id, 3, 'course', NULL, 1, 0, 'M', '0', '0', '', 'education', 'admin', NOW());

SET @course_menu_id = LAST_INSERT_ID();

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`) VALUES
('课程安排', @course_menu_id, 1, 'schedule', 'etm/course/schedule/index', 1, 0, 'C', '0', '0', 'etm:schedule:list', 'date', 'admin', NOW()),
('授课监督', @course_menu_id, 2, 'supervision', 'etm/course/supervision/index', 1, 0, 'C', '0', '0', 'etm:supervision:list', 'eye-open', 'admin', NOW()),
('学生评价', @course_menu_id, 3, 'evaluation', 'etm/course/evaluation/index', 1, 0, 'C', '0', '0', 'etm:evaluation:list', 'rate', 'admin', NOW());

-- 二级菜单 - 考核管理
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`) VALUES
('考核管理', @etm_menu_id, 4, 'assess', NULL, 1, 0, 'M', '0', '0', '', 'chart', 'admin', NOW());

SET @assess_menu_id = LAST_INSERT_ID();

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`) VALUES
('考核配置', @assess_menu_id, 1, 'config', 'etm/assess/config/index', 1, 0, 'C', '0', '0', 'etm:assessConfig:list', 'slider', 'admin', NOW()),
('考核记录', @assess_menu_id, 2, 'record', 'etm/assess/record/index', 1, 0, 'C', '0', '0', 'etm:assessRecord:list', 'skill', 'admin', NOW());

-- 二级菜单 - 薪酬管理
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`) VALUES
('薪酬管理', @etm_menu_id, 5, 'salary', NULL, 1, 0, 'M', '0', '0', '', 'money', 'admin', NOW());

SET @salary_menu_id = LAST_INSERT_ID();

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`) VALUES
('薪酬规则', @salary_menu_id, 1, 'rule', 'etm/salary/rule/index', 1, 0, 'C', '0', '0', 'etm:salaryRule:list', 'component', 'admin', NOW()),
('薪酬结算', @salary_menu_id, 2, 'settle', 'etm/salary/settle/index', 1, 0, 'C', '0', '0', 'etm:salary:list', 'list', 'admin', NOW());

-- 二级菜单 - 合同管理
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`) VALUES
('合同管理', @etm_menu_id, 6, 'contract', NULL, 1, 0, 'M', '0', '0', '', 'documentation', 'admin', NOW());

SET @contract_menu_id = LAST_INSERT_ID();

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`) VALUES
('合同列表', @contract_menu_id, 1, 'list', 'etm/contract/index', 1, 0, 'C', '0', '0', 'etm:contract:list', 'log', 'admin', NOW()),
('续聘管理', @contract_menu_id, 2, 'renewal', 'etm/contract/renewal/index', 1, 0, 'C', '0', '0', 'etm:renewal:list', 'edit', 'admin', NOW()),
('解聘管理', @contract_menu_id, 3, 'termination', 'etm/contract/termination/index', 1, 0, 'C', '0', '0', 'etm:termination:list', 'exit-fullscreen', 'admin', NOW());

-- 二级菜单 - 数据统计
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`) VALUES
('数据统计', @etm_menu_id, 7, 'statistics', 'etm/statistics/index', 1, 0, 'C', '0', '0', 'etm:statistics:view', 'dashboard', 'admin', NOW());

SELECT '业务数据库初始化完成!' AS message;
