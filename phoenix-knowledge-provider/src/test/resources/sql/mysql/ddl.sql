CREATE DATABASE IF NOT EXISTS `phoenix_knowledge`  DEFAULT CHARACTER SET utf8 ;
USE `phoenix_knowledge`;

DROP TABLE IF EXISTS `tb_article`;
CREATE TABLE `tb_article` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uid` bigint(20) DEFAULT NULL COMMENT 'phoenix_user.tb_user.id',
  `author` varchar(255) DEFAULT NULL COMMENT '文章作者，默认为当前登录用户的name',
  `source` varchar(255) DEFAULT NULL COMMENT '文章来源',
  `articleTitle` varchar(255) DEFAULT NULL COMMENT '文章标题',
  `articleContent` longtext COMMENT '文章内容',
  `categoryid` bigint(20) DEFAULT NULL COMMENT 'phoenix_knowledge.tb_category.id',
  `sortId` varchar(255) DEFAULT NULL COMMENT 'phoenix_knowledge.tb_category.sortId',
  `pubdate` timestamp NULL DEFAULT NULL COMMENT '文章发布时间',
  `modifyTime` timestamp NULL DEFAULT NULL COMMENT '最后修改时间',
  `essence` varchar(255) DEFAULT '0' COMMENT '是否加精 0:否 1:是',
  `recycleBin` varchar(255) DEFAULT '0' COMMENT '是否标志为回收站文章0:否  1:是',
  `state` varchar(255) DEFAULT NULL,
  `clickNum` bigint(20) DEFAULT NULL COMMENT '文章点击次数',
  `task_id` varchar(765) DEFAULT NULL COMMENT '与文章附件表关联',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `tb_category`;
CREATE TABLE `tb_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uid` bigint(20) DEFAULT NULL COMMENT 'phoenix_user.tb_user.id',
  `name` varchar(255) DEFAULT NULL COMMENT '分类名称',
  `parentId` bigint(20) DEFAULT '0' COMMENT '父类ID',
  `sortId` varchar(255) DEFAULT NULL COMMENT '排序ID，九位一级 如000000001000000001,为一级分类下的第一个分类',
  `state` varchar(255) DEFAULT '0' COMMENT '分类状态 0:正常   1:删除',
  `subtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modtime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;