<template>
  <div class="course-card" @click="$router.push(`/courses/${course.id}`)">
    <div class="cover-container">
      <img :src="course.coverImage || '/src/assets/images/course/default.jpg'" class="cover">
      <div class="duration">{{ formatDuration(course.duration) }}</div>
      <div class="status" :class="course.status" v-if="showStatus">
        {{ getStatusText(course.status) }}
      </div>
    </div>
    <h3 class="course-title">{{ course.title }}</h3>
    <p class="course-author">{{ course.author }}</p>
    <div class="categories">
      <el-tag
          v-for="category in course.categories"
          :key="category.id"
          size="small"
          class="category-tag"
      >
        {{ category.name }}
      </el-tag>

    </div>

  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const props = defineProps({
  course: {
    type: Object,
    required: true
  },
  showStatus: {
    type: Boolean,
    default: false
  }
})

const router = useRouter()

// 格式化时长（对接后端返回的秒数）
const formatDuration = (seconds) => {
  console.log('接收到的课程时长(秒):', seconds) // 调试日志
  if (!seconds) return '0分钟'
  const minutes = Math.floor(seconds / 60)
  return `${minutes}分钟`
}

// 状态文本映射（对接后端状态字段）
const getStatusText = (status) => {
  console.log('当前课程状态:', status) // 调试日志
  const statusMap = {
    pending: '待审核',
    approved: '已通过',
    rejected: '已拒绝',
    published: '已发布' // 根据您的API可能需要的状态
  }
  return statusMap[status] || status
}
</script>

<style scoped>
.course-card {
  cursor: pointer;
  transition: transform 0.3s;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.course-card:hover {
  transform: translateY(-5px);
}

.cover-container {
  position: relative;
  width: 100%;
  height: 160px;
}

.cover {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.duration {
  position: absolute;
  right: 8px;
  bottom: 8px;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.status {
  position: absolute;
  left: 8px;
  top: 8px;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  color: white;
}

.status.pending {
  background-color: #e6a23c;
}

.status.approved {
  background-color: #67c23a;
}

.status.rejected {
  background-color: #f56c6c;
}

.course-title {
  margin: 10px 10px 5px;
  font-size: 16px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.course-author {
  margin: 0 10px 10px;
  color: #666;
  font-size: 14px;
}

.categories {
  margin: 0 10px 10px;
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.category-tag {
  margin-right: 5px;
}
</style>