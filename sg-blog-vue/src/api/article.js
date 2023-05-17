import request from '@/utils/request'

// 查询文章列表
export function articleList(query) {
    return request({
        url: '/article/articleList',
        method: 'get',
        headers: {
          isToken: false
        },
        params: query
    })
}

export function articleListByUserId(query) {
  return request({
    url: '/article/articleListByUserId',
    method: 'get',
    headers: {
      isToken: false
    },
    params: query
  })
}

//查询最热文章
export function hotArticleList() {
    return request({
        url: '/article/hotArticleList',
        headers: {
          isToken: false
        },
        method: 'get'
    })
}

//获取文章详情
export function getArticle(articleId) {
    return request({
        url: '/article/' + articleId,
        headers: {
          isToken: false
        },
        method: 'get'
    })
}
export function getArticleUser(articleId) {
  return request({
    url: '/article/articleUser/' + articleId,
    headers: {
      isToken: false
    },
    method: 'get'
  })
}
export function checkApply(articleId,userId) {
  return request({
    url: '/article/checkArticleUser/',
    headers: {
      isToken: false
    },
    method: 'get',
    params: {"articleId":articleId,"userId":userId}
  })
}
export function addArticleUser(articleId,userId) {
  return request({
    url: '/article/addVolunteer',
    headers: {
      isToken: false
    },
    method: 'post',
    data: {'articleId':articleId,"userId":userId}
  })
}

export function updateViewCount(articleId) {
    return request({
        url: '/article/updateViewCount/' + articleId,
        headers: {
          isToken: false
        },
        method: 'put'
    })

}
