# 快速开始指南

## 项目初始化步骤

### 1. 数据库初始化

```bash
# 1. 创建数据库
mysql -u root -p
CREATE DATABASE etm_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 2. 执行SQL脚本初始化表
mysql -u root -p etm_db < /path/to/sql/etm_extra_tables.sql

# 3. 如果已有基础表(teacher_info, base_college等)，确保它们也已创建
```

### 2. Maven依赖配置

在项目的 `pom.xml` 中添加以下依赖块（参考 `pom-dependencies.xml`）：

```xml
<!-- 复制 pom-dependencies.xml 中的所有依赖到你的pom.xml -->
```

### 3. 应用配置

复制 `application-dev.yml` 到 `src/main/resources/`：

```bash
cp application-dev.yml src/main/resources/
```

修改配置中的数据库连接信息：
- 数据库URL: `jdbc:mysql://localhost:3306/etm_db`
- 用户名: `root`
- 密码: `root`（改为你的密码）
- 文件上传路径: `/data/upload`（确保目录存在并有写权限）

### 4. 项目编译和运行

```bash
# 清理并编译
mvn clean install

# 运行项目
mvn spring-boot:run

# 或者打包成jar包后运行
mvn package
java -jar target/etm-backend.jar
```

### 5. 验证项目启动

访问应用健康检查端点：
```
http://localhost:8080/api/contract/page?pageNum=1&pageSize=10
```

如果返回空数组（因为还没有数据），说明应用启动成功。

---

## API 路由总览

### 合同管理 (`/api/contract`)
| 方法 | 路由 | 功能 |
|-----|-----|-----|
| GET | `/page` | 分页查询合同 |
| GET | `/{id}` | 获取合同详情 |
| POST | `` | 创建合同 |
| PUT | `/{id}/sign` | 签署合同 |
| GET | `/renewal/page` | 续聘申请分页查询 |
| POST | `/renewal` | 创建续聘申请 |
| PUT | `/renewal/{id}/approve` | 审核续聘申请 |
| GET | `/termination/page` | 解聘记录分页查询 |
| POST | `/termination` | 创建解聘记录 |

### 数据统计 (`/api/statistics`)
| 方法 | 路由 | 功能 |
|-----|-----|-----|
| GET | `/overview` | 系统概览统计 |
| GET | `/teacher?dimension=college` | 教师统计 |
| GET | `/salary?year=2024` | 薪酬统计 |
| GET | `/assessment` | 考核统计 |
| GET | `/export?type=teacher&startDate=&endDate=` | 导出报表 |

### 消息通知 (`/api/notification`)
| 方法 | 路由 | 功能 |
|-----|-----|-----|
| GET | `/page` | 通知分页查询 |
| PUT | `/{id}/read` | 标记为已读 |
| PUT | `/batch-read` | 批量标记为已读 |
| GET | `/unread-count` | 获取未读数量 |

### 文件管理 (`/api/file`)
| 方法 | 路由 | 功能 |
|-----|-----|-----|
| POST | `/upload?type=other` | 上传文件 |
| GET | `/download/{type}/{year}/{month}/{filename}` | 下载文件 |
| GET | `/preview/{type}/{year}/{month}/{filename}` | 预览文件 |

### 系统菜单 (`/api/system/menu`)
| 方法 | 路由 | 功能 |
|-----|-----|-----|
| GET | `/list` | 获取菜单树 |
| GET | `/{id}` | 菜单详情 |
| POST | `` | 创建菜单 |
| PUT | `/{id}` | 更新菜单 |
| DELETE | `/{id}` | 删除菜单 |

---

## API 请求示例

### 1. 创建合同

```bash
curl -X POST http://localhost:8080/api/contract \
  -H "Content-Type: application/json" \
  -d '{
    "teacherId": 1,
    "teacherName": "张三",
    "collegeId": 1,
    "collegeName": "计算机学院",
    "employStartDate": "2024-01-01",
    "employEndDate": "2024-12-31",
    "salary": 5000
  }'
```

### 2. 签署合同

```bash
curl -X PUT http://localhost:8080/api/contract/1/sign
```

### 3. 创建续聘申请

```bash
curl -X POST http://localhost:8080/api/contract/renewal \
  -H "Content-Type: application/json" \
  -d '{
    "teacherId": 1,
    "contractId": 1,
    "collegeId": 1,
    "renewalStartDate": "2025-01-01",
    "renewalEndDate": "2025-12-31",
    "renewalReason": "工作表现优秀",
    "coursePlan": "继续教授高等数学",
    "proposedSalary": 5500
  }'
```

### 4. 审核续聘申请

```bash
curl -X PUT http://localhost:8080/api/contract/renewal/1/approve \
  -H "Content-Type: application/json" \
  -d '{
    "status": 1,
    "comment": "同意续聘"
  }'
```

### 5. 获取统计概览

```bash
curl http://localhost:8080/api/statistics/overview
```

### 6. 上传文件

```bash
curl -X POST http://localhost:8080/api/file/upload?type=contract \
  -F "file=@/path/to/file.pdf"
```

