-- 创建 employee_auth 表
CREATE TABLE employee_auth (
                               id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                               username VARCHAR(255) NOT NULL UNIQUE COMMENT '用户名',
                               password VARCHAR(100) NOT NULL COMMENT 'BCrypt加密后的密码',
                               role VARCHAR(50) NOT NULL COMMENT '用户角色',
                               is_active BOOLEAN DEFAULT TRUE COMMENT '账号是否有效',
                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               last_login_at DATETIME COMMENT '上次登录时间'
) ENGINE=InnoDB COMMENT='员工认证信息表';

-- 创建 employee_profile 表
CREATE TABLE employee_profile (
                                  id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                  auth_id INT NOT NULL COMMENT '对应的认证用户ID',
                                  name VARCHAR(255) COMMENT '名称',
                                  sex VARCHAR(10) COMMENT '性别',
                                  no VARCHAR(100) COMMENT '工号',
                                  age INT COMMENT '年龄',
                                  description LONGTEXT COMMENT '个人介绍',
                                  department_id INT COMMENT '部门ID',
                                  avatar VARCHAR(255) COMMENT '头像URL',
                                  created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  FOREIGN KEY (auth_id) REFERENCES employee_auth(id) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='员工业务信息表';