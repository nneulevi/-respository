<template>
  <div class="meeting-detail">
    <div class="back-button">
      <el-button type="primary" plain @click="goBack">
        <el-icon><ArrowLeft /></el-icon>
        返回首页
      </el-button>
    </div>
    <el-card>
      <template #header>
        <div class="header">
          <h1>{{ meeting.title || '加载中...' }}</h1>
          <div class="meta">
            <el-tag :type="statusType">{{ statusText }}</el-tag>
            <span class="time">{{ formatTime(meeting.startTime) }} 至 {{ formatTime(meeting.endTime) }}</span>
            <span class="creator">创建人: {{ meeting.creator || '匿名' }}</span>
          </div>
        </div>
      </template>

      <div v-if="loading" class="loading-placeholder">
        <el-skeleton :rows="3" animated />
      </div>

      <template v-else>
        <div class="cover-container" v-if="meeting.coverImage">
          <img :src="meeting.coverImage" class="cover">
        </div>

        <div class="join-meeting">
          <el-button type="primary" size="large" @click="joinMeeting" v-if="meeting.meetingLink">
            加入会议
          </el-button>
        </div>

        <div class="address" v-if="meeting.address">
          <el-icon><location /></el-icon>
          {{ meeting.address }}
        </div>

        <div class="content" v-html="meeting.content || '<p>暂无内容</p>'"></div>

        <el-divider />

        <div class="agenda-header">
          <h3>会议议程</h3>
          <el-button
              type="primary"
              size="small"
              @click="showAddAgendaDialog"
              v-if="canAddAgenda"
          >
            添加议程
          </el-button>
        </div>

        <el-timeline v-if="agendas.length">
          <el-timeline-item
              v-for="agenda in agendas"
              :key="agenda.id"
              :timestamp="formatAgendaTime(agenda.startTime, agenda.duration)"
              placement="top"
          >
            <el-card>
              <div class="agenda-item-header">
                <h4>{{ agenda.title }}</h4>
                <div class="agenda-actions" v-if="canEditAgenda(agenda)">
                  <el-button type="text" @click="editAgenda(agenda)">编辑</el-button>
                  <el-button type="text" @click="deleteAgenda(agenda.id)">删除</el-button>
                </div>
              </div>
              <p class="speaker">参会者: {{ agenda.speaker }}</p>
              <p class="duration">发言时长: {{ agenda.duration || 0 }} 分钟</p>
              <div class="agenda-content" v-html="agenda.content || '<p>暂无详细内容</p>'"></div>
            </el-card>
          </el-timeline-item>
        </el-timeline>
        <el-empty v-else description="暂无议程" :image-size="50" />

        <div class="actions" v-if="canEdit">
          <el-button type="primary" @click="editMeeting">编辑会议</el-button>
          <el-button type="danger" @click="deleteMeeting">删除会议</el-button>
        </div>
      </template>
    </el-card>

    <!-- 会议编辑对话框 -->
    <MeetingDialog
        v-model="dialogVisible"
        :meeting="currentMeeting"
        :is-admin="isAdmin"
        @submit="handleSubmit"
    />

    <!-- 议程编辑对话框 -->
    <el-dialog v-model="agendaDialogVisible" :title="currentAgenda.id ? '编辑议程' : '添加议程'">
      <el-form :model="currentAgenda" label-width="80px">
        <el-form-item label="议程标题" required>
          <el-input v-model="currentAgenda.title" placeholder="请输入议程标题" />
        </el-form-item>
        <el-form-item label="参会者" required>
          <el-input v-model="currentAgenda.speaker" placeholder="请输入主讲人姓名" />
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker
              v-model="currentAgenda.startTime"
              type="datetime"
              placeholder="选择开始时间"
              value-format="YYYY-MM-DDTHH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="时长(分钟)">
          <el-input-number
              v-model="currentAgenda.duration"
              :min="0"
              :max="240"
              placeholder="0 表示仅参会不发言"
          />
        </el-form-item>
        <el-form-item label="详细内容">
          <el-input
              v-model="currentAgenda.content"
              type="textarea"
              :rows="4"
              placeholder="请输入议程详细内容"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="agendaDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAgendaSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {ArrowLeft, Location} from '@element-plus/icons-vue'
