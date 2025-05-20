<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>菜单管理</span>
          <el-button type="primary" @click="handleAdd">新增菜单</el-button>
        </div>
      </template>
      
      <el-table v-loading="loading" :data="menuList" row-key="id" border :tree-props="{ children: 'children' }">
        <el-table-column prop="name" label="菜单名称" />
        <el-table-column prop="path" label="路由地址" />
        <el-table-column prop="component" label="组件路径" />
        <el-table-column prop="icon" label="图标">
          <template #default="scope">
            <el-icon v-if="scope.row.icon">
              <component :is="scope.row.icon"></component>
            </el-icon>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="orderNum" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '正常' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMenuTree, addMenu, updateMenu, deleteMenu } from '@/api/menu'

// 表格数据和状态
const menuList = ref([])
const loading = ref(false)

// 获取菜单树
const getList = async () => {
  try {
    loading.value = true
    const res = await getMenuTree()
    console.log('菜单树响应:', res)
    menuList.value = res.data || []
  } catch (error) {
    console.error('获取菜单列表失败', error)
  } finally {
    loading.value = false
  }
}

// 新增菜单
const handleAdd = () => {
  ElMessage.info('新增菜单功能待实现')
}

// 编辑菜单
const handleEdit = (row) => {
  ElMessage.info(`编辑菜单: ${row.name}`)
}

// 删除菜单
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除菜单 ${row.name} 吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteMenu(row.id)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 页面加载时获取数据
onMounted(() => {
  getList()
})
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style> 