<template>
  <div class="user-container">
    <!-- 顶部搜索栏 -->
    <div class="search-bar">
      <el-row :gutter="20">
        <el-col :span="18">
          <el-input
              v-model="searchKeyword"
              placeholder="输入用户名或邮箱搜索"
              clearable
              @clear="fetchUsers"
              @keyup.enter="fetchUsers"
          >
            <template #append>
              <el-button @click="fetchUsers">
                <el-icon><Search /></el-icon>
              </el-button>
            </template>
          </el-input>
        </el-col>
        <el-col :span="6" class="text-right">
          <el-button type="primary" @click="showAddDialog">
            <el-icon><Plus /></el-icon>
            添加用户
          </el-button>
        </el-col>
      </el-row>
    </div>

    <!-- 用户列表 -->
    <div class="user-list">
      <el-table
          :data="users.content"
          border
          stripe
          v-loading="loading"
          style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column label="头像" width="100">
          <template #default="{ row }">
            <el-avatar :src="row.avatar" v-if="row.avatar" />
            <el-avatar v-else>{{ row.username.charAt(0).toUpperCase() }}</el-avatar>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180" />
        <el-table-column prop="updatedAt" label="更新时间" width="180" />
        <el-table-column label="操作" width="220">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="editUser(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteUser(row.id)">删除</el-button>
            <el-button size="small" @click="resetPassword(row.id)">重置密码</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
          background
          layout="prev, pager, next, sizes, total"
          :total="users.totalElements"
          v-model:page-size="pagination.size"
          v-model:current-page="pagination.page"
          @current-change="fetchUsers"
          @size-change="handleSizeChange"
      />
    </div>

    <!-- 添加/编辑对话框 -->
    <el-dialog
        :title="dialogTitle"
        v-model="dialogVisible"
        width="500px"
    >
      <el-form :model="currentUser" label-width="100px" :rules="rules" ref="userForm">
        <el-form-item label="用户名" prop="username" required>
          <el-input v-model="currentUser.username" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email" required>
          <el-input v-model="currentUser.email" type="email" />
        </el-form-item>
        <el-form-item label="头像URL" prop="avatar">
          <el-input v-model="currentUser.avatar" />
          <div v-if="currentUser.avatar" class="avatar-preview">
            <img :src="currentUser.avatar" alt="头像预览" />
          </div>
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!isEditMode">
          <el-input v-model="currentUser.password" type="password" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitUser">确定</el-button>
      </template>
    </el-dialog>

    <!-- 重置密码对话框 -->
    <el-dialog
        v-model="passwordDialogVisible"
        title="重置密码"
        width="400px"
    >
      <el-form :model="passwordForm" label-width="100px" :rules="passwordRules" ref="passwordForm">
        <el-form-item label="新密码" prop="newPassword" required>
          <el-input v-model="passwordForm.newPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword" required>
          <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="passwordDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmResetPassword">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

// 用户数据
const users = reactive({
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
const dialogTitle = ref('添加用户')
const isEditMode = ref(false)
const currentUser = reactive({
  id: null,
  username: '',
  email: '',
  avatar: '',
  password: ''
})

// 密码重置相关
const passwordDialogVisible = ref(false)
const passwordForm = reactive({
  userId: null,
  newPassword: '',
  confirmPassword: ''
})

// 表单验证规则
const rules = reactive({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' }
  ]
})

const passwordRules = reactive({
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入密码不一致!'))
        } else {
          callback()
        }
      }, trigger: 'blur' }
  ]
})

// 获取用户列表
const fetchUsers = async () => {
  try {
    loading.value = true
    const params = {
      page: pagination.page - 1,
      size: pagination.size,
      keyword: searchKeyword.value || null
    }

    const response = await axios.get('/api/users', { params })
    users.content = response.data.content
    users.totalElements = response.data.totalElements
  } catch (error) {
    ElMessage.error('获取用户列表失败: ' + error.message)
  } finally {
    loading.value = false
  }
}

// 每页数量变化
const handleSizeChange = (size) => {
  pagination.size = size
  fetchUsers()
}

// 显示添加对话框
const showAddDialog = () => {
  isEditMode.value = false
  dialogTitle.value = '添加用户'
  Object.assign(currentUser, {
    id: null,
    username: '',
    email: '',
    avatar: '',
    password: ''
  })
  dialogVisible.value = true
}

// 编辑用户
const editUser = (user) => {
  isEditMode.value = true
  dialogTitle.value = '编辑用户'
  Object.assign(currentUser, JSON.parse(JSON.stringify(user)))
  dialogVisible.value = true
}

// 提交用户信息
const submitUser = async () => {
  try {
    if (isEditMode.value) {
      await axios.put(`/api/users/${currentUser.id}`, currentUser)
      ElMessage.success('更新成功')
    } else {
      await axios.post('/api/users', currentUser)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    fetchUsers()
  } catch (error) {
    ElMessage.error(`操作失败: ${error.response?.data?.message || error.message}`)
  }
}

// 删除用户
const deleteUser = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该用户吗？此操作不可恢复！', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await axios.delete(`/api/users/${id}`)
    ElMessage.success('删除成功')
    fetchUsers()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败: ' + error.message)
    }
  }
}

// 重置密码
const resetPassword = (userId) => {
  passwordForm.userId = userId
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
  passwordDialogVisible.value = true
}

// 确认重置密码
const confirmResetPassword = async () => {
  try {
    await axios.put(`/api/users/${passwordForm.userId}/password`, {
      newPassword: passwordForm.newPassword
    })
    ElMessage.success('密码重置成功')
    passwordDialogVisible.value = false
  } catch (error) {
    ElMessage.error(`密码重置失败: ${error.response?.data?.message || error.message}`)
  }
}

// 初始化加载
onMounted(() => {
  fetchUsers()
})
</script>

<style scoped>
.user-container {
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
</style>