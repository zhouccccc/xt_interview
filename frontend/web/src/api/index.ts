import request from '@/utils/request'

// 认证相关 API
export const authApi = {
  login(data: { username: string; password: string }) {
    return request.post('/auth/login', data)
  },
  logout() {
    return request.post('/auth/logout')
  },
  validate() {
    return request.get('/auth/validate')
  }
}

// 账号管理 API
export const accountApi = {
  getAll() {
    return request.get('/accounts')
  },
  // 任务三：用户分页与多条件搜索
  getPage(params: {
    page: number
    size: number
    username?: string
    status?: number | string
    roleLevel?: number | string
  }) {
    return request.get('/accounts/page', { params })
  },
  getById(id: number) {
    return request.get(`/accounts/${id}`)
  },
  create(data: any) {
    return request.post('/accounts', data)
  },
  update(id: number, data: any) {
    return request.put(`/accounts/${id}`, data)
  },
  delete(id: number) {
    return request.delete(`/accounts/${id}`)
  }
}

// 任务一：新增部门管理相关接口
export const departmentApi = {
  // 获取所有部门（用于部门列表页以及用户编辑时的下拉选框）
  getAll() {
    return request.get('/departments')
  },
  getById(id: number) {
    return request.get(`/departments/${id}`)
  },
  create(data: any) {
    return request.post('/departments', data)
  },
  update(id: number, data: any) {
    return request.put(`/departments/${id}`, data)
  },
  delete(id: number) {
    return request.delete(`/departments/${id}`)
  }
}
