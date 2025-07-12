<template>
  <div class="meeting-list">
    <div class="header">
      <h2>会议管理</h2>
      <div class="actions">
        <el-input
            v-model="searchQuery"
            placeholder="搜索会议"
            clearable
            style="width: 300px"
            @keyup.enter="fetchMeetings"
        >
          <template #prefix>
            <el-icon><search /></el-icon>
          </template>
        </el-input>
      </div>
    </div>

    <div class="filter-bar">
      <el-select
          v-model="filterStatus"
          placeholder="全部状态"
          clearable
          style="width: 120px"
      >
        <el-option label="草稿" :value="0" />
        <el-option label="待审核" :value="1" />
        <el-option label="已发布" :value="2" />
      </el-select>
      <el-date-picker
          v-model="filterDate"
          type="date"
          placeholder="选择日期"
          style="width: 150px; margin-left: 10px"
      />
      <el-button
          type="primary"
          style="margin-left: 10px"
          @click="fetchMeetings"
      >
        筛选
      </el-button>
    </div>

    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="6" animated />
    </div>
    <template v-else>
      <div v-if="meetings.length" class="list-container">
        <div class="grid-view">
          <div
              v-for="meeting in meetings"
              :key="meeting.id"
              class="meeting-card"
          >
            <div class="card-media" @click="goToMeetingDetail(meeting.id)">
              <img :src="meeting.coverImage || '/src/assets/images/meeting/default.jpg'" class="cover">
              <div class="status-badge" :class="getStatusClass(meeting.status)">
                {{ getStatusText(meeting.status) }}
              </div>
              <div class="time-info">
                <span>{{ formatTime(meeting.startTime) }}</span>
                <span v-if="meeting.endTime"> - {{ formatTime(meeting.endTime) }}</span>
              </div>
            </div>
            <div class="card-content" @click="goToMeetingDetail(meeting.id)">
              <h3>{{ meeting.title }}</h3>
              <p class="address">
                <el-icon><location /></el-icon>
                <span>{{ meeting.address || '线上会议' }}</span>
              </p>
              <div class="participants">
                <el-icon><user /></el-icon>
                <span>{{ meeting.participants || 0 }}人参加</span>
              </div>
              <p class="content">{{ truncateContent(meeting.content) }}</p>
            </div>
            <div class="card-footer">
              <el-button type="primary" size="small" @click.stop="editMeeting(meeting)">编辑</el-button>
              <el-button type="danger" size="small" @click.stop="deleteMeeting(meeting.id)">删除</el-button>
            </div>
          </div>
        </div>
        <div class="pagination">
          <el-pagination
              v-model:current-page="pagination.current"
              v-model:page-size="pagination.size"
              :total="pagination.total"
              layout="prev, pager, next"
              :page-sizes="[12, 24, 36]"
              @current-change="fetchMeetings"
          />
        </div>
      </div>
      <el-empty v-else description="暂无会议数据" :image-size="100" />
    </template>

    <!-- 添加按钮 -->
    <el-button
        type="primary"
        circle
        class="add-button"
        @click="showAddDialog"
    >
      <el-icon><plus /></el-icon>
    </el-button>

    <MeetingDialog
        v-model="dialogVisible"
        :meeting="currentMeeting"
        :is-admin="userStore.isSuperAdmin"
        @submit="handleSubmit"
    />
  </div>
</template>

<script setup>
import {ref, onMounted, computed} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Search, Plus, Location, User} from '@element-plus/icons-vue'
import MeetingDialog from './MeetingDialog.vue'
import {useUserStore} from '@/stores/user'
import api from '@/api/meeting'
import {useRouter} from 'vue-router'

const router = useRouter()
const userStore = useUserStore()

// 使用计算属性获取用户信息
const isSuperAdmin = computed(() => userStore.isSuperAdmin)
const currentUser = computed(() => userStore.currentUser)
const username = computed(() => userStore.currentUser?.username || '')

// 数据
const meetings = ref([])
const loading = ref(false)

// 搜索和筛选
const searchQuery = ref('')
const filterStatus = ref('')
const filterDate = ref('')

// 分页
const pagination = ref({
  current: 1,
  size: 12,
  total: 0
})

// 对话框
const dialogVisible = ref(false)
const currentMeeting = ref(null)

// 格式化时间
const formatTime = (timeString) => {
  if (!timeString) return ''
  const date = new Date(timeString)
  return date.toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'})
}

// 截断内容
const truncateContent = (content) => {
  if (!content) return '暂无描述'
  return content.length > 100 ? content.substring(0, 100) + '...' : content
}

// 获取状态文本
const getStatusText = (status) => {
  switch (status) {
    case 0:
      return '草稿'
    case 1:
      return '待审核'
    case 2:
      return '已发布'
    default:
      return '未知'
  }
}

// 获取状态类名
const getStatusClass = (status) => {
  switch (status) {
    case 0:
      return 'draft'
    case 1:
      return 'pending'
    case 2:
      return 'published'
    default:
      return ''
  }
}

