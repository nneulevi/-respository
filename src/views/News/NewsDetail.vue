<template>
  <div class="news-detail">
    <div class="back-button">
      <el-button type="primary" plain @click="goBack">
        <el-icon><ArrowLeft /></el-icon>
        返回首页
      </el-button>
    </div>
    <el-card shadow="hover" class="detail-card">
      <template #header>
        <div class="header">
          <h1 class="title">{{ news.title || '加载中...' }}</h1>
          <div class="meta">
            <div class="meta-item">
              <el-icon><User /></el-icon>
              <span class="author">{{ news.author?.name || '匿名' }}</span>
            </div>
            <el-divider direction="vertical" />
            <div class="meta-item">
              <el-icon><Clock /></el-icon>
              <span class="time">{{ formatTime(news.publishTime) }}</span>
            </div>
            <el-divider direction="vertical" />
            <div class="meta-item">
              <el-icon><View /></el-icon>
              <span class="views">{{ news.viewCount || 0 }}浏览</span>
            </div>
            <el-tag
                v-if="news.status && news.status !== 'approved'"
                :type="news.status === 'pending' ? 'warning' : 'danger'"
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
        <div class="content" v-html="news.content || '<p>暂无内容</p>'"></div>

        <div class="cover-container" v-if="news.coverImage">
          <el-image
              :src="news.coverImage"
              class="cover"
              :preview-src-list="[news.coverImage]"
              fit="cover"
          >
            <template #error>
              <div class="image-error">
                <el-icon><Picture /></el-icon>
              </div>
            </template>
          </el-image>
        </div>



        <div class="tags" v-if="news.tags?.length">
          <el-tag
              v-for="(tag, index) in news.tags"
              :key="tag.id"
              :class="['tag-item', `tag-color-${index % 3}`]"
              size="small"
              effect="plain"
          >
            #{{ tag.name }}
          </el-tag>
        </div>
        <div class="actions" v-if="canEdit">
          <el-button type="primary" plain @click="editNews" round>
            <el-icon><Edit /></el-icon>
            编辑
          </el-button>
          <el-button type="danger" plain @click="deleteNews" round>
            <el-icon><Delete /></el-icon>
            删除
          </el-button>
        </div>
      </template>
    </el-card>

    <NewsDialog
        v-model="dialogVisible"
        :news="currentNews"
        :all-tags="tags"
        :is-super-admin="isSuperAdmin"
        @submit="handleSubmit"
    />
    <!-- 评论区 -->
    <el-card shadow="hover" class="comment-section">
      <template #header>
        <div class="comment-header">
          <h3>评论区</h3>
          <span class="comment-count">{{ comments.length }}条评论</span>
        </div>
      </template>

      <!-- 发表评论 -->
      <div class="comment-form" v-if="userStore.currentUser">
        <el-input
            v-model="newComment"
            type="textarea"
            :rows="3"
            placeholder="写下你的评论..."
            maxlength="200"
            show-word-limit
        ></el-input>
        <div class="form-actions">
          <el-button
              type="primary"
              size="small"
              @click="submitComment"
              :disabled="!newComment.trim()"
          >
            发表评论
          </el-button>
        </div>
      </div>
      <div v-else class="login-tip">
        请<el-button type="text" @click="router.push('/login')">登录</el-button>后发表评论
      </div>

      <!-- 评论列表 -->
      <div class="comment-list">
        <div
            v-for="comment in comments"
            :key="comment.id"
            class="comment-item"
        >
          <div class="comment-user">
            <el-avatar :size="36" :src="comment.authorAvatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
            <span class="username">{{ comment.author }}</span>
            <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
          </div>
          <div class="comment-content">{{ comment.content }}</div>
          <div class="comment-actions">
            <el-button
                type="text"
                :icon="View"
                @click="likeComment(comment.id)"
                :class="{ 'liked': comment.isLiked }"
            >
              {{ comment.likes || 0 }}
            </el-button>
            <el-button
                v-if="userStore.isSuperAdmin || comment.author === userStore.currentUser?.username"
                type="text"
                :icon="Delete"
                @click="deleteComment(comment.id)"
            >
              删除
            </el-button>
          </div>
        </div>
      </div>
    </el-card>



  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

