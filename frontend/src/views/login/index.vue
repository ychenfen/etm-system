<template>
  <div class="login-container">
    <!-- 背景装饰 -->
    <div class="bg-decoration">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
    </div>

    <div class="login-card">
      <div class="login-header">
        <div class="logo-icon">
          <i class="el-icon-s-custom"></i>
        </div>
        <h2 class="login-title">高校外聘教师管理系统</h2>
        <p class="login-subtitle">External Teacher Management System</p>
      </div>

      <el-form :model="loginForm" :rules="rules" ref="loginForm" class="login-form">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" prefix-icon="el-icon-user" placeholder="请输入用户名" size="large"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" prefix-icon="el-icon-lock" placeholder="请输入密码" type="password" show-password size="large" @keyup.enter.native="handleLogin"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" style="width: 100%; height: 44px; font-size: 15px;" @click="handleLogin">
            <span v-if="!loading">登 录</span>
            <span v-else>登录中...</span>
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-footer">
        <div class="login-tips">
          <el-divider><span style="color: #c0c4cc; font-size: 12px;">演示账号</span></el-divider>
          <div class="account-list">
            <div class="account-item" @click="fillAccount('admin', 'admin123')">
              <el-tag size="mini" type="danger">管理员</el-tag>
              <span>admin / admin123</span>
            </div>
            <div class="account-item" @click="fillAccount('cs_admin', 'admin123')">
              <el-tag size="mini" type="warning">院系管理</el-tag>
              <span>cs_admin / admin123</span>
            </div>
            <div class="account-item" @click="fillAccount('teacher01', 'admin123')">
              <el-tag size="mini">教师</el-tag>
              <span>teacher01 / admin123</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="login-copyright">
      &copy; 2026 高校外聘教师管理系统 毕业设计
    </div>
  </div>
</template>

<script>
import { login } from '@/api'

export default {
  name: 'Login',
  data() {
    return {
      loginForm: { username: '', password: '' },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      loading: false
    }
  },
  methods: {
    fillAccount(username, password) {
      this.loginForm.username = username
      this.loginForm.password = password
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          login(this.loginForm).then(res => {
            this.$store.dispatch('login', res.data)
            this.$message.success('登录成功')
            this.$router.push('/')
          }).finally(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #1e3c72 0%, #2a5298 50%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

.bg-decoration {
  position: absolute;
  width: 100%;
  height: 100%;
  pointer-events: none;
}
.circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.08;
  background: #fff;
}
.circle-1 { width: 400px; height: 400px; top: -100px; right: -100px; }
.circle-2 { width: 300px; height: 300px; bottom: -80px; left: -80px; }
.circle-3 { width: 200px; height: 200px; top: 50%; left: 60%; }

.login-card {
  width: 440px;
  padding: 45px 40px 30px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0,0,0,0.3);
  z-index: 1;
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.logo-icon {
  width: 64px;
  height: 64px;
  margin: 0 auto 15px;
  background: linear-gradient(135deg, #409EFF, #764ba2);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.logo-icon i {
  font-size: 32px;
  color: #fff;
}

.login-title {
  color: #303133;
  margin: 0 0 5px;
  font-size: 22px;
  font-weight: 600;
  letter-spacing: 1px;
}

.login-subtitle {
  color: #909399;
  margin: 0;
  font-size: 12px;
  letter-spacing: 0.5px;
}

.login-form {
  margin-top: 25px;
}
.login-form .el-input__inner {
  height: 44px;
  border-radius: 8px;
}

.login-footer {
  margin-top: 5px;
}

.account-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.account-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 6px 12px;
  background: #f5f7fa;
  border-radius: 6px;
  cursor: pointer;
  font-size: 13px;
  color: #606266;
  transition: all 0.2s;
}
.account-item:hover {
  background: #ecf5ff;
  color: #409EFF;
}

.login-copyright {
  position: absolute;
  bottom: 20px;
  color: rgba(255,255,255,0.5);
  font-size: 12px;
  z-index: 1;
}
</style>
