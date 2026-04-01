<template>
  <div class="page-container">
    <el-card>
      <div class="search-bar">
        <el-select v-model="query.teacherId" placeholder="选择教师" clearable @change="loadData">
          <el-option v-for="t in teacherList" :key="t.id" :label="t.name" :value="t.id"></el-option>
        </el-select>
        <el-date-picker v-model="query.month" type="month" value-format="yyyy-MM" placeholder="选择月份" clearable @change="loadData"></el-date-picker>
        <el-select v-model="query.status" placeholder="状态" clearable @change="loadData">
          <el-option label="待审核" value="PENDING"></el-option>
          <el-option label="已审核" value="APPROVED"></el-option>
          <el-option label="已发放" value="PAID"></el-option>
        </el-select>
        <el-button type="primary" icon="el-icon-search" @click="loadData">搜索</el-button>
        <el-button type="success" icon="el-icon-plus" @click="openDialog(null)" v-if="canEdit">新增薪酬</el-button>
        <el-button type="warning" icon="el-icon-download" @click="handleExport">导出Excel</el-button>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" empty-text="暂无数据">
        <el-table-column prop="teacherName" label="教师" width="100"></el-table-column>
        <el-table-column prop="month" label="月份" width="100"></el-table-column>
        <el-table-column prop="baseSalary" label="基本工资" width="100"></el-table-column>
        <el-table-column prop="totalHours" label="总课时" width="80"></el-table-column>
        <el-table-column prop="hourRate" label="课时费" width="80"></el-table-column>
        <el-table-column prop="bonus" label="奖金" width="80"></el-table-column>
        <el-table-column prop="deduction" label="扣款" width="80"></el-table-column>
        <el-table-column prop="totalSalary" label="实发金额" width="100">
          <template slot-scope="scope">
            <span style="color: #e6a23c; font-weight: bold;">{{ scope.row.totalSalary }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90">
          <template slot-scope="scope">
            <el-tag :type="salaryStatusType(scope.row.status)">{{ salaryStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="payDate" label="发放日期" width="120"></el-table-column>
        <el-table-column prop="remark" label="备注" show-overflow-tooltip></el-table-column>
        <el-table-column label="操作" width="260" fixed="right" v-if="canEdit">
          <template slot-scope="scope">
            <el-button size="mini" @click="openDialog(scope.row)">编辑</el-button>
            <el-button size="mini" type="success" v-if="scope.row.status==='PENDING'" @click="handleApprove(scope.row.id)">审核</el-button>
            <el-button size="mini" type="warning" v-if="scope.row.status==='APPROVED'" @click="handlePay(scope.row.id)">发放</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-bar">
        <el-pagination background layout="total, sizes, prev, pager, next" :total="total" :page-size.sync="query.size" :current-page.sync="query.current" @current-change="loadData" @size-change="loadData"></el-pagination>
      </div>
    </el-card>

    <el-dialog :title="form.id ? '编辑薪酬' : '新增薪酬'" :visible.sync="dialogVisible" width="700px">
      <el-form :model="form" :rules="rules" ref="form" label-width="90px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="教师" prop="teacherId">
              <el-select v-model="form.teacherId" style="width:100%">
                <el-option v-for="t in teacherList" :key="t.id" :label="t.name" :value="t.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="月份" prop="month">
              <el-date-picker v-model="form.month" type="month" value-format="yyyy-MM" style="width:100%"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="基本工资">
              <el-input-number v-model="form.baseSalary" :precision="2" :min="0" style="width:100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总课时">
              <el-input-number v-model="form.totalHours" :precision="1" :min="0" style="width:100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="课时费">
              <el-input-number v-model="form.hourRate" :precision="2" :min="0" style="width:100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="奖金">
              <el-input-number v-model="form.bonus" :precision="2" :min="0" style="width:100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="扣款">
              <el-input-number v-model="form.deduction" :precision="2" :min="0" style="width:100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="实发金额">
              <el-input-number v-model="form.totalSalary" :precision="2" :min="0" style="width:100%"></el-input-number>
            </el-form-item>
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
import { getSalaryPage, addSalary, updateSalary, deleteSalary, approveSalary, paySalary, getTeacherList, exportSalary } from '@/api'

export default {
  name: 'Salary',
  data() {
    return {
      query: { current: 1, size: 10, teacherId: null, month: '', status: '' },
      tableData: [], total: 0, loading: false,
      dialogVisible: false, teacherList: [],
      form: {},
      rules: {
        teacherId: [{ required: true, message: '请选择教师', trigger: 'change' }],
        month: [{ required: true, message: '请选择月份', trigger: 'change' }]
      }
    }
  },
  computed: {
    canEdit() {
      const role = this.$store.state.userInfo.role
      return role === 'ADMIN' || role === 'DEPARTMENT'
    }
  },
  mounted() {
    this.loadTeachers()
    this.loadData()
  },
  methods: {
    loadTeachers() {
      getTeacherList().then(res => { this.teacherList = res.data })
    },
    loadData() {
      this.loading = true
      getSalaryPage(this.query).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      }).finally(() => { this.loading = false })
    },
    salaryStatusType(s) {
      return { PENDING: 'warning', APPROVED: 'success', PAID: '' }[s] || 'info'
    },
    salaryStatusText(s) {
      return { PENDING: '待审核', APPROVED: '已审核', PAID: '已发放' }[s] || s
    },
    openDialog(row) {
      this.form = row ? { ...row } : { status: 'PENDING', baseSalary: 0, totalHours: 0, hourRate: 0, bonus: 0, deduction: 0, totalSalary: 0 }
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.form && this.$refs.form.clearValidate() })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const api = this.form.id ? updateSalary : addSalary
          api(this.form).then(() => {
            this.$message.success('操作成功')
            this.dialogVisible = false
            this.loadData()
          })
        }
      })
    },
    handleApprove(id) {
      this.$confirm('确定审核通过该薪酬记录？', '提示', { type: 'warning' }).then(() => {
        approveSalary(id).then(() => { this.$message.success('审核通过'); this.loadData() })
      }).catch(() => {})
    },
    handlePay(id) {
      this.$confirm('确定发放该薪酬？', '提示', { type: 'warning' }).then(() => {
        paySalary(id).then(() => { this.$message.success('已发放'); this.loadData() })
      }).catch(() => {})
    },
    handleExport() {
      exportSalary().then(res => {
        const blob = new Blob([res.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = '薪酬记录.xlsx'
        link.click()
        URL.revokeObjectURL(link.href)
      })
    },
    handleDelete(id) {
      this.$confirm('确定删除该薪酬记录？', '提示', { type: 'warning' }).then(() => {
        deleteSalary(id).then(() => { this.$message.success('已删除'); this.loadData() })
      }).catch(() => {})
    }
  }
}
</script>
