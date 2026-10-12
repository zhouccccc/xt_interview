package com.xt.interview.service;

import com.xt.interview.dto.DepartmentDTO;

import java.util.List;

// 任务一：部门业务接口，规范CRUD操作
public interface DepartmentService {

    // 新增部门
    DepartmentDTO createDepartment(DepartmentDTO dto);

    // 修改部门
    DepartmentDTO updateDepartment(Long id, DepartmentDTO dto);

    // 删除部门（软删除）
    void deleteDepartment(Long id);

    // 根据ID获取部门详情
    DepartmentDTO getDepartmentById(Long id);

    // 获取全部未删除部门列表
    List<DepartmentDTO> getAllDepartments();
}
