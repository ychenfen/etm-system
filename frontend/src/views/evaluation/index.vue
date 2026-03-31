<template>
  <div class="page-container">
    <el-card>
      <div class="search-bar">
        <el-select v-model="query.teacherId" placeholder="选择教师" clearable @change="loadData">
          <el-option v-for="t in teacherList" :key="t.id" :label="t.name" :value="t.id"></el-option>
        </el-select>
        <el-input v-model="query.semester" placeholder="学期(如2024-2025-1)" clearable @clear="loadData" style="width:200px"></el-input>
        <el-button type="primary" icon="el-icon-search" @click="loadData">搜索</el-button>
        <el-button type="success" icon="el-icon-plus" @click="openDialog(null)" v-if="canEdit">新增评价</el-button>
        <el-button type="warning" icon="el-icon-download" @click="handleExport">导出Excel</el-button>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" empty-text="暂无数据">
        <el-table-column prop="teacherName" label="教师" width="100"></el-table-column>
        <el-table-column prop="courseName" label="课程" width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="semester" label="学期" width="140"></el-table-column>
        <el-table-column prop="teachingScore" label="教学评分" width="90"></el-table-column>
        <el-table-column prop="attendanceScore" label="考勤评分" width="90"></el-table-column>
        <el-table-column prop="studentScore" label="学生评分" width="90"></el-table-column>
        <el-table-column prop="totalScore" label="总评分" width="80">
          <template slot-scope="scope">
            <span :style="{ color: scope.row.totalScore >= 90 ? '#67c23a' : scope.row.totalScore >= 60 ? '#e6a23c' : '#f56c6c', fontWeight: 'bold' }">{{ scope.row.totalScore }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="evaluator" label="评价人" width="100"></el-table-column>
        <el-table-column prop="evaluationDate" label="评价日期" width="120"></el-table-column>
        <el-table-column prop="comment" label="评语" show-overflow-tooltip></el-table-column>
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

    <el-dialog :title="form.id ? '编辑评价' : '新增评价'" :visible.sync="dialogVisible" width="700px">
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
            <el-form-item label="学期" prop="semester">
              <el-input v-model="form.semester" placeholder="如2024-2025-1"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评价日期">
              <el-date-picker v-model="form.evaluationDate" type="date" value-format="yyyy-MM-dd" style="width:100%"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="教学评分">
              <el-input-number v-model="form.teachingScore" :precision="1" :min="0" :max="100" style="width:100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="考勤评分">
              <el-input-number v-model="form.attendanceScore" :precision="1" :min="0" :max="100" style="width:100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学生评分">
              <el-input-number v-model="form.studentScore" :precision="1" :min="0" :max="100" style="width:100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总评分">
              <el-input-number v-model="form.totalScore" :precision="1" :min="0" :max="100" style="width:100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评价人">
              <el-input v-model="form.evaluator"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="评语"><el-input v-model="form.comment" type="textarea" :rows="3"></el-input></el-form-item>
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
import { getEvalPage, addEval, updateEval, deleteEval, getTeacherList, getCourseList, exportEvaluation } from '@/api'

export default {
  name: 'Evaluation',
  data() {
    return {
      query: { current: 1, size: 10, teacherId: null, semester: '' },
      tableData: [], total: 0, loading: false,
      dialogVisible: false, teacherList: [], courseList: [],
      form: {},
      rules: {
        teacherId: [{ required: true, message: '请选择教师', trigger: 'change' }],
        courseId: [{ required: true, message: '请选择课程', trigger: 'change' }],
        semester: [{ required: true, message: '请输入学期', trigger: 'blur' }]
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
      getEvalPage(this.query).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      }).finally(() => { this.loading = false })
    },
    openDialog(row) {
      this.form = row ? { ...row } : { teachingScore: 0, attendanceScore: 0, studentScore: 0, totalScore: 0 }
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.form && this.$refs.form.clearValidate() })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const api = this.form.id ? updateEval : addEval
          api(this.form).then(() => {
            this.$message.success('操作成功')
            this.dialogVisible = false
            this.loadData()
          })
        }
      })
    },
    handleExport() {
      exportEvaluation().then(res => {
        const blob = new Blob([res.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = '评价记录.xlsx'
        link.click()
        URL.revokeObjectURL(link.href)
      })
    },
    handleDelete(id) {
      this.$confirm('确定删除该评价记录？', '提示', { type: 'warning' }).then(() => {
        deleteEval(id).then(() => { this.$message.success('已删除'); this.loadData() })
      }).catch(() => {})
    }
  }
}
</script>
