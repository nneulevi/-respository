<template>
  <div class="stats-container">
    <div class="stats-header">
      <h1 class="stats-title">书籍数据统计</h1>
      <el-button type="primary" @click="goToDashboard" class="back-button">
        <el-icon><ArrowLeft /></el-icon>
        返回首页
      </el-button>
    </div>

    <div class="charts-wrapper">
      <!-- 类型分布图表 -->
      <div class="chart-card">
        <div class="chart-title">书籍类型分布</div>
        <div v-loading="loadingCategories" ref="categoryChart" class="chart"></div>
      </div>

      <!-- 评分分布图表 -->
      <div class="chart-card">
        <div class="chart-title">书籍评分分布</div>
        <div v-loading="loadingRatings" ref="ratingChart" class="chart"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'
import { ArrowLeft } from '@element-plus/icons-vue'
import { ElButton, ElIcon } from 'element-plus'
import axios from 'axios'

const router = useRouter()
const categoryChart = ref(null)
const ratingChart = ref(null)
const loadingCategories = ref(true)
const loadingRatings = ref(true)

// 颜色方案
const colors = [
  '#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de',
  '#3ba272', '#fc8452', '#9a60b4', '#ea7ccc', '#7cd6cf'
]

const fetchDataAndInitCharts = async () => {
  try {
    // 获取类型分布数据
    const categoryRes = await axios.get('/api/stats/categories')
    initCategoryChart(categoryRes.data)

    // 获取评分分布数据
    const ratingRes = await axios.get('/api/stats/ratings')
    initRatingChart(ratingRes.data)
  } catch (error) {
    console.error('获取统计数据失败:', error)
  } finally {
    loadingCategories.value = false
    loadingRatings.value = false
  }
}

const initCategoryChart = (data) => {
  const chartInstance = echarts.init(categoryChart.value)
  const sortedData = Object.entries(data)
      .sort((a, b) => b[1] - a[1])
      .map(([name, value], index) => ({
        name,
        value,
        itemStyle: { color: colors[index % colors.length] }
      }))

  chartInstance.setOption({
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c}本 ({d}%)'
    },
    legend: {
      orient: 'vertical',
      right: 10,
      top: 'center',
      data: sortedData.map(item => item.name)
    },
    series: [
      {
        name: '类型分布',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '18',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: sortedData
      }
    ]
  })
}

const initRatingChart = (data) => {
  const chartInstance = echarts.init(ratingChart.value)
  const ratingRanges = ['0-8分', '8-9分', '9-9.3分', '9.3-9.7分', '9.7-10分']
  const chartData = ratingRanges.map(range => data[range] || 0)

  chartInstance.setOption({
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      },
      formatter: '评分区间 {b}: {c}本'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: ratingRanges,
      axisLabel: {
        color: '#666'
      },
      axisLine: {
        lineStyle: {
          color: '#ddd'
        }
      }
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        color: '#666'
      },
      axisLine: {
        show: false
      },
      splitLine: {
        lineStyle: {
          color: '#f0f0f0'
        }
      }
    },
    series: [
      {
        name: '评分分布',
        type: 'bar',
        barWidth: '60%',
        data: chartData.map((value, index) => ({
          value,
          itemStyle: {
            color: colors[index]
          }
        })),
        itemStyle: {
          borderRadius: [4, 4, 0, 0]
        }
      }
    ]
  })
}

const goToDashboard = () => {
  router.push('/dashboard')
}

onMounted(() => {
  fetchDataAndInitCharts()
  window.addEventListener('resize', () => {
    if (categoryChart.value) {
      echarts.getInstanceByDom(categoryChart.value)?.resize()
    }
    if (ratingChart.value) {
      echarts.getInstanceByDom(ratingChart.value)?.resize()
    }
  })
})
</script>

<style scoped>
.stats-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 30px;
}

.stats-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 40px;
}

.stats-title {
  font-size: 28px;
  color: #333;
  margin: 0;
}

.back-button {
  padding: 10px 20px;
  font-size: 14px;
}

.charts-wrapper {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(500px, 1fr));
  gap: 30px;
}

.chart-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  padding: 20px;
  transition: transform 0.3s ease;
}

.chart-card:hover {
  transform: translateY(-5px);
}

.chart-title {
  font-size: 18px;
  font-weight: 600;
  color: #444;
  margin-bottom: 20px;
  text-align: center;
}

.chart {
  width: 100%;
  height: 400px;
}

@media (max-width: 768px) {
  .charts-wrapper {
    grid-template-columns: 1fr;
  }

  .stats-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 20px;
  }

  .chart {
    height: 300px;
  }
}
</style>