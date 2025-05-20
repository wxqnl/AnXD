<template>
  <div class="app-container">
    <el-card class="search-card">
      <el-form :model="queryParams" ref="queryForm" :inline="true" class="search-form">
        <el-form-item label="订单号" prop="orderNo">
          <el-input v-model="queryParams.orderNo" placeholder="请输入订单号" clearable />
        </el-form-item>
        <el-form-item label="客户姓名" prop="customerName">
          <el-input v-model="queryParams.customerName" placeholder="请输入客户姓名" clearable />
        </el-form-item>
        <el-form-item label="评分" prop="score">
          <el-select v-model="queryParams.score" placeholder="请选择评分" clearable>
            <el-option label="1星" :value="1" />
            <el-option label="2星" :value="2" />
            <el-option label="3星" :value="3" />
            <el-option label="4星" :value="4" />
            <el-option label="5星" :value="5" />
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
          <span>评价列表</span>
        </div>
      </template>
      
      <el-table v-loading="loading" :data="commentList" border>
        <el-table-column type="index" label="#" width="50" />
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="customerName" label="客户姓名" />
        <el-table-column prop="driverName" label="司机姓名" />
        <el-table-column prop="score" label="评分">
          <template #default="scope">
            <el-rate v-model="scope.row.score" disabled />
          </template>
        </el-table-column>
        <el-table-column prop="content" label="评价内容" show-overflow-tooltip />
        <el-table-column prop="createTime" label="评价时间" width="160" />
        <el-table-column prop="replyContent" label="回复内容" show-overflow-tooltip />
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button type="primary" link @click="handleReply(scope.row)" v-if="!scope.row.replyContent">回复</el-button>
            <el-button type="info" link @click="handleDetail(scope.row)">详情</el-button>
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

    <!-- 回复评价对话框 -->
    <el-dialog title="回复评价" v-model="replyDialogVisible" width="500px">
      <el-form ref="replyFormRef" :model="replyForm" :rules="replyRules" label-width="80px">
        <el-form-item label="订单号">
          <el-input v-model="replyForm.orderNo" disabled />
        </el-form-item>
        <el-form-item label="客户评价">
          <el-input v-model="replyForm.content" type="textarea" disabled :rows="3" />
        </el-form-item>
        <el-form-item label="回复内容" prop="replyContent">
          <el-input v-model="replyForm.replyContent" type="textarea" placeholder="请输入回复内容" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="replyDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitReply">确认</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrderCommentList, getCommentByOrderId, updateComment } from '@/api/order'

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  customerId: undefined,
  driverId: undefined,
  rate: undefined,
  status: undefined
})

// 表格数据和状态
const commentList = ref([])
const loading = ref(false)
const total = ref(0)

// 回复对话框
const replyDialogVisible = ref(false)
const replyFormRef = ref(null)
const replyForm = reactive({
  id: undefined,
  orderId: undefined,
  content: '',
  replyContent: ''
})

// 表单验证规则
const replyRules = {
  replyContent: [
    { required: true, message: '请输入回复内容', trigger: 'blur' },
    { min: 1, max: 200, message: '长度在 1 到 200 个字符', trigger: 'blur' }
  ]
}

// 获取评价列表
const getList = async () => {
  try {
    loading.value = true
    const res = await getOrderCommentList(queryParams)
    console.log('订单评价响应:', res)
    commentList.value = res.data.items || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('获取评价列表失败', error)
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
  queryParams.customerId = undefined
  queryParams.driverId = undefined
  queryParams.rate = undefined
  queryParams.status = undefined
  queryParams.pageNum = 1
  getList()
}

// 查看详情
const handleDetail = async (row) => {
  try {
    const res = await getCommentByOrderId(row.orderId)
    ElMessage.info(`查看评价详情: ${row.orderNo || row.orderId}`)
    console.log('评价详情:', res.data)
  } catch (error) {
    ElMessage.error('获取详情失败')
  }
}

// 回复评价
const handleReply = (row) => {
  replyForm.id = row.id
  replyForm.orderId = row.orderId
  replyForm.content = row.content
  replyForm.replyContent = ''
  replyDialogVisible.value = true
}

// 提交回复
const submitReply = async () => {
  if (!replyFormRef.value) return
  
  await replyFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await updateComment({
          id: replyForm.id,
          orderId: replyForm.orderId,
          replyContent: replyForm.replyContent
        })
        ElMessage.success('回复成功')
        replyDialogVisible.value = false
        getList()
      } catch (error) {
        ElMessage.error('回复失败')
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