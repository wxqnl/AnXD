import request from '@/utils/request'

// 获取司机信息列表
export function getDriverList(params) {
  return request({
    url: '/driver/getDriverInfo',
    method: 'get',
    params
  })
}

// 根据姓名获取司机信息
export function getDriverByName(name) {
  return request({
    url: '/driver/getDriverInfoByName',
    method: 'get',
    params: { name }
  })
}

// 根据工号获取司机信息
export function getDriverByJobNo(jobNo) {
  return request({
    url: '/driver/getDriverInfoByJobNo',
    method: 'get',
    params: { jobNo }
  })
}

// 添加司机信息
export function addDriver(data) {
  return request({
    url: '/driver/addDriverInfo',
    method: 'get',
    params: data
  })
}

// 更新司机信息
export function updateDriver(data) {
  return request({
    url: '/driver/updateDriverInfo',  
    method: 'get',
    params: data
  })
}

// 删除司机信息
export function removeDriver(name) {
  return request({
    url: '/driver/removeDriverInfo',
    method: 'get',
    params: { name }
  })
}

// 获取司机登录日志
export function getDriverLoginLog(name) {
  return request({
    url: '/driver/getDriverLoginLog',
    method: 'get',
    params: { name }
  })
}

// 获取司机账单详情
export function getDriverAccountDetail(params) {
  return request({
    url: '/driver/getDriverAccountDetail',
    method: 'get',
    params: {
      ...params,
      driverId: params.dirverId
    }
  })
}

// 获取司机账户信息(总收入等)
export function getDriverAccount(driverId) {
  return request({
    url: '/driver/getDriverAccount',
    method: 'get',
    params: { driverId }
  })
} 