<template>
  <div class="writer-filter">
    <el-input
        v-model="name"
        placeholder="搜索作家姓名"
        clearable
        style="width: 200px; margin-right: 15px"
        @change="$emit('search')"
    />

    <el-select
        v-model="filters.gender"
        placeholder="性别"
        clearable
        style="width: 120px; margin-left: 10px"
        @change="fetchWriters"
    >
      <el-option label="男" :value="1" />  <!-- 1表示男 -->
      <el-option label="女" :value="0" />  <!-- 0表示女 -->
    </el-select>

    <el-select
        v-model="region"
        placeholder="地区"
        clearable
        style="width: 120px; margin-right: 15px"
        @change="$emit('search')"
    >
      <el-option label="中国" value="CHINA" />
      <el-option label="日本" value="JAPAN" />
      <el-option label="欧美" value="EUROPE" />
    </el-select>

    <el-button type="primary" @click="$emit('search')">
      搜索
    </el-button>
  </div>
</template>

<script setup>
defineProps({
  name: String,
  gender: String,
  region: String
})

defineEmits(['update:name', 'update:gender', 'update:region', 'search'])
</script>

<style scoped>
.writer-filter {
  padding: 15px;
  background: #f5f7fa;
  border-radius: 4px;
  margin-bottom: 20px;
}
</style>