<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-card class="search-card">
      <el-form :model="queryParams" ref="queryForm" :inline="true" class="search-form">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="queryParams.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="queryParams.name" placeholder="请输入姓名" clearable />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="queryParams.phone" placeholder="请输入手机号" clearable />
        </el-form-item>
        <el-form-item label="部门" prop="deptName">
          <el-input v-model="queryParams.deptName" placeholder="请输入部门" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格工具栏 -->
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户列表</span>
        </div>
      </template>

      <!-- 表格区域 -->
      <el-table v-loading="loading" :data="userList" border>
        <el-table-column type="index" label="#" width="50" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="gender" label="性别">
          <template #default="scope">
            {{ scope.row.gender === 1 ? '男' : scope.row.gender === 2 ? '女' : '未知' }}
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="deptName" label="部门" />
        <el-table-column prop="postName" label="岗位" />
        <el-table-column prop="status" label="状态">
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
            <el-button type="info" link @click="viewLoginLog(scope.row)">登录日志</el-button>
            <el-button type="warning" link @click="viewOperLog(scope.row)">操作日志</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页区域 -->
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
    
    <!-- 用户编辑对话框 -->
    <el-dialog title="编辑用户" v-model="dialogVisible" width="500px">
      <el-form ref="userFormRef" :model="userForm" :rules="rules" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" placeholder="请输入用户名" disabled />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="userForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="userForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="部门" prop="deptId">
          <el-select v-model="userForm.deptId" placeholder="请选择部门">
            <el-option v-for="dept in deptOptions" :key="dept.id" :label="dept.name" :value="dept.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="岗位" prop="postId">
          <el-select v-model="userForm.postId" placeholder="请选择岗位">
            <el-option v-for="post in postOptions" :key="post.id" :label="post.name" :value="post.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="userForm.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="userForm.description" type="textarea" placeholder="请输入描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 登录日志对话框 -->
    <el-dialog title="用户登录日志" v-model="loginLogDialogVisible" width="800px">
      <el-table v-loading="logLoading" :data="loginLogList" border>
        <el-table-column type="index" label="#" width="50" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="ipaddr" label="IP地址" width="150" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.msg && scope.row.msg.includes('成功') ? 'success' : 'danger'">
              {{ scope.row.msg && scope.row.msg.includes('成功') ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="msg" label="消息" />
        <el-table-column prop="accessTime" label="访问时间" width="180" />
      </el-table>
      <!-- 登录日志分页区域 -->
      <el-pagination
        v-if="loginLogTotal > 0"
        :current-page="loginLogQuery.pageNum"
        :page-size="loginLogQuery.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="loginLogTotal"
        @size-change="handleLogSizeChange"
        @current-change="handleLogCurrentChange"
        class="pagination"
      />
    </el-dialog>

    <!-- 操作日志对话框 -->
    <el-dialog title="用户操作日志" v-model="operLogDialogVisible" width="900px">
      <el-table v-loading="operLogLoading" :data="operLogList" border>
        <el-table-column type="index" label="#" width="50" />
        <el-table-column prop="operName" label="操作人" width="120" />
        <el-table-column prop="deptName" label="部门" width="120" />
        <el-table-column prop="businessType" label="业务类型" width="100" />
        <el-table-column prop="operatorType" label="操作类型" width="100" />
        <el-table-column prop="method" label="请求方法" width="120" />
        <el-table-column prop="requestMethod" label="HTTP方法" width="100" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="(scope.row.status === 0 || (scope.row.msg && scope.row.msg.includes('成功'))) ? 'success' : 'danger'">
              {{ (scope.row.status === 0 || (scope.row.msg && scope.row.msg.includes('成功'))) ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operTime" label="操作时间" width="180" />
      </el-table>
      <!-- 操作日志分页区域 -->
      <el-pagination
        v-if="operLogTotal > 0"
        :current-page="operLogQuery.pageNum"
        :page-size="operLogQuery.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="operLogTotal"
        @size-change="handleOperLogSizeChange"
        @current-change="handleOperLogCurrentChange"
        class="pagination"
      />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllUsers, updateUser, deleteUser, getUserById, getUserLoginLog, getUserOperLog } from '@/api/user'
import { getDeptTree } from '@/api/dept'
import { getPostList } from '@/api/post'

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  username: undefined,
  name: undefined,
  phone: undefined,
  deptId: undefined,
  postId: undefined,
  status: undefined,
  deptName: undefined,
  postName: undefined,
})

// 表格数据和加载状态
const userList = ref([])
const loading = ref(false)
const total = ref(0)

// 部门和岗位选项
const deptOptions = ref([])
const postOptions = ref([])

// 编辑对话框相关
const dialogVisible = ref(false)
const userFormRef = ref(null)
const userForm = reactive({
  id: '',
  username: '',
  name: '',
  phone: '',
  deptId: undefined,
  postId: undefined,
  status: 1,
  description: ''
})

// 登录日志相关
const loginLogDialogVisible = ref(false)
const loginLogList = ref([])
const loginLogTotal = ref(0)
const logLoading = ref(false)
const loginLogQuery = reactive({
  pageNum: 1,
  pageSize: 10,
  username: '',
  minAccessTime: undefined,
  maxAccessTime: undefined
})

// 操作日志相关
const operLogDialogVisible = ref(false)
const operLogList = ref([])
const operLogTotal = ref(0)
const operLogLoading = ref(false)
const operLogQuery = reactive({
  pageNum: 1,
  pageSize: 10,
  operName: '',
  deptName: '',
  status: undefined,
  operatorType: undefined,
  businessType: undefined,
  requestMethod: undefined,
  minOperTime: undefined,
  maxOperTime: undefined
})

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

// 查询用户列表
const getList = async () => {
  try {
    loading.value = true
    const res = await getAllUsers(queryParams)
    console.log('用户列表响应:', res)
    
    // 获取原始用户列表
    const items = res.data.items || []
    
    // 将部门ID和岗位ID转换为对应的名称
    userList.value = items.map(user => {
      // 准备显示数据
      const displayUser = { ...user }
      
      // 如果没有部门名称，尝试从部门选项中获取
      if (!displayUser.deptName && displayUser.deptId && deptOptions.value.length > 0) {
        const dept = deptOptions.value.find(d => d.id == displayUser.deptId)
        if (dept) {
          displayUser.deptName = dept.name
        }
      }
      
      // 如果没有岗位名称，尝试从岗位选项中获取
      if (!displayUser.postName && displayUser.postId && postOptions.value.length > 0) {
        const post = postOptions.value.find(p => p.id == displayUser.postId)
        if (post) {
          displayUser.postName = post.name
        }
      }
      
      return displayUser
    })
    
    total.value = res.data.total || 0
    
    // 如果部门和岗位下拉选项尚未加载，主动加载它们
    if (deptOptions.value.length === 0 || postOptions.value.length === 0) {
      await Promise.all([
        getDeptOptions(),
        getPostOptions()
      ])
      
      // 重新处理用户列表以添加部门和岗位名称
      userList.value = userList.value.map(user => {
        const updatedUser = { ...user }
        
        if (!updatedUser.deptName && updatedUser.deptId) {
          const dept = deptOptions.value.find(d => d.id == updatedUser.deptId)
          if (dept) {
            updatedUser.deptName = dept.name
          }
        }
        
        if (!updatedUser.postName && updatedUser.postId) {
          const post = postOptions.value.find(p => p.id == updatedUser.postId)
          if (post) {
            updatedUser.postName = post.name
          }
        }
        
        return updatedUser
      })
    }
  } catch (error) {
    console.error('获取用户列表失败', error)
    ElMessage.error('获取用户列表失败: ' + (error.message || '未知错误'))
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
  // 重置查询表单
  queryParams.username = undefined
  queryParams.name = undefined
  queryParams.phone = undefined
  queryParams.deptName = undefined
  queryParams.pageNum = 1
  getList()
}

// 编辑用户
const handleEdit = async (row) => {
  try {
    console.log('开始编辑用户:', row);
    
    // 获取该用户的完整信息，确保有所有字段
    if (row.id) {
      // 可能需要先通过ID获取完整用户信息
      const userDetail = await getUserById(row.id);
      console.log('获取用户详细信息:', userDetail);
      
      if (userDetail) {
        // 填充表单数据
        userForm.id = userDetail.id || row.id;
        userForm.username = userDetail.username || row.username;
        userForm.name = userDetail.name || row.name;
        userForm.phone = userDetail.phone || row.phone;
        userForm.deptId = userDetail.deptId || row.deptId;
        userForm.postId = userDetail.postId || row.postId;
        userForm.status = userDetail.status !== undefined ? userDetail.status : row.status || 1;
        userForm.description = userDetail.description || row.description || '';
        // 保留可能需要的其他字段
        userForm.password = userDetail.password || '';
        userForm.headUrl = userDetail.headUrl || '';
      } else {
        // 如果获取详情失败，使用行数据填充
        Object.keys(userForm).forEach(key => {
          if (row[key] !== undefined) {
            userForm[key] = row[key];
          }
        });
      }
    } else {
      // 没有ID，直接使用行数据
      Object.keys(userForm).forEach(key => {
        if (row[key] !== undefined) {
          userForm[key] = row[key];
        }
      });
    }
    
    // 显示对话框
    dialogVisible.value = true;
    
    // 获取部门和岗位选项
    try {
      await Promise.all([
        getDeptOptions(),
        getPostOptions()
      ]);
    } catch (error) {
      console.error('获取选项数据失败', error);
      // 即使获取选项失败也继续显示对话框
    }
  } catch (error) {
    console.error('编辑用户时出错:', error);
    ElMessage.error('打开编辑对话框失败: ' + (error.message || '未知错误'));
  }
}

// 删除用户
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除用户 ${row.username} 吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteUser(row.id)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 获取部门选项
const getDeptOptions = async () => {
  try {
    const res = await getDeptTree()
    console.log('获取部门选项响应:', res)
    
    // 处理不同的数据格式
    if (res.data && res.data.items) {
      // 如果是分页格式
      deptOptions.value = flattenDeptTree(buildDeptTree(res.data.items))
    } else if (res.data && Array.isArray(res.data)) {
      // 如果直接是数组格式
      deptOptions.value = flattenDeptTree(res.data)
    } else {
      console.warn('未识别的部门数据格式')
      deptOptions.value = []
    }
  } catch (error) {
    console.error('获取部门选项失败', error)
    ElMessage.warning('获取部门选项失败，请稍后再试')
    deptOptions.value = []
  }
}

// 构建部门树结构
const buildDeptTree = (depts) => {
  // 创建映射表
  const deptMap = {}
  depts.forEach(dept => {
    dept.children = []
    deptMap[dept.id] = dept
  })
  
  // 构建树结构
  const result = []
  depts.forEach(dept => {
    if (dept.parentId && deptMap[dept.parentId]) {
      deptMap[dept.parentId].children.push(dept)
    } else {
      result.push(dept)
    }
  })
  
  return result
}

// 扁平化部门树结构
const flattenDeptTree = (deptTree) => {
  if (!deptTree || !Array.isArray(deptTree)) {
    console.warn('部门树结构无效', deptTree)
    return []
  }
  
  const result = []
  const flatten = (items) => {
    items.forEach(item => {
      if (item && item.id) {
        result.push({
          id: item.id,
          name: item.name
        })
        if (item.children && item.children.length > 0) {
          flatten(item.children)
        }
      }
    })
  }
  flatten(deptTree)
  return result
}

// 获取岗位选项
const getPostOptions = async () => {
  try {
    // 使用默认的分页参数
    const params = {
      pageSize: 100,
      pageNum: 1
    }
    
    const res = await getPostList(params)
    console.log('获取岗位选项响应:', res)
    
    // 处理不同的数据格式
    if (res.data && res.data.items) {
      // 分页格式
      postOptions.value = (res.data.items || []).map(item => ({
        id: item.id,
        name: item.name
      }))
    } else if (res.data && Array.isArray(res.data)) {
      // 直接是数组格式
      postOptions.value = res.data.map(item => ({
        id: item.id,
        name: item.name
      }))
    } else {
      console.warn('未识别的岗位数据格式')
      postOptions.value = []
    }
  } catch (error) {
    console.error('获取岗位选项失败', error)
    ElMessage.warning('获取岗位选项失败，请稍后再试')
    postOptions.value = []
  }
}

// 提交表单
const submitForm = () => {
  userFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        console.log('提交前的用户表单数据:', userForm);
        
        // 直接使用完整的userForm数据
        const result = await updateUser(userForm);
        console.log('更新用户返回结果:', result);
        
        // 根据后端返回的boolean判断结果
        if (result === true) {
          ElMessage.success('更新成功');
          dialogVisible.value = false;
          getList(); // 刷新列表
        } else {
          throw new Error('服务器返回失败状态');
        }
      } catch (error) {
        console.error('更新用户失败:', error);
        ElMessage.error('更新失败: ' + (error.message || '未知错误'));
      }
    }
  });
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

