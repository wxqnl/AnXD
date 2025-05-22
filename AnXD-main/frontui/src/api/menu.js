import request from '@/utils/request'

// 获取菜单树
export function getMenuTree() {
  return request({
    url: '/sysMenu/list',
    method: 'get',
    params: {
      pageNum: 1,
      pageSize: 100
    }
  })
}

// 获取菜单详情
export function getMenuById(id) {
  return request({
    url: '/sysMenu/getById',
    method: 'get',
    params: { id }
  })
}

// 添加菜单
export function addMenu(data) {
  return request({
    url: '/sysMenu/add',
    method: 'post',
    data
  })
}

// 更新菜单
export function updateMenu(data) {
  return request({
    url: '/sysMenu/update',
    method: 'post',
    data
  })
}

// 删除菜单
export function deleteMenu(id) {
  return request({
    url: '/sysMenu/delete',
    method: 'post',
    params: { id }
  })
} 