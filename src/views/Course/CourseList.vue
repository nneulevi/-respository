<template>
  <div class="course-page">
    <!-- 热门课程榜 -->
    <div class="course-top-list">
      <h2>🎯 热门课程榜</h2>
      <div class="top-courses">
        <div
            v-for="(course, index) in topCourses"
            :key="course.id"
            class="top-course-card"
            :class="'rank-' + (index + 1)"
            @click="goToCourseDetail(course.id)"
        >
          <span class="rank">{{ index + 1 }}</span>
          <img :src="course.coverImage || '/src/assets/images/course/default.jpg'" class="cover">
          <div class="info">
            <h3>{{ course.title }}</h3>
            <p class="meta">
              <span>{{ course.author }}</span>
              <span>·</span>
              <span>{{ formatDuration(course.duration) }}</span>
            </p>
          </div>
        </div>
      </div>
    </div>

    <!-- 筛选栏 -->
    <div class="filter-bar">
      <el-input
          v-model="filters.title"
          placeholder="搜索课程"
          clearable
          style="width: 200px"
          @keyup.enter="fetchCourses"
      >
        <template #prefix>
          <el-icon><search /></el-icon>
        </template>
      </el-input>

      <el-select
          v-model="filters.category"
          placeholder="课程分类"
          clearable
          style="width: 180px; margin-left: 10px"
      >
        <el-option
            v-for="category in allCategories"
            :key="category.id"
            :label="category.name"
            :value="category.id"
        />
      </el-select>

      <el-select
          v-model="filters.duration"
          placeholder="时长筛选"
          clearable
          style="width: 150px; margin-left: 10px"
      >
        <el-option label="0-30分钟" value="short" />
        <el-option label="30-60分钟" value="medium" />
        <el-option label="60分钟以上" value="long" />
      </el-select>

      <el-button
          type="primary"
          style="margin-left: 10px"
          @click="fetchCourses"
      >
        筛选
      </el-button>
    </div>

    <!-- 课程列表 -->
    <div class="course-list">
      <div
          v-for="course in courses"
          :key="course.id"
          class="course-card"
          @click="goToCourseDetail(course.id)"
      >
        <div class="card-media">
          <img :src="course.coverImage || '/src/assets/images/course/default.jpg'" class="cover">
          <div class="duration">{{ formatDuration(course.duration) }}</div>
        </div>
        <div class="card-content">
          <h3>{{ course.title }}</h3>
          <p class="author">{{ course.author }}</p>
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
          <p class="content-summary">{{ truncateContent(course.content) }}</p>
        </div>
        <div class="card-footer">
          <el-button type="primary" size="small" @click.stop="editCourse(course)">编辑</el-button>
          <el-button type="danger" size="small" @click.stop="deleteCourse(course.id)">删除</el-button>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          layout="prev, pager, next"
          :page-sizes="[12, 24, 36]"
          @current-change="fetchCourses"
      />
    </div>

    <!-- 添加按钮 -->
    <el-button
        type="primary"
        circle
        class="add-button"
        @click="showAddDialog"
    >
      <el-icon><plus /></el-icon>
    </el-button>

    <!-- 添加/编辑对话框 -->
    <course-dialog
        v-model="dialogVisible"
        :course="currentCourse"
        :categories="allCategories"
        :isSuperAdmin="isSuperAdmin"
        @submit="handleSubmit"
    />
  </div>
</template>

<script setup>
import {ref, onMounted, computed} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Search, Plus, View} from '@element-plus/icons-vue'
import CourseDialog from './CourseDialog.vue'
import api from '@/api/course'
import {useUserStore} from '@/stores/user'
import {useRouter} from 'vue-router'

const router = useRouter()
const userStore = useUserStore()

// 计算属性获取用户信息
const isSuperAdmin = computed(() => userStore.isSuperAdmin)
const currentUser = computed(() => userStore.currentUser)
const username = computed(() => userStore.currentUser?.username || 'admin')

// 数据
const topCourses = ref([])
const courses = ref([])
const allCategories = ref([
  { id: 1, name: '编程开发' },
  { id: 2, name: '产品设计' },
  { id: 3, name: '数据分析' },
  { id: 4, name: '人工智能' },
  { id: 5, name: '网络安全' },
  { id: 6, name: '云计算' },
  { id: 7, name: '职场技能' }
])
const loading = ref(false)

// 筛选条件
const filters = ref({
  title: '',
  category: null,
  duration: ''
})

// 分页
const pagination = ref({
  current: 1,
  size: 12,
  total: 0
})

// 对话框
const dialogVisible = ref(false)
const currentCourse = ref(null)