import NewsDialog from './NewsDialog.vue'
import newsApi from '@/api/news'

import {useUserStore} from "@/stores/user.js";
import { User, Clock, View, Edit, Delete, ArrowLeft } from '@element-plus/icons-vue'

// 在方法部分添加返回函数
const goBack = () => {
  router.push('/')
}
const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const isSuperAdmin = computed(() => userStore.isSuperAdmin)
const currentUser = computed(() => userStore.currentUser)



// 响应式数据
const news = ref({
  id: null,
  title: '',
  content: '',
  coverImage: '',
  publishTime: '',
  viewCount: 0,
  status: '',
  author: { id: null, name: '' },
  tags: []
})
const loading = ref(true)
const dialogVisible = ref(false)
const currentNews = ref(null)
const comments = ref([])
const newComment = ref('')
const commentLoading = ref(false)
// 计算属性
const canEdit = computed(() => {
  return userStore.isSuperAdmin || news.value.author?.id === userStore.currentUser?.id
})

const statusText = computed(() => {
  const statusMap = { pending: '待审核', rejected: '已拒绝' }
  return statusMap[news.value.status] || ''
})

// 方法
const formatTime = (time) => {
  return time ? new Date(time).toLocaleString() : '未知时间'
}

const fetchNewsDetail = async () => {
  loading.value = true;
  try {
    const title = decodeURIComponent(route.params.title) // 解码标题
    const response = await newsApi.getNewsDetail(title);
    // 确保数据存在
    if (!response.data) {
      throw new Error('新闻不存在');
    }

    const processTags = (tags) => {
      // 标签ID到名称的映射
      const tagMap = {
        1: "科技",
        2: "财经",
        3: "健康",
        4: "教育",
        5: "体育"
      }

      if (!tags) return []
      if (Array.isArray(tags)) {
        return tags.map(tag => ({
          id: tag.id || tag,
          name: tag.name || tagMap[tag.id] || tagMap[tag] || `未知标签(${tag.id || tag})`
        }))
      }
      if (typeof tags === 'string') {
        return tags.split(',').map(id => ({
          id: id.trim(),
          name: tagMap[id.trim()] || `未知标签(${id})`
        }))
      }
      return []
    }

    // 安全更新数据
    news.value = {
      id: response.data.id,
      title: response.data.title || '无标题',
      content: response.data.content || '暂无内容',
      coverImage: response.data.coverImage || '',
      publishTime: response.data.publishTime || '',
      viewCount: response.data.viewCount || 0,
      status: response.data.status || '',
      author: {
        id: response.data.authorId || null,
        name: response.data.author || '未知作者'
      },
      tags: processTags(response.data.tags) // 确保tags总是数组
    };

    await newsApi.incrementViewCount(news.value.title)
  } catch (error) {
    console.error('加载新闻详情失败:', error);
    ElMessage.error(`加载失败: ${error.response?.data?.message || error.message}`);
    router.push('/404'); // 跳转到404页面
    return; // 提前返回，避免继续执行
  } finally {
    loading.value = false;
  }
};


const editNews = () => {
  currentNews.value = {
    ...news.value,
    tagIds: news.value.tags.map(tag => tag.id)
  }
  dialogVisible.value = true
}

const handleSubmit = async (newsData) => {
  try {
    const formattedData = {
      title: newsData.title,
      summary: newsData.summary || newsData.content.substring(0, 100),
      content: newsData.content,
      coverImage: newsData.coverImage,
      author: newsData.author,
      tags: newsData.tagIds.join(',')
    }

    await newsApi.updateNews(newsData.id, formattedData)
    ElMessage.success('更新成功')
    await fetchNewsDetail() // 刷新数据
    dialogVisible.value = false
  } catch (error) {
    ElMessage.error('更新失败: ' + (error.response?.data?.message || error.message))
  }
}

