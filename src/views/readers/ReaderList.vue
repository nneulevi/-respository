<template>
  <div class="reader-container">
    <!-- 顶部搜索栏 -->
    <div class="search-bar">
      <el-row :gutter="20">

        <el-col :span="18">
          <el-input
              v-model="searchKeyword"
              placeholder="输入读者昵称或用户名搜索"
              clearable
              @clear="fetchReaders"
              @keyup.enter="fetchReaders"
          >
            <template #append>
              <el-button @click="fetchReaders">
                <el-icon><Search /></el-icon>
              </el-button>
            </template>
          </el-input>
        </el-col>
        <el-col :span="6" class="text-right">
          <el-button type="primary" @click="showAddDialog">
            <el-icon><Plus /></el-icon>
            添加读者
          </el-button>
        </el-col>
      </el-row>
    </div>

    <!-- 读者列表 -->
    <div class="reader-list">
      <el-table
          :data="readers.content"
          border
          stripe
          v-loading="loading"
          style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column label="性别" width="80">
          <template #default="{ row }">
                <el-tag :type="{ 1: 'primary', 2: 'danger' }[row.gender] || 'info'">
                  {{ row.genderText }}
                </el-tag>
              </template>
            </el-table-column>
        <el-table-column prop="joinDate" label="加入日期" width="120" />
        <el-table-column label="书架数量" width="100">
          <template #default="{ row }">
            <el-tag>{{ row.bookCount }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220">
          <template #default="{ row }">
            <el-button size="small" @click="viewShelf(row.id)">查看书架</el-button>
            <el-button size="small" type="primary" @click="editReader(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteReader(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
          background
          layout="prev, pager, next, sizes, total"
          :total="readers.totalElements"
          v-model:page-size="pagination.size"
          v-model:current-page="pagination.page"
          @current-change="fetchReaders"
          @size-change="handleSizeChange"
      />
    </div>

    <!-- 添加/编辑对话框 -->
    <el-dialog
        :title="dialogTitle"
        v-model="dialogVisible"
        width="500px"
    >
      <el-form :model="currentReader" label-width="80px">
        <el-form-item label="用户名" prop="username" required>
          <el-input v-model="currentReader.username" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname" required>
          <el-input v-model="currentReader.nickname" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="currentReader.gender">
            <el-radio :value="0">未知</el-radio> <!-- 新写法 -->
            <el-radio :value="1">男</el-radio>
            <el-radio :value="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="头像URL" prop="avatar">
          <el-input v-model="currentReader.avatar" />
          <div v-if="currentReader.avatar" class="avatar-preview">
            <img :src="currentReader.avatar" alt="头像预览" />
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReader">确定</el-button>
      </template>
    </el-dialog>

    <!-- 书架对话框 -->
    <el-dialog
        v-model="shelfDialogVisible"
        :title="`${currentReader.nickname}的书架`"
        width="80%"
    >
      <div v-if="shelfBooks.length > 0" class="shelf-container">
        <div v-for="book in shelfBooks" :key="book.id" class="shelf-item">
          <el-card :body-style="{ padding: '0px' }">
            <img :src="book.bookCover" class="book-cover" />
            <div style="padding: 10px;">
              <h4>{{ book.bookTitle }}</h4>
              <el-progress :percentage="book.readingProgress" />
              <div class="shelf-actions">
                <el-button
                    size="small"
                    type="danger"
                    @click="removeFromShelf(book.id)"
                >
                  移出书架
                </el-button>
              </div>
            </div>
          </el-card>
        </div>
      </div>
      <el-empty v-else description="书架空空如也" />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

// 读者数据
const readers = reactive({
  content: [],
  totalElements: 0
})

// 分页和搜索
const pagination = reactive({
  page: 1,
  size: 10
})
const searchKeyword = ref('')

// 加载状态
const loading = ref(false)

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('添加读者')
const isEditMode = ref(false)
const currentReader = reactive({
  id: 'null',
  username: '',
  nickname: '',
  gender: 0,
  avatar: ''
})

// 书架相关
const shelfDialogVisible = ref(false)
const shelfBooks = ref([])

// 获取读者列表
const fetchReaders = async () => {
  try {
    loading.value = true
    const params = {
      page: pagination.page - 1,
      size: pagination.size,
      keyword: searchKeyword.value || null
    }

    const response = await axios.get('/api/readers', { params })
    readers.content = response.data.content
    readers.totalElements = response.data.totalElements
  } catch (error) {
    ElMessage.error('获取读者列表失败: ' + error.message)
  } finally {
    loading.value = false
  }
}

// 每页数量变化
const handleSizeChange = (size) => {
  pagination.size = size
  fetchReaders()
}

const showAddDialog = () => {
  isEditMode.value = false;
  dialogTitle.value = '添加读者';

  // 直接覆盖 currentReader，确保 username 和 nickname 是字符串
  currentReader.id = null;
  currentReader.username = ""; // 确保是字符串，而不是数组
  currentReader.nickname = ""; // 确保是字符串，而不是数组
  currentReader.gender = 0;
  currentReader.avatar = "";

  dialogVisible.value = true;
};

const editReader = (reader) => {
  isEditMode.value = true
  dialogTitle.value = '编辑读者'
  Object.assign(currentReader, {
    id: reader.id,
    username: Array.isArray(reader.username) ? reader.username[0] : reader.username,
    nickname: Array.isArray(reader.nickname) ? reader.nickname[0] : reader.nickname,
    gender: reader.gender,
    avatar: reader.avatar
  })
  dialogVisible.value = true
}

// 提交读者信息
const submitReader = async () => {
  try {
    // 确保数据格式正确（强制转为字符串）
    const submitData = {
      id: currentReader.id,
      username: String(currentReader.username),
      nickname: String(currentReader.nickname),
      gender: currentReader.gender,
      avatar: currentReader.avatar
    }

    if (isEditMode.value) {
      await axios.put(`/api/readers/${currentReader.id}`, submitData)
      ElMessage.success('更新成功')
    } else {
      await axios.post('/api/readers', submitData)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    fetchReaders()
  } catch (error) {
    ElMessage.error(`操作失败: ${error.response?.data?.message || error.message}`)
  }
}
// 删除读者
const deleteReader = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该读者吗？此操作不可恢复！', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await axios.delete(`/api/readers/${id}`)
    ElMessage.success('删除成功')
    fetchReaders()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败: ' + error.message)
    }
  }
}

