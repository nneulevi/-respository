<template>
  <div class="user-management-container">
    <div class="header-actions">
      <el-button type="primary" plain @click="goBack" class="back-button">
        <el-icon><ArrowLeft /></el-icon>
        返回首页
      </el-button>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="用户名称">
          <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable />
        </el-form-item>

        <el-form-item label="手机号码">
          <el-input v-model="searchForm.phone" placeholder="请输入手机号" clearable />
        </el-form-item>

        <el-form-item label="电子邮箱">
          <el-input v-model="searchForm.email" placeholder="请输入邮箱" clearable />
        </el-form-item>

        <el-form-item label="性别">
          <el-select v-model="searchForm.gender" placeholder="请选择性别" clearable>
            <el-option label="男" value="male" />
            <el-option label="女" value="female" />
            <el-option label="保密" value="secret" />
          </el-select>
        </el-form-item>

        <el-form-item label="注册时间">
          <el-date-picker
              v-model="searchForm.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="YYYY-MM-DD"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 批量操作 -->
    <div class="batch-actions" v-if="selectedUsers.length > 0">
      <el-button type="danger" @click="batchDelete" :loading="batchLoading">批量删除</el-button>
      <el-button @click="batchUpdateStatus(1)" :loading="batchLoading">批量启用</el-button>
      <el-button @click="batchUpdateStatus(0)" :loading="batchLoading">批量禁用</el-button>
      <span class="selected-count">已选择 {{ selectedUsers.length }} 项</span>
    </div>

    <!-- 用户表格 -->
    <el-table
        :data="userList"
        border
        stripe
        v-loading="loading"
        @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column label="头像" width="80">
        <template #default="{row}">
          <el-avatar :size="50" :src="row.avatar_url || defaultAvatar">
            {{ row.username?.charAt(0)?.toUpperCase() }}
          </el-avatar>
        </template>
      </el-table-column>
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="phone" label="手机号" width="120" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column label="性别" width="80">
        <template #default="{row}">
          {{ genderMap[row.gender] || '保密' }}
        </template>
      </el-table-column>
      <el-table-column label="状态" width="120">
        <template #default="{row}">
          <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(row)"
          />
          <span style="margin-left: 8px">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="creatTime" label="注册时间" width="180" />
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{row}">
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          @current-change="handlePageChange"
          @size-change="handleSizeChange"
          layout="total, sizes, prev, pager, next, jumper"
          :page-sizes="[10, 20, 50, 100]"
      />
    </div>

    <!-- 用户编辑对话框 -->
    <el-dialog
        v-model="dialogVisible"
        :title="dialogTitle"
        width="600px"
        :close-on-click-modal="false"
    >
      <el-form
          ref="dialogFormRef"
          :model="dialogForm"
          :rules="dialogRules"
          label-width="100px"
      >
        <el-form-item label="头像">
          <el-upload
              class="avatar-uploader"
              action="/api/upload/avatar"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
          >
            <el-avatar :size="100" :src="dialogForm.avatar_url || defaultAvatar">
              {{ dialogForm.username?.charAt(0)?.toUpperCase() }}
            </el-avatar>
            <template #tip>
              <div class="el-upload__tip">点击上传新头像</div>
            </template>
          </el-upload>
        </el-form-item>

        <el-form-item label="用户名" prop="username">
          <el-input v-model="dialogForm.username" placeholder="请输入用户名" />
        </el-form-item>

        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="dialogForm.phone" placeholder="请输入手机号" />
        </el-form-item>

        <el-form-item label="电子邮箱" prop="email">
          <el-input v-model="dialogForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="修改密码">
          <el-input
              v-model="dialogForm.password"
              placeholder="输入新密码(留空不修改)"
              type="password"
              show-password
          />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="dialogForm.gender">
            <el-radio label="male">男</el-radio>
            <el-radio label="female">女</el-radio>
            <el-radio label="secret">保密</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-switch
              v-model="dialogForm.status"
              :active-value="1"
              :inactive-value="0"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitDialog" :loading="dialogLoading">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'
