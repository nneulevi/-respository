<template>
  <div class="book-list-container">
    <!-- 顶部筛选栏 -->
    <div class="filter-bar">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-select v-model="filters.category" placeholder="类型" clearable>
            <el-option label="全部" value=""></el-option>
            <el-option label="科幻" value="科幻"></el-option>
            <el-option label="玄幻" value="玄幻"></el-option>
            <el-option label="奇幻" value="奇幻"></el-option>
            <el-option label="青春" value="青春"></el-option>
            <el-option label="悬疑" value="悬疑"></el-option>
            <el-option label="历史" value="历史"></el-option>
            <el-option label="武侠" value="武侠"></el-option>
            <el-option label="言情" value="言情"></el-option>
            <el-option label="文学" value="文学"></el-option>
            <el-option label="历史" value="历史"></el-option>
            <el-option label="其他" value="其他"></el-option>
          </el-select>
        </el-col>

        <el-col :span="6">
          <el-select v-model="filters.status" placeholder="状态" clearable>
            <el-option label="全部" value=""></el-option>
            <el-option label="完结" value="1"></el-option>
            <el-option label="连载" value="0"></el-option>
          </el-select>
        </el-col>

        <el-col :span="6">
          <el-select v-model="filters.wordCount" placeholder="字数范围" clearable>
            <el-option label="全部" value=""></el-option>
            <el-option label="50万以下" value="0-50"></el-option>
            <el-option label="50-100万" value="50-100"></el-option>
            <el-option label="100万以上" value="100+"></el-option>
          </el-select>
        </el-col>

        <el-col :span="6">
          <el-button type="primary" @click="fetchBooks">筛选</el-button>
          <el-button @click="resetFilters">重置</el-button>
        </el-col>
      </el-row>
    </div>

    <!-- 添加书籍按钮 -->
    <div class="add-book">
      <el-button type="success" @click="showAddDialog">添加书籍</el-button>
    </div>

    <!-- 书籍列表 -->
    <div class="book-list">
      <el-row :gutter="20">
        <el-col
            v-for="book in books.content"
            :key="book.id"
            :xs="12" :sm="8" :md="6"
            class="book-item"
        >
          <el-card :body-style="{ padding: '0px' }">
            <!-- 添加点击事件 -->
            <img
                :src="book.coverUrl"
                class="book-cover"
                @click="goToBookDetail(book.id)"
                style="cursor: pointer;"
            >
            <div style="padding: 14px;">
              <!-- 书名也添加点击事件 -->
              <h3 style="cursor: pointer;" @click="goToBookDetail(book.id)">{{ book.title }}</h3>
              <p>作者: {{ book.authorName }}</p>
              <p>类型: {{ book.category }}</p>
              <p>字数: {{ (book.wordCount / 10000).toFixed(1) }}万</p>

              <div class="book-actions">
                <el-button type="primary" size="small" @click.stop="editBook(book)">编辑</el-button>
                <el-button type="danger" size="small" @click.stop="deleteBook(book.id)">删除</el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 分页控件 -->
    <div class="pagination">
      <el-pagination
          background
          layout="prev, pager, next"
          :total="books.totalElements"
          :page-size="filters.size"
          :current-page="filters.page + 1"
          @current-change="handlePageChange"
      />
    </div>

    <!-- 添加/编辑对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="50%">
      <el-form :model="currentBook" label-width="80px">
        <el-form-item label="书名" prop="title">
          <el-input v-model="currentBook.title"></el-input>

        </el-form-item>
        <el-form-item label="作者" prop="authorId">
          <el-select
              v-model="currentBook.authorId"
              filterable
              placeholder="选择作者"
              style="width: 100%"
          >
            <el-option
                v-for="author in authors"
                :key="author.id"
                :label="author.name"
                :value="author.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="封面URL">
          <el-input v-model="currentBook.coverUrl"></el-input>
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="currentBook.category" placeholder="请选择类型">
            <el-option label="科幻" value="科幻"></el-option>
            <el-option label="玄幻" value="玄幻"></el-option>
            <el-option label="奇幻" value="奇幻"></el-option>
            <el-option label="青春" value="青春"></el-option>
            <el-option label="悬疑" value="悬疑"></el-option>
            <el-option label="历史" value="历史"></el-option>
            <el-option label="武侠" value="武侠"></el-option>
            <el-option label="言情" value="言情"></el-option>
            <el-option label="文学" value="文学"></el-option>
            <el-option label="历史" value="历史"></el-option>
            <el-option label="其他" value="其他"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="字数">
          <el-input-number v-model="currentBook.wordCount" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="评分">
          <el-rate
              v-model="currentBook.rating"
              :max="10"
              :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
              show-score
              score-template="{value} 分"
          />
        </el-form-item>
        <el-form-item label="描述">
          <el-input type="textarea" v-model="currentBook.description"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitBook">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
