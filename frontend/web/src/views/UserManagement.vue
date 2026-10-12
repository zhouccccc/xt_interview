<template>
  <div class="user-management">
    <!-- 任务三：搜索筛选栏（用户名模糊查询、权限级别、状态） -->
    <div class="search-header">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户名">
          <el-input
            v-model="searchForm.username"
            placeholder="搜索用户名"
            clearable
            @keyup.enter="handleSearch"
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="权限级别">
          <el-select
            v-model="searchForm.roleLevel"
            placeholder="全部权限"
            clearable
            style="width: 150px"
          >
            <el-option label="用户权限" :value="1" />
            <el-option label="管理员权限" :value="2" />
            <el-option label="超级管理员" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select
            v-model="searchForm.status"
            placeholder="全部状态"
            clearable
            style="width: 130px"
          >
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">
            搜索
          </el-button>
          <el-button :icon="Refresh" @click="handleResetSearch">
            重置
          </el-button>
        </el-form-item>
      </el-form>
      <div class="action-buttons">
        <el-button type="success" :icon="Plus" @click="openCreateDialog">
          添加用户
        </el-button>
      </div>
    </div>

    <!-- 用户列表表格 -->
    <el-table
      v-loading="loading"
      :data="users"
      border
      stripe
      style="width: 100%"
    >
      <el-table-column prop="username" label="用户名" min-width="120" />
      <el-table-column prop="name" label="姓名" min-width="100" />
      <!-- 任务二：新增所属部门列展示 -->
      <el-table-column label="所属部门" min-width="130">
        <template #default="{ row }">
          <el-tag v-if="getDepartmentName(row)" type="info" effect="plain">
            {{ getDepartmentName(row) }}
          </el-tag>
          <span v-else class="text-muted">未分配</span>
        </template>
      </el-table-column>
      <el-table-column prop="gender" label="性别" width="70" align="center" />
      <el-table-column prop="phone" label="联系方式" min-width="130" />
      <el-table-column label="权限级别" width="120" align="center">
        <template #default="{ row }">
          <el-tag :type="roleTagType(row.roleLevel)">
            {{ roleText(row.roleLevel) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="80" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="170" />
      <el-table-column label="操作" width="160" fixed="right" align="center">
        <template #default="{ row }">
          <el-button type="primary" link :icon="Edit" @click="openEditDialog(row)">
            编辑
          </el-button>
          <el-button type="danger" link :icon="Delete" @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 任务三：底部集成分页组件 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="pagination.currentPage"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50]"
        :total="pagination.totalElements"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 添加 / 编辑用户对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑用户' : '添加用户'"
      width="520px"
      @close="resetForm"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="90px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="formData.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="formData.name" placeholder="请输入姓名" />
        </el-form-item>
        <!-- 任务二：所属部门下拉选择框（非必填，支持清空） -->
        <el-form-item label="所属部门" prop="departmentId">
          <el-select
            v-model="formData.departmentId"
            placeholder="请选择所属部门（可选）"
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="dept in departmentOptions"
              :key="dept.id"
              :label="dept.name"
              :value="dept.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="formData.gender" placeholder="请选择性别" style="width: 100%">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>
        <el-form-item label="联系方式" prop="phone">
          <el-input v-model="formData.phone" placeholder="请输入联系方式" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="formData.password"
            type="password"
            show-password
            :placeholder="isEdit ? '留空则不修改密码' : '留空则默认123456'"
          />
        </el-form-item>
        <el-form-item label="权限级别" prop="roleLevel">
          <el-select v-model="formData.roleLevel" placeholder="请选择权限级别" style="width: 100%">
            <el-option label="用户权限" :value="1" />
            <el-option label="管理员权限" :value="2" />
            <el-option label="超级管理员权限" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="formData.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { accountApi, departmentApi } from '@/api'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Refresh, Search } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const editId = ref<number>(0)

const users = ref<any[]>([])
// 部门下拉选项数据源
const departmentOptions = ref<any[]>([])

// 搜索栏筛选条件
const searchForm = reactive({
  username: '',
  roleLevel: undefined as number | undefined,
  status: undefined as number | undefined
})

// 分页信息（element-plus页码从1开始）
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  totalElements: 0,
  totalPages: 0
})

const formRef = ref<FormInstance>()

// 表单对象（departmentId允许为空）
const formData = reactive({
  username: '',
  name: '',
  gender: '男',
  phone: '',
  password: '',
  roleLevel: 1,
  status: 1,
  isSuper: 0,
  departmentId: undefined as number | undefined
})

