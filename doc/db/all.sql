use course;
drop table if exists `tb_test`;

 create table `tb_test`(
  	`test_id` INT(2) NOT null AUTO_INCREMENT,
  	`test_name` VARCHAR(200) NOT NULL,
  	 PRIMARY KEY(`test_id`)
  )ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

create table `chapter`(
    `id` char(8) not null comment 'ID',
    `course_id` char(8) comment '课程ID',
    `name` varchar(50) comment '名称',
    primary key (id)
)engine=innodb default charset = utf8mb4 comment '大章';

insert into chapter(id,course_id,name) values(1,100,'雪瞳');
insert into chapter(id,course_id,name) values(2,200,'蝶恋');

create TABLE `section`(
    `id` char(8) not null default '' comment 'ID',
    `title` varchar(50) not null comment '标题',
    `course_id` char(8) comment '课程|course.id',
    `chapter_id` char(8) comment '大章|chapter.id',
    `video` varchar(200) comment '视频',
    `time` int comment '时长|单位秒',
    `charge` char(1) comment '收费|C收费F免费',
    `sort` int comment '顺寻',
    `created_at` DATETIME(3) comment '创建时间',
    `updated_at` DATETIME(3) comment '修改时间',
    primary key (`id`)
)engine=innodb default charset = utf8mb4 comment '小节';

insert into section(id, title, course_id, chapter_id, video, time, charge, sort, created_at, updated_at) values
(1,'雪瞳',11,12,'test',null,null,1,null,null);

create table `course`(
    id char(8) not null default '' comment 'id',
    name varchar (50) not null comment '名称',
    summary varchar (2000) comment '概述',
    time int default 0 comment '时长|单位秒',
    price decimal(8,2) not null default 0.00 comment '价格(元)',
    image  varchar(100) comment '封面',
    level char (1) not null comment '级别|ONE("1","初级"),TWO("2","中级"),THREE("3","高级")',
    charge char (1) comment '收费|CHARGE("C","收费"),FREE("F","免费")',
    status char (1) comment '状态|PUBLISH("P","发布"),DRFT("D","草稿")',
    enroll integer default 0 comment '报名数',
    sort int comment '顺序',
    create_at datetime(3) comment '创建时间',
    update_at datetime(3) comment '修改时间',
    primary key (id)
)engine=innodb default charset = utf8mb4 comment '课程表';

insert into course(id, name,level) values (1,'雪瞳',1);
select *from course

drop table if exists `category`;
create table `category`(
    `id` char(8) not null default '' comment 'id',
    `parent` char (8) not null default '' comment '父id',
    `name` varchar (50) not null comment '名称',
    `sort` int comment '顺序',
    primary key (`id`)
)engine=innodb default charset = utf8mb4 comment '分类表';

insert into category(id, parent, name, sort) VALUES ('00000100','00000000','前端技术',100);
insert into category(id, parent, name, sort) VALUES ('00000101','00000100','html',101);
insert into category(id, parent, name, sort) VALUES ('00000102','00000100','javascript',102);
insert into category(id, parent, name, sort) VALUES ('00000103','00000100','vue',103);
insert into category(id, parent, name, sort) VALUES ('00000104','00000100','react',104);
insert into category(id, parent, name, sort) VALUES ('00000105','00000100','angular',105);
insert into category(id, parent, name, sort) VALUES ('00000106','00000100','node',106);
insert into category(id, parent, name, sort) VALUES ('00000107','00000100','jquery',107);
insert into category(id, parent, name, sort) VALUES ('00000108','00000100','小程序',108);
insert into category(id, parent, name, sort) VALUES ('00000200','00000000','后端技术',200);
insert into category(id, parent, name, sort) VALUES ('00000201','00000200','java',201);
insert into category(id, parent, name, sort) VALUES ('00000202','00000200','spring',202);
insert into category(id, parent, name, sort) VALUES ('00000203','00000200','springboot',203);
insert into category(id, parent, name, sort) VALUES ('00000204','00000200','springcloud',204);
insert into category(id, parent, name, sort) VALUES ('00000205','00000200','mybatis',205);
insert into category(id, parent, name, sort) VALUES ('00000206','00000200','python',206);
insert into category(id, parent, name, sort) VALUES ('00000207','00000200','sprak',207);
insert into category(id, parent, name, sort) VALUES ('00000208','00000200','hadoop',208);
select * from category;

drop table if exists `course_category`;
create table `course_category`(
    `id` char (8) not null default '' comment 'id',
    `course_id` char(8) comment '课程|course.id',
    `category_id` char (8) comment '分类|category.id',
    primary key (id)
)engine=innodb default charset = utf8mb4 comment '课程分类表';
select * from course_category;

drop table if exists `course_content`;
create table `course_content`(
    `id` char (8) not null default '' comment '课程id',
    `content` mediumtext not null comment '课程内容',
    primary key (id)
)engine=innodb default charset = utf8mb4 comment '课程内容表';
select * from course_content;

drop table if exists `teacher`;
create table `teacher`(
    `id` char(8) not null default '' comment 'id',
    `name` varchar (50) not null comment '姓名',
    `nickname` varchar (50) comment '昵称',
    `image` varchar (100) comment '头像',
    `position` varchar (50) comment '职位',
    `motto` varchar (50) comment '座右铭',
    `intro` varchar (500) comment '简介',
    primary key (`id`)
)engine=innodb default charset = utf8mb4 comment '讲师表';
select * from teacher

alter table `course` add column (`teacher_id` char(8) comment '讲师|教师id');

