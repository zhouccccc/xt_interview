package com.xt.interview.repository;

import com.xt.interview.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    // 基础查询：只查未被软删除的部门
    List<Department> findByDeleted(Integer deleted);
    Optional<Department> findByIdAndDeleted(Long id, Integer deleted);
    Optional<Department> findByCodeAndDeleted(String code, Integer deleted);
    // 用于新建/编辑时校验部门编码是否重复
    boolean existsByCodeAndDeleted(String code, Integer deleted);
}
