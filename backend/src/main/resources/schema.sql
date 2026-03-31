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
    role VARCHAR(20) NOT NULL DEFAULT 'TEACHER',
    department_id BIGINT,
    status INT NOT NULL DEFAULT 1,
    deleted INT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
    education VARCHAR(50),
    degree VARCHAR(50),
    title VARCHAR(50),
    work_unit VARCHAR(200),
    speciality VARCHAR(200),
    department_id BIGINT,
    hire_status VARCHAR(20) DEFAULT 'PENDING',
    hire_start_date DATE,
    hire_end_date DATE,
    photo VARCHAR(255),
    address VARCHAR(300),
    bank_account VARCHAR(50),
    bank_name VARCHAR(100),
    remark VARCHAR(500),
    deleted INT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 课程表
CREATE TABLE IF NOT EXISTS course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50),
    department_id BIGINT,
    teacher_id BIGINT,
    semester VARCHAR(50),
    credit DECIMAL(3,1),
    hours INT,
    weekly_hours INT,
    class_name VARCHAR(100),
    student_count INT,
    classroom VARCHAR(100),
    schedule VARCHAR(200),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    deleted INT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 考勤记录表
CREATE TABLE IF NOT EXISTS attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    teacher_id BIGINT NOT NULL,
    course_id BIGINT,
    attendance_date DATE NOT NULL,
    check_in_time TIMESTAMP,
    check_out_time TIMESTAMP,
    status VARCHAR(20) DEFAULT 'NORMAL',
    actual_hours DECIMAL(4,1),
    remark VARCHAR(500),
    deleted INT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 薪酬记录表
CREATE TABLE IF NOT EXISTS salary (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    teacher_id BIGINT NOT NULL,
    "MONTH" VARCHAR(20) NOT NULL,
    base_salary DECIMAL(10,2) DEFAULT 0,
    total_hours DECIMAL(6,1) DEFAULT 0,
    hour_rate DECIMAL(8,2) DEFAULT 0,
    bonus DECIMAL(10,2) DEFAULT 0,
    deduction DECIMAL(10,2) DEFAULT 0,
    total_salary DECIMAL(10,2) DEFAULT 0,
    status VARCHAR(20) DEFAULT 'PENDING',
    pay_date DATE,
    remark VARCHAR(500),
    deleted INT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 评价表
CREATE TABLE IF NOT EXISTS evaluation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    teacher_id BIGINT NOT NULL,
    course_id BIGINT,
    semester VARCHAR(50),
    teaching_score DECIMAL(4,1),
    attendance_score DECIMAL(4,1),
    student_score DECIMAL(4,1),
    total_score DECIMAL(4,1),
    evaluator VARCHAR(50),
    evaluation_date DATE,
    comment VARCHAR(1000),
    deleted INT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 系统通知表
CREATE TABLE IF NOT EXISTS notice (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    type VARCHAR(20) DEFAULT 'SYSTEM',
    publisher VARCHAR(50),
    status INT DEFAULT 1,
    deleted INT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 聘用审批表（三级审批流程）
CREATE TABLE IF NOT EXISTS approval (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    teacher_id BIGINT NOT NULL,
    approval_no VARCHAR(50),
    department_id BIGINT,
    start_date DATE,
    end_date DATE,
    proposed_salary DECIMAL(10,2),
    apply_reason VARCHAR(500),
    current_node VARCHAR(30) DEFAULT 'college_leader',
    approval_status VARCHAR(20) DEFAULT 'PENDING',
    college_status VARCHAR(20),
    college_remark VARCHAR(500),
    college_by VARCHAR(50),
    college_time TIMESTAMP,
    hr_salary_status VARCHAR(20),
    hr_salary_remark VARCHAR(500),
    hr_salary_by VARCHAR(50),
    hr_salary_time TIMESTAMP,
    hr_director_status VARCHAR(20),
    hr_director_remark VARCHAR(500),
    hr_director_by VARCHAR(50),
    hr_director_time TIMESTAMP,
    deleted INT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 聘用合同表
CREATE TABLE IF NOT EXISTS contract (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    teacher_id BIGINT NOT NULL,
    department_id BIGINT,
    contract_no VARCHAR(50),
    approval_id BIGINT,
    start_date DATE,
    end_date DATE,
    salary_standard DECIMAL(10,2),
    contract_status VARCHAR(20) DEFAULT 'UNSIGNED',
    teacher_sign_time TIMESTAMP,
    school_sign_time TIMESTAMP,
    deleted INT NOT NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
