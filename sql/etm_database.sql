-- =============================================
-- 高校外聘老师管理系统 数据库脚本
-- 数据库: MySQL 8.0+
-- 字符集: utf8mb4
-- =============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `etm_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `etm_db`;

-- =============================================
-- 一、系统管理模块
-- =============================================

-- 1.1 用户表
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像地址',
    `user_type` TINYINT DEFAULT 0 COMMENT '用户类型(0系统用户 1外聘老师 2学生)',
    `college_id` BIGINT DEFAULT NULL COMMENT '所属学院ID',
    `status` TINYINT DEFAULT 1 COMMENT '状态(0禁用 1正常)',
    `login_ip` VARCHAR(50) DEFAULT NULL COMMENT '最后登录IP',
    `login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标志(0存在 1删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB COMMENT='用户表';

-- 1.2 角色表
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码',
    `sort` INT DEFAULT 0 COMMENT '显示顺序',
    `status` TINYINT DEFAULT 1 COMMENT '状态(0禁用 1正常)',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标志',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB COMMENT='角色表';

-- 1.3 菜单权限表
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
    `menu_name` VARCHAR(50) NOT NULL COMMENT '菜单名称',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父菜单ID',
    `sort` INT DEFAULT 0 COMMENT '显示顺序',
    `path` VARCHAR(200) DEFAULT '' COMMENT '路由地址',
    `component` VARCHAR(255) DEFAULT NULL COMMENT '组件路径',
    `permission` VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
    `menu_type` CHAR(1) DEFAULT 'M' COMMENT '菜单类型(M目录 C菜单 F按钮)',
    `visible` TINYINT DEFAULT 1 COMMENT '是否显示(0隐藏 1显示)',
    `icon` VARCHAR(100) DEFAULT '#' COMMENT '菜单图标',
    `status` TINYINT DEFAULT 1 COMMENT '状态(0禁用 1正常)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='菜单权限表';

-- 1.4 用户角色关联表
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB COMMENT='用户角色关联表';

-- 1.5 角色菜单关联表
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE=InnoDB COMMENT='角色菜单关联表';

-- 1.6 数据字典类型表
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '字典ID',
    `dict_name` VARCHAR(100) NOT NULL COMMENT '字典名称',
    `dict_type` VARCHAR(100) NOT NULL COMMENT '字典类型',
    `status` TINYINT DEFAULT 1 COMMENT '状态',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dict_type` (`dict_type`)
) ENGINE=InnoDB COMMENT='字典类型表';

-- 1.7 数据字典数据表
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '字典数据ID',
    `dict_type` VARCHAR(100) NOT NULL COMMENT '字典类型',
    `dict_label` VARCHAR(100) NOT NULL COMMENT '字典标签',
    `dict_value` VARCHAR(100) NOT NULL COMMENT '字典值',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT DEFAULT 1 COMMENT '状态',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='字典数据表';

-- =============================================
-- 二、基础信息管理模块
-- =============================================

-- 2.1 二级学院表
DROP TABLE IF EXISTS `base_college`;
CREATE TABLE `base_college` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '学院ID',
    `college_name` VARCHAR(100) NOT NULL COMMENT '学院名称',
    `college_code` VARCHAR(50) DEFAULT NULL COMMENT '学院编码',
    `contact_person` VARCHAR(50) DEFAULT NULL COMMENT '对接人姓名',
    `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '对接人电话',
    `contact_email` VARCHAR(100) DEFAULT NULL COMMENT '对接人邮箱',
    `description` TEXT COMMENT '学院简介',
    `sort` INT DEFAULT 0 COMMENT '显示顺序',
    `status` TINYINT DEFAULT 1 COMMENT '状态(0禁用 1正常)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标志',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='二级学院表';

-- 2.2 外聘老师基础信息表
DROP TABLE IF EXISTS `teacher_info`;
CREATE TABLE `teacher_info` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '外聘老师ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '关联用户ID',
    `teacher_no` VARCHAR(50) DEFAULT NULL COMMENT '外聘老师编号',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `gender` TINYINT DEFAULT NULL COMMENT '性别(0女 1男)',
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
    `title_level` TINYINT DEFAULT NULL COMMENT '职称等级(1初级 2中级 3副高 4正高)',
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
    `teacher_status` TINYINT DEFAULT 0 COMMENT '状态(0待聘用 1聘用中 2待续聘 3已解聘)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标志',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_id_card` (`id_card`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`teacher_status`)
) ENGINE=InnoDB COMMENT='外聘老师基础信息表';

-- 2.3 外聘老师工作经历表
DROP TABLE IF EXISTS `teacher_work_experience`;
CREATE TABLE `teacher_work_experience` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `teacher_id` BIGINT NOT NULL COMMENT '外聘老师ID',
    `company_name` VARCHAR(100) NOT NULL COMMENT '单位名称',
    `position` VARCHAR(100) DEFAULT NULL COMMENT '担任职务',
    `start_date` DATE DEFAULT NULL COMMENT '开始时间',
    `end_date` DATE DEFAULT NULL COMMENT '结束时间',
    `work_content` TEXT COMMENT '工作内容',
    `leave_reason` VARCHAR(255) DEFAULT NULL COMMENT '离职原因',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB COMMENT='外聘老师工作经历表';

-- 2.4 外聘老师资质证书表
DROP TABLE IF EXISTS `teacher_certificate`;
CREATE TABLE `teacher_certificate` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `teacher_id` BIGINT NOT NULL COMMENT '外聘老师ID',
    `cert_type` VARCHAR(50) NOT NULL COMMENT '证书类型(学历证书/学位证书/教师资格证/职称证书/行业证书)',
    `cert_name` VARCHAR(100) NOT NULL COMMENT '证书名称',
    `cert_no` VARCHAR(100) DEFAULT NULL COMMENT '证书编号',
    `issue_org` VARCHAR(100) DEFAULT NULL COMMENT '发证机构',
    `issue_date` DATE DEFAULT NULL COMMENT '发证日期',
    `expire_date` DATE DEFAULT NULL COMMENT '有效期至',
    `cert_level` VARCHAR(50) DEFAULT NULL COMMENT '证书等级',
    `file_url` VARCHAR(255) DEFAULT NULL COMMENT '证书扫描件地址',
    `verify_status` TINYINT DEFAULT 0 COMMENT '审核状态(0待审核 1已通过 2已驳回)',
    `verify_remark` VARCHAR(255) DEFAULT NULL COMMENT '审核备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB COMMENT='外聘老师资质证书表';

-- 2.5 岗位需求表
DROP TABLE IF EXISTS `base_position`;
CREATE TABLE `base_position` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
    `college_id` BIGINT NOT NULL COMMENT '所属学院ID',
    `position_name` VARCHAR(100) NOT NULL COMMENT '岗位名称',
    `subject_category` VARCHAR(50) DEFAULT NULL COMMENT '学科门类',
    `major_direction` VARCHAR(100) DEFAULT NULL COMMENT '专业方向',
    `teaching_level` VARCHAR(50) DEFAULT NULL COMMENT '授课层次(本科/研究生)',
    `course_type` VARCHAR(50) DEFAULT NULL COMMENT '课程类型(理论课/实践课/讲座)',
    `education_requirement` VARCHAR(50) DEFAULT NULL COMMENT '学历要求',
    `title_requirement` VARCHAR(50) DEFAULT NULL COMMENT '职称要求',
    `experience_years` INT DEFAULT 0 COMMENT '工作年限要求',
    `skill_requirement` TEXT COMMENT '技能要求',
    `teaching_hours` INT DEFAULT NULL COMMENT '授课时长(学时/学期)',
    `salary_min` DECIMAL(10,2) DEFAULT NULL COMMENT '薪酬下限(元/课时)',
    `salary_max` DECIMAL(10,2) DEFAULT NULL COMMENT '薪酬上限(元/课时)',
    `required_count` INT DEFAULT 1 COMMENT '需求人数',
    `hired_count` INT DEFAULT 0 COMMENT '已聘人数',
    `description` TEXT COMMENT '岗位描述',
    `status` TINYINT DEFAULT 1 COMMENT '状态(0停用 1启用)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标志',
    PRIMARY KEY (`id`),
    KEY `idx_college_id` (`college_id`)
) ENGINE=InnoDB COMMENT='岗位需求表';

-- =============================================
-- 三、招募与聘用管理模块
-- =============================================

-- 3.1 招募公告表
DROP TABLE IF EXISTS `recruit_notice`;
CREATE TABLE `recruit_notice` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '公告ID',
    `title` VARCHAR(200) NOT NULL COMMENT '公告标题',
    `position_id` BIGINT DEFAULT NULL COMMENT '关联岗位ID',
    `college_id` BIGINT DEFAULT NULL COMMENT '发布学院ID',
    `content` TEXT COMMENT '公告内容',
    `requirements` TEXT COMMENT '报名要求',
    `materials` TEXT COMMENT '所需材料说明',
    `apply_start_time` DATETIME DEFAULT NULL COMMENT '报名开始时间',
    `apply_end_time` DATETIME DEFAULT NULL COMMENT '报名截止时间',
    `publish_channel` VARCHAR(255) DEFAULT NULL COMMENT '发布渠道',
    `contact_person` VARCHAR(50) DEFAULT NULL COMMENT '联系人',
    `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `view_count` INT DEFAULT 0 COMMENT '浏览次数',
    `apply_count` INT DEFAULT 0 COMMENT '报名人数',
    `status` TINYINT DEFAULT 0 COMMENT '状态(0草稿 1已发布 2已结束 3已取消)',
    `publish_time` DATETIME DEFAULT NULL COMMENT '发布时间',
    `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '删除标志',
    PRIMARY KEY (`id`),
    KEY `idx_position_id` (`position_id`),
    KEY `idx_college_id` (`college_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB COMMENT='招募公告表';

-- 3.2 报名申请表
DROP TABLE IF EXISTS `recruit_application`;
CREATE TABLE `recruit_application` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '申请ID',
    `application_no` VARCHAR(50) DEFAULT NULL COMMENT '申请编号',
    `notice_id` BIGINT NOT NULL COMMENT '公告ID',
    `teacher_id` BIGINT NOT NULL COMMENT '外聘老师ID',
    `apply_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
    `self_introduction` TEXT COMMENT '自我介绍',
    `match_score` DECIMAL(5,2) DEFAULT NULL COMMENT '系统匹配度评分',
    -- 首轮筛选(系统自动)
    `first_review_status` TINYINT DEFAULT 0 COMMENT '首轮状态(0待筛选 1通过 2不通过)',
    `first_review_time` DATETIME DEFAULT NULL COMMENT '首轮筛选时间',
    `first_review_remark` VARCHAR(500) DEFAULT NULL COMMENT '首轮筛选备注',
    -- 二轮筛选(学院初审)
    `second_review_status` TINYINT DEFAULT 0 COMMENT '二轮状态(0待审核 1通过 2不通过)',
    `second_reviewer_id` BIGINT DEFAULT NULL COMMENT '二轮审核人ID',
    `second_review_time` DATETIME DEFAULT NULL COMMENT '二轮审核时间',
    `second_review_remark` VARCHAR(500) DEFAULT NULL COMMENT '二轮审核备注',
    -- 终轮筛选(面试评估)
    `interview_time` DATETIME DEFAULT NULL COMMENT '面试时间',
    `interview_location` VARCHAR(200) DEFAULT NULL COMMENT '面试地点',
    `interview_score` DECIMAL(5,2) DEFAULT NULL COMMENT '面试总分',
    `interview_remark` TEXT COMMENT '面试评语',
    `final_status` TINYINT DEFAULT 0 COMMENT '最终状态(0进行中 1录用 2不录用 3放弃)',
    `final_time` DATETIME DEFAULT NULL COMMENT '最终确定时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_notice_teacher` (`notice_id`, `teacher_id`),
    KEY `idx_teacher_id` (`teacher_id`),
    KEY `idx_final_status` (`final_status`)
) ENGINE=InnoDB COMMENT='报名申请表';

