<template>
  <div class="writer-detail">
    <el-card v-if="writer">
      <template #header>
        <div class="writer-header">
          <el-avatar :size="120" :src="getImageUrl(writer.avatarUrl)" @error="handleImageError" />
          <div class="header-info">
            <h2>{{ writer.name }}</h2>
            <div class="meta">
              <el-tag :type="writer.gender == 1 ? 'danger' : 'primary'">  <!-- 用 == 而不是 === -->
                {{ writer.gender == 1 ? '男' : '女' }}  <!-- 松散匹配 -->
              </el-tag>
              <span class="country">{{ writer.country }}</span>
              <span v-if="writer.birthYear" class="age">
                {{ new Date().getFullYear() - parseInt(writer.birthYear) }}岁
              </span>
            </div>
          </div>
          <div class="header-actions">

            <el-button @click="goBack">返回</el-button>
          </div>
        </div>
      </template>

      <el-tabs>
        <el-tab-pane label="基本信息">
          <div class="info-section">
            <h3>个人简介</h3>
            <p class="description">{{ writer.description || '暂无简介' }}</p>
          </div>

          <div class="info-section">
            <h3>代表作</h3>
            <p>{{ writer.representativeWork || '暂无' }}</p>
          </div>

          <div class="info-section">
            <h3>热门作品</h3>
            <div v-if="booksLoading" class="loading-container">
              <el-skeleton :rows="3" animated />
            </div>
            <div v-else-if="books.length" class="book-grid-container">
              <div class="book-grid">
                <BookCard
                    v-for="book in books"
                    :key="book.id"
                    :book="book"
                    show-rating
                    class="book-card"
                />
              </div>
            </div>
            <el-empty v-else description="暂无作品数据" :image-size="100" />
          </div>
        </el-tab-pane>

        <el-tab-pane label="更多信息">
          <div class="info-section">
            <h3>外部链接</h3>
            <el-link
                v-if="writer.externalUrl"
                :href="writer.externalUrl"
                target="_blank"
                type="primary"
            >
              百科页面
            </el-link>
            <p v-else>暂无</p>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <el-skeleton v-else :rows="10" animated />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  ElMessage,
  ElCard,
  ElTabs,
  ElTabPane,
  ElTag,
  ElButton,
  ElLink,
  ElEmpty,
  ElSkeleton
} from 'element-plus'
import BookCard from '../books/BookCard.vue'
import axios from 'axios'

const route = useRoute()
const router = useRouter()
const writer = ref(null)
const books = ref([])
const booksLoading = ref(false)

// 处理图片路径（适应开发和生产环境）
const getImageUrl = (path) => {
  if (!path) return '/images/default-avatar.png'
  if (path.startsWith('/src/assets')) {
    return new URL(path, import.meta.url).href
  }
  return path
}

// 获取作家详情
const fetchWriterDetail = async () => {
  try {
    const response = await axios.get(`/api/writers/${route.params.id}`)
    writer.value = response.data
    // 获取该作家的作品
    await fetchBooksByWriter()
  } catch (error) {
    console.error('获取作家详情失败:', error)
    ElMessage.error('获取作家详情失败')
    router.push('/writers')
  }
}

// 根据作家ID获取作品列表
const fetchBooksByWriter = async () => {
  booksLoading.value = true
  try {
    const response = await axios.get('/api/books', {
      params: {
        authorId: route.params.id,
        pageSize: 10,
        pageNum: 1
      }
    })
    books.value = response.data.content.map(book => ({
      id: book.id,
      title: book.title,
      coverUrl: getImageUrl(book.coverUrl),
      authorName: writer.value.name,
      rating: book.rating,
      category: book.category,
      wordCount: book.wordCount,
      status: book.status === '1' ? 'completed' : 'serializing'
    }))
  } catch (error) {
    console.error('获取作品列表失败:', error)
    ElMessage.error('获取作品列表失败')
  } finally {
    booksLoading.value = false
  }
}



const goBack = () => {
  router.push('/writers')
}

const handleImageError = (e) => {
  e.target.src = '/images/default-avatar.png'
}

onMounted(() => {
  fetchWriterDetail()
})
</script>

<style scoped>
.writer-detail {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}

.writer-header {
  display: flex;
  align-items: center;
  gap: 30px;
}

.header-info {
  flex: 1;
}

.header-info h2 {
  margin: 0 0 10px 0;
  font-size: 24px;
}

.meta {
  display: flex;
  align-items: center;
  gap: 15px;
}

.country, .age {
  color: #666;
  font-size: 14px;
}

.header-actions {
  margin-left: auto;
}

.info-section {
  margin-bottom: 30px;
}

.info-section h3 {
  margin: 0 0 15px 0;
  font-size: 18px;
  color: #333;
  border-left: 4px solid #409eff;
  padding-left: 10px;
}

.description {
  line-height: 1.8;
  color: #444;
}

.book-grid-container {
  width: 100%;
  overflow-x: auto;
  padding-bottom: 20px;
}

.book-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 24px;
  padding: 10px 5px;
  min-width: fit-content;
}

.book-card {
  height: 100%;
  min-height: 320px;
  transition: transform 0.3s ease;
}

.book-card:hover {
  transform: translateY(-5px);
}

.loading-container {
  padding: 20px;
  background: #f5f7fa;
  border-radius: 4px;
}

@media (max-width: 768px) {
  .book-grid {
    grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
    gap: 16px;
  }

  .writer-header {
    flex-direction: column;
    gap: 20px;
  }

  .header-actions {
    margin-left: 0;
    width: 100%;
    justify-content: flex-end;
  }
}
</style>