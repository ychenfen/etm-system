# RuoYi-Vue 集成指南

## 一、项目结构

```
于晨旭毕设/
├── RuoYi-Vue/              # RuoYi-Vue框架(已下载)
│   ├── ruoyi-admin/        # 后端主模块
│   ├── ruoyi-common/       # 公共模块
│   ├── ruoyi-framework/    # 框架核心
│   ├── ruoyi-system/       # 系统模块
│   ├── ruoyi-generator/    # 代码生成器 ⭐
│   ├── ruoyi-quartz/       # 定时任务
│   └── ruoyi-ui/           # 前端Vue项目
│
├── sql/
│   ├── etm_database.sql         # 原始SQL(备用)
│   └── etm_ruoyi_business.sql   # 适配RuoYi的业务SQL ⭐
│
├── 开发规划方案.md
├── 接口设计文档.md
└── RuoYi集成指南.md              # 本文档
```

---

## 二、环境准备

### 2.1 必需环境
- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+ / 8.0
- Redis 6.0+
- Node.js 16+

### 2.2 开发工具
- IntelliJ IDEA (后端)
- VS Code (前端)
- Navicat / DBeaver (数据库)

---

## 三、初始化步骤

### 步骤1: 创建数据库
```bash
# 登录MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE `ry-vue` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `ry-vue`;
```

### 步骤2: 执行RuoYi基础SQL
```bash
# 在RuoYi-Vue/sql目录下，按顺序执行：
mysql -u root -p ry-vue < RuoYi-Vue/sql/ry_20240629.sql
mysql -u root -p ry-vue < RuoYi-Vue/sql/quartz.sql
```

### 步骤3: 执行业务SQL
```bash
# 执行外聘老师管理系统的业务表
mysql -u root -p ry-vue < sql/etm_ruoyi_business.sql
```

### 步骤4: 修改后端配置
编辑 `RuoYi-Vue/ruoyi-admin/src/main/resources/application-druid.yml`:
```yaml
spring:
  datasource:
    druid:
      master:
        url: jdbc:mysql://localhost:3306/ry-vue?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
        username: root
        password: 你的密码
```

编辑 `RuoYi-Vue/ruoyi-admin/src/main/resources/application.yml`:
```yaml
# Redis配置
spring:
  redis:
    host: localhost
    port: 6379
    password:  # 如有密码填写
```

### 步骤5: 启动后端
```bash
cd RuoYi-Vue
mvn clean install -DskipTests

# 启动
cd ruoyi-admin
mvn spring-boot:run
```

后端启动成功后访问: http://localhost:8080

### 步骤6: 启动前端
```bash
cd RuoYi-Vue/ruoyi-ui
npm install
npm run dev
```

前端启动成功后访问: http://localhost:80

**默认账号**: admin / admin123

---

## 四、使用代码生成器

RuoYi 自带代码生成器，可以根据数据库表自动生成 CRUD 代码。

### 4.1 访问代码生成器
1. 登录系统后，进入 **系统工具 → 代码生成**
2. 点击 **导入** 按钮，选择业务表

### 4.2 需要导入的表
按以下顺序导入并生成代码：

| 表名 | 模块名 | 说明 |
|------|--------|------|
| etm_college | college | 学院管理 |
| etm_teacher | teacher | 外聘老师 |
| etm_position | position | 岗位需求 |
| etm_recruit_notice | recruit | 招募公告 |
| etm_application | recruit | 报名申请 |
| etm_employ_approval | recruit | 聘用审批 |
| etm_contract | contract | 聘用合同 |
| etm_schedule | course | 课程安排 |
| etm_supervision | course | 授课监督 |
| etm_evaluation | course | 学生评价 |
| etm_assess_record | assess | 考核记录 |
| etm_salary | salary | 薪酬结算 |
| etm_renewal | contract | 续聘申请 |
| etm_termination | contract | 解聘记录 |

### 4.3 生成配置
导入表后，点击 **编辑** 设置生成选项：

**基本信息**:
- 生成模块名: `etm`
- 生成业务名: 如 `teacher`
- 生成包路径: `com.ruoyi.etm`

**字段信息**:
- 设置每个字段的显示类型(输入框/下拉框/日期等)
- 设置查询方式(精确/模糊/范围)
- 设置必填项

**生成信息**:
- 模板类型: 单表(crud)
- 生成方式: zip压缩包

### 4.4 应用生成的代码

1. 下载生成的zip文件
2. 解压后将代码复制到对应目录:
   - Java代码 → `ruoyi-admin/src/main/java/`
   - Mapper XML → `ruoyi-admin/src/main/resources/mapper/`
   - Vue代码 → `ruoyi-ui/src/views/`
   - API代码 → `ruoyi-ui/src/api/`
   - SQL菜单 → 执行到数据库

---

## 五、自定义开发

