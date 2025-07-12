<template>
  <div class="news-card" @click="goToDetail">
    <div class="cover-container">
      <img :src="news.coverImage || '/src/assets/images/news/default.jpg'" class="cover">
      <div class="status" v-if="showStatus && news.status !== 'approved'">
        {{ statusText }}
      </div>
    </div>
    <div class="content">
      <h3 class="title">{{ news.title }}</h3>
      <p class="summary">{{ news.summary }}</p>
      <div class="meta">
        <span class="author">{{ news.author?.name || '匿名' }}</span>
        <span class="time">{{ formatTime(news.publishTime) }}</span>
        <span class="views">{{ news.viewCount || 0 }}浏览</span>
      </div>
      <div class="tags">
        <el-tag
            v-for="tag in news.tags"
            :key="tag.id"
            size="small"
            class="tag"
        >
          {{ tag.name }}
        </el-tag>
      </div>
    </div>
    <div class="actions" v-if="showActions">
      <el-button size="small" @click.stop="editNews">编辑</el-button>
      <el-button size="small" type="danger" @click.stop="deleteNews">删除</el-button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { computed } from 'vue'
import {ElMessage, ElMessageBox} from "element-plus";

const props = defineProps({
  news: {
    type: Object,
    required: true
  },
  showStatus: {
    type: Boolean,
    default: false
  },
  showActions: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['edit', 'delete'])

const $router = useRouter()

const statusText = computed(() => {
  const statusMap = {
    pending: '待审核',
    rejected: '已拒绝'
  }
  return statusMap[props.news.status] || ''
})

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleDateString()
}

const goToDetail = () => {
  $router.push(`/news/${props.news.id}`)
}

const editNews = () => {
  emit('edit', props.news)
}

const deleteNews = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该动态吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await newsApi.deleteNews(id)
    emit('deleted', id) // 通知父组件
    ElMessage.success('删除成功')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败: ' + (error.response?.data?.message || error.message))
    }
  }
}
</script>

<style scoped>
.news-card {
  display: flex;
  flex-direction: column;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s;
  cursor: pointer;
  height: 100%;
}

.news-card:hover {
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

.status {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  background-color: #e6a23c;
  color: white;
}

.content {
  padding: 15px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.title {
  margin: 0 0 10px 0;
  font-size: 16px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.summary {
  margin: 0 0 10px 0;
  color: #666;
  font-size: 14px;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.meta {
  margin-top: auto;
  font-size: 12px;
  color: #999;
  display: flex;
  align-items: center;
  gap: 10px;
}

.tags {
  margin-top: 10px;
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.tag {
  margin-right: 5px;
}

.actions {
  padding: 10px;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: flex-end;
}
</style>