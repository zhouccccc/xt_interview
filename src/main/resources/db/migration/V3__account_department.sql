-- ==================== 任务二：用户与部门关联 ====================
-- 给用户表增加外键字段，可空（因为部门是非必填项）
ALTER TABLE `sys_account` ADD COLUMN `department_id` BIGINT DEFAULT NULL COMMENT '所属部门ID';

-- 关联外键，部门删掉时用户字段自动置空即可，不用级联删用户
ALTER TABLE `sys_account` ADD CONSTRAINT `fk_account_department` 
    FOREIGN KEY (`department_id`) REFERENCES `sys_department` (`id`) ON DELETE SET NULL;

-- 初始的admin默认挂到研发部(id=1)，方便直接看效果
UPDATE `sys_account` SET `department_id` = 1 WHERE `username` = 'admin';