代码生成器生成的是基础CRUD，复杂业务需要手动开发。

### 5.1 后端开发规范

**目录结构**:
```
ruoyi-admin/src/main/java/com/ruoyi/etm/
├── controller/          # 控制器
│   ├── EtmTeacherController.java
│   └── ...
├── domain/              # 实体类
│   ├── EtmTeacher.java
│   └── ...
├── mapper/              # Mapper接口
│   ├── EtmTeacherMapper.java
│   └── ...
├── service/             # 服务接口
│   ├── IEtmTeacherService.java
│   └── impl/
│       └── EtmTeacherServiceImpl.java
└── vo/                  # 视图对象(自己创建)
    └── TeacherDetailVO.java
```

**示例: 扩展外聘老师查询**
```java
// EtmTeacherController.java
@GetMapping("/detail/{teacherId}")
public AjaxResult getDetail(@PathVariable Long teacherId) {
    TeacherDetailVO detail = etmTeacherService.getTeacherDetail(teacherId);
    return AjaxResult.success(detail);
}

// IEtmTeacherService.java
TeacherDetailVO getTeacherDetail(Long teacherId);

// EtmTeacherServiceImpl.java
@Override
public TeacherDetailVO getTeacherDetail(Long teacherId) {
    TeacherDetailVO vo = new TeacherDetailVO();
    // 基本信息
    EtmTeacher teacher = etmTeacherMapper.selectEtmTeacherByTeacherId(teacherId);
    BeanUtils.copyProperties(teacher, vo);
    // 工作经历
    vo.setWorkList(etmTeacherWorkMapper.selectByTeacherId(teacherId));
    // 证书信息
    vo.setCertList(etmTeacherCertMapper.selectByTeacherId(teacherId));
    return vo;
}
```

### 5.2 前端开发规范

**目录结构**:
```
ruoyi-ui/src/views/etm/
├── teacher/
│   ├── index.vue        # 列表页
│   ├── detail.vue       # 详情页(自己创建)
│   └── form.vue         # 表单页(自己创建)
├── recruit/
│   ├── notice/
│   ├── application/
│   └── approval/
└── ...
```

**示例: 老师详情页**
```vue
<template>
  <div class="app-container">
    <el-descriptions title="基本信息" :column="3" border>
      <el-descriptions-item label="姓名">{{ teacher.name }}</el-descriptions-item>
      <el-descriptions-item label="手机号">{{ teacher.phone }}</el-descriptions-item>
      <el-descriptions-item label="职称">{{ teacher.title }}</el-descriptions-item>
    </el-descriptions>

    <el-divider />

    <h4>工作经历</h4>
    <el-table :data="workList" border>
      <el-table-column prop="companyName" label="单位名称" />
      <el-table-column prop="position" label="职务" />
      <el-table-column prop="startDate" label="开始时间" />
      <el-table-column prop="endDate" label="结束时间" />
    </el-table>
  </div>
</template>

<script>
import { getTeacherDetail } from '@/api/etm/teacher'

export default {
  data() {
    return {
      teacher: {},
      workList: []
    }
  },
  created() {
    this.getDetail()
  },
  methods: {
    getDetail() {
      const teacherId = this.$route.params.id
      getTeacherDetail(teacherId).then(res => {
        this.teacher = res.data
        this.workList = res.data.workList || []
      })
    }
  }
}
</script>
```

---

## 六、核心业务开发要点

### 6.1 多级审批流程
聘用审批涉及3个节点，使用状态机模式：

```java
public void approve(Long approvalId, String node, String status, String remark) {
    EtmEmployApproval approval = getById(approvalId);

    switch (node) {
        case "college":
            approval.setCollegeStatus(status);
            approval.setCollegeTime(new Date());
            approval.setCollegeRemark(remark);
            if ("1".equals(status)) {
                approval.setCurrentNode("hr_salary");
            }
            break;
        case "hr_salary":
            approval.setHrSalaryStatus(status);
            // ...
            if ("1".equals(status)) {
                approval.setCurrentNode("hr_director");
            }
            break;
        case "hr_director":
            approval.setHrDirectorStatus(status);
            // ...
            if ("1".equals(status)) {
                approval.setApprovalStatus("1"); // 全部通过
                // 自动生成合同
                generateContract(approval);
            }
            break;
    }

    if ("2".equals(status)) {
        approval.setApprovalStatus("2"); // 驳回
    }

    updateById(approval);
}
```

