create table `ry-vue`.t_carline
(
    uid                bigint(32) auto_increment comment '唯一uid'
        primary key,
    carline_model_type varchar(10)                              null comment '汽车名称',
    golden_car_name    varchar(32)                              null comment '版本规范车名称',
    status             tinyint(1) default 1                     null comment '当前车辆状态 ',
    create_time        timestamp  default '0000-00-00 00:00:00' null comment '创建时间',
    update_time        timestamp  default '0000-00-00 00:00:00' null comment '更新时间',
    create_by_uid      varchar(64)                              null comment '创建者',
    update_by_uid      varchar(64)                              null comment '更新者',
    sort               int        default 0                     null comment '排序字段越大越靠前',
    carline_icon       varchar(50)                              null comment '车标',
    is_show            tinyint(1) default 1                     null comment '是否显示：1显示0 不显示'
)
    comment '`车型基本数据表`' charset = utf8;

create table `ry-vue`.t_carline_component
(
    uid                bigint(32) auto_increment comment '组件主键'
        primary key,
    component_uid      bigint(32)                              not null comment '组件主键',
    carline_info_uid   bigint(32)                              not null comment '车型参数主键',
    minimal_hw         varchar(32)                             null comment '最低配置',
    part_number        varchar(50)                             null comment '序列号',
    temporary_variable varchar(40)                             null,
    create_time        timestamp default '0000-00-00 00:00:00' not null comment '创建时间',
    update_time        timestamp default '0000-00-00 00:00:00' not null comment '更新时间',
    create_by_uid      varchar(64)                             null comment '创建者',
    sort               int       default 0                     null comment '排序字段',
    update_by_uid      varchar(64)                             null comment '更新者'
)
    comment '`设备-组件关联表（n2n）`' charset = utf8;

create table `ry-vue`.t_carline_info
(
    carline_info_uid bigint(32) auto_increment comment '主键'
        primary key,
    cluster_uid      varchar(10)                               null comment '版本号id',
    market_type      varchar(50)                               null comment '区域中国（zn）台湾(z1)香港(z2)北美(na0)东南亚(as0)，逗号分割',
    device_name      varchar(32)                               null,
    db_version       varchar(10)                               null comment '数据库版本',
    resp             varchar(255)                              null comment '反馈',
    variant_type     varchar(5)                                null comment 'variant',
    vin_code         varchar(50)                               null comment 'vin码',
    platform_type    varchar(10)                               null comment '平台类型',
    basic_type       varchar(30) default '‘0’'                 not null comment '类型',
    create_time      timestamp   default '0000-00-00 00:00:00' not null comment '创建时间',
    update_time      timestamp   default '0000-00-00 00:00:00' not null comment '更新时间',
    create_by_uid    varchar(64)                               null comment '创建者',
    update_by_uid    varchar(64)                               null comment '更新者'
)
    comment '`虚拟设备参数表`' charset = utf8;

create table `ry-vue`.t_category_menu
(
    uid                  bigint(32) auto_increment comment '主键'
        primary key,
    menu_name            varchar(255)                               not null comment '菜单名称',
    menu_level           tinyint(1)                                 null comment '菜单级别',
    summary              varchar(200)                               null comment '简介',
    parent_uid           varchar(32)                                null comment '父uid',
    url                  varchar(255)                               null comment 'url地址',
    icon                 varchar(50)                                null comment '图标',
    sort                 int                                        null comment '排序字段越大越靠前',
    status               tinyint(1)   default 1                     null comment '状态（1正常0停用）',
    create_time          timestamp    default '0000-00-00 00:00:00' not null comment '创建时间',
    update_time          timestamp    default '0000-00-00 00:00:00' not null comment '更新时间',
    is_show              tinyint(1)   default 1                     null comment '是否显示 1:是 0:否',
    menu_type            tinyint(1)   default 0                     not null comment '菜单类型 0: 菜单   1: 按钮',
    is_jump_external_url tinyint(1)   default 0                     null comment '是否跳转外部链接 0：否1：是',
    create_by_uid        varchar(64)                                null comment '创建者',
    update_by_uid        varchar(64)                                null comment '更新者',
    path                 varchar(200) default '""'                  null comment '路由地址',
    component            varchar(255)                               null comment '组件路径',
    query                varchar(255)                               null comment '路由参数',
    is_cache             tinyint(1)   default 0                     null comment '是否缓存（1缓存0不缓存）',
    perms                varchar(100)                               null comment '权限标识'
)
    comment '`菜单权限表`' charset = utf8;

