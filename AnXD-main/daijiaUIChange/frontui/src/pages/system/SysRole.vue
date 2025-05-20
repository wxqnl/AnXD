<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>角色管理</span>
          <el-button type="primary" @click="handleAdd">新增角色</el-button>
        </div>
      </template>
      
      <el-table v-loading="loading" :data="roleList" border>
        <el-table-column type="index" label="#" width="50" />
        <el-table-column prop="roleName" label="角色名称" />
        <el-table-column prop="roleCode" label="角色编码" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="status" label="状态">
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
      
      <el-pagination
        v-if="total > 0"
        :current-page="queryParams.pageNum"
        :page-size="queryParams.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>
    
    <!-- 角色编辑/新增对话框 -->
    <el-dialog 
      :title="dialogTitle" 
      v-model="dialogVisible" 
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form 
        ref="roleFormRef" 
        :model="roleForm" 
        :rules="rules" 
        label-width="100px"
      >
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="roleForm.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="roleForm.roleCode" placeholder="请输入角色编码" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input 
            v-model="roleForm.description" 
            type="textarea" 
            :rows="3"
            placeholder="请输入角色描述" 
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRoleList, addRole, updateRole, deleteRole, getRoleById } from '@/api/role'

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  roleName: '',
  roleCode: ''
})

// 表格数据和状态
const roleList = ref([])
const loading = ref(false)
const total = ref(0)

// 对话框相关
const dialogVisible = ref(false)
const isEdit = ref(false)
const dialogTitle = computed(() => isEdit.value ? '编辑角色' : '新增角色')

// 表单相关
const roleFormRef = ref(null)
const roleForm = reactive({
  id: undefined,
  roleName: '',
  roleCode: '',
  description: ''
})

// 表单验证规则
const rules = {
  roleName: [
    { required: true, message: '角色名称不能为空', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  roleCode: [
    { required: true, message: '角色编码不能为空', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  description: [
    { max: 200, message: '长度不能超过 200 个字符', trigger: 'blur' }
  ]
}

// 获取角色列表
const getList = async () => {
  try {
    loading.value = true
    const res = await getRoleList(queryParams)
    console.log('角色列表响应:', res)
    roleList.value = res.data.items || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('获取角色列表失败', error)
    ElMessage.error('获取角色列表失败')
  } finally {
    loading.value = false
  }
}

// 重置表单
const resetForm = () => {
  roleForm.id = undefined
  roleForm.roleName = ''
  roleForm.roleCode = ''
  roleForm.description = ''
  
  // 如果表单ref存在，重置校验状态
  if (roleFormRef.value) {
    roleFormRef.value.resetFields()
  }
}

// 新增角色
const handleAdd = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

// 编辑角色
const handleEdit = async (row) => {
  isEdit.value = true
  resetForm()
  
  try {
    // 获取最新的角色详情
    const res = await getRoleById(row.id)
    if (res && res.data) {
      // 填充表单数据
      roleForm.id = res.data.id
      roleForm.roleName = res.data.roleName
      roleForm.roleCode = res.data.roleCode
      roleForm.description = res.data.description
    } else {
      // 如果获取详情失败，使用表格行数据
      Object.assign(roleForm, row)
    }
    
    dialogVisible.value = true
  } catch (error) {
    console.error('获取角色详情失败', error)
    ElMessage.error('获取角色详情失败')
  }
}

// 删除角色
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除角色 ${row.roleName} 吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteRole(row.id)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      console.error('删除角色失败', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 提交表单
const submitForm = () => {
  roleFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          // 编辑角色
          await updateRole(roleForm)
          ElMessage.success('更新成功')
        } else {
          // 新增角色
          await addRole(roleForm)
          ElMessage.success('添加成功')
        }
        
        dialogVisible.value = false
        getList() // 刷新列表
      } catch (error) {
        console.error('保存角色失败', error)
        ElMessage.error('保存失败: ' + (error.message || '未知错误'))
      }
    }
  })
}

// 分页操作
const handleSizeChange = (size) => {
  queryParams.pageSize = size
  getList()
}

const handleCurrentChange = (page) => {
  queryParams.pageNum = page
  getList()
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

.el-pagination {
  margin-top: 20px;
  text-align: right;
}
</style> 