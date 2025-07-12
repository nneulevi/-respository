<template>
  <div class="news-list">


    <div class="header">
      <h2>行业动态</h2>
      <div class="actions">
        <el-input
            v-model="searchQuery"
            placeholder="搜索动态"
            clearable
            style="width: 300px"
            @keyup.enter="fetchNews"
        >
          <template #prefix>
            <el-icon><search /></el-icon>
          </template>
        </el-input>
      </div>
    </div>

    <div class="filter-bar">
      <el-select
          v-model="filterStatus"
          placeholder="全部状态"
          clearable
          style="width: 120px"
      >
        <el-option label="待审核" value="pending" />
        <el-option label="已通过" value="approved" />
        <el-option label="已拒绝" value="rejected" />
      </el-select>
      <el-select
          v-model="filterTag"
          placeholder="全部标签"
          clearable
          style="width: 120px; margin-left: 10px"
      >
        <el-option
            v-for="tag in allTags"
            :key="tag.id"
            :label="tag.name"
            :value="tag.id"
        />
      </el-select>
      <el-button
          type="primary"
          style="margin-left: 10px"
          @click="fetchNews"
      >
        筛选
      </el-button>
    </div>

    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="6" animated />
    </div>
    <template v-else>
      <div v-if="newsList.length" class="list-container">
        <div class="grid-view">
          <div
              v-for="news in newsList"
              :key="news.id"
              class="news-card"
          >
            <div class="card-media" @click="goToNewsDetail(news)">
              <img :src="news.coverImage || '/src/assets/images/news/default.jpg'" class="cover">
              <div class="view-count">
                <el-icon><view /></el-icon>
                <span>{{ news.viewCount || 0 }}</span>
              </div>
            </div>
            <div class="card-content" @click="goToNewsDetail(news.id)">
              <h3>{{ news.title }}</h3>
              <p class="author">{{ news.author?.name || '未知作者' }}</p>
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
              <p class="content-summary">{{ truncateContent(news.content) }}</p>
            </div>
            <div class="card-footer">
              <el-button type="primary" size="small" @click.stop="editNews(news)">编辑</el-button>
              <el-button type="danger" size="small" @click.stop="deleteNews(news.id)">删除</el-button>
            </div>
          </div>
        </div>
        <div class="pagination">
          <el-pagination
              v-model:current-page="pagination.current"
              v-model:page-size="pagination.size"
              :total="pagination.total"
              layout="prev, pager, next"
              :page-sizes="[12, 24, 36]"
              @current-change="fetchNews"
          />
        </div>
      </div>
      <el-empty v-else description="暂无动态数据" :image-size="100" />
    </template>

    <!-- 添加按钮 -->
    <el-button
        type="primary"
        circle
        class="add-button"
        @click="showAddDialog"
    >
      <el-icon><plus /></el-icon>
    </el-button>

    <NewsDialog
        v-model="dialogVisible"
        :news="currentNews"
        :all-tags="allTags"
        :is-super-admin="userStore.isSuperAdmin"
        :current-user="userStore.currentUser"
        @submit="handleSubmit"
    />
  </div>
</template>

<script setup>
import {ref, onMounted, computed} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Search, Plus, View} from '@element-plus/icons-vue'
import NewsDialog from './NewsDialog.vue'
import newsApi from '@/api/news'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'

const router = useRouter()

const userStore = useUserStore()
const isSuperAdmin = computed(() => userStore.isSuperAdmin)
const currentUser = computed(() => userStore.currentUser)


// 数据
const topNews = ref([])
const newsList = ref([])
const allTags = ref([])
const loading = ref(false)
const sortBy = ref('')

// 搜索和筛选
const searchQuery = ref('')
const filterStatus = ref('')
const filterTag = ref('')

// 分页
const pagination = ref({
  current: 1,
  size: 12,
  total: 0
})

// 对话框
const dialogVisible = ref(false)
const currentNews = ref(null)

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString()
}

// 截断内容
const truncateContent = (content) => {
  if (!content) return '暂无内容'
  const plainText = content.replace(/<[^>]*>/g, '')
  return plainText.length > 100 ? plainText.substring(0, 100) + '...' : plainText
}

