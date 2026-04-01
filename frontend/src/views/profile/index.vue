<template>
  <div class="page-container">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card>
          <div class="profile-header">
            <el-avatar :size="80" icon="el-icon-user-solid" style="background: #409EFF;"></el-avatar>
            <h3 style="margin: 15px 0 5px;">{{ userInfo.realName || userInfo.username }}</h3>
            <el-tag :type="roleType(userInfo.role)" size="small">{{ roleText(userInfo.role) }}</el-tag>
          </div>
          <el-divider></el-divider>
          <div class="profile-info">
            <p><i class="el-icon-user"></i> 用户名：{{ userInfo.username }}</p>
            <p><i class="el-icon-phone"></i> 手机：{{ userInfo.phone || '未设置' }}</p>
            <p><i class="el-icon-message"></i> 邮箱：{{ userInfo.email || '未设置' }}</p>
            <p><i class="el-icon-office-building"></i> 院系：{{ userInfo.departmentName || '无' }}</p>
            <p><i class="el-icon-time"></i> 注册时间：{{ userInfo.createTime }}</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card>
          <div slot="header"><b>编辑个人信息</b></div>
          <el-form :model="form" :rules="rules" ref="form" label-width="80px" style="max-width: 500px;">
            <el-form-item label="姓名" prop="realName">
              <el-input v-model="form.realName"></el-input>
            </el-form-item>
            <el-form-item label="手机号">
              <el-input v-model="form.phone"></el-input>
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="form.email"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveProfile">保存修改</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card style="margin-top: 15px;" v-if="teacherInfo">
          <div slot="header"><b>教师详细信息</b></div>
          <el-descriptions :column="2" border size="medium">
            <el-descriptions-item label="姓名">{{ teacherInfo.name }}</el-descriptions-item>
            <el-descriptions-item label="性别">{{ teacherInfo.gender }}</el-descriptions-item>
            <el-descriptions-item label="职称">{{ teacherInfo.title }}</el-descriptions-item>
            <el-descriptions-item label="学历">{{ teacherInfo.education }}</el-descriptions-item>
            <el-descriptions-item label="学位">{{ teacherInfo.degree }}</el-descriptions-item>
            <el-descriptions-item label="工作单位">{{ teacherInfo.workUnit }}</el-descriptions-item>
            <el-descriptions-item label="专业特长">{{ teacherInfo.speciality }}</el-descriptions-item>
            <el-descriptions-item label="聘用状态">
              <el-tag :type="statusType(teacherInfo.hireStatus)">{{ statusText(teacherInfo.hireStatus) }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="聘期起">{{ teacherInfo.hireStartDate }}</el-descriptions-item>
            <el-descriptions-item label="聘期止">{{ teacherInfo.hireEndDate }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getUserInfo, updateUser, getTeacherById } from '@/api'

export default {
  name: 'Profile',
  data() {
    return {
      userInfo: {},
      teacherInfo: null,
      form: {},
      rules: {
        realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadProfile()
  },
  methods: {
    loadProfile() {
      getUserInfo().then(res => {
        this.userInfo = res.data
        this.form = { id: res.data.id, realName: res.data.realName, phone: res.data.phone, email: res.data.email }
        if (res.data.role === 'TEACHER') {
          this.loadTeacherInfo()
        }
      })
    },
    loadTeacherInfo() {
      const teacherId = this.userInfo.teacherId
      if (teacherId) {
        getTeacherById(teacherId).then(res => {
          this.teacherInfo = res.data
        })
      }
    },
    saveProfile() {
      this.$refs.form.validate(valid => {
        if (valid) {
          updateUser(this.form).then(() => {
            this.$message.success('个人信息已更新')
            this.loadProfile()
            const storeUser = { ...this.$store.state.userInfo, realName: this.form.realName, phone: this.form.phone, email: this.form.email }
            this.$store.commit('SET_USER', storeUser)
          })
        }
      })
    },
    roleType(r) {
      return { ADMIN: 'danger', DEPARTMENT: 'warning', TEACHER: '' }[r] || 'info'
    },
    roleText(r) {
      return { ADMIN: '管理员', DEPARTMENT: '院系管理员', STAFF: '教务人员', TEACHER: '外聘教师' }[r] || r
    },
    statusType(s) {
      return { PENDING: 'warning', APPROVED: 'success', REJECTED: 'danger', EXPIRED: 'info' }[s] || 'info'
    },
    statusText(s) {
      return { PENDING: '待审核', APPROVED: '已聘用', REJECTED: '已拒绝', EXPIRED: '已到期' }[s] || s
    }
  }
}
</script>

<style scoped>
.profile-header {
  text-align: center;
  padding: 30px 0 20px;
  background: linear-gradient(135deg, #e0ecff 0%, #f0f4ff 100%);
  border-radius: 12px 12px 0 0;
  margin: -20px -20px 20px -20px;
}
.profile-info {
  padding: 0 10px;
}
.profile-info p {
  padding: 10px 14px;
  color: #4b5563;
  font-size: 14px;
  border-bottom: 1px solid #f3f4f6;
  margin: 0;
  display: flex;
  align-items: center;
}
.profile-info p:last-child {
  border-bottom: none;
}
.profile-info p i {
  margin-right: 10px;
  color: #60a5fa;
  font-size: 16px;
  width: 20px;
  text-align: center;
}
</style>