const deleteNews = async () => {
  try {
    await ElMessageBox.confirm('确定删除该动态吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await newsApi.deleteNews(news.value.id)
    ElMessage.success('删除成功')
    router.push('/news')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败: ' + (error.response?.data?.message || error.message))
      console.error(error)
    }
  }
}
const fetchComments = async () => {
  try {
    console.log('[前端] 开始获取评论，新闻ID:', news.value.id);
    commentLoading.value = true;
    const response = await newsApi.getComments(news.value.id, {
      sortBy: 'time'
    });
    console.log('[前端] 获取评论响应:', response);
    comments.value = response.data || [];
  } catch (error) {
    console.error('[前端] 获取评论错误:', error.response || error);
    ElMessage.error('获取评论失败: ' + (error.response?.data?.message || error.message));
  } finally {
    commentLoading.value = false;
  }
};

const submitComment = async () => {
  try {
    console.log('[前端] 提交评论内容:', newComment.value);
    const commentData = {
      news_id: news.value.id,
      content: newComment.value,
      author: userStore.currentUser.username
    };
    console.log('[前端] 完整评论数据:', commentData);

    const response = await newsApi.addComment(commentData);
    console.log('[前端] 评论提交响应:', response);

    ElMessage.success('评论发表成功');
    newComment.value = '';
    await fetchComments(); // 重新加载评论
  } catch (error) {
    console.error('[前端] 评论提交错误:', error.response || error);
    ElMessage.error('发表评论失败: ' + (error.response?.data?.message || error.message));
  }
};

// 点赞评论
const likeComment = async (commentId) => {
  try {
    await newsApi.likeComment(commentId)
    const comment = comments.value.find(c => c.id === commentId)
    if (comment) {
      comment.likes = (comment.likes || 0) + 1
      comment.isLiked = true
    }
  } catch (error) {
    ElMessage.error('点赞失败: ' + error.message)
  }
}

// 删除评论
const deleteComment = async (commentId) => {
  try {
    await ElMessageBox.confirm('确定删除这条评论吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await newsApi.deleteComment(commentId)
    comments.value = comments.value.filter(c => c.id !== commentId)
    ElMessage.success('评论删除成功')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败: ' + error.message)
    }
  }
}
onMounted(async () => {
  await fetchNewsDetail()
  console.log(userStore.currentUser)
  console.log(userStore.isSuperAdmin)
  fetchComments()
})
</script>

<style scoped lang="scss">
.news-detail {
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

  .title {
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

  /* 视频容器样式 */
  :deep(.video-container) {
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

  /* 段落样式 */
  :deep(p) {
    margin: 20px 0;
    color: #333;
    line-height: 1.7;
  }

  /* 强调文本 */
  :deep(strong) {
    color: var(--el-color-primary);
    background: rgba(64, 158, 255, 0.1);
    padding: 2px 4px;
    border-radius: 3px;
  }

  /* 列表样式 */
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
}

.cover-container {
  margin: 30px 0;
  border-radius: 8px;
  overflow: hidden;
  text-align: center;

  .cover {
    max-width: 100%;
    max-height: 500px;
    object-fit: contain;
    border-radius: 8px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  }

  .image-error {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 200px;
    background: #f5f7fa;
    color: #909399;

    .el-icon {
      font-size: 40px;
    }
  }
}

.additional-content {
  margin: 30px 0;
  padding: 20px;
  background: #f9fafc;
  border-radius: 8px;
  border-left: 4px solid var(--el-color-primary);

  h2 {
    margin-top: 0;
    color: var(--el-color-primary);
  }

  p {
    margin: 15px 0;
    line-height: 1.7;
  }
}

.tags {
  margin: 24px 0;
  display: flex;
  gap: 10px;
  flex-wrap: wrap;

  .tag-item {
    cursor: pointer;
    transition: all 0.2s ease;
    border: none !important;
    font-weight: 500;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
    }

    &.tag-color-0 {
      background: rgba(144, 238, 144, 0.2); /* 淡绿色 */
      color: #2e8b57;
    }

    &.tag-color-1 {
      background: rgba(173, 216, 230, 0.2); /* 淡蓝色 */
      color: #4682b4;
    }

    &.tag-color-2 {
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
  .news-detail {
    padding: 12px;
  }

  .header .title {
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