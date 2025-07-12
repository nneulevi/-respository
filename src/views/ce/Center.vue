<template>
  <div class="personal-center">
    <!-- 顶部标题 -->
    <div class="header">
      <div class="header-content">
        <h2>个人信息中心</h2>
        <el-button type="danger" link class="logout-btn" @click="handleLogout">
          退出登录
        </el-button>
        <el-button type="primary" link class="back-home" @click="goToHome">
          返回首页
        </el-button>
      </div>
      <el-divider />
    </div>

    <!-- 主体内容 -->
    <div class="content">
      <el-tabs v-model="state.activeTab" type="card" class="personal-tabs" lazy>
        <!-- 基本资料 -->
        <el-tab-pane label="基本资料" name="basic">
          <el-form
              ref="basicForm"
              :model="state.forms.basic"
              :rules="basicRules"
              label-width="100px"
              class="personal-form"
          >
            <el-form-item label="用户头像">
              <el-upload
                  class="avatar-uploader"
                  action="/api/upload"
                  :show-file-list="false"
                  :on-success="handleAvatarSuccess"
                  :before-upload="beforeAvatarUpload"
                  accept="image/*"
              >
                <img v-if="state.forms.basic.avatar_url" :src="state.forms.basic.avatar_url" class="avatar" />
                <div v-else class="uploader-area">
                  <el-icon class="uploader-icon"><UserFilled /></el-icon>
                  <span class="uploader-text">点击上传头像</span>
                </div>
                <template #tip>
                  <div class="el-upload__tip">支持JPG/PNG格式，大小不超过2MB</div>
                </template>
              </el-upload>
            </el-form-item>

            <el-form-item label="用户昵称" prop="nickname">
              <el-input
                  v-model="state.forms.basic.nickname"
                  placeholder="请输入昵称"
                  clearable
              />
            </el-form-item>

            <el-form-item label="手机号码" prop="phone">
              <el-input
                  v-model="state.forms.basic.phone"
                  placeholder="请输入手机号"
                  maxlength="11"
              />
            </el-form-item>

            <el-form-item label="电子邮箱" prop="email">
              <el-input
                  v-model="state.forms.basic.email"
                  placeholder="请输入邮箱"
              />
            </el-form-item>

            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="state.forms.basic.gender">
                <el-radio :label="1">男</el-radio>
                <el-radio :label="0">女</el-radio>
                <el-radio :label="2">保密</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item>
              <el-button
                  type="primary"
                  @click="submitBasicInfo"
                  :loading="state.loading.basic"
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
              :model="state.forms.password"
              :rules="passwordRules"
              label-width="100px"
              class="personal-form"
          >
            <el-form-item label="旧密码" prop="oldPassword">
              <el-input
                  v-model="state.forms.password.oldPassword"
                  type="password"
                  show-password
                  placeholder="请输入当前密码"
              />
            </el-form-item>

            <el-form-item label="新密码" prop="newPassword">
              <el-input
                  v-model="state.forms.password.newPassword"
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
                  v-model="state.forms.password.confirmPassword"
                  type="password"
                  show-password
                  placeholder="请再次输入新密码"
              />
            </el-form-item>

            <el-form-item>
              <el-button
                  type="primary"
                  @click="submitPassword"
                  :loading="state.loading.password"
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
            <el-descriptions-item label="用户名">{{ state.forms.basic.username }}</el-descriptions-item>
            <el-descriptions-item label="用户角色">
              <el-tag :type="roleTypeMap[userStore.currentUser?.status]">
                {{ roleNameMap[userStore.currentUser?.status] }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="注册时间">{{ formatDate(state.forms.basic.creatTime) }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { UserFilled } from '@element-plus/icons-vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

// 统一状态管理
const state = reactive({
  activeTab: 'basic',
  loading: {
    basic: false,
    password: false
  },
  forms: {
    basic: {
      username: '',
      nickname: '',
      avatar_url: '',
      phone: '',
      email: '',
      gender: 0,
      creatTime: ''
    },
    password: {
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    }
  }
})

// 表单引用
const basicForm = ref(null)
const passwordForm = ref(null)

// 角色映射
const roleNameMap = {
  1: '管理员',
  0: '普通用户'
}

const roleTypeMap = {
  1: 'danger',
  0: 'success'
}

// 密码强度计算
const passwordStrength = computed(() => {
  if (!state.forms.password.newPassword) return 0
  if (state.forms.password.newPassword.length < 8) return 1

  const hasNumber = /\d/.test(state.forms.password.newPassword)
  const hasLetter = /[a-zA-Z]/.test(state.forms.password.newPassword)
  const hasSpecial = /[^a-zA-Z0-9]/.test(state.forms.password.newPassword)

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

// 初始化用户数据
const fetchUserInfo = async () => {
  try {
    const username = userStore.currentUser?.username
    if (!username) return

    const response = await axios.get(`/getUserinfo?username=${username}`)
    if (response.data) {
      Object.assign(state.forms.basic, {
        username: response.data.username,
        nickname: response.data.username,
        avatar_url: response.data.avatar_url,
        phone: response.data.phone,
        email: response.data.email,
        gender: response.data.gender === 'male' ? 0 :
            response.data.gender === 'female' ? 1 : 2,
        creatTime: response.data.creatTime
      })
    }
  } catch (error) {
    ElMessage.error('获取用户信息失败')
  }
}
const handleLogout = () => {
  // 调用store的logout方法
  userStore.logout()

  // 清除可能存在的路由历史
  router.replace('/newlogin').then(() => {
    // 强制刷新页面以确保完全清除状态
    window.location.reload()
  })
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
  if (value !== state.forms.password.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const handleAvatarSuccess = async (response) => {
  console.log('[头像上传响应]', response);

  try {
    if (response.code === 200 || response.code === "200") {
      const avatarUrl = response.url || response.data?.url;
      state.forms.basic.avatar_url = avatarUrl;

      // 更新用户信息到后端
      const updateResponse = await axios.post('/changeUserinfo', {
        avatar_url: avatarUrl
      }, {
        params: {
          now_username: userStore.currentUser.username,
          username: state.forms.basic.username
        }
      });

      if (updateResponse.data === 1) {
        ElMessage.success('头像更新成功');
        // 更新store中的用户信息
        userStore.updateUserInfo({
          ...userStore.currentUser,
          avatar_url: avatarUrl
        });
      } else {
        throw new Error('头像更新失败');
      }
    } else {
      throw new Error(response.message || "头像上传失败");
    }
  } catch (error) {
    console.error('[头像处理失败]', error);
    ElMessage.error(error.message);
    // 回滚头像显示
    state.forms.basic.avatar_url = userStore.currentUser?.avatar_url;
  }
};

const beforeAvatarUpload = (file) => {
  console.log('[头像文件]', file);
  const isImage = file.type.includes("image/");
  const isLt2M = file.size / 1024 / 1024 < 2;

  if (!isImage) {
    ElMessage.error("只能上传图片文件！");
    return false;
  }
  if (!isLt2M) {
    ElMessage.error("图片大小不能超过2MB！");
    return false;
  }
  return true;
};

// 提交基本信息
const submitBasicInfo = async () => {
  try {
    await basicForm.value.validate()
    state.loading.basic = true

    const response = await axios.post('/changeUserinfo',
        // 请求体（User_d对象）
        {
          username: state.forms.basic.nickname || undefined, // 如果要改用户名
          phone: state.forms.basic.phone,
          email: state.forms.basic.email,
          gender: state.forms.basic.gender === 0 ? 'male' :
              state.forms.basic.gender === 1 ? 'female' : 'secret',

        },
        // 配置查询参数
        {
          params: {
            now_username: userStore.currentUser.username,
            username: userStore.currentUser.username // 原始用户名
          }
        }
    );

    if (response.data === 1) {
      ElMessage.success('个人信息更新成功')
      // 创建更新后的用户对象
      const updatedUser = {
        ...userStore.currentUser,
        username: state.forms.basic.nickname,
        phone: state.forms.basic.phone,
        email: state.forms.basic.email,
        gender: state.forms.basic.gender,
        avatar_url: state.forms.basic.avatar_url
      }

      console.log('更新后的用户对象:', updatedUser)

      // 更新store和localStorage
      const updateSuccess = userStore.updateUserInfo(updatedUser)
      if (!updateSuccess) {
        console.error('更新用户信息到store失败')
      }

      console.log('更新后的store状态:', userStore.currentUser)


    }
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '更新失败')
  } finally {
    state.loading.basic = false
  }
}

// 提交密码修改
const submitPassword = async () => {
  try {
    await passwordForm.value.validate()
    state.loading.password = true

    const response = await axios.post('/changeUserinfo', {

      password: state.forms.password.newPassword
    },
    // 配置查询参数
    {
      params: {
        now_username: userStore.currentUser.username,
            username: userStore.currentUser.username // 原始用户名
      }
    }


    )

    if (response.data === 1) {
      ElMessage.success('密码修改成功')
      // 重置密码表单
      Object.assign(state.forms.password, {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      })
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '密码修改失败')
  } finally {
    state.loading.password = false
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

const goToHome = () => {
  router.push('/')
}

// 初始化
onMounted(() => {
  fetchUserInfo()
})
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
.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logout-btn {
  margin-right: 15px;
  color: var(--el-color-danger);
}

.logout-btn:hover {
  color: var(--el-color-danger-light-3);
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