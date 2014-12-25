CREATE DATABASE IF NOT EXISTS `phoenix_knowledge`  DEFAULT CHARACTER SET utf8 ;
USE `phoenix_knowledge`;
/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2014-10-30 16:15:34                          */
/*==============================================================*/


drop table if exists tb_attachment;

drop table if exists tb_column;

drop table if exists tb_column_knowledge;

drop table if exists tb_column_tag;

drop table if exists tb_column_visible;

drop table if exists tb_knowledge_base;

drop table if exists tb_knowledge_book_bookmark;

drop table if exists tb_knowledge_category;

drop table if exists tb_knowledge_collection;

drop table if exists tb_knowledge_column_subscribe;

drop table if exists tb_knowledge_comment;

drop table if exists tb_knowledge_draft;

drop table if exists tb_knowledge_recycle;

drop table if exists tb_knowledge_report;

drop table if exists tb_knowledge_statics;

drop table if exists tb_user_category;

drop table if exists tb_user_permission;

drop table if exists tb_user_tags;

/*==============================================================*/
/* Table: tb_attachment                                         */
/*==============================================================*/
create table tb_attachment
(
   id                   bigint(20) not null auto_increment comment 'ID',
   taskId               varchar(255) comment 'taskID',
   file_name            varchar(500) comment '文件名称',
   file_size            bigint(20) comment '文件大小',
   file_type            varchar(30) comment '文件类型',
   download_url         varchar(500) comment '下载地址',
   userId               bigint(20) comment '创建人id',
   createtime           timestamp null,
   primary key (id)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

alter table tb_attachment comment '附件表';

/*==============================================================*/
/* Table: tb_column                                             */
/*==============================================================*/
create table tb_column
(
   id                   bigint(20) not null auto_increment comment '栏目id',
   columnName           varchar(50) comment '栏目名称',
   user_id              bigint(20) comment '用户Id',
   parent_id            bigint(20) comment '父级id',
   createtime           timestamp null comment '创建时间',
   path_name            varchar(255) comment '路径名称',
   column_level_path    varchar(70) comment '分类层级路径  00000000100000001',
   del_status           tinyint comment '删除状态(1：删除 0- 正常)',
   update_time          timestamp null comment '更新时间',
   subscribe_count      bigint(20) comment '订阅数',
   type                 smallint comment '栏目类型',
   primary key (id)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

alter table tb_column comment '栏目表';

/*==============================================================*/
/* Table: tb_column_knowledge                                   */
/*==============================================================*/
create table tb_column_knowledge
(
   knowledge_id         bigint(20) not null comment '知识id',
   column_id            bigint(20) comment '栏目id',
   user_id              bigint(20) comment '用户id',
   type                 smallint comment '类型(11种类型)',
   primary key (knowledge_id)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

alter table tb_column_knowledge comment '栏目知识关系表';

/*==============================================================*/
/* Table: tb_column_tag                                         */
/*==============================================================*/
create table tb_column_tag
(
   id                   bigint(20) not null auto_increment comment '标签ID',
   column_id            bigint(20) comment '栏目id',
   columnName           varchar(32) comment '栏目名称',
   column_path          varchar(255) comment '栏目路径',
   tag                  varchar(255) comment '标签（多个标签用逗号分隔）',
   user_id              bigint(20) comment '用户id',
   createtime           timestamp null comment '创建时间',
   primary key (id)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

alter table tb_column_tag comment '栏目标签表';

/*==============================================================*/
/* Table: tb_column_visible                                     */
/*==============================================================*/
create table tb_column_visible
(
   id                   BIGINT(20) not null auto_increment comment '主键',
   user_id              BIGINT(20) default NULL comment '用户id',
   column_id            BIGINT(20) default NULL comment '栏目id',
   ctime                TIMESTAMP not null default '0000-00-00 00:00:00' comment '创建时间',
   utime                TIMESTAMP not null default '0000-00-00 00:00:00' comment '修改时间',
   pcid                 bigint(20) default 0 comment '父栏目id',
   state                smallint default 0 comment '0 可见 1 不可见',
   column_name          VARCHAR(200) default NULL comment '栏目名称',
   sort_id              varchar(200) comment '排序id',
   primary key (id)
)
ENGINE=INNODB AUTO_INCREMENT=184 DEFAULT CHARSET=utf8;

alter table tb_column_visible comment '栏目定制表';

/*==============================================================*/
/* Table: tb_knowledge_base                                     */
/*==============================================================*/
create table tb_knowledge_base
(
   knowledge_id         bigint(20) not null comment '知识ID',
   user_id              bigint(20) comment '用户id',
   title                varchar(255) comment '标题',
   author               varchar(50) comment '作者名称',
   path                 varchar(50) comment '栏目路径',
   createtime           timestamp null comment '发表时间',
   tag                  varchar(255) comment '标签',
   c_desc               varchar(255) comment '简介',
   column_id            bigint(20) comment '栏目id',
   pic_path             varchar(255) comment '封面图片地址',
   column_type          smallint comment '11种栏目类型',
   essence              smallint comment 'essence 是否加精（0-不加 1-加）',
   taskid               varchar(255) comment '附件taskid',
   primary key (knowledge_id)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

alter table tb_knowledge_base comment '知识基本表';

/*==============================================================*/
/* Table: tb_knowledge_book_bookmark                            */
/*==============================================================*/
create table tb_knowledge_book_bookmark
(
   id                   bigint(20) not null auto_increment comment 'id',
   knowledge_id         bigint(20) comment '知识id',
   pageno               int(5) comment '书签页码',
   title                varchar(50) comment '书签标题',
   b_desc               varchar(255) comment '书签描述',
   createtime           timestamp null comment '创建时间',
   primary key (id)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

alter table tb_knowledge_book_bookmark comment '知识书签表';

/*==============================================================*/
/* Table: tb_knowledge_category                                 */
/*==============================================================*/
create table tb_knowledge_category
(
   id                   bigint(20) not null auto_increment comment 'id',
   knowledge_id         bigint(20) comment '知识id',
   category_id          bigint(20) comment '目录Id',
   status               char(1) comment '状态（0：不生效，1：生效）',
   primary key (id)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

alter table tb_knowledge_category comment '知识目录表';

/*==============================================================*/
/* Table: tb_knowledge_collection                               */
/*==============================================================*/
create table tb_knowledge_collection
(
   id                   bigint(20) not null auto_increment comment 'id',
   knowledge_id         bigint(20) comment '知识id',
   createtime           timestamp null comment '收藏时间',
   source               int(1) comment '来源(1：自己，2：好友，3：金桐脑，4：全平台，5：组织)',
   category_id          bigint(20) comment '目录id(左侧目录.id)',
   collection_tags      varchar(255) comment '收藏标签',
   collection_comment   varchar(1000) comment '收藏评论',
   userId               bigint(20) comment '用户ID',
   primary key (id)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

alter table tb_knowledge_collection comment '知识收藏表';

/*==============================================================*/
/* Table: tb_knowledge_column_subscribe                         */
/*==============================================================*/
create table tb_knowledge_column_subscribe
(
   id                   bigint(20) not null auto_increment comment 'id',
   user_id              bigint(20) comment '用户id',
   column_id            bigint(20) comment '栏目id',
   sub_date             timestamp null comment '订阅时间',
   column_type          smallint comment '栏目类型（默认0：其他,1：资讯，2：投融工具，3：行业，4：经典案例，5：图书报告，6：资产管理，7：宏观，8：观点，9：判例，10，法律法规，11：文章）',
   primary key (id)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

alter table tb_knowledge_column_subscribe comment '订阅信息表';

/*==============================================================*/
/* Table: tb_knowledge_comment                                  */
/*==============================================================*/
create table tb_knowledge_comment
(
   id                   bigint(20) not null auto_increment comment '评论id',
   knowledge_id         bigint(20) comment '知识id',
   content              varchar(500) comment '评论内容',
   createtime           timestamp null comment '评论时间',
   status               boolean comment '评论状态(0：已删除，1：正常)',
   parentid             bigint(20) comment '上级评论id（0：根级，非0：对应评论Id）',
   user_id              bigint(20) comment '用户id',
   count                bigint(20) comment '子评论数',
   username             varchar(255) comment '用户名',
   pic                  varchar(255) comment '头像地址',
   primary key (id)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

alter table tb_knowledge_comment comment '知识评论表';

/*==============================================================*/
/* Table: tb_knowledge_draft                                    */
/*==============================================================*/
create table tb_knowledge_draft
(
   knowledge_id         bigint(20) not null auto_increment comment '知识id',
   draftname            varchar(100) comment '草稿名称',
   drafttype            varchar(10) comment '草稿类型（,1：资讯，2：投融工具，3：行业，4：经典案例，5：图书报告，6：资产管理，7：宏观，8：观点，9：判例，10，法律法规，11：文章）',
   createtime           timestamp null comment '保存时间',
   userid               bigint(20) comment '用户ID',
   type                 char(2) comment '类型（,1：资讯，2：投融工具，3：行业，4：经典案例，5：图书报告，6：资产管理，7：宏观，8：观点，9：判例，10，法律法规，11：文章）',
   primary key (knowledge_id)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

alter table tb_knowledge_draft comment '知识草稿箱';

/*==============================================================*/
/* Table: tb_knowledge_recycle                                  */
/*==============================================================*/
create table tb_knowledge_recycle
(
   knowledge_id         bigint(20) not null comment '知识id',
   title                varchar(100) comment '知识名称',
   type                 varchar(10) comment '类型（,1：资讯，2：投融工具，3：行业，4：经典案例，5：图书报告，6：资产管理，7：宏观，8：观点，9：判例，10，法律法规，11：文章）',
   createtime           timestamp null comment '更新时间',
   userid               bigint(20) comment '用户ID',
   catetoryid           bigint(20) comment '目录ID',
   primary key (knowledge_id)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

alter table tb_knowledge_recycle comment '知识回收站';

/*==============================================================*/
/* Table: tb_knowledge_report                                   */
/*==============================================================*/
create table tb_knowledge_report
(
   id                   bigint(20) not null auto_increment comment 'id',
   knowledge_id         bigint(20) comment '知识id',
   type                 char(2) comment '类型(1-色情淫秽 2-骚扰谩骂 3- 广告欺诈 4-反动言论 5-其他)',
   rep_desc             varchar(500) comment '描述',
   createtime           timestamp null comment '创建时间',
   user_id              bigint(20) comment '举报人id',
   userName             varchar(50) comment '举报人名',
   knowledgeTitle       varchar(255) comment '知识标题',
   updateTime           timestamp null comment '修改时间',
   results              varchar(500) comment '处理结果',
   status               int(1) comment '处理状态( 0 -未处理 1-已处理  )',
   columnType           smallint comment '栏目类型',
   primary key (id)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

alter table tb_knowledge_report comment '知识举报表';

/*==============================================================*/
/* Table: tb_knowledge_statics                                  */
/*==============================================================*/
create table tb_knowledge_statics 
( 
knowledge_id bigint(20) not null comment '知识id', 
commentCount bigint(20) default 0 comment '评论数', 
shareCount bigint(20) default 0 comment '分享数', 
collectionCount bigint(20) default 0 comment '收藏数', 
clickCount bigint(20) default 0 comment '点击数', 
source smallint comment '知识来源(0-系统 1-用户)', 
type smallint, 
primary key (knowledge_id)
); 

alter table tb_knowledge_statics comment '知识统计表';

/*==============================================================*/
/* Table: tb_user_category                                      */
/*==============================================================*/
create table tb_user_category
(
   id                   bigint(20) not null auto_increment comment 'id',
   user_id              bigint(20) comment '用户Id',
   categoryName         varchar(50) comment '目录名称',
   sortid               varchar(255) comment '路径Id，000000001000000001',
   createtime           timestamp null comment '创建时间',
   path_name            varchar(255) comment '路径名称',
   parent_id            bigint(20) comment '父级id',
   category_type        smallint comment '0 正常目录 1 收藏夹',
   primary key (id)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

alter table tb_user_category comment '目录表(左树)';

/*==============================================================*/
/* Table: tb_user_permission                                    */
/*==============================================================*/
create table tb_user_permission
(
   receive_user_id      bigint(20) comment '接收者Id',
   knowledge_id         bigint(20) comment '知识id',
   send_user_id         bigint(20) comment '发起者id',
   type                 int(1) comment '类型（2-大乐，3-中乐，4-小乐）',
   mento                varchar(255) comment '分享留言',
   createtime           timestamp null comment '创建时间',
   column_type          smallint comment '知识所属类目ID，共十一种顶级类目之一',
   column_id            bigint(20) comment '栏目id'
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

alter table tb_user_permission comment '用户权限表';

/*==============================================================*/
/* Table: tb_user_tags                                          */
/*==============================================================*/
create table tb_user_tags
(
   tagId                bigint not null auto_increment comment '标签ID',
   userId               bigint comment '用户ID',
   tagname              varchar(255) comment '标签名称',
   primary key (tagId)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

alter table tb_user_tags comment '用户标签表';



CREATE INDEX idx_col_colLevPath ON tb_column (column_level_path); 
CREATE INDEX idx_per_typeRidSid ON tb_user_permission (column_type,send_user_id,receive_user_id); 
CREATE INDEX idx_cvis_cidUid ON tb_column_visible (column_id,user_id); 
CREATE INDEX idx_kc_kidCateid ON tb_knowledge_category (knowledge_id,category_id); 
CREATE INDEX idx_uc_uidPidSoidType ON tb_user_category (user_id,parent_id,sortid,category_type); 
CREATE INDEX idx_ks_type ON tb_knowledge_statics (TYPE); 
CREATE INDEX idx_ct_cidUid ON tb_column_tag(column_id,user_id);


USE `phoenix_cloud`;

CREATE TABLE `tb_industry_word` (
   `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `title` VARCHAR(255) DEFAULT NULL COMMENT '行业词条名称',
   `investment_classify_id` INT(11) DEFAULT NULL COMMENT '行业分类id',
   `create_user_id` BIGINT(20) DEFAULT NULL COMMENT '用户id',
   `create_user_name` VARCHAR(255) DEFAULT NULL COMMENT '用户登录名',
   `read_num` BIGINT(20) DEFAULT '0' COMMENT '阅读次数',
   `create_date` DATETIME DEFAULT NULL COMMENT '创建时间',
   `investment_status` CHAR(1) DEFAULT '0' COMMENT '行业标题审核状态:等待审核 0 通过 1 未通过 2屏蔽3',
   PRIMARY KEY (`id`)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='行业分类信息表';


CREATE TABLE `tb_industry_version` (
   `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
   `investment_id` BIGINT(20) DEFAULT NULL COMMENT '行业词条id',
   `card_explain` VARCHAR(2000) DEFAULT NULL,
   `create_user_id` BIGINT(20) DEFAULT NULL COMMENT '创建这个词条内容的用户id',
   `create_user_name` VARCHAR(255) DEFAULT NULL,
   `create_date` DATETIME DEFAULT NULL,
   `edit_date` DATETIME DEFAULT NULL,
   `edit_reason` VARCHAR(500) DEFAULT NULL,
   `content` LONGTEXT,
   `version` BIGINT(20) DEFAULT NULL COMMENT '版本号，后台生成的',
   `audit_status` CHAR(1) DEFAULT NULL COMMENT '此版本词条的审核状态 0为未审核 1为审核通过 2为审核未通过  默认新增词条时为0',
   `isusing` CHAR(1) DEFAULT NULL COMMENT '使用状态 Y为可以使用 N为屏蔽 默认新增时为N 通过审核后为Y 屏蔽后为N',
   `card_pic_path` VARCHAR(255) DEFAULT NULL COMMENT '名片图片路径',
   `card_pic_small_path` VARCHAR(255) DEFAULT NULL COMMENT '名片图片缩略图路径',
   `card_title` VARCHAR(255) DEFAULT NULL COMMENT '名片标题',
   PRIMARY KEY (`id`),
   KEY `idx_investment_version_inid` (`investment_id`)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='行业内容图片信息';



 CREATE TABLE `tb_industry_synonyms` (
   `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `investment_id` BIGINT(20) DEFAULT NULL COMMENT '行业词条id',
   `synonyms_word` VARCHAR(255) DEFAULT NULL COMMENT '行业词条同义词',
   `version` BIGINT(20) DEFAULT NULL COMMENT '版本号',
   PRIMARY KEY (`id`)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='行业同义词信息表';


CREATE TABLE `tb_industry_pic` (
   `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
   `investment_id` BIGINT(20) DEFAULT NULL COMMENT '行业词条id',
   `url` VARCHAR(255) DEFAULT NULL COMMENT '如果是编辑器中提取的则存在url',
   `pic_path` VARCHAR(255) DEFAULT NULL COMMENT 'nginx上图片路径，去掉nfs部分',
   `pic_small_path` VARCHAR(255) DEFAULT NULL COMMENT '本图片的缩略图路径，在图册查看里面使用',
   `pic_small_edit_path` VARCHAR(255) DEFAULT NULL COMMENT '缩略图 在编辑器里使用',
   `pic_name` VARCHAR(255) DEFAULT NULL COMMENT '图片名字 nginx生成的',
   `pic_real_name` VARCHAR(255) DEFAULT NULL COMMENT '图片的真实名字',
   `pic_desc` VARCHAR(255) DEFAULT NULL COMMENT '图片描述',
   `ishome_page` CHAR(1) DEFAULT NULL COMMENT '是否把此图片作为封面 默认插入数据库时为N',
   `create_user_id` BIGINT(20) DEFAULT NULL COMMENT '上传这个图片的用户id',
   `create_user_name` VARCHAR(255) DEFAULT NULL COMMENT '上传此图片的用户登录名',
   `create_date` DATETIME DEFAULT NULL COMMENT '创建时间',
   `pic_book_id` VARCHAR(100) DEFAULT NULL COMMENT '图册编号 随机生成的数字',
   `pic_book_name` VARCHAR(255) DEFAULT NULL COMMENT '图册名',
   `pic_type` CHAR(1) DEFAULT NULL COMMENT '图片类型，S是单独上传的图片（单独图片组成图册）\n                                M是上传的整个图册里的图片 C是名片图片',
   `pic_status` CHAR(1) DEFAULT NULL COMMENT '图片状态：0-待生效 1-已生效 2-屏蔽',
   `pic_del` CHAR(1) DEFAULT NULL COMMENT '图片待删除标记 1-待删除',
   `update_date` DATETIME DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='行业图片信息表';


CREATE TABLE `tb_industry_reference` (
   `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `investment_id` BIGINT(20) DEFAULT NULL COMMENT '行业词条id',
   `material_type` VARCHAR(50) DEFAULT '' COMMENT '引用资料类型，例如网络 著作等',
   `material_name` VARCHAR(255) DEFAULT '' COMMENT '资料或文章名',
   `url` VARCHAR(255) DEFAULT '' COMMENT '引用的文章的url',
   `website` VARCHAR(255) DEFAULT '' COMMENT '引用的文章的网址',
   `reference_date` VARCHAR(20) DEFAULT '' COMMENT '引用日期',
   `publication_date` VARCHAR(20) DEFAULT '' COMMENT '发表日期',
   `version` BIGINT(20) DEFAULT NULL COMMENT '这个参考资料的版本号 后台生成数字',
   `STATUS` CHAR(1) DEFAULT NULL,
   `update_date` DATETIME DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=INNODB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8 COMMENT='行业词条参考资料信息表';

 CREATE TABLE `tb_industry_catalog` (
   `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
   `investment_id` BIGINT(20) DEFAULT NULL COMMENT '行业词条id',
   `catalog_name` VARCHAR(255) DEFAULT NULL COMMENT '词条目录名',
   `level` INT(11) DEFAULT NULL COMMENT '目录级别 一级就是1 二级就是2',
   `pid` BIGINT(20) DEFAULT NULL COMMENT '上级目录id',
   `sort` INT(11) DEFAULT NULL COMMENT '排序',
   `version` BIGINT(20) DEFAULT NULL COMMENT '版本号，后台生成的',
   PRIMARY KEY (`id`)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='词条目录信息表，只有两级目录';

ALTER TABLE tb_investment_version MODIFY card_explain LONGTEXT;


ALTER TABLE tb_investment_version MODIFY edit_reason LONGTEXT;

ALTER TABLE tb_industry_version MODIFY card_explain LONGTEXT;

ALTER TABLE tb_case MODIFY summary LONGTEXT;

-- 导出 phoenix_ecosphere 的数据库结构 
CREATE DATABASE IF NOT EXISTS `phoenix_ecosphere` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */; 
USE `phoenix_ecosphere`; 


-- 导出 表 phoenix_ecosphere.tb_ecosphere_correct 结构 
CREATE TABLE IF NOT EXISTS `tb_ecosphere_correct` ( 
`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增序列', 
`joinKey` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '用户ID_物料类型ID_物料ID_Segment_ColumnName 1 需求 2 人脉 3 全平台普通用户 4 组织(全平台组织用户) 5 客户 6 知识', 
`correctType` tinyint(4) NOT NULL COMMENT '1 需求 2 人脉 3 全平台普通用户 4 组织(全平台组织用户) 5 客户 6 知识', 
`correctSaveId` bigint(20) DEFAULT NULL COMMENT '其它各个维度', 
`correctSaveStrId` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '人脉ID', 
PRIMARY KEY (`id`), 
KEY `userId` (`joinKey`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='生态纠正表'; 


use phoenix_admin; 
INSERT INTO `tb_permission`(id,`name`,`number`,`remark`,`parentId`) VALUES ('96', '删除云知识','cloud:delete','删除云知识','57'); 
INSERT INTO `tb_permission`(id,`name`,`number`,`remark`,`parentId`) VALUES ('97', '修改云知识','cloud:update','修改云知识','57'); 
INSERT INTO `tb_permission`(id,`name`,`number`,`remark`,`parentId`) VALUES ('98', '审核云知识','cloud:approve','审核云知识','57'); 
INSERT INTO `tb_permission`(id,`name`,`number`,`remark`,`parentId`) VALUES ('99', '栏目管理','cloud:column','栏目管理','57'); 
INSERT INTO `tb_permission`(id,`name`,`number`,`remark`,`parentId`) VALUES ('961', '举报管理','cloud:report','举报管理','57'); 
update tb_role r set r.permissionString = CONCAT(permissionString,';cloud:delete;cloud:approve;cloud:update;cloud:column;cloud:report'), 
r.remark = CONCAT(remark,'删除云知识、审核云知识、修改云知识、栏目管理、举报管理') 
where r.id = '1';

CREATE TABLE `tb_user_tags` ( 
`tagId` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '标签ID', 
`userId` BIGINT(20) DEFAULT NULL COMMENT '用户ID', 
`tagname` VARCHAR(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '标签名称', 
PRIMARY KEY (`tagId`) 
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='用户标签表';

 