create table `ry-vue`.t_clean_mapping
(
    uid           bigint(32) auto_increment comment '主键'
        primary key,
    confuse_data  varchar(100)                            null comment '混乱值',
    guess_value   varchar(100)                            null comment '猜想值',
    use_scene     varchar(30)                             null comment '使用场景',
    create_time   timestamp default '0000-00-00 00:00:00' not null comment '创建时间',
    update_time   timestamp default '0000-00-00 00:00:00' not null comment '更新时间',
    create_by_uid varchar(64)                             null comment '创建者',
    update_by_uid varchar(64)                             null comment '更新者'
)
    comment '`BIGINT(32)`' charset = utf8;

create table `ry-vue`.t_cluster
(
    uid                   bigint(32) auto_increment comment '唯一uid'
        primary key,
    carline_uid           varchar(32)                              null comment '车基础信息id',
    cluster_name          varchar(32)                              null comment '版本名称',
    project_type          varchar(32)                              null comment '工程id',
    device_type           varchar(10)                              not null comment 'bc代表bench，car代表car，gd代表目标goldeninfo',
    cluster_update_point  varchar(32)                              null comment '更新点',
    cluster_update_by_uid varchar(32)                              null comment '更新者主键',
    last_updated          varchar(30)                              null comment '最近更新',
    status                tinyint(1) default 1                     null comment '当前版本状态 ',
    device_circle_num               tinyint(1) default 1                     not null comment '1-10的循环',
    create_time           timestamp  default '0000-00-00 00:00:00' not null comment '创建时间',
    update_time           timestamp  default '0000-00-00 00:00:00' not null comment '更新时间',
    create_by_uid         varchar(64)                              null comment '创建者',
    update_by_uid         varchar(64)                              null comment '更新者',
    sort                  int        default 0                     null comment '排序字段越大越靠前',
    is_show               tinyint(1) default 1                     null comment '是否显示：1显示0 不显示'
)
    comment '`版本管控表`' charset = utf8;

create table `ry-vue`.t_component_data
(
    uid                bigint(32) auto_increment comment '序列号，唯一标志主键'
        primary key,
    component_type     varchar(255)                            null comment 'mu代表mu，gw代表gw，hud代表hud，km代表kombi等',
    component_version  varchar(32)                             null comment '配件版本',
    ware_type          varchar(10)                             null comment 'hw代表硬件，sw软件，ot代表其他',
    sw_version         varchar(100)                            null,
    part_number        varchar(100)                            null,
    temporary_variable varchar(100)                            null,
    hw_version         varchar(100)                            null,
    minimal_hw         varchar(100)                            null,
    create_time        timestamp default '0000-00-00 00:00:00' not null comment '创建时间',
    update_time        timestamp default '0000-00-00 00:00:00' not null comment '更新时间',
    create_by_uid      varchar(64)                             null comment '创建者',
    update_by_uid      varchar(64)                             null comment '更新者',
    component_name     varchar(10)                             null comment '配件名称'
)
    comment '`组件参数表`' charset = utf8;