### 7. 查询通知列表

```bash
curl "http://localhost:8080/api/notification/page?pageNum=1&pageSize=10"
```

---

## 核心功能说明

### 合同号自动生成
- 格式: `CON` + `yyyyMM` + 四位序号
- 示例: `CON202401001`, `CON202401002`

### 续聘审核流程
1. 创建续聘申请（approval_status=0）
2. 审核通过（approval_status=1）
3. 系统自动创建新合同
4. 新合同关联到续聘申请的 `newContractId` 字段

### 数据统计维度
- **教师统计**: 按学院、职称、状态分组
- **薪酬统计**: 按月份汇总总额
- **考核统计**: 按等级分布和占比

### 文件存储结构
```
uploadPath/
├── contract/2024/01/uuid1.pdf
├── contract/2024/02/uuid2.pdf
├── other/2024/01/uuid3.jpg
└── ...
```

---

## 配置说明

### MyBatis Plus 配置
- 自动填充: createTime, updateTime
- 逻辑删除: deleted字段（0=未删除, 1=已删除）
- 驼峰转换: 数据库下划线转Java驼峰命名

### 文件上传配置
- 支持格式: jpg, jpeg, png, pdf, docx, xlsx
- 最大文件大小: 需在Spring Boot配置中设置 `spring.servlet.multipart.max-file-size`
- 访问前缀: 配置为 `http://localhost:8080/files`

---

## 扩展建议

### 1. 添加权限控制
```java
// 在SecurityConfig中配置权限拦截
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/api/contract/**").hasRole("ADMIN")
            .antMatchers("/api/statistics/**").hasRole("ADMIN")
            // ...
            .anyRequest().authenticated()
            .and().formLogin()
            .and().csrf().disable();
    }
}
```

### 2. 添加国际化支持
```yaml
spring:
  messages:
    basename: i18n/messages
    encoding: UTF-8
```

### 3. 添加缓存机制
```java
@Cacheable(value = "contracts", key = "#id")
public EmploymentContract getContractDetail(Long id) {
    // ...
}
```

### 4. 添加审计日志
```java
@EnableJpaAuditing
@Configuration
public class AuditConfig {
    // 配置Auditing
}
```

### 5. 添加API文档
```java
@Configuration
public class Swagger3Config {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("ETM API")
                .version("1.0.0"));
    }
}
```

---

## 常见问题

### Q1: 启动时报"表不存在"错误
**A**: 执行 `etm_extra_tables.sql` 脚本创建所有必需的表

### Q2: 文件上传失败
**A**: 检查配置的上传路径 `file.upload-path` 是否存在且有写权限
```bash
mkdir -p /data/upload
chmod 777 /data/upload
```

### Q3: 统计数据查询为空
**A**: 确保数据库中有相应的数据记录，统计查询依赖于实际数据

### Q4: 续聘审核后没有创建新合同
**A**: 检查：
1. 原合同是否存在
2. 审核status是否为1（通过）
3. 检查数据库是否有新合同记录

### Q5: 菜单树结构不正确
**A**: 确保菜单的parentId配置正确，根菜单的parentId应为0

---

## 项目结构清单

```
etm-backend/
├── src/main/java/com/etm/
│   ├── common/              # 通用工具和配置
│   │   ├── config/          # 配置类
│   │   ├── exception/       # 异常处理
│   │   ├── result/          # 响应结果类
│   │   └── utils/           # 工具类
│   │
│   ├── modules/
│   │   ├── contract/        # 合同管理模块
│   │   ├── statistics/      # 数据统计模块
│   │   ├── notification/    # 消息通知模块
│   │   ├── file/            # 文件管理模块
│   │   ├── system/          # 系统管理模块
│   │   ├── teacher/         # 教师模块（存根）
│   │   ├── college/         # 学院模块（存根）
│   │   ├── course/          # 课程模块（存根）
│   │   ├── salary/          # 薪酬模块（存根）
│   │   └── assessment/      # 考核模块（存根）
│   │
│   └── EtmBackendApplication.java   # 启动类
│
├── src/main/resources/
│   ├── application.yml              # 主配置
│   ├── application-dev.yml          # 开发环境配置
│   └── mapper/                      # MyBatis XML映射文件（如需要）
│
├── sql/
│   └── etm_extra_tables.sql         # 数据库初始化脚本
│
├── pom.xml                          # Maven配置
└── README.md                        # 项目说明
```

---

## 技术支持

遇到问题时，请检查：
1. 日志输出: `logs/etm-backend.log`
2. 控制台输出: Spring Boot启动日志
3. 数据库连接: 确保MySQL服务运行且连接参数正确
4. 依赖版本: 参考 `pom-dependencies.xml` 中的版本号

---

## 下一步

1. 根据实际需求调整数据库表结构
2. 完善业务逻辑和数据验证
3. 添加权限和认证机制
4. 编写单元测试和集成测试
5. 生成API文档
6. 部署到测试/生产环境

祝您开发愉快！
