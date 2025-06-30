<template>
  <div class="book-detail">
    <el-card v-if="book">
      <template #header>
        <h2>{{ book.title }}</h2>
      </template>
      <div class="detail-content">
        <img
            :src="book.coverUrl || '/images/default-book.png'"
            class="book-cover"
            @error="handleImageError"
        >
        <div class="detail-info">
          <p><strong>作者:</strong> {{ book.authorName || '未知作者' }}</p>
          <p><strong>类型:</strong> {{ book.category || '未分类' }}</p>
          <p><strong>状态:</strong> {{ bookStatus }}</p>
          <p><strong>字数:</strong> {{ formatWordCount(book.wordCount) }}</p>
          <p class="rating-row">
            <strong>评分:</strong>
            <el-rate
                v-model="bookRating"
                disabled
                :max="10"
                :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                allow-half
            />
            <span class="rating-text">{{ bookRating.toFixed(1) }}</span>
          </p>
          <p><strong>简介:</strong> {{ book.description || '暂无简介' }}</p>
        </div>
      </div>
    </el-card>
    <el-skeleton v-else :rows="5" animated />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'
import { ElRate } from 'element-plus'

const route = useRoute()
const book = ref(null)

// 计算属性处理评分
const bookRating = computed(() => {
  return book.value?.rating || 0
})

// 计算属性处理书籍状态
const bookStatus = computed(() => {
  if (!book.value) return ''
  return book.value.status === 'completed' ? '完本' : '连载中'
})

// 格式化字数
const formatWordCount = (count) => {
  if (!count) return '字数未知'
  if (count < 10000) return `${count}字`
  return `${(count / 10000).toFixed(1)}万字`
}

// 图片加载失败处理
const handleImageError = (e) => {
  e.target.src = '/images/default-book.png'
}

onMounted(async () => {
  try {
    const response = await axios.get(`/api/books/${route.params.id}`)
    // 确保数据有默认值
    book.value = {
      ...response.data,
      rating: response.data.rating || 0,
      coverUrl: response.data.coverUrl || '/images/default-book.png'
    }
  } catch (error) {
    console.error('获取书籍详情失败:', error)
  }
})
</script>

<style scoped>
.book-detail {
  max-width: 800px;
  margin: 20px auto;
  padding: 0 20px;
}

.detail-content {
  display: flex;
  gap: 30px;
}

.book-cover {
  width: 250px;
  height: 350px;
  object-fit: cover;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.detail-info {
  flex: 1;
}

.detail-info p {
  margin-bottom: 15px;
  font-size: 16px;
  line-height: 1.6;
}

.rating-row {
  display: flex;
  align-items: center;
  gap: 10px;
}

.rating-text {
  color: #FF9900;
  font-weight: bold;
  font-size: 16px;
}

@media (max-width: 768px) {
  .detail-content {
    flex-direction: column;
  }

  .book-cover {
    width: 100%;
    height: auto;
    max-height: 400px;
  }
}
</style>