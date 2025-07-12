<template>
  <el-dialog
      v-model="visible"
      :title="form.id ? '编辑作家' : '添加作家'"
      width="600px"
  >
    <el-form
        :model="form"
        label-width="100px"
        :rules="rules"
        ref="formRef"
    >
      <el-form-item label="姓名" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>

      <el-form-item label="头像URL" prop="avatarUrl">
        <el-input v-model="form.avatarUrl" />
        <div v-if="form.avatarUrl" class="avatar-preview">
          <el-avatar :size="100" :src="form.avatarUrl" />
        </div>
        <el-button
            v-else
            type="primary"
            size="small"
            @click="form.avatarUrl = '/src/assets/images/writer/default.jpg'"
        >
          使用默认头像
        </el-button>
      </el-form-item>

      <el-form-item label="性别" prop="gender">
        <el-radio-group v-model="form.gender">
          <el-radio :value="1">男</el-radio>
          <el-radio :value="0">女</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="国家" prop="country">
        <el-input v-model="form.country" />
      </el-form-item>

      <el-form-item label="出生年份" prop="birthYear">
        <el-input-number
            v-model="form.birthYear"
            :min="1900"
            :max="new Date().getFullYear()"
        />
      </el-form-item>

      <el-form-item label="代表作" prop="representativeWork">
        <el-input v-model="form.representativeWork" />
      </el-form-item>

      <el-form-item label="简介" prop="description">
        <el-input type="textarea" v-model="form.description" :rows="4" />
      </el-form-item>

      <el-form-item label="百科链接" prop="externalUrl">
        <el-input v-model="form.externalUrl" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="submitForm">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  modelValue: Boolean,
  writer: Object
})

const emit = defineEmits(['update:modelValue', 'submit'])

const formRef = ref(null)
const visible = ref(props.modelValue)
const form = ref(initForm())

const rules = {
  name: [
    { required: true, message: '请输入作家姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在2到20个字符', trigger: 'blur' }
  ],
  country: [
    { required: true, message: '请输入国家', trigger: 'blur' }
  ],
  birthYear: [
    { required: true, message: '请选择出生年份', trigger: 'blur' }
  ]
}

function initForm() {
  return {
    id: null,
    name: '',
    avatarUrl: '',
    gender: 1,
    country: '中国',
    birthYear: null,
    representativeWork: '',
    description: '',
    externalUrl: ''
  }
}

const resetForm = () => {
  form.value = initForm()
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

watch(() => props.modelValue, (val) => {
  visible.value = val
})

watch(() => props.writer, (val) => {
  if (val) {
    form.value = { ...val }
  } else {
    resetForm()
  }
}, { immediate: true })

watch(visible, (val) => {
  emit('update:modelValue', val)
  if (!val) {
    resetForm()
  }
})

const submitForm = async () => {
  try {
    await formRef.value.validate()

    console.log("[前端] 提交表单数据:", JSON.stringify(form.value, null, 2))

    // 清理不需要的字段
    const submitData = {
      ...form.value,
      bookCount: undefined,
      popularBooks: undefined,
      age: undefined,
      genderText: undefined
    }

    emit('submit', submitData)
    visible.value = false
  } catch (error) {
    console.error("[前端] 表单验证失败:", error)
    ElMessage.warning('请检查表单填写是否正确')
  }
}
</script>

<style scoped>
.avatar-preview {
  margin-top: 10px;
  text-align: center;
}
</style>