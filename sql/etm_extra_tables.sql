-- assessment_period表
CREATE TABLE IF NOT EXISTS `assessment_period` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `period_name` VARCHAR(100) NOT NULL,
    `period_type` TINYINT DEFAULT 1 COMMENT '1学期 2年度',
    `start_date` DATE,
    `end_date` DATE,
    `status` TINYINT DEFAULT 0 COMMENT '0未开始 1进行中 2已结束',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='考核周期表';

-- assessment_config表
CREATE TABLE IF NOT EXISTS `assessment_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `dimension_name` VARCHAR(100) NOT NULL,
    `dimension_code` VARCHAR(100) NOT NULL,
    `weight` INT DEFAULT 0,
    `description` VARCHAR(255),
    `is_active` TINYINT DEFAULT 1,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='考核维度配置';

-- assessment_record表
CREATE TABLE IF NOT EXISTS `assessment_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `teacher_id` BIGINT,
    `period_id` BIGINT,
    `teaching_quality_score` DECIMAL(5,2),
    `attendance_score` DECIMAL(5,2),
    `contribution_score` DECIMAL(5,2),
    `total_score` DECIMAL(5,2),
    `grade` VARCHAR(20),
    `report_url` VARCHAR(255),
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='考核记录';

-- assessment_detail表
CREATE TABLE IF NOT EXISTS `assessment_detail` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `record_id` BIGINT,
    `dimension_code` VARCHAR(100),
    `dimension_name` VARCHAR(100),
    `raw_score` DECIMAL(5,2),
    `weight` INT,
    `weighted_score` DECIMAL(5,2),
    `deduction_reason` VARCHAR(255),
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='考核明细';

-- salary_rule表
CREATE TABLE IF NOT EXISTS `salary_rule` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `rule_name` VARCHAR(100) NOT NULL,
    `course_type` VARCHAR(50),
    `teaching_level` VARCHAR(50),
    `base_salary` DECIMAL(10,2),
    `description` VARCHAR(255),
    `is_active` TINYINT DEFAULT 1,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='薪酬规则';

-- salary_settlement表
CREATE TABLE IF NOT EXISTS `salary_settlement` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `settlement_no` VARCHAR(50),
    `teacher_id` BIGINT,
    `teacher_name` VARCHAR(50),
    `settlement_period` VARCHAR(20),
    `period_type` TINYINT DEFAULT 1,
    `college_id` BIGINT,
    `actual_hours` DECIMAL(10,2),
    `base_salary` DECIMAL(10,2),
    `title_coefficient` DECIMAL(5,2),
    `assess_coefficient` DECIMAL(5,2),
    `base_amount` DECIMAL(10,2),
    `title_allowance` DECIMAL(10,2),
    `assess_bonus` DECIMAL(10,2),
    `deduction_amount` DECIMAL(10,2) DEFAULT 0,
    `deduction_reason` VARCHAR(255),
    `total_amount` DECIMAL(10,2),
    `verify_status` TINYINT DEFAULT 0,
    `payment_status` TINYINT DEFAULT 0,
    `payment_time` DATETIME,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='薪酬结算';

-- employment_contract表
CREATE TABLE IF NOT EXISTS `employment_contract` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `teacher_id` BIGINT,
    `teacher_name` VARCHAR(50),
    `college_id` BIGINT,
    `college_name` VARCHAR(100),
    `contract_no` VARCHAR(50),
    `employ_start_date` DATE,
    `employ_end_date` DATE,
    `salary` DECIMAL(10,2),
    `contract_file_url` VARCHAR(255),
    `sign_status` TINYINT DEFAULT 0,
    `sign_time` DATETIME,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='聘用合同';

-- renewal_application表
CREATE TABLE IF NOT EXISTS `renewal_application` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `teacher_id` BIGINT,
    `contract_id` BIGINT,
    `college_id` BIGINT,
    `renewal_start_date` DATE,
    `renewal_end_date` DATE,
    `renewal_reason` VARCHAR(500),
    `course_plan` VARCHAR(500),
    `proposed_salary` DECIMAL(10,2),
    `approval_status` TINYINT DEFAULT 0,
    `approver_id` BIGINT,
    `approval_comment` VARCHAR(255),
    `approval_time` DATETIME,
    `new_contract_id` BIGINT,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='续聘申请';

-- termination_record表
CREATE TABLE IF NOT EXISTS `termination_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `teacher_id` BIGINT,
    `teacher_name` VARCHAR(50),
    `contract_id` BIGINT,
    `termination_type` TINYINT COMMENT '1到期不续 2考核不合格 3主动离职 4其他',
    `termination_reason` VARCHAR(500),
    `termination_date` DATE,
    `notice_file_url` VARCHAR(255),
    `handover_status` TINYINT DEFAULT 0,
    `handover_details` TEXT,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='解聘记录';

-- notification表
CREATE TABLE IF NOT EXISTS `notification` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT,
    `title` VARCHAR(100),
    `content` TEXT,
    `msg_type` VARCHAR(50),
    `is_read` TINYINT DEFAULT 0,
    `send_channel` VARCHAR(20) DEFAULT 'system',
    `send_time` DATETIME,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_is_read` (`is_read`)
) ENGINE=InnoDB COMMENT='消息通知';

