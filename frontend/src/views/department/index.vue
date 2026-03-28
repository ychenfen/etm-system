<template>
  <div class="page-container">
    <el-card>
      <div class="search-bar">
        <el-input v-model="query.keyword" placeholder="院系名称/编码" clearable @clear="loadData"></el-input>
        <el-button type="primary" icon="el-icon-search" @click="loadData">搜索</el-button>
        <el-button type="success" icon="el-icon-plus" @click="openDialog(null)">新增院系</el-button>
      </div>
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="name" label="院系名称" width="200"></el-table-column>
        <el-table-column prop="code" label="编码" width="100"></el-table-column>
        <el-table-column prop="contactPerson" label="联系人" width="100"></el-table-column>
        <el-table-column prop="contactPhone" label="联系电话" width="130"></el-table-column>
        <el-table-column prop="teacherCount" label="教师数" width="80"></el-table-column>
        <el-table-column prop="description" label="描述" show-overflow-tooltip></el-table-column>
        <el-table-column label="操作" width="160">
          <template slot-scope="scope">
            <el-button size="mini" @click="openDialog(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-bar">
        <el-pagination background layout="total, prev, pager, next" :total="total" :page-size.sync="query.size" :current-page.sync="query.current" @current-change="loadData"></el-pagination>
      </div>
    </el-card>
    <el-dialog :title="form.id ? '编辑院系' : '新增院系'" :visible.sync="dialogVisible" width="500px">
      <el-form :model="form" :rules="rules" ref="form" label-width="80px">
        <el-form-item label="名称" prop="name"><el-input v-model="form.name"></el-input></el-form-item>
        <el-form-item label="编码" prop="code"><el-input v-model="form.code"></el-input></el-form-item>
        <el-form-item label="联系人"><el-input v-model="form.contactPerson"></el-input></el-form-item>
        <el-form-item label="联系电话"><el-input v-model="form.contactPhone"></el-input></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" :rows="3"></el-input></el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getDeptPage, addDept, updateDept, deleteDept } from '@/api'
export default {
  name: 'Department',
  data() {
    return {
      query: { current: 1, size: 10, keyword: '' }, tableData: [], total: 0, loading: false,
      dialogVisible: false, form: {},
      rules: { name: [{ required: true, message: '请输入名称', trigger: 'blur' }], code: [{ required: true, message: '请输入编码', trigger: 'blur' }] }
    }
  },
  mounted() { this.loadData() },
  methods: {
    loadData() {
      this.loading = true
      getDeptPage(this.query).then(res => { this.tableData = res.data.records; this.total = res.data.total }).finally(() => { this.loading = false })
    },
    openDialog(row) {
      this.form = row ? { ...row } : {}
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.form && this.$refs.form.clearValidate() })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const api = this.form.id ? updateDept : addDept
          api(this.form).then(() => { this.$message.success('操作成功'); this.dialogVisible = false; this.loadData() })
        }
      })
    },
    handleDelete(id) {
      this.$confirm('确定删除？', '提示', { type: 'warning' }).then(() => {
        deleteDept(id).then(() => { this.$message.success('已删除'); this.loadData() })
      }).catch(() => {})
    }
  }
}
</script>
