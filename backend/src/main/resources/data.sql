-- 初始化演示数据
-- 使用幂等写法，避免 H2 文件库重复启动时因重复插入导致启动失败

-- 院系数据
INSERT INTO department (name, code, contact_person, contact_phone, description)
SELECT '计算机科学与技术学院', 'CS', '张主任', '13800000001', '负责计算机相关专业教学'
WHERE NOT EXISTS (SELECT 1 FROM department WHERE code = 'CS');

INSERT INTO department (name, code, contact_person, contact_phone, description)
SELECT '数学与统计学院', 'MATH', '李主任', '13800000002', '负责数学与统计学相关专业教学'
WHERE NOT EXISTS (SELECT 1 FROM department WHERE code = 'MATH');

INSERT INTO department (name, code, contact_person, contact_phone, description)
SELECT '外国语学院', 'FL', '王主任', '13800000003', '负责外语相关专业教学'
WHERE NOT EXISTS (SELECT 1 FROM department WHERE code = 'FL');

INSERT INTO department (name, code, contact_person, contact_phone, description)
SELECT '经济管理学院', 'EM', '赵主任', '13800000004', '负责经济管理相关专业教学'
WHERE NOT EXISTS (SELECT 1 FROM department WHERE code = 'EM');

INSERT INTO department (name, code, contact_person, contact_phone, description)
SELECT '艺术设计学院', 'ART', '陈主任', '13800000005', '负责艺术设计相关专业教学'
WHERE NOT EXISTS (SELECT 1 FROM department WHERE code = 'ART');

-- 用户账户 (密码: admin123)
INSERT INTO sys_user (username, password, real_name, phone, email, role, status)
SELECT 'admin', '0192023a7bbd73250516f069df18b500', '系统管理员', '13800000000', 'admin@etm.edu.cn', 'ADMIN', 1
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE username = 'admin');

INSERT INTO sys_user (username, password, real_name, phone, email, role, department_id, status)
SELECT
    'cs_admin',
    '0192023a7bbd73250516f069df18b500',
    '张建国',
    '13800000011',
    'zhangjg@etm.edu.cn',
    'DEPARTMENT',
    (SELECT MIN(id) FROM department WHERE code = 'CS'),
    1
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE username = 'cs_admin');

INSERT INTO sys_user (username, password, real_name, phone, email, role, department_id, status)
SELECT
    'math_admin',
    '0192023a7bbd73250516f069df18b500',
    '李文华',
    '13800000012',
    'liwh@etm.edu.cn',
    'DEPARTMENT',
    (SELECT MIN(id) FROM department WHERE code = 'MATH'),
    1
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE username = 'math_admin');

INSERT INTO sys_user (username, password, real_name, phone, email, role, department_id, status)
SELECT
    'teacher01',
    '0192023a7bbd73250516f069df18b500',
    '刘伟',
    '13900000001',
    'liuwei@example.com',
    'TEACHER',
    (SELECT MIN(id) FROM department WHERE code = 'CS'),
    1
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE username = 'teacher01');

INSERT INTO sys_user (username, password, real_name, phone, email, role, department_id, status)
SELECT
    'teacher02',
    '0192023a7bbd73250516f069df18b500',
    '陈静',
    '13900000002',
    'chenjing@example.com',
    'TEACHER',
    (SELECT MIN(id) FROM department WHERE code = 'CS'),
    1
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE username = 'teacher02');

INSERT INTO sys_user (username, password, real_name, phone, email, role, department_id, status)
SELECT
    'teacher03',
    '0192023a7bbd73250516f069df18b500',
    '王磊',
    '13900000003',
    'wanglei@example.com',
    'TEACHER',
    (SELECT MIN(id) FROM department WHERE code = 'MATH'),
    1
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE username = 'teacher03');

-- 外聘教师信息
INSERT INTO teacher (
    user_id, name, gender, birth_date, id_card, phone, email, education, degree,
    title, work_unit, speciality, department_id, hire_status, hire_start_date, hire_end_date
)
SELECT
    (SELECT MIN(id) FROM sys_user WHERE username = 'teacher01'),
    '刘伟', '男', '1985-03-15', '110101198503150011', '13900000001', 'liuwei@example.com',
    '研究生', '硕士', '高级工程师', '某科技有限公司', 'Java开发, 软件工程',
    (SELECT MIN(id) FROM department WHERE code = 'CS'),
    'APPROVED', '2025-09-01', '2026-07-01'
