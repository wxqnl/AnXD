import request from '@/utils/request'
import qs from 'qs'

// 获取优惠券列表
export function getCouponList(params) {
  return request({
    url: '/coupon/getCouponInfo',
    method: 'post', 
    params
  })
}

// 获取优惠券详情
export function getCouponByName(name) {
  return request({
    url: '/coupon/getCouponInfoByName',
    method: 'get',
    params: { name }
  })
}

// 创建优惠券
export function createCoupon(data) {
  return request({
    url: '/coupon/addCouponInfo',
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    data
  })
}

// 更新优惠券
export function updateCoupon(data) {
  return request({
    url: '/coupon/updateCouponInfo',
    method: 'post', 
    headers: {
      'Content-Type': 'application/json'
    },
    data
  })
}

// 删除优惠券
export function deleteCoupon(id) {
  return request({
    url: '/coupon/removeCouponInfo',
    method: 'get',
    params: { id }
  })
}

// 启用/禁用优惠券
export function changeCouponStatus(data) {
  return request({
    url: '/coupon/updateCouponInfo',
    method: 'post',
    data
  })
}

// 获取客户优惠券列表
export function getCustomerCouponList(params) {
  return request({
    url: '/coupon/getCustomerCouponInfoByUserId',
    method: 'post',
    params
  })
} 