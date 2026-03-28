<template>
  <div class="page-container">
    <el-row :gutter="20">
      <el-col :span="6" v-for="(item, index) in statCards" :key="index">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-number" :style="{ color: item.color }">{{ item.value }}</div>
            <div class="stat-label">{{ item.label }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <div slot="header"><b>各院系教师分布</b></div>
          <div ref="deptChart" style="height: 320px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header"><b>聘用状态统计</b></div>
          <div ref="hireChart" style="height: 320px;"></div>
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
        { label: '外聘教师总数', value: 0, color: '#409EFF' },
        { label: '课程总数', value: 0, color: '#67C23A' },
        { label: '待审核聘用', value: 0, color: '#E6A23C' },
        { label: '待审核薪酬', value: 0, color: '#F56C6C' }
      ]
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
        this.statCards[2].value = d.pendingHireCount
        this.statCards[3].value = d.pendingSalaryCount
        this.renderDeptChart(d.departmentTeacherStats || [])
        this.renderHireChart(d.hireStatusStats || [])
      })
    },
    renderDeptChart(data) {
      const chart = echarts.init(this.$refs.deptChart)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: data.map(i => i.name), axisLabel: { rotate: 20, fontSize: 11 } },
        yAxis: { type: 'value', minInterval: 1 },
        series: [{ type: 'bar', data: data.map(i => i.value), itemStyle: { color: '#409EFF' }, barMaxWidth: 40 }]
      })
    },
    renderHireChart(data) {
      const chart = echarts.init(this.$refs.hireChart)
      chart.setOption({
        tooltip: { trigger: 'item' },
        legend: { bottom: 0 },
        series: [{
          type: 'pie', radius: ['40%', '65%'],
          data: data,
          emphasis: { itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0,0,0,0.5)' } }
        }]
      })
    }
  }
}
</script>
