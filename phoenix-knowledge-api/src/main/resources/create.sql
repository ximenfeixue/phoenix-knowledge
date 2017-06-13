CREATE DATABASE phoenix_knowledge_new;
USE phoenix_knowledge_new;
CREATE TABLE `tb_knowledge_base` (
  `id` bigint(20) NOT NULL COMMENT '知识id',
  `knowledge_id` bigint(20) NOT NULL COMMENT '知识id',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建人',
  `create_user_name` varchar(50) DEFAULT NULL COMMENT '创建人名称',
  `column_id` bigint(20) NOT NULL COMMENT '知识类型（默认0：其他,1：资讯，2：投融工具，3：行业，4：经典案例，5：图书报告，6：资产管理，7：宏观，8：观点，9：判例，10，法律法规，11：文章）',
  `type` smallint(2) NOT NULL COMMENT '19种栏目类型',
  `source` varchar(255) DEFAULT NULL COMMENT '来源',
  `content_desc` varchar(255) DEFAULT NULL COMMENT '描述(知识前50个字符)',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `essence` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否加精(0-不加 1-加)',
  `visible` tinyint(2) NOT NULL DEFAULT '0' COMMENT '可见范围，默认0：为全平台可见，1：为自己可见，2：好友可见',
  `public_date` bigint(20) NOT NULL DEFAULT '0' COMMENT '发布时间',
  `modify_date` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改时间',
  `audit_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '审核状态(0：未通过，1：审核中，2：审核通过)',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态（0为无效/删除，1为有效，2为草稿，3,：回收站）',
  `report_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '举报状态(3：举报审核未通过，即无非法现象，2：举报审核通过，1:未被举报，0：已被举报)',
  `create_date` bigint(20) NOT NULL COMMENT '创建时间',
  `attachment_id` int(20) NOT NULL DEFAULT '0',
  `coverPic` varchar(255) DEFAULT NULL,
  `tags` varchar(255) DEFAULT NULL COMMENT '标签Id集合',
  `content` text,
  `modify_user_id` bigint(20) NOT NULL DEFAULT '0',
  `modify_user_name` varchar(255) DEFAULT NULL,
  `userStar` tinyint(4) NOT NULL DEFAULT '0' COMMENT '星标(1:是,0:否(默认))',
  `isOld` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否是旧数据 (1:是,0:否)',
  `cpath` varchar(255) DEFAULT NULL,
  `privated` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否私密 (1:是,0:否)',
  `taskId` varchar(255) DEFAULT NULL,
  `read_count` int (20)  NOT NULL DEFAULT '0' COMMENT '阅读数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='知识基础表';


CREATE TABLE `tb_knowledge_reference` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'id',
  `knowledge_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '知识Id（投融工具和行业有）',
  `article_name` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL COMMENT 'URL地址',
  `website_name` varchar(255) DEFAULT NULL COMMENT '网站名',
  `status` char(1) DEFAULT NULL COMMENT '标示本条资料是否有效，1：为有效，0：为无效',
  `create_date` bigint(20) NOT NULL COMMENT '创建时间',
  `ref_date` bigint(20) NOT NULL COMMENT '引用时间',
  `modify_date` bigint(20) NOT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='参考资料表';
 
CREATE TABLE `tb_knowledge_count` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `type` smallint(6) NOT NULL DEFAULT '1' COMMENT '栏目类型',
  `title` varchar(255) DEFAULT NULL COMMENT '知识标题',
  `userId` bigint(20) NOT NULL DEFAULT '0' COMMENT '知识所有者id',
  `userName` varchar(100) DEFAULT NULL COMMENT '知识所有者姓名',
  `commentCount` bigint(20) NOT NULL DEFAULT '0' COMMENT '评论数',
  `shareCount` bigint(20) NOT NULL DEFAULT '0' COMMENT '分享数',
  `collectCount` bigint(20) NOT NULL DEFAULT '0' COMMENT '收藏数',
  `clickCount` bigint(20) NOT NULL DEFAULT '0' COMMENT '点击数',
  `hotCount` bigint(20) NOT NULL DEFAULT '0' COMMENT '知识热度',
  `source` smallint(6) NOT NULL  DEFAULT '0' COMMENT '知识来源(0-系统 1-用户)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='知识统计表';
 