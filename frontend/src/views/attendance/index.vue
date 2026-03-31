<template>
  <div class="page-container">
    <el-card>
      <div class="search-bar">
        <el-select v-model="query.teacherId" placeholder="选择教师" clearable @change="loadData">
          <el-option v-for="t in teacherList" :key="t.id" :label="t.name" :value="t.id"></el-option>
        </el-select>
        <el-select v-model="query.courseId" placeholder="选择课程" clearable @change="loadData">
          <el-option v-for="c in courseList" :key="c.id" :label="c.name" :value="c.id"></el-option>
        </el-select>
        <el-select v-model="query.status" placeholder="考勤状态" clearable @change="loadData">
          <el-option label="正常" value="NORMAL"></el-option>
          <el-option label="迟到" value="LATE"></el-option>
          <el-option label="早退" value="EARLY_LEAVE"></el-option>
          <el-option label="缺勤" value="ABSENT"></el-option>
          <el-option label="请假" value="LEAVE"></el-option>
        </el-select>
        <el-button type="primary" icon="el-icon-search" @click="loadData">搜索</el-button>
        <el-button type="success" icon="el-icon-plus" @click="openDialog(null)" v-if="canEdit">新增考勤</el-button>
        <el-button type="warning" icon="el-icon-download" @click="handleExport">导出Excel</el-button>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" empty-text="暂无数据">
        <el-table-column prop="teacherName" label="教师" width="100"></el-table-column>
        <el-table-column prop="courseName" label="课程" width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="attendanceDate" label="考勤日期" width="120"></el-table-column>
        <el-table-column prop="checkInTime" label="签到时间" width="170"></el-table-column>
        <el-table-column prop="checkOutTime" label="签退时间" width="170"></el-table-column>
        <el-table-column prop="actualHours" label="实际课时" width="90"></el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="attendStatusType(scope.row.status)">{{ attendStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" show-overflow-tooltip></el-table-column>
        <el-table-column label="操作" width="160" fixed="right" v-if="canEdit">
          <template slot-scope="scope">
            <el-button size="mini" @click="openDialog(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-bar">
        <el-pagination background layout="total, sizes, prev, pager, next" :total="total" :page-size.sync="query.size" :current-page.sync="query.current" @current-change="loadData" @size-change="loadData"></el-pagination>
      </div>
    </el-card>

    <el-dialog :title="form.id ? '编辑考勤' : '新增考勤'" :visible.sync="dialogVisible" width="600px">
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
            <el-form-item label="课程" prop="courseId">
              <el-select v-model="form.courseId" style="width:100%">
                <el-option v-for="c in courseList" :key="c.id" :label="c.name" :value="c.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="考勤日期" prop="attendanceDate">
              <el-date-picker v-model="form.attendanceDate" type="date" value-format="yyyy-MM-dd" style="width:100%"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-select v-model="form.status" style="width:100%">
                <el-option label="正常" value="NORMAL"></el-option>
                <el-option label="迟到" value="LATE"></el-option>
                <el-option label="早退" value="EARLY_LEAVE"></el-option>
                <el-option label="缺勤" value="ABSENT"></el-option>
                <el-option label="请假" value="LEAVE"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="签到时间">
              <el-date-picker v-model="form.checkInTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" style="width:100%"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="签退时间">
              <el-date-picker v-model="form.checkOutTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" style="width:100%"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="实际课时">
              <el-input-number v-model="form.actualHours" :precision="1" :min="0" :max="12" style="width:100%"></el-input-number>
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
import { getAttendancePage, addAttendance, updateAttendance, deleteAttendance, getTeacherList, getCourseList, exportAttendance } from '@/api'

export default {
  name: 'Attendance',
  data() {
    return {
      query: { current: 1, size: 10, teacherId: null, courseId: null, status: '' },
      tableData: [], total: 0, loading: false,
      dialogVisible: false, teacherList: [], courseList: [],
      form: {},
      rules: {
        teacherId: [{ required: true, message: '请选择教师', trigger: 'change' }],
        courseId: [{ required: true, message: '请选择课程', trigger: 'change' }],
        attendanceDate: [{ required: true, message: '请选择日期', trigger: 'change' }]
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
    this.loadOptions()
    this.loadData()
  },
  methods: {
    loadOptions() {
      getTeacherList().then(res => { this.teacherList = res.data })
      getCourseList().then(res => { this.courseList = res.data })
    },
    loadData() {
      this.loading = true
      getAttendancePage(this.query).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      }).finally(() => { this.loading = false })
    },
    attendStatusType(s) {
      return { NORMAL: 'success', LATE: 'warning', EARLY_LEAVE: 'warning', ABSENT: 'danger', LEAVE: 'info' }[s] || 'info'
    },
    attendStatusText(s) {
      return { NORMAL: '正常', LATE: '迟到', EARLY_LEAVE: '早退', ABSENT: '缺勤', LEAVE: '请假' }[s] || s
    },
    openDialog(row) {
      this.form = row ? { ...row } : { status: 'NORMAL' }
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.form && this.$refs.form.clearValidate() })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const api = this.form.id ? updateAttendance : addAttendance
          api(this.form).then(() => {
            this.$message.success('操作成功')
            this.dialogVisible = false
            this.loadData()
          })
        }
      })
    },
    handleExport() {
      exportAttendance().then(res => {
        const blob = new Blob([res.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = '考勤记录.xlsx'
        link.click()
        URL.revokeObjectURL(link.href)
      })
    },
    handleDelete(id) {
      this.$confirm('确定删除该考勤记录？', '提示', { type: 'warning' }).then(() => {
        deleteAttendance(id).then(() => { this.$message.success('已删除'); this.loadData() })
      }).catch(() => {})
    }
  }
}
</script>
