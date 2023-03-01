drop table if exists sys_dict_data;
create table testkit.sys_dict_data
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



drop table if exists sys_user;
create table sys_user
(
    user_id       bigint auto_increment comment '用户ID'
        primary key,
    dept_id       bigint                    null comment '部门ID',
    user_name     varchar(30)               not null comment '用户账号',
    nick_name     varchar(30)               not null comment '用户昵称',
    user_type     varchar(2)   default '00' null comment '用户类型（00系统用户）',
    email         varchar(50)  default ''   null comment '用户邮箱',
    phonenumber   varchar(11)  default ''   null comment '手机号码',
    sex           char         default '0'  null comment '用户性别（0男 1女 2未知）',
    avatar        varchar(100) default ''   null comment '头像地址',
    password      varchar(100) default ''   null comment '密码',
    status        char         default '0'  null comment '帐号状态（0正常 1停用）',
    del_flag      char         default '0'  null comment '删除标志（0代表存在 2代表删除）',
    login_ip      varchar(128) default ''   null comment '最后登录IP',
    login_date    datetime                  null comment '最后登录时间',
    create_by     varchar(64)  default ''   null comment '创建者',
    create_time   datetime                  null comment '创建时间',
    update_by     varchar(64)  default ''   null comment '更新者',
    update_time   datetime                  null comment '更新时间',
    remark        varchar(500)              null comment '备注',
    test_group_id int                       null
)
    comment '用户信息表' charset = utf8;

drop table if exists t_dtc_report;
create table testkit.t_dtc_report
(
    uid              bigint(32) auto_increment comment 'auto_increment'
        primary key,
    ecu_id           varchar(255)  null comment 'Adresse',
    level            varchar(255)  null comment 'Systembezeichnung',
    select_condition varchar(255)  null comment 'HWTeilenummer',
    component_type   varchar(255)  null comment 'SearchedOdxFileVersion',
    component_name   varchar(255)  null comment 'SWVersion',
    select_value     varchar(1024) null comment 'HWVersion',
    Adresse          varchar(255)  null comment 'ZdcName',
    variant          varchar(255)  null comment 'ZdcVersion'
)
    comment '`BIGINT(32)`' charset = utf8;
INSERT INTO t_dtc_report (uid, ecu_id, level, select_condition, component_type, component_name, select_value, Adresse, variant) VALUES (1, 'Vehicle', '//', null, 'VIN', null, '{"Fahrgestellnummer": ""}', null, null);
INSERT INTO t_dtc_report (uid, ecu_id, level, select_condition, component_type, component_name, select_value, Adresse, variant) VALUES (2, '005F', '//Diagnosebloecke/Diagnoseblock', '{"Adresse": "005F"}', 'MU', null, '{
            "variant": {
                    "SearchedOdxFileVersion": "EV_HCP3([\\\\S]*?)Node[\\\\s\\\\S]*?",
                    "Systembezeichnung": "[ ]*MU-T{0,1}([BHP])[\\\\\\\\s\\\\\\\\S]*?"
            },
            "SWVersion": null,
            "HWVersion": null,
            "HWTeilenummer": null
        }', '005F', null);
INSERT INTO t_dtc_report (uid, ecu_id, level, select_condition, component_type, component_name, select_value, Adresse, variant) VALUES (3, '0017', '//Diagnosebloecke/Diagnoseblock', '{"Adresse": "0017"}', 'KOMBI', null, '{
            "SWVersion": null,
            "HWVersion": null,
            "HWTeilenummer": null
        }', '0017', null);
INSERT INTO t_dtc_report (uid, ecu_id, level, select_condition, component_type, component_name, select_value, Adresse, variant) VALUES (4, '0019', '//Diagnosebloecke/Diagnoseblock', '{"Adresse": "0019"}', 'Gateway', '{
            "SearchedOdxFileVersion": "EV_(HCP5|[^_]+?(?=AU|MEB|UNECE)|[^_]+)[\\\\S ]*?"
        }', '{
            "SWVersion": null,
            "HWVersion": null,
            "HWTeilenummer": null
        }', '0019', null);
INSERT INTO t_dtc_report (uid, ecu_id, level, select_condition, component_type, component_name, select_value, Adresse, variant) VALUES (5, '0075', '//Diagnosebloecke/Diagnoseblock', '{"Adresse": "0075"}', 'Conbox/OCU', '{
            "SearchedOdxFileVersion": "EV_(HCP5|[^_]+?(?=AU|MEB|UNECE)|[^_]+)[\\\\S ]*?"
        }', '{
            "SWVersion": null,
            "HWVersion": null,
            "HWTeilenummer": null
        }', '0075', null);
