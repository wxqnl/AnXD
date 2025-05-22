import request from '@/utils/request'

// 获取部门列表
export function getDeptTree(params) {
  // 确保必须参数存在
  const queryParams = {
    pageSize: params?.pageSize || 10,
    pageNum: params?.pageNum || 1,
    ...params
  }
  return request({
    url: '/sysDept/list',
    method: 'get',
    params: queryParams
  })
}

// 获取部门详情
export function getDeptById(id) {
  return request({
    url: '/sysDept/getById',
    method: 'get',
    params: { id }
  })
}

// 添加部门
export function addDept(data) {
  return request({
    url: '/sysDept/add',
    method: 'post',
    data
  })
}

// 更新部门
export function updateDept(data) {
  return request({
    url: '/sysDept/update',
    method: 'post',
    data
  })
}

// 删除部门
export function deleteDept(id) {
  return request({
    url: '/sysDept/delete',
    method: 'post',
    params: { id }
  })
} 