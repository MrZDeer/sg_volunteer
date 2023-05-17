import request from '@/utils/request'


// 查询申报列表
export function listDeclaration(query) {
  return request({
    url: '/content/declaration/list',
    method: 'get',
    params: query
  })
}

// 修改审核
export function updateDeclaration(data) {
  return request({
    url: '/content/declaration',
    method: 'put',
    data: data
  })
}
