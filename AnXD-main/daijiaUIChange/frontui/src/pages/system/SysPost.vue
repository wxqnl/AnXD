<template>
  <div class="app-container">
    <el-card class="search-card">
      <el-form :model="queryParams" ref="queryForm" :inline="true" class="search-form">
        <el-form-item label="岗位名称" prop="name">
          <el-input v-model="queryParams.name" placeholder="请输入岗位名称" clearable />
        </el-form-item>
        <el-form-item label="岗位编码" prop="postCode">
          <el-input v-model="queryParams.postCode" placeholder="请输入岗位编码" clearable />
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
          <span>岗位列表</span>
          <el-button type="primary" @click="handleAdd">新增岗位</el-button>
        </div>
      </template>
      
      <el-table v-loading="loading" :data="postList" border>
        <el-table-column type="index" label="#" width="50" />
        <el-table-column prop="name" label="岗位名称" />
        <el-table-column prop="postCode" label="岗位编码" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '正常' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" />
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

    <!-- 添加/编辑岗位对话框 -->
    <el-dialog 
      :title="dialogTitle" 
      v-model="dialogVisible" 
      width="500px" 
      append-to-body
      :close-on-click-modal="false"
    >
      <el-form 
        ref="postFormRef" 
        :model="postForm" 
        :rules="rules" 
        label-width="80px"
      >
        <el-form-item label="岗位名称" prop="name">
          <el-input v-model="postForm.name" placeholder="请输入岗位名称" />
        </el-form-item>
        <el-form-item label="岗位编码" prop="postCode">
          <el-input v-model="postForm.postCode" placeholder="请输入岗位编码" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="postForm.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="postForm.description" type="textarea" placeholder="请输入描述" />
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
import { getPostList, addPost, updatePost, deletePost, getPostById } from '@/api/post'

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: '',
  postCode: '',
  status: undefined
})

// 表格数据和状态
const postList = ref([])
const loading = ref(false)
const total = ref(0)

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const postFormRef = ref(null)
const postForm = reactive({
  id: undefined,
  name: '',
  postCode: '',
  status: 1,
  description: ''
})

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入岗位名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  postCode: [
    { required: true, message: '请输入岗位编码', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ],
  description: [
    { max: 200, message: '长度不能超过 200 个字符', trigger: 'blur' }
  ]
}

// 获取岗位列表
const getList = async () => {
  try {
    loading.value = true
    const res = await getPostList(queryParams)
    console.log('岗位列表响应:', res)
    postList.value = res.data.items || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('获取岗位列表失败', error)
  } finally {
    loading.value = false
  }
}

// 查询按钮
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

// 重置查询
const resetQuery = () => {
  queryParams.name = ''
  queryParams.postCode = ''
  queryParams.status = undefined
  queryParams.pageNum = 1
  getList()
}

// 重置表单
const resetForm = () => {
  postForm.id = undefined
  postForm.name = ''
  postForm.postCode = ''
  postForm.status = 1
  postForm.description = ''
  nextTick(() => {
    postFormRef.value?.resetFields()
  })
}

// 新增岗位
const handleAdd = () => {
  resetForm()
  dialogTitle.value = '新增岗位'
  dialogVisible.value = true
}

// 编辑岗位
const handleEdit = async (row) => {
  resetForm()
  dialogTitle.value = '编辑岗位'
  dialogVisible.value = true
  
  // 获取最新的岗位信息
  try {
    const res = await getPostById(row.id)
    if (res.data) {
      // 填充表单数据
      Object.assign(postForm, res.data)
    }
  } catch (error) {
    ElMessage.error('获取岗位信息失败')
  }
}

// 删除岗位
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除岗位 ${row.name} 吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deletePost(row.id)
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
  postFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (postForm.id) {
          // 编辑岗位
          await updatePost(postForm)
          ElMessage.success('修改岗位成功')
        } else {
          // 新增岗位
          await addPost(postForm)
          ElMessage.success('新增岗位成功')
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

.search-card {
  margin-bottom: 20px;
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