INSERT INTO t_dtc_report (uid, ecu_id, level, select_condition, component_type, component_name, select_value, Adresse, variant) VALUES (6, '0009', '//Diagnosebloecke/Diagnoseblock', '{"Adresse": "0009"}', 'BCM1', null, '{
            "SWVersion": null,
            "HWVersion": null,
            "HWTeilenummer": null
        }', '0009', null);
INSERT INTO t_dtc_report (uid, ecu_id, level, select_condition, component_type, component_name, select_value, Adresse, variant) VALUES (7, '0046', '//Diagnosebloecke/Diagnoseblock', '{"Adresse": "0046"}', 'BCM2', null, '{
            "SWVersion": null,
            "HWVersion": null,
            "HWTeilenummer": null
        }', '0046', null);
INSERT INTO t_dtc_report (uid, ecu_id, level, select_condition, component_type, component_name, select_value, Adresse, variant) VALUES (8, '005F-ZDC', '//Diagnosebloecke/Diagnoseblock', '{"Adresse": "005F"}', 'MU-ZDC', null, '{
            "ZdcName": null,
            "ZdcVersion": null
        }', '', null);
INSERT INTO t_dtc_report (uid, ecu_id, level, select_condition, component_type, component_name, select_value, Adresse, variant) VALUES (9, '005F-AED', '//Diagnosebloecke/Diagnoseblock/SubTeilnehmer/Sub', '{"Systembezeichnung":"AED"}', 'Asterix', null, '{
            "SWVersion": null,
            "HWVersion": null,
            "HWTeilenummer": null
        }', null, null);