// 格式化时长
const formatDuration = (seconds) => {
  if (!seconds) return '0分钟'
  const minutes = Math.floor(seconds / 60)
  return `${minutes}分钟`
}

// 截断内容
const truncateContent = (content) => {
  if (!content) return '暂无简介'
  const text = content.replace(/<[^>]*>/g, '')
  return text.length > 100 ? text.substring(0, 100) + '...' : text
}

// 获取热门课程
const fetchTopCourses = async () => {
  try {
    console.log('开始获取热门课程...')
    const res = await api.getCoursesByFilter({
      size: 5,
      sortBy: 'likes'
    })
    console.log('热门课程获取成功:', res)
    topCourses.value = res.data.map(course => ({
      id: course.id,
      title: course.title,
      coverImage: course.coverImage || '/src/assets/images/course/default.jpg',
      duration: course.duration,
      author: course.author,
      likes: course.likes || 0
    }))
  } catch (error) {
    console.error('获取热门课程失败:', error)
    ElMessage.error('获取热门课程失败')
  }
}

const fetchCourses = async () => {
  loading.value = true;
  try {
    const params = {
      page: pagination.value.current,
      size: pagination.value.size,
      title: filters.value.title,
      category: filters.value.category,
      duration: filters.value.duration,
    };

    console.log('请求参数:', params);  // 打印请求参数

    const res = await api.getCoursesByFilter(params);
    console.log('API返回原始数据:', {
      data: res.data,
      total: res.total,
      isSuperAdmin: isSuperAdmin.value,
      currentUser: username.value
    });

    // 打印所有课程的status情况
    const statusCount = res.data.reduce((acc, course) => {
      acc[course.status] = (acc[course.status] || 0) + 1;
      return acc;
    }, {});
    console.log('课程状态统计:', statusCount);

    // 打印过滤前的所有课程ID和状态
    console.log('过滤前的课程列表:', res.data.map(course => ({
      id: course.id,
      title: course.title,
      status: course.status,
      author: course.author
    })));

    const filteredCourses = res.data.filter(course => {
      if (true) {
        console.log(`管理员模式 - 保留课程ID: ${course.id}, 状态: ${course.status}`);
        return true;
      }
      const shouldKeep = course.status === 1;
      console.log(`普通用户模式 - 课程ID: ${course.id}, 状态: ${course.status}, 保留: ${shouldKeep}`);
      return shouldKeep;
    });

    console.log('过滤后的课程数量:', filteredCourses.length);
    console.log('过滤后的课程列表:', filteredCourses.map(course => ({
      id: course.id,
      title: course.title,
      status: course.status
    })));

    courses.value = filteredCourses.map(course => ({
      id: course.id,
      title: course.title,
      content: course.content,
      summary: course.summary,
      coverImage: course.coverImage || '/src/assets/images/course/default.jpg',
      videoUrl: course.videoUrl,
      duration: course.duration,
      author: course.author,
      categories: course.categories || [],
      status: course.status
    }));

    // 分页总数处理
    const finalTotal = isSuperAdmin.value ? res.total : filteredCourses.length;
    console.log('最终分页总数:', finalTotal);
    pagination.value.total = finalTotal;

  } catch (error) {
    console.error('获取课程列表失败:', error);
    ElMessage.error('获取课程列表失败');
  } finally {
    loading.value = false;
  }
};

// 跳转到课程详情
const goToCourseDetail = (id) => {
  console.log('跳转到课程详情，ID:', id)
  router.push(`/courses/${id}`)
}

// 编辑课程
const editCourse = (course) => {
  console.log('编辑课程:', course)
  currentCourse.value = {
    ...course,
    categoryIds: course.categories?.map(c => c.id) || [] // 转换分类格式
  }
  dialogVisible.value = true // 确保这个值被设置为true
}

// 删除课程
const deleteCourse = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该课程吗？删除后不可恢复', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    console.log('开始删除课程，ID:', id)
    await api.refuseCourse(id)
    console.log('课程删除成功')
    ElMessage.success('删除成功')

    // 如果删除的是当前页最后一条，回到上一页
    if (courses.value.length === 1 && pagination.value.current > 1) {
      pagination.value.current--
    }
    fetchCourses()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error(error.response?.data?.message || '删除失败')
    }
  }
}

// 显示添加对话框
const showAddDialog = () => {
  console.log('显示添加课程对话框')
  currentCourse.value = {
    title: '',
    videoUrl: '',
    coverImage: '',
    duration: 0,
    author: username.value,
    summary: '',
    content: '',
    categories: [],
    categoryIds: [] // 确保包含这个字段以匹配对话框预期
  }
  dialogVisible.value = true // 确保这个值被设置为true
}

