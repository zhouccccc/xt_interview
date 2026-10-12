-- ==================== 任务一：部门表迁移脚本 ====================
-- 按题目要求设计：code编码唯一、deleted做软删除标记
CREATE TABLE IF NOT EXISTS `sys_department` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '部门ID',
    `name` VARCHAR(100) NOT NULL COMMENT '部门名称',
    `code` VARCHAR(50) NOT NULL COMMENT '部门编码',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '部门描述',
    `status` INT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `deleted` INT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_department_code` (`code`),
    INDEX `idx_department_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表';

-- 预置2条初始测试数据（研发部、运维部）
INSERT INTO `sys_department` (`name`, `code`, `description`, `status`, `deleted`) VALUES
('研发部', 'RD', '负责公司核心产品与系统的设计与研发', 1, 0),
('运维部', 'OPS', '负责基础设施运行维护、网络安全与部署保障', 1, 0);
