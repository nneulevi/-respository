<template>
  <div class="auth-container">
    <el-card class="auth-card" shadow="hover">
      <div class="auth-header">
        <h2>系统登录</h2>
        <p>请选择登录身份</p>
      </div>

      <el-tabs v-model="activeTab" stretch>
        <el-tab-pane label="企业用户" name="enterprise">
          <el-form @submit.prevent="handleLogin" :model="form" :rules="rules" ref="loginForm">
            <el-form-item prop="username">
              <el-input v-model="form.username" placeholder="请输入企业账号" prefix-icon="User" size="large"/>
            </el-form-item>

            <el-form-item prop="password">
              <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password
                        prefix-icon="Lock" size="large"/>
            </el-form-item>
            <el-form-item prop="enterprise">
              <el-input
                  v-model="form.enterprise"
                  placeholder="请输入企业ID"
                  prefix-icon="OfficeBuilding"
                  size="large"
              />
            </el-form-item>
            <el-form-item prop="code" v-if="showCode">
              <el-input v-model="form.code" placeholder="请输入验证码" size="large">
                <template #append>
                  <el-image :src="captchaUrl" @click="refreshCaptcha" style="cursor: pointer;height: 40px"/>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" native-type="submit" :loading="loading" size="large" round>
                登录
              </el-button>
              <div class="auth-footer">
                <el-link type="info" @click="$router.push('/newregister?type=enterprise')">企业注册</el-link>
                <el-divider direction="vertical"/>
                <el-link type="info" @click="$router.push('/newregister?type=employee')">员工注册</el-link>
              </div>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="超级管理员" name="admin">
          <el-form @submit.prevent="handleAdminLogin" :model="adminForm" :rules="adminRules" ref="adminFormRef">
            <el-form-item prop="username">
              <el-input
                  v-model="adminForm.username"
                  placeholder="请输入管理员账号"
                  prefix-icon="User"
                  size="large"
              />
            </el-form-item>

            <el-form-item prop="password">
              <el-input
                  v-model="adminForm.password"
                  type="password"
                  placeholder="请输入密码"
                  show-password
                  prefix-icon="Lock"
                  size="large"
              />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" native-type="submit" :loading="adminLoading" size="large" round>
                登录
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>


<script setup>
import {ref} from 'vue'
import {useRouter} from 'vue-router'
import {ElMessage} from 'element-plus'
import axios from 'axios'
import { watch } from 'vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const router = useRouter()
const activeTab = ref('enterprise')
// 验证码相关
const captchaUrl = ref('/api/auth/captcha?t=' + Date.now())
const showCode = ref(false)
const loading = ref(false)
const adminLoading = ref(false)
const loginForm = ref(null)
const adminFormRef = ref(null)
// 模拟用户数据库
const mockUserDatabase = {
  enterprise: [
    {
      username: 'company1',
      password: '123456',
      code: '1234', // 模拟验证码
      name: '测试企业1',
      token: 'mock-enterprise-token-1'
    },
    {
      username: 'company2',
      password: '123456',
      code: '5678',
      name: '测试企业2',
      token: 'mock-enterprise-token-2'
    }
  ],
  admin: [
    {
      username: 'admin',
      password: 'admin123',
      token: 'mock-admin-token-1'
    }
  ]
}

// 当前有效的验证码（模拟）
let currentValidCode = '1234'

// 企业用户表单
const form = ref({
  username: '',
  password: '',
  enterprise: '',
  code: ''
})

// 管理员表单
const adminForm = ref({
  username: '',
  password: ''
})

