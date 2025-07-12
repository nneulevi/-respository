<template>
  <div class="search-results-container">
    <!-- 搜索头部 -->
    <div class="search-header">
      <h2>搜索结果：{{ route.query.q }}</h2>
      <div class="search-count">
        <span v-if="writerResults.length > 0">{{ writerResults.length }} 位作家</span>
        <span v-if="bookResults.length > 0">{{ bookResults.length }} 部作品</span>
      </div>
    </div>

    <!-- 作家结果 -->
    <section v-if="writerResults.length > 0" class="result-section">
      <h3 class="section-title">
        <el-icon><User /></el-icon>
        相关作家
      </h3>
      <div class="writer-list">
        <div
            v-for="writer in writerResults"
            :key="writer.id"
            class="writer-card"
            @click="goToWriterDetail(writer.id)"
        >
          <div class="card-header">
            <img
                :src="writer.avatarUrl"
                class="avatar"
                @error="handleImageError"
            >
            <div class="writer-info">
              <h3>{{ writer.name }}</h3>
              <p class="meta">
                <span>{{ writer.genderText }}</span>
                <span>·</span>
                <span>{{ writer.country || '未知地区' }}</span>
                <span>·</span>
                <span>{{ writer.bookCount || 0 }} 部作品</span>
              </p>
            </div>
          </div>
          <div class="card-body">
            <p class="description">{{ writer.description || '暂无简介' }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 书籍结果 -->
    <section v-if="bookResults.length > 0" class="result-section">
      <h3 class="section-title">
        <el-icon><Notebook /></el-icon>
        {{ writerResults.length > 0 ? '该作家的作品' : '相关作品' }}
      </h3>
      <div class="book-list">
        <BookCard
            v-for="book in bookResults"
            :key="book.id"
            :book="book"
            :show-rating="true"
            class="book-card"
        />
      </div>
    </section>

    <!-- 无结果提示 -->
    <el-empty
        v-if="showNoResults"
        description="未找到相关内容"
        :image-size="200"
    >
      <template #description>
        <p>没有找到与 "{{ route.query.q }}" 相关的内容</p>
        <el-button type="primary" @click="goToHome">返回首页</el-button>
      </template>
    </el-empty>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-state">
      <el-icon class="is-loading"><Loading /></el-icon>
      正在搜索 "{{ route.query.q }}"...
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElEmpty, ElIcon, ElButton } from 'element-plus'
import { User, Notebook, Loading } from '@element-plus/icons-vue'
import BookCard from './books/BookCard.vue'
import axios from 'axios'

const route = useRoute()
const router = useRouter()

const writerResults = ref([])
const bookResults = ref([])
const loading = ref(false)

const showNoResults = computed(() => {
  return !loading.value && bookResults.value.length === 0 && writerResults.value.length === 0
})

const performSearch = async (keyword) => {
  try {
    loading.value = true
    const response = await axios.get('/api/search', {
      params: { keyword }
    })

    writerResults.value = (response.data.writers || []).map(writer => ({
      ...writer,
      avatarUrl: writer.avatarUrl || '/images/default-avatar.png',
      genderText: writer.gender === 0 ? '女' : '男'
    }))

    bookResults.value = (response.data.books || []).map(book => ({
      ...book,
      coverUrl: book.coverUrl || '/images/default-book.png',
      rating: book.rating || 0
    }))

  } catch (error) {
    console.error('搜索失败:', error)
  } finally {
    loading.value = false
  }
}

const handleImageError = (e) => {
  const img = e.target
  if (img.classList.contains('avatar')) {
    img.src = '/images/default-avatar.png'
  } else {
    img.src = '/images/default-book.png'
  }
}

const goToWriterDetail = (id) => {
  router.push({ name: 'WriterDetail', params: { id } })
}

const goToHome = () => {
  router.push({ name: 'home' })
}

watch(() => route.query.q, (newVal) => {
  if (newVal) performSearch(newVal)
}, { immediate: true })
</script>

<style scoped>
.search-results-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.search-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.search-header h2 {
  margin: 0;
  font-size: 24px;
  color: #333;
}

.search-count {
  color: #666;
  font-size: 14px;
}

.search-count span {
  margin-left: 15px;
}

.result-section {
  margin-bottom: 40px;
}

.section-title {
  display: flex;
  align-items: center;
  margin: 0 0 20px 0;
  font-size: 20px;
  color: #333;
  padding-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
}

.section-title .el-icon {
  margin-right: 8px;
  font-size: 24px;
}

.writer-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
}

.writer-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.writer-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 15px;
}

.writer-info h3 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.meta {
  margin: 8px 0 0;
  font-size: 14px;
  color: #909399;
}

.meta span {
  margin-right: 8px;
}

.description {
  margin: 0;
  color: #666;
  font-size: 14px;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.book-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 25px;
}

.loading-state {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px;
  color: #666;
  gap: 10px;
}

@media (max-width: 768px) {
  .writer-list {
    grid-template-columns: 1fr;
  }

  .search-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .search-count {
    margin-top: 10px;
  }
}
</style>