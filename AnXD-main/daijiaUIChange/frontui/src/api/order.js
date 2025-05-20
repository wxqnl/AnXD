import request from '@/utils/request'

// ========== 订单相关接口 ==========

// 获取订单列表
export function getOrderList(params) {
  return request({
    url: '/order/getOrderInfo',
    method: 'post',
    params
  })
}

// 获取订单详情
export function getOrderByOrderNo(orderNo) {
  return request({
    url: '/order/getByOrderNo',
    method: 'post',
    params: { orderNo }
  })
}

// 创建订单
export function createOrder(data) {
  return request({
    url: '/order/add',
    method: 'post',
    data
  })
}

// 更新订单
export function updateOrder(data) {
  return request({
    url: '/order/update',
    method: 'post',
    data
  })
}

// 删除订单
export function deleteOrder(orderNo) {
  return request({
    url: '/order/delete',
    method: 'post',
    params: { orderNo }
  })
}

// ========== 订单评价相关接口 ==========

// 获取订单评价列表
export function getOrderCommentList(params) {
  return request({
    url: '/orderComment/list',
    method: 'post',
    params
  })
}

// 根据订单ID获取评价
export function getCommentByOrderId(orderId) {
  return request({
    url: '/orderComment/getByOrderId',
    method: 'get',
    params: { orderId }
  })
}

// 添加订单评价
export function addComment(data) {
  return request({
    url: '/orderComment/add',
    method: 'post',
    data
  })
}

// 更新订单评价
export function updateComment(data) {
  return request({
    url: '/orderComment/update',
    method: 'post',
    data
  })
}

// 删除订单评价
export function deleteComment(orderId) {
  return request({
    url: '/orderComment/delete',
    method: 'post',
    params: { orderId }
  })
}

// 获取订单反馈信息
export function getOrderFeedback(orderId) {
  return request({
    url: '/feedback/getFeed',
    method: 'get',
    params: { orderId }
  })
} 