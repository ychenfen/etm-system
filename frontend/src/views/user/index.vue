<template>
  <div class="page-container">
    <el-card>
      <div class="search-bar">
        <el-input v-model="query.keyword" placeholder="用户名/姓名/手机" clearable @clear="loadData" style="width:200px"></el-input>
        <el-select v-model="query.role" placeholder="角色" clearable @change="loadData">
          <el-option label="管理员" value="ADMIN"></el-option>
          <el-option label="院系管理员" value="DEPARTMENT"></el-option>
          <el-option label="外聘教师" value="TEACHER"></el-option>
        </el-select>
        <el-button type="primary" icon="el-icon-search" @click="loadData">搜索</el-button>
        <el-button type="success" icon="el-icon-plus" @click="openDialog(null)">新增用户</el-button>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" empty-text="暂无数据">
        <el-table-column prop="username" label="用户名" width="120"></el-table-column>
        <el-table-column prop="realName" label="姓名" width="100"></el-table-column>
        <el-table-column prop="phone" label="手机号" width="130"></el-table-column>
        <el-table-column prop="email" label="邮箱" width="180" show-overflow-tooltip></el-table-column>
        <el-table-column prop="role" label="角色" width="100">
          <template slot-scope="scope">
            <el-tag :type="roleType(scope.row.role)" size="small">{{ roleText(scope.row.role) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="departmentName" label="所属院系" width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="scope">
            <el-switch :value="scope.row.status === 1" active-color="#13ce66" inactive-color="#ff4949" @change="handleStatusChange(scope.row)"></el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170"></el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="openDialog(scope.row)">编辑</el-button>
            <el-button size="mini" type="warning" @click="handleResetPwd(scope.row.id)">重置密码</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-bar">
        <el-pagination background layout="total, sizes, prev, pager, next" :total="total" :page-size.sync="query.size" :current-page.sync="query.current" @current-change="loadData" @size-change="loadData"></el-pagination>
      </div>
    </el-card>

    <el-dialog :title="form.id ? '编辑用户' : '新增用户'" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" :rules="rules" ref="form" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="form.username" :disabled="!!form.id"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="!form.id">
            <el-form-item label="密码" prop="password">
              <el-input v-model="form.password" type="password" show-password placeholder="默认123456"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="realName">
              <el-input v-model="form.realName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号">
              <el-input v-model="form.phone"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱">
              <el-input v-model="form.email"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="角色" prop="role">
              <el-select v-model="form.role" style="width:100%">
                <el-option label="管理员" value="ADMIN"></el-option>
                <el-option label="院系管理员" value="DEPARTMENT"></el-option>
                <el-option label="外聘教师" value="TEACHER"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属院系">
              <el-select v-model="form.departmentId" clearable style="width:100%">
                <el-option v-for="d in deptList" :key="d.id" :label="d.name" :value="d.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getUserPage, addUser, updateUser, deleteUser, resetPassword, changeUserStatus, getDeptList } from '@/api'

export default {
  name: 'User',
  data() {
    return {
      query: { current: 1, size: 10, keyword: '', role: '' },
      tableData: [], total: 0, loading: false,
      dialogVisible: false, deptList: [],
      form: {},
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        role: [{ required: true, message: '请选择角色', trigger: 'change' }]
      }
    }
  },
  mounted() {
    this.loadDepts()
    this.loadData()
  },
  methods: {
    loadDepts() {
      getDeptList().then(res => { this.deptList = res.data })
    },
    loadData() {
      this.loading = true
      getUserPage(this.query).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      }).finally(() => { this.loading = false })
    },
    roleType(r) {
      return { ADMIN: 'danger', DEPARTMENT: 'warning', TEACHER: '' }[r] || 'info'
    },
    roleText(r) {
      return { ADMIN: '管理员', DEPARTMENT: '院系管理员', TEACHER: '外聘教师' }[r] || r
    },
    openDialog(row) {
      this.form = row ? { ...row, password: undefined } : { status: 1, password: '123456' }
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.form && this.$refs.form.clearValidate() })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const api = this.form.id ? updateUser : addUser
          api(this.form).then(() => {
            this.$message.success('操作成功')
            this.dialogVisible = false
            this.loadData()
          })
        }
      })
    },
    handleStatusChange(row) {
      const newStatus = row.status === 1 ? 0 : 1
      changeUserStatus(row.id, newStatus).then(() => {
        this.$message.success('状态已更新')
        this.loadData()
      })
    },
    handleResetPwd(id) {
      this.$confirm('确定重置该用户密码为123456？', '提示', { type: 'warning' }).then(() => {
        resetPassword(id).then(() => { this.$message.success('密码已重置为123456') })
      }).catch(() => {})
    },
    handleDelete(id) {
      this.$confirm('确定删除该用户？', '提示', { type: 'warning' }).then(() => {
        deleteUser(id).then(() => { this.$message.success('已删除'); this.loadData() })
      }).catch(() => {})
    }
  }
}
</script>
