<template>
  <div class="rich-text-editor">
    <!-- 工具栏 -->
    <div class="toolbar" v-if="toolbarVisible">
      <el-button-group>
        <el-button size="small" @click="execCommand('bold')">
          <strong>B</strong>
        </el-button>
        <el-button size="small" @click="execCommand('italic')">
          <em>I</em>
        </el-button>
        <el-button size="small" @click="execCommand('underline')">
          <u>U</u>
        </el-button>
        <el-button size="small" @click="execCommand('insertUnorderedList')">
          <span>• List</span>
        </el-button>
        <el-button size="small" @click="execCommand('insertOrderedList')">
          <span>1. List</span>
        </el-button>
        <el-button size="small" @click="execCommand('createLink')">
          <el-icon><link /></el-icon>
        </el-button>
        <el-button size="small" @click="execCommand('undo')">
          <el-icon><refresh-left /></el-icon>
        </el-button>
        <el-button size="small" @click="execCommand('redo')">
          <el-icon><refresh-right /></el-icon>
        </el-button>
      </el-button-group>
    </div>

    <!-- 富文本编辑区域 -->
    <div
        ref="editor"
        class="editor-content"
        contenteditable="true"
        @input="handleInput"
        @blur="handleBlur"
        @paste="handlePaste"
        :placeholder="placeholder"
    ></div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, nextTick } from 'vue'
import { Link, RefreshLeft, RefreshRight } from '@element-plus/icons-vue'

const props = defineProps({
  modelValue: String,
  placeholder: {
    type: String,
    default: '请输入内容...'
  },
  toolbarVisible: {
    type: Boolean,
    default: true
  }
})

const emit = defineEmits(['update:modelValue', 'blur'])

const editor = ref(null)

// 初始化内容
onMounted(() => {
  if (props.modelValue) {
    editor.value.innerHTML = props.modelValue
  }
})

// 监听 modelValue 变化
watch(() => props.modelValue, (newVal) => {
  if (editor.value && newVal !== editor.value.innerHTML) {
    editor.value.innerHTML = newVal
  }
})

// 处理输入事件
const handleInput = () => {
  emit('update:modelValue', editor.value.innerHTML)
}

// 处理失焦事件
const handleBlur = () => {
  emit('blur', editor.value.innerHTML)
}

// 处理粘贴事件（移除格式）
const handlePaste = (e) => {
  e.preventDefault()
  const text = (e.clipboardData || window.clipboardData).getData('text/plain')
  document.execCommand('insertText', false, text)
}

// 执行命令（加粗、斜体等）
const execCommand = (command, value = null) => {
  document.execCommand(command, false, value)
  editor.value.focus()
}
</script>

<style scoped>
.rich-text-editor {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 5px;
}

.toolbar {
  margin-bottom: 5px;
  padding: 5px;
  background: #f5f7fa;
  border-radius: 4px;
}

.editor-content {
  min-height: 150px;
  padding: 10px;
  outline: none;
}

.editor-content:empty:before {
  content: attr(placeholder);
  color: #c0c4cc;
}

.editor-content:focus {
  border-color: #409eff;
}
</style>