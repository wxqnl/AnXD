<template>
  <div class="app-container">
    <el-card class="search-card">
      <el-form :model="queryParams" ref="queryForm" :inline="true" class="search-form">
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="queryParams.nickname" placeholder="请输入昵称" clearable />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="queryParams.gender" placeholder="请选择性别" clearable>
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="queryParams.phone" placeholder="请输入手机号" clearable />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="正常" :value="1" />
            <el-option label="停用" :value="2" />
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
          <span>客户列表</span>
        </div>
      </template>
      
      <el-table v-loading="loading" :data="customerList" border>
        <el-table-column type="index" label="#" width="50" />
        <el-table-column prop="nickname" label="昵称" width="120" />
        <el-table-column prop="gender" label="性别" width="80" />
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column prop="wxOpenId" label="微信OpenID" width="200" show-overflow-tooltip />
        <el-table-column label="头像" width="80">
          <template #default="scope">
            <el-avatar v-if="scope.row.avatarUrl" :src="scope.row.avatarUrl" :size="40"></el-avatar>
            <el-avatar v-else :size="40" icon="el-icon-user"></el-avatar>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280">
          <template #default="scope">
            <el-button type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(scope.row)">删除</el-button>
            <el-button type="info" link @click="viewLog(scope.row)">查看日志</el-button>
            <el-button type="warning" link @click="viewDetail(scope.row)">查看详情</el-button>
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

    <!-- 客户编辑对话框 -->
    <el-dialog 
      title="编辑客户" 
      v-model="dialogVisible" 
      width="500px" 
      append-to-body
      :close-on-click-modal="false"
    >
      <el-form 
        ref="customerFormRef" 
        :model="customerForm" 
        :rules="rules" 
        label-width="100px"
      >
        <el-form-item label="客户ID" prop="id">
          <el-input v-model="customerForm.id" disabled />
        </el-form-item>
        <el-form-item label="微信OpenID" prop="wxOpenId">
          <el-input v-model="customerForm.wxOpenId" disabled />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="customerForm.nickname" disabled />
        </el-form-item>
        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="customerForm.phone" disabled />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="customerForm.status">
            <el-radio :value="1">正常</el-radio>
            <el-radio :value="2">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancelForm">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 客户详情对话框 -->
    <el-dialog 
      title="客户详情" 
      v-model="detailDialogVisible" 
      width="600px"
    >
      <el-descriptions title="基本信息" :column="2" border>
        <el-descriptions-item label="客户ID">{{ detailForm.id }}</el-descriptions-item>
        <el-descriptions-item label="微信OpenID">{{ detailForm.wxOpenId }}</el-descriptions-item>
        <el-descriptions-item label="昵称">{{ detailForm.nickname }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ detailForm.gender }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ detailForm.phone }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="detailForm.status === 1 ? 'success' : 'danger'">
            {{ detailForm.status === 1 ? '正常' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>

      <template v-if="detailForm.avatarUrl">
        <el-divider content-position="center">头像</el-divider>
        <div class="avatar-container">
          <el-image 
            :src="detailForm.avatarUrl" 
            :preview-src-list="[detailForm.avatarUrl]"
            fit="contain"
            style="width: 120px; height: 120px;"
          ></el-image>
        </div>
      </template>
    </el-dialog>

    <!-- 登录日志对话框 -->
    <el-dialog 
      title="客户登录日志" 
      v-model="logDialogVisible" 
      width="600px"
    >
      <div v-if="logData">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="客户ID">{{ logData.customerId }}</el-descriptions-item>
          <el-descriptions-item label="IP地址">{{ logData.ipaddr || '未记录' }}</el-descriptions-item>
          <el-descriptions-item label="登录状态">
            <el-tag :type="logData.status ? 'danger' : 'success'">
              {{ logData.status ? '失败' : '成功' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="提示信息">{{ logData.msg || '无' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <div v-else class="empty-data">
        暂无登录日志数据
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllCustomerInfo, getCustomerInfo, removeCustomer, updateCustomer, getCustomerLoginLog } from '@/api/customer'

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  nickname: '',
  gender: undefined,
  phone: '',
  status: undefined
})

// 表格数据和状态
const customerList = ref([])
const loading = ref(false)
const total = ref(0)

// 编辑对话框相关
const dialogVisible = ref(false)
const customerFormRef = ref(null)
const customerForm = reactive({
  id: '',
  wxOpenId: '',
  nickname: '',
  gender: '',
  avatarUrl: '',
  phone: '',
  status: 1
})

// 详情对话框相关
const detailDialogVisible = ref(false)
const detailForm = reactive({})

// 日志相关
const logDialogVisible = ref(false)
const logData = ref(null)

// 表单验证规则
const rules = {
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// 获取客户列表
const getList = async () => {
  try {
    loading.value = true
    const res = await getAllCustomerInfo(queryParams)
    console.log('客户列表响应:', res)
    customerList.value = res.data.items || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('获取客户列表失败', error)
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
  queryParams.nickname = ''
  queryParams.gender = undefined
  queryParams.phone = ''
  queryParams.status = undefined
  queryParams.pageNum = 1
  getList()
}

// 重置表单
const resetForm = () => {
  customerForm.id = ''
  customerForm.wxOpenId = ''
  customerForm.nickname = ''
  customerForm.gender = ''
  customerForm.avatarUrl = ''
  customerForm.phone = ''
  customerForm.status = 1
  nextTick(() => {
    customerFormRef.value?.resetFields()
  })
}

// 编辑客户
const handleEdit = async (row) => {
  resetForm()
  dialogVisible.value = true
  
  // 获取最新的客户信息
  try {
    // 优先使用昵称查询
    const res = await getCustomerInfo(row.nickname)
    if (res.data) {
      // 填充表单数据
      Object.assign(customerForm, res.data)
    }
  } catch (error) {
    console.error('获取客户信息失败', error)
    ElMessage.error('获取客户信息失败')
  }
}

// 查看客户详情
const viewDetail = async (row) => {
  try {
    detailDialogVisible.value = true
    // 优先使用昵称查询
    const res = await getCustomerInfo(row.nickname)
    if (res.data) {
      Object.assign(detailForm, res.data)
    }
  } catch (error) {
    console.error('获取客户详情失败', error)
    ElMessage.error('获取客户详情失败')
  }
}

// 查看登录日志
const viewLog = async (row) => {
  try {
    logDialogVisible.value = true
    logData.value = null
    
    // 使用nickname查询登录日志
    const res = await getCustomerLoginLog(row.nickname)
    console.log('登录日志响应:', res)
    
    if (res.data) {
      logData.value = res.data
    }
  } catch (error) {
    console.error('获取登录日志失败', error)
    ElMessage.error('获取登录日志失败')
  }
}

// 删除客户
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除客户 ${row.nickname} 吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await removeCustomer(row.nickname)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      console.error('删除客户失败', error)
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
  customerFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 编辑客户
        const res = await updateCustomer(customerForm)
        ElMessage.success('修改客户信息成功')
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

.avatar-container {
  display: flex;
  justify-content: center;
  margin: 20px 0;
}

.empty-data {
  text-align: center;
  color: #909399;
  padding: 30px 0;
}
</style> 