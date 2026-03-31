<template>
  <div class="page-container">
    <el-card>
      <div slot="header"><b>我的教学评价</b></div>

      <el-row :gutter="20" style="margin-bottom:20px">
        <el-col :span="6">
          <div class="mini-stat" style="border-left:3px solid #409EFF;"><span class="num">{{ avgScore }}</span><span class="label">平均总评分</span></div>
        </el-col>
        <el-col :span="6">
          <div class="mini-stat" style="border-left:3px solid #67C23A;"><span class="num">{{ avgTeaching }}</span><span class="label">教学评分</span></div>
        </el-col>
        <el-col :span="6">
          <div class="mini-stat" style="border-left:3px solid #E6A23C;"><span class="num">{{ avgAttend }}</span><span class="label">考勤评分</span></div>
        </el-col>
        <el-col :span="6">
          <div class="mini-stat" style="border-left:3px solid #F56C6C;"><span class="num">{{ avgStudent }}</span><span class="label">学生评分</span></div>
        </el-col>
      </el-row>

      <el-table :data="tableData" border stripe v-loading="loading" empty-text="暂无数据">
        <el-table-column prop="courseName" label="课程" width="180" show-overflow-tooltip></el-table-column>
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
      </el-table>

      <div class="pagination-bar">
        <el-pagination background layout="total, prev, pager, next" :total="total" :page-size.sync="query.size" :current-page.sync="query.current" @current-change="loadData"></el-pagination>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getEvalPage } from '@/api'

export default {
  name: 'MyEvaluation',
  data() {
    return {
      query: { current: 1, size: 10, teacherId: null },
      tableData: [], total: 0, loading: false,
      avgScore: 0, avgTeaching: 0, avgAttend: 0, avgStudent: 0
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
      getEvalPage(this.query).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
        this.calcAvg(res.data.records)
      }).finally(() => { this.loading = false })
    },
    calcAvg(records) {
      if (records.length === 0) return
      const avg = (arr, key) => (arr.reduce((s, r) => s + (parseFloat(r[key]) || 0), 0) / arr.length).toFixed(1)
      this.avgScore = avg(records, 'totalScore')
      this.avgTeaching = avg(records, 'teachingScore')
      this.avgAttend = avg(records, 'attendanceScore')
      this.avgStudent = avg(records, 'studentScore')
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