// 提交表单（增强调试版）
const handleSubmit = async (courseData) => {
  try {
    console.group('=== CourseList 处理提交 ===')
    console.log('📥 接收到 Dialog 数据:', JSON.parse(JSON.stringify(courseData)))

    // 转换数据格式
    const formattedData = {
      title: courseData.title,
      videoUrl: courseData.videoUrl,
      coverImage: courseData.coverImage,
      duration: courseData.duration,
      author: courseData.author,
      summary: courseData.summary,
      content: courseData.content,
      categories: courseData.categoryIds || [], // 保持数组格式
      status: courseData.status,
      sortOrder: courseData.sortOrder
    }

    console.log('🔄 List 转换后数据:', JSON.parse(JSON.stringify(formattedData)))

    if (courseData.id) {
      console.log('🛠 准备更新课程，ID:', courseData.id)
      await api.updateCourse({
        ...formattedData,
        id: courseData.id,
        now_username: userStore.currentUser.username
      })
      ElMessage.success('更新成功')
    } else {

      console.log('🆕 准备添加课程')
      if(!userStore.isSuperAdmin)
      { console.log('普通用户提交课程')
        await api.submitCourse({
        title: formattedData.title,
        videoUrl: formattedData.videoUrl,
        coverImage: formattedData.coverImage,
        duration: formattedData.duration,
        author: formattedData.author,
        summary: formattedData.summary,
        content: formattedData.content,
        categories: formattedData.categories }
      )
      }  else {
        console.log('管理员提交课程')
        await api.commitCourse({
          title: formattedData.title,
          videoUrl: formattedData.videoUrl,
          coverImage: formattedData.coverImage,
          duration: formattedData.duration,
          author: formattedData.author,
          summary: formattedData.summary,
          content: formattedData.content,
          categories: formattedData.categories
        })
      }
      ElMessage.success('添加成功')
    }

    console.groupEnd()
    fetchCourses()
  } catch (error) {
    console.error('❌ List 处理失败:', error)
    ElMessage.error(error.response?.data?.message || '操作失败')
  } finally {
    dialogVisible.value = false
  }
}
// 初始化加载
onMounted(async () => {
  console.log('课程列表页初始化')
  await userStore.initUser()
  console.log('用户信息:', userStore.currentUser)

  fetchTopCourses()
  fetchCourses()

  if (!userStore.isSuperAdmin) {
    console.log('普通用户进入页面')
  } else {
    console.log('管理员进入页面')
  }

})
</script>

<style scoped>
.course-page {
  padding: 20px;
}

.course-top-list {
  margin-bottom: 30px;
}

.top-courses {
  display: flex;
  gap: 15px;
  margin-top: 15px;
}

.top-course-card {
  flex: 1;
  display: flex;
  align-items: center;
  padding: 15px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.3s;
}

.top-course-card:hover {
  transform: translateY(-5px);
}

.rank {
  font-size: 24px;
  font-weight: bold;
  margin-right: 15px;
  color: #666;
}

.cover {
  width: 80px;
  height: 60px;
  border-radius: 4px;
  object-fit: cover;
  margin-right: 15px;
}

.info h3 {
  margin: 0;
  font-size: 18px;
}

.meta {
  margin: 5px 0 0;
  color: #888;
  font-size: 14px;
}

.filter-bar {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.course-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.course-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.3s;
  display: flex;
  flex-direction: column;
}

.course-card:hover {
  transform: translateY(-5px);
}

.card-media {
  position: relative;
  height: 160px;
}

.card-media .cover {
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

.card-content {
  padding: 15px;
  flex: 1;
}

.card-content h3 {
  margin: 0 0 8px;
  font-size: 16px;
  line-height: 1.4;
}

.author {
  margin: 0 0 10px;
  color: #666;
  font-size: 14px;
}

.categories {
  margin-bottom: 10px;
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.category-tag {
  margin-right: 5px;
}

.content-summary {
  margin: 10px 0 0;
  color: #666;
  font-size: 14px;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.card-footer {
  padding: 10px 15px;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: flex-end;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.add-button {
  position: fixed;
  right: 30px;
  bottom: 30px;
  width: 50px;
  height: 50px;
  font-size: 20px;
}

/* 排名样式 */
.top-course-card.rank-1 .rank {
  color: #ffd700;
}

.top-course-card.rank-2 .rank {
  color: #c0c0c0;
}

.top-course-card.rank-3 .rank {
  color: #cd7f32;
}
</style>