-- 3.3 面试评分表
DROP TABLE IF EXISTS `recruit_interview_score`;
CREATE TABLE `recruit_interview_score` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `application_id` BIGINT NOT NULL COMMENT '申请ID',
    `interviewer_id` BIGINT NOT NULL COMMENT '面试官ID',
    `interviewer_name` VARCHAR(50) DEFAULT NULL COMMENT '面试官姓名',
    `teaching_concept` DECIMAL(5,2) DEFAULT NULL COMMENT '教学理念(满分20)',
    `professional_ability` DECIMAL(5,2) DEFAULT NULL COMMENT '专业能力(满分30)',
    `expression_ability` DECIMAL(5,2) DEFAULT NULL COMMENT '表达能力(满分20)',
    `practical_experience` DECIMAL(5,2) DEFAULT NULL COMMENT '实践经验(满分20)',
    `overall_impression` DECIMAL(5,2) DEFAULT NULL COMMENT '综合印象(满分10)',
    `total_score` DECIMAL(5,2) DEFAULT NULL COMMENT '总分',
    `evaluation` TEXT COMMENT '综合评价',
    `is_recommend` TINYINT DEFAULT NULL COMMENT '是否推荐录用(0否 1是)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_application_id` (`application_id`)
) ENGINE=InnoDB COMMENT='面试评分表';