import { useUserStore } from '@/stores/user'
// 在图标引入部分添加 ArrowLeft
import { ArrowLeft } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 在方法部分添加返回函数
const goBack = () => {
  router.push('/news')
}
// 获取当前用户(管理员)信息
const userStore = useUserStore()
const isSuperAdmin = computed(() => userStore.isSuperAdmin)
const currentUser = computed(() => userStore.currentUser)
// 默认头像
const defaultAvatar = ref('https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png')

// 性别映射
const genderMap = {
  male: '男',
  female: '女',
  secret: '保密'
}

// 搜索表单
const searchForm = reactive({
  username: '',
  phone: '',
  email: '',
  gender: '',
  dateRange: []
})

// 分页数据
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 用户列表
const userList = ref([])
const selectedUsers = ref([])
const loading = ref(false)
const batchLoading = ref(false)
const dialogLoading = ref(false)

// 对话框相关
const dialogVisible = ref(false)
const dialogFormRef = ref(null)
const isCreate = ref(false)
const dialogTitle = computed(() => isCreate.value ? '新增用户' : '编辑用户')
const dialogForm = reactive({
  username: '',
  phone: '',
  email: '',
  gender: 'male',
  status: 1,
  avatar_url: '',
  password: '' // 新增密码字段
})

// 表单验证规则
const dialogRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在3到20个字符', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (!/^[a-zA-Z0-9_]+$/.test(value)) {
          callback(new Error('只能包含字母、数字和下划线'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]

}

// 头像上传处理
const handleAvatarSuccess = (res) => {
  if (res.code === 200) {
    dialogForm.avatar_url = res.data.url
    ElMessage.success('头像上传成功')
  }
}

const beforeAvatarUpload = (file) => {
  const isJPGorPNG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPGorPNG) {
    ElMessage.error('头像图片只能是 JPG/PNG 格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('头像图片大小不能超过 2MB!')
    return false
  }
  return true
}

