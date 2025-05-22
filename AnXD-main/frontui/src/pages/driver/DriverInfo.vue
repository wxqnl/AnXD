<template>
  <div class="app-container">
    <el-card class="search-card">
      <el-form :model="queryParams" ref="queryForm" :inline="true" class="search-form">
        <el-form-item label="司机姓名" prop="name">
          <el-input v-model="queryParams.name" placeholder="请输入司机姓名" clearable />
        </el-form-item>
        <el-form-item label="工号" prop="jobNo">
          <el-input v-model="queryParams.jobNo" placeholder="请输入工号" clearable />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="queryParams.phone" placeholder="请输入手机号" clearable />
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
          <span>司机列表</span>
        </div>
      </template>
      
      <el-table v-loading="loading" :data="driverList" border>
        <el-table-column type="index" label="#" width="50" />
        <el-table-column prop="name" label="司机姓名" width="100" />
        <el-table-column prop="jobNo" label="工号" width="100" />
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column prop="gender" label="性别" width="60" />
        <el-table-column prop="nickname" label="昵称" width="100" />
        <el-table-column prop="driverLicenseClazz" label="准驾车型" width="80" />
        <el-table-column prop="score" label="评分" width="80">
          <template #default="scope">
            {{ scope.row.score || '暂无' }}
          </template>
        </el-table-column>
        <el-table-column prop="orderCount" label="订单量" width="80">
          <template #default="scope">
            {{ scope.row.orderCount || 0 }}
          </template>
        </el-table-column>
        <el-table-column prop="authStatus" label="认证状态" width="100">
          <template #default="scope">
            <el-tag :type="getAuthStatusType(scope.row.authStatus)">
              {{ getAuthStatusName(scope.row.authStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '正常' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="350">
          <template #default="scope">
            <el-button type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(scope.row)">删除</el-button>
            <el-button type="info" link @click="viewLog(scope.row)">查看日志</el-button>
            <el-button type="success" link @click="viewAccount(scope.row)">查看账户</el-button>
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

    <!-- 编辑司机对话框 -->
    <el-dialog 
      title="司机审核" 
      v-model="dialogVisible" 
      width="800px" 
      append-to-body
      :close-on-click-modal="false"
    >
      <el-form 
        ref="driverFormRef" 
        :model="driverForm" 
        :rules="rules" 
        label-width="120px"
      >
        <el-tabs v-model="editActiveTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="司机姓名" prop="name">
                  <el-input v-model="driverForm.name" disabled />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="手机号码" prop="phone">
                  <el-input v-model="driverForm.phone" disabled />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="身份证号" prop="idcardNo">
                  <el-input v-model="driverForm.idcardNo" disabled />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="驾驶证号" prop="driverLicenseNo">
                  <el-input v-model="driverForm.driverLicenseNo" disabled />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-divider content-position="center">审核信息</el-divider>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="认证状态" prop="authStatus">
                  <el-select v-model="driverForm.authStatus" placeholder="请选择认证状态">
                    <el-option label="未认证" :value="0" />
                    <el-option label="已认证" :value="1" />
                    <el-option label="认证中" :value="2" />
                    <el-option label="认证失败" :value="3" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="账户状态" prop="status">
                  <el-radio-group v-model="driverForm.status">
                    <el-radio :value="1">正常</el-radio>
                    <el-radio :value="0">停用</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-form-item label="审核备注" prop="auditRemark">
              <el-input 
                v-model="driverForm.auditRemark" 
                type="textarea" 
                placeholder="请输入审核备注信息，如拒绝原因等" 
                rows="3"
              />
            </el-form-item>
          </el-tab-pane>
          
          <el-tab-pane label="身份证照片" name="idcard">
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="image-container">
                  <p class="image-title">身份证正面</p>
                  <el-image 
                    v-if="driverForm.idcardFrontUrl" 
                    :src="driverForm.idcardFrontUrl" 
                    fit="contain"
                    :preview-src-list="[driverForm.idcardFrontUrl]"
                  />
                  <div v-else class="no-image">未上传</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="image-container">
                  <p class="image-title">身份证背面</p>
                  <el-image 
                    v-if="driverForm.idcardBackUrl" 
                    :src="driverForm.idcardBackUrl" 
                    fit="contain"
                    :preview-src-list="[driverForm.idcardBackUrl]"
                  />
                  <div v-else class="no-image">未上传</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="image-container">
                  <p class="image-title">手持身份证</p>
                  <el-image 
                    v-if="driverForm.idcardHandUrl" 
                    :src="driverForm.idcardHandUrl" 
                    fit="contain"
                    :preview-src-list="[driverForm.idcardHandUrl]"
                  />
                  <div v-else class="no-image">未上传</div>
                </div>
              </el-col>
            </el-row>
          </el-tab-pane>
          
          <el-tab-pane label="驾驶证照片" name="license">
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="image-container">
                  <p class="image-title">驾驶证正面</p>
                  <el-image 
                    v-if="driverForm.driverLicenseFrontUrl" 
                    :src="driverForm.driverLicenseFrontUrl" 
                    fit="contain"
                    :preview-src-list="[driverForm.driverLicenseFrontUrl]"
                  />
                  <div v-else class="no-image">未上传</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="image-container">
                  <p class="image-title">行驶证副页</p>
                  <el-image 
                    v-if="driverForm.driverLicenseBackUrl" 
                    :src="driverForm.driverLicenseBackUrl" 
                    fit="contain"
                    :preview-src-list="[driverForm.driverLicenseBackUrl]"
                  />
                  <div v-else class="no-image">未上传</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="image-container">
                  <p class="image-title">手持驾驶证</p>
                  <el-image 
                    v-if="driverForm.driverLicenseHandUrl" 
                    :src="driverForm.driverLicenseHandUrl" 
                    fit="contain"
                    :preview-src-list="[driverForm.driverLicenseHandUrl]"
                  />
                  <div v-else class="no-image">未上传</div>
                </div>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancelForm">取消</el-button>
          <el-button type="primary" @click="submitForm">提交审核</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 登录日志对话框 -->
    <el-dialog title="司机登录日志" v-model="logDialogVisible" width="800px">
      <div v-if="logData">
        <el-table :data="[logData]" border style="width: 100%">
          <el-table-column prop="driverId" label="司机ID" width="100" />
          <el-table-column prop="msg" label="登录消息" width="150" />
          <el-table-column prop="ipaddr" label="IP地址" width="150">
            <template #default="scope">
              {{ scope.row.ipaddr || '未记录' }}
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status ? 'success' : 'info'">
                {{ scope.row.status ? '成功' : '小程序登录' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="登录时间" width="180">
            <template #default="scope">
              {{ formatDateTime(scope.row.createTime) }}
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div v-else-if="logList && logList.length > 0">
        <el-table :data="logList" border style="width: 100%">
          <el-table-column prop="driverId" label="司机ID" width="100" />
          <el-table-column prop="msg" label="登录消息" width="150" />
          <el-table-column prop="ipaddr" label="IP地址" width="150">
            <template #default="scope">
              {{ scope.row.ipaddr || '未记录' }}
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status ? 'success' : 'info'">
                {{ scope.row.status ? '成功' : '小程序登录' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="登录时间" width="180">
            <template #default="scope">
              {{ formatDateTime(scope.row.createTime) }}
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div v-else class="empty-data">
        暂无登录日志数据
      </div>
    </el-dialog>

    <!-- 账户信息对话框 -->
    <el-dialog title="司机账户信息" v-model="accountDialogVisible" width="700px">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="账户概览" name="overview">
          <div v-if="accountData">
            <el-descriptions :column="2" border>
              <el-descriptions-item label="司机ID">{{ accountData.driverId }}</el-descriptions-item>
              <el-descriptions-item label="司机姓名">{{ accountData.driverName || '未知' }}</el-descriptions-item>
              <el-descriptions-item label="总收入">
                <span class="income">¥ {{ accountData.totalIncome || '0.00' }}</span>
              </el-descriptions-item>
              <el-descriptions-item label="可提现金额">
                <span class="withdraw">¥ {{ accountData.availableAmount || '0.00' }}</span>
              </el-descriptions-item>
              <el-descriptions-item label="冻结金额">¥ {{ accountData.frozenAmount || '0.00' }}</el-descriptions-item>
              <el-descriptions-item label="已提现金额">¥ {{ accountData.withdrawAmount || '0.00' }}</el-descriptions-item>
              <el-descriptions-item label="账户状态" :span="2">
                <el-tag :type="accountData.status === 1 ? 'success' : 'danger'">
                  {{ accountData.status === 1 ? '正常' : '冻结' }}
                </el-tag>
              </el-descriptions-item>
            </el-descriptions>
          </div>
          <div v-else class="empty-data">
            暂无账户信息数据
          </div>
        </el-tab-pane>
        <el-tab-pane label="账单明细" name="detail">
          <div v-if="accountDetailError" class="error-message">
            <el-alert title="获取账单明细失败" type="error" :closable="false" show-icon>
              <p>{{ accountDetailError }}</p>
              <el-button size="small" type="primary" @click="retryGetAccountDetail">重试</el-button>
            </el-alert>
          </div>
          <el-table v-else v-loading="detailLoading" :data="accountDetailList" border>
            <el-table-column type="index" label="#" width="50" />
            <el-table-column prop="tradeNo" label="交易号" width="180" />
            <el-table-column prop="tradeType" label="交易类型">
              <template #default="scope">
                <el-tag :type="getTradeTypeTag(scope.row.tradeType)">
                  {{ getTradeTypeName(scope.row.tradeType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="amount" label="金额">
              <template #default="scope">
                <span :class="scope.row.amount > 0 ? 'income-amount' : 'expense-amount'">
                  {{ scope.row.amount > 0 ? '+' : '' }}{{ scope.row.amount }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="交易时间" width="160" />
            <el-table-column prop="remark" label="备注" />
          </el-table>
          
          <el-pagination
            v-if="!accountDetailError && accountDetailTotal > 0"
            v-model:current-page="accountDetailQuery.pageNum"
            v-model:page-size="accountDetailQuery.pageSize"
            :page-sizes="[5, 10, 20, 50]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="accountDetailTotal"
            @size-change="handleDetailSizeChange"
            @current-change="handleDetailCurrentChange"
            class="detail-pagination"
          />
          <div v-else-if="!accountDetailError && accountDetailList.length === 0" class="empty-data">
            暂无交易明细数据
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>

    <!-- 司机详情对话框 -->
    <el-dialog title="司机详情" v-model="detailDialogVisible" width="800px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="司机姓名">{{ detailForm.name || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="昵称">{{ detailForm.nickname || '未设置' }}</el-descriptions-item>
        <el-descriptions-item label="工号">{{ detailForm.jobNo || '未分配' }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ detailForm.phone || '未设置' }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ detailForm.gender || '未知' }}</el-descriptions-item>
        <el-descriptions-item label="生日">{{ formatDate(detailForm.birthday) || '未设置' }}</el-descriptions-item>
        <el-descriptions-item label="订单数量">{{ detailForm.orderCount || 0 }}</el-descriptions-item>
        <el-descriptions-item label="评分">{{ detailForm.score || '暂无评分' }}</el-descriptions-item>
      </el-descriptions>
      
      <el-divider content-position="center">证件信息</el-divider>
      
      <el-descriptions :column="2" border>
        <el-descriptions-item label="身份证号">{{ detailForm.idcardNo || '未设置' }}</el-descriptions-item>
        <el-descriptions-item label="身份证有效期">{{ formatDate(detailForm.idcardExpire) || '未设置' }}</el-descriptions-item>
        <el-descriptions-item label="身份证地址" :span="2">{{ detailForm.idcardAddress || '未设置' }}</el-descriptions-item>
        <el-descriptions-item label="驾驶证号">{{ detailForm.driverLicenseNo || '未设置' }}</el-descriptions-item>
        <el-descriptions-item label="准驾车型">{{ detailForm.driverLicenseClazz || '未设置' }}</el-descriptions-item>
        <el-descriptions-item label="驾驶证有效期">{{ formatDate(detailForm.driverLicenseExpire) || '未设置' }}</el-descriptions-item>
        <el-descriptions-item label="驾驶证初领日期">{{ formatDate(detailForm.driverLicenseIssueDate) || '未设置' }}</el-descriptions-item>
      </el-descriptions>
      
      <el-divider content-position="center">紧急联系人</el-divider>
      
      <el-descriptions :column="2" border>
        <el-descriptions-item label="联系人姓名">{{ detailForm.contactName || '未设置' }}</el-descriptions-item>
        <el-descriptions-item label="联系人电话">{{ detailForm.contactPhone || '未设置' }}</el-descriptions-item>
        <el-descriptions-item label="联系人关系">{{ detailForm.contactRelationship || '未设置' }}</el-descriptions-item>
        <el-descriptions-item label="微信OpenID">{{ detailForm.wxOpenId || '未关联' }}</el-descriptions-item>
      </el-descriptions>
      
      <el-divider content-position="center">状态信息</el-divider>
      
      <el-descriptions :column="2" border>
        <el-descriptions-item label="认证状态">
          <el-tag :type="getAuthStatusType(detailForm.authStatus)">
            {{ getAuthStatusName(detailForm.authStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="账户状态">
          <el-tag :type="detailForm.status === 1 ? 'success' : 'danger'">
            {{ detailForm.status === 1 ? '正常' : '停用' }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  getDriverList, 
  updateDriver, 
  removeDriver, 
  getDriverLoginLog, 
  getDriverByName,
  getDriverAccountDetail,
  getDriverAccount
} from '@/api/driver'

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: '',
  jobNo: '',
  phone: '',
  status: undefined
})

// 表格数据和状态
const driverList = ref([])
const loading = ref(false)
const total = ref(0)

// 弹窗相关
const dialogVisible = ref(false)
const driverFormRef = ref(null)
const editActiveTab = ref('basic')
const driverForm = reactive({
  name: '',
  nickname: '',
  jobNo: '',
  phone: '',
  gender: '',
  birthday: '',
  idcardNo: '',
  idcardAddress: '',
  idcardExpire: '',
  idcardFrontUrl: '', // 身份证正面
  idcardBackUrl: '',  // 身份证背面
  idcardHandUrl: '',  // 手持身份证
  driverLicenseNo: '',
  driverLicenseClazz: '',
  driverLicenseExpire: '',
  driverLicenseIssueDate: '',
  driverLicenseFrontUrl: '', // 驾驶证正面
  driverLicenseBackUrl: '',  // 行驶证副页
  driverLicenseHandUrl: '',  // 手持驾驶证
  contactName: '',
  contactPhone: '',
  contactRelationship: '',
  authStatus: 0,
  status: 1,
  auditRemark: '' // 审核备注
})

// 日志查看相关
const logDialogVisible = ref(false)
const logData = ref(null)
const logList = ref([])

// 账户信息相关
const accountDialogVisible = ref(false)
const accountData = ref(null)
const activeTab = ref('overview')
const accountDetailList = ref([])
const accountDetailTotal = ref(0)
const detailLoading = ref(false)
const currentDriverId = ref('')
const accountDetailError = ref('')

// 账单查询参数
const accountDetailQuery = reactive({
  pageNum: 1,
  pageSize: 10,
  dirverId: '',
  tradeType: '',
  tradeNo: '',
  minAmount: undefined,
  maxAmount: undefined
})

// 司机详情相关
const detailDialogVisible = ref(false)
const detailForm = reactive({})

// 表单验证规则
const rules = {
  authStatus: [
    { required: true, message: '请选择认证状态', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// 获取司机列表
const getList = async () => {
  try {
    loading.value = true
    const res = await getDriverList(queryParams)
    console.log('司机列表响应:', res)
    driverList.value = res.data.items || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('获取司机列表失败', error)
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
  queryParams.jobNo = ''
  queryParams.phone = ''
  queryParams.status = undefined
  queryParams.pageNum = 1
  getList()
}

// 重置表单
const resetForm = () => {
  driverForm.name = ''
  driverForm.nickname = ''
  driverForm.jobNo = ''
  driverForm.phone = ''
  driverForm.gender = ''
  driverForm.birthday = ''
  driverForm.idcardNo = ''
  driverForm.idcardAddress = ''
  driverForm.idcardExpire = ''
  driverForm.idcardFrontUrl = ''
  driverForm.idcardBackUrl = ''
  driverForm.idcardHandUrl = ''
  driverForm.driverLicenseNo = ''
  driverForm.driverLicenseClazz = ''
  driverForm.driverLicenseExpire = ''
  driverForm.driverLicenseIssueDate = ''
  driverForm.driverLicenseFrontUrl = ''
  driverForm.driverLicenseBackUrl = ''
  driverForm.driverLicenseHandUrl = ''
  driverForm.contactName = ''
  driverForm.contactPhone = ''
  driverForm.contactRelationship = ''
  driverForm.authStatus = 0
  driverForm.status = 1
  driverForm.auditRemark = ''
  editActiveTab.value = 'basic'
  nextTick(() => {
    driverFormRef.value?.resetFields()
  })
}

// 编辑司机
const handleEdit = async (row) => {
  resetForm()
  dialogVisible.value = true
  
  // 获取最新的司机信息
  try {
    const res = await getDriverByName(row.name)
    if (res.data) {
      // 填充表单数据前处理日期格式
      const driverData = { ...res.data }
      if (driverData.birthday) {
        driverData.birthday = formatDate(driverData.birthday)
      }
      if (driverData.idcardExpire) {
        driverData.idcardExpire = formatDate(driverData.idcardExpire)
      }
      if (driverData.driverLicenseExpire) {
        driverData.driverLicenseExpire = formatDate(driverData.driverLicenseExpire)
      }
      if (driverData.driverLicenseIssueDate) {
        driverData.driverLicenseIssueDate = formatDate(driverData.driverLicenseIssueDate)
      }
      
      // 填充表单数据
      Object.assign(driverForm, driverData)
    }
  } catch (error) {
    ElMessage.error('获取司机信息失败')
  }
}

// 删除司机
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除司机 ${row.name} 吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await removeDriver(row.name)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 查看日志
const viewLog = async (row) => {
  try {
    logDialogVisible.value = true
    logData.value = null
    logList.value = []
    
    const res = await getDriverLoginLog(row.name)
    console.log('登录日志响应:', res)
    
    if (res.data) {
      // 处理后端返回的数据，后端可能返回单个对象或数组
      if (Array.isArray(res.data)) {
        logList.value = res.data.map(item => ({
          ...item,
          driverId: item.driver_id || item.driverId,
          createTime: item.create_time || item.createTime
        }))
      } else if (Array.isArray(res.data.items)) {
        // 如果返回的是分页数据
        logList.value = res.data.items.map(item => ({
          ...item,
          driverId: item.driver_id || item.driverId,
          createTime: item.create_time || item.createTime
        }))
      } else {
        // 单个对象情况
        logData.value = {
          ...res.data,
          driverId: res.data.driver_id || res.data.driverId,
          createTime: res.data.create_time || res.data.createTime
        }
      }
    }
  } catch (error) {
    console.error('获取登录日志失败', error)
    ElMessage.error('获取登录日志失败')
  }
}

// 查看账户信息
const viewAccount = async (row) => {
  accountDialogVisible.value = true
  activeTab.value = 'overview'
  currentDriverId.value = row.id || row.driverId
  
  // 获取账户概览
  try {
    const res = await getDriverAccount(currentDriverId.value)
    accountData.value = res.data
  } catch (error) {
    console.error('获取账户信息失败', error)
    ElMessage.error('获取账户信息失败')
    accountData.value = null
  }
  
  // 账单查询参数设置
  accountDetailQuery.dirverId = currentDriverId.value
  
  // 获取账单明细
  getAccountDetail()
}

// 获取账单明细
const getAccountDetail = async () => {
  try {
    detailLoading.value = true
    accountDetailError.value = ''
    console.log('账单查询参数:', accountDetailQuery) // 调试日志
    const res = await getDriverAccountDetail(accountDetailQuery)
    if (res.data) {
      accountDetailList.value = res.data.items || []
      accountDetailTotal.value = res.data.total || 0
    } else {
      accountDetailList.value = []
      accountDetailTotal.value = 0
      accountDetailError.value = '服务器返回数据为空'
    }
  } catch (error) {
    console.error('获取账单明细失败', error)
    accountDetailList.value = []
    accountDetailTotal.value = 0
    accountDetailError.value = error.message || '获取账单明细数据失败'
  } finally {
    detailLoading.value = false
  }
}

// 重试获取账单明细
const retryGetAccountDetail = () => {
  getAccountDetail()
}

// 账单分页操作
const handleDetailSizeChange = (size) => {
  accountDetailQuery.pageSize = size
  getAccountDetail()
}

const handleDetailCurrentChange = (page) => {
  accountDetailQuery.pageNum = page
  getAccountDetail()
}

// 交易类型标签颜色
const getTradeTypeTag = (type) => {
  const typeMap = {
    '1': 'success', // 订单收入
    '2': 'danger',  // 提现
    '3': 'warning', // 退款
    '4': 'info'     // 其他
  }
  return typeMap[type] || 'info'
}

// 交易类型名称
const getTradeTypeName = (type) => {
  const typeMap = {
    '1': '订单收入',
    '2': '提现',
    '3': '退款',
    '4': '其他'
  }
  return typeMap[type] || '未知类型'
}

// 取消表单
const cancelForm = () => {
  dialogVisible.value = false
  resetForm()
}

// 提交表单
const submitForm = async () => {
  driverFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 创建一个新对象，只包含需要更新的字段
        const updateData = {
          name: driverForm.name,
          nickname: driverForm.nickname || '',
          avatarUrl: driverForm.avatarUrl || '',
          phone: driverForm.phone || '',
          gender: driverForm.gender || '',
          score: driverForm.score || 0,
          status: driverForm.status,
          authStatus: driverForm.authStatus
        }
        
        console.log('提交的司机数据:', updateData)
        
        // 编辑司机
        const res = await updateDriver(updateData)
        ElMessage.success('修改司机信息成功')
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

// 格式化日期时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return '未记录';
  
  try {
    // 处理ISO格式时间
    const date = new Date(dateTime);
    if (isNaN(date.getTime())) return dateTime; // 如果无法解析则返回原始值
    
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
      hour12: false
    }).replace(/\//g, '-');
  } catch (e) {
    console.error('日期格式化错误', e);
    return dateTime;
  }
}

// 获取认证状态类型
const getAuthStatusType = (status) => {
  const typeMap = {
    0: 'info',     // 未认证
    1: 'success',  // 已认证
    2: 'warning',  // 认证中
    3: 'danger'    // 认证失败
  }
  return typeMap[status] || 'info'
}

// 获取认证状态名称
const getAuthStatusName = (status) => {
  const nameMap = {
    0: '未认证',
    1: '已认证',
    2: '认证中',
    3: '认证失败'
  }
  return nameMap[status] || '未知'
}

// 格式化日期
const formatDate = (date) => {
  if (!date) return '';
  
  if (typeof date === 'string' && date.includes('T')) {
    // 处理ISO格式日期
    return date.split('T')[0];
  }
  
  if (date instanceof Date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }
  
  return date;
}

// 查看司机详情
const viewDetail = async (row) => {
  try {
    detailDialogVisible.value = true
    const res = await getDriverByName(row.name)
    if (res.data) {
      Object.assign(detailForm, res.data)
    }
  } catch (error) {
    console.error('获取司机详情失败', error)
    ElMessage.error('获取司机详情失败')
  }
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

.empty-data {
  text-align: center;
  color: #909399;
  padding: 30px 0;
}

.income {
  color: #67C23A;
  font-weight: bold;
}

.withdraw {
  color: #409EFF;
  font-weight: bold;
}

.income-amount {
  color: #67C23A;
}

.expense-amount {
  color: #F56C6C;
}

.detail-pagination {
  margin-top: 15px;
}

.error-message {
  margin-bottom: 15px;
}

.image-container {
  margin-bottom: 20px;
  text-align: center;
}

.image-title {
  margin-bottom: 10px;
  font-weight: bold;
}

.no-image {
  height: 200px;
  background-color: #f5f7fa;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #909399;
  border: 1px dashed #d9d9d9;
  border-radius: 4px;
}

.el-image {
  width: 100%;
  height: 200px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
}
</style> 