-- 3.4 聘用审批表
DROP TABLE IF EXISTS `employ_approval`;
CREATE TABLE `employ_approval` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '审批ID',
    `approval_no` VARCHAR(50) DEFAULT NULL COMMENT '审批编号',
    `application_id` BIGINT NOT NULL COMMENT '报名申请ID',
    `teacher_id` BIGINT NOT NULL COMMENT '外聘老师ID',
    `college_id` BIGINT NOT NULL COMMENT '学院ID',
    `position_id` BIGINT DEFAULT NULL COMMENT '岗位ID',
    `proposed_salary` DECIMAL(10,2) DEFAULT NULL COMMENT '拟定课时薪酬',
    `employ_start_date` DATE DEFAULT NULL COMMENT '拟聘开始日期',
    `employ_end_date` DATE DEFAULT NULL COMMENT '拟聘结束日期',
    `apply_reason` TEXT COMMENT '申请理由',
    -- 学院负责人审批
    `college_leader_status` TINYINT DEFAULT 0 COMMENT '学院负责人审批(0待审批 1通过 2驳回)',
    `college_leader_id` BIGINT DEFAULT NULL COMMENT '学院负责人ID',
    `college_leader_time` DATETIME DEFAULT NULL COMMENT '审批时间',
    `college_leader_remark` VARCHAR(500) DEFAULT NULL COMMENT '审批意见',
    -- 人事处薪酬岗审批
    `hr_salary_status` TINYINT DEFAULT 0 COMMENT '薪酬岗审批(0待审批 1通过 2驳回)',
    `hr_salary_id` BIGINT DEFAULT NULL COMMENT '薪酬岗ID',
    `hr_salary_time` DATETIME DEFAULT NULL COMMENT '审批时间',
    `hr_salary_remark` VARCHAR(500) DEFAULT NULL COMMENT '审批意见',
    `approved_salary` DECIMAL(10,2) DEFAULT NULL COMMENT '核定课时薪酬',
    -- 人事处处长审批
    `hr_director_status` TINYINT DEFAULT 0 COMMENT '处长审批(0待审批 1通过 2驳回)',
    `hr_director_id` BIGINT DEFAULT NULL COMMENT '处长ID',
    `hr_director_time` DATETIME DEFAULT NULL COMMENT '审批时间',
    `hr_director_remark` VARCHAR(500) DEFAULT NULL COMMENT '审批意见',
    -- 整体状态
    `current_node` VARCHAR(50) DEFAULT 'college_leader' COMMENT '当前审批节点',
    `approval_status` TINYINT DEFAULT 0 COMMENT '整体状态(0审批中 1已通过 2已驳回 3已撤回)',
    `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_teacher_id` (`teacher_id`),
    KEY `idx_approval_status` (`approval_status`)
) ENGINE=InnoDB COMMENT='聘用审批表';

-- 3.5 聘用合同表
DROP TABLE IF EXISTS `employ_contract`;
CREATE TABLE `employ_contract` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '合同ID',
    `contract_no` VARCHAR(50) NOT NULL COMMENT '合同编号',
    `teacher_id` BIGINT NOT NULL COMMENT '外聘老师ID',
    `college_id` BIGINT NOT NULL COMMENT '学院ID',
    `approval_id` BIGINT DEFAULT NULL COMMENT '审批ID',
    `contract_type` TINYINT DEFAULT 1 COMMENT '合同类型(1首次聘用 2续聘)',
    `start_date` DATE NOT NULL COMMENT '合同开始日期',
    `end_date` DATE NOT NULL COMMENT '合同结束日期',
    `salary_standard` DECIMAL(10,2) DEFAULT NULL COMMENT '课时薪酬标准',
    `work_content` TEXT COMMENT '工作内容',
    `contract_terms` TEXT COMMENT '合同条款',
    `contract_file_url` VARCHAR(255) DEFAULT NULL COMMENT '合同文件地址',
    `sign_status` TINYINT DEFAULT 0 COMMENT '签署状态(0待签署 1学校已签 2双方已签)',
    `teacher_sign_time` DATETIME DEFAULT NULL COMMENT '老师签署时间',
    `school_sign_time` DATETIME DEFAULT NULL COMMENT '学校签署时间',
    `contract_status` TINYINT DEFAULT 1 COMMENT '合同状态(0已作废 1生效中 2已到期 3已终止)',
    `terminate_reason` VARCHAR(500) DEFAULT NULL COMMENT '终止原因',
    `terminate_time` DATETIME DEFAULT NULL COMMENT '终止时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_contract_no` (`contract_no`),
    KEY `idx_teacher_id` (`teacher_id`),
    KEY `idx_contract_status` (`contract_status`)
) ENGINE=InnoDB COMMENT='聘用合同表';

-- =============================================
-- 四、授课管理模块
-- =============================================

-- 4.1 课程信息表
DROP TABLE IF EXISTS `course_info`;
CREATE TABLE `course_info` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '课程ID',
    `course_code` VARCHAR(50) DEFAULT NULL COMMENT '课程代码',
    `course_name` VARCHAR(100) NOT NULL COMMENT '课程名称',
    `college_id` BIGINT DEFAULT NULL COMMENT '开课学院ID',
    `course_type` VARCHAR(50) DEFAULT NULL COMMENT '课程类型(理论课/实践课/讲座)',
    `teaching_level` VARCHAR(50) DEFAULT NULL COMMENT '授课层次(本科/研究生)',
    `credit` DECIMAL(3,1) DEFAULT NULL COMMENT '学分',
    `total_hours` INT DEFAULT NULL COMMENT '总学时',
    `theory_hours` INT DEFAULT NULL COMMENT '理论学时',
    `practice_hours` INT DEFAULT NULL COMMENT '实践学时',
    `description` TEXT COMMENT '课程简介',
    `status` TINYINT DEFAULT 1 COMMENT '状态(0停用 1启用)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_college_id` (`college_id`)
) ENGINE=InnoDB COMMENT='课程信息表';

