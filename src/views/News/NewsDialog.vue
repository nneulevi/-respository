<template>
  <el-dialog
      v-model="dialogVisible"
      :title="form.id ? '编辑动态' : '发布动态'"
      width="800px"
      top="5vh"
      destroy-on-close
  >
    <el-form :model="form" label-width="100px" ref="formRef">
      <el-form-item label="动态标题" prop="title" required>
        <el-input v-model="form.title" placeholder="请输入动态标题" />
      </el-form-item>

      <el-form-item label="封面图片" prop="coverImage">
        <el-upload
            class="cover-uploader"
            action="/api/upload"
            :show-file-list="false"
            :on-success="handleCoverSuccess"
            :before-upload="beforeCoverUpload"
            accept="image/*"
        >
          <img v-if="form.coverImage" :src="form.coverImage" class="cover-image">
          <div v-else class="uploader-area">
            <el-icon class="uploader-icon"><Plus /></el-icon>
            <span class="uploader-text">点击上传封面</span>
          </div>
          <template #tip>
            <div class="el-upload__tip">支持JPG/PNG格式，大小不超过5MB</div>
          </template>
        </el-upload>
      </el-form-item>

      <el-form-item label="动态简介" prop="summary" required>
        <el-input
            v-model="form.summary"
            type="textarea"
            :rows="3"
            placeholder="请输入动态简介"
            maxlength="200"
            show-word-limit
        />
      </el-form-item>

      <el-form-item label="动态内容" prop="content" required>
        <RichTextEditor v-model="form.content" />
      </el-form-item>

      <el-form-item label="动态标签">
        <el-select
            v-model="form.tagIds"
            multiple
            filterable
            placeholder="请选择标签"
            style="width: 100%"
        >
          <el-option
              v-for="tag in allTags"
              :key="tag.id"
              :label="tag.name"
              :value="tag.id"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="审核状态" v-if="isSuperAdmin">
        <el-radio-group v-model="form.status">
          <el-radio label="pending">待审核</el-radio>
          <el-radio label="approved">已通过</el-radio>
          <el-radio label="rejected">已拒绝</el-radio>
        </el-radio-group>
      </el-form-item>
      <!-- 普通用户不显示状态选择 -->
      <el-form-item v-else>
        <el-tag type="info">提交后需管理员审核</el-tag>
      </el-form-item>
    </el-form>


    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="handleSubmit">提交</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import RichTextEditor from '@/components/RichTextEditor.vue'

const props = defineProps({
  modelValue: Boolean,
  news: Object,
  allTags: Array,
  isSuperAdmin: Boolean,
  currentUser: Object  // 添加这个prop
})

const emit = defineEmits(['update:modelValue', 'submit'])

const form = ref({
  id: null,
  title: '',
  summary: '',
  content: '',
  coverImage: '',
  tagIds: [],
  status: 'pending',
  author: ''
})

const formRef = ref(null)

const resetForm = () => {
  form.value = {
    id: null,
    title: '',
    summary: '',
    content: '',
    coverImage: '',
    tagIds: [],
    status: 'pending',
    author: ''
  }
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

const dialogVisible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

watch(() => props.news, (val) => {
  if (val) {
    // 编辑现有新闻
    form.value = {
      ...val,
      tagIds: val.tags?.map(t => t.id) || [],
      status: val.status || (props.isSuperAdmin ? 'approved' : 'pending'),
      author: val.author?.name || val.author || props.currentUser?.username
    }
  } else {
    // 新增新闻
    resetForm()
    form.value.status = props.isSuperAdmin ? 'approved' : 'pending' // 管理员默认直接发布，企业用户需审核
    form.value.author = props.currentUser?.username // 自动填充当前用户
  }
}, { immediate: true })

const handleCoverSuccess = (response) => {
  console.log('[封面响应]', response) // 打印完整响应

  if (response.code === 200 || response.code === "200") {
    form.value.coverImage = response.url || response.data?.url
    console.log('[封面URL]', form.value.coverImage)

    // 强制视图更新
    form.value = { ...form.value }
    console.log('[封面更新后form]', JSON.parse(JSON.stringify(form.value)))

    ElMessage.success("封面上传成功")
  } else {
    console.error('[封面上传失败]', response)
    ElMessage.error(response.message || "封面上传失败")
  }
}

const beforeCoverUpload = (file) => {
  console.log('[封面文件]', file)
  const isImage = file.type.includes("image/")
  const isLt5M = file.size / 1024 / 1024 < 5

  if (!isImage) {
    ElMessage.error("只能上传图片文件！")
    return false
  }
  if (!isLt5M) {
    ElMessage.error("图片大小不能超过5MB！")
    return false
  }
  return true
}

const handleSubmit = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return

    const newsData = {
      ...form.value,
      // 确保作者信息
      author: form.value.author || props.currentUser?.username,
      // 根据身份处理状态
      status: props.isSuperAdmin
          ? form.value.status // 管理员可自由设置状态
          : 'pending' // 企业用户只能提交待审核
    }

    emit('submit', newsData)
    dialogVisible.value = false
  })
}
</script>
<style scoped>

.cover-uploader {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 200px;
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: border-color 0.3s;
}

.cover-uploader:hover {
  border-color: var(--el-color-primary);
}

.uploader-area {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  color: var(--el-text-color-secondary);
}


.cover-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.cover-uploader:hover {
  border-color: #409eff;
}


.uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>