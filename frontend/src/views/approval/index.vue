<template>
  <div class="page-container">
    <el-card>
      <div class="search-bar">
        <el-input v-model="query.teacherId" placeholder="教师ID" clearable style="width:150px" @clear="loadData"></el-input>
        <el-select v-model="query.status" placeholder="审批状态" clearable @change="loadData" style="width:140px">
          <el-option label="审批中" value="PENDING"></el-option>
          <el-option label="已通过" value="APPROVED"></el-option>
          <el-option label="已驳回" value="REJECTED"></el-option>
          <el-option label="已撤回" value="REVOKED"></el-option>
        </el-select>
        <el-button type="primary" icon="el-icon-search" @click="loadData">搜索</el-button>
        <el-button type="success" icon="el-icon-plus" @click="openSubmitDialog">发起审批</el-button>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" empty-text="暂无数据">
        <el-table-column prop="approvalNo" label="审批编号" width="180"></el-table-column>
        <el-table-column prop="teacherName" label="教师姓名" width="100"></el-table-column>
        <el-table-column prop="departmentName" label="所属院系" width="140"></el-table-column>
        <el-table-column prop="startDate" label="拟聘起" width="110"></el-table-column>
        <el-table-column prop="endDate" label="拟聘止" width="110"></el-table-column>
        <el-table-column prop="proposedSalary" label="拟定薪酬" width="100">
          <template slot-scope="scope">{{ scope.row.proposedSalary }}元/课时</template>
        </el-table-column>
        <el-table-column prop="currentNode" label="当前节点" width="140">
          <template slot-scope="scope">
            <el-tag size="small" type="info">{{ nodeText(scope.row.currentNode) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="approvalStatus" label="总体状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="statusType(scope.row.approvalStatus)">{{ statusText(scope.row.approvalStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="260" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="showDetail(scope.row)">时间线</el-button>
            <el-button size="mini" type="success"
              v-if="scope.row.approvalStatus==='PENDING'"
              @click="openApproveDialog(scope.row, true)">通过</el-button>
            <el-button size="mini" type="danger"
              v-if="scope.row.approvalStatus==='PENDING'"
              @click="openApproveDialog(scope.row, false)">驳回</el-button>
            <el-button size="mini" type="warning"
              v-if="scope.row.approvalStatus==='PENDING' && scope.row.currentNode==='college_leader'"
              @click="handleRevoke(scope.row.id)">撤回</el-button>
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

    <!-- 发起审批对话框 -->
    <el-dialog title="发起聘用审批" :visible.sync="submitDialogVisible" width="520px">
      <el-form :model="submitForm" label-width="100px" :rules="submitRules" ref="submitForm">
        <el-form-item label="教师ID" prop="teacherId">
          <el-input v-model.number="submitForm.teacherId" placeholder="输入外聘教师ID"></el-input>
        </el-form-item>
        <el-form-item label="院系ID" prop="departmentId">
          <el-input v-model.number="submitForm.departmentId" placeholder="输入院系ID"></el-input>
        </el-form-item>
        <el-form-item label="聘期开始" prop="startDate">
          <el-date-picker v-model="submitForm.startDate" type="date" value-format="yyyy-MM-dd" style="width:100%"></el-date-picker>
        </el-form-item>
        <el-form-item label="聘期结束" prop="endDate">
          <el-date-picker v-model="submitForm.endDate" type="date" value-format="yyyy-MM-dd" style="width:100%"></el-date-picker>
        </el-form-item>
        <el-form-item label="薪酬(元/课时)" prop="proposedSalary">
          <el-input-number v-model="submitForm.proposedSalary" :min="0" :precision="2"></el-input-number>
        </el-form-item>
        <el-form-item label="申请理由">
          <el-input type="textarea" v-model="submitForm.applyReason" rows="3"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="submitDialogVisible=false">取消</el-button>
        <el-button type="primary" @click="doSubmit">提交审批</el-button>
      </span>
    </el-dialog>

    <!-- 审批操作对话框 -->
    <el-dialog :title="approvePass ? '通过审批' : '驳回审批'" :visible.sync="approveDialogVisible" width="440px">
      <el-form label-width="80px">
        <el-form-item label="当前节点">
          <el-tag>{{ nodeText(currentApproval.currentNode) }}</el-tag>
        </el-form-item>
        <el-form-item label="审批意见">
          <el-input type="textarea" v-model="approveRemark" rows="3" :placeholder="approvePass ? '可填写通过意见' : '请填写驳回原因'"></el-input>
        </el-form-item>
        <el-form-item label="审批人">
          <el-input v-model="approveOperator" placeholder="请输入审批人姓名"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="approveDialogVisible=false">取消</el-button>
        <el-button :type="approvePass ? 'success' : 'danger'" @click="doApprove">确认{{ approvePass ? '通过' : '驳回' }}</el-button>
      </span>
    </el-dialog>

    <!-- 审批时间线详情 -->
    <el-dialog title="审批进度时间线" :visible.sync="detailDialogVisible" width="560px">
      <div style="padding:0 20px" v-if="detailData">
        <el-descriptions title="基本信息" :column="2" border size="small" style="margin-bottom:20px">
          <el-descriptions-item label="审批编号">{{ detailData.approvalNo }}</el-descriptions-item>
          <el-descriptions-item label="教师姓名">{{ detailData.teacherName }}</el-descriptions-item>
          <el-descriptions-item label="聘期">{{ detailData.startDate }} ~ {{ detailData.endDate }}</el-descriptions-item>
          <el-descriptions-item label="拟定薪酬">{{ detailData.proposedSalary }} 元/课时</el-descriptions-item>
          <el-descriptions-item label="申请理由" :span="2">{{ detailData.applyReason || '—' }}</el-descriptions-item>
        </el-descriptions>

        <el-timeline>
          <el-timeline-item timestamp="发起申请" placement="top" color="#409EFF">
            <el-card shadow="never" body-style="padding:10px">
              <p>审批编号：{{ detailData.approvalNo }}</p>
            </el-card>
          </el-timeline-item>
          <el-timeline-item timestamp="学院负责人审批" placement="top"
            :color="nodeColor(detailData.collegeStatus)">
            <el-card shadow="never" body-style="padding:10px">
              <p v-if="detailData.collegeStatus">
                <el-tag size="mini" :type="statusType(detailData.collegeStatus)">{{ statusText(detailData.collegeStatus) }}</el-tag>
                &nbsp; 审批人：{{ detailData.collegeBy }} &nbsp; {{ detailData.collegeTime }}
              </p>
              <p v-if="detailData.collegeRemark" style="color:#666;font-size:13px">意见：{{ detailData.collegeRemark }}</p>
              <p v-if="!detailData.collegeStatus" style="color:#999">等待审批...</p>
            </el-card>
          </el-timeline-item>
          <el-timeline-item timestamp="人事处薪酬岗审批" placement="top"
            :color="nodeColor(detailData.hrSalaryStatus)">
            <el-card shadow="never" body-style="padding:10px">
              <p v-if="detailData.hrSalaryStatus">
                <el-tag size="mini" :type="statusType(detailData.hrSalaryStatus)">{{ statusText(detailData.hrSalaryStatus) }}</el-tag>
                &nbsp; 审批人：{{ detailData.hrSalaryBy }} &nbsp; {{ detailData.hrSalaryTime }}
              </p>
              <p v-if="detailData.hrSalaryRemark" style="color:#666;font-size:13px">意见：{{ detailData.hrSalaryRemark }}</p>
              <p v-if="!detailData.hrSalaryStatus" style="color:#999">等待审批...</p>
            </el-card>
          </el-timeline-item>
          <el-timeline-item timestamp="人事处处长审批" placement="top"
            :color="nodeColor(detailData.hrDirectorStatus)">
            <el-card shadow="never" body-style="padding:10px">
              <p v-if="detailData.hrDirectorStatus">
                <el-tag size="mini" :type="statusType(detailData.hrDirectorStatus)">{{ statusText(detailData.hrDirectorStatus) }}</el-tag>
                &nbsp; 审批人：{{ detailData.hrDirectorBy }} &nbsp; {{ detailData.hrDirectorTime }}
              </p>
              <p v-if="detailData.hrDirectorRemark" style="color:#666;font-size:13px">意见：{{ detailData.hrDirectorRemark }}</p>
              <p v-if="!detailData.hrDirectorStatus" style="color:#999">等待审批...</p>
            </el-card>
          </el-timeline-item>
          <el-timeline-item
            v-if="detailData.approvalStatus==='APPROVED'"
            timestamp="审批通过，合同已生成" placement="top" color="#67C23A">
            <el-card shadow="never" body-style="padding:10px">
              <p style="color:#67C23A">✓ 全部审批通过，聘用合同已自动生成</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'ApprovalPage',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      query: { current: 1, size: 10, status: '', teacherId: '' },
      submitDialogVisible: false,
      submitForm: { teacherId: null, departmentId: null, startDate: '', endDate: '', proposedSalary: 100, applyReason: '' },
      submitRules: {
        teacherId: [{ required: true, message: '请输入教师ID', trigger: 'blur' }],
        departmentId: [{ required: true, message: '请输入院系ID', trigger: 'blur' }],
        startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
        endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }]
      },
      approveDialogVisible: false,
      approvePass: true,
      approveRemark: '',
      approveOperator: '',
      currentApproval: {},
      detailDialogVisible: false,
      detailData: null
    }
  },
  created() { this.loadData() },
  methods: {
    loadData() {
      this.loading = true
      request.get('/approval/page', { params: this.query }).then(res => {
        this.tableData = res.data.records || []
        this.total = res.data.total || 0
      }).finally(() => { this.loading = false })
    },
    openSubmitDialog() {
      this.submitForm = { teacherId: null, departmentId: null, startDate: '', endDate: '', proposedSalary: 100, applyReason: '' }
      this.submitDialogVisible = true
    },
    doSubmit() {
      this.$refs.submitForm.validate(valid => {
        if (!valid) return
        request.post('/approval', this.submitForm).then(() => {
          this.$message.success('审批申请已提交')
          this.submitDialogVisible = false
          this.loadData()
        })
      })
    },
    openApproveDialog(row, pass) {
      this.currentApproval = row
      this.approvePass = pass
      this.approveRemark = ''
      this.approveOperator = ''
      this.approveDialogVisible = true
    },
    doApprove() {
      if (!this.approveOperator) { this.$message.warning('请填写审批人姓名'); return }
      request.put(`/approval/${this.currentApproval.id}/approve`, {
        node: this.currentApproval.currentNode,
        pass: this.approvePass,
        remark: this.approveRemark,
        operatorName: this.approveOperator
      }).then(() => {
        this.$message.success('审批操作成功')
        this.approveDialogVisible = false
        this.loadData()
      })
    },
    handleRevoke(id) {
      this.$confirm('确认撤回该审批申请？', '提示', { type: 'warning' }).then(() => {
        request.put(`/approval/${id}/revoke`).then(() => {
          this.$message.success('已撤回')
          this.loadData()
        })
      })
    },
    showDetail(row) {
      this.detailData = row
      this.detailDialogVisible = true
    },
    nodeText(node) {
      const map = { college_leader: '学院负责人', hr_salary: '人事处薪酬岗', hr_director: '人事处处长', finished: '已完成' }
      return map[node] || node || '—'
    },
    statusText(s) {
      const map = { PENDING: '审批中', APPROVED: '已通过', REJECTED: '已驳回', REVOKED: '已撤回' }
      return map[s] || s || '—'
    },
    statusType(s) {
      const map = { PENDING: 'warning', APPROVED: 'success', REJECTED: 'danger', REVOKED: 'info' }
      return map[s] || ''
    },
    nodeColor(s) {
      if (!s) return '#C0C4CC'
      return s === 'APPROVED' ? '#67C23A' : '#F56C6C'
    }
  }
}
</script>
