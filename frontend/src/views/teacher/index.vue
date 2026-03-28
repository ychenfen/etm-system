<template>
  <div class="page-container">
    <el-card>
      <div class="search-bar">
        <el-input v-model="query.keyword" placeholder="姓名/电话/单位" clearable @clear="loadData"></el-input>
        <el-select v-model="query.departmentId" placeholder="选择院系" clearable @change="loadData">
          <el-option v-for="d in deptList" :key="d.id" :label="d.name" :value="d.id"></el-option>
        </el-select>
        <el-select v-model="query.hireStatus" placeholder="聘用状态" clearable @change="loadData">
          <el-option label="待审核" value="PENDING"></el-option>
          <el-option label="已聘用" value="APPROVED"></el-option>
          <el-option label="已拒绝" value="REJECTED"></el-option>
          <el-option label="已到期" value="EXPIRED"></el-option>
        </el-select>
        <el-button type="primary" icon="el-icon-search" @click="loadData">搜索</el-button>
        <el-button type="success" icon="el-icon-plus" @click="openDialog(null)">新增教师</el-button>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="name" label="姓名" width="90"></el-table-column>
        <el-table-column prop="gender" label="性别" width="60"></el-table-column>
        <el-table-column prop="phone" label="电话" width="120"></el-table-column>
        <el-table-column prop="title" label="职称" width="100"></el-table-column>
        <el-table-column prop="education" label="学历" width="80"></el-table-column>
        <el-table-column prop="workUnit" label="工作单位" width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="departmentName" label="所属院系" width="160" show-overflow-tooltip></el-table-column>
        <el-table-column prop="hireStatus" label="聘用状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="statusType(scope.row.hireStatus)">{{ statusText(scope.row.hireStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="hireStartDate" label="聘期起" width="110"></el-table-column>
        <el-table-column prop="hireEndDate" label="聘期止" width="110"></el-table-column>
        <el-table-column label="操作" width="240" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="openDialog(scope.row)">编辑</el-button>
            <el-button size="mini" type="success" v-if="scope.row.hireStatus==='PENDING'" @click="handleApprove(scope.row.id)">通过</el-button>
            <el-button size="mini" type="warning" v-if="scope.row.hireStatus==='PENDING'" @click="handleReject(scope.row.id)">拒绝</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-bar">
        <el-pagination background layout="total, sizes, prev, pager, next" :total="total" :page-size.sync="query.size" :current-page.sync="query.current" @current-change="loadData" @size-change="loadData"></el-pagination>
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="form.id ? '编辑教师' : '新增教师'" :visible.sync="dialogVisible" width="700px">
      <el-form :model="form" :rules="rules" ref="form" label-width="90px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名" prop="name"><el-input v-model="form.name"></el-input></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别"><el-radio-group v-model="form.gender"><el-radio label="男">男</el-radio><el-radio label="女">女</el-radio></el-radio-group></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电话" prop="phone"><el-input v-model="form.phone"></el-input></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱"><el-input v-model="form.email"></el-input></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身份证号"><el-input v-model="form.idCard"></el-input></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出生日期"><el-date-picker v-model="form.birthDate" type="date" value-format="yyyy-MM-dd" style="width:100%"></el-date-picker></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学历"><el-select v-model="form.education" style="width:100%"><el-option label="本科" value="本科"></el-option><el-option label="研究生" value="研究生"></el-option><el-option label="博士后" value="博士后"></el-option></el-select></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学位"><el-select v-model="form.degree" style="width:100%"><el-option label="学士" value="学士"></el-option><el-option label="硕士" value="硕士"></el-option><el-option label="博士" value="博士"></el-option></el-select></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职称"><el-input v-model="form.title"></el-input></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工作单位"><el-input v-model="form.workUnit"></el-input></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属院系" prop="departmentId"><el-select v-model="form.departmentId" style="width:100%"><el-option v-for="d in deptList" :key="d.id" :label="d.name" :value="d.id"></el-option></el-select></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="专业特长"><el-input v-model="form.speciality"></el-input></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="聘期起"><el-date-picker v-model="form.hireStartDate" type="date" value-format="yyyy-MM-dd" style="width:100%"></el-date-picker></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="聘期止"><el-date-picker v-model="form.hireEndDate" type="date" value-format="yyyy-MM-dd" style="width:100%"></el-date-picker></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="银行账号"><el-input v-model="form.bankAccount"></el-input></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开户银行"><el-input v-model="form.bankName"></el-input></el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" :rows="2"></el-input></el-form-item>
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
import { getTeacherPage, addTeacher, updateTeacher, deleteTeacher, approveTeacher, rejectTeacher, getDeptList } from '@/api'

export default {
  name: 'Teacher',
  data() {
    return {
      query: { current: 1, size: 10, keyword: '', departmentId: null, hireStatus: '' },
      tableData: [], total: 0, loading: false,
      dialogVisible: false, deptList: [],
      form: {},
      rules: {
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入电话', trigger: 'blur' }],
        departmentId: [{ required: true, message: '请选择院系', trigger: 'change' }]
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
      getTeacherPage(this.query).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      }).finally(() => { this.loading = false })
    },
    statusType(s) {
      return { PENDING: 'warning', APPROVED: 'success', REJECTED: 'danger', EXPIRED: 'info' }[s] || 'info'
    },
    statusText(s) {
      return { PENDING: '待审核', APPROVED: '已聘用', REJECTED: '已拒绝', EXPIRED: '已到期' }[s] || s
    },
    openDialog(row) {
      this.form = row ? { ...row } : { gender: '男', hireStatus: 'PENDING' }
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.form && this.$refs.form.clearValidate() })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const api = this.form.id ? updateTeacher : addTeacher
          api(this.form).then(() => {
            this.$message.success('操作成功')
            this.dialogVisible = false
            this.loadData()
          })
        }
      })
    },
    handleApprove(id) {
      this.$confirm('确定审核通过该教师？', '提示', { type: 'warning' }).then(() => {
        approveTeacher(id).then(() => { this.$message.success('已通过'); this.loadData() })
      }).catch(() => {})
    },
    handleReject(id) {
      this.$confirm('确定拒绝该教师？', '提示', { type: 'warning' }).then(() => {
        rejectTeacher(id).then(() => { this.$message.success('已拒绝'); this.loadData() })
      }).catch(() => {})
    },
    handleDelete(id) {
      this.$confirm('确定删除该教师？', '提示', { type: 'warning' }).then(() => {
        deleteTeacher(id).then(() => { this.$message.success('已删除'); this.loadData() })
      }).catch(() => {})
    }
  }
}
</script>
