create table article
(
    id          bigint auto_increment comment '主键ID'
        primary key,
    title       varchar(255)                         not null comment '标题',
    category    varchar(50)                          not null comment '分类',
    summary     varchar(500)                         null comment '摘要',
    content     longtext                             null comment '内容',
    cover       varchar(255)                         null comment '封面图片URL',
    video_url   varchar(255)                         null comment '视频链接',
    type        int        default 0                 null comment '0:文章 1:视频',
    views       int        default 0                 null comment '浏览量',
    create_time datetime   default CURRENT_TIMESTAMP null comment '发布时间',
    update_time datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    version     int        default 1                 null comment '版本号',
    deleted     tinyint(1) default 0                 null comment '逻辑删除'
)
    comment '知识普及文章表' charset = utf8mb4;

create table forum_comment
(
    id          bigint auto_increment
        primary key,
    post_id     bigint                               not null comment '帖子ID',
    user_id     bigint                               not null comment '评论人ID',
    username    varchar(50)                          null comment '评论人用户名(冗余)',
    content     varchar(1000)                        not null comment '评论内容',
    create_time datetime   default CURRENT_TIMESTAMP null comment '评论时间',
    update_time datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    version     int        default 1                 null comment '版本号',
    deleted     tinyint(1) default 0                 null comment '逻辑删除'
)
    comment '论坛评论表' charset = utf8mb4;

create table forum_post
(
    id          bigint auto_increment
        primary key,
    user_id     bigint                               not null comment '发帖人ID',
    username    varchar(50)                          null comment '发帖人用户名(冗余)',
    title       varchar(255)                         not null comment '标题',
    content     text                                 null comment '内容',
    views       int        default 0                 null comment '浏览量',
    create_time datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    version     int        default 1                 null comment '版本号',
    deleted     tinyint(1) default 0                 null comment '逻辑删除'
)
    comment '论坛帖子表' charset = utf8mb4;

create table health_record
(
    id           bigint unsigned auto_increment comment '主键ID（自增主键）'
        primary key,
    user_id      bigint unsigned not null comment '用户ID',
    weight       decimal(5, 2)   null comment '体重',
    height       decimal(5, 2)   null comment '身高',
    temperature  double          null comment '体温',
    systolic     int             null comment '收缩压',
    diastolic    int             null comment '舒张压',
    sleep_hours  decimal(3, 1)   null comment '昨日睡眠时长',
    heart_rate   int             null comment '心率',
    blood_sugar  double          null comment '血糖',
    water_intake int             null comment '饮水量',
    steps        int             null comment '步数',
    mood         varchar(20)     null comment '心情',
    create_time  datetime        not null comment '创建时间',
    update_time  datetime        not null comment '最后更新时间',
    deleted      int             null comment '是否逻辑删除，0为未删除，非0为已删除',
    version      int             null comment '乐观锁版本号'
)
    comment '健康记录表' charset = utf8mb4;
create table news_info
(
    id           bigint auto_increment
        primary key,
    title        varchar(255)                          not null comment '标题',
    summary      varchar(500)                          null comment '摘要',
    source       varchar(100)                          null comment '来源',
    level        varchar(20) default 'info'            null comment '紧急等级 info/warning',
    content      longtext                              null comment '内容',
    publish_time datetime    default CURRENT_TIMESTAMP null comment '发布时间',
    create_time  datetime    default CURRENT_TIMESTAMP null comment '创建时间',
    update_time  datetime    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    version      int         default 1                 null comment '版本号',
    deleted      tinyint(1)  default 0                 null comment '逻辑删除'
)
    comment '疫情动态表' charset = utf8mb4;

create table user_operate_stream
(
    id           bigint unsigned auto_increment comment '主键ID（自增主键）'
        primary key,
    create_time  datetime    not null comment '创建时间',
    update_time  datetime    not null comment '最后更新时间',
    user_id      varchar(64) null comment '用户ID',
    operate_type varchar(64) null comment '操作类型',
    operate_time datetime    null comment '操作时间',
    param        text        null comment '操作参数',
    extend_info  text        null comment '扩展字段',
    deleted      int         null comment '是否逻辑删除，0为未删除，非0为已删除',
    version      int         null comment '乐观锁版本号'
);

create table users
(
    id              bigint unsigned auto_increment comment '主键ID（自增主键）'
        primary key,
    create_time     datetime     not null comment '创建时间',
    update_time     datetime     not null comment '最后更新时间',
    nick_name       varchar(255) null comment '用户昵称',
    password        varchar(255) null comment '密码哈希',
    state           varchar(64)  null comment '用户状态',
    role            varchar(64)  null comment '用户角色',
    telephone       varchar(20)  null comment '手机号码',
    last_login_time datetime     null comment '最后登录时间',
    avatar          varchar(255) null comment '用户头像URL',
    certification   tinyint(1)   null comment '实名认证状态（TRUE或FALSE）',
    real_name       varchar(255) null comment '真实姓名',
    id_card_hash    varchar(255) null comment '身份证哈希',
    deleted         int          null comment '是否逻辑删除，0为未删除，非0为已删除',
    version         int          null comment '乐观锁版本号',
    email           varchar(128) null comment '邮箱'
);
