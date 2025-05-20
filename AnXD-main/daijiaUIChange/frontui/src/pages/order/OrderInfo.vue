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
        <el-form-item label="司机姓名" prop="driverName">
          <el-input v-model="queryParams.driverName" placeholder="请输入司机姓名" clearable />
        </el-form-item>
        <el-form-item label="订单状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="请选择订单状态" clearable>
            <el-option label="待接单" :value="1" />
            <el-option label="已接单" :value="2" />
            <el-option label="进行中" :value="3" />
            <el-option label="已完成" :value="4" />
            <el-option label="已取消" :value="5" />
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
          <span>订单列表</span>
        </div>
      </template>
      
      <el-table v-loading="loading" :data="orderList" border>
        <el-table-column type="index" label="#" width="50" />
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="customerName" label="客户名称" />
        <el-table-column prop="driverName" label="司机名称" />
        <el-table-column prop="startLocation" label="起始地点" show-overflow-tooltip />
        <el-table-column prop="endLocation" label="结束地点" show-overflow-tooltip />
        <el-table-column label="订单金额">
          <template #default="scope">
            <span v-if="scope.row.realAmount">¥{{ scope.row.realAmount }}</span>
            <span v-else-if="scope.row.expectAmount">¥{{ scope.row.expectAmount }} (预估)</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="接单时间" width="160">
          <template #default="scope">
            {{ scope.row.acceptTime || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="订单状态">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160">
          <template #default="scope">
            <el-button type="primary" link @click="handleDetail(scope.row)">详情</el-button>
            <el-button type="success" link @click="handleStatus(scope.row)" v-if="scope.row.status < 4">修改状态</el-button>
            <el-button type="info" link @click="handleFeedback(scope.row)">查看反馈</el-button>
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrderList, getOrderByOrderNo, updateOrder, getOrderFeedback } from '@/api/order'

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  orderNo: '',
  customerName: '',
  driverName: '',
  status: undefined
})

// 表格数据和状态
const orderList = ref([])
const loading = ref(false)
const total = ref(0)