// 查看登录日志
const viewLoginLog = async (row) => {
  try {
    // 清空之前的日志数据，避免渲染错误
    loginLogList.value = [];
    loginLogTotal.value = 0;
    
    // 然后显示对话框
    loginLogDialogVisible.value = true;
    logLoading.value = true;
    
    // 显示对话框后再加载日志，以便用户看到加载状态
    loginLogQuery.username = row.username;
    loginLogQuery.pageNum = 1;
    loginLogQuery.pageSize = 10;
    console.log('准备查询用户登录日志:', row.username);
    
    // 使用setTimeout延迟执行，避免渲染冲突
    setTimeout(async () => {
      try {
        await getLoginLogList();
      } catch (err) {
        console.error('加载登录日志失败:', err);
        ElMessage.error('加载日志数据出错');
      } finally {
        logLoading.value = false;
      }
    }, 100);
  } catch (error) {
    console.error('显示登录日志对话框失败:', error);
    ElMessage.error('无法加载登录日志');
    loginLogDialogVisible.value = false;
    logLoading.value = false;
  }
}

// 获取登录日志列表
const getLoginLogList = async () => {
  try {
    console.log('发送登录日志查询参数:', loginLogQuery);
    
    // 简化请求参数
    const simplifiedParams = {
      pageNum: loginLogQuery.pageNum,
      pageSize: loginLogQuery.pageSize,
      username: loginLogQuery.username
    };
    
    const res = await getUserLoginLog(simplifiedParams);
    console.log('登录日志响应:', res);
    
    // 处理响应
    if (res && res.data) {
      // 确保数据为数组格式
      const items = Array.isArray(res.data.items) ? res.data.items : [];
      
      // 调试每条日志记录
      if (items.length > 0) {
        console.log('日志详细结构示例:', items[0]);
        console.log('所有日志状态值:');
        items.forEach((log, index) => {
          console.log(`日志 ${index+1} - status: ${log.status}, msg: ${log.msg}`);
        });
      }
      
      loginLogList.value = items;
      loginLogTotal.value = res.data.total || 0;
      
      if (loginLogList.value.length === 0) {
        ElMessage.info('该用户没有登录日志记录');
      }
    } else {
      loginLogList.value = [];
      loginLogTotal.value = 0;
      ElMessage.info('未获取到日志数据');
    }
  } catch (error) {
    console.error('获取登录日志失败', error);
    ElMessage.error('获取登录日志出错: ' + (error.message || '未知错误'));
    loginLogList.value = [];
    loginLogTotal.value = 0;
  }
}

