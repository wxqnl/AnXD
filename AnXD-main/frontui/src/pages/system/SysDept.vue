<template>
  <div class="app-container">
    <el-card class="search-card">
      <el-form :model="queryParams" ref="queryForm" :inline="true" class="search-form">
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="queryParams.name" placeholder="请输入部门名称" clearable />
        </el-form-item>
        <el-form-item label="负责人" prop="leader">
          <el-input v-model="queryParams.leader" placeholder="请输入负责人" clearable />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="正常" :value="1" />
            <el-option label="停用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <template #header>
        <div class="card-header">
          <span>部门管理</span>
          <el-button type="primary" @click="handleAdd">新增部门</el-button>
        </div>
      </template>
      
      <el-table v-loading="loading" :data="deptList" row-key="id" border :tree-props="{ children: 'children' }" :default-expand-all="true">
        <el-table-column prop="name" label="部门名称" />
        <el-table-column prop="leader" label="负责人" />
        <el-table-column prop="phone" label="联系电话" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="sortValue" label="排序" width="80" />
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
      
      <el-pagination
        v-if="total > 0"
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 添加/编辑部门对话框 -->
    <el-dialog 
      :title="dialogTitle" 
      v-model="dialogVisible" 
      width="500px" 
      append-to-body
      :close-on-click-modal="false"
    >
      <el-form 
        ref="deptFormRef" 
        :model="deptForm" 
        :rules="rules" 
        label-width="80px"
      >
        <el-form-item label="上级部门" prop="parentId">
          <el-tree-select
            v-model="deptForm.parentId"
            :data="deptTreeOptions"
            :render-after-expand="false"
            node-key="id"
            :props="{ label: 'name', value: 'id', children: 'children' }"
            placeholder="请选择上级部门"
            check-strictly
            clearable
          />
        </el-form-item>
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="deptForm.name" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="负责人" prop="leader">
          <el-input v-model="deptForm.leader" placeholder="请输入负责人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="deptForm.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="排序" prop="sortValue">
          <el-input-number v-model="deptForm.sortValue" :min="0" :max="999" controls-position="right" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="deptForm.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancelForm">取 消</el-button>
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getDeptTree, addDept, updateDept, deleteDept, getDeptById } from '@/api/dept'

// 表格数据和状态
const deptList = ref([])
const deptTreeOptions = ref([])
const loading = ref(false)
const total = ref(0)
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: '',
  leader: '',
  status: undefined
})

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const deptFormRef = ref(null)
const deptForm = reactive({
  id: undefined,
  parentId: 0,
  name: '',
  leader: '',
  phone: '',
  sortValue: 0,
  status: 1
})

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入部门名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  parentId: [
    { required: true, message: '请选择上级部门', trigger: 'change' }
  ],
  sortValue: [
    { required: true, message: '请输入排序', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// 查询按钮
const handleQuery = () => {
  queryParams.pageNum = 1  // 确保有值
  queryParams.pageSize = queryParams.pageSize || 10  // 确保有值
  getList()
}

// 重置查询
const resetQuery = () => {
  queryParams.name = ''
  queryParams.leader = ''
  queryParams.status = undefined
  queryParams.pageNum = 1  // 确保有值
  queryParams.pageSize = 10  // 确保有值 
  getList()
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

// 获取部门树
const getList = async () => {
  try {
    loading.value = true
    
    // 确保必填参数存在
    const params = {
      pageSize: queryParams.pageSize || 10,
      pageNum: queryParams.pageNum || 1,
      name: queryParams.name,
      leader: queryParams.leader,
      status: queryParams.status
    }
    
    // 获取部门列表数据，传递分页参数
    const res = await getDeptTree(params)
    console.log('部门列表响应:', res)
    
    // 处理后端返回的数据
    if (res.data && res.data.items) {
      // 使用后端返回的数据
      deptList.value = res.data.items || []
      total.value = res.data.total || 0
      
      // 构建部门树结构
      deptList.value = deptList.value.map(item => ({
        ...item,
        parentId: item.parentId || 0, // 确保parentId不为空
        children: [] // 初始化children数组
      }))
      
      // 构建树形选择器数据
      deptTreeOptions.value = [{ id: 0, name: '顶级部门', parentId: -1 }].concat(
        deptList.value.map(item => ({
          id: item.id,
          name: item.name,
          parentId: item.parentId
        }))
      )
    } else {
      deptList.value = []
      deptTreeOptions.value = [{ id: 0, name: '顶级部门', parentId: -1 }]
      total.value = 0
    }
  } catch (error) {
    console.error('获取部门列表失败', error)
    ElMessage.error('获取部门列表失败：' + (error.message || '未知错误'))
  } finally {
    loading.value = false
  }
}

// 重置表单
const resetForm = () => {
  deptForm.id = undefined
  deptForm.parentId = 0
  deptForm.name = ''
  deptForm.leader = ''
  deptForm.phone = ''
  deptForm.sortValue = 0
  deptForm.status = 1
  nextTick(() => {
    deptFormRef.value?.resetFields()
  })
}

// 新增部门
const handleAdd = () => {
  resetForm()
  dialogTitle.value = '新增部门'
  dialogVisible.value = true
}

// 编辑部门
const handleEdit = async (row) => {
  resetForm()
  dialogTitle.value = '编辑部门'
  dialogVisible.value = true
  
  // 获取最新的部门信息
  try {
    const res = await getDeptById(row.id)
    if (res.data) {
      // 填充表单数据
      Object.assign(deptForm, res.data)
    }
  } catch (error) {
    ElMessage.error('获取部门信息失败')
  }
}

// 删除部门
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除部门 ${row.name} 吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteDept(row.id)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 取消表单
const cancelForm = () => {
  dialogVisible.value = false
  resetForm()
}

// 提交表单
const submitForm = async () => {
  deptFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (deptForm.id) {
          // 编辑部门
          await updateDept(deptForm)
          ElMessage.success('修改部门成功')
        } else {
          // 新增部门
          await addDept(deptForm)
          ElMessage.success('新增部门成功')
        }
        dialogVisible.value = false
        getList()
      } catch (error) {
        console.error('操作失败', error)
        ElMessage.error('操作失败，请稍后重试')
      }
    }
  })
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