import MeetingDialog from './MeetingDialog.vue'
import api from '@/api/meeting'
import { useUserStore } from '@/stores/user'


// 在方法部分添加返回函数
const goBack = () => {
  router.push('/')
}
const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

// 响应式数据
const meeting = ref({
  id: '',
  title: '',
  content: '',
  coverImage: '',
  meetingLink: '',
  address: '',
  status: 0,
  startTime: '',
  endTime: '',
  creator: ''
})

const agendas = ref([])
const loading = ref(true)
const dialogVisible = ref(false)
const agendaDialogVisible = ref(false)
const currentMeeting = ref(null)
const currentAgenda = ref({
  id: null,
  meeting_id: null,
  title: '',
  speaker: '',
  startTime: '',
  duration: 0,
  content: ''
})

// 计算属性
const statusText = computed(() => {
  const statusMap = {
    0: '草稿',
    1: '待审核',
    2: '已发布'
  }
  return statusMap[meeting.value.status] || ''
})

const statusType = computed(() => {
  const typeMap = {
    0: 'info',
    1: 'warning',
    2: 'success'
  }
  return typeMap[meeting.value.status] || ''
})

const isAdmin = computed(() => userStore.isSuperAdmin)
const canAddAgenda = computed(() => !!userStore.currentUser)

// 检查用户是否有权限修改/删除议程
const canEditAgenda = (agenda) => {
  const currentUser = userStore.currentUser
  if (!currentUser) return false // 未登录用户无权限

  return (
      isAdmin.value || // 管理员可以操作所有议程
      meeting.value.creator === currentUser.username || // 会议创建者可以操作所有议程
      agenda.speaker === currentUser.username // 议程主讲人可以操作自己的议程
  )
}

// 方法
const formatTime = (timeStr) => {
  if (!timeStr) return ''
  const date = new Date(timeStr)
  return date.toLocaleString()
}

const formatAgendaTime = (startTime, duration) => {
  if (!startTime) return '时间待定'
  const start = new Date(startTime)
  const end = new Date(start.getTime() + duration * 60000)

  return `${start.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })} - ${end.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })} (${duration}分钟)`
}

const joinMeeting = () => {
  if (meeting.value.meetingLink) {
    window.open(meeting.value.meetingLink, '_blank')
  }
}

const fetchMeetingDetail = async () => {
  loading.value = true
  try {
    console.log('开始获取会议详情，会议ID:', route.params.id)

    // 获取会议详情
    const meetingRes = await api.getMeetingDetail(route.params.id)
    console.log('会议详情获取成功:', meetingRes.data)

    // 获取会议议程
    const agendaRes = await api.getAgendasByTime({
      meeting_id: route.params.id,
      page: 1,
      size: 100 // 获取所有议程
    })
    console.log('议程列表获取成功:', agendaRes.data)

    meeting.value = meetingRes.data
    agendas.value = agendaRes.data?.list || []

  } catch (error) {
    console.error('加载失败:', error)
    ElMessage.error('加载会议失败: ' + (error.response?.data?.message || error.message))
  } finally {
    loading.value = false
  }
}

const showAddAgendaDialog = () => {
  currentAgenda.value = {
    id: null,
    meeting_id: meeting.value.id,
    title: '',
    speaker: userStore.currentUser?.username || '',
    startTime: meeting.value.startTime || '',
    duration: 0, // 默认 0 表示不发言
    content: ''
  }
  agendaDialogVisible.value = true
}

const editAgenda = (agenda) => {
  currentAgenda.value = { ...agenda }
  agendaDialogVisible.value = true
}

