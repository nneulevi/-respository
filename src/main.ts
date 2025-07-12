
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import EmelentPlus from 'element-plus'
import 'element-plus/dist/index.css'

import * as ElementPlusIconsVue from '@element-plus/icons-vue'
// 在 main.js 添加
axios.defaults.baseURL = 'http://localhost:8080'

axios.defaults.withCredentials = true; // 所有请求自动带cookie

import Echarts from 'vue-echarts'
import 'echarts'


import App from './App.vue'
import router from './router'
import axios from "axios";
console.log('路由配置:', router.getRoutes())
const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
//全局组件
app.component("Echarts",Echarts) 
app.use(createPinia())
app.use(router)
app.use(EmelentPlus)
app.mount('#app')
