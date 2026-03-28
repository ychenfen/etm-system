<template>
  <div class="page-container">
    <el-card>
      <div class="search-bar">
        <el-input v-model="query.keyword" placeholder="课程名称/编码" clearable @clear="loadData"></el-input>
        <el-select v-model="query.departmentId" placeholder="选择院系" clearable @change="loadData">
          <el-option v-for="d in deptList" :key="d.id" :label="d.name" :value="d.id"></el-option>
        </el-select>
        <el-button type="primary" icon="el-icon-search" @click="loadData">搜索</el-button>
        <el-button type="success" icon="el-icon-plus" @click="openDialog(null)">新增课程</el-button>
      </div>
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="name" label="课程名称" width="150"></el-table-column>
        <el-table-column prop="code" label="课程编码" width="100"></el-table-column>
        <el-table-column prop="teacherName" label="授课教师" width="100"></el-table-column>
        <el-table-column prop="departmentName" label="院系" width="160" show-overflow-tooltip></el-table-column>
        <el-table-column prop="semester" label="学期" width="120"></el-table-column>
        <el-table-column prop="credit" label="学分" width="60"></el-table-column>
        <el-table-column prop="hours" label="学时" width="60"></el-table-column>
        <el-table-column prop="className" label="授课班级" width="120"></el-table-column>
        <el-table-column prop="classroom" label="教室" width="80"></el-table-column>
        <el-table-column prop="schedule" label="上课时间" width="180" show-overflow-tooltip></el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status==='ACTIVE'?'success':scope.row.status==='COMPLETED'?'info':'danger'" size="small">{{ {ACTIVE:'进行中',COMPLETED:'已完成',CANCELLED:'已取消'}[scope.row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
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
    <el-dialog :title="form.id ? '编辑课程' : '新增课程'" :visible.sync="dialogVisible" width="650px">
      <el-form :model="form" :rules="rules" ref="form" label-width="90px">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="课程名称" prop="name"><el-input v-model="form.name"></el-input></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="课程编码" prop="code"><el-input v-model="form.code"></el-input></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="授课教师" prop="teacherId"><el-select v-model="form.teacherId" filterable style="width:100%"><el-option v-for="t in teacherList" :key="t.id" :label="t.name" :value="t.id"></el-option></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="所属院系" prop="departmentId"><el-select v-model="form.departmentId" style="width:100%"><el-option v-for="d in deptList" :key="d.id" :label="d.name" :value="d.id"></el-option></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="学期"><el-input v-model="form.semester" placeholder="如 2025-2026-2"></el-input></el-form-item></el-col>
          <el-col :span="6"><el-form-item label="学分"><el-input-number v-model="form.credit" :min="0.5" :step="0.5" :max="10" style="width:100%"></el-input-number></el-form-item></el-col>
          <el-col :span="6"><el-form-item label="学时"><el-input-number v-model="form.hours" :min="1" style="width:100%"></el-input-number></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="授课班级"><el-input v-model="form.className"></el-input></el-form-item></el-col>
          <el-col :span="6"><el-form-item label="人数"><el-input-number v-model="form.studentCount" :min="1" style="width:100%"></el-input-number></el-form-item></el-col>
          <el-col :span="6"><el-form-item label="教室"><el-input v-model="form.classroom"></el-input></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="上课时间"><el-input v-model="form.schedule" placeholder="如 周一3-4节,周三5-6节"></el-input></el-form-item></el-col>
        </el-row>
      </el-form>
      <div slot="footer"><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="submitForm">确定</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import { getCoursePage, addCourse, updateCourse, deleteCourse, getDeptList, getTeacherList } from '@/api'
export default {
  name: 'Course',
  data() {
    return {
      query: { current: 1, size: 10, keyword: '', departmentId: null }, tableData: [], total: 0, loading: false,
      dialogVisible: false, deptList: [], teacherList: [], form: {},
      rules: { name: [{ required: true, message: '请输入课程名称', trigger: 'blur' }], code: [{ required: true, message: '请输入编码', trigger: 'blur' }] }
    }
  },
  mounted() { getDeptList().then(r => { this.deptList = r.data }); getTeacherList().then(r => { this.teacherList = r.data }); this.loadData() },
  methods: {
    loadData() { this.loading = true; getCoursePage(this.query).then(r => { this.tableData = r.data.records; this.total = r.data.total }).finally(() => { this.loading = false }) },
    openDialog(row) { this.form = row ? { ...row } : { status: 'ACTIVE' }; this.dialogVisible = true; this.$nextTick(() => { this.$refs.form && this.$refs.form.clearValidate() }) },
    submitForm() { this.$refs.form.validate(v => { if (v) { (this.form.id ? updateCourse : addCourse)(this.form).then(() => { this.$message.success('操作成功'); this.dialogVisible = false; this.loadData() }) } }) },
    handleDelete(id) { this.$confirm('确定删除？', '提示', { type: 'warning' }).then(() => { deleteCourse(id).then(() => { this.$message.success('已删除'); this.loadData() }) }).catch(() => {}) }
  }
}
</script>
