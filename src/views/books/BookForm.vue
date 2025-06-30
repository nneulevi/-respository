<template>
  <div class="book-form">
    <h1 v-if="$route.name === 'book-add'">添加书籍</h1>
    <h1 v-else>编辑书籍</h1>
    <p>当前书籍ID: {{ $route.params.id || '新书籍' }}</p>
    <router-link to="/books">返回列表</router-link>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'

const route = useRoute()
console.log('BookForm组件已加载', route.params)
</script>

<style scoped>
.book-form {
  padding: 20px;
}
</style>