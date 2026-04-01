<template>
  <div class="page-container dashboard-page">
    <!-- 欢迎区域 -->
    <div class="welcome-bar">
      <div>
        <h3 style="margin: 0; color: #303133;">{{ greeting }}，{{ userInfo.realName || userInfo.username }}</h3>
        <p style="margin: 5px 0 0; color: #909399; font-size: 13px;">欢迎使用高校外聘教师管理系统</p>
      </div>
      <div style="color: #909399; font-size: 13px;">
        <i class="el-icon-date"></i> {{ today }}
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16">
      <el-col :span="4" v-for="(item, index) in statCards" :key="index">
        <div class="stat-card-v2" :style="{ borderTop: '3px solid ' + item.color }">
          <div class="stat-icon" :style="{ background: item.color + '15', color: item.color }">
            <i :class="item.icon"></i>
          </div>
          <div class="stat-value" :style="{ color: item.color }">{{ item.value }}</div>
          <div class="stat-title">{{ item.label }}</div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="16" style="margin-top: 16px;">
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <div slot="header" class="card-header">
            <span><i class="el-icon-s-data" style="margin-right: 6px;"></i>各院系教师分布</span>
          </div>
          <div ref="deptChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <div slot="header" class="card-header">
            <span><i class="el-icon-pie-chart" style="margin-right: 6px;"></i>聘用状态统计</span>
          </div>
          <div ref="hireChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top: 16px;">
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <div slot="header" class="card-header">
            <span><i class="el-icon-check" style="margin-right: 6px;"></i>考勤状态概览</span>
          </div>
          <div ref="attendChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <div slot="header" class="card-header">
            <span><i class="el-icon-money" style="margin-right: 6px;"></i>月度薪酬统计</span>
          </div>
          <div ref="salaryChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 到期预警 -->
    <el-row :gutter="16" style="margin-top: 16px;" v-if="expiringTeachers.length > 0">
      <el-col :span="24">
        <el-card shadow="hover">
          <div slot="header" class="card-header" style="color: #E6A23C;">
            <span><i class="el-icon-warning" style="margin-right: 6px;"></i>合同即将到期预警（30天内）</span>
            <el-tag type="danger" size="mini">{{ expiringTeachers.length }}人</el-tag>
          </div>
          <el-table :data="expiringTeachers" stripe size="small" empty-text="暂无数据">
            <el-table-column prop="name" label="教师姓名" width="120"></el-table-column>
            <el-table-column prop="departmentName" label="所属院系" width="200"></el-table-column>
            <el-table-column prop="hireEndDate" label="合同到期日" width="120"></el-table-column>
            <el-table-column label="剩余天数" width="120">
              <template slot-scope="scope">
                <el-tag :type="calcDays(scope.row.hireEndDate) <= 7 ? 'danger' : 'warning'" size="small">{{ calcDays(scope.row.hireEndDate) }}天</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="紧急程度">
              <template slot-scope="scope">
                <el-progress :percentage="Math.max(0, 100 - calcDays(scope.row.hireEndDate) * 3.3)" :color="calcDays(scope.row.hireEndDate) <= 7 ? '#F56C6C' : '#E6A23C'" :stroke-width="14" :text-inside="true" :format="() => ''"></el-progress>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最新通知 -->
    <el-row :gutter="16" style="margin-top: 16px;">
      <el-col :span="24">
        <el-card shadow="hover">
          <div slot="header" class="card-header">
            <span><i class="el-icon-bell" style="margin-right: 6px;"></i>最新通知公告</span>
            <el-button type="text" size="small" @click="$router.push('/notice')">查看全部 <i class="el-icon-arrow-right"></i></el-button>
          </div>
          <el-table :data="recentNotices" stripe size="small" empty-text="暂无通知">
            <el-table-column prop="title" label="标题" show-overflow-tooltip>
              <template slot-scope="scope">
                <span style="cursor: pointer; color: #409EFF;">{{ scope.row.title }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="type" label="类型" width="120">
              <template slot-scope="scope">
                <el-tag :type="noticeTypeTag(scope.row.type)" size="mini" effect="plain">{{ noticeTypeText(scope.row.type) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="发布时间" width="180"></el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getDashboard } from '@/api'
import * as echarts from 'echarts'

export default {
  name: 'Dashboard',
  data() {
    return {
      statCards: [
        { label: '教师总数', value: 0, color: '#409EFF', icon: 'el-icon-user' },
        { label: '课程总数', value: 0, color: '#67C23A', icon: 'el-icon-notebook-2' },
        { label: '院系总数', value: 0, color: '#0bbd87', icon: 'el-icon-office-building' },
        { label: '待审核聘用', value: 0, color: '#E6A23C', icon: 'el-icon-warning' },
        { label: '待审核薪酬', value: 0, color: '#F56C6C', icon: 'el-icon-money' },
        { label: '考勤出勤率', value: '0%', color: '#7C4DFF', icon: 'el-icon-date' }
      ],
      recentNotices: [],
      expiringTeachers: []
    }
  },
  computed: {
    userInfo() {
      return this.$store.state.userInfo
    },
    greeting() {
      const h = new Date().getHours()
      if (h < 6) return '凌晨好'
      if (h < 12) return '上午好'
      if (h < 14) return '中午好'
      if (h < 18) return '下午好'
      return '晚上好'
    },
    today() {
      const d = new Date()
      const week = ['日', '一', '二', '三', '四', '五', '六']
      return `${d.getFullYear()}年${d.getMonth() + 1}月${d.getDate()}日 星期${week[d.getDay()]}`
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      getDashboard().then(res => {
        const d = res.data
        this.statCards[0].value = d.teacherCount
        this.statCards[1].value = d.courseCount
        this.statCards[2].value = d.departmentCount
        this.statCards[3].value = d.pendingHireCount
        this.statCards[4].value = d.pendingSalaryCount
        const rate = d.attendanceCount > 0 ? Math.round(d.normalAttendanceCount / d.attendanceCount * 100) : 0
        this.statCards[5].value = rate + '%'
        this.recentNotices = d.recentNotices || []
        this.expiringTeachers = d.expiringTeachers || []
        this.$nextTick(() => {
          this.renderDeptChart(d.departmentTeacherStats || [])
          this.renderHireChart(d.hireStatusStats || [])
          this.renderAttendChart(d.attendanceCount, d.normalAttendanceCount)
          this.renderSalaryChart(d.monthlySalaryStats || [])
        })
      })
    },
    renderDeptChart(data) {
      const chart = echarts.init(this.$refs.deptChart)
      chart.setOption({
        tooltip: { trigger: 'axis', backgroundColor: 'rgba(50,50,50,0.9)', textStyle: { color: '#fff' } },
        grid: { left: 40, right: 20, top: 20, bottom: 40 },
        xAxis: { type: 'category', data: data.map(i => i.name), axisLabel: { rotate: 20, fontSize: 11 } },
        yAxis: { type: 'value', minInterval: 1 },
        series: [{
          type: 'bar', data: data.map(i => i.value), barMaxWidth: 35, barMinHeight: 5,
          itemStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: '#409EFF' }, { offset: 1, color: '#79bbff' }]), borderRadius: [4, 4, 0, 0] }
        }]
      })
    },
    renderHireChart(data) {
      const chart = echarts.init(this.$refs.hireChart)
      const colorMap = { '待审核': '#E6A23C', '已聘用': '#67C23A', '已拒绝': '#F56C6C', '已到期': '#909399' }
      chart.setOption({
        tooltip: { trigger: 'item' },
        legend: { bottom: 10, itemWidth: 10, itemHeight: 10, textStyle: { fontSize: 12 } },
        series: [{
          type: 'pie', radius: ['45%', '70%'], center: ['50%', '45%'],
          data: data.map(i => ({ ...i, itemStyle: { color: colorMap[i.name] } })),
          label: { formatter: '{b}\n{d}%', fontSize: 11 },
          emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(0,0,0,0.2)' } }
        }]
      })
    },
    renderAttendChart(total, normal) {
      const chart = echarts.init(this.$refs.attendChart)
      const abnormal = total - normal
      chart.setOption({
        tooltip: { trigger: 'item' },
        legend: { bottom: 10, itemWidth: 10, itemHeight: 10 },
        series: [{
          type: 'pie', radius: ['45%', '70%'], center: ['50%', '45%'],
          data: [
            { value: normal, name: '正常出勤', itemStyle: { color: '#67C23A' } },
            { value: abnormal, name: '异常', itemStyle: { color: '#F56C6C' } }
          ],
          label: { formatter: '{b}\n{c}次 ({d}%)', fontSize: 11 }
        }]
      })
    },
    renderSalaryChart(data) {
      const chart = echarts.init(this.$refs.salaryChart)
      chart.setOption({
        tooltip: { trigger: 'axis', formatter: '{b}<br/>薪酬总额: ¥{c}', backgroundColor: 'rgba(50,50,50,0.9)', textStyle: { color: '#fff' } },
        grid: { left: 60, right: 20, top: 20, bottom: 30 },
        xAxis: { type: 'category', data: data.map(i => i.name), boundaryGap: false },
        yAxis: { type: 'value', axisLabel: { formatter: '¥{value}' } },
        series: [{
          type: 'line', data: data.map(i => i.value), smooth: true,
          areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: 'rgba(64,158,255,0.3)' }, { offset: 1, color: 'rgba(64,158,255,0.02)' }]) },
          itemStyle: { color: '#409EFF' }, lineStyle: { width: 2.5 }, symbolSize: 6
        }]
      })
    },
    calcDays(dateStr) {
      if (!dateStr) return 0
      const end = new Date(dateStr)
      const now = new Date()
      return Math.max(0, Math.ceil((end - now) / (1000 * 60 * 60 * 24)))
    },
    noticeTypeTag(t) {
      return { SYSTEM: 'danger', TEACHING: '', ADMIN: 'warning', OTHER: 'info' }[t] || 'info'
    },
    noticeTypeText(t) {
      return { SYSTEM: '系统通知', TEACHING: '教学通知', ADMIN: '行政通知', OTHER: '其他' }[t] || t
    }
  }
}
</script>

<style scoped>
.dashboard-page {
  padding: 16px !important;
}

.welcome-bar {
  background: linear-gradient(135deg, #409EFF 0%, #7C4DFF 100%);
  border-radius: 10px;
  padding: 20px 24px;
  margin-bottom: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #fff;
}
.welcome-bar h3 { color: #fff !important; }
.welcome-bar p { color: rgba(255,255,255,0.8) !important; }
.welcome-bar > div:last-child { color: rgba(255,255,255,0.7); }

.stat-card-v2 {
  background: #fff;
  border-radius: 8px;
  padding: 16px;
  text-align: center;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  transition: transform 0.2s, box-shadow 0.2s;
}
.stat-card-v2:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 20px rgba(0,0,0,0.1);
}
.stat-icon {
  width: 42px;
  height: 42px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 10px;
  font-size: 20px;
}
.stat-value {
  font-size: 26px;
  font-weight: 700;
  margin-bottom: 4px;
}
.stat-title {
  font-size: 12px;
  color: #909399;
}

.chart-card {
  border-radius: 8px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}
</style>