// 获取订单列表
const getList = async () => {
  try {
    loading.value = true
    const res = await getOrderList(queryParams)
    console.log('订单列表响应:', res)
    orderList.value = res.data.items || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('获取订单列表失败', error)
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
  queryParams.orderNo = ''
  queryParams.customerName = ''
  queryParams.driverName = ''
  queryParams.status = undefined
  queryParams.pageNum = 1
  getList()
}

// 获取订单状态文本
const getStatusText = (status) => {
  const statusMap = {
    1: '等待接单',
    2: '已接单',
    3: '司机已到达',
    4: '开始代驾',
    5: '结束代驾',
    6: '未付款',
    7: '已付款',
    8: '订单已结束',
    9: '顾客撤单',
    10: '司机撤单',
    11: '事故关闭',
    12: '其他'
  }
  return statusMap[status] || '未知'
}

// 获取订单状态对应的标签类型
const getStatusType = (status) => {
  const typeMap = {
    1: 'info',      // 等待接单
    2: 'warning',   // 已接单
    3: 'warning',   // 司机已到达
    4: 'primary',   // 开始代驾
    5: 'primary',   // 结束代驾
    6: 'danger',    // 未付款
    7: 'success',   // 已付款
    8: 'success',   // 订单已结束
    9: 'danger',    // 顾客撤单
    10: 'danger',   // 司机撤单
    11: 'danger',   // 事故关闭
    12: 'info'      // 其他
  }
  return typeMap[status] || ''
}

// 查看订单详情
const handleDetail = async (row) => {
  try {
    loading.value = true
    const res = await getOrderByOrderNo(row.orderNo)
    if (res.data) {
      const order = res.data
      // 展示详情对话框
      ElMessageBox.alert(
        `<div class="order-detail">
          <p><strong>订单号：</strong>${order.orderNo || '暂无'}</p>
          <p><strong>客户ID：</strong>${order.customerId || '暂无'}</p>
          <p><strong>司机ID：</strong>${order.driverId || '暂无'}</p>
          <p><strong>起始地点：</strong>${order.startLocation || '暂无'}</p>
          <p><strong>结束地点：</strong>${order.endLocation || '暂无'}</p>
          <p><strong>预估金额：</strong>${order.expectAmount ? '¥' + order.expectAmount : '暂无'}</p>
          <p><strong>实际金额：</strong>${order.realAmount ? '¥' + order.realAmount : '暂无'}</p>
          <p><strong>预估里程：</strong>${order.expectDistance ? order.expectDistance + 'km' : '暂无'}</p>
          <p><strong>实际里程：</strong>${order.realDistance ? order.realDistance + 'km' : '暂无'}</p>
          <p><strong>订单状态：</strong>${getStatusText(order.status) || '暂无'}</p>
          <p><strong>接单时间：</strong>${order.acceptTime || '暂无'}</p>
          <p><strong>开始时间：</strong>${order.startServiceTime || '暂无'}</p>
          <p><strong>结束时间：</strong>${order.endServiceTime || '暂无'}</p>
          <p><strong>支付时间：</strong>${order.payTime || '暂无'}</p>
          <p><strong>车牌号：</strong>${order.carLicense || '暂无'}</p>
          <p><strong>车型：</strong>${order.carType || '暂无'}</p>
          <p><strong>备注：</strong>${order.remark || '暂无'}</p>
        </div>`,
        '订单详情',
        {
          dangerouslyUseHTMLString: true,
          confirmButtonText: '确定'
        }
      )
    } else {
      ElMessage.warning('未找到该订单详情')
    }
  } catch (error) {
    console.error('获取订单详情失败', error)
    ElMessage.error('获取订单详情失败')
  } finally {
    loading.value = false
  }
}

// 修改订单状态
const handleStatus = (row) => {
  // 定义一个临时状态变量
  const newStatus = ref(row.status === 1 ? 2 : row.status === 2 ? 3 : row.status === 3 ? 4 : 5);
  
  ElMessageBox.prompt('请输入新的订单状态 (1=待接单, 2=已接单, 3=进行中, 4=已完成, 5=已取消)', '修改订单状态', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputValue: newStatus.value,
    inputValidator: (value) => {
      const num = parseInt(value);
      return (num >= 1 && num <= 5) ? true : '请输入1-5之间的数字';
    }
  }).then(({ value }) => {
    const statusNum = parseInt(value);
    updateOrder({
      ...row,
      status: statusNum
    }).then(() => {
      ElMessage.success('状态修改成功');
      getList();
    }).catch(error => {
      ElMessage.error('状态修改失败');
    });
  }).catch(() => {
    // 取消操作
  });
}

// 查看订单反馈
const handleFeedback = async (row) => {
  try {
    // 使用订单号查询详细信息
    const orderDetail = await getOrderByOrderNo(row.orderNo)
    if (!orderDetail.data) {
      ElMessage.error('无法获取订单详情')
      return
    }
    
    // 从订单详情中获取ID (在你的示例中，可能是customerId，根据实际接口调整)
    // 由于反馈接口需要orderId，我们尝试使用多个可能的ID字段
    const orderId = orderDetail.data.id || orderDetail.data.orderId || orderDetail.data.customerId
    
    if (!orderId) {
      ElMessage.error('无法获取有效的订单ID')
      return
    }
    
    console.log('使用订单ID查询反馈:', orderId)
    const res = await getOrderFeedback(orderId)
    
    if (res.data) {
      ElMessageBox.alert(res.data, '订单反馈', {
        confirmButtonText: '确定'
      })
    } else {
      ElMessage.info('该订单暂无反馈信息')
    }
  } catch (error) {
    console.error('获取订单反馈失败', error)
    ElMessage.error('获取订单反馈失败: ' + (error.message || '未知错误'))
  }
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

/* 订单详情样式 */
:deep(.order-detail) {
  text-align: left;
  line-height: 1.8;
}

:deep(.order-detail p) {
  margin: 5px 0;
}

:deep(.order-detail strong) {
  display: inline-block;
  width: 90px;
  font-weight: bold;
}
</style> 