const handleAgendaSubmit = async () => {
  try {
    console.log('提交议程数据:', currentAgenda.value)

    if (currentAgenda.value.id) {
      // 更新议程
      await api.updateAgenda(currentAgenda.value)
      console.log('议程更新成功')
      ElMessage.success('议程更新成功')
    } else {
      // 添加议程
      await api.addAgenda(currentAgenda.value)
      console.log('议程添加成功')
      ElMessage.success('议程添加成功')
    }

    await fetchMeetingDetail() // 刷新数据
    agendaDialogVisible.value = false
  } catch (error) {
    console.error('保存议程失败:', error)
    ElMessage.error('保存议程失败: ' + (error.response?.data?.message || error.message))
  }
}

const deleteAgenda = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该议程吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    console.log('开始删除议程，ID:', id)
    await api.deleteAgenda(id)
    console.log('议程删除成功')

    ElMessage.success('议程删除成功')
    await fetchMeetingDetail() // 刷新数据
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除议程失败:', error)
      ElMessage.error('删除议程失败: ' + (error.response?.data?.message || error.message))
    }
  }
}

const editMeeting = () => {
  currentMeeting.value = { ...meeting.value }
  dialogVisible.value = true
}

const handleSubmit = async (meetingData) => {
  try {
    console.log('提交会议数据:', meetingData)

    if (meetingData.id) {
      await api.updateMeeting(meetingData.id, {
        title: meetingData.title,
        address: meetingData.address,
        startTime: meetingData.startTime,
        endTime: meetingData.endTime,
        content: meetingData.content,
        coverImage: meetingData.coverImage,
        creator: userStore.currentUser?.username
      })
      console.log('会议更新成功')

      ElMessage.success('会议更新成功')
      await fetchMeetingDetail() // 刷新数据
    }
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error('保存失败: ' + (error.response?.data?.message || error.message))
  } finally {
    dialogVisible.value = false
  }
}

const deleteMeeting = async () => {
  try {
    await ElMessageBox.confirm('确定删除该会议吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    console.log('开始删除会议，ID:', meeting.value.id)
    await api.deleteMeeting(meeting.value.id, userStore.currentUser?.username)
    console.log('会议删除成功')

    ElMessage.success('会议删除成功')
    router.push('/meetings')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败: ' + (error.response?.data?.message || error.message))
    }
  }
}

onMounted(() => {
  fetchMeetingDetail()
})
</script>

<style scoped>
.meeting-detail {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
}

.header {
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.header h1 {
  margin: 0 0 10px 0;
  font-size: 24px;
}

.meta {
  display: flex;
  align-items: center;
  gap: 15px;
  color: #666;
  font-size: 14px;
}

.cover-container {
  margin: 20px 0;
}

.cover {
  width: 100%;
  max-height: 400px;
  object-fit: contain;
  border-radius: 4px;
}

.join-meeting {
  margin: 20px 0;
  text-align: center;
}

.address {
  display: flex;
  align-items: center;
  gap: 5px;
  margin: 10px 0;
  color: #666;
  font-size: 16px;
}

.content {
  line-height: 1.8;
  font-size: 16px;
  color: #333;
  margin: 20px 0;
}

.content :deep(img) {
  max-width: 100%;
  height: auto;
}

.content :deep(p) {
  margin: 0 0 15px 0;
}

.agenda-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.agenda-item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.agenda-actions {
  margin-left: 10px;
}

.speaker, .duration {
  margin: 5px 0;
  color: #666;
  font-size: 14px;
}

.agenda-content {
  margin: 10px 0 0;
  color: #333;
}

.agenda-content :deep(p) {
  margin: 5px 0;
}

.actions {
  margin-top: 20px;
  text-align: right;
}

.loading-placeholder {
  padding: 20px;
}

@media (max-width: 768px) {
  .header h1 {
    font-size: 20px;
  }

  .meta {
    flex-wrap: wrap;
    gap: 8px;
  }

  .agenda-item-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .agenda-actions {
    margin-left: 0;
    margin-top: 10px;
  }
}
</style>