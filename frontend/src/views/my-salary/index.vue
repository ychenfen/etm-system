<template>
  <div class="page-container">
    <el-card>
      <div slot="header" style="display:flex; justify-content:space-between; align-items:center;">
        <b>我的薪酬记录</b>
        <el-date-picker v-model="query.month" type="month" value-format="yyyy-MM" placeholder="选择月份" clearable @change="loadData" size="small"></el-date-picker>
      </div>

      <el-row :gutter="20" style="margin-bottom:20px">
        <el-col :span="8">
          <div class="mini-stat" style="border-left:3px solid #409EFF;"><span class="num">{{ totalAmount }}</span><span class="label">累计薪酬(元)</span></div>
        </el-col>
        <el-col :span="8">
          <div class="mini-stat" style="border-left:3px solid #67C23A;"><span class="num">{{ paidCount }}</span><span class="label">已发放笔数</span></div>
        </el-col>
        <el-col :span="8">
          <div class="mini-stat" style="border-left:3px solid #E6A23C;"><span class="num">{{ pendingCount }}</span><span class="label">待审核/待发放</span></div>
        </el-col>
      </el-row>

      <el-table :data="tableData" border stripe v-loading="loading" empty-text="暂无数据">
        <el-table-column prop="month" label="月份" width="100"></el-table-column>
        <el-table-column prop="baseSalary" label="基本工资" width="100"></el-table-column>
        <el-table-column prop="totalHours" label="总课时" width="80"></el-table-column>
        <el-table-column prop="hourRate" label="课时费" width="80"></el-table-column>
        <el-table-column prop="bonus" label="奖金" width="80"></el-table-column>
        <el-table-column prop="deduction" label="扣款" width="80"></el-table-column>
        <el-table-column prop="totalSalary" label="实发金额" width="110">
          <template slot-scope="scope">
            <span style="color:#e6a23c; font-weight:bold">{{ scope.row.totalSalary }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90">
          <template slot-scope="scope">
            <el-tag :type="statusType(scope.row.status)" size="small">{{ statusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="payDate" label="发放日期" width="120"></el-table-column>
        <el-table-column prop="remark" label="备注" show-overflow-tooltip></el-table-column>
      </el-table>

      <div class="pagination-bar">
        <el-pagination background layout="total, prev, pager, next" :total="total" :page-size.sync="query.size" :current-page.sync="query.current" @current-change="loadData"></el-pagination>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getSalaryPage } from '@/api'

export default {
  name: 'MySalary',
  data() {
    return {
      query: { current: 1, size: 10, teacherId: null, month: '' },
      tableData: [], total: 0, loading: false,
      totalAmount: 0, paidCount: 0, pendingCount: 0
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.loading = true
      const userInfo = this.$store.state.userInfo
      this.query.teacherId = userInfo.teacherId || userInfo.id
      getSalaryPage(this.query).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
        this.calcStats(res.data.records)
      }).finally(() => { this.loading = false })
    },
    calcStats(records) {
      this.totalAmount = records.reduce((sum, r) => sum + (parseFloat(r.totalSalary) || 0), 0).toFixed(2)
      this.paidCount = records.filter(r => r.status === 'PAID').length
      this.pendingCount = records.filter(r => r.status !== 'PAID').length
    },
    statusType(s) {
      return { PENDING: 'warning', APPROVED: 'success', PAID: '' }[s] || 'info'
    },
    statusText(s) {
      return { PENDING: '待审核', APPROVED: '已审核', PAID: '已发放' }[s] || s
    }
  }
}
</script>

<style scoped>
.mini-stat {
  padding: 18px 20px;
  background: #fff;
  border-radius: 10px;
  display: flex;
  align-items: center;
  gap: 14px;
  box-shadow: 0 1px 6px rgba(0,0,0,0.04);
  transition: transform 0.2s, box-shadow 0.2s;
}
.mini-stat:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0,0,0,0.08);
}
.mini-stat .num { font-size: 30px; font-weight: 700; color: #1f2937; }
.mini-stat .label { font-size: 13px; color: #6b7280; }
</style>
