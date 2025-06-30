<template>
  <div id="app">
    <!-- 只保留 router-view -->
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App'
  // 不需要手动导入和注册 Login 组件
  // Router 会自动处理组件渲染
}
</script>

<style>
/* 可以添加全局样式 */
#app {
  height: 100vh;
}

html, body, #app {
  height: 100%;
  margin: 0;
}
</style>


