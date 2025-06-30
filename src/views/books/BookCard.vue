<template>
  <div class="book-card" @click="handleClick">
    <div class="cover-container">
      <img
          :src="book.coverUrl || '/images/default-book.png'"
          class="book-cover"
          @error="handleImageError"
          alt="书籍封面"
      >
      <div v-if="showRating" class="rating-badge">
        <el-rate
            v-model="displayRating"
            disabled
            :max="10"
            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
            allow-half
            text-color="#ff9900"
        />
        <span class="rating-text">{{ displayRating.toFixed(1) }}</span>
      </div>
      <div v-if="book.status" class="status-badge">
        {{ book.status === 'completed' ? '完本' : '连载' }}
      </div>
    </div>

    <h3 class="book-title">{{ book.title || '未知书名' }}</h3>
    <p class="book-author">{{ book.authorName || '未知作者' }}</p>
    <div class="book-meta">
      <span class="category">{{ book.category || '未分类' }}</span>
      <span class="word-count">{{ formatWordCount(book.wordCount) }}</span>
    </div>
    <el-progress
        v-if="showProgress"
        :percentage="book.readingProgress || 0"
        :stroke-width="6"
        :show-text="false"
    />
  </div>
</template>

<script setup>
import { ElRate, ElProgress } from 'element-plus'
import { useRouter } from 'vue-router'
import { computed } from 'vue'

const router = useRouter()

const props = defineProps({
  book: {
    type: Object,
    required: true,
    validator: (book) => {
      return 'id' in book && 'title' in book
    }
  },
  showRating: {
    type: Boolean,
    default: false
  },
  showProgress: {
    type: Boolean,
    default: false
  }
})

const displayRating = computed(() => {
  return props.book.rating || 0
})

const formatWordCount = (count) => {
  if (!count && count !== 0) return '字数未知'
  if (count < 10000) return `${count}字`
  return `${(count / 10000).toFixed(1)}万字`
}

const handleClick = () => {
  router.push(`/books/${props.book.id}`)
}

const handleImageError = (e) => {
  e.target.src = '/images/default-book.png'
}
</script>

<style scoped>
.book-card {
  cursor: pointer;
  transition: all 0.3s ease;
  height: 100%;
  display: flex;
  flex-direction: column;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.book-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.cover-container {
  position: relative;
  width: 100%;
  height: 0;
  padding-bottom: 140%;
  margin-bottom: 12px;
  border-radius: 4px;
  overflow: hidden;
}

.book-cover {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  background-color: #f5f5f5;
  transition: transform 0.5s ease;
}

.book-card:hover .book-cover {
  transform: scale(1.05);
}

.rating-badge {
  display: flex;
  align-items: center;
  position: absolute;
  bottom: 8px;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 4px 8px;
  z-index: 2;
}

.status-badge {
  position: absolute;
  top: 8px;
  right: 8px;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 12px;
  z-index: 2;
}

.rating-text {
  margin-left: 8px;
  font-size: 14px;
  font-weight: bold;
  color: #FF9900;
}

.book-title {
  font-size: 15px;
  margin: 0 0 6px;
  font-weight: 500;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  min-height: 42px;
  line-height: 1.4;
  padding: 0 8px;
}

.book-author {
  font-size: 13px;
  color: #666;
  margin: 0 0 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  padding: 0 8px;
}

.book-meta {
  display: flex;
  justify-content: space-between;
  margin-top: auto;
  font-size: 12px;
  padding: 0 8px 8px;
}

.category {
  background: #f0f2f5;
  color: #666;
  padding: 2px 6px;
  border-radius: 4px;
}

.word-count {
  color: #909399;
}

.el-progress {
  margin-top: auto;
}

@media (max-width: 768px) {
  .book-title {
    font-size: 14px;
    min-height: 38px;
  }

  .book-author {
    font-size: 12px;
  }
}
</style>