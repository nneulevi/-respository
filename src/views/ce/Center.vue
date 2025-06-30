<template>
  <div class="personal-center">
    <!-- 顶部标题 -->
    <div class="header">
      <div class="header-content">
        <h2>个人信息中心</h2>
        <el-button
            type="primary"
            link
            class="back-home"
            @click="goToHome"
        >
          返回首页
        </el-button>
      </div>
      <el-divider />
    </div>

    <!-- 主体内容 -->
    <div class="content">
      <el-tabs v-model="activeTab" type="card" class="personal-tabs">
        <!-- 基本资料 -->
        <el-tab-pane label="基本资料" name="basic">
          <el-form
              ref="basicForm"
              :model="userInfo"
              :rules="basicRules"
              label-width="100px"
              class="personal-form"
          >
            <el-form-item label="用户头像">
              <el-upload
                  class="avatar-uploader"
                  action="/api/upload/avatar"
                  :show-file-list="false"
                  :on-success="handleAvatarSuccess"
                  :before-upload="beforeAvatarUpload"
              >
                <img v-if="userInfo.avatar" :src="userInfo.avatar" class="avatar" />
                <el-icon v-else class="avatar-uploader-icon"><UserFilled /></el-icon>
              </el-upload>
            </el-form-item>

            <el-form-item label="用户昵称" prop="nickname">
              <el-input
                  v-model="userInfo.nickname"
                  placeholder="请输入昵称"
                  clearable
              />
            </el-form-item>

            <el-form-item label="手机号码" prop="phone">
              <el-input
                  v-model="userInfo.phone"
                  placeholder="请输入手机号"
                  maxlength="11"
              />
            </el-form-item>

            <el-form-item label="电子邮箱" prop="email">
              <el-input
                  v-model="userInfo.email"
                  placeholder="请输入邮箱"
              />
            </el-form-item>

            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="userInfo.gender">
                <el-radio :label="1">男</el-radio>
                <el-radio :label="2">女</el-radio>
                <el-radio :label="0">保密</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item>
              <el-button
                  type="primary"
                  @click="submitBasicInfo"
                  :loading="basicLoading"
              >
                保存修改
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 修改密码 -->
        <el-tab-pane label="修改密码" name="password">
          <el-form
              ref="passwordForm"
              :model="passwordForm"
              :rules="passwordRules"
              label-width="100px"
              class="personal-form"
          >
            <el-form-item label="旧密码" prop="oldPassword">
              <el-input
                  v-model="passwordForm.oldPassword"
                  type="password"
                  show-password
                  placeholder="请输入当前密码"
              />
            </el-form-item>

            <el-form-item label="新密码" prop="newPassword">
              <el-input
                  v-model="passwordForm.newPassword"
                  type="password"
                  show-password
                  placeholder="8-20位，包含字母和数字"
              />
              <div class="password-strength">
                <span :class="{'active': passwordStrength >= 1}">弱</span>
                <span :class="{'active': passwordStrength >= 2}">中</span>
                <span :class="{'active': passwordStrength >= 3}">强</span>
              </div>
            </el-form-item>

            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input
                  v-model="passwordForm.confirmPassword"
                  type="password"
                  show-password
                  placeholder="请再次输入新密码"
              />
            </el-form-item>

            <el-form-item>
              <el-button
                  type="primary"
                  @click="submitPassword"
                  :loading="passwordLoading"
              >
                确认修改
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 个人信息 -->
        <el-tab-pane label="个人信息" name="info">
          <el-descriptions
              :column="1"
              border
              class="personal-info"
          >
            <el-descriptions-item label="用户ID">{{ userInfo.id }}</el-descriptions-item>
            <el-descriptions-item label="用户名">{{ userInfo.username }}</el-descriptions-item>
            <el-descriptions-item label="用户角色">
              <el-tag :type="roleTypeMap[userInfo.role]">
                {{ roleNameMap[userInfo.role] }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="注册时间">{{ formatDate(userInfo.createTime) }}</el-descriptions-item>
            <el-descriptions-item label="最后登录时间">{{ formatDate(userInfo.lastLoginTime) }}</el-descriptions-item>
            <el-descriptions-item label="登录IP">{{ userInfo.lastLoginIp || '无记录' }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, reactive } from 'vue'
import { ElMessage, ElLoading } from 'element-plus'
import { UserFilled } from '@element-plus/icons-vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()
const activeTab = ref('basic')

// 用户信息
const userInfo = ref({
  id: 10001,
  username: 'zhangsan',
  nickname: '张三',
  avatar: '',
  phone: '13800138000',
  email: 'zhangsan@example.com',
  gender: 1,
  role: 3,
  createTime: '2023-06-01T10:30:00',
  lastLoginTime: '2023-06-15T15:45:22',
  lastLoginIp: '192.168.1.100'
})

// 角色映射
const roleNameMap = {
  1: '超级管理员',
  2: '企业管理员',
  3: '普通用户'
}

const roleTypeMap = {
  1: 'danger',
  2: 'warning',
  3: 'success'
}

// 密码表单
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})
const goToHome = () => {
  router.push('/')
}
// 密码强度计算
const passwordStrength = computed(() => {
  if (!passwordForm.newPassword) return 0
  if (passwordForm.newPassword.length < 8) return 1

  const hasNumber = /\d/.test(passwordForm.newPassword)
  const hasLetter = /[a-zA-Z]/.test(passwordForm.newPassword)
  const hasSpecial = /[^a-zA-Z0-9]/.test(passwordForm.newPassword)

  return (hasNumber ? 1 : 0) + (hasLetter ? 1 : 0) + (hasSpecial ? 1 : 0)
})

