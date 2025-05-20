import request from '@/utils/request'
import qs from 'qs'
// 登录接口
export function login(data) {
  return request({
    url: '/securityLogin/login',
    method: 'post',
    data: qs.stringify(data),
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  })
}

// 获取用户信息
export function getUserInfo() {
  return request({
    url: '/securityLogin/getUserInfo',
    method: 'get'
  })
}

// 获取所有用户
export function getAllUsers(params) {
  return request({
    url: '/sysUser/SearchAllUser',
    method: 'get',
    params
  })
}

// 根据ID获取用户
export function getUserById(id) {
  return request({
    url: `/sysUser/${id}`,
    method: 'get'
  })
}

// 创建用户
export function createUser(data) {
  return request({
    url: '/sysUser',
    method: 'post',
    data
  })
}

// 更新用户
export function updateUser(data) {
  // 确保数据结构完全符合后端SysUser对象的期望
  const userData = {
    // 确保id为字符串类型(String)
    id: data.id ? String(data.id) : '',
    username: data.username || '',
    password: data.password || '',
    name: data.name || '',
    phone: data.phone || '',
    headUrl: data.headUrl || '',
    // 确保deptId为长整型(Long)
    deptId: data.deptId ? Number(data.deptId) : null,
    // 确保postId为长整型(Long)
    postId: data.postId ? Number(data.postId) : null,
    description: data.description || '',
    // 确保status为整型(Integer)
    status: data.status !== undefined ? Number(data.status) : 1
  };
  
  // 移除null值字段
  Object.keys(userData).forEach(key => {
    if (userData[key] === null || userData[key] === undefined) {
      delete userData[key];
    }
  });
  
  console.log('发送严格格式化的用户数据:', userData);
  
  return request({
    url: '/sysUser',
    method: 'put',
    data: userData,
    headers: {
      'Content-Type': 'application/json'
    },
    timeout: 20000
  });
}

// 删除用户
export function deleteUser(id) {
  return request({
    url: `/sysUser/${id}`,
    method: 'delete'
  })
}

// 获取用户登录日志
export function getUserLoginLog(params) {
  // 简化请求参数
  const requestParams = {
    pageNum: 1,
    pageSize: 10
  };
  
  // 仅添加用户名参数
  if (params.username) {
    requestParams.username = params.username;
  }
  
  console.log('登录日志请求参数:', requestParams);
  
  return request({
    url: '/sysUser/getUserLoginLog',
    method: 'post',
    params: requestParams
  });
}

// 获取用户操作日志
export function getUserOperLog(params) {
  // 简化请求参数
  const requestParams = {
    pageNum: 1,
    pageSize: 10
  };
  
  // 仅添加操作人名称
  if (params.operName) {
    requestParams.operName = params.operName;
  }
  
  console.log('操作日志请求参数:', requestParams);
  
  return request({
    url: '/sysUser/getUserOperLog',
    method: 'post',
    params: requestParams
  });
} 