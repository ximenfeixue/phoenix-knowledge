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
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

ALTER TABLE tb_attachment COMMENT '附件表';

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
   taskid varchar(255) DEFAULT '',
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

/*根级目录*/
INSERT INTO tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) VALUES("资讯","0","0",STR_TO_DATE("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯","000000001","0",STR_TO_DATE("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
INSERT INTO tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) VALUES("投融工具","0","0",STR_TO_DATE("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"投融工具","000000002","0",STR_TO_DATE("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
INSERT INTO tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) VALUES("行业","0","0",STR_TO_DATE("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"行业","000000003","0",STR_TO_DATE("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
INSERT INTO tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) VALUES("经典案例","0","0",STR_TO_DATE("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"经典案例","000000004","0",STR_TO_DATE("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
INSERT INTO tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) VALUES("图书报告","0","0",STR_TO_DATE("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"图书报告","000000005","0",STR_TO_DATE("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
INSERT INTO tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) VALUES("资产管理","0","0",STR_TO_DATE("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资产管理","000000006","0",STR_TO_DATE("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
INSERT INTO tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) VALUES("宏观","0","0",STR_TO_DATE("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"宏观","000000007","0",STR_TO_DATE("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
INSERT INTO tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) VALUES("观点","0","0",STR_TO_DATE("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"观点","000000008","0",STR_TO_DATE("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
INSERT INTO tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) VALUES("判例","0","0",STR_TO_DATE("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"判例","000000009","0",STR_TO_DATE("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
INSERT INTO tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) VALUES("法律法规","0","0",STR_TO_DATE("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"法律法规","000000010","0",STR_TO_DATE("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
INSERT INTO tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) VALUES("文章","0","0",STR_TO_DATE("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"文章","000000011","0",STR_TO_DATE("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");

/*资讯*/
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("能源矿产","0","1",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/能源矿产","000000001000000001","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("石油与天然气能源设备与服务","0","12",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/能源矿产/石油与天然气能源设备与服务","000000001000000001000000001","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("石油、天然气","0","12",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/能源矿产/石油、天然气","000000001000000001000000002","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("3煤炭","0","12",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/能源矿产/3煤炭","000000001000000001000000003","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("核能","0","12",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/能源矿产/核能","000000001000000001000000004","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("新能源","0","12",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/能源矿产/新能源","000000001000000001000000005","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("页岩气","0","12",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/能源矿产/页岩气","000000001000000001000000006","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("可燃冰","0","12",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/能源矿产/可燃冰","000000001000000001000000007","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("其他能源","0","12",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/能源矿产/其他能源","000000001000000001000000008","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("黑色金属","0","12",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/能源矿产/黑色金属","000000001000000001000000009","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("有色金属","0","12",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/能源矿产/有色金属","000000001000000001000000010","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("贵金属","0","12",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/能源矿产/贵金属","000000001000000001000000011","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("稀有金属","0","12",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/能源矿产/稀有金属","000000001000000001000000012","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("稀土矿","0","12",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/能源矿产/稀土矿","000000001000000001000000013","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("建筑装饰类矿产","0","12",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/能源矿产/建筑装饰类矿产","000000001000000001000000014","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("耐火土石","0","12",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/能源矿产/耐火土石","000000001000000001000000015","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("陶瓷用矿产","0","12",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/能源矿产/陶瓷用矿产","000000001000000001000000016","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("机电工业用矿产","0","12",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/能源矿产/机电工业用矿产","000000001000000001000000017","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("珠宝类矿产","0","12",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/能源矿产/珠宝类矿产","000000001000000001000000018","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("化学原料类矿产","0","12",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/能源矿产/化学原料类矿产","000000001000000001000000019","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("工商业","0","1",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/工商业","000000001000000002","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("航天与国防","0","32",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/工商业/航天与国防","000000001000000002000000001","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("建筑与工程","0","32",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/工商业/建筑与工程","000000001000000002000000002","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("电气设备","0","32",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/工商业/电气设备","000000001000000002000000003","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("机械制造","0","32",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/工商业/机械制造","000000001000000002000000004","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("贸易公司","0","32",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/工商业/贸易公司","000000001000000002000000005","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("钢铁","0","32",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/工商业/钢铁","000000001000000002000000006","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("有色冶金","0","32",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/工商业/有色冶金","000000001000000002000000007","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("其他工业设备制造","0","32",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/工商业/其他工业设备制造","000000001000000002000000008","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("化学制品","0","32",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/工商业/化学制品","000000001000000002000000009","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("橡胶和塑料制品","0","32",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/工商业/橡胶和塑料制品","000000001000000002000000010","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("建筑材料制造","0","32",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/工商业/建筑材料制造","000000001000000002000000011","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("水利和环保","0","32",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/工商业/水利和环保","000000001000000002000000012","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("造纸和林业","0","32",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/工商业/造纸和林业","000000001000000002000000013","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("容器与包装","0","32",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/工商业/容器与包装","000000001000000002000000014","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("商业服务","0","32",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/工商业/商业服务","000000001000000002000000015","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("交通运输","0","32",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/工商业/交通运输","000000001000000002000000016","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("仪器仪表制造","0","32",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/工商业/仪器仪表制造","000000001000000002000000017","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("医药及消费品","0","1",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品","000000001000000003","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("化学制药","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/化学制药","000000001000000003000000001","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("生物制药","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/生物制药","000000001000000003000000002","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("原料药及中间体","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/原料药及中间体","000000001000000003000000003","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("植物药中药饮片","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/植物药中药饮片","000000001000000003000000004","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("医疗器械","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/医疗器械","000000001000000003000000005","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("医疗卫生","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/医疗卫生","000000001000000003000000006","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("医药商业","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/医药商业","000000001000000003000000007","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("制药装备","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/制药装备","000000001000000003000000008","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("汽车与汽车零部件","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/汽车与汽车零部件","000000001000000003000000009","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("家庭耐用消费品","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/家庭耐用消费品","000000001000000003000000010","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("休闲娱乐用品","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/休闲娱乐用品","000000001000000003000000011","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("服装鞋帽纺织品","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/服装鞋帽纺织品","000000001000000003000000012","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("奢侈品","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/奢侈品","000000001000000003000000013","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("零售业","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/零售业","000000001000000003000000014","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("酒店、餐饮及休闲服务","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/酒店、餐饮及休闲服务","000000001000000003000000015","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("教育培训","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/教育培训","000000001000000003000000016","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("食品","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/食品","000000001000000003000000017","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("饮料","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/饮料","000000001000000003000000018","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("日用品","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/日用品","000000001000000003000000019","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("酿酒","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/酿酒","000000001000000003000000020","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("烟草制品","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/烟草制品","000000001000000003000000021","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("皮革服装及包袋","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/皮革服装及包袋","000000001000000003000000022","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("农业","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/农业","000000001000000003000000023","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("林业","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/林业","000000001000000003000000024","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("畜牧业","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/畜牧业","000000001000000003000000025","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("渔业","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/渔业","000000001000000003000000026","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("农林牧渔服务业","0","50",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/医药及消费品/农林牧渔服务业","000000001000000003000000027","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("金融地产","0","1",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/金融地产","000000001000000004","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("银行","0","78",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/金融地产/银行","000000001000000004000000001","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("保险公司","0","78",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/金融地产/保险公司","000000001000000004000000002","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("基金管理公司（原来为基金公司）","0","78",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/金融地产/基金管理公司（原来为基金公司）","000000001000000004000000003","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("私募股权基金","0","78",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/金融地产/私募股权基金","000000001000000004000000004","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("主权财富基金","0","78",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/金融地产/主权财富基金","000000001000000004000000005","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("证券公司与投资银行","0","78",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/金融地产/证券公司与投资银行","000000001000000004000000006","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("期货公司","0","78",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/金融地产/期货公司","000000001000000004000000007","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("信托公司","0","78",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/金融地产/信托公司","000000001000000004000000008","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("融资租赁公司","0","78",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/金融地产/融资租赁公司","000000001000000004000000009","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("典当行","0","78",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/金融地产/典当行","000000001000000004000000010","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("财务公司","0","78",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/金融地产/财务公司","000000001000000004000000011","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("担保公司","0","78",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/金融地产/担保公司","000000001000000004000000012","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("小额贷款公司","0","78",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/金融地产/小额贷款公司","000000001000000004000000013","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("资产管理公司","0","78",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/金融地产/资产管理公司","000000001000000004000000014","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("金融控股公司","0","78",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/金融地产/金融控股公司","000000001000000004000000015","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("交易所","0","78",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/金融地产/交易所","000000001000000004000000016","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("其他金融公司","0","78",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/金融地产/其他金融公司","000000001000000004000000017","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("房地产开发与经营","0","78",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/金融地产/房地产开发与经营","000000001000000004000000018","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("房地产经纪","0","78",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/金融地产/房地产经纪","000000001000000004000000019","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("物业管理","0","78",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/金融地产/物业管理","000000001000000004000000020","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("房地产投资信托","0","78",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/金融地产/房地产投资信托","000000001000000004000000021","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("建筑装饰业","0","78",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/金融地产/建筑装饰业","000000001000000004000000022","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("规划设计","0","78",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/金融地产/规划设计","000000001000000004000000023","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("公用事业","0","1",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/公用事业","000000001000000005","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("电力生产","0","102",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/公用事业/电力生产","000000001000000005000000001","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("电网","0","102",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/公用事业/电网","000000001000000005000000002","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("热力生产和供应","0","102",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/公用事业/热力生产和供应","000000001000000005000000003","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("燃气生产和供应","0","102",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/公用事业/燃气生产和供应","000000001000000005000000004","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("自来水生产和供应","0","102",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/公用事业/自来水生产和供应","000000001000000005000000005","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("TMT","0","1",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/TMT","000000001000000006","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("计算机、通信和其他电子设备制造","0","108",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/TMT/计算机、通信和其他电子设备制造","000000001000000006000000001","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("信息传输服务","0","108",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/TMT/信息传输服务","000000001000000006000000002","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("软件和信息技术服务","0","108",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/TMT/软件和信息技术服务","000000001000000006000000003","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("传媒","0","108",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/TMT/传媒","000000001000000006000000004","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("搜索引擎","0","108",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/TMT/搜索引擎","000000001000000006000000005","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("门户网站","0","108",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/TMT/门户网站","000000001000000006000000006","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("电子商务","0","108",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/TMT/电子商务","000000001000000006000000007","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("团购","0","108",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/TMT/团购","000000001000000006000000008","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("互联网金融","0","108",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/TMT/互联网金融","000000001000000006000000009","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("即时通讯","0","108",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/TMT/即时通讯","000000001000000006000000010","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("网络游戏","0","108",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/TMT/网络游戏","000000001000000006000000011","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("SNS","0","108",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/TMT/SNS","000000001000000006000000012","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("视频网站","0","108",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/TMT/视频网站","000000001000000006000000013","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("网络招聘","0","108",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/TMT/网络招聘","000000001000000006000000014","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("信息安全","0","108",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/TMT/信息安全","000000001000000006000000015","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("应用平台","0","108",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/TMT/应用平台","000000001000000006000000016","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("云计算","0","108",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/TMT/云计算","000000001000000006000000017","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("物联网","0","108",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/TMT/物联网","000000001000000006000000018","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("大数据","0","108",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/TMT/大数据","000000001000000006000000019","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("手机应用","0","108",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/TMT/手机应用","000000001000000006000000020","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("移动支付","0","108",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/TMT/移动支付","000000001000000006000000021","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("其他互联网内容与应用","0","108",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/TMT/其他互联网内容与应用","000000001000000006000000022","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("投融工具","0","1",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/投融工具","000000001000000007","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("银行","0","131",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/投融工具/银行","000000001000000007000000001","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("保险","0","131",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/投融工具/保险","000000001000000007000000002","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("债券","0","131",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/投融工具/债券","000000001000000007000000003","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("信托","0","131",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/投融工具/信托","000000001000000007000000004","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("担保","0","131",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/投融工具/担保","000000001000000007000000005","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("上市","0","131",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/投融工具/上市","000000001000000007000000006","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("并购","0","131",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/投融工具/并购","000000001000000007000000007","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("财务公司","0","131",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/投融工具/财务公司","000000001000000007000000008","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("融资租赁","0","131",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/投融工具/融资租赁","000000001000000007000000009","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("典当","0","131",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/投融工具/典当","000000001000000007000000010","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("PE/VC","0","131",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/投融工具/PE/VC","000000001000000007000000011","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("艺术品","0","131",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/投融工具/艺术品","000000001000000007000000012","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("黄金","0","131",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/投融工具/黄金","000000001000000007000000013","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("外汇","0","131",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/投融工具/外汇","000000001000000007000000014","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("期货","0","131",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/投融工具/期货","000000001000000007000000015","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("资产管理","0","131",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"资讯/投融工具/资产管理","000000001000000007000000016","0",str_to_date("2014-10-30 19:20:01","%Y-%m-%d %h:%i:%s"),"0");


/*投融工具*/
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("银行","0","2",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"投融工具/银行","000000002000000001","0",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("保险","0","2",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"投融工具/保险","000000002000000002","0",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("债券","0","2",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"投融工具/债券","000000002000000003","0",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("信托","0","2",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"投融工具/信托","000000002000000004","0",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("担保","0","2",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"投融工具/担保","000000002000000005","0",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("上市","0","2",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"投融工具/上市","000000002000000006","0",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("并购","0","2",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"投融工具/并购","000000002000000007","0",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("财务公司","0","2",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"投融工具/财务公司","000000002000000008","0",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("融资租赁","0","2",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"投融工具/融资租赁","000000002000000009","0",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("典当","0","2",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"投融工具/典当","000000002000000010","0",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("PE/VC","0","2",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"投融工具/PE/VC","000000002000000011","0",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("艺术品","0","2",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"投融工具/艺术品","000000002000000012","0",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("黄金","0","2",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"投融工具/黄金","000000002000000013","0",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("外汇","0","2",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"投融工具/外汇","000000002000000014","0",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("期货","0","2",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"投融工具/期货","000000002000000015","0",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("资产管理","0","2",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"投融工具/资产管理","000000002000000016","0",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("法律","0","2",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"投融工具/法律","000000002000000017","0",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("会计","0","2",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"投融工具/会计","000000002000000018","0",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("资产评估","0","2",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"投融工具/资产评估","000000002000000019","0",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"0");
insert into tb_column  (columnName,user_id,parent_id,createtime,path_name,column_level_path,del_status,update_time,subscribe_count) values("财经公关","0","2",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"投融工具/财经公关","000000002000000020","0",str_to_date("2014-10-30 19:43:52","%Y-%m-%d %h:%i:%s"),"0");