const fetchMeetings = async () => {
  loading.value = true;
  try {
    const params = {
      page: pagination.value.current,
      size: pagination.value.size,
      startTime: filterDate.value ? new Date(filterDate.value).toISOString() : undefined,
      // 关键修改点1：管理员根据筛选条件传status，普通用户固定传2
      status: !isSuperAdmin.value ? 2 : (filterStatus.value !== '' ? filterStatus.value : undefined),
      // 关键修改点2：管理员不需要creator参数，普通用户也不需要
      creator: undefined
    };

    const response = await api.getCertainStatusMeetings(params);
    const data = response.data || {};

    // 获取所有会议数据
    let allMeetings = data.list.map(item => ({
      id: item.id,
      title: item.title,
      content: item.content,
      address: item.address,
      startTime: item.startTime,
      endTime: item.endTime,
      coverImage: item.coverImage,
      status: item.status,
      creator: item.creator
    }));

    // 仅在前端实现标题搜索功能（因为后端没有提供标题搜索）
    if (searchQuery.value) {
      const searchTerm = searchQuery.value.toLowerCase();
      allMeetings = allMeetings.filter(meeting =>
          meeting.title.toLowerCase().includes(searchTerm)
      );
    }

    // 更新数据
    meetings.value = allMeetings;
    // 使用后端返回的总数
    pagination.value.total = data.total || allMeetings.length;

  } catch (error) {
    console.error("请求失败:", error);
    ElMessage.error("加载会议列表失败: " + (error.response?.data?.message || error.message));
  } finally {
    loading.value = false;
  }
}

const goToMeetingDetail = (id) => {
  console.log('跳转到会议详情:', id)
  router.push(`/meetings/${id}`)
}

// 添加/编辑会议
const showAddDialog = () => {
  console.log('显示添加会议对话框')
  currentMeeting.value = {
    title: '',
    content: '',
    startTime: '',
    endTime: '',
    address: '',
    coverImage: '',
    status: 0, // 默认草稿
    isOnline: false,
    creator: username.value
  }
  dialogVisible.value = true
}

const editMeeting = (meeting) => {
  console.log('编辑会议:', meeting)
  currentMeeting.value = {
    ...meeting,
    creator: username.value
  }
  dialogVisible.value = true
}

const handleSubmit = async (meetingData) => {
  try {
    console.log('提交会议数据:', JSON.parse(JSON.stringify(meetingData))) // 打印完整数据

    const submitData = {
      id: meetingData.id,
      title: meetingData.title,
      address: meetingData.address,
      startTime: meetingData.startTime,
      endTime: meetingData.endTime,
      content: meetingData.content,
      coverImage: meetingData.coverImage, // 确保包含封面
      status: meetingData.status,
      creator: username.value
    };

    if (meetingData.id) {
      // 更新会议 - 添加调试信息
      console.log('[更新会议] 提交数据:', submitData);
      await api.updateMeeting(meetingData.id, submitData);
      ElMessage.success('更新成功');
    } else {
      // 添加会议
      console.log('[添加会议] 提交数据:', {
        ...submitData,
        now_username: userStore.currentUser.username
      });
      await api.addMeeting(submitData, {
        params: {
          now_username: userStore.currentUser.username
        }
      });
      ElMessage.success('创建成功');
    }

    fetchMeetings();
  } catch (error) {
    console.error('操作失败:', {
      message: error.message,
      response: error.response?.data,
      stack: error.stack
    });
    ElMessage.error(error.response?.data?.message || '操作失败');
  } finally {
    dialogVisible.value = false;
  }
}

// 删除会议
const deleteMeeting = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该会议吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    console.log('删除会议ID:', id)
    await api.deleteMeeting(id, username.value)
    ElMessage.success('删除成功')
    fetchMeetings()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error(error.response?.data?.message || '删除失败')
    }
  }
}

// 审核通过会议
const passMeeting = async (id) => {
  try {
    console.log('通过会议ID:', id)
    await api.passMeeting(id)
    ElMessage.success('会议已通过')
    fetchMeetings()
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error(error.response?.data?.message || '操作失败')
  }
}

// 拒绝会议
const refuseMeeting = async (id) => {
  try {
    console.log('拒绝会议ID:', id)
    await api.refuseMeeting(id)
    ElMessage.success('会议已拒绝')
    fetchMeetings()
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error(error.response?.data?.message || '操作失败')
  }
}

// 初始化加载
onMounted(async () => {
  console.log('会议列表页初始化')
  await userStore.initUser()
  console.log('当前用户信息:', currentUser.value)

  if (!userStore.isSuperAdmin) {
    console.log('普通用户进入页面')
  } else {
    console.log('管理员进入页面')
  }

  fetchMeetings()
})
</script>

<style scoped>
.meeting-list {
  padding: 20px;
  position: relative;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.actions {
  display: flex;
  gap: 10px;
}

.filter-bar {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.loading-container {
  padding: 20px;
}

.list-container {
  margin-top: 20px;
}

.grid-view {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.meeting-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.3s;
  display: flex;
  flex-direction: column;
}

.meeting-card:hover {
  transform: translateY(-5px);
}

.card-media {
  position: relative;
  height: 160px;
}

.card-media .cover {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.status-badge {
  position: absolute;
  left: 8px;
  top: 8px;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  color: white;
}

.status-badge.draft {
  background-color: #909399;
}

.status-badge.pending {
  background-color: #e6a23c;
}

.status-badge.published {
  background-color: #67c23a;
}

.time-info {
  position: absolute;
  left: 8px;
  bottom: 8px;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.card-content {
  padding: 15px;
  flex: 1;
}

.card-content h3 {
  margin: 0 0 8px;
  font-size: 16px;
  line-height: 1.4;
}

.location, .participants {
  display: flex;
  align-items: center;
  margin: 5px 0;
  color: #666;
  font-size: 14px;
}

.location .el-icon, .participants .el-icon {
  margin-right: 5px;
}

.description {
  margin: 10px 0 0;
  color: #666;
  font-size: 14px;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.card-footer {
  padding: 10px 15px;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: flex-end;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.add-button {
  position: fixed;
  right: 30px;
  bottom: 30px;
  width: 50px;
  height: 50px;
  font-size: 20px;
}

@media (max-width: 768px) {
  .header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .actions {
    width: 100%;
  }

  .grid-view {
    grid-template-columns: 1fr;
  }
}
</style>