<template>
  <div class="auth-container">
    <el-card class="auth-card" shadow="hover">
      <div class="auth-header">
        <h2>欢迎登录青棠小说</h2>
        <p>请输入您的账号信息</p>
      </div>

      <el-form
          @submit.prevent="handleLogin"
          :model="form"
          :rules="rules"
          ref="loginForm"
      >
        <el-form-item prop="username">
          <el-input
              v-model="form.username"
              placeholder="请输入用户名"
              prefix-icon="User"
              size="large"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              show-password
              prefix-icon="Lock"
              size="large"
          />
        </el-form-item>

        <el-form-item>
          <el-button
              type="primary"
              native-type="submit"
              :loading="loading"
              size="large"
              round
              class="login-btn"
          >
            登录
          </el-button>
          <div class="auth-footer">
            <el-link type="info" @click="$router.push('/register')">还没有账号？立即注册</el-link>
          </div>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios' // 需要导入axios

const router = useRouter()

// 表单数据
const form = ref({
  username: '',
  password: ''
})

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 16, message: '长度在 3 到 16 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ]
}

const loading = ref(false)
const loginForm = ref(null)

const handleLogin = async () => {
  try {
    // 验证表单
    await loginForm.value.validate()

    loading.value = true

    const response = await axios.post('/api/auth/login', form.value, {
      withCredentials: true
    })

    ElMessage.success('登录成功')
    router.push('/')
  } catch (err) {
    if (err.response) {
      const msg = err.response.data?.message || '登录失败'
      ElMessage.error(msg)
    } else if (!err.message.includes('validate')) {
      ElMessage.error('网络错误，请稍后重试')
    }
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.auth-card {
  width: 420px;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.auth-header {
  text-align: center;
  margin-bottom: 30px;
}

.auth-header h2 {
  color: #333;
  margin-bottom: 8px;
  font-weight: 600;
}

.auth-header p {
  color: #888;
  font-size: 14px;
}

:deep(.el-form-item) {
  margin-bottom: 24px;
}

.login-btn {
  width: 100%;
  margin-top: 10px;
}

.auth-footer {
  margin-top: 16px;
  text-align: center;
  width: 100%;
}

.el-link {
  font-size: 14px;
}
</style>