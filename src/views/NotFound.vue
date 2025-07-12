<template>
  <div class="not-found">
    <h1>404 - 页面不存在</h1>
    <p>您访问的路径 {{ $route.path }} 不存在</p>
    <router-link to="/">返回首页</router-link>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'

const route = useRoute()
console.log('NotFound组件已加载', route.path)
</script>

<style scoped>
.not-found {
  padding: 20px;
  text-align: center;
}
</style>