-- 初始化数据

-- 院系数据
INSERT INTO department (name, code, contact_person, contact_phone, description) VALUES
('计算机科学与技术学院', 'CS', '张主任', '13800000001', '负责计算机相关专业教学'),
('数学与统计学院', 'MATH', '李主任', '13800000002', '负责数学与统计学相关专业教学'),
('外国语学院', 'FL', '王主任', '13800000003', '负责外语相关专业教学'),
('经济管理学院', 'EM', '赵主任', '13800000004', '负责经济管理相关专业教学'),
('艺术设计学院', 'ART', '陈主任', '13800000005', '负责艺术设计相关专业教学');

-- 管理员账户 (密码: admin123)
INSERT INTO sys_user (username, password, real_name, phone, email, role, status) VALUES
('admin', 'e64b78fc3bc91bcbc7dc232ba8ec59e0', '系统管理员', '13800000000', 'admin@etm.edu.cn', 'ADMIN', 1);

-- 院系管理员
INSERT INTO sys_user (username, password, real_name, phone, email, role, department_id, status) VALUES
('cs_admin', 'e64b78fc3bc91bcbc7dc232ba8ec59e0', '张建国', '13800000011', 'zhangjg@etm.edu.cn', 'DEPARTMENT', 1, 1),
('math_admin', 'e64b78fc3bc91bcbc7dc232ba8ec59e0', '李文华', '13800000012', 'liwh@etm.edu.cn', 'DEPARTMENT', 2, 1);

-- 外聘教师用户
INSERT INTO sys_user (username, password, real_name, phone, email, role, department_id, status) VALUES
('teacher01', 'e64b78fc3bc91bcbc7dc232ba8ec59e0', '刘伟', '13900000001', 'liuwei@example.com', 'TEACHER', 1, 1),
('teacher02', 'e64b78fc3bc91bcbc7dc232ba8ec59e0', '陈静', '13900000002', 'chenjing@example.com', 'TEACHER', 1, 1),
('teacher03', 'e64b78fc3bc91bcbc7dc232ba8ec59e0', '王磊', '13900000003', 'wanglei@example.com', 'TEACHER', 2, 1);

-- 外聘教师信息
INSERT INTO teacher (user_id, name, gender, birth_date, id_card, phone, email, education, degree, title, work_unit, speciality, department_id, hire_status, hire_start_date, hire_end_date) VALUES
(4, '刘伟', '男', '1985-03-15', '110101198503150011', '13900000001', 'liuwei@example.com', '研究生', '硕士', '高级工程师', '某科技有限公司', 'Java开发, 软件工程', 1, 'APPROVED', '2025-09-01', '2026-07-01'),
(5, '陈静', '女', '1988-07-22', '110101198807220022', '13900000002', 'chenjing@example.com', '研究生', '博士', '副教授', '某研究院', '人工智能, 机器学习', 1, 'APPROVED', '2025-09-01', '2026-07-01'),
(6, '王磊', '男', '1982-11-08', '110101198211080033', '13900000003', 'wanglei@example.com', '研究生', '博士', '教授', '某大学', '高等数学, 概率论', 2, 'PENDING', '2026-03-01', '2026-07-01');

-- 课程数据
INSERT INTO course (name, code, department_id, teacher_id, semester, credit, hours, weekly_hours, class_name, student_count, classroom, schedule, status) VALUES
('Java高级编程', 'CS301', 1, 1, '2025-2026-2', 3.0, 48, 3, '计科2301班', 45, 'A301', '周一3-4节,周三5-6节', 'ACTIVE'),
('机器学习导论', 'CS401', 1, 2, '2025-2026-2', 3.0, 48, 3, '计科2201班', 38, 'B201', '周二1-2节,周四3-4节', 'ACTIVE'),
('高等数学A', 'MATH101', 2, 3, '2025-2026-2', 4.0, 64, 4, '数学2301班', 50, 'C101', '周一1-2节,周三1-2节', 'ACTIVE');

-- 考勤数据
INSERT INTO attendance (teacher_id, course_id, attendance_date, check_in_time, check_out_time, status, actual_hours) VALUES
(1, 1, '2026-03-02', '2026-03-02 08:00:00', '2026-03-02 09:40:00', 'NORMAL', 2.0),
(1, 1, '2026-03-04', '2026-03-04 14:00:00', '2026-03-04 15:40:00', 'NORMAL', 2.0),
(2, 2, '2026-03-03', '2026-03-03 08:00:00', '2026-03-03 09:40:00', 'NORMAL', 2.0),
(1, 1, '2026-03-09', '2026-03-09 08:15:00', '2026-03-09 09:40:00', 'LATE', 2.0),
(2, 2, '2026-03-10', '2026-03-10 08:00:00', '2026-03-10 09:40:00', 'NORMAL', 2.0);

-- 薪酬数据
INSERT INTO salary (teacher_id, month, base_salary, total_hours, hour_rate, bonus, deduction, total_salary, status) VALUES
(1, '2026-03', 0, 32, 150.00, 500, 0, 5300.00, 'APPROVED'),
(2, '2026-03', 0, 28, 180.00, 600, 0, 5640.00, 'PENDING');

-- 评价数据
INSERT INTO evaluation (teacher_id, course_id, semester, teaching_score, attendance_score, student_score, total_score, evaluator, evaluation_date, comment) VALUES
(1, 1, '2025-2026-1', 92.0, 95.0, 88.0, 91.7, '张建国', '2026-01-15', '教学认真负责，实践经验丰富，学生反馈良好'),
(2, 2, '2025-2026-1', 95.0, 98.0, 92.0, 95.0, '张建国', '2026-01-15', '学术水平高，教学方法新颖，深受学生欢迎');

-- 通知数据
INSERT INTO notice (title, content, type, publisher) VALUES
('关于2025-2026学年第二学期外聘教师聘用工作的通知', '各院系：\n\n根据学校教学工作安排，现启动2025-2026学年第二学期外聘教师聘用工作。请各院系根据教学需要，做好外聘教师的遴选和推荐工作。\n\n具体要求如下：\n1. 外聘教师须具有硕士及以上学历或中级及以上职称\n2. 聘用材料请于2026年2月28日前提交\n3. 联系人：教务处 李老师 电话：010-12345678', 'HIRE', '教务处'),
('关于外聘教师薪酬发放的通知', '各位外聘教师：\n\n2026年3月份课时费将于4月15日前发放至个人银行账户，请注意查收。如有疑问请联系财务处。', 'SALARY', '财务处');
