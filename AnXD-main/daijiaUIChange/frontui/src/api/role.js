import request from '@/utils/request'

// 获取角色列表
export function getRoleList(params) {
  return request({
    url: '/sysRole/list',
    method: 'get',
    params
  })
}

// 获取角色详情
export function getRoleById(id) {
  return request({
    url: '/sysRole/getById',
    method: 'get',
    params: { id }
  })
}

// 添加角色
export function addRole(data) {
  return request({
    url: '/sysRole/add',
    method: 'post',
    data
  })
}

// 更新角色
export function updateRole(data) {
  return request({
    url: '/sysRole/update',
    method: 'post',
    data
  })
}

// 删除角色
export function deleteRole(id) {
  return request({
    url: '/sysRole/delete',
    method: 'post',
    params: { id }
  })
} 