/*
 Navicat Premium Data Transfer

 Source Server         : aimer
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Host           : localhost:3306
 Source Schema         : friend1

 Target Server Type    : MySQL
 Target Server Version : 80032 (8.0.32)
 File Encoding         : 65001

 Date: 28/07/2024 15:14:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for apply
-- ----------------------------
DROP TABLE IF EXISTS `apply`;
CREATE TABLE `apply`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `acceptUserAccount` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '接收申请用户ID',
  `applyUserAccount` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发出申请用户ID',
  `isAgree` int NULL DEFAULT 2 COMMENT '是否同意，0拒绝，1同意，2默认没有回复',
  `answer` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '回答的问题',
  `introduction` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '自我介绍',
  `createTime` timestamp NULL DEFAULT NULL COMMENT '发送申请的时间',
  `problem` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '我的问题',
  `updateTime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `acceptUserAccount`(`acceptUserAccount` ASC) USING BTREE,
  INDEX `applyUserAccount`(`applyUserAccount` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 227 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tagName` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签名称',
  `parentId` bigint NULL DEFAULT NULL COMMENT '父标签 id',
  `isParent` tinyint NOT NULL DEFAULT 0 COMMENT '0 - 不是, 1 - 父标签',
  `createTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniIdx_tagName`(`tagName` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 78 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '标签' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `username` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userAccount` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账号',
  `avatarUrl` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像',
  `userPassword` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `userStatus` int NOT NULL DEFAULT 0 COMMENT '状态 0 - 正常',
  `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  `tags` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签 json 列表',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '位置名字',
  `longitude` decimal(10, 7) NULL DEFAULT NULL COMMENT '经度',
  `dimension` decimal(10, 7) NULL DEFAULT NULL COMMENT '维度',
  `age` year NULL DEFAULT NULL COMMENT '出生年',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `userAccount`(`userAccount` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1150018 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_extra
-- ----------------------------
DROP TABLE IF EXISTS `user_extra`;
CREATE TABLE `user_extra`  (
  `phone` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `wchat` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信号',
  `introduction` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '无' COMMENT '自我介绍',
  `problem` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '无' COMMENT '社交问题',
  `createTime` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `userAccount` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账号',
  `gender` tinyint NULL DEFAULT NULL COMMENT '女1，男0',
  PRIMARY KEY (`userAccount`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- View structure for user_complete
-- ----------------------------
DROP VIEW IF EXISTS `user_complete`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `user_complete` AS select `user_extra`.`phone` AS `phone`,`user_extra`.`problem` AS `problem`,`user_extra`.`introduction` AS `introduction`,`user_extra`.`wchat` AS `wchat`,`user_extra`.`updateTime` AS `updateTime`,`user_extra`.`createTime` AS `createTime`,`user_extra`.`gender` AS `gender`,`user`.`id` AS `id`,`user`.`avatarUrl` AS `avatarUrl`,`user`.`userPassword` AS `userPassword`,`user`.`userStatus` AS `userStatus`,`user`.`isDelete` AS `isDelete`,`user`.`tags` AS `tags`,`user`.`address` AS `address`,`user`.`longitude` AS `longitude`,`user`.`dimension` AS `dimension`,`user`.`username` AS `username`,`user`.`userAccount` AS `userAccount`,`user_extra`.`userAccount` AS `extra_userAccount`,`user`.`age` AS `age` from (`user_extra` join `user`) where (`user`.`userAccount` = `user_extra`.`userAccount`);

-- ----------------------------
-- Triggers structure for table user
-- ----------------------------
DROP TRIGGER IF EXISTS `insert_userAccount_trigger`;
delimiter ;;
CREATE TRIGGER `insert_userAccount_trigger` AFTER INSERT ON `user` FOR EACH ROW BEGIN
    INSERT INTO user_extra (userAccount)
    VALUES (NEW.userAccount);
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
