# 高校外聘教师管理系统 (ETM System)

基于 **Spring Boot + Vue 2 + Element UI** 的高校外聘教师管理系统。

## 技术栈

### 后端
- Spring Boot 2.7
- MyBatis-Plus
- H2 Database（开发环境）/ MySQL（生产环境）
- JWT 认证
- Knife4j (Swagger) API 文档

### 前端
- Vue 2
- Element UI
- Axios
- Vue Router
- Vuex
- ECharts

## 功能模块

- 用户登录与权限管理（管理员/院系管理员/外聘教师）
- 院系管理
- 外聘教师信息管理
- 聘用审核管理
- 课程管理
- 考勤管理
- 薪酬管理
- 教学评价管理
- 系统通知管理
- 数据统计仪表盘

## 快速开始

### 后端启动
```bash
cd backend
mvn spring-boot:run
```
后端默认运行在 http://localhost:8088/api

说明：开发环境默认使用 `backend/data/` 下的本地 H2 文件库，数据会在重启后保留。
如果你想重置为初始演示数据，删除 `backend/data/` 后重新启动后端即可。

### 前端启动
```bash
cd frontend
npm install
npm run serve
```
前端默认运行在 http://localhost:8080

### 默认账户
| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 院系管理员 | cs_admin | admin123 |
| 外聘教师 | teacher01 | admin123 |