const formRules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  roleLevel: [{ required: true, message: '请选择权限级别', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

function roleText(level: number): string {
  const map: Record<number, string> = { 1: '用户权限', 2: '管理员权限', 3: '超级管理员' }
  return map[level] || '未知'
}

function roleTagType(level: number): 'info' | 'warning' | 'danger' {
  const map: Record<number, 'info' | 'warning' | 'danger'> = {
    1: 'info',
    2: 'warning',
    3: 'danger'
  }
  return map[level] || 'info'
}

// 获取部门展示名：优先用DTO返回的departmentName，兜底用本地列表查
function getDepartmentName(row: any): string {
  if (row.departmentName) return row.departmentName
  if (row.departmentId) {
    const dept = departmentOptions.value.find((d) => d.id === row.departmentId)
    if (dept) return dept.name
  }
  return ''
}

// 预先拉取所有部门供下拉框选择
async function loadDepartments() {
  try {
    departmentOptions.value = await departmentApi.getAll()
  } catch (error) {
    console.error('加载部门失败', error)
  }
}

// 分页拉取用户列表，后台page从0开始算所以这里减1
async function loadUsers() {
  loading.value = true
  try {
    const res: any = await accountApi.getPage({
      page: pagination.currentPage - 1,
      size: pagination.pageSize,
      username: searchForm.username || undefined,
      status: searchForm.status,
      roleLevel: searchForm.roleLevel
    })

    if (res && res.list !== undefined) {
      users.value = res.list
      pagination.totalElements = res.totalElements || 0
      pagination.totalPages = res.totalPages || 0
    } else if (Array.isArray(res)) {
      users.value = res
      pagination.totalElements = res.length
    }
  } catch (error: any) {
    ElMessage.error(error.message || '加载用户列表失败')
  } finally {
    loading.value = false
  }
}

// 点击搜索：重置到第1页重新查
function handleSearch() {
  pagination.currentPage = 1
  loadUsers()
}

// 点击重置：清空搜索条件并回到第1页
function handleResetSearch() {
  searchForm.username = ''
  searchForm.roleLevel = undefined
  searchForm.status = undefined
  pagination.currentPage = 1
  loadUsers()
}

// 切换每页条数
function handleSizeChange(val: number) {
  pagination.pageSize = val
  pagination.currentPage = 1
  loadUsers()
}

// 切换页码
function handleCurrentChange(val: number) {
  pagination.currentPage = val
  loadUsers()
}

// 重置弹窗表单
function resetForm() {
  formData.username = ''
  formData.name = ''
  formData.gender = '男'
  formData.phone = ''
  formData.password = ''
  formData.roleLevel = 1
  formData.status = 1
  formData.isSuper = 0
  formData.departmentId = undefined
  isEdit.value = false
  editId.value = 0
  formRef.value?.resetFields()
}

function openCreateDialog() {
  resetForm()
  dialogVisible.value = true
}

// 编辑时回显当前行的数据，包含部门ID
function openEditDialog(row: any) {
  resetForm()
  isEdit.value = true
  editId.value = row.id
  formData.username = row.username
  formData.name = row.name
  formData.gender = row.gender || '男'
  formData.phone = row.phone || ''
  formData.roleLevel = row.roleLevel
  formData.status = row.status
  formData.isSuper = row.isSuper
  formData.departmentId = row.departmentId || undefined
  dialogVisible.value = true
}

// 提交表单保存用户
async function handleSubmit() {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return

    submitting.value = true
    try {
      const data: any = { ...formData }
      // 留空不传密码字段，后端就不会覆盖旧密码
      if (data.password === '') {
        delete data.password
      }
      // 没选部门传null
      if (!data.departmentId) {
        data.departmentId = null
      }

      if (isEdit.value) {
        await accountApi.update(editId.value, data)
        ElMessage.success('用户更新成功')
      } else {
        await accountApi.create(data)
        ElMessage.success('用户创建成功')
      }
      dialogVisible.value = false
      await loadUsers()
    } catch (error: any) {
      ElMessage.error(error.message || '操作失败')
    } finally {
      submitting.value = false
    }
  })
}

// 删除确认
async function handleDelete(row: any) {
  try {
    await ElMessageBox.confirm(
      `确定要删除用户 "${row.username}" 吗？`,
      '删除确认',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    await accountApi.delete(row.id)
    ElMessage.success('用户删除成功')
    await loadUsers()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败')
    }
  }
}

onMounted(() => {
  loadDepartments()
  loadUsers()
})
</script>

<style scoped>
.user-management {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
}

.search-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
  flex-wrap: wrap;
  gap: 12px;
}

.search-form {
  margin-bottom: 0;
}

.action-buttons {
  margin-bottom: 18px;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.text-muted {
  color: #909399;
  font-size: 13px;
}
</style>
