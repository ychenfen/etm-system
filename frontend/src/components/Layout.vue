<template>
  <el-container style="height: 100vh">
    <!-- 侧边栏 -->
    <el-aside :width="collapse ? '64px' : '230px'" style="background: linear-gradient(180deg, #1a1f36 0%, #252d47 50%, #1e293b 100%); transition: width 0.3s; box-shadow: 2px 0 12px rgba(0,0,0,0.15);">
      <div class="logo" v-show="!collapse">
        <h3 style="color: #fff; text-align: center; line-height: 60px; margin: 0; font-size: 15px; font-weight: 500; letter-spacing: 2px;">外聘教师管理系统</h3>
      </div>
      <div class="logo" v-show="collapse">
        <h3 style="color: #60a5fa; text-align: center; line-height: 60px; margin: 0; font-weight: 700; font-size: 18px;">ETM</h3>
      </div>
      <el-menu
        :default-active="$route.path"
        background-color="transparent"
        text-color="#94a3b8"
        active-text-color="#60a5fa"
        :collapse="collapse"
        :collapse-transition="false"
        router
      >
        <el-menu-item index="/dashboard">
          <i class="el-icon-s-home"></i>
          <span slot="title">首页</span>
        </el-menu-item>
        <el-menu-item index="/teacher" v-if="isAdmin || isDept">
          <i class="el-icon-user"></i>
          <span slot="title">教师管理</span>
        </el-menu-item>
        <el-menu-item index="/department" v-if="isAdmin">
          <i class="el-icon-office-building"></i>
          <span slot="title">院系管理</span>
        </el-menu-item>
        <el-menu-item index="/course">
          <i class="el-icon-notebook-2"></i>
          <span slot="title">课程管理</span>
        </el-menu-item>
        <el-menu-item index="/attendance">
          <i class="el-icon-date"></i>
          <span slot="title">考勤管理</span>
        </el-menu-item>
        <el-menu-item index="/salary" v-if="isAdmin || isDept">
          <i class="el-icon-money"></i>
          <span slot="title">薪酬管理</span>
        </el-menu-item>
        <el-menu-item index="/evaluation">
          <i class="el-icon-star-off"></i>
          <span slot="title">教学评价</span>
        </el-menu-item>
        <el-menu-item index="/notice">
          <i class="el-icon-bell"></i>
          <span slot="title">通知公告</span>
        </el-menu-item>
        <el-menu-item index="/my-attendance" v-if="isTeacher">
          <i class="el-icon-date"></i>
          <span slot="title">我的考勤</span>
        </el-menu-item>
        <el-menu-item index="/my-salary" v-if="isTeacher">
          <i class="el-icon-money"></i>
          <span slot="title">我的薪酬</span>
        </el-menu-item>
        <el-menu-item index="/my-evaluation" v-if="isTeacher">
          <i class="el-icon-star-off"></i>
          <span slot="title">我的评价</span>
        </el-menu-item>
        <el-menu-item index="/approval" v-if="isAdmin || isDept">
          <i class="el-icon-s-check"></i>
          <span slot="title">聘用审批</span>
        </el-menu-item>
        <el-menu-item index="/contract" v-if="isAdmin || isDept">
          <i class="el-icon-document"></i>
          <span slot="title">聘用合同</span>
        </el-menu-item>
        <el-menu-item index="/user" v-if="isAdmin">
          <i class="el-icon-s-custom"></i>
          <span slot="title">用户管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <!-- 顶部导航 -->
      <el-header style="background: #fff; border-bottom: 1px solid #eef1f6; display: flex; align-items: center; justify-content: space-between; padding: 0 24px; box-shadow: 0 1px 4px rgba(0,0,0,0.04);">
        <div style="display: flex; align-items: center;">
          <i :class="collapse ? 'el-icon-s-unfold' : 'el-icon-s-fold'" style="font-size: 20px; cursor: pointer;" @click="toggleCollapse"></i>
          <el-breadcrumb separator="/" style="margin-left: 15px;">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-if="$route.meta.title">{{ $route.meta.title }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div style="display: flex; align-items: center;">
          <span style="margin-right: 15px; color: #606266;">{{ userInfo.realName || userInfo.username }}</span>
          <el-dropdown @command="handleCommand">
            <el-avatar :size="36" icon="el-icon-user-solid"></el-avatar>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="profile">个人中心</el-dropdown-item>
              <el-dropdown-item command="password">修改密码</el-dropdown-item>
              <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 主内容区 -->
      <el-main style="background-color: #f0f2f5; padding: 15px;">
        <router-view />
      </el-main>
    </el-container>

    <!-- 修改密码对话框 -->
    <el-dialog title="修改密码" :visible.sync="pwdDialogVisible" width="400px">
      <el-form :model="pwdForm" :rules="pwdRules" ref="pwdForm" label-width="80px">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="pwdForm.oldPassword" type="password" show-password></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="pwdForm.newPassword" type="password" show-password></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="pwdForm.confirmPassword" type="password" show-password></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="pwdDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitPassword">确定</el-button>
      </div>
    </el-dialog>
  </el-container>
</template>

<script>
import { changePassword } from '@/api'

export default {
  name: 'Layout',
  data() {
    const validateConfirm = (rule, value, callback) => {
      if (value !== this.pwdForm.newPassword) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      collapse: false,
      pwdDialogVisible: false,
      pwdForm: { oldPassword: '', newPassword: '', confirmPassword: '' },
      pwdRules: {
        oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
        newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }, { min: 6, message: '密码长度不少于6位', trigger: 'blur' }],
        confirmPassword: [{ required: true, message: '请确认密码', trigger: 'blur' }, { validator: validateConfirm, trigger: 'blur' }]
      }
    }
  },
  computed: {
    userInfo() {
      return this.$store.state.userInfo
    },
    isAdmin() {
      return this.userInfo.role === 'ADMIN'
    },
    isDept() {
      return this.userInfo.role === 'DEPARTMENT'
    },
    isTeacher() {
      return this.userInfo.role === 'TEACHER'
    }
  },
  methods: {
    toggleCollapse() {
      this.collapse = !this.collapse
    },
    handleCommand(cmd) {
      if (cmd === 'logout') {
        this.$confirm('确定退出登录吗？', '提示', { type: 'warning' }).then(() => {
          this.$store.dispatch('logout')
          this.$router.push('/login')
        }).catch(() => {})
      } else if (cmd === 'profile') {
        this.$router.push('/profile')
      } else if (cmd === 'password') {
        this.pwdDialogVisible = true
        this.pwdForm = { oldPassword: '', newPassword: '', confirmPassword: '' }
      }
    },
    submitPassword() {
      this.$refs.pwdForm.validate(valid => {
        if (valid) {
          changePassword({ oldPassword: this.pwdForm.oldPassword, newPassword: this.pwdForm.newPassword }).then(() => {
            this.$message.success('密码修改成功，请重新登录')
            this.pwdDialogVisible = false
            this.$store.dispatch('logout')
            this.$router.push('/login')
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.logo {
  height: 60px;
  border-bottom: 1px solid rgba(255,255,255,0.06);
  background: rgba(0,0,0,0.15);
}
.el-menu {
  border-right: none;
  padding: 8px 0;
}
.el-menu-item {
  margin: 3px 10px;
  border-radius: 8px;
  height: 44px;
  line-height: 44px;
  font-size: 14px;
  transition: all 0.2s ease;
}
.el-menu-item i {
  margin-right: 6px;
  font-size: 16px;
}
.el-menu-item.is-active {
  background: linear-gradient(135deg, rgba(96,165,250,0.2) 0%, rgba(96,165,250,0.1) 100%) !important;
  color: #60a5fa !important;
  font-weight: 500;
  box-shadow: inset 3px 0 0 #60a5fa;
}
.el-menu-item:hover:not(.is-active) {
  background: rgba(255,255,255,0.06) !important;
  color: #e2e8f0 !important;
}
</style>
