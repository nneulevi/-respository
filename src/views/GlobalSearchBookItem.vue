<!-- GlobalSearchBookItem.vue -->
<template>
  <li class="global-book-item">
    <el-card shadow="hover">
      <div class="cover">
        <img :src="book.coverUrl || defaultCover" />
      </div>
      <h4>{{ book.title }}</h4>
      <p class="author">{{ book.authorName }}</p>
      <div class="highlight" v-html="highlightMatches(book.title)" />
    </el-card>
  </li>
</template>

<script setup>
defineProps({
  book: { type: Object, required: true }
})

// 关键词高亮逻辑
const highlightMatches = (text) => {
  // 实现略（可根据搜索词标记匹配内容）
}
</script>