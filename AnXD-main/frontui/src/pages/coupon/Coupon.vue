<template>
  <div class="app-container">
    <el-card class="search-card">
      <el-form :model="queryParams" ref="queryForm" :inline="true" class="search-form">
        <el-form-item label="优惠券名称" prop="name">
          <el-input v-model="queryParams.name" placeholder="请输入优惠券名称" clearable />
        </el-form-item>
        <el-form-item label="券类型" prop="couponType">
          <el-select v-model="queryParams.couponType" placeholder="请选择券类型" clearable>
            <el-option label="现金券" :value="1" />
            <el-option label="折扣券" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="未发布" :value="0" />
            <el-option label="已发布" :value="1" />
            <el-option label="已过期" :value="-1" />
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
          <span>优惠券列表</span>
          <el-button type="primary" @click="handleAdd">新增优惠券</el-button>
        </div>
      </template>
      
      <el-table v-loading="loading" :data="couponList" border>
        <el-table-column type="index" label="#" width="50" />
        <el-table-column prop="name" label="优惠券名称" />
        <el-table-column prop="couponType" label="券类型">
          <template #default="scope">
            {{ getCouponTypeText(scope.row.couponType) }}
          </template>
        </el-table-column>
        <el-table-column label="金额/折扣">
          <template #default="scope">
            {{ formatCouponValue(scope.row) }}
          </template>
        </el-table-column>
        <el-table-column prop="conditionAmount" label="使用门槛">
          <template #default="scope">
            {{ formatCondition(scope.row) }}
          </template>
        </el-table-column>
        <el-table-column prop="expireTime" label="过期时间" />
        <el-table-column prop="publishCount" label="发行数量" width="90" />
        <el-table-column prop="useCount" label="已使用" width="80" />
        <el-table-column prop="receiveCount" label="已领取" width="80" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
            <el-button :type="scope.row.status === 1 ? 'warning' : 'success'" link @click="handleStatus(scope.row)" v-if="scope.row.status !== -1">
              {{ scope.row.status === 1 ? '停用' : '发布' }}
            </el-button>
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

    <!-- 添加/编辑优惠券对话框 -->
    <el-dialog 
      :title="dialogTitle" 
      v-model="dialogVisible" 
      width="550px" 
      append-to-body
      :close-on-click-modal="false"
    >
      <el-form 
        ref="couponFormRef" 
        :model="couponForm" 
        :rules="rules" 
        label-width="100px"
      >
        <el-form-item label="优惠券名称" prop="name">
          <el-input v-model="couponForm.name" placeholder="请输入优惠券名称" />
        </el-form-item>
        <el-form-item label="券类型" prop="couponType">
          <el-radio-group v-model="couponForm.couponType">
            <el-radio :label="1">现金券</el-radio>
            <el-radio :label="2">折扣券</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="couponForm.couponType === 1" label="金额" prop="amount">
          <el-input-number v-model="couponForm.amount" :precision="2" :step="0.01" :min="0" placeholder="请输入金额" />
          <span class="unit">元</span>
        </el-form-item>
        <el-form-item v-if="couponForm.couponType === 2" label="折扣" prop="discount">
          <el-input-number v-model="couponForm.discount" :precision="1" :step="0.1" :min="0.1" :max="10" placeholder="请输入折扣" />
          <span class="unit">折</span>
        </el-form-item>
        <el-form-item label="使用门槛" prop="conditionAmount">
          <el-input-number v-model="couponForm.conditionAmount" :precision="2" :step="0.01" :min="0" placeholder="请输入使用门槛" />
          <span class="unit">元 (0表示无门槛)</span>
        </el-form-item>
        <el-form-item label="发行数量" prop="publishCount">
          <el-input-number v-model="couponForm.publishCount" :min="1" placeholder="请输入发行数量" />
        </el-form-item>
        <el-form-item label="每人限领" prop="perLimit">
          <el-input-number v-model="couponForm.perLimit" :min="1" placeholder="请输入每人限领张数" />
          <span class="unit">张</span>
        </el-form-item>
        <el-form-item label="过期时间" prop="expireTime">
          <el-date-picker v-model="couponForm.expireTime" type="datetime" placeholder="请选择过期时间" format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="couponForm.status">
            <el-radio :label="0">未发布</el-radio>
            <el-radio :label="1">已发布</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="couponForm.description" type="textarea" placeholder="请输入描述" />
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
import { getCouponList, createCoupon, updateCoupon, deleteCoupon, getCouponByName } from '@/api/coupon'

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: '',
  couponType: undefined,
  status: undefined
})

