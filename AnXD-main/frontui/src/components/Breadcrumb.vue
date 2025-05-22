<template>
  <el-breadcrumb separator="/">
    <el-breadcrumb-item v-for="(item, index) in breadcrumbs" :key="index">
      <span v-if="index === breadcrumbs.length - 1" class="no-redirect">{{ item.title }}</span>
      <router-link v-else :to="item.path">{{ item.title }}</router-link>
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>

<script setup>
import { computed, watch, ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const breadcrumbs = ref([])

// 生成面包屑数据
const getBreadcrumb = () => {
  // 重置面包屑
  breadcrumbs.value = []
  
  // 始终有首页
  breadcrumbs.value.push({
    path: '/dashboard',
    title: '首页'
  })
  
  // 加入当前页面
  if (route.meta && route.meta.title && route.path !== '/dashboard') {
    breadcrumbs.value.push({
      path: route.path,
      title: route.meta.title
    })
  }
}

// 初始化
onMounted(() => {
  getBreadcrumb()
})

// 监听路由变化，生成面包屑
watch(
  () => route.path,
  () => getBreadcrumb()
)
</script>

<style scoped>
.el-breadcrumb {
  display: inline-block;
  line-height: 60px;
}

.no-redirect {
  color: #97a8be;
  cursor: text;
}
</style>