WHERE NOT EXISTS (
    SELECT 1 FROM teacher
    WHERE user_id = (SELECT MIN(id) FROM sys_user WHERE username = 'teacher01')
);

INSERT INTO teacher (
    user_id, name, gender, birth_date, id_card, phone, email, education, degree,
    title, work_unit, speciality, department_id, hire_status, hire_start_date, hire_end_date
)
SELECT
    (SELECT MIN(id) FROM sys_user WHERE username = 'teacher02'),
    '陈静', '女', '1988-07-22', '110101198807220022', '13900000002', 'chenjing@example.com',
    '研究生', '博士', '副教授', '某研究院', '人工智能, 机器学习',
    (SELECT MIN(id) FROM department WHERE code = 'CS'),
    'APPROVED', '2025-09-01', '2026-07-01'
WHERE NOT EXISTS (
    SELECT 1 FROM teacher
    WHERE user_id = (SELECT MIN(id) FROM sys_user WHERE username = 'teacher02')
);

INSERT INTO teacher (
    user_id, name, gender, birth_date, id_card, phone, email, education, degree,
    title, work_unit, speciality, department_id, hire_status, hire_start_date, hire_end_date
)
SELECT
    (SELECT MIN(id) FROM sys_user WHERE username = 'teacher03'),
    '王磊', '男', '1982-11-08', '110101198211080033', '13900000003', 'wanglei@example.com',
    '研究生', '博士', '教授', '某大学', '高等数学, 概率论',
    (SELECT MIN(id) FROM department WHERE code = 'MATH'),
    'PENDING', '2026-03-01', '2026-07-01'
WHERE NOT EXISTS (
    SELECT 1 FROM teacher
    WHERE user_id = (SELECT MIN(id) FROM sys_user WHERE username = 'teacher03')
);

-- 课程数据
INSERT INTO course (
    name, code, department_id, teacher_id, semester, credit, hours, weekly_hours,
    class_name, student_count, classroom, schedule, status
)
SELECT
    'Java高级编程',
    'CS301',
    (SELECT MIN(id) FROM department WHERE code = 'CS'),
    (
        SELECT MIN(t.id)
        FROM teacher t
        JOIN sys_user u ON u.id = t.user_id
        WHERE u.username = 'teacher01'
    ),
    '2025-2026-2', 3.0, 48, 3, '计科2301班', 45, 'A301', '周一3-4节,周三5-6节', 'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM course WHERE code = 'CS301');

INSERT INTO course (
    name, code, department_id, teacher_id, semester, credit, hours, weekly_hours,
    class_name, student_count, classroom, schedule, status
)
SELECT
    '机器学习导论',
    'CS401',
    (SELECT MIN(id) FROM department WHERE code = 'CS'),
    (
        SELECT MIN(t.id)
        FROM teacher t
        JOIN sys_user u ON u.id = t.user_id
        WHERE u.username = 'teacher02'
    ),
    '2025-2026-2', 3.0, 48, 3, '计科2201班', 38, 'B201', '周二1-2节,周四3-4节', 'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM course WHERE code = 'CS401');

INSERT INTO course (
    name, code, department_id, teacher_id, semester, credit, hours, weekly_hours,
    class_name, student_count, classroom, schedule, status
)
SELECT
    '高等数学A',
    'MATH101',
    (SELECT MIN(id) FROM department WHERE code = 'MATH'),
    (
        SELECT MIN(t.id)
        FROM teacher t
        JOIN sys_user u ON u.id = t.user_id
        WHERE u.username = 'teacher03'
    ),
    '2025-2026-2', 4.0, 64, 4, '数学2301班', 50, 'C101', '周一1-2节,周三1-2节', 'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM course WHERE code = 'MATH101');