-- 4.2 课程安排表
DROP TABLE IF EXISTS `course_schedule`;
CREATE TABLE `course_schedule` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '安排ID',
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
    `schedule_status` TINYINT DEFAULT 1 COMMENT '状态(0已取消 1进行中 2已完成)',
    `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_teacher_id` (`teacher_id`),
    KEY `idx_college_id` (`college_id`),
    KEY `idx_semester` (`semester`)
) ENGINE=InnoDB COMMENT='课程安排表';

-- 4.3 授课监督记录表
DROP TABLE IF EXISTS `course_supervision`;
CREATE TABLE `course_supervision` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `schedule_id` BIGINT NOT NULL COMMENT '课程安排ID',
    `teacher_id` BIGINT NOT NULL COMMENT '外聘老师ID',
    `supervisor_id` BIGINT NOT NULL COMMENT '监督人员ID',
    `supervisor_name` VARCHAR(50) DEFAULT NULL COMMENT '监督人员姓名',
    `supervision_date` DATE NOT NULL COMMENT '监督日期',
    `supervision_time` VARCHAR(50) DEFAULT NULL COMMENT '监督时段',
    `is_on_time` TINYINT DEFAULT NULL COMMENT '是否按时上课(0否 1是)',
    `late_minutes` INT DEFAULT NULL COMMENT '迟到分钟数',
    `content_consistency` TINYINT DEFAULT NULL COMMENT '授课内容与大纲一致性(1-5分)',
    `teaching_method` TINYINT DEFAULT NULL COMMENT '教学方法合理性(1-5分)',
    `classroom_atmosphere` TINYINT DEFAULT NULL COMMENT '课堂氛围(1-5分)',
    `student_feedback` TINYINT DEFAULT NULL COMMENT '学生反馈(1-5分)',
    `total_score` DECIMAL(5,2) DEFAULT NULL COMMENT '总评分',
    `evidence_urls` TEXT COMMENT '佐证材料(照片/视频URL,JSON数组)',
    `problems` TEXT COMMENT '发现问题',
    `suggestions` TEXT COMMENT '改进建议',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_schedule_id` (`schedule_id`),
    KEY `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB COMMENT='授课监督记录表';

-- 4.4 学生评价表
DROP TABLE IF EXISTS `course_evaluation`;
CREATE TABLE `course_evaluation` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评价ID',
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
    `is_anonymous` TINYINT DEFAULT 1 COMMENT '是否匿名(0否 1是)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_schedule_id` (`schedule_id`),
    KEY `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB COMMENT='学生评价表';

-- 4.5 调课申请表
DROP TABLE IF EXISTS `course_change_request`;
CREATE TABLE `course_change_request` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '申请ID',
    `request_no` VARCHAR(50) DEFAULT NULL COMMENT '申请编号',
    `schedule_id` BIGINT NOT NULL COMMENT '课程安排ID',
    `teacher_id` BIGINT NOT NULL COMMENT '外聘老师ID',
    `change_type` TINYINT NOT NULL COMMENT '变更类型(1调课 2停课)',
    `reason` TEXT NOT NULL COMMENT '申请原因',
    `original_date` DATE DEFAULT NULL COMMENT '原上课日期',
    `original_period` VARCHAR(50) DEFAULT NULL COMMENT '原上课节次',
    `new_date` DATE DEFAULT NULL COMMENT '新上课日期',
    `new_period` VARCHAR(50) DEFAULT NULL COMMENT '新上课节次',
    `new_location` VARCHAR(100) DEFAULT NULL COMMENT '新上课地点',
    `alternative_plan` TEXT COMMENT '替代方案',
    `approval_status` TINYINT DEFAULT 0 COMMENT '审批状态(0待审批 1已通过 2已驳回)',
    `approver_id` BIGINT DEFAULT NULL COMMENT '审批人ID',
    `approver_name` VARCHAR(50) DEFAULT NULL COMMENT '审批人姓名',
    `approval_time` DATETIME DEFAULT NULL COMMENT '审批时间',
    `approval_remark` VARCHAR(500) DEFAULT NULL COMMENT '审批意见',
    `notify_status` TINYINT DEFAULT 0 COMMENT '通知状态(0未通知 1已通知)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_schedule_id` (`schedule_id`),
    KEY `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB COMMENT='调课申请表';

-- =============================================
-- 五、考核评估模块
-- =============================================

-- 5.1 考核维度配置表
DROP TABLE IF EXISTS `assess_dimension_config`;
CREATE TABLE `assess_dimension_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `dimension_code` VARCHAR(50) NOT NULL COMMENT '维度编码',
    `dimension_name` VARCHAR(100) NOT NULL COMMENT '维度名称',
    `parent_code` VARCHAR(50) DEFAULT NULL COMMENT '父维度编码',
    `weight` DECIMAL(5,2) NOT NULL COMMENT '权重(%)',
    `max_score` DECIMAL(5,2) DEFAULT 100 COMMENT '满分',
    `score_source` VARCHAR(50) DEFAULT NULL COMMENT '评分来源(manual/auto)',
    `calculation_rule` TEXT COMMENT '计算规则说明',
    `description` TEXT COMMENT '维度说明',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `is_active` TINYINT DEFAULT 1 COMMENT '是否启用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dimension_code` (`dimension_code`)
) ENGINE=InnoDB COMMENT='考核维度配置表';

-- 5.2 考核周期表
DROP TABLE IF EXISTS `assess_period`;
CREATE TABLE `assess_period` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `period_name` VARCHAR(100) NOT NULL COMMENT '考核周期名称',
    `period_type` TINYINT DEFAULT 1 COMMENT '周期类型(1学期 2学年)',
    `start_date` DATE NOT NULL COMMENT '开始日期',
    `end_date` DATE NOT NULL COMMENT '结束日期',
    `assess_start_date` DATE DEFAULT NULL COMMENT '考核开始日期',
    `assess_end_date` DATE DEFAULT NULL COMMENT '考核截止日期',
    `status` TINYINT DEFAULT 0 COMMENT '状态(0未开始 1进行中 2已结束)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='考核周期表';