// 表格数据和状态
const couponList = ref([])
const loading = ref(false)
const total = ref(0)

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const couponFormRef = ref(null)
const couponForm = reactive({
  id: undefined,
  name: '',
  couponType: 1,
  amount: undefined,
  discount: undefined,
  conditionAmount: 0,
  publishCount: 100,
  perLimit: 1,
  useCount: 0,
  receiveCount: 0,
  expireTime: '',
  description: '',
  status: 0
})

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入优惠券名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  couponType: [
    { required: true, message: '请选择券类型', trigger: 'change' }
  ],
  amount: [
    { required: true, message: '请输入金额', trigger: 'blur' }
  ],
  discount: [
    { required: true, message: '请输入折扣', trigger: 'blur' }
  ],
  conditionAmount: [
    { required: true, message: '请输入使用门槛', trigger: 'blur' }
  ],
  publishCount: [
    { required: true, message: '请输入发行数量', trigger: 'blur' }
  ],
  perLimit: [
    { required: true, message: '请输入每人限领张数', trigger: 'blur' }
  ],
  expireTime: [
    { required: true, message: '请选择过期时间', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// 获取优惠券列表
const getList = async () => {
  try {
    loading.value = true
    const res = await getCouponList(queryParams)
    console.log('优惠券列表响应:', res)
    couponList.value = res.data.items || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('获取优惠券列表失败', error)
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
  queryParams.couponType = undefined
  queryParams.status = undefined
  queryParams.pageNum = 1
  getList()
}

// 获取券类型文本
const getCouponTypeText = (type) => {
  const typeMap = {
    1: '现金券',
    2: '折扣券'
  }
  return typeMap[type] || '未知'
}

// 格式化优惠券金额/折扣
const formatCouponValue = (row) => {
  if (row.couponType === 1) {
    return `￥${row.amount}`
  } else if (row.couponType === 2) {
    return `${row.discount}折`
  }
  return ''
}

// 格式化使用门槛
const formatCondition = (row) => {
  return row.conditionAmount > 0 ? `满${row.conditionAmount}元可用` : '无门槛'
}

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    0: '未发布',
    1: '已发布',
    '-1': '已过期'
  }
  return statusMap[status] || '未知'
}

// 获取状态对应的标签类型
const getStatusType = (status) => {
  const typeMap = {
    0: 'info',
    1: 'success',
    '-1': 'danger'
  }
  return typeMap[status] || ''
}

// 重置表单
const resetForm = () => {
  couponForm.id = undefined
  couponForm.name = ''
  couponForm.couponType = 1
  couponForm.amount = undefined
  couponForm.discount = undefined
  couponForm.conditionAmount = 0
  couponForm.publishCount = 100
  couponForm.perLimit = 1
  couponForm.useCount = 0
  couponForm.receiveCount = 0
  couponForm.expireTime = ''
  couponForm.description = ''
  couponForm.status = 0
  
  nextTick(() => {
    couponFormRef.value?.resetFields()
  })
}

// 新增优惠券
const handleAdd = () => {
  resetForm()
  dialogTitle.value = '新增优惠券'
  dialogVisible.value = true
}

// 编辑优惠券
const handleEdit = async (row) => {
  resetForm()
  dialogTitle.value = '编辑优惠券'
  dialogVisible.value = true
  
  try {
    // 通过名称获取最新的优惠券信息
    const res = await getCouponByName(row.name)
    if (res.data) {
      // 填充表单数据
      Object.assign(couponForm, res.data)
    }
  } catch (error) {
    console.error('获取优惠券信息失败', error)
    ElMessage.error('获取优惠券信息失败')
  }
}

// 更新优惠券状态
const handleStatus = (row) => {
  const statusText = row.status === 1 ? '停用' : '发布'
  const newStatus = row.status === 1 ? 0 : 1
  
  ElMessageBox.confirm(`确定要${statusText}优惠券 ${row.name} 吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      // 先获取完整的优惠券信息
      const res = await getCouponByName(row.name)
      if (!res.data) {
        throw new Error('获取优惠券信息失败')
      }
      
      // 更新状态
      const couponData = res.data
      couponData.status = newStatus
      
      await updateCoupon(couponData)
      ElMessage.success(`${statusText}成功`)
      getList()
    } catch (error) {
      console.error('更新状态失败', error)
      ElMessage.error(`${statusText}失败: ${error.message || '未知错误'}`)
    }
  }).catch(() => {})
}

// 删除优惠券
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除优惠券 ${row.name} 吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteCoupon(row.id)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      console.error('删除失败', error)
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
  couponFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 编辑模式
        if (couponForm.id) {
          // 先获取完整的优惠券信息
          const res = await getCouponByName(couponForm.name)
          if (!res.data) {
            throw new Error('获取优惠券信息失败')
          }
          
          // 更新表单数据到后端对象
          const couponData = res.data
          couponData.name = couponForm.name
          couponData.couponType = couponForm.couponType
          couponData.conditionAmount = couponForm.conditionAmount || 0
          couponData.publishCount = couponForm.publishCount
          couponData.perLimit = couponForm.perLimit
          couponData.description = couponForm.description || ''
          couponData.status = couponForm.status
          
          // 根据券类型设置金额或折扣
          if (couponForm.couponType === 1) {
            couponData.amount = couponForm.amount
            couponData.discount = null
          } else if (couponForm.couponType === 2) {
            couponData.discount = couponForm.discount
            couponData.amount = 0
          }
          
          // 处理过期时间 - 使用T分隔的ISO格式
          if (couponForm.expireTime) {
            if (couponForm.expireTime instanceof Date) {
              couponData.expireTime = couponForm.expireTime.toISOString().slice(0, 19)
            } else {
              couponData.expireTime = new Date(couponForm.expireTime).toISOString().slice(0, 19)
            }
          }
          
          await updateCoupon(couponData)
          ElMessage.success('修改优惠券成功')
        } else {
          // 新增模式
          const submitData = {}
          
          // 基本字段
          submitData.name = couponForm.name
          submitData.couponType = Number(couponForm.couponType)
          submitData.conditionAmount = Number(couponForm.conditionAmount || 0)
          submitData.publishCount = Number(couponForm.publishCount)
          submitData.perLimit = Number(couponForm.perLimit)
          submitData.description = couponForm.description || ''
          submitData.status = Number(couponForm.status)
          submitData.useCount = 0
          submitData.receiveCount = 0
          
          // 根据券类型设置金额或折扣
          if (couponForm.couponType === 1) {
            submitData.amount = Number(couponForm.amount)
            submitData.discount = null
          } else if (couponForm.couponType === 2) {
            submitData.discount = Number(couponForm.discount)
            submitData.amount = 0
          }
          
          // 处理过期时间 - 使用T分隔的ISO格式
          if (couponForm.expireTime) {
            if (couponForm.expireTime instanceof Date) {
              submitData.expireTime = couponForm.expireTime.toISOString().slice(0, 19)
            } else {
              submitData.expireTime = new Date(couponForm.expireTime).toISOString().slice(0, 19)
            }
          }
          
          await createCoupon(submitData)
          ElMessage.success('新增优惠券成功')
        }
        
        dialogVisible.value = false
        getList()
      } catch (error) {
        console.error('操作失败', error)
        ElMessage.error(`操作失败: ${error.message || '未知错误'}`)
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

.unit {
  margin-left: 5px;
  color: #666;
}
</style> 