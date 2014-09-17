CREATE DATABASE IF NOT EXISTS `phoenix_knowledge`  DEFAULT CHARACTER SET utf8 ;
USE `phoenix_knowledge`;
/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2014-9-16 15:50:59                           */
/*==============================================================*/


drop table if exists tb_column_knowledge;

drop table if exists tb_knowledge_book_bookmark;

drop table if exists tb_knowledge_category;

drop table if exists tb_knowledge_collection;

drop table if exists tb_knowledge_column;

drop table if exists tb_knowledge_column_subscribe;

drop table if exists tb_knowledge_comment;

drop table if exists tb_knowledge_r_tag;

drop table if exists tb_knowledge_report;

drop table if exists tb_knowledge_statics;

drop table if exists tb_knowledge_tag;

drop table if exists tb_user_category;

drop table if exists tb_user_permission;

/*==============================================================*/
/* Table: tb_column_knowledge                                   */
/*==============================================================*/
create table tb_column_knowledge
(
   column_id            bigint(20) comment '栏目id',
   knowledge_id         bigint(20) comment '知识id',
   user_id              bigint(20) comment '用户id',
   type                 smallint comment '类型(11种类型)'
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

alter table tb_column_knowledge comment '栏目知识关系表';

/*==============================================================*/
/* Table: tb_knowledge_book_bookmark                            */
/*==============================================================*/
create table tb_knowledge_book_bookmark
(
   id                   bigint(20) not null comment 'id',
   knowledge_id         bigint(20) comment '知识id',
   pageno               int(5) comment '书签页码',
   title                varchar(50) comment '书签标题',
   b_desc               varchar(255) comment '书签描述',
   createtime           timestamp comment '创建时间',
   primary key (id)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;

alter table tb_knowledge_book_bookmark comment '知识书签表';

/*==============================================================*/
/* Table: tb_knowledge_category                                 */
/*==============================================================*/
create table tb_knowledge_category
(
   id                   bigint(20) not null comment 'id',
   knowledge_id         bigint(20) comment '知识id',
   category_id          bigint(20) comment '目录Id',
   status               char(1) comment '状态（0：不生效，1：生效）',
   sortid               varchar(255) comment '排序id',
   user_id              bigint(20) comment '用户id',
   title                varchar(255) comment '标题',
   author               varchar(50) comment '作者名称',
   path                 smallint comment '栏目路径',
   share_author         varchar(255) comment '分享者',
   createtime           timestamp comment '发表时间',
   tag                  varchar(255) comment '标签',
   c_desc               varchar(255) comment '简介',
   column_id            bigint(20) comment '栏目id',
   pic_path             varchar(255) comment '封面图片地址',
   primary key (id)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;

alter table tb_knowledge_category comment '知识目录表';

/*==============================================================*/
/* Table: tb_knowledge_collection                               */
/*==============================================================*/
create table tb_knowledge_collection
(
   id                   bigint(20) not null comment 'id',
   knowledge_id         bigint(20) comment '知识id',
   column_id            bigint(20) comment '栏目id',
   timestamp            datetime comment '收藏时间',
   knowledgeType        char(2) comment '知识类型（默认0：其他,1：资讯，2：投融工具，3：行业，4：经典案例，5：图书报告，6：资产管理，7：宏观，8：观点，9：判例，10，法律法规，11：文章）',
   source               varchar(255) comment '来源(1：自己，2：好友，3：金桐脑，4：全平台，5：组织)',
   category_id          bigint(20) comment '目录id(左侧目录.id)',
   primary key (id)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8;

alter table tb_knowledge_collection comment '知识收藏表';

/*==============================================================*/
/* Table: tb_knowledge_column                                   */
/*==============================================================*/
create table tb_column
(
   id                   bigint(20) not null comment '栏目id',
   columnName           varchar(50) comment '栏目名称',
   user_id              bigint(20) comment '用户Id',
   parent_id            bigint(20) comment '父级id',
   createtime           timestamp comment '创建时间',
   path_name            varchar(255) comment '路径名称',
   column_level_path    varchar(70) comment '分类层级路径  00000000100000001',
   del_status           tinyint comment '删除状态(1：删除 0- 正常)',
   update_time          timestamp comment '更新时间',
   subscribe_count      bigint(20) comment '订阅数',
   primary key (id)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;

alter table tb_knowledge_column comment '栏目表';

/*==============================================================*/
/* Table: tb_knowledge_column_subscribe                         */
/*==============================================================*/
create table tb_knowledge_column_subscribe
(
   id                   bigint(20) comment 'id',
   user_id              bigint(20) comment '用户id',
   column_id            bigint(20) comment '栏目id',
   sub_date             timestamp comment '订阅时间',
   column_type          smallint comment '栏目类型（默认0：其他,1：资讯，2：投融工具，3：行业，4：经典案例，5：图书报告，6：资产管理，7：宏观，8：观点，9：判例，10，法律法规，11：文章）'
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8;

alter table tb_knowledge_column_subscribe comment '订阅信息表';

/*==============================================================*/
/* Table: tb_knowledge_comment                                  */
/*==============================================================*/
create table tb_knowledge_comment
(
   id                   bigint(20) not null comment '评论id',
   knowledge_id         bigint(20) comment '知识id',
   content              varchar(500) comment '评论内容',
   createtime           timestamp comment '评论时间',
   status               boolean comment '评论状态(0：已删除，1：正常)',
   parentid             bigint(20) comment '上级评论id（0：根级，非0：对应评论Id）',
   user_id              bigint(20) comment '用户id',
   count                bigint(20) comment '子评论数',
   primary key (id)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8;

alter table tb_knowledge_comment comment '知识评论表';

/*==============================================================*/
/* Table: tb_knowledge_r_tag                                    */
/*==============================================================*/
create table tb_category_tag
(
   id                   bigint(20) not null comment 'id',
   column_id            bigint(20) comment '栏目id',
   tag               	varchar(255) comment '标签',
   user_id              bigint(20) comment '用户id',
   createtime           timestamp comment '创建时间',
   primary key (id)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8;

alter table tb_category_tag comment '知识标签表';

/*==============================================================*/
/* Table: tb_knowledge_report                                   */
/*==============================================================*/
create table tb_knowledge_report
(
   id                   bigint(20) comment 'id',
   knowledge_id         bigint(20) comment '知识id',
   type                 char(2) comment '类型',
   rep_desc             varchar(500) comment '描述',
   createtime           timestamp comment '创建时间',
   user_id              bigint(20) comment '举报人id'
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8;

alter table tb_knowledge_report comment '知识举报表';

/*==============================================================*/
/* Table: tb_knowledge_statics                                  */
/*==============================================================*/
create table tb_knowledge_statics
(
   knowledge_id         bigint(20) not null comment '知识id',
   commentCount         bigint(20) comment '评论数',
   shareCount           bigint(20) comment '分享数',
   collectionCount      bigint(20) comment '收藏数',
   clickCount           bigint(20) comment '点击数',
   source               smallint comment '来源',
   title                varchar(255) comment '标题',
   type                 smallint comment '类型',
   primary key (knowledge_id)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8;

alter table tb_knowledge_statics comment '知识统计表';

 

/*==============================================================*/
/* Table: tb_user_category                                      */
/*==============================================================*/
create table tb_user_category
(
   id                   bigint(20) not null comment 'id',
   userid               bigint(20) comment '用户Id',
   categoryName         varchar(50) comment '目录名称',
   sortid               varchar(255) comment '路径Id，000000001000000001',
   createtime           timestamp comment '创建时间',
   modifytime           timestamp comment '父级id',
   parentid             bigint(20),
   primary key (id)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8;

alter table tb_user_category comment '目录表(左树)';

/*==============================================================*/
/* Table: tb_user_permission                                    */
/*==============================================================*/
create table tb_user_permission
(
   receive_user_id      bigint(20) comment '接收者Id',
   knowledge_id         bigint(20) comment '知识id',
   send_user_id         bigint(20) comment '发起者id',
   type                 int(1) comment '类型（1-收藏，2-分享，3-可见性，4-全平台可见）',
   mento                varchar(255) comment '分享留言',
   createtime           timestamp comment '创建时间',
   column_id            smallint comment '知识所属类目ID，共十一种顶级类目之一'
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8;

alter table tb_user_permission comment '用户权限表';

