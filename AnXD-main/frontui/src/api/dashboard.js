import request from '@/utils/request'

// 获取仪表盘统计数据
export function getDashboardStats() {
  return request({
    url: '/dashboard/stats',
    method: 'get'
  })
}

// 获取司机总数
export function getDriverCount() {
  return request({
    url: '/driver/getDriverCount',
    method: 'get'
  })
}

// 获取客户总数
export function getCustomerCount() {
  return request({
    url: '/customer/getCustomerCount',
    method: 'get'
  })
}

// 获取订单总数
export function getOrderCount() {
  return request({
    url: '/order/getOrderCount',
    method: 'get'
  })
}

// 获取今日订单数
export function getTodayOrderCount() {
  return request({
    url: '/order/getTodayOrderCount',
    method: 'get'
  })
}

// 获取最近登录记录
export function getRecentLoginLogs() {
  return request({
    url: '/sysUser/getRecentLoginLog',
    method: 'get',
    params: {
      pageNum: 1,
      pageSize: 5
    }
  })
} 