create table `ry-vue`.t_matrix
(
    uid                      bigint(32) auto_increment comment '序列号，唯一标志主键'
        primary key,
    matrix_type              varchar(10)                             null comment 'matrix类型，1为matrix下的，2为validateAll下的',
    golden_car_type          varchar(10)                             null comment '目标车类型',
    golden_cluster_name_type varchar(10)                             null comment '目标车版本名称',
    carline_model_type       varchar(10)                             null comment '汽车名称',
    cluster_name             varchar(10)                             null comment '版本号名称',
    platform_type            varchar(10)                             null comment '平台类型',
    project_type             varchar(10)                             null comment '工程id',
    variant_type             varchar(10)                             null comment 'variant',
    market_types             varchar(50)                             null comment '区域中国（zn）台湾(z1)香港(z2)北美(na0)东南亚(as0)，逗号分割',
    ocu_cbox_type            varchar(10)                             null comment 'ocu_cbox',
    gateway_type             varchar(10)                             null comment 'gateway',
    create_time              timestamp default '0000-00-00 00:00:00' not null comment '创建时间',
    update_time              timestamp default '0000-00-00 00:00:00' not null comment '更新时间',
    create_by_uid            varchar(64)                             null comment '创建者',
    update_by_uid            varchar(64)                             null comment '更新者'
)
    comment '`BIGINT(32)`' charset = utf8;

create table `ry-vue`.t_role
(
    uid                bigint(32) auto_increment comment '角色id主键'
        primary key,
    role_name          varchar(255)                             not null comment '角色名',
    role_key           varchar(50)                              null comment '角色权限字符串',
    role_sort          int(4)     default 0                     null comment '显示顺序',
    create_time        timestamp  default '0000-00-00 00:00:00' not null comment '创建时间',
    update_time        timestamp  default '0000-00-00 00:00:00' not null comment '更新时间',
    create_by_uid      varchar(64)                              null comment '创建者',
    update_by_uid      varchar(64)                              null comment '更新者',
    status             tinyint(1) default 1                     null comment '角色状态（1正常0停用）',
    del_flag           char       default '1'                   not null comment '删除标志（1代表存在0代表删除）',
    summary            varchar(255)                             null comment '角色介绍',
    category_menu_uids text                                     null comment '角色管辖的菜单的UID'
)
    comment '`角色表`' charset = utf8;

create table `ry-vue`.t_sys_dict_data
(
    uid           bigint(32) auto_increment comment '主键'
        primary key,
    oid           int                                      null comment '自增键oid',
    dict_type_uid varchar(255)                             null comment '字典类型UID',
    dict_label    varchar(255)                             null comment '字典标签',
    dict_value    varchar(255)                             null comment '字典键值',
    css_class     varchar(255)                             null comment '样式属性（其他样式扩展）',
    list_class    varchar(255)                             null comment '表格回显样式',
    is_default    tinyint(1) default 0                     null comment '是否默认（1是0否）',
    create_by_uid varchar(32)                              null comment '创建人UID',
    update_by_uid varchar(32)                              null comment '最后更新人UID',
    remark        varchar(255)                             null comment '备注',
    status        tinyint(1) default 1                     null comment '状态',
    create_time   timestamp  default '0000-00-00 00:00:00' not null comment '创建时间',
    update_time   timestamp  default '0000-00-00 00:00:00' not null comment '更新时间',
    is_publish    tinyint(1) default 1                     null comment '是否发布(1:是0:否)',
    sort          int        default 0                     not null comment '排序字段'
)
    comment '`字典数据表`' charset = utf8;

create table `ry-vue`.t_sys_dict_type
(
    uid           bigint(32) auto_increment comment '主键'
        primary key,
    oid           int                                      not null comment '自增键oid',
    dict_name     varchar(255)                             null comment '字典名称',
    dict_type     varchar(255)                             null comment '字典类型',
    create_by_uid varchar(32)                              null comment '创建人UID',
    update_by_uid varchar(32)                              null comment '最后更新人UID',
    remark        varchar(255)                             null comment '备注',
    status        tinyint(1) default 1                     null comment '状态',
    create_time   timestamp  default '0000-00-00 00:00:00' not null comment '创建时间',
    update_time   timestamp  default '0000-00-00 00:00:00' not null comment '更新时间',
    is_publish    tinyint(1) default 1                     null comment '是否发布(1:是0:否)',
    sort          int        default 0                     not null comment '排序字段'
)
    comment '`字典类型表`' charset = utf8;

