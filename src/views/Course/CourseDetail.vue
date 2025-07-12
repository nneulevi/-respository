<template>
  <div class="course-detail">
    <div class="back-button">
      <el-button type="primary" plain @click="goBack">
        <el-icon><ArrowLeft /></el-icon>
        返回首页
      </el-button>
    </div>
    <el-card shadow="hover" class="detail-card">
      <template #header>
        <div class="header">
          <h1>{{ course.title || '加载中...' }}</h1>
          <div class="meta">
            <div class="meta-item">
              <el-icon><User /></el-icon>
              <span class="author">{{ course.author || '匿名' }}</span>
            </div>
            <el-divider direction="vertical" />
            <div class="meta-item">
              <el-icon><Clock /></el-icon>
              <span class="duration">{{ formatDuration(course.duration) }}</span>
            </div>
            <el-divider direction="vertical" />
            <div class="meta-item">
              <el-icon><View /></el-icon>
              <span class="views">{{ course.viewCount || 0 }}浏览</span>
            </div>
            <el-tag
                v-if="course.status && course.status !== 'published'"
                :type="course.status === 'draft' ? 'info' : 'warning'"
                class="status-tag"
            >
              {{ statusText }}
            </el-tag>
          </div>
        </div>
      </template>

      <div v-if="loading" class="loading-placeholder">
        <el-skeleton :rows="3" animated />
      </div>

      <template v-else>
        <div class="content" v-html="course.content || '<p>暂无内容</p>'"></div>

        <div class="video-container" v-if="course.videoUrl">
          <video controls>
            <source :src="course.videoUrl" type="video/mp4">
            您的浏览器不支持HTML5视频
          </video>
        </div>

        <div class="cover-container" v-if="course.coverImage">
          <img :src="course.coverImage" class="cover">
        </div>

        <div class="additional-content">
          <h3>课程详细信息</h3>
          <p>本课程由{{ course.author }}教授主讲，总时长{{ formatDuration(course.duration) }}，适合所有对人工智能感兴趣的学员学习。</p>

          <h3>学习目标</h3>
          <ul>
            <li>理解人工智能的基本概念和发展历程</li>
            <li>掌握机器学习和深度学习的基础知识</li>
            <li>能够应用AI技术解决简单实际问题</li>
            <li>了解AI领域的最新发展趋势</li>
          </ul>
        </div>

        <div class="categories" v-if="course.categories?.length">
          <el-tag
              v-for="(category, index) in course.categories"
              :key="category.id"
              :class="['category-item', `category-color-${index % 3}`]"
              size="small"
              effect="plain"
          >
            {{ category.name }}
          </el-tag>
        </div>

        <div class="actions" v-if="canEdit">
          <el-button type="primary" @click="editCourse" round>
            <el-icon><Edit /></el-icon>
            编辑
          </el-button>
          <el-button type="danger" @click="deleteCourse" round>
            <el-icon><Delete /></el-icon>
            删除
          </el-button>
        </div>
      </template>
    </el-card>

    <CourseDialog
        v-model="dialogVisible"
        :course="currentCourse"
        :all-categories="categories"
        :is-admin="isAdmin"
        @submit="handleSubmit"
    />
  </div>
</template>

<script setup>
import {ref, onMounted, computed} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {ElMessage, ElMessageBox, ElIcon} from 'element-plus'
import {User, Clock, View, Edit, Delete, ArrowLeft} from '@element-plus/icons-vue'
import CourseDialog from './CourseDialog.vue'
import api from '@/api/course'
import {useUserStore} from '@/stores/user'


// 在方法部分添加返回函数
const goBack = () => {
  router.push('/')
}
const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

// 响应式数据
const course = ref({
  id: null,
  title: '',
  content: '',
  videoUrl: '',
  coverImage: '',
  duration: 0,
  author: '',
  viewCount: 0,
  status: 'published',
  categories: []
})
const loading = ref(true)
const dialogVisible = ref(false)
const currentCourse = ref(null)

// 计算属性
const canEdit = computed(() => {
  return userStore.isSuperAdmin || course.value.author === userStore.currentUser?.username
})

const statusText = computed(() => {
  const statusMap = {draft: '草稿', pending: '待审核', published: '已发布'}
  return statusMap[course.value.status] || ''
})

// 方法
const formatDuration = (seconds) => {
  if (!seconds) return '0分钟'
  const hours = Math.floor(seconds / 3600)
  const minutes = Math.floor((seconds % 3600) / 60)

  let result = ''
  if (hours > 0) result += `${hours}小时`
  if (minutes > 0) result += `${minutes}分钟`

  return result || '0分钟'
}

const fetchCourseDetail = async () => {
  loading.value = true
  try {
    console.log('开始获取课程详情，ID:', route.params.id)
    const res = await api.getCourseDetail(route.params.id)
    console.log('课程详情获取成功:', res.data)

    course.value = {
      id: res.data.id,
      title: res.data.title,
      content: res.data.content,
      videoUrl: res.data.videoUrl,
      coverImage: res.data.coverImage || '/src/assets/images/course/default.jpg',
      duration: res.data.duration,
      author: res.data.author,
      viewCount: res.data.viewCount || 0,
      status: res.data.status || 'published',
      categories: res.data.categories || []
    }
  } catch (error) {
    console.error('加载失败:', error)
    ElMessage.error('加载课程失败')
  } finally {
    loading.value = false
  }
}

