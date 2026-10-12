package com.xt.interview.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// 任务一：部门实体类，对应 sys_department 表
@Entity
@Table(name = "sys_department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 部门名称
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // 部门编码，业务唯一标识（如 RD, OPS）
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    // 部门描述
    @Column(name = "description", length = 500)
    private String description;

    // 状态：1-启用，0-禁用，默认1
    @Column(name = "status")
    private Integer status;

    // 软删除标记：0-正常，1-已删除
    @Column(name = "deleted")
    private Integer deleted;

    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        // 新建时兜底赋默认值
        if (status == null) status = 1;
        if (deleted == null) deleted = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