const isEditMode = ref(false)
import { ref, reactive, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import router from '@/router'
import BookDetail  from "./BookDetail.vue";
const authors = ref([])
// 书籍数据
const books = reactive({
  content: [],
  totalElements: 0
})
const fetchAuthors = async () => {
  try {
    const response = await axios.get('/api/writers?size=100')
    authors.value = response.data.content
  } catch (error) {
    console.error('获取作者列表失败:', error)
  }
}
// 筛选条件
const filters = reactive({
  page: 0,
  size: 12,
  region: '',
  category: '',
  status: '',
  wordCount: '',
  sort: 'hot',
  rating: 5 // 默认5分
})
const goToBookDetail = (bookId) => {
  router.push({ name: 'BookDetail', params: { id: bookId } })
}
// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('添加书籍')
const currentBook = reactive({
  id: null,
  title: '',
  authorName: '',
  coverUrl: '',
  category: '',
  wordCount: 0,
  description: ''

})

// 获取书籍列表
const fetchBooks = async () => {
  try {
    const params = {
      page: filters.page,
      size: filters.size,
      category: filters.category || null,
      status: filters.status !== '' ? parseInt(filters.status) : null,
      minWordCount: filters.wordCount === '0-50' ? 0 :
          filters.wordCount === '50-100' ? 500000 :
              filters.wordCount === '100+' ? 1000000 : null,
      maxWordCount: filters.wordCount === '0-50' ? 499999 :
          filters.wordCount === '50-100' ? 999999 : null
    };

    // 移除空值参数
    Object.keys(params).forEach(key => {
      if (params[key] === null || params[key] === '') {
        delete params[key];
      }
    });

    const response = await axios.get('/api/books', { params });
    books.content = response.data.content;
    books.totalElements = response.data.totalElements;
  } catch (error) {
    console.error('获取书籍列表失败:', error);
    ElMessage.error('获取书籍列表失败');
  }
};
// 重置筛选条件
const resetFilters = () => {
  filters.page = 0
  filters.region = ''
  filters.category = ''
  filters.status = ''
  filters.wordCount = ''
  fetchBooks()
}

// 分页变化
const handlePageChange = (page) => {

  filters.page = page - 1
  fetchBooks()
}

// 显示添加对话框
// 添加书籍入口
// 显示添加对话框
const showAddDialog = () => {
  isEditMode.value = false
  Object.assign(currentBook, {
    id: null,
    title: '',
    authorName: '',
    coverUrl: '',
    category: '玄幻',
    wordCount: 0,
    description: '',
    rating: 5 // 默认5分
  })
  dialogVisible.value = true
}

// 编辑书籍入口
const editBook = (book) => {
  event.stopPropagation() // 阻止事件冒泡
  isEditMode.value = true
  Object.assign(currentBook, JSON.parse(JSON.stringify(book)))
  dialogVisible.value = true
}

// 提交保存
const submitBook = async () => {
  try {
    if (isEditMode.value) {
      await axios.put(`/api/books/${currentBook.id}`, currentBook)
      ElMessage.success('更新成功')
    } else {
      await axios.post('/api/books', currentBook)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    await fetchBooks()
  } catch (error) {
    ElMessage.error(`操作失败: ${error.response?.data?.message || error.message}`)
  }
}
// 在您的Vue组件中添加
const handleAdd = async () => {
  try {
    const res = await axios.post('/api/books', formData)
    ElMessage.success(`添加成功！ID: ${res.data.id}`)
    fetchBooks() // 刷新列表
  } catch (err) {
    ElMessage.error(`添加失败: ${err.response?.data?.message || err.message}`)
  }
}
// 删除书籍
const deleteBook = async (id) => {
  event.stopPropagation() // 阻止事件冒泡
  try {
    await ElMessageBox.confirm('确定删除该书籍吗？', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await axios.delete(`/api/books/${id}`)
    ElMessage.success('删除成功')
    fetchBooks()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}
// 在删除操作后
const handleDelete = async (id) => {
  await axios.delete(`/api/books/${id}`)
  fetchBooks() // 立即刷新
}
// 初始化加载
onMounted(() => {
  fetchBooks()
  fetchAuthors()
})
</script>

<style scoped>
.book-list-container {
  padding: 20px;
}
.book-cover:hover {
  opacity: 0.9;
  transform: scale(1.02);
  transition: all 0.3s ease;
}

/* 书名悬停效果 */
h3:hover {
  color: #409eff;
}
.filter-bar {
  margin-bottom: 20px;
}

.add-book {
  margin-bottom: 20px;
}

.book-list {
  margin-bottom: 20px;
}

.book-item {
  margin-bottom: 20px;
}

.book-cover {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.book-actions {
  margin-top: 10px;
  display: flex;
  justify-content: space-between;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>