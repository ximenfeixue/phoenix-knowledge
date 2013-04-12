CREATE DATABASE IF NOT EXISTS `phoenix_knowledge`  DEFAULT CHARACTER SET utf8 ;

USE `phoenix_knowledge`;
-- ----------------------------
-- 文章表
-- ----------------------------
DROP TABLE IF EXISTS `tb_article`;
CREATE TABLE `tb_article` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uid` bigint(20) DEFAULT NULL COMMENT 'phoenix_user.tb_user.id',
  `author` varchar(255) DEFAULT NULL COMMENT '文章作者，默认为当前登录用户的name',
  `scource` varchar(255) DEFAULT NULL COMMENT '文章来源',
  `articleTitle` varchar(255) DEFAULT NULL COMMENT '文章标题',
  `articlesContent` text COMMENT '文章内容',
  `categoryid` bigint(20) DEFAULT NULL COMMENT 'phoenix_knowledge.tb_category.id',
  `sortId` varchar(255) DEFAULT NULL COMMENT 'phoenix_knowledge.tb_category.sortId',
  `pubdate` varchar(0) DEFAULT NULL COMMENT '文章发布时间',
  `modifyTime` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '最后修改时间',
  `essence` varchar(255) DEFAULT '0' COMMENT '是否加精 0:否 1:是',
  `recycleBin` varchar(255) DEFAULT '0' COMMENT '是否标志为回收站文章0:否  1:是',
  `state` varchar(255) DEFAULT NULL,
  `clickNum` bigint(20) DEFAULT NULL COMMENT '文章点击次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table 文章分类表
-- ----------------------------
DROP TABLE IF EXISTS `tb_category`;
CREATE TABLE `tb_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uid` bigint(20) DEFAULT NULL COMMENT 'phoenix_user.tb_user.id',
  `categoryName` varchar(255) DEFAULT NULL COMMENT '分类名称',
  `parentId` bigint(20) DEFAULT '0' COMMENT '父类ID',
  `sortId` varchar(255) DEFAULT NULL COMMENT '排序ID，三位一级 如001001,为一级分类下的第一个分类',
  `state` varchar(255) DEFAULT '0' COMMENT '分类状态 0:正常   1:删除',
  `subtime` varchar(255) DEFAULT NULL COMMENT '分类创建时间',
  `modtime` varchar(255) DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- 文件meta信息表
-- ----------------------------
DROP TABLE IF EXISTS `tb_file`;
CREATE TABLE `tb_file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `articleId` bigint(20) DEFAULT NULL COMMENT 'phoenix_knowledge.tb_article.id',
  `showName` varchar(255) DEFAULT NULL COMMENT '文件名(文件原始名称，需在前端显示)',
  `fiileName` varchar(255) DEFAULT NULL COMMENT '上传后生成的新的文件名称',
  `extension` varchar(255) DEFAULT NULL COMMENT '文件扩展名',
  `filePath` varchar(255) DEFAULT NULL COMMENT '附件路径',
  `uptime` varchar(255) DEFAULT NULL COMMENT '上传时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



