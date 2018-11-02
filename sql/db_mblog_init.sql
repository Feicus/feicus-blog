/*
 Navicat Premium Data Transfer

 Source Server         : 本地库
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : localhost:3306
 Source Schema         : db_mblog

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 31/10/2018 17:27:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for mto_channels
-- ----------------------------
DROP TABLE IF EXISTS `mto_channels`;
CREATE TABLE `mto_channels`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_2s863lts1h6m7c30152262cvj`(`key_`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mto_channels
-- ----------------------------
INSERT INTO `mto_channels` VALUES (1, 'life', '生活随笔', 0);
INSERT INTO `mto_channels` VALUES (2, 'recommendation', '书籍推荐', 0);
INSERT INTO `mto_channels` VALUES (3, 'anthology', '个人文章', 0);
INSERT INTO `mto_channels` VALUES (4, 'movie', '影音推荐', 0);
INSERT INTO `mto_channels` VALUES (5, 'journey', '旅行记录', 0);
INSERT INTO `mto_channels` VALUES (6, 'secret', '秘密花园', 1);

-- ----------------------------
-- Table structure for mto_comments
-- ----------------------------
DROP TABLE IF EXISTS `mto_comments`;
CREATE TABLE `mto_comments`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `author_id` bigint(20) DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `created` datetime(0) DEFAULT NULL,
  `pid` bigint(20) NOT NULL,
  `status` int(11) NOT NULL,
  `to_id` bigint(20) DEFAULT NULL,
  `is_delete` int(4) DEFAULT 0 COMMENT '0默认 1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mto_config
-- ----------------------------
DROP TABLE IF EXISTS `mto_config`;
CREATE TABLE `mto_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `key_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_99vo6d7ci4wlxruo3gd0q2jq8`(`key_`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mto_config
-- ----------------------------
INSERT INTO `mto_config` VALUES (1, 'site_name', '0', '时光清浅');
INSERT INTO `mto_config` VALUES (3, 'site_domain', '0', 'http://feicus.com');
INSERT INTO `mto_config` VALUES (4, 'site_keywords', '0', '文章博客');
INSERT INTO `mto_config` VALUES (5, 'site_description', '0', '腹有诗书气自华');
INSERT INTO `mto_config` VALUES (6, 'site_metas', '0', '');
INSERT INTO `mto_config` VALUES (7, 'site_copyright', '0', 'Copyright © Feicus');
INSERT INTO `mto_config` VALUES (8, 'site_icp', '0', '');
INSERT INTO `mto_config` VALUES (11, 'site_oauth_qq', '0', '');
INSERT INTO `mto_config` VALUES (12, 'qq_app_id', '0', '');
INSERT INTO `mto_config` VALUES (13, 'qq_app_key', '0', '');
INSERT INTO `mto_config` VALUES (14, 'site_oauth_weibo', '0', '');
INSERT INTO `mto_config` VALUES (15, 'weibo_client_id', '0', '');
INSERT INTO `mto_config` VALUES (16, 'weibo_client_sercret', '0', '');

-- ----------------------------
-- Table structure for mto_favors
-- ----------------------------
DROP TABLE IF EXISTS `mto_favors`;
CREATE TABLE `mto_favors`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created` datetime(0) DEFAULT NULL,
  `own_id` bigint(20) DEFAULT NULL,
  `post_id` bigint(20) DEFAULT NULL,
  `is_delete` int(4) DEFAULT 0 COMMENT '0不删除 1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mto_feeds
-- ----------------------------
DROP TABLE IF EXISTS `mto_feeds`;
CREATE TABLE `mto_feeds`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `author_id` bigint(20) DEFAULT NULL,
  `created` datetime(0) DEFAULT NULL,
  `own_id` bigint(20) DEFAULT NULL,
  `post_id` bigint(20) DEFAULT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mto_follows
-- ----------------------------
DROP TABLE IF EXISTS `mto_follows`;
CREATE TABLE `mto_follows`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created` datetime(0) DEFAULT NULL,
  `follow_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `is_delete` int(4) DEFAULT 0 COMMENT '0默认 1删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKlbcc3hcj1cikyow8cvlk1eupe`(`follow_id`) USING BTREE,
  INDEX `FKso66aluvvri4r5a5x3lh31t8s`(`user_id`) USING BTREE,
  CONSTRAINT `FKlbcc3hcj1cikyow8cvlk1eupe` FOREIGN KEY (`follow_id`) REFERENCES `mto_users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKso66aluvvri4r5a5x3lh31t8s` FOREIGN KEY (`user_id`) REFERENCES `mto_users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for mto_notify
-- ----------------------------
DROP TABLE IF EXISTS `mto_notify`;
CREATE TABLE `mto_notify`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created` datetime(0) DEFAULT NULL,
  `event` int(11) NOT NULL,
  `from_id` bigint(20) DEFAULT NULL,
  `own_id` bigint(20) DEFAULT NULL,
  `post_id` bigint(20) DEFAULT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mto_posts
-- ----------------------------
DROP TABLE IF EXISTS `mto_posts`;
CREATE TABLE `mto_posts`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `author_id` bigint(20) DEFAULT NULL,
  `channel_id` int(11) DEFAULT NULL,
  `comments` int(11) NOT NULL,
  `created` datetime(0) DEFAULT NULL,
  `favors` int(11) NOT NULL,
  `featured` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  `summary` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `thumbnail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `views` int(11) NOT NULL,
  `weight` int(11) NOT NULL,
  `is_delete` int(4) DEFAULT 0 COMMENT '删除标志0不删除 1删除',
  `is_public` int(4) DEFAULT 1 COMMENT '默认公开1 私有0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;



-- ----------------------------
-- Table structure for mto_posts_attribute
-- ----------------------------
DROP TABLE IF EXISTS `mto_posts_attribute`;
CREATE TABLE `mto_posts_attribute`  (
  `id` bigint(20) NOT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for mto_users
-- ----------------------------
DROP TABLE IF EXISTS `mto_users`;
CREATE TABLE `mto_users`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created` datetime(0) DEFAULT NULL,
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `last_login` datetime(0) DEFAULT NULL,
  `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status` int(11) NOT NULL,
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '/assets/images/ava/default.png',
  `updated` datetime(0) DEFAULT NULL,
  `gender` int(11) NOT NULL,
  `role_id` int(11) DEFAULT NULL,
  `source` int(11) NOT NULL,
  `active_email` int(11) DEFAULT NULL,
  `comments` int(11) NOT NULL,
  `fans` int(11) NOT NULL,
  `favors` int(11) NOT NULL,
  `follows` int(11) NOT NULL,
  `posts` int(11) NOT NULL,
  `signature` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mto_users
-- ----------------------------
INSERT INTO `mto_users` VALUES (1, NOW(), 'example@qq.com', NOW(), NULL, 'UUKHSDDI5KPA43A8VL06V0TU2', 0, 'admin', 'admin', '/dist/images/ava/default.png', NOW(), 0, 1, 0, 0, 0, 0, 0, 0, 0, '');

-- ----------------------------
-- Table structure for mto_users_open_oauth
-- ----------------------------
DROP TABLE IF EXISTS `mto_users_open_oauth`;
CREATE TABLE `mto_users_open_oauth`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `access_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `expire_in` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `oauth_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `oauth_type` int(11) DEFAULT NULL,
  `oauth_user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `refresh_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mto_verify
-- ----------------------------
DROP TABLE IF EXISTS `mto_verify`;
CREATE TABLE `mto_verify`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `created` datetime(0) NOT NULL,
  `expired` datetime(0) NOT NULL,
  `status` int(11) DEFAULT NULL,
  `target` varchar(96) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_m7p0b526c4xlgjn787t22om2g`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for shiro_permission
-- ----------------------------
DROP TABLE IF EXISTS `shiro_permission`;
CREATE TABLE `shiro_permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `parent_id` bigint(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `weight` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_89ve8ffuihnryt1nw4o2t1feu`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shiro_permission
-- ----------------------------
INSERT INTO `shiro_permission` VALUES (1, '进入后台', 'admin', 0, 0, 0);
INSERT INTO `shiro_permission` VALUES (2, '栏目管理', 'channel:list', 0, 0, 0);
INSERT INTO `shiro_permission` VALUES (3, '编辑栏目', 'channel:update', 2, 0, 0);
INSERT INTO `shiro_permission` VALUES (4, '删除栏目', 'channel:delete', 2, 0, 0);
INSERT INTO `shiro_permission` VALUES (5, '文章管理', 'post:list', 0, 0, 0);
INSERT INTO `shiro_permission` VALUES (6, '编辑文章', 'post:update', 5, 0, 0);
INSERT INTO `shiro_permission` VALUES (7, '删除文章', 'post:logicdelete', 5, 0, 0);
INSERT INTO `shiro_permission` VALUES (8, '恢复文章', 'post:recovery', 5, 0, 0);
INSERT INTO `shiro_permission` VALUES (9, '评论管理', 'comment:list', 0, 0, 0);
INSERT INTO `shiro_permission` VALUES (10, '删除评论', 'comment:delete', 8, 0, 0);
INSERT INTO `shiro_permission` VALUES (11, '用户管理', 'user:list', 0, 0, 0);
INSERT INTO `shiro_permission` VALUES (12, '用户授权', 'user:role', 11, 0, 0);
INSERT INTO `shiro_permission` VALUES (13, '修改密码', 'user:pwd', 11, 0, 0);
INSERT INTO `shiro_permission` VALUES (14, '激活用户', 'user:open', 11, 0, 0);
INSERT INTO `shiro_permission` VALUES (15, '关闭用户', 'user:close', 11, 0, 0);
INSERT INTO `shiro_permission` VALUES (16, '角色管理', 'role:list', 0, 0, 0);
INSERT INTO `shiro_permission` VALUES (17, '修改角色', 'role:update', 16, 0, 0);
INSERT INTO `shiro_permission` VALUES (18, '删除角色', 'role:delete', 16, 0, 0);
INSERT INTO `shiro_permission` VALUES (19, '系统配置', 'config:list', 0, 0, 0);
INSERT INTO `shiro_permission` VALUES (20, '修改配置', 'config:update', 19, 0, 0);
INSERT INTO `shiro_permission` VALUES (21, '物理删除文章', 'post:delete', 5, 0, 0);


-- ----------------------------
-- Table structure for shiro_role
-- ----------------------------
DROP TABLE IF EXISTS `shiro_role`;
CREATE TABLE `shiro_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shiro_role
-- ----------------------------
INSERT INTO `shiro_role` VALUES (1, '管理员', 'admin', 0);

-- ----------------------------
-- Table structure for shiro_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `shiro_role_permission`;
CREATE TABLE `shiro_role_permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `permission_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shiro_role_permission
-- ----------------------------
INSERT INTO `shiro_role_permission` VALUES (1, 1, 1);
INSERT INTO `shiro_role_permission` VALUES (2, 2, 1);
INSERT INTO `shiro_role_permission` VALUES (3, 3, 1);
INSERT INTO `shiro_role_permission` VALUES (4, 4, 1);
INSERT INTO `shiro_role_permission` VALUES (5, 5, 1);
INSERT INTO `shiro_role_permission` VALUES (6, 6, 1);
INSERT INTO `shiro_role_permission` VALUES (7, 7, 1);
INSERT INTO `shiro_role_permission` VALUES (8, 8, 1);
INSERT INTO `shiro_role_permission` VALUES (9, 9, 1);
INSERT INTO `shiro_role_permission` VALUES (10, 10, 1);
INSERT INTO `shiro_role_permission` VALUES (11, 11, 1);
INSERT INTO `shiro_role_permission` VALUES (12, 12, 1);
INSERT INTO `shiro_role_permission` VALUES (13, 13, 1);
INSERT INTO `shiro_role_permission` VALUES (14, 14, 1);
INSERT INTO `shiro_role_permission` VALUES (15, 15, 1);
INSERT INTO `shiro_role_permission` VALUES (16, 16, 1);
INSERT INTO `shiro_role_permission` VALUES (17, 17, 1);
INSERT INTO `shiro_role_permission` VALUES (18, 18, 1);
INSERT INTO `shiro_role_permission` VALUES (19, 19, 1);
INSERT INTO `shiro_role_permission` VALUES (20, 20, 1);
INSERT INTO `shiro_role_permission` VALUES (21, 21, 1);

-- ----------------------------
-- Table structure for shiro_user_role
-- ----------------------------
DROP TABLE IF EXISTS `shiro_user_role`;
CREATE TABLE `shiro_user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shiro_user_role
-- ----------------------------
INSERT INTO `shiro_user_role` VALUES (1, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
