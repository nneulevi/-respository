<template>
  <el-dialog
      v-model="visible"
      :title="form.id ? '编辑课程' : '添加课程'"
      width="700px"
  >
    <el-form
        :model="form"
        label-width="100px"
        :rules="rules"
        ref="formRef"
    >
      <el-form-item label="课程名称" prop="title">
        <el-input v-model="form.title" placeholder="请输入课程名称" />
      </el-form-item>

      <el-form-item label="课程封面" prop="coverImage">
        <el-upload
            class="cover-uploader"
            action="/api/upload"
            :show-file-list="false"
            :on-success="handleCoverSuccess"
            :before-upload="beforeCoverUpload"
        >
          <img v-if="form.coverImage" :src="form.coverImage" class="cover-image">
          <el-icon v-else class="cover-uploader-icon"><plus /></el-icon>
        </el-upload>
      </el-form-item>

      <el-form-item label="课程视频" prop="videoUrl">
        <el-upload
            class="video-uploader"
            action="/api/upload"
            :show-file-list="false"
            :on-success="handleVideoSuccess"
            :before-upload="beforeVideoUpload"
        >
          <el-button type="primary">上传视频</el-button>
          <template #tip>
            <div class="el-upload__tip">支持MP4格式，大小不超过500MB</div>
          </template>
        </el-upload>
        <div v-if="form.videoUrl" class="video-preview">
          <video :src="form.videoUrl" controls width="100%"></video>
        </div>
      </el-form-item>

      <el-form-item label="课程作者" prop="author">
        <el-input v-model="form.author" placeholder="请输入课程作者" />
      </el-form-item>

      <el-form-item label="课程排序" prop="sortOrder">
        <el-input-number v-model="form.sortOrder" :min="0" />
      </el-form-item>

      <el-form-item label="课程分类" prop="categories">
        <el-select
            v-model="form.categoryIds"
            multiple
            placeholder="请选择分类"
            style="width: 100%"
        >
          <el-option
              v-for="category in categories"
              :key="category.id"
              :label="category.name"
              :value="category.id"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="课程简介" prop="content">
        <el-input
            v-model="form.content"
            type="textarea"
            :rows="4"
            placeholder="请输入课程简介"
        />
      </el-form-item>

      <el-form-item label="审核状态" v-if="isSuperAdmin && form.id">
        <el-select v-model="form.status" placeholder="请选择状态">
          <el-option label="待审核" value="0" />
          <el-option label="已通过" value="1" />
          <el-option label="已拒绝" value="2" />
        </el-select>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="submitForm">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch, computed } from 'vue'
import { ElMessage } from 'element-plus'

// 1. 定义所有需要的属性和方法
const props = defineProps({
  modelValue: Boolean,
  course: Object,
  categories: Array,
  isSuperAdmin: Boolean
})

const emit = defineEmits(['update:modelValue', 'submit'])

// 2. 正确定义所有变量
const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

const formRef = ref(null)
const rules = ref({
  // 简单示例规则，可根据需要扩展
  title: [{ required: true, message: '请输入课程名称', trigger: 'blur' }]
})

const form = ref({
  id: null,
  title: '',
  videoUrl: '',
  coverImage: '',
  duration: 0,
  author: '',
  summary: '',
  content: '',
  status: 'pending',
  categoryIds: [],
  sortOrder: 0
})

// 3. 封面上传逻辑（带调试信息）
const handleCoverSuccess = (response) => {
  console.log('[封面响应]', response) // 打印完整响应

  if (response.code === 200 || response.code === "200") {
    form.value.coverImage = response.url
    console.log('[封面URL]', form.value.coverImage) // 打印设置后的URL

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
  if (!isImage) ElMessage.error("只能上传图片文件！")
  if (!isLt5M) ElMessage.error("图片大小不能超过5MB！")
  return isImage && isLt5M
}

// 4. 视频上传逻辑（带调试信息）
const handleVideoSuccess = (response) => {
  console.log('[视频响应]', response) // 打印完整响应

  if (response.code === 200 || response.code === "200") {
    form.value.videoUrl = response.url
    console.log('[视频URL]', form.value.videoUrl) // 打印设置后的URL

    // 强制视图更新
    form.value = { ...form.value }
    console.log('[视频更新后form]', JSON.parse(JSON.stringify(form.value)))

    ElMessage.success("视频上传成功")
  } else {
    console.error('[视频上传失败]', response)
    ElMessage.error(response.message || "视频上传失败")
  }
}

const beforeVideoUpload = (file) => {
  console.log('[视频文件]', file)
  const isVideo = file.type.includes("video/")
  const isLt500M = file.size / 1024 / 1024 < 500
  if (!isVideo) ElMessage.error("只能上传视频文件！")
  if (!isLt500M) ElMessage.error("视频大小不能超过500MB！")
  return isVideo && isLt500M
}

// 5. 监听课程数据变化
watch(() => props.course, (newCourse) => {
  if (newCourse) {
    console.log('[接收到课程数据]', newCourse)
    form.value = {
      ...newCourse,
      status: newCourse.status || 0, // 直接使用数字
      categoryIds: newCourse.categories?.map(c => c.id) || []
    }
    console.log('[初始化后的form]', JSON.parse(JSON.stringify(form.value)))
  }
}, { immediate: true, deep: true })

// 6. 提交表单（增强调试版）
const submitForm = async () => {
  try {
    console.group('=== CourseDialog 提交流程 ===')
    await formRef.value.validate()

    // 准备提交数据（确保包含所有必要字段）
    const submitData = {
      id: form.value.id,
      title: form.value.title,
      videoUrl: form.value.videoUrl,
      coverImage: form.value.coverImage,
      duration: form.value.duration,
      author: form.value.author,
      summary: form.value.summary,
      content: form.value.content,
      status: form.value.status,
      categoryIds: form.value.categoryIds, // 保持原始ID数组
      sortOrder: form.value.sortOrder
    }

    console.log('📤 Dialog 提交数据:', JSON.parse(JSON.stringify(submitData)))
    console.groupEnd()

    emit('submit', submitData)
    visible.value = false
  } catch (error) {
    console.error('❌ Dialog 验证失败:', error)
    ElMessage.error('请检查表单内容')
  }
}
</script>

<style scoped>
.cover-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 150px;
  height: 150px;
}

.cover-uploader:hover {
  border-color: #409EFF;
}

.cover-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.cover-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 150px;
  height: 150px;
  line-height: 150px;
  text-align: center;
}

.video-preview {
  margin-top: 10px;
  border: 1px solid #eee;
  padding: 5px;
  border-radius: 4px;
}
</style>