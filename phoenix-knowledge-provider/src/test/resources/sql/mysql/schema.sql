CREATE DATABASE IF NOT EXISTS `phoenix_knowledge`  DEFAULT CHARACTER SET utf8 ;
USE `phoenix_knowledge`;
/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2014-10-30 16:15:34                          */
/*==============================================================*/


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
/* Table: tb_column                                             */
/*==============================================================*/
create table tb_column
(
   id                   bigint(20) not null auto_increment comment '栏目id',
   columnName           varchar(50) comment '栏目名称',
   user_id              bigint(20) comment '用户Id',
   parent_id            bigint(20) comment '父级id',
   createtime           timestamp comment '创建时间',
   path_name            varchar(255) comment '路径名称',
   column_level_path    varchar(70) comment '分类层级路径  00000000100000001',
   del_status           tinyint comment '删除状态(1：删除 0- 正常)',
   update_time          timestamp comment '更新时间',
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
   createtime           timestamp comment '创建时间',
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
   createtime           timestamp comment '发表时间',
   tag                  varchar(255) comment '标签',
   c_desc               varchar(255) comment '简介',
   column_id            bigint(20) comment '栏目id',
   pic_path             varchar(255) comment '封面图片地址',
   column_type          smallint comment '11种栏目类型',
   essence              smallint comment 'essence 是否加精（0-不加 1-加）',
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
   createtime           timestamp comment '创建时间',
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
   createtime           timestamp comment '收藏时间',
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
   sub_date             timestamp comment '订阅时间',
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
   createtime           timestamp comment '评论时间',
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
   createtime           timestamp comment '保存时间',
   userid               bigint(20) comment '用户ID',
   type                 char(1) comment '类型（,1：资讯，2：投融工具，3：行业，4：经典案例，5：图书报告，6：资产管理，7：宏观，8：观点，9：判例，10，法律法规，11：文章）',
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
   createtime           timestamp comment '更新时间',
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
   createtime           timestamp comment '创建时间',
   user_id              bigint(20) comment '举报人id',
   primary key (id)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

alter table tb_knowledge_report comment '知识举报表';

/*==============================================================*/
/* Table: tb_knowledge_statics                                  */
/*==============================================================*/
create table tb_knowledge_statics
(
   knowledge_id         bigint(20) not null comment '知识id',
   commentCount         bigint(20) default 0 comment '评论数',
   shareCount           bigint(20) default 0 comment '分享数',
   collectionCount      bigint(20) default 0 comment '收藏数',
   clickCount           bigint(20) default 0 comment '点击数',
   title                varchar(255) comment '标题',
   source               smallint comment '知识来源(0-系统 1-用户)',
   type                 smallint,
   primary key (knowledge_id)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

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
   createtime           timestamp comment '创建时间',
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
   createtime           timestamp comment '创建时间',
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

DROP TABLE IF EXISTS tb_attachment;

/*==============================================================*/
/* Table: tb_attachment                                         */
/*==============================================================*/
CREATE TABLE tb_attachment
(
   id                   BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
   taskId               VARCHAR(255) COMMENT 'taskID',
   file_name            VARCHAR(500) COMMENT '文件名称',
   file_size            BIGINT(20) COMMENT '文件大小',
   file_type            VARCHAR(30) COMMENT '文件类型',
   download_url         VARCHAR(500) COMMENT '下载地址',
   userId               BIGINT(20) COMMENT '创建人id',
   PRIMARY KEY (id)
)  ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

ALTER TABLE tb_attachment COMMENT '附件表';