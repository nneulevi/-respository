<template>
  <el-dialog
      v-model="visible"
      :title="form.id ? '编辑动态' : '发布动态'"
      width="800px"
      top="5vh"
  >
    <!-- 调试信息展示 -->
    <div v-if="debugMode" class="debug-info">
      <h4>调试信息（模拟数据模式）</h4>
      <p>当前使用模拟数据，视频路径: {{ form.videoUrl }}</p>
    </div>
    <el-form :model="form" label-width="100px" :rules="rules" ref="formRef">
      <el-form-item label="动态标题" prop="title">
        <el-input v-model="form.title" placeholder="请输入动态标题" />
      </el-form-item>

      <el-form-item label="封面图片" prop="coverImage">
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

      <!-- 新增视频上传 -->
      <el-form-item label="课程视频">
        <el-upload
            class="video-uploader"
            action="/api/upload"
            :show-file-list="false"
            :on-success="handleVideoSuccess"
            :before-upload="beforeVideoUpload"
            accept="video/mp4"
        >
          <el-button type="primary" size="small">上传视频</el-button>
          <span v-if="form.videoUrl" class="video-name">{{ form.videoName }}</span>
        </el-upload>

        <!-- 视频预览 -->
        <div v-if="form.videoUrl" class="video-preview">
          <video controls width="100%" style="max-width: 100%; margin-top: 10px;">
            <source :src="form.videoUrl" type="video/mp4">
            您的浏览器不支持HTML5视频
          </video>
        </div>
      </el-form-item>

      <el-form-item label="动态简介" prop="summary">
        <el-input
            v-model="form.summary"
            type="textarea"
            :rows="3"
            placeholder="请输入动态简介"
            maxlength="200"
            show-word-limit
        />
      </el-form-item>

      <el-form-item label="动态内容" prop="content">
        <rich-text-editor
            v-model="form.content"
            placeholder="请输入动态内容"
        />
      </el-form-item>

      <el-form-item label="动态标签" prop="tags">
        <el-select
            v-model="form.tagIds"
            multiple
            filterable
            allow-create
            default-first-option
            placeholder="请选择或输入标签"
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

      <el-form-item label="审核状态" v-if="isSuperAdmin && form.id">
        <el-radio-group v-model="form.status">
          <el-radio label="pending">待审核</el-radio>
          <el-radio label="approved">已通过</el-radio>
          <el-radio label="rejected">已拒绝</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="submitForm">提交</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import RichTextEditor from '@/components/RichTextEditor.vue'

// ==================== 需要删除的模拟数据部分（接入真实数据后） ====================
const debugMode = ref(true) // 调试模式开关（上线前设为false）
import demo1Video from '@/assets/videos/course/demo1.mp4' // 模拟视频导入

// 模拟数据 - 需要替换为真实API获取的数据
const mockNewsDetail = {
  id: 101,
  title: "2023年人工智能课程演示（模拟数据）",
  summary: "本课程包含人工智能基础知识和实战演示（模拟数据）",
  content: `<p>课程视频演示（模拟数据）：</p>
            <video controls width="100%" style="max-width:600px; margin:20px 0;">
              <source src="${demo1Video}" type="video/mp4">
              您的浏览器不支持HTML5视频
            </video>`,
  coverImage: "https://via.placeholder.com/800x450?text=模拟封面",
  videoUrl: demo1Video,
  videoName: "demo1.mp4",
  status: "approved",
  tags: [
    { id: 1, name: "人工智能" },
    { id: 2, name: "课程" }
  ]
}
// ==================== 模拟数据结束 ====================

const props = defineProps({
  modelValue: Boolean,
  news: Object,    // 接入真实数据后，这里会接收API返回的数据
  allTags: Array,  // 接入真实数据后，这里会接收API返回的标签
  isSuperAdmin: Boolean
})

const emit = defineEmits(['update:modelValue', 'submit'])

const formRef = ref(null)
const visible = ref(props.modelValue)
const form = ref(initForm())

function initForm() {
  return {
    id: null,
    title: '',
    summary: '',
    content: '',
    coverImage: '',
    videoUrl: '', // 接入真实数据后，这里会自动填充API返回的视频URL
    videoName: '',
    tagIds: [],
    status: 'pending'
  }
}

// 表单验证规则（根据实际需求调整）
const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  // ...其他规则
}

// 初始化加载数据
watch(() => props.news, (val) => {
  if (val) {
    // 真实数据传入时的处理
    form.value = {
      ...initForm(),
      ...val,
      tagIds: val.tags?.map(t => t.id) || []
    }
  } else {
    // ==================== 模拟数据加载（接入真实数据后删除else部分） ====================
    form.value = {
      ...initForm(),
      ...mockNewsDetail,
      tagIds: mockNewsDetail.tags.map(t => t.id)
    }
    // ==================== 模拟数据加载结束 ====================
  }
}, { immediate: true })

// 提交表单（需要对接真实API）
const submitForm = async () => {
  try {
    await formRef.value.validate()

    const submitData = {
      ...form.value,
      tags: form.value.tagIds.map(id => ({ id }))
    }

    /*
     * 接入真实数据后替换为：
     * await api.submitNews(submitData)
     * ElMessage.success('提交成功')
     */
    console.log('模拟提交数据:', submitData) // 调试用（上线前删除）
    ElMessage.success('模拟提交成功（实际会调用API）')

    emit('submit', submitData)
    visible.value = false
  } catch (error) {
    ElMessage.warning('请检查表单')
  }
}

// 封面和视频上传处理（需要对接真实API）
const handleCoverSuccess = (response) => {
  /*
   * 接入真实数据后替换为：
   * form.value.coverImage = response.data.url
   */
  form.value.coverImage = "https://via.placeholder.com/800x450?text=模拟封面"
  ElMessage.success('模拟封面上传成功')
}

const handleVideoSuccess = (response, file) => {
  /*
   * 接入真实数据后替换为：
   * form.value.videoUrl = response.data.url
   * form.value.videoName = file.name
   */
  form.value.videoUrl = demo1Video
  form.value.videoName = "demo1.mp4"
  ElMessage.success('模拟视频上传成功')
}
</script>

<style scoped>
.cover-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 200px;
  height: 120px;
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
  width: 200px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}

/* 视频上传样式 */
.video-uploader {
  margin-bottom: 10px;
}

.video-name {
  margin-left: 10px;
  color: #666;
  font-size: 14px;
}

.video-preview {
  margin-top: 10px;
  border: 1px solid #eee;
  padding: 10px;
  border-radius: 4px;
}
.debug-info {
  background: #f0f7ff;
  padding: 10px;
  margin-bottom: 15px;
  border-radius: 4px;
  border-left: 4px solid #409EFF;
}

</style>