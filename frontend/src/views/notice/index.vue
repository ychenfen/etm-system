<template>
  <div class="page-container">
    <el-card>
      <div class="search-bar">
        <el-input v-model="query.keyword" placeholder="搜索标题/内容" clearable @clear="loadData" style="width:200px"></el-input>
        <el-select v-model="query.type" placeholder="通知类型" clearable @change="loadData">
          <el-option label="系统通知" value="SYSTEM"></el-option>
          <el-option label="教学通知" value="TEACHING"></el-option>
          <el-option label="行政通知" value="ADMIN"></el-option>
          <el-option label="其他" value="OTHER"></el-option>
        </el-select>
        <el-button type="primary" icon="el-icon-search" @click="loadData">搜索</el-button>
        <el-button type="success" icon="el-icon-plus" @click="openDialog(null)" v-if="canEdit">发布通知</el-button>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" empty-text="暂无数据">
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip></el-table-column>
        <el-table-column prop="type" label="类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="typeTagType(scope.row.type)" size="small">{{ typeText(scope.row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="publisher" label="发布人" width="100"></el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'" size="small">{{ scope.row.status === 1 ? '已发布' : '草稿' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发布时间" width="170"></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="viewDetail(scope.row)">查看</el-button>
            <el-button size="mini" @click="openDialog(scope.row)" v-if="canEdit">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)" v-if="canEdit">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-bar">
        <el-pagination background layout="total, sizes, prev, pager, next" :total="total" :page-size.sync="query.size" :current-page.sync="query.current" @current-change="loadData" @size-change="loadData"></el-pagination>
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="form.id ? '编辑通知' : '发布通知'" :visible.sync="dialogVisible" width="700px">
      <el-form :model="form" :rules="rules" ref="form" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title"></el-input>
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" style="width:100%">
            <el-option label="系统通知" value="SYSTEM"></el-option>
            <el-option label="教学通知" value="TEACHING"></el-option>
            <el-option label="行政通知" value="ADMIN"></el-option>
            <el-option label="其他" value="OTHER"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">发布</el-radio>
            <el-radio :label="0">草稿</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="8" placeholder="请输入通知内容"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog :title="detailData.title" :visible.sync="detailVisible" width="600px">
      <div style="margin-bottom: 10px;">
        <el-tag :type="typeTagType(detailData.type)" size="small">{{ typeText(detailData.type) }}</el-tag>
        <span style="margin-left: 10px; color: #909399; font-size: 13px;">{{ detailData.publisher }} | {{ detailData.createTime }}</span>
      </div>
      <div style="white-space: pre-wrap; line-height: 1.8; color: #606266;">{{ detailData.content }}</div>
    </el-dialog>
  </div>
</template>

<script>
import { getNoticePage, addNotice, updateNotice, deleteNotice } from '@/api'

export default {
  name: 'Notice',
  data() {
    return {
      query: { current: 1, size: 10, keyword: '', type: '' },
      tableData: [], total: 0, loading: false,
      dialogVisible: false, detailVisible: false,
      form: {},
      detailData: {},
      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        type: [{ required: true, message: '请选择类型', trigger: 'change' }],
        content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
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
    this.loadData()
  },
  methods: {
    loadData() {
      this.loading = true
      getNoticePage(this.query).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      }).finally(() => { this.loading = false })
    },
    typeTagType(t) {
      return { SYSTEM: 'danger', TEACHING: '', ADMIN: 'warning', OTHER: 'info' }[t] || 'info'
    },
    typeText(t) {
      return { SYSTEM: '系统通知', TEACHING: '教学通知', ADMIN: '行政通知', OTHER: '其他' }[t] || t
    },
    openDialog(row) {
      this.form = row ? { ...row } : { type: 'TEACHING', status: 1, publisher: this.$store.state.userInfo.realName || this.$store.state.userInfo.username }
      this.dialogVisible = true
      this.$nextTick(() => { this.$refs.form && this.$refs.form.clearValidate() })
    },
    viewDetail(row) {
      this.detailData = row
      this.detailVisible = true
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const api = this.form.id ? updateNotice : addNotice
          api(this.form).then(() => {
            this.$message.success('操作成功')
            this.dialogVisible = false
            this.loadData()
          })
        }
      })
    },
    handleDelete(id) {
      this.$confirm('确定删除该通知？', '提示', { type: 'warning' }).then(() => {
        deleteNotice(id).then(() => { this.$message.success('已删除'); this.loadData() })
      }).catch(() => {})
    }
  }
}
</script>