create table `ry-vue`.t_sys_logininfor
(
    info_id        bigint(32) auto_increment comment 'auto_increment'
        primary key,
    user_name      varchar(50)                             not null comment '用户账号',
    ipaddr         varchar(128)                            null comment '登录IP地址',
    login_location varchar(255)                            null comment '登录地点',
    browser        varchar(50)                             null comment '浏览器类型',
    os             varchar(50)                             null comment '操作系统',
    status         char      default '1'                   null comment '登录状态（1成功0失败）',
    msg            varchar(255)                            null comment '提示消息',
    login_time     timestamp default '0000-00-00 00:00:00' not null comment '访问时间'
)
    comment '`系统访问记录表`' charset = utf8;

create table `ry-vue`.t_sys_oper_log
(
    oper_id        bigint(32) auto_increment comment 'auto_increment'
        primary key,
    title          varchar(50)                             null comment '模块标题',
    method         varchar(100)                            null comment '方法名称',
    request_method varchar(10)                             null comment '请求方式',
    oper_user_id   varchar(50)                             null comment '操作人员id',
    dept_name      varchar(50)                             null comment '部门名称',
    oper_time      timestamp default '0000-00-00 00:00:00' not null comment '操作时间',
    oper_url       varchar(255)                            null comment '请求URL',
    oper_ip        varchar(128)                            null comment '主机地址',
    oper_location  varchar(255)                            null comment '操作地点',
    oper_param     varchar(2000)                           null comment '请求参数',
    json_result    varchar(2000)                           null comment '返回参数',
    status         int(1)    default 0                     null comment '操作状态',
    error_msg      varchar(2000)                           null comment '错误消息'
)
    comment '`操作日志记录表`' charset = utf8;

create table `ry-vue`.t_user
(
    uid             bigint(32) auto_increment comment '唯一uid主键'
        primary key,
    user_name       varchar(255)                              not null comment '用户名',
    pass_word       varchar(32)                               not null comment '密码',
    nick_name       varchar(30)                               null comment '昵称',
    gender          tinyint(1)                                null comment '性别(1:男2:女)',
    email           varchar(60)                               null comment '邮箱',
    birthday        date                                      null comment '出生年月日',
    mobile          varchar(50)                               null comment '手机',
    valid_code      varchar(255)                              null comment '邮箱验证码',
    login_count     int         default 0                     null comment '登录次数',
    last_login_time timestamp   default '0000-00-00 00:00:00' not null comment '最后登录时间',
    last_login_ip   varchar(50) default '127.0.0.1'           null comment '最后登录IP',
    create_time     timestamp   default '0000-00-00 00:00:00' not null comment '创建时间',
    update_time     timestamp   default '0000-00-00 00:00:00' not null comment '更新时间',
    user_type       tinyint(1)  default 0                     not null comment '用户标签：0：普通用户1：管理员2：博主',
    loading_valid   tinyint(1)  default 0                     not null comment '是否通过加载校验【0未通过1通过）',
    status          char        default '1'                   not null comment '帐号状态（1正常0停用）',
    del_flag        char        default '1'                   not null comment '删除标志（1代表存在0代表删除）',
    create_by_uid   varchar(64)                               null comment '创建者',
    update_by_uid   varchar(64)                               null comment '更新者',
    remark          varchar(500)                              null comment '备注',
    occupation      varchar(255)                              null comment '职业',
    role_uid        varchar(32)                               null comment '拥有的角色uid'
)
    comment '`用户表`' charset = utf8;

