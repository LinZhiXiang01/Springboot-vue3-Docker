-- 创建管理员信息表
CREATE TABLE `admin` (
                         `auth_id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
                         `username` varchar(255) DEFAULT NULL COMMENT '账号',
                         `password` varchar(255) DEFAULT NULL COMMENT '密码',
                         `name` varchar(255) DEFAULT NULL COMMENT '名称',
                         `role` varchar(255) DEFAULT NULL COMMENT '角色',
                         `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
                         PRIMARY KEY (`auth_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='管理员信息表';


-- 创建 employee_auth 表
CREATE TABLE employee_auth (
                               auth_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                               username VARCHAR(255) NOT NULL UNIQUE COMMENT '用户名',
                               password VARCHAR(100) NOT NULL COMMENT 'BCrypt加密后的密码',
                               role VARCHAR(50) NOT NULL COMMENT '用户角色',
                               is_active BOOLEAN DEFAULT TRUE COMMENT '账号是否有效',
                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               last_login_at DATETIME COMMENT '上次登录时间'
) ENGINE=InnoDB COMMENT='员工认证信息表';

-- 创建 employee_profile 表
CREATE TABLE employee_profile (
                                  profile_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                  auth_id INT NOT NULL COMMENT '对应的认证用户ID',
                                  name VARCHAR(255) COMMENT '名称',
                                  sex VARCHAR(10) COMMENT '性别',
                                  no VARCHAR(100) COMMENT '工号',
                                  age INT COMMENT '年龄',
                                  description LONGTEXT COMMENT '个人介绍',
                                  department_id INT COMMENT '部门ID',
                                  avatar VARCHAR(255) COMMENT '头像URL',
                                  created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  FOREIGN KEY (auth_id) REFERENCES employee_auth(auth_id) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='员工业务信息表';

-- 创建文章信息表
CREATE TABLE `article` (
                           `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
                           `title` varchar(255) DEFAULT NULL COMMENT '标题',
                           `img` varchar(255) DEFAULT NULL COMMENT '封面',
                           `content` longtext COMMENT '内容',
                           `time` datetime DEFAULT NULL COMMENT '时间',
                           `description` varchar(255) DEFAULT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文章信息';