-- 考勤数据
INSERT INTO attendance (
    teacher_id, course_id, attendance_date, check_in_time, check_out_time, status, actual_hours
)
SELECT
    (
        SELECT MIN(t.id)
        FROM teacher t
        JOIN sys_user u ON u.id = t.user_id
        WHERE u.username = 'teacher01'
    ),
    (SELECT MIN(id) FROM course WHERE code = 'CS301'),
    '2026-03-02', '2026-03-02 08:00:00', '2026-03-02 09:40:00', 'NORMAL', 2.0
WHERE NOT EXISTS (
    SELECT 1 FROM attendance
    WHERE teacher_id = (
        SELECT MIN(t.id)
        FROM teacher t
        JOIN sys_user u ON u.id = t.user_id
        WHERE u.username = 'teacher01'
    )
    AND course_id = (SELECT MIN(id) FROM course WHERE code = 'CS301')
    AND attendance_date = '2026-03-02'
);

INSERT INTO attendance (
    teacher_id, course_id, attendance_date, check_in_time, check_out_time, status, actual_hours
)
SELECT
    (
        SELECT MIN(t.id)
        FROM teacher t
        JOIN sys_user u ON u.id = t.user_id
        WHERE u.username = 'teacher01'
    ),
    (SELECT MIN(id) FROM course WHERE code = 'CS301'),
    '2026-03-04', '2026-03-04 14:00:00', '2026-03-04 15:40:00', 'NORMAL', 2.0
WHERE NOT EXISTS (
    SELECT 1 FROM attendance
    WHERE teacher_id = (
        SELECT MIN(t.id)
        FROM teacher t
        JOIN sys_user u ON u.id = t.user_id
        WHERE u.username = 'teacher01'
    )
    AND course_id = (SELECT MIN(id) FROM course WHERE code = 'CS301')
    AND attendance_date = '2026-03-04'
);

INSERT INTO attendance (
    teacher_id, course_id, attendance_date, check_in_time, check_out_time, status, actual_hours
)
SELECT
    (
        SELECT MIN(t.id)
        FROM teacher t
        JOIN sys_user u ON u.id = t.user_id
        WHERE u.username = 'teacher02'
    ),
    (SELECT MIN(id) FROM course WHERE code = 'CS401'),
    '2026-03-03', '2026-03-03 08:00:00', '2026-03-03 09:40:00', 'NORMAL', 2.0
WHERE NOT EXISTS (
    SELECT 1 FROM attendance
    WHERE teacher_id = (
        SELECT MIN(t.id)
        FROM teacher t
        JOIN sys_user u ON u.id = t.user_id
        WHERE u.username = 'teacher02'
    )
    AND course_id = (SELECT MIN(id) FROM course WHERE code = 'CS401')
    AND attendance_date = '2026-03-03'
);

INSERT INTO attendance (
    teacher_id, course_id, attendance_date, check_in_time, check_out_time, status, actual_hours
)
SELECT
    (
        SELECT MIN(t.id)
        FROM teacher t
        JOIN sys_user u ON u.id = t.user_id
        WHERE u.username = 'teacher01'
    ),
    (SELECT MIN(id) FROM course WHERE code = 'CS301'),
    '2026-03-09', '2026-03-09 08:15:00', '2026-03-09 09:40:00', 'LATE', 2.0
WHERE NOT EXISTS (
    SELECT 1 FROM attendance
    WHERE teacher_id = (
        SELECT MIN(t.id)
        FROM teacher t
        JOIN sys_user u ON u.id = t.user_id
        WHERE u.username = 'teacher01'
    )
    AND course_id = (SELECT MIN(id) FROM course WHERE code = 'CS301')
    AND attendance_date = '2026-03-09'
);

INSERT INTO attendance (
    teacher_id, course_id, attendance_date, check_in_time, check_out_time, status, actual_hours
)
SELECT
    (
        SELECT MIN(t.id)
        FROM teacher t
        JOIN sys_user u ON u.id = t.user_id
        WHERE u.username = 'teacher02'
    ),
    (SELECT MIN(id) FROM course WHERE code = 'CS401'),
    '2026-03-10', '2026-03-10 08:00:00', '2026-03-10 09:40:00', 'NORMAL', 2.0