const viewShelf = async (readerId) => {
  try {
    const response = await axios.get(`/api/readers/${readerId}/shelf`)
    shelfBooks.value = response.data
    const reader = readers.content.find(r => r.id === readerId)
    // 设置当前读者的ID和昵称
    currentReader.id = readerId
    currentReader.nickname = reader.nickname
    shelfDialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取书架失败: ' + error.message)
  }
}

// 从书架移除书籍
const removeFromShelf = async (shelfId) => {
  try {
    await ElMessageBox.confirm('确定将此书移出书架吗？', '提示')

    // 确保readerId是有效的
    if (!currentReader.id) {
      throw new Error('无效的用户ID')
    }

    await axios.delete(`/api/readers/${currentReader.id}/bookshelf`, {
      data: [shelfId]
    })

    ElMessage.success('移除成功')
    viewShelf(currentReader.id)
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(`移除失败: ${error.response?.data?.message || error.message}`)
    }
  }
}

// 初始化加载
onMounted(() => {
  fetchReaders()
})
</script>

<style scoped>
.reader-container {
  padding: 20px;
}

.search-bar {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.avatar-preview {
  margin-top: 10px;
}
.avatar-preview img {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 50%;
}

.shelf-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 20px;
}

.shelf-item {
  transition: all 0.3s;
}
.shelf-item:hover {
  transform: translateY(-5px);
}

.book-cover {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.shelf-actions {
  margin-top: 10px;
  display: flex;
  justify-content: center;
}
</style>