-- 文件表
drop table if exists `file`;
create table `file`(
    `id` char(8) not null default '' comment 'id',
    `path` varchar (100) not null comment '相对路径',
    `name` varchar (100) comment '文件名',
    `suffix` varchar (10) comment '后缀',
    `size` int comment '大小|字节B',
    `use` char (1) comment '用途|枚举[FileUseEnum]:COURSE("C","课程"),TEACHER("T","教师")',
    `created_at` datetime(3) comment '创建时间',
    `updated_at` datetime(3) comment '修改时间',
    primary key (`id`),
    unique key `path_unique` (`path`) --唯一键
)engine=innodb default charset = utf8mb4 comment '文件';


-- 课程内容文件表
drop table if exists `course_content_file`;
create table `course_content_file`(
  `id` char(8) not null default '' comment 'id',
  `course_id` char (8) not null comment '课程id',
  `url` varchar (100) comment '地址',
  `name` varchar (100) comment '文件名',
  `size` int comment '大小|字节b',
  primary key (`id`)
)engine=innodb default charset = utf8mb4 comment '课程内容文件';

alter table `file` add column (`shard_index` int comment '已上传分片');
alter table `file` add column (`shard_size` int comment '分片大小|B');
alter table `file` add column (`shard_total` int comment '分片总数');
alter table `file` add column (`key` varchar (32) comment '文件标识');
alter table `file` add unique key key_unique (`key`);


alter table `section` add column (`vod` char (32) comment 'vod|阿里云vod');
alter table `file` add column (`vod` char (32) comment 'vod|阿里云vod');


drop table if exists `user`;
create table `user`(
    `id` char(8) not null default '' comment 'id',
    `login_name` varchar (50) not null comment '登录名',
    `name` varchar (50) comment '昵称',
    `password` char(32) not null comment '密码',
    primary key (`id`),
    unique key `login_name_unique` (`login_name`)
)engine=innodb default charset = utf8mb4 comment '用户';

insert into `user` values ('10000000','雪瞳','雪瞳','123');

-- 权限拦截的对象 用户
-- 权限拦截的点 菜单 路由 接口 按钮
-- 控制用户对资源的访问
-- 权限的操作 配置、读取、拦截
-- 角色 用户 资源
-- 经典权限管理设计  ==> 用户和角色管理、角色和资源关联
-- 功能点 配置、读取、拦截
-- 配置 ：   用户管理: 用户表、界面 已完成
--          资源管理：资源表、资源配置界面
--          角色管理：角色表、角色管理界面
--          用户角色管理配置 用户角色管理表 复用角色管理界面
--          角色资源关联配置 角色资源关联配置表 复用角色管理界面
-- 读取：  用户权限读取 用户登陆，读取该用户所有权限
-- 拦截：  用户操作业务进行权限拦截
--          前端界面拦截 菜单、路由、按钮 hidden disabled
--          后端 接口 gateway过滤器
-- 权限初始化

-- 资源表
drop table if exists`resources`;
create table `resources`(
    `id` char(6) not null default '' comment 'id',
    `name` varchar (100) not null comment '名称|菜单按钮',
    `page` varchar (50) null comment '页面|路由',
    `request` varchar (400) null comment '请求|接口',
    `parent` char (6) comment '父id',
    primary key (`id`)
)engine=innodb default charset = utf8mb4 comment '资源';

insert into `resources` values ('01','系统管理',null,null,null);
insert into `resources` values ('0101','用户管理','/system/user',null,'01');
insert into `resources` values ('010101','保存',null,'["/system/admin/user/saveuserinfo","/system/admin/user/queryuserinfo"]','0101');
insert into `resources` values ('010102','删除',null,'["/system/admin/user/deleteuserinfo"]','0101');
insert into `resources` values ('010103','重置密码',null,'["/system/admin/user/savepassword"]','0101');

insert into `resources` values ('0102','资源管理','/system/resources',null,'01');
insert into `resources` values ('010201','保存/显示',null,'["/system/admin/saveresource"]','0102');

insert into `resources` values ('0103','角色管理','/system/role',null,'01');
insert into `resources` values ('010301','角色/权限管理',null,'["/system/admin/editrole"]','0103');


-- 角色表
drop table if exists `role`;
create table `role`(
    `id` char (8) not null default '' comment 'id',
    `name` varchar (50) not null comment '角色',
    `desc` varchar (100) not null comment '描述',
    primary key (`id`)
)engine=innodb default charset = utf8mb4 comment '角色';

insert into `role` values ('00000000','系统管理员','管理用户、角色权限');
insert into `role` values ('00000001','开发','资源维护');
insert into `role` values ('00000002','业务管理员','业务管理');


-- 角色资源关联表
drop table if exists `role_resources`;
create table `role_resources`(
    `id` char (8) not null default '' comment 'id',
    `role_id` char (8) not null comment '角色id',
    `resources_id` char(6) not null comment '资源id',
    primary key (`id`)
)engine=innodb default charset = utf8mb4 comment '角色资源关联表';

insert into `role_resources` values ('00000000','00000000','01');
insert into `role_resources` values ('00000001','00000000','0101');
insert into `role_resources` values ('00000002','00000000','010101');
insert into `role_resources` values ('00000003','00000000','010102');
insert into `role_resources` values ('00000004','00000000','010103');
insert into `role_resources` values ('00000005','00000000','0102');
insert into `role_resources` values ('00000006','00000000','010201');
insert into `role_resources` values ('00000007','00000000','0103');
insert into `role_resources` values ('00000008','00000000','010301');


-- 角色用户关联表
drop table if exists `role_user`;
create table `role_user`(
    `id` char (8) not null default '' comment 'id',
    `role_id` char (8) not null comment '角色id',
    `user_id` char(8) not null comment '用户id',
    primary key (`id`)
)engine=innodb default charset = utf8mb4 comment '角色用户关联表';

insert into role_user values ('00000000','00000000','10000000');