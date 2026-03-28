-- 高校外聘教师管理系统 数据库初始化脚本

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    real_name VARCHAR(50),
    phone VARCHAR(20),
    email VARCHAR(100),
    avatar VARCHAR(255),
    role VARCHAR(20) NOT NULL DEFAULT 'TEACHER' COMMENT '角色: ADMIN-管理员, DEPARTMENT-院系管理员, TEACHER-外聘教师',
    department_id BIGINT,
    status INT NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    deleted INT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 院系/部门表
CREATE TABLE IF NOT EXISTS department (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50),
    contact_person VARCHAR(50),
    contact_phone VARCHAR(20),
    description VARCHAR(500),
    status INT NOT NULL DEFAULT 1,
    deleted INT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 外聘教师信息表
CREATE TABLE IF NOT EXISTS teacher (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    name VARCHAR(50) NOT NULL,
    gender VARCHAR(10),
    birth_date DATE,
    id_card VARCHAR(20),
    phone VARCHAR(20),
    email VARCHAR(100),
    education VARCHAR(50) COMMENT '学历',
    degree VARCHAR(50) COMMENT '学位',
    title VARCHAR(50) COMMENT '职称',
    work_unit VARCHAR(200) COMMENT '工作单位',
    speciality VARCHAR(200) COMMENT '专业特长',
    department_id BIGINT,
    hire_status VARCHAR(20) DEFAULT 'PENDING' COMMENT '聘用状态: PENDING-待审核, APPROVED-已聘用, REJECTED-已拒绝, EXPIRED-已到期',
    hire_start_date DATE,
    hire_end_date DATE,
    photo VARCHAR(255),
    address VARCHAR(300),
    bank_account VARCHAR(50),
    bank_name VARCHAR(100),
    remark VARCHAR(500),
    deleted INT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 课程表
CREATE TABLE IF NOT EXISTS course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50),
    department_id BIGINT,
    teacher_id BIGINT,
    semester VARCHAR(50) COMMENT '学期',
    credit DECIMAL(3,1) COMMENT '学分',
    hours INT COMMENT '总学时',
    weekly_hours INT COMMENT '周学时',
    class_name VARCHAR(100) COMMENT '授课班级',
    student_count INT COMMENT '学生人数',
    classroom VARCHAR(100) COMMENT '教室',
    schedule VARCHAR(200) COMMENT '上课时间',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE-进行中, COMPLETED-已完成, CANCELLED-已取消',
    deleted INT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 考勤记录表
CREATE TABLE IF NOT EXISTS attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    teacher_id BIGINT NOT NULL,
    course_id BIGINT,
    attendance_date DATE NOT NULL,
    check_in_time TIMESTAMP COMMENT '签到时间',
    check_out_time TIMESTAMP COMMENT '签退时间',
    status VARCHAR(20) DEFAULT 'NORMAL' COMMENT '状态: NORMAL-正常, LATE-迟到, EARLY_LEAVE-早退, ABSENT-缺勤, LEAVE-请假',
    actual_hours DECIMAL(4,1) COMMENT '实际课时',
    remark VARCHAR(500),
    deleted INT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 薪酬记录表
CREATE TABLE IF NOT EXISTS salary (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    teacher_id BIGINT NOT NULL,
    month VARCHAR(20) NOT NULL COMMENT '月份 yyyy-MM',
    base_salary DECIMAL(10,2) DEFAULT 0 COMMENT '基本课时费',
    total_hours DECIMAL(6,1) DEFAULT 0 COMMENT '总课时',
    hour_rate DECIMAL(8,2) DEFAULT 0 COMMENT '课时单价',
    bonus DECIMAL(10,2) DEFAULT 0 COMMENT '奖金',
    deduction DECIMAL(10,2) DEFAULT 0 COMMENT '扣款',
    total_salary DECIMAL(10,2) DEFAULT 0 COMMENT '应发金额',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态: PENDING-待审核, APPROVED-已审核, PAID-已发放',
    pay_date DATE COMMENT '发放日期',
    remark VARCHAR(500),
    deleted INT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 评价表
CREATE TABLE IF NOT EXISTS evaluation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    teacher_id BIGINT NOT NULL,
    course_id BIGINT,
    semester VARCHAR(50),
    teaching_score DECIMAL(4,1) COMMENT '教学评分',
    attendance_score DECIMAL(4,1) COMMENT '考勤评分',
    student_score DECIMAL(4,1) COMMENT '学生评价分',
    total_score DECIMAL(4,1) COMMENT '综合评分',
    evaluator VARCHAR(50) COMMENT '评价人',
    evaluation_date DATE,
    comment VARCHAR(1000) COMMENT '评价内容',
    deleted INT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 系统通知表
CREATE TABLE IF NOT EXISTS notice (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    type VARCHAR(20) DEFAULT 'SYSTEM' COMMENT '类型: SYSTEM-系统通知, HIRE-聘用通知, SALARY-薪酬通知',
    publisher VARCHAR(50),
    status INT DEFAULT 1 COMMENT '状态: 0-草稿, 1-已发布',
    deleted INT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