-- 5.3 考核记录表
DROP TABLE IF EXISTS `assess_record`;
CREATE TABLE `assess_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '考核ID',
    `assess_no` VARCHAR(50) DEFAULT NULL COMMENT '考核编号',
    `teacher_id` BIGINT NOT NULL COMMENT '外聘老师ID',
    `period_id` BIGINT NOT NULL COMMENT '考核周期ID',
    `college_id` BIGINT DEFAULT NULL COMMENT '学院ID',
    -- 各维度得分
    `teaching_quality_score` DECIMAL(5,2) DEFAULT NULL COMMENT '教学质量得分',
    `attendance_score` DECIMAL(5,2) DEFAULT NULL COMMENT '履职情况得分',
    `contribution_score` DECIMAL(5,2) DEFAULT NULL COMMENT '附加贡献得分',
    `total_score` DECIMAL(5,2) DEFAULT NULL COMMENT '考核总分',
    `grade` VARCHAR(20) DEFAULT NULL COMMENT '考核等级(优秀/合格/不合格)',
    `rank_in_college` INT DEFAULT NULL COMMENT '学院内排名',
    -- 评价与建议
    `strength` TEXT COMMENT '主要优点',
    `weakness` TEXT COMMENT '存在不足',
    `suggestion` TEXT COMMENT '改进建议',
    `report_url` VARCHAR(255) DEFAULT NULL COMMENT '考核报告地址',
    -- 状态
    `assess_status` TINYINT DEFAULT 0 COMMENT '状态(0待考核 1考核中 2已完成)',
    `result_notify_status` TINYINT DEFAULT 0 COMMENT '结果通知状态',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_teacher_period` (`teacher_id`, `period_id`),
    KEY `idx_period_id` (`period_id`),
    KEY `idx_grade` (`grade`)
) ENGINE=InnoDB COMMENT='考核记录表';

-- 5.4 考核明细表
DROP TABLE IF EXISTS `assess_detail`;
CREATE TABLE `assess_detail` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `record_id` BIGINT NOT NULL COMMENT '考核记录ID',
    `dimension_code` VARCHAR(50) NOT NULL COMMENT '维度编码',
    `dimension_name` VARCHAR(100) DEFAULT NULL COMMENT '维度名称',
    `raw_score` DECIMAL(5,2) DEFAULT NULL COMMENT '原始得分',
    `weight` DECIMAL(5,2) DEFAULT NULL COMMENT '权重',
    `weighted_score` DECIMAL(5,2) DEFAULT NULL COMMENT '加权得分',
    `score_detail` TEXT COMMENT '得分明细(JSON)',
    `deduction_reason` TEXT COMMENT '扣分原因',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_record_id` (`record_id`)
) ENGINE=InnoDB COMMENT='考核明细表';

-- 5.5 考核整改通知表
DROP TABLE IF EXISTS `assess_rectification`;
CREATE TABLE `assess_rectification` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `record_id` BIGINT NOT NULL COMMENT '考核记录ID',
    `teacher_id` BIGINT NOT NULL COMMENT '外聘老师ID',
    `rectification_content` TEXT NOT NULL COMMENT '整改内容',
    `deadline` DATE NOT NULL COMMENT '整改期限',
    `notify_time` DATETIME DEFAULT NULL COMMENT '通知时间',
    `rectification_status` TINYINT DEFAULT 0 COMMENT '整改状态(0待整改 1已整改 2整改不合格)',
    `rectification_report` TEXT COMMENT '整改报告',
    `review_result` TEXT COMMENT '复查结果',
    `reviewer_id` BIGINT DEFAULT NULL COMMENT '复查人ID',
    `review_time` DATETIME DEFAULT NULL COMMENT '复查时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_record_id` (`record_id`),
    KEY `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB COMMENT='考核整改通知表';

-- =============================================
-- 六、薪酬结算模块
-- =============================================

-- 6.1 薪酬规则配置表
DROP TABLE IF EXISTS `salary_rule`;
CREATE TABLE `salary_rule` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `rule_name` VARCHAR(100) NOT NULL COMMENT '规则名称',
    `course_type` VARCHAR(50) DEFAULT NULL COMMENT '适用课程类型',
    `teaching_level` VARCHAR(50) DEFAULT NULL COMMENT '适用授课层次',
    `base_salary` DECIMAL(10,2) NOT NULL COMMENT '基础课时费(元)',
    `description` TEXT COMMENT '规则说明',
    `is_active` TINYINT DEFAULT 1 COMMENT '是否启用',
    `effective_date` DATE DEFAULT NULL COMMENT '生效日期',
    `expire_date` DATE DEFAULT NULL COMMENT '失效日期',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='薪酬规则配置表';

-- 6.2 职称系数配置表
DROP TABLE IF EXISTS `salary_title_coefficient`;
CREATE TABLE `salary_title_coefficient` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `title_level` TINYINT NOT NULL COMMENT '职称等级(1初级 2中级 3副高 4正高)',
    `title_name` VARCHAR(50) NOT NULL COMMENT '职称名称',
    `coefficient` DECIMAL(4,2) NOT NULL COMMENT '系数',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_title_level` (`title_level`)
) ENGINE=InnoDB COMMENT='职称系数配置表';

