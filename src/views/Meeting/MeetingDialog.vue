<template>
  <el-dialog
      v-model="visible"
      :title="form.id ? '编辑会议' : '创建会议'"
      width="800px"
      top="5vh"
  >
    <el-form :model="form" label-width="100px" :rules="rules" ref="formRef">
      <el-form-item label="会议名称" prop="title">
        <el-input v-model="form.title" placeholder="请输入会议名称" />
      </el-form-item>

      <el-form-item label="会议封面" prop="coverImage">
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
            <el-icon class="uploader-icon"><plus /></el-icon>
            <span class="uploader-text">点击上传封面</span>
          </div>
          <template #tip>
            <div class="el-upload__tip">支持JPG/PNG格式，大小不超过5MB</div>
          </template>
        </el-upload>
      </el-form-item>

      <el-form-item label="会议时间" prop="timeRange">
        <el-date-picker
            v-model="form.timeRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>

      <el-form-item label="会议地点" prop="address">
        <el-input v-model="form.address" placeholder="请输入会议地点" />
      </el-form-item>

      <el-form-item label="会议内容" prop="content">
        <rich-text-editor
            v-model="form.content"
            placeholder="请输入会议内容"
        />
      </el-form-item>

      <el-form-item label="会议议程" prop="agendas">
        <div class="agenda-list">
          <div v-for="(agenda, index) in form.agendas" :key="index" class="agenda-item">
            <el-form-item
                :prop="`agendas.${index}.title`"
                :rules="{ required: true, message: '请输入议程标题', trigger: 'blur' }"
            >
              <el-input v-model="agenda.title" placeholder="议程标题" />
            </el-form-item>
            <div class="agenda-details">
              <el-form-item
                  :prop="`agendas.${index}.speaker`"
                  :rules="{ required: true, message: '请输入主讲人', trigger: 'blur' }"
              >
                <el-input v-model="agenda.speaker" placeholder="主讲人" />
              </el-form-item>
              <el-form-item
                  :prop="`agendas.${index}.startTime`"
                  :rules="{ required: true, message: '请选择开始时间', trigger: 'change' }"
              >
                <el-date-picker
                    v-model="agenda.startTime"
                    type="datetime"
                    placeholder="开始时间"
                    value-format="YYYY-MM-DD HH:mm:ss"
                />
              </el-form-item>
              <el-form-item
                  :prop="`agendas.${index}.duration`"
                  :rules="{ required: true, message: '请输入时长', trigger: 'blur' }"
              >
                <el-input-number v-model="agenda.duration" :min="5" :max="240" /> 分钟
              </el-form-item>
            </div>
            <el-form-item label="议程内容">
              <el-input
                  v-model="agenda.content"
                  type="textarea"
                  :rows="2"
                  placeholder="议程内容"
              />
            </el-form-item>
            <el-button
                type="danger"
                size="small"
                circle
                @click="removeAgenda(index)"
            >
              <el-icon><delete /></el-icon>
            </el-button>
          </div>
          <el-button type="primary" plain @click="addAgenda">添加议程</el-button>
        </div>
      </el-form-item>

      <el-form-item label="审核状态" v-if="isAdmin && form.id">
        <el-select v-model="form.status">
          <el-option :value="0" label="草稿" />
          <el-option :value="1" label="待审核" />
          <el-option :value="2" label="已发布" />
        </el-select>
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
import { Plus, Delete } from '@element-plus/icons-vue'
import RichTextEditor from '@/components/RichTextEditor.vue'
import api from '@/api/meeting'

const props = defineProps({
  modelValue: Boolean,
  meeting: Object,
  isAdmin: Boolean
})

const emit = defineEmits(['update:modelValue', 'submit'])

const formRef = ref(null)
const visible = ref(props.modelValue)
const form = ref(initForm())

const rules = {
  title: [
    { required: true, message: '请输入会议名称', trigger: 'blur' },
    { min: 5, max: 100, message: '长度在5到100个字符', trigger: 'blur' }
  ],
  timeRange: [
    { required: true, message: '请选择会议时间', trigger: 'change' }
  ],
  address: [
    { required: true, message: '请输入会议地点', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入会议内容', trigger: 'blur' }
  ]
}

function initForm() {
  return {
    id: null,
    title: '',
    coverImage: '',
    timeRange: [],
    startTime: null,
    endTime: null,
    address: '',
    content: '',
    status: 0,
    agendas: []
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

watch(() => props.meeting, (val) => {
  if (val) {
    form.value = {
      ...val,
      timeRange: [val.startTime, val.endTime],
      agendas: val.agendas || []
    }
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

const handleCoverSuccess = (response) => {
  console.log('[封面响应]', response);

  if (response.code === 200 || response.code === "200") {
    form.value.coverImage = response.url || response.data?.url;
    console.log('[封面URL]', form.value.coverImage);
    ElMessage.success("封面上传成功");
  } else {
    console.error('[封面上传失败]', response);
    ElMessage.error(response.message || "封面上传失败");
  }
};

const beforeCoverUpload = (file) => {
  console.log('[封面文件]', file);
  const isImage = file.type.includes("image/");
  const isLt5M = file.size / 1024 / 1024 < 5;

  if (!isImage) {
    ElMessage.error("只能上传图片文件！");
    return false;
  }
  if (!isLt5M) {
    ElMessage.error("图片大小不能超过5MB！");
    return false;
  }
  return true;
};

const addAgenda = () => {
  form.value.agendas.push({
    title: '',
    speaker: '',
    startTime: null,
    duration: 30,
    content: ''
  })
}

const removeAgenda = (index) => {
  form.value.agendas.splice(index, 1)
}

const submitForm = async () => {
  try {
    await formRef.value.validate()

    // 处理时间范围
    const [startTime, endTime] = form.value.timeRange
    const submitData = {
      ...form.value,
      startTime,
      endTime,
      timeRange: undefined
    }

    // 处理议程数据
    submitData.agendas = submitData.agendas.map(agenda => ({
      ...agenda,
      meeting_id: submitData.id
    }))

    emit('submit', submitData)
    visible.value = false
  } catch (error) {
    ElMessage.warning('请检查表单填写是否正确')
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

.agenda-list {
  border: 1px solid #eee;
  border-radius: 4px;
  padding: 10px;
}

.agenda-item {
  position: relative;
  padding: 10px;
  margin-bottom: 15px;
  border: 1px dashed #ddd;
  border-radius: 4px;
}

.agenda-details {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
  margin: 10px 0;
}

.agenda-item :deep(.el-button) {
  position: absolute;
  top: 5px;
  right: 5px;
}
</style>