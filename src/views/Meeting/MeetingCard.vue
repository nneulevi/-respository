<template>
  <div class="meeting-card" @click="goToDetail">
    <div class="cover-container">
      <img :src="meeting.coverImage || '/src/assets/images/book-covers/guicui.webp'" class="cover">
      <div class="status" :class="statusClass">
        {{ statusText }}
      </div>
      <div class="time">
        {{ formatDate(meeting.startTime) }}
      </div>
    </div>
    <div class="content">
      <h3 class="title">{{ meeting.title }}</h3>
      <p class="address">{{ meeting.address }}</p>
      <div class="meta">
        <span class="creator">{{ meeting.creator || '匿名' }}</span>
        <span class="duration">{{ formatDuration(meeting.startTime, meeting.endTime) }}</span>
      </div>
    </div>
    <div class="actions" v-if="showActions">
      <el-button size="small" @click.stop="editMeeting">编辑</el-button>
      <el-button size="small" type="danger" @click.stop="deleteMeeting">删除</el-button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { computed } from 'vue'
import api from '@/api/meeting'

const props = defineProps({
  meeting: {
    type: Object,
    required: true
  },
  showActions: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['edit', 'delete'])

const $router = useRouter()

const statusText = computed(() => {
  const statusMap = {
    0: '草稿',
    1: '待审核',
    2: '已发布'
  }
  return statusMap[props.meeting.status] || ''
})

const statusClass = computed(() => {
  const classMap = {
    0: 'draft',
    1: 'pending',
    2: 'published'
  }
  return classMap[props.meeting.status] || ''
})

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString()
}

const formatDuration = (start, end) => {
  if (!start || !end) return ''
  const startTime = new Date(start).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
  const endTime = new Date(end).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
  return `${startTime} - ${endTime}`
}

const goToDetail = () => {
  if (props.meeting.status === 2) { // 只有已发布的会议可以查看详情
    $router.push(`/meetings/${props.meeting.id}`)
  }
}

const editMeeting = () => {
  emit('edit', props.meeting)
}

const deleteMeeting = async () => {
  try {
    await ElMessageBox.confirm('确定删除该会议吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await api.deleteMeeting(props.meeting.id, props.meeting.creator)
    emit('delete', props.meeting.id)
    ElMessage.success('删除成功')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败: ' + (error.response?.data?.message || error.message))
    }
  }
}
</script>
<style scoped>
.meeting-card {
  display: flex;
  flex-direction: column;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s;
  cursor: pointer;
  height: 100%;
}

.meeting-card:hover {
  transform: translateY(-5px);
}

.cover-container {
  position: relative;
  width: 100%;
  height: 160px;
}

.cover {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.status {
  position: absolute;
  top: 10px;
  left: 10px;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  color: white;
}

.status.draft {
  background-color: #909399;
}

.status.pending {
  background-color: #e6a23c;
}

.status.published {
  background-color: #67c23a;
}

.time {
  position: absolute;
  bottom: 10px;
  left: 10px;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.content {
  padding: 15px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.title {
  margin: 0 0 10px 0;
  font-size: 16px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.address {
  margin: 0 0 10px 0;
  color: #666;
  font-size: 14px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.meta {
  margin-top: auto;
  font-size: 12px;
  color: #999;
  display: flex;
  justify-content: space-between;
}

.actions {
  padding: 10px;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: flex-end;
}
</style>