INSERT INTO t_dtc_report (uid, ecu_id, level, select_condition, component_type, component_name, select_value, Adresse, variant) VALUES (10, '005F-Data Medium', '//Diagnosebloecke/Diagnoseblock/SubTeilnehmer/Sub', '{"SubtName": "Data Medium"}', 'DBVersion', null, '{"SWVersion": null}', null, null);
INSERT INTO sys_user (user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, del_flag, login_ip, login_date, create_by, create_time, update_by, update_time, remark, test_group_id) VALUES (1, 103, 'admin', 'testkit', '00', 'ry@163.com', '15888888888', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2022-12-21 11:58:41', 'admin', '2022-11-08 16:04:32', '', '2022-12-21 11:58:41', '管理员', null);
INSERT INTO sys_user (user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, del_flag, login_ip, login_date, create_by, create_time, update_by, update_time, remark, test_group_id) VALUES (2, 105, 'tt', 'testkit', '00', 'ry@qq.com', '15666666666', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '192.168.2.61', '2022-12-14 14:46:04', 'admin', '2022-11-08 16:04:32', '', '2022-12-14 14:46:04', '测试员', null);
drop table if exists sys_user_role;
create table testkit.sys_user_role
(
    user_id bigint not null comment '用户ID',
    role_id bigint not null comment '角色ID',
    primary key (user_id, role_id)
)
    comment '用户和角色关联表' charset = utf8;
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO sys_user_role (user_id, role_id) VALUES (2, 2);
INSERT INTO sys_user_role (user_id, role_id) VALUES (3, 3);


drop table if exists testkit.sys_role;
create table testkit.sys_role
(
    role_id             bigint auto_increment comment '角色ID'
        primary key,
    role_name           varchar(30)             not null comment '角色名称',
    role_key            varchar(100)            not null comment '角色权限字符串',
    role_sort           int(4)                  not null comment '显示顺序',
    data_scope          char        default '1' null comment '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
    menu_check_strictly tinyint(1)  default 1   null comment '菜单树选择项是否关联显示',
    dept_check_strictly tinyint(1)  default 1   null comment '部门树选择项是否关联显示',
    status              char                    not null comment '角色状态（0正常 1停用）',
    del_flag            char        default '0' null comment '删除标志（0代表存在 2代表删除）',
    create_by           varchar(64) default ''  null comment '创建者',
    create_time         datetime                null comment '创建时间',
    update_by           varchar(64) default ''  null comment '更新者',
    update_time         datetime                null comment '更新时间',
    remark              varchar(500)            null comment '备注'
)
    comment '角色信息表' charset = utf8;

 INSERT INTO testkit.sys_role (role_id, role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (1, '超级管理员', 'admin', 2, '1', 1, 1, '0', '0', 'admin', '2022-11-08 16:04:32', '', null, '超级管理员');
 INSERT INTO testkit.sys_role (role_id, role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (2, '普通管理员', 'admin', 2, '1', 1, 1, '0', '0', 'admin', '2022-11-08 16:04:32', '', null, '普通管理员');
 INSERT INTO testkit.sys_role (role_id, role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, update_by, update_time, remark) VALUES (3, '普通用户', 'admin', 2, '1', 1, 1, '0', '0', 'admin', '2022-11-08 16:04:32', '', null, '普通用户');



drop table if exists `testkit`.sys_dict_type;
create table `testkit`.sys_dict_type
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


INSERT INTO testkit.sys_dict_type (dict_id, dict_name, dict_type, matrix_type, status, create_by, create_time, update_by, update_time, remark) VALUES (1, '用户性别', 'sys_user_sex', '0', '0', 'admin', '2022-11-08 16:04:34', '', null, '用户性别列表');
INSERT INTO testkit.sys_dict_type (dict_id, dict_name, dict_type, matrix_type, status, create_by, create_time, update_by, update_time, remark) VALUES (2, '菜单状态', 'sys_show_hide', '0', '0', 'admin', '2022-11-08 16:04:34', '', null, '菜单状态列表');
INSERT INTO testkit.sys_dict_type (dict_id, dict_name, dict_type, matrix_type, status, create_by, create_time, update_by, update_time, remark) VALUES (3, '系统开关', 'sys_normal_disable', '0', '0', 'admin', '2022-11-08 16:04:34', '', null, '系统开关列表');
INSERT INTO testkit.sys_dict_type (dict_id, dict_name, dict_type, matrix_type, status, create_by, create_time, update_by, update_time, remark) VALUES (4, '任务状态', 'sys_job_status', '0', '0', 'admin', '2022-11-08 16:04:34', '', null, '任务状态列表');
INSERT INTO testkit.sys_dict_type (dict_id, dict_name, dict_type, matrix_type, status, create_by, create_time, update_by, update_time, remark) VALUES (5, '任务分组', 'sys_job_group', '0', '0', 'admin', '2022-11-08 16:04:34', '', null, '任务分组列表');
INSERT INTO testkit.sys_dict_type (dict_id, dict_name, dict_type, matrix_type, status, create_by, create_time, update_by, update_time, remark) VALUES (6, '系统是否', 'sys_yes_no', '0', '0', 'admin', '2022-11-08 16:04:34', '', null, '系统是否列表');
INSERT INTO testkit.sys_dict_type (dict_id, dict_name, dict_type, matrix_type, status, create_by, create_time, update_by, update_time, remark) VALUES (7, '通知类型', 'sys_notice_type', '0', '0', 'admin', '2022-11-08 16:04:34', '', null, '通知类型列表');
INSERT INTO testkit.sys_dict_type (dict_id, dict_name, dict_type, matrix_type, status, create_by, create_time, update_by, update_time, remark) VALUES (8, '通知状态', 'sys_notice_status', '0', '0', 'admin', '2022-11-08 16:04:34', '', null, '通知状态列表');
INSERT INTO testkit.sys_dict_type (dict_id, dict_name, dict_type, matrix_type, status, create_by, create_time, update_by, update_time, remark) VALUES (9, '操作类型', 'sys_oper_type', '0', '0', 'admin', '2022-11-08 16:04:34', '', null, '操作类型列表');
INSERT INTO testkit.sys_dict_type (dict_id, dict_name, dict_type, matrix_type, status, create_by, create_time, update_by, update_time, remark) VALUES (10, '系统状态', 'sys_common_status', '0', '0', 'admin', '2022-11-08 16:04:34', '', null, '登录状态列表');
INSERT INTO testkit.sys_dict_type (dict_id, dict_name, dict_type, matrix_type, status, create_by, create_time, update_by, update_time, remark) VALUES (11, 'platform', 'platformType', '1', '0', 'admin', '2022-11-09 14:04:51', '', null, '版本');
INSERT INTO testkit.sys_dict_type (dict_id, dict_name, dict_type, matrix_type, status, create_by, create_time, update_by, update_time, remark) VALUES (12, 'cluster', 'clusterName', '1', '0', 'admin', '2022-11-09 14:06:37', '', null, '工程');
INSERT INTO testkit.sys_dict_type (dict_id, dict_name, dict_type, matrix_type, status, create_by, create_time, update_by, update_time, remark) VALUES (13, 'project', 'projectType', '1', '0', 'admin', '2022-11-09 14:06:37', '', null, '平台');
INSERT INTO testkit.sys_dict_type (dict_id, dict_name, dict_type, matrix_type, status, create_by, create_time, update_by, update_time, remark) VALUES (14, 'variant', 'variantType', '1', '0', 'admin', '2022-11-09 14:06:37', '', null, '市场');
INSERT INTO testkit.sys_dict_type (dict_id, dict_name, dict_type, matrix_type, status, create_by, create_time, update_by, update_time, remark) VALUES (15, 'carline', 'carlineModelType', '1', '0', 'admin', '2022-11-09 14:06:37', '', null, null);
INSERT INTO testkit.sys_dict_type (dict_id, dict_name, dict_type, matrix_type, status, create_by, create_time, update_by, update_time, remark) VALUES (16, 'ocuCbox', 'ocuCboxType', '1', '0', 'admin', '2022-11-09 14:06:37', '', null, null);
INSERT INTO testkit.sys_dict_type (dict_id, dict_name, dict_type, matrix_type, status, create_by, create_time, update_by, update_time, remark) VALUES (17, 'gateway', 'gatewayType', '1', '0', 'admin', '2022-11-09 14:06:37', '', null, null);
INSERT INTO testkit.sys_dict_type (dict_id, dict_name, dict_type, matrix_type, status, create_by, create_time, update_by, update_time, remark) VALUES (18, 'market', 'marketType', '1', '0', 'admin', '2022-11-09 14:06:37', '', null, null);
INSERT INTO testkit.sys_dict_type (dict_id, dict_name, dict_type, matrix_type, status, create_by, create_time, update_by, update_time, remark) VALUES (19, 'goldenCar', 'goldenCarType', '1', '0', 'admin', '2022-11-09 14:06:37', '', null, null);
INSERT INTO testkit.sys_dict_type (dict_id, dict_name, dict_type, matrix_type, status, create_by, create_time, update_by, update_time, remark) VALUES (20, 'goldenCluster', 'goldenClusterNameType', '1', '0', 'admin', '2022-11-09 14:06:37', '', null, null);
INSERT INTO testkit.sys_dict_type (dict_id, dict_name, dict_type, matrix_type, status, create_by, create_time, update_by, update_time, remark) VALUES (21, 'functionGroup', 'functionGroupType', '1', '0', 'admin', '2022-11-09 14:06:37', '', null, null);
INSERT INTO testkit.sys_dict_type (dict_id, dict_name, dict_type, matrix_type, status, create_by, create_time, update_by, update_time, remark) VALUES (22, 'task', 'taskType', '1', '0', 'admin', '2022-11-09 14:06:37', '', null, null);

alter table testkit.t_component_data
    modify component_version varchar(70) null comment '配件版本号';

alter table testkit.t_component_data
    modify component_name varchar(100) null comment '配件名称';

alter table t_carline_component
    add variant_type varchar(50) null comment '配件等级';
alter table testkit.t_cluster
    change car_num device_circle_num tinyint(1) default 1 not null comment '1-10的循环';
alter table testkit.t_cluster     change car_num device_circle_num tinyint(1) default 1 not null comment '1-10';
alter table testkit.t_cluster change car_num device_circle_num tinyint(1) default 1 not null comment '1-10';
alter table testkit.t_cluster
    add week_info varchar(16) null after status;

alter table testkit.t_cluster
    add auto_save_version_name varchar(63) null after week_info;
alter table testkit.t_carline_info
    add carline_model_type varchar(32) null;
alter table testkit.t_desktop_record
    modify comment varchar(255) null comment 'comment';

alter table testkit.t_desktop_record
    add update_time timestamp default now() null after oper_time;

INSERT INTO testkit.sys_dict_data (matrix_type, dict_label, dict_value, dict_type, status, dict_sort, params, css_class, list_class, is_default, create_by, create_time, update_by, update_time, remark, search_value) VALUES ('0', 'BENCH', '1', 'deviceType', DEFAULT, DEFAULT, null, null, null, DEFAULT, DEFAULT, null, DEFAULT, null, null, null);
INSERT INTO testkit.sys_dict_data (matrix_type, dict_label, dict_value, dict_type, status, dict_sort, params, css_class, list_class, is_default, create_by, create_time, update_by, update_time, remark, search_value) VALUES ('0', 'CAR', '2', 'deviceType', DEFAULT, DEFAULT, null, null, null, DEFAULT, DEFAULT, null, DEFAULT, null, null, null);


INSERT INTO testkit.sys_job (job_id, job_name, job_group, invoke_target, cron_expression, misfire_policy, concurrent, status, create_by, create_time, update_by, update_time, remark) VALUES (201, '定时从idexReport刷新device数据', 'DEFAULT', 'deviceTask.quarzImportAllDTCReport', '0 0 20 * * ?', '1', '1', '0', 'admin', '2023-01-03 15:35:02', 'admin', '2023-02-10 23:17:29', '');
INSERT INTO testkit.sys_job (job_id, job_name, job_group, invoke_target, cron_expression, misfire_policy, concurrent, status, create_by, create_time, update_by, update_time, remark) VALUES (200, 'test', 'DEFAULT', 'deviceTask.test', '0/10 * * * * ?', '1', '1', '0', 'admin', '2023-01-03 15:35:02', 'admin', '2023-02-10 23:17:29', '');
UPDATE testkit.sys_job t SET t.status = '1' WHERE t.job_id = 200 ;


alter table testkit.t_desktop_record
    modify mileacge double null comment 'mileacge';
alter table testkit.t_desktop_record
    modify test_hour double null comment 'testHour';
alter table testkit.t_desktop_record
    modify planned_ticket int(4) null comment 'plannedTicket';