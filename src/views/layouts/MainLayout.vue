<template>
  <div class="main-layout">
    <!-- 顶部导航栏 -->
    <header class="header">
      <div class="header-container">
        <!-- 左侧logo和标题 -->
        <div class="header-left">
          <router-link to="/dashboard" class="logo-link">
            <img
                src="/src/assets/logo.jpg"
                alt="青棠小说"
                class="logo"
                v-if="logoLoaded"
                @error="logoLoaded = false"
            >
            <span class="site-name">测盟会</span>
          </router-link>
        </div>

        <!-- 右侧导航菜单 -->
        <div class="header-right">
          <nav class="main-nav">
            <router-link
                v-for="item in navItems"
                :key="item.path"
                :to="item.path"
                class="nav-link"
            >
              {{ item.name }}
            </router-link>
          </nav>

          <!-- 搜索框 -->
          <div class="search-box">
            <el-input
                v-model="searchQuery"
                placeholder="搜索"
                clearable
                @keyup.enter="handleSearch"
            >
              <template #prefix>
                <el-icon><search /></el-icon>
              </template>
            </el-input>
          </div>
        </div>
      </div>
    </header>

    <!-- 主要内容区 -->
    <main class="content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Search } from '@element-plus/icons-vue'
import {ElMessage} from "element-plus";
import {computed} from 'vue'
const router = useRouter()
const logoLoaded = ref(true)
const searchQuery = ref('')
import {useUserStore} from '@/stores/user'

const userStore = useUserStore()
// 计算属性获取用户信息
const isSuperAdmin = computed(() => userStore.isSuperAdmin)
const currentUser = computed(() => userStore.currentUser)
const username = computed(() => userStore.currentUser?.username || 'admin')
const navItems = computed(() => {
  const items = [
    { path: '/course', name: '课程' },
    { path: '/news', name: '资讯' },
    { path: '/meeting', name: '会议' },
    { path: '/center', name: '个人中心' },
  ]

  // 只有管理员才显示用户管理
  if (isSuperAdmin.value) {
    items.unshift({ path: '/user', name: '用户管理' })
  }

  return items
})

const handleSearch = () => {
  const query = searchQuery.value.trim()
  if (query) {
    router.push({
      path: '/search',
      query: { q: query }
    })
  } else {
    ElMessage.warning('请输入搜索内容')
  }
}
</script>

<style scoped>
.main-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

/* 顶部导航栏样式 */
.header {
  background: #f5f5f5; /* 浅灰色背景 */
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 1000;
  height: 80px; /* 高度提升一倍 */
  display: flex;
  align-items: center; /* 垂直居中 */
}
.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 30px; /* 增加左右内边距 */
  width: 100%;
}

.header-left {
  display: flex;
  align-items: center;
  min-width: 200px;
}

.logo-link {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: #333;
  gap: 12px; /* 增大logo和文字间距 */
}

.logo {
  width: 36px; /* 适当增大logo */
  height: 36px;
  object-fit: contain;
}

.site-name {
  font-size: 24px; /* 增大字体 */
  font-weight: bold;
  color: black;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 40px; /* 增大右侧元素间距 */
}

.main-nav {
  display: flex;
  gap: 30px; /* 增大导航项间距 */
}

.nav-link {
  text-decoration: none;
  color: #333;
  font-size: 18px; /* 增大字体 */
  padding: 8px 12px; /* 增加内边距 */
  position: relative;
  transition: all 0.3s ease;
}

.nav-link:hover {
  color: #ff4d4f;
  transform: translateY(-2px); /* 添加悬停效果 */
}

.nav-link.router-link-active {
  color: #ff4d4f;
  font-weight: 600; /* 加粗选中项 */
}

.nav-link.router-link-active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 60%; /* 缩短下划线长度 */
  height: 3px; /* 加粗下划线 */
  background: #ff4d4f;
  border-radius: 2px;
}

.search-box {
  width: 280px; /* 适当增大搜索框 */
}

/* 主要内容区调整 */
.content {
  flex: 1;
  padding: 30px;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .header {
    height: 60px;
  }
  .site-name {
    font-size: 18px;
  }
  .main-nav {
    gap: 15px;
  }
  .nav-link {
    font-size: 14px;
    padding: 6px 8px;
  }
  .search-box {
    width: 180px;
  }
}
</style>