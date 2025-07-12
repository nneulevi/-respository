<template>
  <div class="writer-page">
    <!-- 作家榜 -->
    <div class="writer-top-list">
      <h2>📚 热门作家榜</h2>
      <div class="top-writers">
        <div
            v-for="(writer, index) in topWriters"
            :key="writer.id"
            class="top-writer-card"
            :class="'rank-' + (index + 1)"
            @click="goToWriterDetail(writer.id)"
        >
          <span class="rank">{{ index + 1 }}</span>
          <img :src="writer.avatarUrl || '/src/assets/images/writer/default.jpg'" class="avatar">
          <div class="info">
            <h3>{{ writer.name }}</h3>
            <p class="book-count">{{ writer.bookCount }} 部作品</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 筛选栏 -->
    <div class="filter-bar">
      <el-input
          v-model="filters.name"
          placeholder="搜索作家"
          clearable
          style="width: 200px"
          @keyup.enter="fetchWriters"
      >
        <template #prefix>
          <el-icon><search /></el-icon>
        </template>
      </el-input>

      <el-select
          v-model="filters.gender"
          placeholder="性别"
          clearable
          style="width: 120px; margin-left: 10px"
      >
        <el-option label="男" :value="1" />
        <el-option label="女" :value="0" />
      </el-select>

      <el-select
          v-model="filters.country"
          placeholder="国家"
          clearable
          style="width: 120px; margin-left: 10px"
      >
        <el-option label="中国" value="中国" />
        <el-option label="日本" value="日本" />
        <el-option label="欧美" value="欧美" />
      </el-select>

      <el-button
          type="primary"
          style="margin-left: 10px"
          @click="fetchWriters"
      >
        筛选
      </el-button>
    </div>

    <!-- 作家列表 -->
    <div class="writer-list">
      <div
          v-for="writer in writers"
          :key="writer.id"
          class="writer-card"
          @click="goToWriterDetail(writer.id)"
      >
        <div class="card-header">
          <img :src="writer.avatarUrl || '/src/assets/images/writer/default.jpg'" class="avatar">
          <div class="writer-info">
            <h3>{{ writer.name }}</h3>
            <p class="meta">
              <span>{{ writer.gender === 0 ? '女' : '男' }}</span>
              <span>·</span>
              <span>{{ writer.country }}</span>
              <span>·</span>
              <span>{{ writer.bookCount }} 部作品</span>
            </p>
          </div>
        </div>
        <div class="card-body">
          <p class="description">{{ writer.description || '暂无简介' }}</p>
          <div class="popular-books">
            <span>代表作：</span>
            <span v-if="writer.representativeWork">{{ writer.representativeWork }}</span>
            <span v-else>暂无代表作</span>
          </div>
        </div>
        <div class="card-footer">
          <el-button type="primary" size="small" @click.stop="editWriter(writer)">编辑</el-button>
          <el-button type="danger" size="small" @click.stop="deleteWriter(writer.id)">删除</el-button>
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
          @current-change="fetchWriters"
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
    <writer-dialog
        v-model="dialogVisible"
        :writer="currentWriter"
        @submit="handleSubmit"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus } from '@element-plus/icons-vue'
import WriterDialog from './WriterDialog.vue'
import axios from 'axios'

const router = useRouter()

// 筛选条件
const filters = ref({
  name: '',
  gender: null,
  country: ''
})

// 分页
const pagination = ref({
  current: 1,
  size: 12,
  total: 0
})

// 数据
const topWriters = ref([])
const writers = ref([])
const dialogVisible = ref(false)
const currentWriter = ref(null)

// 获取热门作家榜
const fetchTopWriters = async () => {
  try {
    const res = await axios.get('/api/writers/top?limit=5')
    topWriters.value = res.data
  } catch (error) {
    ElMessage.error('获取热门作家榜失败')
  }
}

// 获取作家列表
const fetchWriters = async () => {
  try {
    const params = {
      name: filters.value.name.trim() || undefined,
      gender: [0, 1].includes(filters.value.gender)
          ? filters.value.gender
          : undefined,
      country: filters.value.country || undefined,
      page: pagination.value.current - 1,
      size: pagination.value.size
    }

    const res = await axios.get('/api/writers', { params })
    writers.value = res.data.content
    pagination.value.total = res.data.totalElements
  } catch (error) {
    ElMessage.error(`数据加载失败: ${error.response?.data?.message || error.message}`)
  }
}

const goToWriterDetail = (id) => {
  router.push(`/writers/${id}`)
}

const editWriter = (writer) => {
  currentWriter.value = JSON.parse(JSON.stringify(writer))
  dialogVisible.value = true
}

// 删除作家
const deleteWriter = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该作家吗？', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await axios.delete(`/api/writers/${id}`)
    ElMessage.success('删除成功')
    fetchWriters()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 添加作家
const showAddDialog = () => {
  currentWriter.value = {
    name: '',
    avatarUrl: '',
    gender: 1,
    country: '中国',
    birthYear: null,
    representativeWork: '',
    description: '',
    externalUrl: ''
  }
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = async (newWriter) => {
  try {
    const url = newWriter.id ? `/api/writers/${newWriter.id}` : '/api/writers'
    const method = newWriter.id ? 'put' : 'post'

    const res = await axios[method](url, newWriter)

    ElMessage.success(newWriter.id ? '更新成功' : '添加成功')
    fetchWriters()
  } catch (error) {
    ElMessage.error(`操作失败: ${error.response?.data?.message || error.message}`)
  } finally {
    dialogVisible.value = false
  }
}

// 初始化加载
onMounted(() => {
  fetchTopWriters()
  fetchWriters()
})
</script>

<style scoped>
.writer-page {
  padding: 20px;
}

.writer-top-list {
  margin-bottom: 30px;
}

.top-writers {
  display: flex;
  gap: 15px;
  margin-top: 15px;
}

.top-writer-card {
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

.top-writer-card:hover {
  transform: translateY(-5px);
}

.rank {
  font-size: 24px;
  font-weight: bold;
  margin-right: 15px;
  color: #666;
}

.avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 15px;
}

.info h3 {
  margin: 0;
  font-size: 18px;
}

.book-count {
  margin: 5px 0 0;
  color: #888;
  font-size: 14px;
}

.filter-bar {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.writer-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.writer-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.3s;
}

.writer-card:hover {
  transform: translateY(-5px);
}

.card-header {
  display: flex;
  padding: 15px;
  border-bottom: 1px solid #eee;
}

.writer-info {
  margin-left: 15px;
}

.writer-info h3 {
  margin: 0;
  font-size: 18px;
}

.meta {
  margin: 5px 0 0;
  color: #888;
  font-size: 14px;
}

.card-body {
  padding: 15px;
}

.description {
  margin: 0;
  color: #666;
  font-size: 14px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.popular-books {
  margin-top: 10px;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 5px;
}

.book-tag {
  margin-right: 5px;
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
</style>