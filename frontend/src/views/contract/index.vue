<template>
  <div class="page-container">
    <el-card>
      <div class="search-bar">
        <el-input v-model="query.teacherId" placeholder="教师ID" clearable style="width:150px" @clear="loadData"></el-input>
        <el-select v-model="query.status" placeholder="合同状态" clearable @change="loadData" style="width:140px">
          <el-option label="未签署" value="UNSIGNED"></el-option>
          <el-option label="已签署" value="SIGNED"></el-option>
          <el-option label="已到期" value="EXPIRED"></el-option>
          <el-option label="已终止" value="TERMINATED"></el-option>
        </el-select>
        <el-button type="primary" icon="el-icon-search" @click="loadData">搜索</el-button>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" empty-text="暂无数据">
        <el-table-column prop="contractNo" label="合同编号" width="200"></el-table-column>
        <el-table-column prop="teacherName" label="教师姓名" width="100"></el-table-column>
        <el-table-column prop="departmentName" label="所属院系" width="140"></el-table-column>
        <el-table-column prop="startDate" label="合同起始" width="110"></el-table-column>
        <el-table-column prop="endDate" label="合同终止" width="110"></el-table-column>
        <el-table-column prop="salaryStandard" label="薪酬标准" width="120">
          <template slot-scope="scope">{{ scope.row.salaryStandard }} 元/课时</template>
        </el-table-column>
        <el-table-column prop="contractStatus" label="合同状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="statusType(scope.row.contractStatus)">{{ statusText(scope.row.contractStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="teacherSignTime" label="教师签署时间" width="160"></el-table-column>
        <el-table-column prop="schoolSignTime" label="学校签署时间" width="160"></el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="showDetail(scope.row)">详情</el-button>
            <el-button size="mini" type="primary"
              v-if="scope.row.contractStatus==='UNSIGNED' && !scope.row.teacherSignTime"
              @click="handleTeacherSign(scope.row.id)">教师签署</el-button>
            <el-button size="mini" type="success"
              v-if="scope.row.contractStatus==='UNSIGNED' && scope.row.teacherSignTime && !scope.row.schoolSignTime"
              @click="handleSchoolSign(scope.row.id)">学校签署</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-bar">
        <el-pagination background layout="total, prev, pager, next"
          :total="total" :page-size.sync="query.size"
          :current-page.sync="query.current"
          @current-change="loadData"></el-pagination>
      </div>
    </el-card>

    <!-- 合同详情对话框 -->
    <el-dialog title="合同详情" :visible.sync="detailDialogVisible" width="560px">
      <div style="padding:0 10px" v-if="detailData">
        <el-descriptions title="合同基本信息" :column="2" border size="small" style="margin-bottom:20px">
          <el-descriptions-item label="合同编号" :span="2">{{ detailData.contractNo }}</el-descriptions-item>
          <el-descriptions-item label="教师姓名">{{ detailData.teacherName }}</el-descriptions-item>
          <el-descriptions-item label="所属院系">{{ detailData.departmentName }}</el-descriptions-item>
          <el-descriptions-item label="合同起始">{{ detailData.startDate }}</el-descriptions-item>
          <el-descriptions-item label="合同终止">{{ detailData.endDate }}</el-descriptions-item>
          <el-descriptions-item label="薪酬标准">{{ detailData.salaryStandard }} 元/课时</el-descriptions-item>
          <el-descriptions-item label="合同状态">
            <el-tag :type="statusType(detailData.contractStatus)">{{ statusText(detailData.contractStatus) }}</el-tag>
          </el-descriptions-item>
        </el-descriptions>

        <el-descriptions title="签署进度" :column="1" border size="small">
          <el-descriptions-item label="教师签署">
            <span v-if="detailData.teacherSignTime" style="color:#67C23A">
              <i class="el-icon-circle-check"></i> {{ detailData.teacherSignTime }}
            </span>
            <span v-else style="color:#909399">未签署</span>
          </el-descriptions-item>
          <el-descriptions-item label="学校签署">
            <span v-if="detailData.schoolSignTime" style="color:#67C23A">
              <i class="el-icon-circle-check"></i> {{ detailData.schoolSignTime }}
            </span>
            <span v-else style="color:#909399">未签署</span>
          </el-descriptions-item>
          <el-descriptions-item label="关联审批ID">{{ detailData.approvalId }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <span slot="footer">
        <el-button @click="detailDialogVisible=false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'ContractPage',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      query: { current: 1, size: 10, status: '', teacherId: '' },
      detailDialogVisible: false,
      detailData: null
    }
  },
  created() { this.loadData() },
  methods: {
    loadData() {
      this.loading = true
      request.get('/contract/page', { params: this.query }).then(res => {
        this.tableData = res.data.records || []
        this.total = res.data.total || 0
      }).finally(() => { this.loading = false })
    },
    showDetail(row) {
      this.detailData = row
      this.detailDialogVisible = true
    },
    handleTeacherSign(id) {
      this.$confirm('确认教师签署该合同？', '提示', { type: 'info' }).then(() => {
        request.put(`/contract/${id}/teacher-sign`).then(() => {
          this.$message.success('教师签署成功')
          this.loadData()
        })
      })
    },
    handleSchoolSign(id) {
      this.$confirm('确认学校盖章签署该合同？签署后合同正式生效。', '提示', { type: 'warning' }).then(() => {
        request.put(`/contract/${id}/school-sign`).then(() => {
          this.$message.success('学校签署成功，合同已正式生效')
          this.loadData()
        })
      })
    },
    statusText(s) {
      const map = { UNSIGNED: '未签署', SIGNED: '已签署', EXPIRED: '已到期', TERMINATED: '已终止' }
      return map[s] || s || '—'
    },
    statusType(s) {
      const map = { UNSIGNED: 'warning', SIGNED: 'success', EXPIRED: 'info', TERMINATED: 'danger' }
      return map[s] || ''
    }
  }
}
</script>
