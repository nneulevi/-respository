<template>
  <div class="writer-card" @click="$router.push(`/writers/${writer.id}`)">
    <div class="avatar-container">
      <img :src="writer.avatar || '/src/assets/images/writer/default.jpg'" class="avatar">
      <div class="book-count">{{ writer.bookCount }}部作品</div>
    </div>
    <h3 class="writer-name">{{ writer.name }}</h3>
  </div>
</template>

<script setup>
import {createRouter as $router} from "vue-router";

defineProps({
  writer: {
    type: Object,
    required: true
  }
})
</script>

<style scoped>
.writer-card {
  text-align: center;
  cursor: pointer;
  transition: transform 0.3s;
}

.writer-card:hover {
  transform: translateY(-5px);
}

.avatar-container {
  position: relative;
  width: 100%;
  margin-bottom: 10px;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  margin: 0 auto;
  background-color: #f5f5f5;
}

.book-count {
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 12px;
}

.writer-name {
  font-size: 14px;
  margin: 0;
}
</style>