// 格式化新闻数据
// 格式化新闻数据 - 修改后的版本
const formatNewsData = (news) => {
  // 标签ID到名称的映射
  const tagMap = {
    1: "科技",
    2: "财经",
    3: "健康",
    4: "教育",
    5: "体育"
  }

  // 处理标签数据
  const processTags = (tags) => {
    if (!tags) return []
    if (typeof tags === 'string') {
      return tags.split(',').map(id => ({
        id: id.trim(),
        name: tagMap[id.trim()] || `未知标签(${id})`
      }))
    }
    if (Array.isArray(tags)) {
      return tags.map(tag => ({
        id: tag.id || tag,
        name: tag.name || tagMap[tag.id] || tagMap[tag] || `未知标签(${tag.id || tag})`
      }))
    }
    return []
  }

  return {
    id: news.id,
    title: news.title,
    content: news.content,
    summary: news.summary,
    coverImage: news.coverImage || '/src/assets/images/news/default.jpg',
    publishTime: news.publishTime,
    viewCount: news.viewCount || 0,
    author: news.author,
    tags: processTags(news.tags) // 处理后的标签数组
  }
}

// 生成模拟新闻数据
const generateMockNews = () => {
  return Array.from({length: 6}, (_, i) => ({
    id: i + 1,
    title: `示例新闻标题 ${i + 1}`,
    content: `这是示例新闻内容 ${i + 1}，包含一些测试数据用于前端展示和调试。`,
    coverImage: i % 2 === 0 ? "https://via.placeholder.com/800x450" : null,
    publishTime: new Date().toISOString(),
    viewCount: Math.floor(Math.random() * 1000),
    status: "approved",
    author: userStore.currentUser?.username || "admin",
    tags: [] // 使用空数组
  }))
}


const fetchNews = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.value.current,
      pageSize: pagination.value.size,
      // 不传title到后端，前端自己过滤
      author: undefined, // 普通用户也能看所有已通过的新闻
      tags: filterTag.value
    }

    let response
    if (isSuperAdmin.value && filterStatus.value === 'pending') {
      // 管理员查看待审核
      response = await newsApi.getPendingNews(params)
    } else {
      // 其他情况都获取已通过的
      response = await newsApi.getNewsList(params)
    }

    let newsData = response.data.map(formatNewsData)

    // 前端实现标题搜索
    if (searchQuery.value) {
      const searchTerm = searchQuery.value.toLowerCase()
      newsData = newsData.filter(news =>
          news.title.toLowerCase().includes(searchTerm)
      )
    }

    newsList.value = newsData
    pagination.value.total = response.total
  } catch (error) {
    console.error('获取新闻列表失败:', error)
    ElMessage.error('获取新闻列表失败')
    newsList.value = generateMockNews()
    pagination.value.total = newsList.value.length
  } finally {
    loading.value = false
  }
}
// 获取热门新闻 - 使用模拟数据
const fetchTopNews = async () => {
  try {
    // 模拟热门新闻数据
    topNews.value = [
      {
        id: 101,
        title: "2023年人工智能行业发展趋势报告",
        content: "人工智能行业的最新发展趋势分析...",
        coverImage: "https://via.placeholder.com/800x450?text=AI+News",
        publishTime: "2023-05-15T14:30:00",
        viewCount: 2568,
        author: "AI研究院"
      },
      {
        id: 102,
        title: "全球数字经济白皮书发布",
        content: "最新全球数字经济白皮书内容摘要...",
        publishTime: "2023-06-20T09:15:00",
        viewCount: 1892,
        author: "数字经济研究中心"
      },
      {
        id: 103,
        title: "新能源技术突破性进展",
        content: "科学家宣布新能源技术取得重大突破...",
        coverImage: "https://via.placeholder.com/800x450?text=Energy",
        publishTime: "2023-07-10T16:45:00",
        viewCount: 1543,
        author: "科技日报"
      }
    ].map(formatNewsData)
  } catch (error) {
    console.error('获取热门新闻失败:', error)
    ElMessage.error('获取热门新闻失败')
  }
}

// 获取标签 - 使用模拟数据
const fetchAllTags = async () => {
  try {
    // 模拟标签数据
    allTags.value = [
      {id: 1, name: "科技"},
      {id: 2, name: "财经"},
      {id: 3, name: "健康"},
      {id: 4, name: "教育"},
      {id: 5, name: "体育"}
    ]
  } catch (error) {
    console.error('获取标签失败:', error)
    ElMessage.error('获取标签失败')
  }
}

const goToNewsDetail = (news) => {
  console.log('跳转到新闻详情页:', news.title)
  router.push({
    name: 'NewsDetail',
    params: { title: encodeURIComponent(news.title) } // 对标题进行编码，防止特殊字符问题
  })
}

