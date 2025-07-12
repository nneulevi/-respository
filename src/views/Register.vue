<template>
  <div class="auth-container">
    <el-card class="auth-card">
      <h2>注册</h2>
      <el-form @submit.prevent="handleRegister">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" native-type="submit" :loading="loading">注册</el-button>
          <el-button @click="$router.push('/login')">去登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const router = useRouter()
const form = ref({
  username: '',
  password: '',
  email: ''
})
const loading = ref(false)

const handleRegister = async () => {
  if (!form.value.username || !form.value.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }

  loading.value = true
  try {
    const response = await axios.post('/api/auth/register', form.value)
    ElMessage.success(response.data)
    router.push('/login')
  } catch (error) {
    ElMessage.error(error.response?.data || '注册失败')
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
  background-color: #f5f7fa;
}

.auth-card {
  width: 400px;
  padding: 20px;
}
</style>