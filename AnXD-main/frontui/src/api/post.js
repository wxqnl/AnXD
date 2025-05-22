import request from '@/utils/request'

// 获取岗位列表
export function getPostList(params) {
  return request({
    url: '/sysPost/list',
    method: 'get',
    params
  })
}

// 获取岗位详情
export function getPostById(id) {
  return request({
    url: '/sysPost/getById',
    method: 'get',
    params: { id }
  })
}

// 添加岗位
export function addPost(data) {
  return request({
    url: '/sysPost/add',
    method: 'post',
    data
  })
}

// 更新岗位
export function updatePost(data) {
  return request({
    url: '/sysPost/update',
    method: 'post',
    data
  })
}

// 删除岗位
export function deletePost(id) {
  return request({
    url: '/sysPost/delete',
    method: 'post',
    params: { id }
  })
}