WHERE NOT EXISTS (
    SELECT 1 FROM attendance
    WHERE teacher_id = (
        SELECT MIN(t.id)
        FROM teacher t
        JOIN sys_user u ON u.id = t.user_id
        WHERE u.username = 'teacher02'
    )
    AND course_id = (SELECT MIN(id) FROM course WHERE code = 'CS401')
    AND attendance_date = '2026-03-10'
);

-- 薪酬数据
INSERT INTO salary (
    teacher_id, "MONTH", base_salary, total_hours, hour_rate, bonus, deduction, total_salary, status
)
SELECT
    (
        SELECT MIN(t.id)
        FROM teacher t
        JOIN sys_user u ON u.id = t.user_id
        WHERE u.username = 'teacher01'
    ),
    '2026-03', 0, 32, 150.00, 500, 0, 5300.00, 'APPROVED'
WHERE NOT EXISTS (
    SELECT 1 FROM salary
    WHERE teacher_id = (
        SELECT MIN(t.id)
        FROM teacher t
        JOIN sys_user u ON u.id = t.user_id
        WHERE u.username = 'teacher01'
    )
    AND "MONTH" = '2026-03'
);

INSERT INTO salary (
    teacher_id, "MONTH", base_salary, total_hours, hour_rate, bonus, deduction, total_salary, status
)
SELECT
    (
        SELECT MIN(t.id)
        FROM teacher t
        JOIN sys_user u ON u.id = t.user_id
        WHERE u.username = 'teacher02'
    ),
    '2026-03', 0, 28, 180.00, 600, 0, 5640.00, 'PENDING'
WHERE NOT EXISTS (
    SELECT 1 FROM salary
    WHERE teacher_id = (
        SELECT MIN(t.id)
        FROM teacher t
        JOIN sys_user u ON u.id = t.user_id
        WHERE u.username = 'teacher02'
    )
    AND "MONTH" = '2026-03'
);

-- 评价数据
INSERT INTO evaluation (
    teacher_id, course_id, semester, teaching_score, attendance_score, student_score,
    total_score, evaluator, evaluation_date, comment
)
SELECT
    (
        SELECT MIN(t.id)
        FROM teacher t
        JOIN sys_user u ON u.id = t.user_id
        WHERE u.username = 'teacher01'
    ),
    (SELECT MIN(id) FROM course WHERE code = 'CS301'),
    '2025-2026-1', 92.0, 95.0, 88.0, 91.7, '张建国', '2026-01-15',
    '教学认真负责，实践经验丰富，学生反馈良好'
WHERE NOT EXISTS (
    SELECT 1 FROM evaluation
    WHERE teacher_id = (
        SELECT MIN(t.id)
        FROM teacher t
        JOIN sys_user u ON u.id = t.user_id
        WHERE u.username = 'teacher01'
    )
    AND course_id = (SELECT MIN(id) FROM course WHERE code = 'CS301')
    AND semester = '2025-2026-1'
);

INSERT INTO evaluation (
    teacher_id, course_id, semester, teaching_score, attendance_score, student_score,
    total_score, evaluator, evaluation_date, comment
)
SELECT
    (
        SELECT MIN(t.id)
        FROM teacher t
        JOIN sys_user u ON u.id = t.user_id
        WHERE u.username = 'teacher02'
    ),
    (SELECT MIN(id) FROM course WHERE code = 'CS401'),
    '2025-2026-1', 95.0, 98.0, 92.0, 95.0, '张建国', '2026-01-15',
    '学术水平高，教学方法新颖，深受学生欢迎'
WHERE NOT EXISTS (
    SELECT 1 FROM evaluation
    WHERE teacher_id = (
        SELECT MIN(t.id)
        FROM teacher t
        JOIN sys_user u ON u.id = t.user_id
        WHERE u.username = 'teacher02'
    )
    AND course_id = (SELECT MIN(id) FROM course WHERE code = 'CS401')
    AND semester = '2025-2026-1'
);