watch(adminForm, (newVal) => {
  console.log('adminForm变化:', newVal)
}, { deep: true })
// 表单验证规则
const rules = {
  username: [
    {required: true, message: '请输入用户名', trigger: 'blur'},
    {min: 3, max: 16, message: '长度在 3 到 16 个字符', trigger: 'blur'}
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
    {min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
  ],
  enterprise: [
    { required: true, message: '请输入企业ID', trigger: 'blur' },
    { min: 1, max: 20, message: '企业ID长度在1到20个字符', trigger: 'blur' }
  ],
  code: [
    {required: true, message: '请输入验证码', trigger: 'blur'},
    {len: 4, message: '验证码长度为4位', trigger: 'blur'}
  ]
}

const adminRules = {
  username: [
    {required: true, message: '请输入管理员账号', trigger: 'blur'}
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'}
  ]
}



// 刷新验证码
const refreshCaptcha = () => {
  // 模拟生成新的验证码
  currentValidCode = Math.floor(1000 + Math.random() * 9000).toString()
  captchaUrl.value = '/api/auth/captcha?t=' + Date.now()
  console.log('模拟验证码已更新:', currentValidCode) // 实际开发中删除这行
}

// 模拟API请求
const mockApiRequest = (type, credentials) => {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      const users = mockUserDatabase[type]
      const user = users.find(u =>
          u.username === credentials.username &&
          u.password === credentials.password
      )

      if (type === 'enterprise' && credentials.code !== currentValidCode) {
        return reject({response: {data: {message: '验证码错误'}}})
      }

      if (user) {
        resolve({
          data: {
            token: user.token,
            userInfo: {
              username: user.username,
              name: user.name || '管理员'
            }
          }
        })
      } else {
        reject({response: {data: {message: '用户名或密码错误'}}})
      }
    }, 500)
  })
}
const handleLogin = async () => {
  try {
    await loginForm.value.validate()
    loading.value = true

    console.log('发送登录请求数据:', {
      username: form.value.username,
      password: form.value.password,
      status: 0, // 企业用户
      enterprise: form.value.enterprise // 新增企业ID
    })

    const response = await axios.post('/login', {
      username: form.value.username,
      password: form.value.password,
      status: 0, // 企业用户
      enterprise: form.value.enterprise // 必须包含企业ID
    }, {
      headers: {
        'Content-Type': 'application/json'
      }
    })

    console.log('登录响应:', response.data)

    if (response.data.success) {
      localStorage.setItem('token', response.data.token)
      localStorage.setItem('userInfo', JSON.stringify(response.data.userInfo))
      console.log('登录成功，存储的userInfo:', response.data.userInfo)

      // 确保初始化用户信息
      userStore.initUser()

      // 调试检查用户状态
      console.log('当前用户状态:', {
        currentUser: userStore.currentUser,
        isSuperAdmin: userStore.isSuperAdmin
      })

      ElMessage.success('登录成功')
      router.push('/')
    } else {
      ElMessage.error(response.data.message || '登录失败')
    }
  } catch (err) {
    console.error('完整错误对象:', err)
    console.error('错误响应数据:', err.response?.data)

    if (err.response) {
      // 400 错误的具体信息
      const errorMsg = err.response.data?.message ||
          err.response.data?.error ||
          '请求参数错误'
      ElMessage.error(`登录失败: ${errorMsg}`)
    } else {
      ElMessage.error('网络错误，请稍后重试')
    }
  } finally {
    loading.value = false
  }
}

const handleAdminLogin = async () => {
  try {
    console.log('开始管理员登录验证')
    await adminFormRef.value.validate()
    console.log('管理员表单验证通过', adminForm.value)

    adminLoading.value = true

    console.log('发送管理员登录请求...')
    const response = await axios.post('http://localhost:8080/login', {
      username: adminForm.value.username,
      password: adminForm.value.password,
      status: 1 // 1表示管理员
    }).catch(error => {
      console.error('管理员登录请求错误:', error)
      throw error
    })

    console.log('管理员登录响应:', response.data)

    if (response.data.success) {
      localStorage.setItem('token', response.data.token)
      localStorage.setItem('userInfo', JSON.stringify(response.data.userInfo))
      console.log('管理员登录成功，存储的userInfo:', response.data.userInfo)

      userStore.initUser()

      console.log('当前管理员状态:', {
        currentUser: userStore.currentUser,
        isSuperAdmin: userStore.isSuperAdmin
      })

      ElMessage.success('管理员登录成功')
      router.push('/')
    } else {
      ElMessage.error(response.data.message || '管理员登录失败')
    }
  } catch (err) {
    console.error('管理员登录过程出错:', err)
    if (err.response) {
      const msg = err.response.data?.message || '登录失败'
      ElMessage.error(msg)
    } else if (!err.message.includes('validate')) {
      ElMessage.error('网络错误，请稍后重试')
    }
  } finally {
    adminLoading.value = false
  }
}
// 初始化时生成验证码
refreshCaptcha()
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