-- 6.3 考核系数配置表
DROP TABLE IF EXISTS `salary_assess_coefficient`;
CREATE TABLE `salary_assess_coefficient` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `grade` VARCHAR(20) NOT NULL COMMENT '考核等级',
    `coefficient` DECIMAL(4,2) NOT NULL COMMENT '系数',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_grade` (`grade`)
) ENGINE=InnoDB COMMENT='考核系数配置表';

-- 6.4 薪酬结算表
DROP TABLE IF EXISTS `salary_settlement`;
CREATE TABLE `salary_settlement` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '结算ID',
    `settlement_no` VARCHAR(50) NOT NULL COMMENT '结算编号',
    `teacher_id` BIGINT NOT NULL COMMENT '外聘老师ID',
    `college_id` BIGINT DEFAULT NULL COMMENT '学院ID',
    `settlement_period` VARCHAR(50) NOT NULL COMMENT '结算周期(如:2024-09)',
    `period_type` TINYINT DEFAULT 1 COMMENT '周期类型(1月度 2学期)',
    -- 课时相关
    `scheduled_hours` DECIMAL(10,2) DEFAULT 0 COMMENT '计划课时',
    `actual_hours` DECIMAL(10,2) DEFAULT 0 COMMENT '实际课时',
    `absent_hours` DECIMAL(10,2) DEFAULT 0 COMMENT '缺勤课时',
    -- 薪酬计算
    `base_salary` DECIMAL(10,2) DEFAULT NULL COMMENT '基础课时费',
    `title_coefficient` DECIMAL(4,2) DEFAULT 1.00 COMMENT '职称系数',
    `assess_coefficient` DECIMAL(4,2) DEFAULT 1.00 COMMENT '考核系数',
    `base_amount` DECIMAL(10,2) DEFAULT 0 COMMENT '基础金额',
    `title_allowance` DECIMAL(10,2) DEFAULT 0 COMMENT '职称补贴',
    `assess_bonus` DECIMAL(10,2) DEFAULT 0 COMMENT '考核奖励',
    `other_bonus` DECIMAL(10,2) DEFAULT 0 COMMENT '其他奖励',
    `deduction_amount` DECIMAL(10,2) DEFAULT 0 COMMENT '扣款金额',
    `deduction_reason` TEXT COMMENT '扣款原因',
    `total_amount` DECIMAL(10,2) NOT NULL COMMENT '应发总额',
    `tax_amount` DECIMAL(10,2) DEFAULT 0 COMMENT '代扣税额',
    `actual_amount` DECIMAL(10,2) DEFAULT NULL COMMENT '实发金额',
    -- 状态
    `verify_status` TINYINT DEFAULT 0 COMMENT '校验状态(0待校验 1学院已确认 2人事已确认)',
    `college_verifier_id` BIGINT DEFAULT NULL COMMENT '学院校验人',
    `college_verify_time` DATETIME DEFAULT NULL COMMENT '学院校验时间',
    `hr_verifier_id` BIGINT DEFAULT NULL COMMENT '人事校验人',
    `hr_verify_time` DATETIME DEFAULT NULL COMMENT '人事校验时间',
    `payment_status` TINYINT DEFAULT 0 COMMENT '发放状态(0待发放 1已推送财务 2已发放)',
    `payment_time` DATETIME DEFAULT NULL COMMENT '发放时间',
    `payment_voucher` VARCHAR(255) DEFAULT NULL COMMENT '发放凭证',
    `remark` TEXT COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_settlement` (`teacher_id`, `settlement_period`),
    KEY `idx_settlement_period` (`settlement_period`),
    KEY `idx_payment_status` (`payment_status`)
) ENGINE=InnoDB COMMENT='薪酬结算表';

-- 6.5 薪酬结算明细表
DROP TABLE IF EXISTS `salary_settlement_detail`;
CREATE TABLE `salary_settlement_detail` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `settlement_id` BIGINT NOT NULL COMMENT '结算ID',
    `schedule_id` BIGINT DEFAULT NULL COMMENT '课程安排ID',
    `course_name` VARCHAR(100) DEFAULT NULL COMMENT '课程名称',
    `teaching_hours` DECIMAL(10,2) DEFAULT NULL COMMENT '授课课时',
    `unit_price` DECIMAL(10,2) DEFAULT NULL COMMENT '课时单价',
    `amount` DECIMAL(10,2) DEFAULT NULL COMMENT '金额',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_settlement_id` (`settlement_id`)
) ENGINE=InnoDB COMMENT='薪酬结算明细表';

-- =============================================
-- 七、续聘与解聘管理模块
-- =============================================

-- 7.1 续聘申请表
DROP TABLE IF EXISTS `renewal_application`;
CREATE TABLE `renewal_application` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '申请ID',
    `application_no` VARCHAR(50) DEFAULT NULL COMMENT '申请编号',
    `teacher_id` BIGINT NOT NULL COMMENT '外聘老师ID',
    `college_id` BIGINT NOT NULL COMMENT '学院ID',
    `contract_id` BIGINT NOT NULL COMMENT '原合同ID',
    `renewal_start_date` DATE NOT NULL COMMENT '续聘开始日期',
    `renewal_end_date` DATE NOT NULL COMMENT '续聘结束日期',
    `renewal_reason` TEXT COMMENT '续聘理由',
    `course_plan` TEXT COMMENT '课程安排计划',
    `assess_report_url` VARCHAR(255) DEFAULT NULL COMMENT '历史考核报告',
    `proposed_salary` DECIMAL(10,2) DEFAULT NULL COMMENT '拟定薪酬',
    -- 审批流程
    `approval_status` TINYINT DEFAULT 0 COMMENT '审批状态(0待审批 1已通过 2已驳回)',
    `approver_id` BIGINT DEFAULT NULL COMMENT '审批人ID',
    `approval_time` DATETIME DEFAULT NULL COMMENT '审批时间',
    `approval_remark` VARCHAR(500) DEFAULT NULL COMMENT '审批意见',
    `new_contract_id` BIGINT DEFAULT NULL COMMENT '新合同ID',
    `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_teacher_id` (`teacher_id`),
    KEY `idx_contract_id` (`contract_id`)
) ENGINE=InnoDB COMMENT='续聘申请表';

-- 7.2 解聘记录表
DROP TABLE IF EXISTS `termination_record`;
CREATE TABLE `termination_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `record_no` VARCHAR(50) DEFAULT NULL COMMENT '记录编号',
    `teacher_id` BIGINT NOT NULL COMMENT '外聘老师ID',
    `contract_id` BIGINT NOT NULL COMMENT '合同ID',
    `college_id` BIGINT DEFAULT NULL COMMENT '学院ID',
    `termination_type` TINYINT NOT NULL COMMENT '解聘类型(1合同到期 2考核不合格 3主动离职 4其他)',
    `termination_reason` TEXT COMMENT '解聘原因',
    `apply_date` DATE DEFAULT NULL COMMENT '申请日期',
    `termination_date` DATE NOT NULL COMMENT '解聘日期',
    `notice_file_url` VARCHAR(255) DEFAULT NULL COMMENT '解聘通知书',
    -- 审批
    `approval_status` TINYINT DEFAULT 0 COMMENT '审批状态(0待审批 1已通过 2已驳回)',
    `approver_id` BIGINT DEFAULT NULL COMMENT '审批人',
    `approval_time` DATETIME DEFAULT NULL COMMENT '审批时间',
    `approval_remark` VARCHAR(500) DEFAULT NULL COMMENT '审批意见',
    -- 交接
    `handover_status` TINYINT DEFAULT 0 COMMENT '交接状态(0待交接 1交接中 2已完成)',
    `handover_content` TEXT COMMENT '交接内容',
    `handover_receiver_id` BIGINT DEFAULT NULL COMMENT '交接接收人',
    `handover_complete_time` DATETIME DEFAULT NULL COMMENT '交接完成时间',
    -- 薪酬结算
    `final_settlement_id` BIGINT DEFAULT NULL COMMENT '最终薪酬结算ID',
    `settlement_status` TINYINT DEFAULT 0 COMMENT '结算状态(0待结算 1已结算)',
    `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_teacher_id` (`teacher_id`),
    KEY `idx_contract_id` (`contract_id`)
) ENGINE=InnoDB COMMENT='解聘记录表';