-- 通知数据
INSERT INTO notice (title, content, type, publisher)
SELECT
    '关于2025-2026学年第二学期外聘教师聘用工作的通知',
    '各院系：\n\n根据学校教学工作安排，现启动2025-2026学年第二学期外聘教师聘用工作。请各院系根据教学需要，做好外聘教师的遴选和推荐工作。\n\n具体要求如下：\n1. 外聘教师须具有硕士及以上学历或中级及以上职称\n2. 聘用材料请于2026年2月28日前提交\n3. 联系人：教务处 李老师 电话：010-12345678',
    'ADMIN',
    '教务处'
WHERE NOT EXISTS (
    SELECT 1 FROM notice
    WHERE title = '关于2025-2026学年第二学期外聘教师聘用工作的通知'
);

INSERT INTO notice (title, content, type, publisher)
SELECT
    '关于外聘教师薪酬发放的通知',
    '各位外聘教师：\n\n2026年3月份课时费将于4月15日前发放至个人银行账户，请注意查收。如有疑问请联系财务处。',
    'ADMIN',
    '财务处'
WHERE NOT EXISTS (
    SELECT 1 FROM notice
    WHERE title = '关于外聘教师薪酬发放的通知'
);

INSERT INTO notice (title, content, type, publisher)
SELECT
    '关于本学期教学质量检查的通知',
    '各院系、各位外聘教师：\n\n本学期期中教学质量检查将于4月中旬开展，请各位教师做好教学准备。检查内容包括教学计划执行、课堂教学质量、作业批改等。',
    'TEACHING',
    '教务处'
WHERE NOT EXISTS (
    SELECT 1 FROM notice
    WHERE title = '关于本学期教学质量检查的通知'
);

-- 聘用审批演示数据
INSERT INTO approval (teacher_id, approval_no, department_id, start_date, end_date, proposed_salary, apply_reason,
    current_node, approval_status,
    college_status, college_remark, college_by, college_time,
    hr_salary_status, hr_salary_remark, hr_salary_by, hr_salary_time,
    hr_director_status, hr_director_remark, hr_director_by, hr_director_time)
SELECT 1, 'APR20260001', 1, '2026-03-01', '2026-08-31', 150.00, '拟聘为计算机学院外聘讲师，承担数据结构课程教学',
    'finished', 'APPROVED',
    'APPROVED', '同意聘用', '李院长', '2026-01-10 10:00:00',
    'APPROVED', '薪酬标准符合规定', '王薪酬', '2026-01-11 14:00:00',
    'APPROVED', '同意，请办理聘用手续', '张处长', '2026-01-12 09:30:00'
WHERE NOT EXISTS (SELECT 1 FROM approval WHERE approval_no = 'APR20260001');

INSERT INTO approval (teacher_id, approval_no, department_id, start_date, end_date, proposed_salary, apply_reason,
    current_node, approval_status,
    college_status, college_remark, college_by, college_time)
SELECT 2, 'APR20260002', 2, '2026-03-01', '2027-02-28', 180.00, '拟聘为商学院外聘副教授，承担市场营销课程教学',
    'hr_salary', 'PENDING',
    'APPROVED', '人员符合要求，同意推荐', '陈院长', '2026-01-15 11:00:00',
    NULL, NULL, NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM approval WHERE approval_no = 'APR20260002');

INSERT INTO approval (teacher_id, approval_no, department_id, start_date, end_date, proposed_salary, apply_reason,
    current_node, approval_status)
SELECT 3, 'APR20260003', 3, '2026-04-01', '2026-12-31', 120.00, '拟聘为文学院外聘讲师，承担现代文学课程',
    'college_leader', 'PENDING',
    NULL, NULL, NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM approval WHERE approval_no = 'APR20260003');

-- 聘用合同演示数据（对应已通过的审批 APR20260001）
INSERT INTO contract (teacher_id, department_id, contract_no, start_date, end_date, salary_standard,
    contract_status, teacher_sign_time, school_sign_time)
SELECT 1, 1, 'CTR20260001', '2026-03-01', '2026-08-31', 150.00,
    'SIGNED', '2026-01-14 16:00:00', '2026-01-15 10:00:00'
WHERE NOT EXISTS (SELECT 1 FROM contract WHERE contract_no = 'CTR20260001');