// 获取用户列表
const fetchUserList = async () => {
  try {
    loading.value = true

    const params = {
      pageNum: pagination.current,
      pageSize: pagination.size,
      username: searchForm.username,
      phone: searchForm.phone,
      email: searchForm.email,
      gender: searchForm.gender,
      startDate: searchForm.dateRange?.[0],
      endDate: searchForm.dateRange?.[1]
    }

    const response = await axios.get('/getUserlist', { params })
    userList.value = response.data.list.map(user => ({
      ...user,
      // 确保性别字段有默认值
      gender: user.gender || 'secret'
    }))
    pagination.total = response.data.total
  } catch (error) {
    ElMessage.error('获取用户列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索用户
const handleSearch = () => {
  pagination.current = 1
  fetchUserList()
}

// 重置搜索
const resetSearch = () => {
  searchForm.username = ''
  searchForm.phone = ''
  searchForm.email = ''
  searchForm.gender = ''
  searchForm.dateRange = []
  handleSearch()
}

// 分页变化
const handlePageChange = () => {
  fetchUserList()
}

// 每页数量变化
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.current = 1
  fetchUserList()
}

// 表格选择变化
const handleSelectionChange = (selection) => {
  selectedUsers.value = selection
}

// 编辑用户
const handleEdit = (row) => {
  isCreate.value = false
  Object.assign(dialogForm, {
    username: row.username,
    phone: row.phone,
    email: row.email,
    gender: row.gender || 'secret', // 处理可能的undefined
    status: row.status,
    avatar_url: row.avatar_url,
    password: '' // 重置密码字段
  })
  dialogVisible.value = true
}

// 删除用户
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定删除用户 "${row.username}" 吗?`, '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await axios.delete(`/deleteUser?username=${row.username}`)
    ElMessage.success('删除成功')
    fetchUserList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 批量删除
const batchDelete = async () => {
  try {
    await ElMessageBox.confirm(`确定删除选中的 ${selectedUsers.value.length} 个用户吗?`, '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    batchLoading.value = true
    await Promise.all(
        selectedUsers.value.map(user =>
            axios.delete(`/deleteUser?username=${user.username}`)
        )
    )

    ElMessage.success('批量删除成功')
    selectedUsers.value = []
    fetchUserList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('批量删除失败')
    }
  } finally {
    batchLoading.value = false
  }
}

// 批量更新状态
const batchUpdateStatus = async (status) => {
  try {
    const action = status === 1 ? '启用' : '禁用'
    await ElMessageBox.confirm(`确定${action}选中的 ${selectedUsers.value.length} 个用户吗?`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    batchLoading.value = true
    await Promise.all(
        selectedUsers.value.map(user =>
            axios.post('/changeUserinfo', {
              now_username: user.username,
              username: user.username,
              status: status
            })
        )
    )

    ElMessage.success(`批量${action}成功`)
    fetchUserList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(`批量${action}失败`)
    }
  } finally {
    batchLoading.value = false
  }
}

// 状态变更处理
const handleStatusChange = async (row) => {
  try {
    console.log(row)
    console.log(userStore.currentUser.username)
    console.log(userStore.currentUser)
    const response = await axios.post('/changeUserinfo',
        // 请求体
        {
          status: row.status,
        },
        // 配置查询参数
        {
          params: {
            now_username: userStore.currentUser.username, // 当前管理员用户名
            username: row.username // 被编辑用户的原始用户名
          }
        }
    );




    ElMessage.success('状态更新成功')
  } catch (error) {
    ElMessage.error('状态更新失败')
    // 恢复原状态
    row.status = row.status === 1 ? 0 : 1
  }
}

// 修改提交对话框表单方法
const submitDialog = async () => {
  try {
    dialogLoading.value = true

    // 准备提交数据
    const submitData = {
      username: dialogForm.username,
      phone: dialogForm.phone,
      email: dialogForm.email,
      gender: dialogForm.gender,
      status: dialogForm.status,
      avatar_url: dialogForm.avatar_url
    }

    // 如果有新密码，添加到提交数据中
    if (dialogForm.password) {
      submitData.password = dialogForm.password
    }

    if (isCreate.value) {
      // 创建用户时必须设置密码
      submitData.password = dialogForm.password || 'defaultPassword' // 你需要设置一个默认密码或确保必填
      await axios.post('/adduser', submitData)
      ElMessage.success('用户创建成功')
    } else {
      const response = await axios.post('/changeUserinfo',
          submitData,
          {
            params: {
              now_username: userStore.currentUser.username,
              username: dialogForm.username
            }
          }
      )
      if (response.data === 1) {
        ElMessage.success('用户更新成功')
      } else {
        ElMessage.error('更新失败')
      }
    }

    dialogVisible.value = false
    fetchUserList()
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '操作失败')
  } finally {
    dialogLoading.value = false
  }
}

// 初始化加载
onMounted(async () => {
  await userStore.initUser();
  fetchUserList()
  console.log(userStore.currentUser)
  console.log(userStore.isSuperAdmin)
})
</script>

<style scoped>
.user-management-container {
  padding: 20px;
}
.search-bar {
  margin-bottom: 20px;
}
.batch-actions {
  margin-bottom: 15px;
}
.selected-count {
  margin-left: 15px;
  color: #666;
}
.pagination {
  margin-top: 20px;
  text-align: right;
}
.avatar-uploader {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.avatar-uploader :deep(.el-upload) {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}
.avatar-uploader :deep(.el-upload:hover) {
  border-color: var(--el-color-primary);
}
.el-upload__tip {
  margin-top: 10px;
  color: var(--el-text-color-secondary);
  font-size: 12px;
}
</style>

<style scoped>
.user-management-container {
  padding: 20px;
}
.search-bar {
  margin-bottom: 20px;
}
.batch-actions {
  margin-bottom: 15px;
}
.selected-count {
  margin-left: 15px;
  color: #666;
}
.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>

<style scoped>
.user-management-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
}

.search-bar {
  margin-bottom: 20px;
}

.action-bar {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.el-form-item__tip {
  font-size: 12px;
  color: #999;
  line-height: 1.5;
}
</style>