create table `ry-vue`.sys_dict_data
(
    dict_code    bigint auto_increment comment '字典编码'
        primary key,
    matrix_type  varchar(32)              null,
    dict_label   varchar(100) default ''  null comment '字典标签',
    dict_value   varchar(100) default ''  null comment '字典键值',
    dict_type    varchar(100) default ''  null comment '字典类型',
    status       char         default '0' null comment '状态（0正常 1停用）',
    dict_sort    int(4)       default 0   null comment '字典排序',
    params       varchar(30)              null,
    css_class    varchar(100)             null comment '样式属性（其他样式扩展）',
    list_class   varchar(100)             null comment '表格回显样式',
    is_default   char         default 'N' null comment '是否默认（Y是 N否）',
    create_by    varchar(64)  default ''  null comment '创建者',
    create_time  datetime                 null comment '创建时间',
    update_by    varchar(64)  default ''  null comment '更新者',
    update_time  datetime                 null comment '更新时间',
    remark       varchar(500)             null comment '备注',
    search_value varchar(30)              null
)
    comment '字典数据表' charset = utf8;

create table `ry-vue`.sys_dict_type
(
    dict_id     bigint auto_increment comment '字典主键'
        primary key,
    dict_name   varchar(100) default ''  null comment '字典名称',
    dict_type   varchar(100) default ''  null comment '字典类型',
    matrix_type varchar(32)              null,
    status      char         default '0' null comment '状态（0正常 1停用）',
    create_by   varchar(64)  default ''  null comment '创建者',
    create_time datetime                 null comment '创建时间',
    update_by   varchar(64)  default ''  null comment '更新者',
    update_time datetime                 null comment '更新时间',
    remark      varchar(500)             null comment '备注',
    constraint dict_type
        unique (dict_type)
)
    comment '字典类型表' charset = utf8;

