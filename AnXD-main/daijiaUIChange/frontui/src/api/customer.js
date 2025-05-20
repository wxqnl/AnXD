import request from '@/utils/request'

// 获取客户信息列表
export function getAllCustomerInfo(params) {
  return request({
    url: '/customer/getAllCustomerInfo',
    method: 'post',
    params
  })
}

// 获取指定客户信息
export function getCustomerInfo(username) {
  return request({
    url: '/customer/getCustomerInfo',
    method: 'post',
    params: { username }
  })
}

// 新增客户
export function addCustomer(data) {
  return request({
    url: '/customer/add',
    method: 'post',
    params: data
  })
}

// 删除客户
export function removeCustomer(username) {
  return request({
    url: '/customer/remove',
    method: 'post',
    params: { username }
  })
}

// 更新客户信息
export function updateCustomer(data) {
  return request({
    url: '/customer/update',
    method: 'post',
    params: data
  })
}

// 获取客户登录日志
export function getCustomerLoginLog(username) {
  return request({
    url: '/customer/getCustomerLoginLog',
    method: 'post',
    params: { username },
    data: { username }
  })
} 