// 表单验证规则
const basicRules = {
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 2, max: 12, message: '长度在2到12个字符', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入当前密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { validator: validatePassword, trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

// 自定义密码验证
function validatePassword(rule, value, callback) {
  if (value.length < 8) {
    callback(new Error('密码长度不能少于8位'))
  } else if (!/\d/.test(value) || !/[a-zA-Z]/.test(value)) {
    callback(new Error('密码必须包含字母和数字'))
  } else {
    callback()
  }
}

// 确认密码验证
function validateConfirmPassword(rule, value, callback) {
  if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

// 头像上传处理
const handleAvatarSuccess = (res) => {
  if (res.code === 200) {
    userInfo.value.avatar = res.data.url
    ElMessage.success('头像上传成功')
  } else {
    ElMessage.error(res.message || '头像上传失败')
  }
}

const beforeAvatarUpload = (file) => {
  const isJPGorPNG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPGorPNG) {
    ElMessage.error('头像图片只能是 JPG/PNG 格式!')
  }
  if (!isLt2M) {
    ElMessage.error('头像图片大小不能超过 2MB!')
  }

  return isJPGorPNG && isLt2M
}

// 提交基本信息
const basicLoading = ref(false)
const submitBasicInfo = async () => {
  try {
    const valid = await basicForm.value.validate()
    if (!valid) return

    basicLoading.value = true
    const response = await axios.put('/api/user/profile', userInfo.value)

    ElMessage.success('个人信息更新成功')
  } catch (error) {
    console.error('更新失败:', error)
    ElMessage.error(error.response?.data?.message || '更新失败')
  } finally {
    basicLoading.value = false
  }
}

// 提交密码修改
const passwordLoading = ref(false)
const submitPassword = async () => {
  try {
    const valid = await passwordForm.value.validate()
    if (!valid) return

    passwordLoading.value = true
    const response = await axios.post('/api/user/change-password', {
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword
    })

    ElMessage.success('密码修改成功')
    passwordForm.value = { oldPassword: '', newPassword: '', confirmPassword: '' }
  } catch (error) {
    console.error('密码修改失败:', error)
    ElMessage.error(error.response?.data?.message || '密码修改失败')
  } finally {
    passwordLoading.value = false
  }
}

// 日期格式化
const formatDate = (dateString) => {
  if (!dateString) return '无记录'
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false
  }).replace(/\//g, '-')
}
</script>

<style scoped>
.personal-center {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.header {
  margin-bottom: 20px;
}

.header h2 {
  margin: 0;
  font-size: 20px;
  color: #333;
}

.content {
  padding: 0 20px;
}

.personal-tabs {
  margin-top: 10px;
}

.personal-form {
  max-width: 600px;
  margin-top: 20px;
}

.avatar-uploader {
  display: inline-block;
}
.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.back-home {
  margin-right: 20px;
}
.avatar-uploader :deep(.el-upload) {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader :deep(.el-upload:hover) {
  border-color: var(--el-color-primary);
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}

.avatar {
  width: 120px;
  height: 120px;
  display: block;
}

.password-strength {
  display: flex;
  margin-top: 8px;
}

.password-strength span {
  flex: 1;
  text-align: center;
  padding: 2px 0;
  color: #999;
  background: #f0f0f0;
  margin-right: 3px;
}

.password-strength span:last-child {
  margin-right: 0;
}

.password-strength span.active {
  color: #fff;
  background: var(--el-color-primary);
}

.personal-info {
  margin-top: 20px;
  max-width: 600px;
}
</style>