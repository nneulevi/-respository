<template>
  <div class="home-view">
    <!-- 轮播图区块 -->
    <section class="banner-section">
      <el-carousel height="300px" :interval="5000" arrow="always">
        <el-carousel-item v-for="banner in banners" :key="banner.id">
          <img :src="banner.imageUrl" class="banner-image" :alt="banner.title">
          <div class="banner-title">{{ banner.title }}</div>
        </el-carousel-item>
      </el-carousel>
    </section>

    <!-- 热门书榜区块 -->
    <section class="section">
      <div class="section-header">
        <h2 class="section-title">📊 热门书榜</h2>
        <router-link to="/books?sort=rating,desc" class="more-link">更多 ></router-link>
      </div>
      <div class="scroll-container">
        <div class="scroll-content">
          <BookCard
              v-for="book in hotBooks"
              :key="'hot-'+book.id"
              :book="book"
              :show-rating="true"
              class="card-item"
          />
        </div>
      </div>
    </section>

    <!-- 精选作品区块 -->
    <section class="section">
      <div class="section-header">
        <h2 class="section-title">🌟 玄幻专区</h2>
        <router-link to="/books?category=玄幻" class="more-link">更多 ></router-link>
      </div>
      <div class="scroll-container">
        <div class="scroll-content">
          <BookCard
              v-for="book in featuredBooks"
              :key="'featured-'+book.id"
              :book="book"
              :show-rating="true"
              class="card-item"
          />
        </div>
      </div>
    </section>

    <!-- 优秀作家区块 -->
    <section class="section">
      <div class="section-header">
        <h2 class="section-title">✍️ 优秀作家</h2>
        <router-link to="/writers" class="more-link">更多 ></router-link>
      </div>
      <div class="scroll-container">
        <div class="scroll-content">
          <div
              v-for="writer in topWriters"
              :key="writer.id"
              class="writer-card"
              @click="goToWriterDetail(writer.id)"
          >
            <img
                :src="writer.avatarUrl"
                class="writer-avatar"
                @error="handleImageError"
            >
            <div class="writer-info">
              <h3>{{ writer.name }}</h3>
              <p>{{ writer.bookCount || 0 }} 部作品</p>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElCarousel, ElCarouselItem } from 'element-plus'
import BookCard from './books/BookCard.vue'
import WriterDetail from "./writers/WriterDetail.vue";
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter();
const banners = ref([])
const hotBooks = ref([])
const featuredBooks = ref([])
const topWriters = ref([])

const fetchHomeData = async () => {
  try {
    const [bannersRes, hotBooksRes, featuredRes, writersRes] = await Promise.all([
      axios.get('/api/banners'),
      axios.get('/api/books', {
        params: {
          size: 8,
          sort: 'rating,desc',
          minRating: 7
        }
      }),
      axios.get('/api/books', {
        params: {
          size: 6,
          category: '玄幻',
          sort: 'rating,desc'
        }
      }),
      axios.get('/api/writers', {
        params: {
          size: 5,
          sort: 'bookCount,desc',
          withAvatar: true
        }
      })
    ])

    banners.value = bannersRes.data
    hotBooks.value = hotBooksRes.data.content.map(book => ({
      ...book,
      coverUrl: book.coverUrl || '/images/default-book.png',
      rating: book.rating || 0
    }))
    featuredBooks.value = featuredRes.data.content.map(book => ({
      ...book,
      coverUrl: book.coverUrl || '/images/default-book.png',
      rating: book.rating || 0
    }))
    topWriters.value = writersRes.data.content.map(writer => ({
      ...writer,
      avatarUrl: writer.avatarUrl || '/images/default-avatar.png',  // 确保有默认头像
      bookCount: writer.bookCount || 0
    }))
  } catch (error) {
    console.error('获取首页数据失败:', error)
  }
}
const goToWriterDetail = (id) => {
  router.push({ name: 'WriterDetail', params: { id } })
}
onMounted(() => {
  fetchHomeData()
})
</script>

<style scoped>
.home-view {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
}

.banner-section {
  margin-bottom: 40px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.banner-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.banner-title {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 15px 20px;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
  color: white;
  font-size: 18px;
}

.section {
  margin-bottom: 50px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 0 10px;
}

.section-title {
  font-size: 22px;
  color: #333;
  margin: 0;
}

.more-link {
  color: #666;
  font-size: 14px;
  text-decoration: none;
  transition: color 0.3s;
}

.more-link:hover {
  color: #ff4d4f;
}

.scroll-container {
  overflow-x: auto;
  padding: 10px 0;
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.scroll-container::-webkit-scrollbar {
  display: none;
}

.scroll-content {
  display: inline-flex;
  gap: 20px;
  padding: 0 10px;
}

.card-item {
  flex: 0 0 auto;
  width: 180px;
}

@media (max-width: 768px) {
  .card-item {
    width: 150px;
  }
}
.writer-card {
  width: 150px;
  cursor: pointer;
  transition: transform 0.3s;
}

.writer-card:hover {
  transform: translateY(-5px);
}

.writer-avatar {
  width: 100%;
  height: 150px;
  object-fit: cover;
  border-radius: 50%;
  margin-bottom: 10px;
}

.writer-info {
  text-align: center;
}

.writer-info h3 {
  margin: 0;
  font-size: 16px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.writer-info p {
  margin: 5px 0 0;
  font-size: 14px;
  color: #666;
}
</style>