// 登录日志分页操作
const handleLogSizeChange = (size) => {
  loginLogQuery.pageSize = size;
  logLoading.value = true;
  // 重新获取日志数据
  getLoginLogList().finally(() => {
    logLoading.value = false;
  });
}

const handleLogCurrentChange = (page) => {
  loginLogQuery.pageNum = page;
  logLoading.value = true;
  // 重新获取日志数据
  getLoginLogList().finally(() => {
    logLoading.value = false;
  });
}

// 查看操作日志
const viewOperLog = async (row) => {
  try {
    // 清空之前的日志数据，避免渲染错误
    operLogList.value = [];
    operLogTotal.value = 0;
    
    // 然后显示对话框
    operLogDialogVisible.value = true;
    operLogLoading.value = true;
    
    // 显示对话框后再加载日志
    operLogQuery.operName = row.username;
    operLogQuery.pageNum = 1;
    operLogQuery.pageSize = 10;
    console.log('准备查询用户操作日志:', row.username);
    
    // 使用setTimeout延迟执行，避免渲染冲突
    setTimeout(async () => {
      try {
        await getOperLogList();
      } catch (err) {
        console.error('加载操作日志失败:', err);
        ElMessage.error('加载日志数据出错');
      } finally {
        operLogLoading.value = false;
      }
    }, 100);
  } catch (error) {
    console.error('显示操作日志对话框失败:', error);
    ElMessage.error('无法加载操作日志');
    operLogDialogVisible.value = false;
    operLogLoading.value = false;
  }
}