-- course_schedule表
CREATE TABLE IF NOT EXISTS `course_schedule` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `teacher_id` BIGINT,
    `college_id` BIGINT,
    `semester` VARCHAR(20),
    `course_name` VARCHAR(100),
    `class_name` VARCHAR(100),
    `student_count` INT,
    `week_start` INT,
    `week_end` INT,
    `day_of_week` INT,
    `period_start` INT,
    `period_end` INT,
    `location` VARCHAR(100),
    `total_hours` DECIMAL(10,2),
    `exam_method` VARCHAR(50),
    `status` TINYINT DEFAULT 0,
    `syllabus_url` VARCHAR(255),
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='课程安排';

-- teaching_supervision表
CREATE TABLE IF NOT EXISTS `teaching_supervision` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `schedule_id` BIGINT,
    `supervisor_id` BIGINT,
    `supervision_date` DATE,
    `is_on_time` TINYINT,
    `content_consistency` INT,
    `teaching_method` INT,
    `classroom_atmosphere` INT,
    `student_feedback` INT,
    `problems` VARCHAR(500),
    `suggestions` VARCHAR(500),
    `evidence_urls` VARCHAR(500),
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='授课监督记录';

-- student_evaluation表
CREATE TABLE IF NOT EXISTS `student_evaluation` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `schedule_id` BIGINT,
    `student_id` BIGINT,
    `clarity_score` INT,
    `interaction_score` INT,
    `homework_score` INT,
    `attitude_score` INT,
    `overall_score` INT,
    `comments` VARCHAR(500),
    `is_anonymous` TINYINT DEFAULT 1,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='学生评价';

-- schedule_change_request表
CREATE TABLE IF NOT EXISTS `schedule_change_request` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `schedule_id` BIGINT,
    `teacher_id` BIGINT,
    `change_type` TINYINT COMMENT '1调课 2停课',
    `reason` VARCHAR(500),
    `original_date` DATE,
    `original_period` VARCHAR(50),
    `new_date` DATE,
    `new_period` VARCHAR(50),
    `new_location` VARCHAR(100),
    `approval_status` TINYINT DEFAULT 0,
    `approver_id` BIGINT,
    `approval_time` DATETIME,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='调课申请';

-- recruit_application表
CREATE TABLE IF NOT EXISTS `recruit_application` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `notice_id` BIGINT,
    `teacher_id` BIGINT,
    `apply_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `self_introduction` TEXT,
    `match_score` DECIMAL(5,2),
    `first_review_status` TINYINT DEFAULT 0,
    `first_review_comment` VARCHAR(255),
    `second_review_status` TINYINT DEFAULT 0,
    `second_review_comment` VARCHAR(255),
    `second_review_by` BIGINT,
    `second_review_time` DATETIME,
    `interview_score` DECIMAL(5,2),
    `interview_comment` VARCHAR(500),
    `final_status` TINYINT DEFAULT 0,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='招募申请';

-- employment_approval表
CREATE TABLE IF NOT EXISTS `employment_approval` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `application_id` BIGINT,
    `teacher_id` BIGINT,
    `college_id` BIGINT,
    `position_id` BIGINT,
    `proposed_salary` DECIMAL(10,2),
    `employ_start_date` DATE,
    `employ_end_date` DATE,
    `apply_reason` VARCHAR(500),
    `college_leader_status` TINYINT DEFAULT 0,
    `college_leader_comment` VARCHAR(255),
    `college_leader_time` DATETIME,
    `hr_salary_status` TINYINT DEFAULT 0,
    `hr_salary_comment` VARCHAR(255),
    `hr_salary_time` DATETIME,
    `hr_director_status` TINYINT DEFAULT 0,
    `hr_director_comment` VARCHAR(255),
    `hr_director_time` DATETIME,
    `final_status` TINYINT DEFAULT 0,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='聘用审批';

-- recruitment_notice表
CREATE TABLE IF NOT EXISTS `recruitment_notice` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(200) NOT NULL,
    `college_id` BIGINT,
    `position_id` BIGINT,
    `content` TEXT,
    `requirements` TEXT,
    `apply_start_time` DATETIME,
    `apply_end_time` DATETIME,
    `status` TINYINT DEFAULT 0,
    `create_by` VARCHAR(50),
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='招募公告';

-- sys_menu表
CREATE TABLE IF NOT EXISTS `sys_menu` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `parent_id` BIGINT DEFAULT 0,
    `menu_name` VARCHAR(100),
    `menu_title` VARCHAR(100),
    `menu_type` VARCHAR(20),
    `route_path` VARCHAR(255),
    `component_path` VARCHAR(255),
    `permission_code` VARCHAR(100),
    `icon` VARCHAR(100),
    `sort_order` INT DEFAULT 0,
    `status` TINYINT DEFAULT 1,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='系统菜单';
