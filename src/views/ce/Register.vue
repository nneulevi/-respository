<template>
  <div class="auth-container">
    <el-card class="auth-card">
      <div class="auth-header">
        <h2>{{ registerType === 'enterprise' ? '企业注册' : '员工注册' }}</h2>
        <p>请填写注册信息</p>
      </div>

      <el-form @submit.prevent="handleRegister" :model="form" :rules="rules" ref="registerForm">
        <!-- 企业注册表单 -->
        <template v-if="registerType === 'enterprise'">
          <el-form-item label="企业名称" prop="enterpriseName">
            <el-input v-model="form.enterpriseName" placeholder="请输入企业全称"/>
          </el-form-item>

          <el-form-item label="统一社会信用代码" prop="licenseNumber">
            <el-input v-model="form.licenseNumber" placeholder="请输入营业执照上的统一社会信用代码"/>
          </el-form-item>

          <el-form-item label="联系人" prop="contactPerson">
            <el-input v-model="form.contactPerson" placeholder="请输入企业联系人姓名"/>
          </el-form-item>

          <el-form-item label="联系电话" prop="contactPhone">
            <el-input v-model="form.contactPhone" placeholder="请输入联系人手机号"/>
          </el-form-item>
        </template>

        <!-- 员工注册表单 -->
        <template v-else>
          <el-form-item label="账户名" prop="username">
            <el-input v-model="form.username" placeholder="请输入账户名"/>
          </el-form-item>

          <el-form-item label="手机号" prop="phone">
            <el-input v-model="form.phone" placeholder="请输入您的手机号"/>
          </el-form-item>

          <el-form-item label="密码" prop="password">
            <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password/>
          </el-form-item>

          <el-form-item label="重复密码" prop="confirmPassword">
            <el-input v-model="form.confirmPassword" type="password" placeholder="请再次输入密码"/>
          </el-form-item>

          <el-form-item label="企业ID" prop="enterpriseId">
            <el-input v-model="form.enterpriseId" placeholder="请输入企业ID"/>
          </el-form-item>
        </template>

        <el-form-item>
          <el-button type="primary" native-type="submit" :loading="loading">提交注册</el-button>
          <el-button @click="$router.push('/newlogin')">返回登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import {ref} from 'vue'
import {useRouter, useRoute} from 'vue-router'
import {ElMessage} from 'element-plus'
import axios from 'axios'

const router = useRouter()
const route = useRoute()
const registerType = ref(route.query.type || 'enterprise')

// 表单数据
const form = ref({
  // 企业注册字段
  enterpriseName: '',
  licenseNumber: '',
  contactPerson: '',
  contactPhone: '',

  // 员工注册字段
  username: '',
  phone: '',
  password: '',
  confirmPassword: '',
  enterpriseId: ''
})

// 表单验证规则
const rules = {
  enterpriseName: [
    {required: true, message: '请输入企业名称', trigger: 'blur'},
    {min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur'}
  ],
  licenseNumber: [
    {required: true, message: '请输入统一社会信用代码', trigger: 'blur'},
    {pattern: /^[0-9A-Z]{18}$/, message: '请输入18位数字或大写字母组成的信用代码', trigger: 'blur'}
  ],
  contactPerson: [
    {required: true, message: '请输入联系人姓名', trigger: 'blur'}
  ],
  contactPhone: [
    {required: true, message: '请输入联系电话', trigger: 'blur'},
    {pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur'}
  ],
  username: [
    {required: true, message: '请输入账户名', trigger: 'blur'},
    {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'}
  ],
  phone: [
    {required: true, message: '请输入手机号', trigger: 'blur'},
    {pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur'}
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
    {min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
  ],
  confirmPassword: [
    {required: true, message: '请再次输入密码', trigger: 'blur'},
    {
      validator: (rule, value, callback) => {
        if (value !== form.value.password) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  enterpriseId: [
    {required: true, message: '请输入企业ID', trigger: 'blur'}
  ]
}

const loading = ref(false)
const registerForm = ref(null)

// 处理注册
const handleRegister = async () => {
  try {
    // 验证表单
    await registerForm.value.validate()

    loading.value = true

    if (registerType.value === 'enterprise') {
      // 企业注册
      const response = await axios.post('/Register', {
        enterpriseName: form.value.enterpriseName,
        licenseNumber: form.value.licenseNumber,
        contactPerson: form.value.contactPerson,
        contactPhone: form.value.contactPhone,
        audiStatus: 1 // 审核中
      })

      if (response.status === 200) {
        ElMessage.success('企业注册成功，请等待管理员审核')
        router.push('/newlogin')
      }
    } else {
      // 员工注册
      const response = await axios.post('/adduser', {
        username: form.value.username,
        phone: form.value.phone,
        password: form.value.password, // 注意: 实际项目中密码应该在前端加密或由后端加密
        enterprise: form.value.enterpriseId,
        status: 0 // 假设1表示审核中
      })

      if (response.status === 200) {
        ElMessage.success('员工注册成功，请等待管理员审核')
        router.push('/newlogin')
      }
    }
  } catch (error) {
    if (error.response) {
      const msg = error.response.data.message || '注册失败'
      ElMessage.error(msg)
    } else {
      ElMessage.error('注册失败，请稍后重试')
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
  background-color: #f5f7fa;
}

.auth-card {
  width: 400px;
  padding: 20px;
}
</style>