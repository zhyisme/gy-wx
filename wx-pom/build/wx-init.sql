/*
Navicat MySQL Data Transfer

Source Server         : 104.224.135.130-bwg
Source Server Version : 50552
Source Host           : 104.224.135.130:3306
Source Database       : wx

Target Server Type    : MYSQL
Target Server Version : 50552
File Encoding         : 65001

Date: 2016-10-25 17:26:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_resource
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource` (
  `ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `NAME` varchar(32) NOT NULL COMMENT '节点名称',
  `PARENT_ID` bigint(20) NOT NULL COMMENT '父节点ID',
  `URL` varchar(256) DEFAULT NULL COMMENT 'url链接',
  `DESCRIPTION` varchar(255) DEFAULT NULL COMMENT '节点描述',
  `STATUS` smallint(4) NOT NULL DEFAULT '1' COMMENT '状态，0未启用，1启用',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8 COMMENT='系统资源配置';

-- ----------------------------
-- Records of sys_resource
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER_NAME` varchar(20) NOT NULL COMMENT '用户名',
  `ACCOUNT_NAME` varchar(32) NOT NULL COMMENT '账户',
  `PASSWORD` varchar(32) NOT NULL COMMENT '密码',
  `SALT` varchar(64) NOT NULL COMMENT '加盐值',
  `DESCRIPTION` varchar(128) DEFAULT NULL COMMENT '描述',
  `STATUS` int(2) NOT NULL DEFAULT '1' COMMENT '状态，1启动，0禁用',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `INDEX_ACCOUNT_NAME` (`ACCOUNT_NAME`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6000000006 DEFAULT CHARSET=utf8 COMMENT='系统管理用户记录';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('6000000001', 'admin', 'admin', '9e03062320ba2b285c1042e373e64670', '4157c3feef4a6ed91b2c28cf4392f2d1', '请问', '1', '2016-06-22 02:29:53', '2016-06-22 02:29:55');
INSERT INTO `sys_user` VALUES ('6000000002', 'guanyang', 'guanyang', '4de591fcc2d6f31f41d5141cee739ff8', '5b63f75fffa0d9ed109e835f6efaafaa', '管理员', '1', '2016-06-24 13:22:49', '2016-07-16 13:50:21');
INSERT INTO `sys_user` VALUES ('6000000003', 'liangfenglong', 'liangfenglong', 'b10e33a95a4360da4724dfeb8c18bf5d', 'cecaf922577d0c66ec498ad3135e4213', '管理员', '1', '2016-07-27 17:48:12', '2016-07-27 17:48:27');
INSERT INTO `sys_user` VALUES ('6000000004', 'cgf', 'cgf', '26e545ccc2ef2deba7e2a83cd823fee0', 'f6bca9c5f07806488f4a860f76e5fad9', 'cgf', '1', '2016-08-05 23:53:46', '2016-08-05 23:53:28');
INSERT INTO `sys_user` VALUES ('6000000005', '测试', 'test', '3f45e2370ab0b80a58adff08171473ae', '3794b55bf58852bb12c8da47ad173f0e', '测试', '1', '2016-08-05 23:41:37', '2016-08-06 00:24:48');

-- ----------------------------
-- Table structure for system_config
-- ----------------------------
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config` (
  `ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `CODE` varchar(16) NOT NULL COMMENT '标识',
  `DESCRIPTION` varchar(64) DEFAULT NULL COMMENT '配置描述',
  `VALUE` varchar(512) NOT NULL COMMENT '配置值',
  `USER_ID` varchar(32) DEFAULT NULL COMMENT '操作人编码',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '操作时间',
  `REMARK` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `IDX_CODE` (`CODE`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8 COMMENT='系统配置表';

-- ----------------------------
-- Records of system_config
-- ----------------------------

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `USER_ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户编码',
  `UNION_ID` varchar(64) NOT NULL COMMENT '用户统一标识',
  `LEVEL_CODE` int(11) NOT NULL DEFAULT '0' COMMENT '级别码，0无等级，1VIP1，类推',
  `NICK_NAME` varchar(64) DEFAULT NULL COMMENT '用户昵称',
  `SEX` smallint(1) DEFAULT '1' COMMENT '性别，1男，2女',
  `PROVINCE` varchar(32) DEFAULT NULL COMMENT '省份',
  `CITY` varchar(32) DEFAULT NULL COMMENT '城市',
  `COUNTRY` varchar(32) DEFAULT NULL COMMENT '国家',
  `HEAD_IMG_URL` varchar(512) DEFAULT NULL COMMENT '用户头像',
  `STATUS` smallint(2) NOT NULL DEFAULT '1' COMMENT '状态，1启用，0禁用',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `REG_REWARD` smallint(2) DEFAULT '1' COMMENT '注册奖励是否发放，1未发放，2已发放',
  PRIMARY KEY (`USER_ID`),
  UNIQUE KEY `INDEX_UNION_ID` (`UNION_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8000000054 DEFAULT CHARSET=utf8 COMMENT='前台用户信息表';

-- ----------------------------
-- Records of user_info
-- ----------------------------

-- ----------------------------
-- Table structure for user_openid_info
-- ----------------------------
DROP TABLE IF EXISTS `user_openid_info`;
CREATE TABLE `user_openid_info` (
  `ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USER_ID` bigint(20) NOT NULL COMMENT '用户编码',
  `UNION_ID` varchar(64) NOT NULL COMMENT '用户统一标识',
  `OPEN_ID` varchar(32) NOT NULL COMMENT '用户ID',
  `TYPE` smallint(4) NOT NULL COMMENT '应用类型，1网站，2公众号',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `INDEX_OPEN_ID_TYPE` (`OPEN_ID`,`TYPE`) USING BTREE,
  KEY `INDEX_USER_ID` (`USER_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=235 DEFAULT CHARSET=utf8 COMMENT='用户openId信息表';

-- ----------------------------
-- Records of user_openid_info
-- ----------------------------

-- ----------------------------
-- Table structure for weixin_menu_config
-- ----------------------------
DROP TABLE IF EXISTS `weixin_menu_config`;
CREATE TABLE `weixin_menu_config` (
  `ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `MENU_NAME` varchar(32) NOT NULL COMMENT '菜单名称',
  `MENU_TYPE` varchar(32) NOT NULL DEFAULT 'view' COMMENT '菜单类型',
  `PARENT_ID` bigint(20) NOT NULL DEFAULT '-1' COMMENT '父层ID',
  `REPLY_ID` bigint(20) NOT NULL DEFAULT '-1' COMMENT '回复ID',
  `SORT_NO` int(4) NOT NULL COMMENT '排序码',
  `ENABLE` int(4) NOT NULL DEFAULT '1' COMMENT '是否生效，1是，0否',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`ID`),
  KEY `INDEX_REPLY_ID` (`REPLY_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='微信菜单配置';

-- ----------------------------
-- Records of weixin_menu_config
-- ----------------------------
INSERT INTO `weixin_menu_config` VALUES ('1', '一级菜单1', 'click', '-1', '6', '1', '1', '2016-06-19 16:40:09', '2016-09-26 21:42:35');
INSERT INTO `weixin_menu_config` VALUES ('6', '一级菜单2', 'click', '-1', '3', '2', '1', '2016-08-08 22:35:34', '2016-08-09 00:37:27');
INSERT INTO `weixin_menu_config` VALUES ('8', '一级菜单3', 'click', '-1', '5', '3', '1', '2016-08-09 00:36:31', '2016-08-09 00:37:30');
INSERT INTO `weixin_menu_config` VALUES ('9', '二级菜单21', 'click', '6', '3', '1', '1', '2016-08-09 00:37:54', '2016-08-09 00:38:59');
INSERT INTO `weixin_menu_config` VALUES ('10', '二级菜单22', 'view', '6', '4', '2', '1', '2016-08-09 00:38:18', '2016-08-09 00:39:17');
INSERT INTO `weixin_menu_config` VALUES ('11', '二级菜单31', 'click', '8', '5', '1', '1', '2016-08-09 00:45:33', '2016-08-09 00:45:33');
INSERT INTO `weixin_menu_config` VALUES ('12', '二级菜单32', 'view', '8', '4', '2', '1', '2016-08-09 00:47:09', '2016-08-09 00:48:57');
INSERT INTO `weixin_menu_config` VALUES ('13', '二级菜单23', 'click', '6', '5', '3', '1', '2016-08-21 20:01:43', '2016-08-21 20:03:57');

-- ----------------------------
-- Table structure for weixin_reply_config
-- ----------------------------
DROP TABLE IF EXISTS `weixin_reply_config`;
CREATE TABLE `weixin_reply_config` (
  `ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `KEYWORDS` varchar(60) NOT NULL COMMENT '关键字，唯一标识',
  `TITLE` varchar(64) NOT NULL COMMENT '标题',
  `DESCRIPTION` varchar(256) DEFAULT NULL COMMENT '描述',
  `REPLY_TEXT` varchar(512) DEFAULT NULL COMMENT '回复文本',
  `REPLY_LINK` varchar(256) DEFAULT NULL COMMENT '回复链接',
  `REPLY_IMG` varchar(256) DEFAULT NULL COMMENT '回复图片',
  `REPLY_TYPE` int(4) NOT NULL DEFAULT '1' COMMENT '回复类型,1:文本,2:链接,3:图文',
  `ENABLE` int(4) NOT NULL DEFAULT '1' COMMENT '是否生效，1是，0否',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `INDEX_KEYWORDS` (`KEYWORDS`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='微信回复配置';

-- ----------------------------
-- Records of weixin_reply_config
-- ----------------------------
INSERT INTO `weixin_reply_config` VALUES ('1', '9999', '默认回复', '', '默认回复\r\n<a href=\"wwww.chaopinxiang.com\">潮品巷</a>\r\n【1】回复1001，查看文本 /勾引\r\n【2】回复1003，查看图文 /握手', '', '', '1', '1', '2016-08-08 02:28:05', '2016-09-21 17:26:45');
INSERT INTO `weixin_reply_config` VALUES ('2', '9998', '关注回复', '', '关注回复\r\n<a href=\"http://www.chaopinxiang.com\">潮品巷</a>\r\n【1】回复1001，查看文本 /勾引\r\n【2】回复1003，查看图文 /握手', '', '', '1', '1', '2016-08-08 02:28:05', '2016-08-29 23:03:12');
INSERT INTO `weixin_reply_config` VALUES ('3', '1001', '测试文本内容回复', null, '测试文本内容回复\r\n<a href=\"http://www.chaopinxiang.com\">潮品巷</a>\r\n【1】回复1001，查看文本 /勾引\r\n【2】回复1003，查看图文 /握手', null, null, '1', '1', '2016-08-08 02:28:05', '2016-08-29 23:03:12');
INSERT INTO `weixin_reply_config` VALUES ('4', '1002', '测试连接', 'aeqwe', null, 'http://www.chaopinxiang.com', null, '2', '1', '2016-08-08 02:34:40', '2016-09-21 13:55:03');
INSERT INTO `weixin_reply_config` VALUES ('5', '1003', '标题标题标题', '描述描述描述描述描述\r\n描述描述描述描述描述', null, 'http://www.chaopinxiang.com', 'http://wx.qlogo.cn/mmopen/JCrjicctRMofp13ZCNVpjNNIQSlib8NaHG7mfRIfTXRyMP97LicslVibjFraacog6lFt8aUuL19eTHibiazAlgLU1xvg/0', '3', '1', '2016-08-08 02:35:23', '2016-08-26 11:56:41');
INSERT INTO `weixin_reply_config` VALUES ('6', 'RP9001', '送你一个钻石大礼包', '朋友领取的钻石越多你就会得到越多,分享之后自己也可以领哦！', null, 'http://m.chaopinxiang.com', 'http://chaopinxiangprd.img-cn-beijing.aliyuncs.com/other/RP9001/RP9001_1.png', '3', '1', '2016-09-26 21:34:20', '2016-09-26 22:35:14');
INSERT INTO `weixin_reply_config` VALUES ('7', 'RP9002', '红包超过限制', '红包超过限制', '每天只能领取一次，请明天再来试试吧', null, null, '1', '1', '2016-09-26 21:35:31', '2016-09-26 22:33:22');

-- ----------------------------
-- Table structure for weixin_reply_log
-- ----------------------------
DROP TABLE IF EXISTS `weixin_reply_log`;
CREATE TABLE `weixin_reply_log` (
  `ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `OPEN_ID` varchar(32) NOT NULL COMMENT '微信用户id',
  `TYPE` varchar(24) NOT NULL DEFAULT 'text' COMMENT '类型，包括普通消息、事件消息',
  `CREATE_TIME` datetime NOT NULL COMMENT '操作时间',
  `CONTENT` varchar(512) DEFAULT NULL COMMENT '内容',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='微信回复日志';

-- ----------------------------
-- Records of weixin_reply_log
-- ----------------------------

-- ----------------------------
-- Table structure for weixin_template_message
-- ----------------------------
DROP TABLE IF EXISTS `weixin_template_message`;
CREATE TABLE `weixin_template_message` (
  `ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `MESSAGE_TYPE` smallint(4) NOT NULL COMMENT '类型，1代付模板，2代付通知，3红包消息',
  `MESSAGE_CODE` varchar(64) NOT NULL COMMENT '消息标识',
  `TEMPLATE_ID` varchar(64) NOT NULL COMMENT '模板ID',
  `USER_ID` bigint(20) NOT NULL COMMENT '用户编码',
  `URL` varchar(512) DEFAULT NULL COMMENT '详情链接',
  `CONTENT` varchar(1024) DEFAULT NULL COMMENT '详细内容，保存json数据',
  `STATUS` smallint(2) NOT NULL DEFAULT '1' COMMENT '是否发送，1未发送，2已发送',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `INDEX_USER_AND_TYPE_AND_CODE` (`USER_ID`,`MESSAGE_TYPE`,`MESSAGE_CODE`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8 COMMENT='微信模板消息记录';

-- ----------------------------
-- Records of weixin_template_message
-- ----------------------------

-- ----------------------------
-- Table structure for weixin_user_record
-- ----------------------------
DROP TABLE IF EXISTS `weixin_user_record`;
CREATE TABLE `weixin_user_record` (
  `ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `OPEN_ID` varchar(32) NOT NULL COMMENT '微信用户id',
  `SUB_STATUS` char(1) NOT NULL DEFAULT '1' COMMENT '关注状态，1已关注，2取消关注',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime NOT NULL COMMENT '更新时间',
  `SCENE_ID` varchar(32) DEFAULT NULL COMMENT '二维码场景ID',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `INDEX_OPEN_ID` (`OPEN_ID`) USING BTREE,
  KEY `INDEX_UPDATE_TIME` (`UPDATE_TIME`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='微信用户记录';

-- ----------------------------
-- Records of weixin_user_record
-- ----------------------------