// 获取操作日志列表
const getOperLogList = async () => {
  try {
    console.log('发送操作日志查询参数:', operLogQuery);
    
    // 简化请求参数
    const simplifiedParams = {
      pageNum: operLogQuery.pageNum,
      pageSize: operLogQuery.pageSize,
      operName: operLogQuery.operName
    };
    
    const res = await getUserOperLog(simplifiedParams);
    console.log('操作日志响应:', res);
    
    // 处理响应
    if (res && res.data) {
      // 确保数据为数组格式
      const items = Array.isArray(res.data.items) ? res.data.items : [];
      operLogList.value = items;
      operLogTotal.value = res.data.total || 0;
      
      if (operLogList.value.length === 0) {
        ElMessage.info('该用户没有操作日志记录');
      }
    } else {
      operLogList.value = [];
      operLogTotal.value = 0;
      ElMessage.info('未获取到日志数据');
    }
  } catch (error) {
    console.error('获取操作日志失败', error);
    ElMessage.error('获取操作日志出错: ' + (error.message || '未知错误'));
    operLogList.value = [];
    operLogTotal.value = 0;
  }
}

// 操作日志分页操作
const handleOperLogSizeChange = (size) => {
  operLogQuery.pageSize = size;
  operLogLoading.value = true;
  // 重新获取日志数据
  getOperLogList().finally(() => {
    operLogLoading.value = false;
  });
}

const handleOperLogCurrentChange = (page) => {
  operLogQuery.pageNum = page;
  operLogLoading.value = true;
  // 重新获取日志数据
  getOperLogList().finally(() => {
    operLogLoading.value = false;
  });
}

// 页面加载时获取数据
onMounted(async () => {
  try {
    // 先加载部门和岗位选项
    await Promise.all([
      getDeptOptions(),
      getPostOptions()
    ]);
    
    // 然后加载用户列表
    await getList();
  } catch (error) {
    console.error('页面初始化失败:', error);
    ElMessage.error('加载数据失败，请刷新页面重试');
  }
});
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

.search-form {
  display: flex;
  flex-wrap: wrap;
}

.el-pagination {
  margin-top: 20px;
  text-align: right;
}

.pagination {
  margin-top: 15px;
  text-align: right;
}
</style> 