-- 7.3 交接材料清单表
DROP TABLE IF EXISTS `handover_material`;
CREATE TABLE `handover_material` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `termination_id` BIGINT NOT NULL COMMENT '解聘记录ID',
    `material_name` VARCHAR(100) NOT NULL COMMENT '材料名称',
    `material_type` VARCHAR(50) DEFAULT NULL COMMENT '材料类型',
    `is_submitted` TINYINT DEFAULT 0 COMMENT '是否已提交(0否 1是)',
    `submit_time` DATETIME DEFAULT NULL COMMENT '提交时间',
    `file_url` VARCHAR(255) DEFAULT NULL COMMENT '文件地址',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_termination_id` (`termination_id`)
) ENGINE=InnoDB COMMENT='交接材料清单表';

-- =============================================
-- 八、消息与档案管理模块
-- =============================================

-- 8.1 消息通知表
DROP TABLE IF EXISTS `sys_notification`;
CREATE TABLE `sys_notification` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '消息ID',
    `user_id` BIGINT NOT NULL COMMENT '接收用户ID',
    `title` VARCHAR(200) NOT NULL COMMENT '消息标题',
    `content` TEXT COMMENT '消息内容',
    `msg_type` VARCHAR(50) DEFAULT NULL COMMENT '消息类型(system/approval/remind/notice)',
    `biz_type` VARCHAR(50) DEFAULT NULL COMMENT '业务类型',
    `biz_id` BIGINT DEFAULT NULL COMMENT '关联业务ID',
    `is_read` TINYINT DEFAULT 0 COMMENT '是否已读(0未读 1已读)',
    `read_time` DATETIME DEFAULT NULL COMMENT '阅读时间',
    `send_channel` VARCHAR(50) DEFAULT 'site' COMMENT '发送渠道(site站内信/email邮件/sms短信)',
    `send_status` TINYINT DEFAULT 1 COMMENT '发送状态(0失败 1成功)',
    `send_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_is_read` (`is_read`),
    KEY `idx_msg_type` (`msg_type`)
) ENGINE=InnoDB COMMENT='消息通知表';

-- 8.2 消息模板表
DROP TABLE IF EXISTS `sys_msg_template`;
CREATE TABLE `sys_msg_template` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '模板ID',
    `template_code` VARCHAR(50) NOT NULL COMMENT '模板编码',
    `template_name` VARCHAR(100) NOT NULL COMMENT '模板名称',
    `msg_type` VARCHAR(50) DEFAULT NULL COMMENT '消息类型',
    `title_template` VARCHAR(200) DEFAULT NULL COMMENT '标题模板',
    `content_template` TEXT COMMENT '内容模板',
    `send_channel` VARCHAR(50) DEFAULT 'site' COMMENT '发送渠道',
    `is_active` TINYINT DEFAULT 1 COMMENT '是否启用',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_template_code` (`template_code`)
) ENGINE=InnoDB COMMENT='消息模板表';

-- 8.3 档案操作日志表
DROP TABLE IF EXISTS `archive_operation_log`;
CREATE TABLE `archive_operation_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `archive_type` VARCHAR(50) NOT NULL COMMENT '档案类型(teacher/contract/salary/assess)',
    `archive_id` BIGINT NOT NULL COMMENT '档案ID',
    `archive_name` VARCHAR(200) DEFAULT NULL COMMENT '档案名称',
    `operation_type` VARCHAR(50) NOT NULL COMMENT '操作类型(view/download/print/edit/export)',
    `operator_id` BIGINT NOT NULL COMMENT '操作人ID',
    `operator_name` VARCHAR(50) DEFAULT NULL COMMENT '操作人姓名',
    `operator_dept` VARCHAR(100) DEFAULT NULL COMMENT '操作人部门',
    `operation_detail` TEXT COMMENT '操作详情',
    `ip_address` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
    `user_agent` VARCHAR(500) DEFAULT NULL COMMENT '浏览器信息',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    PRIMARY KEY (`id`),
    KEY `idx_archive` (`archive_type`, `archive_id`),
    KEY `idx_operator_id` (`operator_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB COMMENT='档案操作日志表';