const editCourse = () => {
  console.log('编辑课程:', course.value)
  currentCourse.value = {...course.value}
  dialogVisible.value = true
}

const handleSubmit = async (courseData) => {
  try {
    console.log('提交课程更新:', courseData)

    const formattedData = {
      id: courseData.id,
      title: courseData.title,
      summary: courseData.summary,
      now_username: userStore.currentUser?.username
    }

    await api.updateCourse(formattedData)
    console.log('课程更新成功')

    ElMessage.success('更新成功')
    fetchCourseDetail()
    dialogVisible.value = false
  } catch (error) {
    console.error('更新失败:', error)
    ElMessage.error(error.response?.data?.message || '更新失败')
  }
}

const deleteCourse = async () => {
  try {
    await ElMessageBox.confirm('确定删除该课程吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    console.log('开始删除课程，ID:', course.value.id)
    await api.deleteCourse(course.value.id)
    console.log('课程删除成功')

    ElMessage.success('删除成功')
    router.push('/courses')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error(error.response?.data?.message || '删除失败')
    }
  }
}

const updateLikes = async () => {
  try {
    console.log('更新点赞数，课程ID:', course.value.id)
    await api.updateLikes(course.value.id)
    console.log('点赞数更新成功')
    fetchCourseDetail()
  } catch (error) {
    console.error('点赞失败:', error)
    ElMessage.error('点赞失败')
  }
}

// 生命周期
onMounted(() => {
  console.log('课程详情页初始化')
  fetchCourseDetail()
})
</script>

<style scoped lang="scss">
.course-detail {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;

  .detail-card {
    border-radius: 12px;
    overflow: hidden;
    transition: all 0.3s ease;

    &:hover {
      box-shadow: 0 6px 18px rgba(0, 0, 0, 0.1);
    }
  }
}

.header {
  padding-bottom: 15px;

  h1 {
    margin: 0 0 16px 0;
    font-size: 26px;
    font-weight: 600;
    color: #333;
    line-height: 1.4;
  }

  .meta {
    display: flex;
    align-items: center;
    gap: 8px;
    color: #666;
    font-size: 14px;
    flex-wrap: wrap;

    .meta-item {
      display: flex;
      align-items: center;
      gap: 4px;

      .el-icon {
        font-size: 16px;
      }
    }

    .status-tag {
      margin-left: 8px;
    }
  }
}

.content {
  line-height: 1.8;
  font-size: 16px;
  color: #444;
  margin-bottom: 24px;

  :deep(h3) {
    color: var(--el-color-primary);
    margin: 24px 0 16px;
  }

  :deep(ol),
  :deep(ul) {
    padding-left: 28px;
    margin: 16px 0;
    color: #555;
  }

  :deep(li) {
    margin-bottom: 10px;
    position: relative;

    &::before {
      content: "•";
      color: var(--el-color-primary);
      position: absolute;
      left: -15px;
    }
  }

  :deep(strong) {
    color: var(--el-color-primary);
    background: rgba(64, 158, 255, 0.1);
    padding: 2px 4px;
    border-radius: 3px;
  }
}

.video-container {
  margin: 30px 0;
  text-align: center;

  video {
    width: 100%;
    max-width: 800px;
    height: auto;
    border-radius: 8px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
    background: #000;
    display: inline-block;
  }
}

.cover-container {
  margin: 30px 0;
  text-align: center;

  .cover {
    max-width: 100%;
    max-height: 500px;
    object-fit: contain;
    border-radius: 8px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  }
}

.additional-content {
  margin: 30px 0;
  padding: 20px;
  background: #f9fafc;
  border-radius: 8px;
  border-left: 4px solid var(--el-color-primary);

  h3 {
    margin-top: 0;
    color: var(--el-color-primary);
  }

  ul {
    padding-left: 24px;

    li {
      margin-bottom: 10px;
      color: #555;
    }
  }
}

.categories {
  margin: 24px 0;
  display: flex;
  gap: 10px;
  flex-wrap: wrap;

  .category-item {
    cursor: pointer;
    transition: all 0.2s ease;
    border: none !important;
    font-weight: 500;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
    }

    &.category-color-0 {
      background: rgba(144, 238, 144, 0.2); /* 淡绿色 */
      color: #2e8b57;
    }

    &.category-color-1 {
      background: rgba(173, 216, 230, 0.2); /* 淡蓝色 */
      color: #4682b4;
    }

    &.category-color-2 {
      background: rgba(255, 182, 193, 0.2); /* 淡粉色 */
      color: #db7093;
    }
  }
}

.actions {
  margin-top: 24px;
  padding-top: 16px;
  border-top: 1px dashed #eee;
  text-align: right;

  .el-button {
    margin-left: 12px;

    .el-icon {
      margin-right: 4px;
    }
  }
}

.loading-placeholder {
  padding: 40px 20px;
}

@media (max-width: 768px) {
  .course-detail {
    padding: 12px;
  }

  .header h1 {
    font-size: 22px;
  }

  .meta {
    gap: 6px;

    .el-divider {
      margin: 0 4px;
    }
  }

  .content {
    font-size: 15px;
  }
}
</style>