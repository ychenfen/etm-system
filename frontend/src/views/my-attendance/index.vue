<template>
  <div class="page-container">
    <el-card>
      <div slot="header" style="display:flex; justify-content:space-between; align-items:center;">
        <b>我的考勤记录</b>
        <div>
          <el-date-picker v-model="query.month" type="month" value-format="yyyy-MM" placeholder="选择月份" clearable @change="loadData" size="small" style="margin-right:10px"></el-date-picker>
          <el-select v-model="query.status" placeholder="状态" clearable @change="loadData" size="small" style="width:120px">
            <el-option label="正常" value="NORMAL"></el-option>
            <el-option label="迟到" value="LATE"></el-option>
            <el-option label="早退" value="EARLY_LEAVE"></el-option>
            <el-option label="缺勤" value="ABSENT"></el-option>
            <el-option label="请假" value="LEAVE"></el-option>
          </el-select>
        </div>
      </div>

      <el-row :gutter="20" style="margin-bottom:20px">
        <el-col :span="6">
          <div class="mini-stat" style="border-left:3px solid #409EFF;"><span class="num">{{ stats.total }}</span><span class="label">总记录</span></div>
        </el-col>
        <el-col :span="6">
          <div class="mini-stat" style="border-left:3px solid #67C23A;"><span class="num">{{ stats.normal }}</span><span class="label">正常</span></div>
        </el-col>
        <el-col :span="6">
          <div class="mini-stat" style="border-left:3px solid #E6A23C;"><span class="num">{{ stats.late }}</span><span class="label">迟到/早退</span></div>
        </el-col>
        <el-col :span="6">
          <div class="mini-stat" style="border-left:3px solid #F56C6C;"><span class="num">{{ stats.absent }}</span><span class="label">缺勤</span></div>
        </el-col>
      </el-row>

      <el-table :data="tableData" border stripe v-loading="loading" empty-text="暂无数据">
        <el-table-column prop="courseName" label="课程" width="180" show-overflow-tooltip></el-table-column>
        <el-table-column prop="attendanceDate" label="日期" width="120"></el-table-column>
        <el-table-column prop="checkInTime" label="签到" width="170"></el-table-column>
        <el-table-column prop="checkOutTime" label="签退" width="170"></el-table-column>
        <el-table-column prop="actualHours" label="课时" width="80"></el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="statusType(scope.row.status)" size="small">{{ statusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" show-overflow-tooltip></el-table-column>
      </el-table>

      <div class="pagination-bar">
        <el-pagination background layout="total, prev, pager, next" :total="total" :page-size.sync="query.size" :current-page.sync="query.current" @current-change="loadData"></el-pagination>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getAttendancePage } from '@/api'

export default {
  name: 'MyAttendance',
  data() {
    return {
      query: { current: 1, size: 10, teacherId: null, status: '', month: '' },
      tableData: [], total: 0, loading: false,
      stats: { total: 0, normal: 0, late: 0, absent: 0 }
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
      getAttendancePage(this.query).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
        this.calcStats(res.data.records)
      }).finally(() => { this.loading = false })
    },
    calcStats(records) {
      this.stats.total = this.total
      this.stats.normal = records.filter(r => r.status === 'NORMAL').length
      this.stats.late = records.filter(r => r.status === 'LATE' || r.status === 'EARLY_LEAVE').length
      this.stats.absent = records.filter(r => r.status === 'ABSENT').length
    },
    statusType(s) {
      return { NORMAL: 'success', LATE: 'warning', EARLY_LEAVE: 'warning', ABSENT: 'danger', LEAVE: 'info' }[s] || 'info'
    },
    statusText(s) {
      return { NORMAL: '正常', LATE: '迟到', EARLY_LEAVE: '早退', ABSENT: '缺勤', LEAVE: '请假' }[s] || s
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