-- 8.4 系统操作日志表
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `module` VARCHAR(50) DEFAULT NULL COMMENT '操作模块',
    `operation` VARCHAR(50) DEFAULT NULL COMMENT '操作类型',
    `method` VARCHAR(200) DEFAULT NULL COMMENT '请求方法',
    `request_url` VARCHAR(500) DEFAULT NULL COMMENT '请求URL',
    `request_method` VARCHAR(10) DEFAULT NULL COMMENT '请求方式',
    `request_params` TEXT COMMENT '请求参数',
    `response_result` TEXT COMMENT '返回结果',
    `operator_id` BIGINT DEFAULT NULL COMMENT '操作人ID',
    `operator_name` VARCHAR(50) DEFAULT NULL COMMENT '操作人姓名',
    `ip_address` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
    `location` VARCHAR(100) DEFAULT NULL COMMENT '操作地点',
    `status` TINYINT DEFAULT 1 COMMENT '状态(0失败 1成功)',
    `error_msg` TEXT COMMENT '错误信息',
    `execute_time` BIGINT DEFAULT NULL COMMENT '执行时长(ms)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    PRIMARY KEY (`id`),
    KEY `idx_operator_id` (`operator_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB COMMENT='系统操作日志表';

-- =============================================
-- 九、初始化数据
-- =============================================

-- 9.1 初始化角色
INSERT INTO `sys_role` (`role_name`, `role_code`, `sort`, `status`, `remark`) VALUES
('超级管理员', 'admin', 1, 1, '系统超级管理员，拥有所有权限'),
('人事处处长', 'hr_director', 2, 1, '人事处处长，负责最终审批'),
('人事处薪酬岗', 'hr_salary', 3, 1, '人事处薪酬岗，负责薪酬审核'),
('人事处普通员工', 'hr_staff', 4, 1, '人事处普通员工'),
('学院管理员', 'college_admin', 5, 1, '二级学院管理员'),
('学院教学负责人', 'college_leader', 6, 1, '学院教学负责人，负责审批'),
('教学监督员', 'supervisor', 7, 1, '负责授课监督'),
('外聘老师', 'teacher', 8, 1, '外聘老师角色'),
('学生', 'student', 9, 1, '学生角色，仅用于评价');

-- 9.2 初始化管理员用户(密码: admin123)
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `user_type`, `status`) VALUES
('admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '系统管理员', 0, 1);

-- 9.3 初始化用户角色关联
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (1, 1);

-- 9.4 初始化数据字典类型
INSERT INTO `sys_dict_type` (`dict_name`, `dict_type`, `status`, `remark`) VALUES
('性别', 'sys_gender', 1, '用户性别'),
('用户状态', 'sys_user_status', 1, '用户状态列表'),
('教师状态', 'teacher_status', 1, '外聘教师状态'),
('学历', 'education', 1, '学历类型'),
('学位', 'degree', 1, '学位类型'),
('职称等级', 'title_level', 1, '职称等级'),
('课程类型', 'course_type', 1, '课程类型'),
('授课层次', 'teaching_level', 1, '授课层次'),
('考核等级', 'assess_grade', 1, '考核等级'),
('审批状态', 'approval_status', 1, '审批状态'),
('合同状态', 'contract_status', 1, '合同状态'),
('解聘类型', 'termination_type', 1, '解聘类型');

-- 9.5 初始化数据字典数据
INSERT INTO `sys_dict_data` (`dict_type`, `dict_label`, `dict_value`, `sort`) VALUES
-- 性别
('sys_gender', '女', '0', 1),
('sys_gender', '男', '1', 2),
-- 用户状态
('sys_user_status', '禁用', '0', 1),
('sys_user_status', '正常', '1', 2),
-- 教师状态
('teacher_status', '待聘用', '0', 1),
('teacher_status', '聘用中', '1', 2),
('teacher_status', '待续聘', '2', 3),
('teacher_status', '已解聘', '3', 4),
-- 学历
('education', '专科', '1', 1),
('education', '本科', '2', 2),
('education', '硕士研究生', '3', 3),
('education', '博士研究生', '4', 4),
-- 学位
('degree', '学士', '1', 1),
('degree', '硕士', '2', 2),
('degree', '博士', '3', 3),
-- 职称等级
('title_level', '初级(助教)', '1', 1),
('title_level', '中级(讲师)', '2', 2),
('title_level', '副高(副教授)', '3', 3),
('title_level', '正高(教授)', '4', 4),
-- 课程类型
('course_type', '理论课', '1', 1),
('course_type', '实践课', '2', 2),
('course_type', '讲座', '3', 3),
-- 授课层次
('teaching_level', '本科', '1', 1),
('teaching_level', '研究生', '2', 2),
-- 考核等级
('assess_grade', '优秀', '1', 1),
('assess_grade', '合格', '2', 2),
('assess_grade', '不合格', '3', 3),
-- 审批状态
('approval_status', '待审批', '0', 1),
('approval_status', '已通过', '1', 2),
('approval_status', '已驳回', '2', 3),
('approval_status', '已撤回', '3', 4),
-- 合同状态
('contract_status', '已作废', '0', 1),
('contract_status', '生效中', '1', 2),
('contract_status', '已到期', '2', 3),
('contract_status', '已终止', '3', 4),
-- 解聘类型
('termination_type', '合同到期', '1', 1),
('termination_type', '考核不合格', '2', 2),
('termination_type', '主动离职', '3', 3),
('termination_type', '其他', '4', 4);

-- 9.6 初始化职称系数
INSERT INTO `salary_title_coefficient` (`title_level`, `title_name`, `coefficient`) VALUES
(1, '助教', 1.00),
(2, '讲师', 1.10),
(3, '副教授', 1.15),
(4, '教授', 1.20);

-- 9.7 初始化考核系数
INSERT INTO `salary_assess_coefficient` (`grade`, `coefficient`) VALUES
('优秀', 1.10),
('合格', 1.00),
('不合格', 0.80);

-- 9.8 初始化考核维度配置
INSERT INTO `assess_dimension_config` (`dimension_code`, `dimension_name`, `weight`, `score_source`, `description`, `sort`) VALUES
('teaching_quality', '教学质量考核', 50.00, 'auto', '包含课堂监督评分、学生评价平均分、课程考核通过率', 1),
('attendance', '履职情况考核', 30.00, 'auto', '包含授课出勤率、调课停课次数、作业批改及时性', 2),
('contribution', '附加贡献考核', 20.00, 'manual', '包含课程建设参与、学生竞赛指导、教学改进建议', 3);

-- 9.9 初始化消息模板
INSERT INTO `sys_msg_template` (`template_code`, `template_name`, `msg_type`, `title_template`, `content_template`) VALUES
('APPLY_RESULT', '报名审核结果通知', 'notice', '您的外聘教师报名审核结果', '尊敬的${teacherName}，您申请的${positionName}岗位审核结果为：${result}。${remark}'),
('APPROVAL_PROGRESS', '聘用审批进度提醒', 'approval', '聘用审批进度更新', '您提交的${teacherName}老师聘用申请，当前审批状态：${status}。'),
('COURSE_CHANGE', '授课时间变更通知', 'notice', '课程时间变更通知', '${courseName}课程时间已变更，原时间：${originalTime}，新时间：${newTime}。'),
('ASSESS_RESULT', '考核结果通知', 'notice', '${period}考核结果通知', '尊敬的${teacherName}老师，您${period}考核结果为：${grade}，得分：${score}分。'),
('SALARY_PAYMENT', '薪酬发放提醒', 'notice', '${period}薪酬已发放', '尊敬的${teacherName}老师，您${period}薪酬￥${amount}已发放，请注意查收。'),
('CONTRACT_EXPIRE', '聘用到期续聘提醒', 'remind', '聘用合同即将到期提醒', '${teacherName}老师的聘用合同将于${expireDate}到期，请及时处理续聘事宜。');

SELECT '数据库初始化完成！' AS message;