// 显示添加对话框
const showAddDialog = () => {
  currentNews.value = {
    title: '',
    content: '',
    coverImage: '',
    tags: [],
    author: userStore.currentUser?.username || 'admin'
  }
  dialogVisible.value = true
}

// 编辑新闻
const editNews = (news) => {
  currentNews.value = {
    ...news,
    tagIds: news.tags.map(tag => tag.id)
  }
  dialogVisible.value = true
}
const handleSubmit = async (newsData) => {
  console.log('[Submit] 收到的表单数据:', JSON.parse(JSON.stringify(newsData)));

  try {
    const formattedData = {
      title: newsData.title,
      summary: newsData.summary,
      content: newsData.content,
      coverImage: newsData.coverImage,
      author: newsData.author || userStore.currentUser.username, // 确保作者
      tags: newsData.tagIds?.join(',') || '', // 确保标签格式
    };
    console.log('[Submit] 格式化后的数据:', formattedData);

    let response;
    if (newsData.id) {
      console.log('[Submit] 执行更新操作');
      response = await newsApi.updateNews(
          newsData.id,
          formattedData,
          {
            params: {
              now_username: userStore.currentUser.username // 从store获取
            }
          }
      );
    } else {
      // 新增新闻（区分管理员和普通用户）
      if (userStore.isSuperAdmin) {
        console.log('管理员发布新闻')
        response = await newsApi.addNews({
          ...formattedData,
          viewCount: 0,
          publishTime: new Date().toISOString()
        })
        await newsApi.deletepengdingNews(newsData.id)
      } else {
        console.log('普通用户发布新闻')
        response = await newsApi.submitNews(formattedData)


      }
    }

    console.log('[Submit] 后端响应:', response);
    ElMessage.success(newsData.id ? '更新成功' : '发布成功');
    fetchNews();
  } catch (error) {
    console.error('[Submit] 错误详情:', error.response?.data || error.message);
    ElMessage.error(`操作失败: ${error.response?.data?.message || error.message}`);
  }
};


// 修改后的deleteNews方法
const deleteNews = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该动态吗？删除后不可恢复', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await newsApi.deleteNews(id)
    ElMessage.success('删除成功')

    // 如果删除的是当前页最后一条，回到上一页
    if (newsList.value.length === 1 && pagination.value.current > 1) {
      pagination.value.current--
    }
    fetchNews()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error(error.response?.data?.message || '删除失败')
    }
  }
}

// 初始化加载
onMounted(async () => {
  await userStore.initUser();
  fetchTopNews()
  fetchNews()
  fetchAllTags()
  if (!userStore.isSuperAdmin) {
    console.log('普通用户进入页面')
  } else {
    console.log('管理员进入页面')
  }
  console.log('当前用户:', userStore.currentUser)
})
</script>

<style scoped>
.news-list {
  padding: 20px;
  position: relative;
}

.news-top-list {
  margin-bottom: 30px;
}

.top-news {
  display: flex;
  gap: 15px;
  margin-top: 15px;
}

.top-news-card {
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

.top-news-card:hover {
  transform: translateY(-5px);
}

.rank {
  font-size: 24px;
  font-weight: bold;
  margin-right: 15px;
  color: #666;
}

.top-news-card .cover {
  width: 80px;
  height: 60px;
  border-radius: 4px;
  object-fit: cover;
  margin-right: 15px;
}

.top-news-card .info h3 {
  margin: 0;
  font-size: 18px;
}

.top-news-card .meta {
  margin: 5px 0 0;
  color: #888;
  font-size: 14px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.actions {
  display: flex;
  gap: 10px;
}

.filter-bar {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.loading-container {
  padding: 20px;
}

.list-container {
  margin-top: 20px;
}

.grid-view {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.news-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.3s;
  display: flex;
  flex-direction: column;
}

.news-card:hover {
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

.view-count {
  position: absolute;
  right: 8px;
  bottom: 8px;
  display: flex;
  align-items: center;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.view-count .el-icon {
  margin-right: 4px;
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

.tags {
  margin-bottom: 10px;
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.tag {
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
.top-news-card.rank-1 .rank {
  color: #ffd700;
}

.top-news-card.rank-2 .rank {
  color: #c0c0c0;
}

.top-news-card.rank-3 .rank {
  color: #cd7f32;
}

@media (max-width: 768px) {
  .header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .actions {
    width: 100%;
  }

  .grid-view {
    grid-template-columns: 1fr;
  }
}
</style>