### 6.2 薪酬自动计算
```java
public void generateSalary(String settlePeriod, Long collegeId) {
    // 获取该学院该月所有外聘老师的授课记录
    List<EtmSchedule> schedules = scheduleMapper.selectByPeriodAndCollege(settlePeriod, collegeId);

    // 按老师分组
    Map<Long, List<EtmSchedule>> groupByTeacher = schedules.stream()
        .collect(Collectors.groupingBy(EtmSchedule::getTeacherId));

    for (Map.Entry<Long, List<EtmSchedule>> entry : groupByTeacher.entrySet()) {
        Long teacherId = entry.getKey();
        List<EtmSchedule> teacherSchedules = entry.getValue();

        EtmTeacher teacher = teacherMapper.selectById(teacherId);

        // 计算总课时
        BigDecimal totalHours = teacherSchedules.stream()
            .map(s -> new BigDecimal(s.getCompletedHours()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 获取基础薪酬
        BigDecimal baseSalary = getBaseSalary(teacherSchedules.get(0));

        // 获取职称系数
        BigDecimal titleCoef = getTitleCoefficient(teacher.getTitleLevel());

        // 获取考核系数
        BigDecimal assessCoef = getAssessCoefficient(teacherId);

        // 计算总薪酬
        BigDecimal totalAmount = baseSalary
            .multiply(totalHours)
            .multiply(titleCoef)
            .multiply(assessCoef);

        // 保存结算记录
        EtmSalary salary = new EtmSalary();
        salary.setTeacherId(teacherId);
        salary.setSettlePeriod(settlePeriod);
        salary.setActualHours(totalHours);
        salary.setTitleCoef(titleCoef);
        salary.setAssessCoef(assessCoef);
        salary.setTotalAmount(totalAmount);
        salaryMapper.insert(salary);
    }
}
```

### 6.3 考核自动汇总
```java
public void generateAssessRecord(Long periodId, Long teacherId) {
    // 获取教学质量分(监督+评价)
    BigDecimal supervisionAvg = supervisionMapper.getAvgScore(periodId, teacherId);
    BigDecimal evaluationAvg = evaluationMapper.getAvgScore(periodId, teacherId);
    BigDecimal teachingScore = supervisionAvg.multiply(new BigDecimal("0.6"))
        .add(evaluationAvg.multiply(new BigDecimal("0.4")));

    // 获取履职情况分
    BigDecimal attendanceRate = scheduleMapper.getAttendanceRate(periodId, teacherId);
    BigDecimal attendanceScore = attendanceRate.multiply(new BigDecimal("100"));

    // 附加贡献分(手动填写)
    BigDecimal contributionScore = new BigDecimal("80"); // 默认值

    // 计算总分(按权重)
    BigDecimal totalScore = teachingScore.multiply(new BigDecimal("0.5"))
        .add(attendanceScore.multiply(new BigDecimal("0.3")))
        .add(contributionScore.multiply(new BigDecimal("0.2")));

    // 判定等级
    String grade;
    if (totalScore.compareTo(new BigDecimal("90")) >= 0) {
        grade = "优秀";
    } else if (totalScore.compareTo(new BigDecimal("60")) >= 0) {
        grade = "合格";
    } else {
        grade = "不合格";
    }

    // 保存考核记录
    EtmAssessRecord record = new EtmAssessRecord();
    record.setPeriodId(periodId);
    record.setTeacherId(teacherId);
    record.setTeachingScore(teachingScore);
    record.setAttendanceScore(attendanceScore);
    record.setContributionScore(contributionScore);
    record.setTotalScore(totalScore);
    record.setGrade(grade);
    assessRecordMapper.insert(record);
}
```

---

## 七、开发顺序建议

1. **第一阶段: 基础CRUD** (代码生成器)
   - 学院管理
   - 外聘老师管理
   - 岗位需求管理

2. **第二阶段: 招募流程**
   - 招募公告
   - 在线报名
   - 多级审批
   - 合同生成

3. **第三阶段: 授课管理**
   - 课程安排
   - 授课监督
   - 学生评价
   - 调课申请

4. **第四阶段: 考核薪酬**
   - 考核配置
   - 考核汇总
   - 薪酬计算
   - 薪酬发放

5. **第五阶段: 合同续聘**
   - 续聘流程
   - 解聘流程
   - 状态跟踪

6. **第六阶段: 数据统计**
   - 数据概览
   - 图表统计
   - 报表导出

---

## 八、常见问题

### Q1: 代码生成后菜单不显示？
执行生成的SQL中的菜单插入语句，然后刷新页面。

### Q2: 接口403无权限？
在角色管理中给当前角色分配对应菜单权限。

### Q3: 如何自定义字典？
系统管理 → 字典管理，添加如:
- `etm_teacher_status`: 外聘老师状态
- `etm_title_level`: 职称等级

### Q4: 文件上传配置？
修改 `application.yml`:
```yaml
ruoyi:
  profile: /data/uploadPath
```

---

## 九、参考资源

- [RuoYi-Vue 官方文档](http://doc.ruoyi.vip/)
- [RuoYi-Vue Gitee](https://gitee.com/y_project/RuoYi-Vue)
- [Element UI 组件库](https://element.eleme.cn/)
