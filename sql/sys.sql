create table `ry-vue`.gen_table
(
    table_id          bigint auto_increment comment '编号'
        primary key,
    table_name        varchar(200) default ''     null comment '表名称',
    table_comment     varchar(500) default ''     null comment '表描述',
    sub_table_name    varchar(64)                 null comment '关联子表的表名',
    sub_table_fk_name varchar(64)                 null comment '子表关联的外键名',
    class_name        varchar(100) default ''     null comment '实体类名称',
    tpl_category      varchar(200) default 'crud' null comment '使用的模板（crud单表操作 tree树表操作）',
    package_name      varchar(100)                null comment '生成包路径',
    module_name       varchar(30)                 null comment '生成模块名',
    business_name     varchar(30)                 null comment '生成业务名',
    function_name     varchar(50)                 null comment '生成功能名',
    function_author   varchar(50)                 null comment '生成功能作者',
    gen_type          char         default '0'    null comment '生成代码方式（0zip压缩包 1自定义路径）',
    gen_path          varchar(200) default '/'    null comment '生成路径（不填默认项目路径）',
    options           varchar(1000)               null comment '其它生成选项',
    create_by         varchar(64)  default ''     null comment '创建者',
    create_time       datetime                    null comment '创建时间',
    update_by         varchar(64)  default ''     null comment '更新者',
    update_time       datetime                    null comment '更新时间',
    remark            varchar(500)                null comment '备注'
)
    comment '代码生成业务表' charset = utf8;

create table `ry-vue`.gen_table_column
(
    column_id      bigint auto_increment comment '编号'
        primary key,
    table_id       varchar(64)               null comment '归属表编号',
    column_name    varchar(200)              null comment '列名称',
    column_comment varchar(500)              null comment '列描述',
    column_type    varchar(100)              null comment '列类型',
    java_type      varchar(500)              null comment 'JAVA类型',
    java_field     varchar(200)              null comment 'JAVA字段名',
    is_pk          char                      null comment '是否主键（1是）',
    is_increment   char                      null comment '是否自增（1是）',
    is_required    char                      null comment '是否必填（1是）',
    is_insert      char                      null comment '是否为插入字段（1是）',
    is_edit        char                      null comment '是否编辑字段（1是）',
    is_list        char                      null comment '是否列表字段（1是）',
    is_query       char                      null comment '是否查询字段（1是）',
    query_type     varchar(200) default 'EQ' null comment '查询方式（等于、不等于、大于、小于、范围）',
    html_type      varchar(200)              null comment '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
    dict_type      varchar(200) default ''   null comment '字典类型',
    sort           int                       null comment '排序',
    create_by      varchar(64)  default ''   null comment '创建者',
    create_time    datetime                  null comment '创建时间',
    update_by      varchar(64)  default ''   null comment '更新者',
    update_time    datetime                  null comment '更新时间'
)
    comment '代码生成业务表字段' charset = utf8;

create table `ry-vue`.qrtz_calendars
(
    sched_name    varchar(120) not null comment '调度名称',
    calendar_name varchar(200) not null comment '日历名称',
    calendar      blob         not null comment '存放持久化calendar对象',
    primary key (sched_name, calendar_name)
)
    comment '日历信息表';

create table `ry-vue`.qrtz_fired_triggers
(
    sched_name        varchar(120) not null comment '调度名称',
    entry_id          varchar(95)  not null comment '调度器实例id',
    trigger_name      varchar(200) not null comment 'qrtz_triggers表trigger_name的外键',
    trigger_group     varchar(200) not null comment 'qrtz_triggers表trigger_group的外键',
    instance_name     varchar(200) not null comment '调度器实例名',
    fired_time        bigint(13)   not null comment '触发的时间',
    sched_time        bigint(13)   not null comment '定时器制定的时间',
    priority          int          not null comment '优先级',
    state             varchar(16)  not null comment '状态',
    job_name          varchar(200) null comment '任务名称',
    job_group         varchar(200) null comment '任务组名',
    is_nonconcurrent  varchar(1)   null comment '是否并发',
    requests_recovery varchar(1)   null comment '是否接受恢复执行',
    primary key (sched_name, entry_id)
)
    comment '已触发的触发器表';

create table `ry-vue`.qrtz_job_details
(
    sched_name        varchar(120) not null comment '调度名称',
    job_name          varchar(200) not null comment '任务名称',
    job_group         varchar(200) not null comment '任务组名',
    description       varchar(250) null comment '相关介绍',
    job_class_name    varchar(250) not null comment '执行任务类名称',
    is_durable        varchar(1)   not null comment '是否持久化',
    is_nonconcurrent  varchar(1)   not null comment '是否并发',
    is_update_data    varchar(1)   not null comment '是否更新数据',
    requests_recovery varchar(1)   not null comment '是否接受恢复执行',
    job_data          blob         null comment '存放持久化job对象',
    primary key (sched_name, job_name, job_group)
)
    comment '任务详细信息表';

create table `ry-vue`.qrtz_locks
(
    sched_name varchar(120) not null comment '调度名称',
    lock_name  varchar(40)  not null comment '悲观锁名称',
    primary key (sched_name, lock_name)
)
    comment '存储的悲观锁信息表';

create table `ry-vue`.qrtz_paused_trigger_grps
(
    sched_name    varchar(120) not null comment '调度名称',
    trigger_group varchar(200) not null comment 'qrtz_triggers表trigger_group的外键',
    primary key (sched_name, trigger_group)
)
    comment '暂停的触发器表';

create table `ry-vue`.qrtz_scheduler_state
(
    sched_name        varchar(120) not null comment '调度名称',
    instance_name     varchar(200) not null comment '实例名称',
    last_checkin_time bigint(13)   not null comment '上次检查时间',
    checkin_interval  bigint(13)   not null comment '检查间隔时间',
    primary key (sched_name, instance_name)
)
    comment '调度器状态表';

create table `ry-vue`.qrtz_triggers
(
    sched_name     varchar(120) not null comment '调度名称',
    trigger_name   varchar(200) not null comment '触发器的名字',
    trigger_group  varchar(200) not null comment '触发器所属组的名字',
    job_name       varchar(200) not null comment 'qrtz_job_details表job_name的外键',
    job_group      varchar(200) not null comment 'qrtz_job_details表job_group的外键',
    description    varchar(250) null comment '相关介绍',
    next_fire_time bigint(13)   null comment '上一次触发时间（毫秒）',
    prev_fire_time bigint(13)   null comment '下一次触发时间（默认为-1表示不触发）',
    priority       int          null comment '优先级',
    trigger_state  varchar(16)  not null comment '触发器状态',
    trigger_type   varchar(8)   not null comment '触发器的类型',
    start_time     bigint(13)   not null comment '开始时间',
    end_time       bigint(13)   null comment '结束时间',
    calendar_name  varchar(200) null comment '日程表名称',
    misfire_instr  smallint(2)  null comment '补偿执行的策略',
    job_data       blob         null comment '存放持久化job对象',
    primary key (sched_name, trigger_name, trigger_group),
    constraint qrtz_triggers_ibfk_1
        foreign key (sched_name, job_name, job_group) references `ry-vue`.qrtz_job_details (sched_name, job_name, job_group)
)
    comment '触发器详细信息表';

create table `ry-vue`.qrtz_blob_triggers
(
    sched_name    varchar(120) not null comment '调度名称',
    trigger_name  varchar(200) not null comment 'qrtz_triggers表trigger_name的外键',
    trigger_group varchar(200) not null comment 'qrtz_triggers表trigger_group的外键',
    blob_data     blob         null comment '存放持久化Trigger对象',
    primary key (sched_name, trigger_name, trigger_group),
    constraint qrtz_blob_triggers_ibfk_1
        foreign key (sched_name, trigger_name, trigger_group) references `ry-vue`.qrtz_triggers (sched_name, trigger_name, trigger_group)
)
    comment 'Blob类型的触发器表';

create table `ry-vue`.qrtz_cron_triggers
(
    sched_name      varchar(120) not null comment '调度名称',
    trigger_name    varchar(200) not null comment 'qrtz_triggers表trigger_name的外键',
    trigger_group   varchar(200) not null comment 'qrtz_triggers表trigger_group的外键',
    cron_expression varchar(200) not null comment 'cron表达式',
    time_zone_id    varchar(80)  null comment '时区',
    primary key (sched_name, trigger_name, trigger_group),
    constraint qrtz_cron_triggers_ibfk_1
        foreign key (sched_name, trigger_name, trigger_group) references `ry-vue`.qrtz_triggers (sched_name, trigger_name, trigger_group)
)
    comment 'Cron类型的触发器表';

create table `ry-vue`.qrtz_simple_triggers
(
    sched_name      varchar(120) not null comment '调度名称',
    trigger_name    varchar(200) not null comment 'qrtz_triggers表trigger_name的外键',
    trigger_group   varchar(200) not null comment 'qrtz_triggers表trigger_group的外键',
    repeat_count    bigint(7)    not null comment '重复的次数统计',
    repeat_interval bigint(12)   not null comment '重复的间隔时间',
    times_triggered bigint(10)   not null comment '已经触发的次数',
    primary key (sched_name, trigger_name, trigger_group),
    constraint qrtz_simple_triggers_ibfk_1
        foreign key (sched_name, trigger_name, trigger_group) references `ry-vue`.qrtz_triggers (sched_name, trigger_name, trigger_group)
)
    comment '简单触发器的信息表';

create table `ry-vue`.qrtz_simprop_triggers
(
    sched_name    varchar(120)   not null comment '调度名称',
    trigger_name  varchar(200)   not null comment 'qrtz_triggers表trigger_name的外键',
    trigger_group varchar(200)   not null comment 'qrtz_triggers表trigger_group的外键',
    str_prop_1    varchar(512)   null comment 'String类型的trigger的第一个参数',
    str_prop_2    varchar(512)   null comment 'String类型的trigger的第二个参数',
    str_prop_3    varchar(512)   null comment 'String类型的trigger的第三个参数',
    int_prop_1    int            null comment 'int类型的trigger的第一个参数',
    int_prop_2    int            null comment 'int类型的trigger的第二个参数',
    long_prop_1   bigint         null comment 'long类型的trigger的第一个参数',
    long_prop_2   bigint         null comment 'long类型的trigger的第二个参数',
    dec_prop_1    decimal(13, 4) null comment 'decimal类型的trigger的第一个参数',
    dec_prop_2    decimal(13, 4) null comment 'decimal类型的trigger的第二个参数',
    bool_prop_1   varchar(1)     null comment 'Boolean类型的trigger的第一个参数',
    bool_prop_2   varchar(1)     null comment 'Boolean类型的trigger的第二个参数',
    primary key (sched_name, trigger_name, trigger_group),
    constraint qrtz_simprop_triggers_ibfk_1
        foreign key (sched_name, trigger_name, trigger_group) references `ry-vue`.qrtz_triggers (sched_name, trigger_name, trigger_group)
)
    comment '同步机制的行锁表';

create index sched_name
    on `ry-vue`.qrtz_triggers (sched_name, job_name, job_group);

create table `ry-vue`.r_cluster
(
    ID_CLUSTER             bigint       not null
        primary key,
    NAME                   varchar(255) null,
    BASE_PORT              varchar(255) null,
    SOCKETS_BUFFER_SIZE    varchar(255) null,
    SOCKETS_FLUSH_INTERVAL varchar(255) null,
    SOCKETS_COMPRESSED     tinyint(1)   null,
    DYNAMIC_CLUSTER        tinyint(1)   null
);

create table `ry-vue`.r_cluster_slave
(
    ID_CLUSTER_SLAVE bigint not null
        primary key,
    ID_CLUSTER       int    null,
    ID_SLAVE         int    null
);

create table `ry-vue`.r_condition
(
    ID_CONDITION        bigint       not null
        primary key,
    ID_CONDITION_PARENT int          null,
    NEGATED             tinyint(1)   null,
    OPERATOR            varchar(255) null,
    LEFT_NAME           varchar(255) null,
    CONDITION_FUNCTION  varchar(255) null,
    RIGHT_NAME          varchar(255) null,
    ID_VALUE_RIGHT      int          null
);

create table `ry-vue`.r_database
(
    ID_DATABASE         bigint       not null
        primary key,
    NAME                varchar(255) null,
    ID_DATABASE_TYPE    int          null,
    ID_DATABASE_CONTYPE int          null,
    HOST_NAME           varchar(255) null,
    DATABASE_NAME       mediumtext   null,
    PORT                int          null,
    USERNAME            varchar(255) null,
    PASSWORD            varchar(255) null,
    SERVERNAME          varchar(255) null,
    DATA_TBS            varchar(255) null,
    INDEX_TBS           varchar(255) null
);

create table `ry-vue`.r_database_attribute
(
    ID_DATABASE_ATTRIBUTE bigint       not null
        primary key,
    ID_DATABASE           int          null,
    CODE                  varchar(255) null,
    VALUE_STR             mediumtext   null,
    constraint IDX_RDAT
        unique (ID_DATABASE, CODE)
);

create table `ry-vue`.r_database_contype
(
    ID_DATABASE_CONTYPE bigint       not null
        primary key,
    CODE                varchar(255) null,
    DESCRIPTION         varchar(255) null
);

create table `ry-vue`.r_database_type
(
    ID_DATABASE_TYPE bigint       not null
        primary key,
    CODE             varchar(255) null,
    DESCRIPTION      varchar(255) null
);

create table `ry-vue`.r_dependency
(
    ID_DEPENDENCY     bigint       not null
        primary key,
    ID_TRANSFORMATION int          null,
    ID_DATABASE       int          null,
    TABLE_NAME        varchar(255) null,
    FIELD_NAME        varchar(255) null
);

create table `ry-vue`.r_directory
(
    ID_DIRECTORY        bigint       not null
        primary key,
    ID_DIRECTORY_PARENT int          null,
    DIRECTORY_NAME      varchar(255) null,
    constraint IDX_RDIR
        unique (ID_DIRECTORY_PARENT, DIRECTORY_NAME)
);

create table `ry-vue`.r_element
(
    ID_ELEMENT      bigint     not null
        primary key,
    ID_ELEMENT_TYPE int        null,
    NAME            mediumtext null
);

create table `ry-vue`.r_element_attribute
(
    ID_ELEMENT_ATTRIBUTE        bigint       not null
        primary key,
    ID_ELEMENT                  int          null,
    ID_ELEMENT_ATTRIBUTE_PARENT int          null,
    ATTR_KEY                    varchar(255) null,
    ATTR_VALUE                  mediumtext   null
);

create table `ry-vue`.r_element_type
(
    ID_ELEMENT_TYPE bigint     not null
        primary key,
    ID_NAMESPACE    int        null,
    NAME            mediumtext null,
    DESCRIPTION     mediumtext null
);

create table `ry-vue`.r_job
(
    ID_JOB               bigint       not null
        primary key,
    ID_DIRECTORY         int          null,
    NAME                 varchar(255) null,
    DESCRIPTION          mediumtext   null,
    EXTENDED_DESCRIPTION mediumtext   null,
    JOB_VERSION          varchar(255) null,
    JOB_STATUS           int          null,
    ID_DATABASE_LOG      int          null,
    TABLE_NAME_LOG       varchar(255) null,
    CREATED_USER         varchar(255) null,
    CREATED_DATE         datetime     null,
    MODIFIED_USER        varchar(255) null,
    MODIFIED_DATE        datetime     null,
    USE_BATCH_ID         tinyint(1)   null,
    PASS_BATCH_ID        tinyint(1)   null,
    USE_LOGFIELD         tinyint(1)   null,
    SHARED_FILE          varchar(255) null
);

create table `ry-vue`.r_job_attribute
(
    ID_JOB_ATTRIBUTE bigint       not null
        primary key,
    ID_JOB           int          null,
    NR               int          null,
    CODE             varchar(255) null,
    VALUE_NUM        bigint       null,
    VALUE_STR        mediumtext   null,
    constraint IDX_JATT
        unique (ID_JOB, CODE, NR)
);

create table `ry-vue`.r_job_hop
(
    ID_JOB_HOP            bigint     not null
        primary key,
    ID_JOB                int        null,
    ID_JOBENTRY_COPY_FROM int        null,
    ID_JOBENTRY_COPY_TO   int        null,
    ENABLED               tinyint(1) null,
    EVALUATION            tinyint(1) null,
    UNCONDITIONAL         tinyint(1) null
);

create table `ry-vue`.r_job_lock
(
    ID_JOB_LOCK  bigint     not null
        primary key,
    ID_JOB       int        null,
    ID_USER      int        null,
    LOCK_MESSAGE mediumtext null,
    LOCK_DATE    datetime   null
);

create table `ry-vue`.r_job_note
(
    ID_JOB  int null,
    ID_NOTE int null
);

create table `ry-vue`.r_jobentry
(
    ID_JOBENTRY      bigint       not null
        primary key,
    ID_JOB           int          null,
    ID_JOBENTRY_TYPE int          null,
    NAME             varchar(255) null,
    DESCRIPTION      mediumtext   null
);

create table `ry-vue`.r_jobentry_attribute
(
    ID_JOBENTRY_ATTRIBUTE bigint       not null
        primary key,
    ID_JOB                int          null,
    ID_JOBENTRY           int          null,
    NR                    int          null,
    CODE                  varchar(255) null,
    VALUE_NUM             double       null,
    VALUE_STR             mediumtext   null,
    constraint IDX_RJEA
        unique (ID_JOBENTRY_ATTRIBUTE, CODE, NR)
);

create table `ry-vue`.r_jobentry_copy
(
    ID_JOBENTRY_COPY bigint     not null
        primary key,
    ID_JOBENTRY      int        null,
    ID_JOB           int        null,
    ID_JOBENTRY_TYPE int        null,
    NR               int        null,
    GUI_LOCATION_X   int        null,
    GUI_LOCATION_Y   int        null,
    GUI_DRAW         tinyint(1) null,
    PARALLEL         tinyint(1) null
);

create table `ry-vue`.r_jobentry_database
(
    ID_JOB      int null,
    ID_JOBENTRY int null,
    ID_DATABASE int null
);

create index IDX_RJD1
    on `ry-vue`.r_jobentry_database (ID_JOB);

create index IDX_RJD2
    on `ry-vue`.r_jobentry_database (ID_DATABASE);

create table `ry-vue`.r_jobentry_type
(
    ID_JOBENTRY_TYPE bigint       not null
        primary key,
    CODE             varchar(255) null,
    DESCRIPTION      varchar(255) null
);

create table `ry-vue`.r_log
(
    ID_LOG          bigint       not null
        primary key,
    NAME            varchar(255) null,
    ID_LOGLEVEL     int          null,
    LOGTYPE         varchar(255) null,
    FILENAME        varchar(255) null,
    FILEEXTENTION   varchar(255) null,
    ADD_DATE        tinyint(1)   null,
    ADD_TIME        tinyint(1)   null,
    ID_DATABASE_LOG int          null,
    TABLE_NAME_LOG  varchar(255) null
);

create table `ry-vue`.r_loglevel
(
    ID_LOGLEVEL bigint       not null
        primary key,
    CODE        varchar(255) null,
    DESCRIPTION varchar(255) null
);

create table `ry-vue`.r_namespace
(
    ID_NAMESPACE bigint     not null
        primary key,
    NAME         mediumtext null
);

create table `ry-vue`.r_note
(
    ID_NOTE                      bigint     not null
        primary key,
    VALUE_STR                    mediumtext null,
    GUI_LOCATION_X               int        null,
    GUI_LOCATION_Y               int        null,
    GUI_LOCATION_WIDTH           int        null,
    GUI_LOCATION_HEIGHT          int        null,
    FONT_NAME                    mediumtext null,
    FONT_SIZE                    int        null,
    FONT_BOLD                    tinyint(1) null,
    FONT_ITALIC                  tinyint(1) null,
    FONT_COLOR_RED               int        null,
    FONT_COLOR_GREEN             int        null,
    FONT_COLOR_BLUE              int        null,
    FONT_BACK_GROUND_COLOR_RED   int        null,
    FONT_BACK_GROUND_COLOR_GREEN int        null,
    FONT_BACK_GROUND_COLOR_BLUE  int        null,
    FONT_BORDER_COLOR_RED        int        null,
    FONT_BORDER_COLOR_GREEN      int        null,
    FONT_BORDER_COLOR_BLUE       int        null,
    DRAW_SHADOW                  tinyint(1) null
);

create table `ry-vue`.r_partition
(
    ID_PARTITION        bigint       not null
        primary key,
    ID_PARTITION_SCHEMA int          null,
    PARTITION_ID        varchar(255) null
);

create table `ry-vue`.r_partition_schema
(
    ID_PARTITION_SCHEMA  bigint       not null
        primary key,
    NAME                 varchar(255) null,
    DYNAMIC_DEFINITION   tinyint(1)   null,
    PARTITIONS_PER_SLAVE varchar(255) null
);

create table `ry-vue`.r_repository_log
(
    ID_REPOSITORY_LOG bigint       not null
        primary key,
    REP_VERSION       varchar(255) null,
    LOG_DATE          datetime     null,
    LOG_USER          varchar(255) null,
    OPERATION_DESC    mediumtext   null
);

create table `ry-vue`.r_slave
(
    ID_SLAVE        bigint       not null
        primary key,
    NAME            varchar(255) null,
    HOST_NAME       varchar(255) null,
    PORT            varchar(255) null,
    WEB_APP_NAME    varchar(255) null,
    USERNAME        varchar(255) null,
    PASSWORD        varchar(255) null,
    PROXY_HOST_NAME varchar(255) null,
    PROXY_PORT      varchar(255) null,
    NON_PROXY_HOSTS varchar(255) null,
    MASTER          tinyint(1)   null
);

create table `ry-vue`.r_step
(
    ID_STEP           bigint       not null
        primary key,
    ID_TRANSFORMATION int          null,
    NAME              varchar(255) null,
    DESCRIPTION       mediumtext   null,
    ID_STEP_TYPE      int          null,
    DISTRIBUTE        tinyint(1)   null,
    COPIES            int          null,
    GUI_LOCATION_X    int          null,
    GUI_LOCATION_Y    int          null,
    GUI_DRAW          tinyint(1)   null,
    COPIES_STRING     varchar(255) null
);

create table `ry-vue`.r_step_attribute
(
    ID_STEP_ATTRIBUTE bigint       not null
        primary key,
    ID_TRANSFORMATION int          null,
    ID_STEP           int          null,
    NR                int          null,
    CODE              varchar(255) null,
    VALUE_NUM         bigint       null,
    VALUE_STR         mediumtext   null,
    constraint IDX_RSAT
        unique (ID_STEP, CODE, NR)
);

create table `ry-vue`.r_step_database
(
    ID_TRANSFORMATION int null,
    ID_STEP           int null,
    ID_DATABASE       int null
);

create index IDX_RSD1
    on `ry-vue`.r_step_database (ID_TRANSFORMATION);

create index IDX_RSD2
    on `ry-vue`.r_step_database (ID_DATABASE);

create table `ry-vue`.r_step_type
(
    ID_STEP_TYPE bigint       not null
        primary key,
    CODE         varchar(255) null,
    DESCRIPTION  varchar(255) null,
    HELPTEXT     varchar(255) null
);

create table `ry-vue`.r_trans_attribute
(
    ID_TRANS_ATTRIBUTE bigint       not null
        primary key,
    ID_TRANSFORMATION  int          null,
    NR                 int          null,
    CODE               varchar(255) null,
    VALUE_NUM          bigint       null,
    VALUE_STR          mediumtext   null,
    constraint IDX_TATT
        unique (ID_TRANSFORMATION, CODE, NR)
);

create table `ry-vue`.r_trans_cluster
(
    ID_TRANS_CLUSTER  bigint not null
        primary key,
    ID_TRANSFORMATION int    null,
    ID_CLUSTER        int    null
);

create table `ry-vue`.r_trans_hop
(
    ID_TRANS_HOP      bigint     not null
        primary key,
    ID_TRANSFORMATION int        null,
    ID_STEP_FROM      int        null,
    ID_STEP_TO        int        null,
    ENABLED           tinyint(1) null
);

create table `ry-vue`.r_trans_lock
(
    ID_TRANS_LOCK     bigint     not null
        primary key,
    ID_TRANSFORMATION int        null,
    ID_USER           int        null,
    LOCK_MESSAGE      mediumtext null,
    LOCK_DATE         datetime   null
);

create table `ry-vue`.r_trans_note
(
    ID_TRANSFORMATION int null,
    ID_NOTE           int null
);

create table `ry-vue`.r_trans_partition_schema
(
    ID_TRANS_PARTITION_SCHEMA bigint not null
        primary key,
    ID_TRANSFORMATION         int    null,
    ID_PARTITION_SCHEMA       int    null
);

create table `ry-vue`.r_trans_slave
(
    ID_TRANS_SLAVE    bigint not null
        primary key,
    ID_TRANSFORMATION int    null,
    ID_SLAVE          int    null
);

create table `ry-vue`.r_trans_step_condition
(
    ID_TRANSFORMATION int null,
    ID_STEP           int null,
    ID_CONDITION      int null
);

create table `ry-vue`.r_transformation
(
    ID_TRANSFORMATION    bigint       not null
        primary key,
    ID_DIRECTORY         int          null,
    NAME                 varchar(255) null,
    DESCRIPTION          mediumtext   null,
    EXTENDED_DESCRIPTION mediumtext   null,
    TRANS_VERSION        varchar(255) null,
    TRANS_STATUS         int          null,
    ID_STEP_READ         int          null,
    ID_STEP_WRITE        int          null,
    ID_STEP_INPUT        int          null,
    ID_STEP_OUTPUT       int          null,
    ID_STEP_UPDATE       int          null,
    ID_DATABASE_LOG      int          null,
    TABLE_NAME_LOG       varchar(255) null,
    USE_BATCHID          tinyint(1)   null,
    USE_LOGFIELD         tinyint(1)   null,
    ID_DATABASE_MAXDATE  int          null,
    TABLE_NAME_MAXDATE   varchar(255) null,
    FIELD_NAME_MAXDATE   varchar(255) null,
    OFFSET_MAXDATE       double       null,
    DIFF_MAXDATE         double       null,
    CREATED_USER         varchar(255) null,
    CREATED_DATE         datetime     null,
    MODIFIED_USER        varchar(255) null,
    MODIFIED_DATE        datetime     null,
    SIZE_ROWSET          int          null
);

create table `ry-vue`.r_user
(
    ID_USER     bigint       not null
        primary key,
    LOGIN       varchar(255) null,
    PASSWORD    varchar(255) null,
    NAME        varchar(255) null,
    DESCRIPTION varchar(255) null,
    ENABLED     tinyint(1)   null
);

create table `ry-vue`.r_value
(
    ID_VALUE   bigint       not null
        primary key,
    NAME       varchar(255) null,
    VALUE_TYPE varchar(255) null,
    VALUE_STR  varchar(255) null,
    IS_NULL    tinyint(1)   null
);

create table `ry-vue`.r_version
(
    ID_VERSION    bigint     not null
        primary key,
    MAJOR_VERSION int        null,
    MINOR_VERSION int        null,
    UPGRADE_DATE  datetime   null,
    IS_UPGRADE    tinyint(1) null
);

create table `ry-vue`.sys_config
(
    config_id    int(5) auto_increment comment '参数主键'
        primary key,
    config_name  varchar(100) default ''  null comment '参数名称',
    config_key   varchar(100) default ''  null comment '参数键名',
    config_value varchar(500) default ''  null comment '参数键值',
    config_type  char         default 'N' null comment '系统内置（Y是 N否）',
    create_by    varchar(64)  default ''  null comment '创建者',
    create_time  datetime                 null comment '创建时间',
    update_by    varchar(64)  default ''  null comment '更新者',
    update_time  datetime                 null comment '更新时间',
    remark       varchar(500)             null comment '备注'
)
    comment '参数配置表' charset = utf8;

create table `ry-vue`.sys_dept
(
    dept_id     bigint auto_increment comment '部门id'
        primary key,
    parent_id   bigint      default 0   null comment '父部门id',
    ancestors   varchar(50) default ''  null comment '祖级列表',
    dept_name   varchar(30) default ''  null comment '部门名称',
    order_num   int(4)      default 0   null comment '显示顺序',
    leader      varchar(20)             null comment '负责人',
    phone       varchar(11)             null comment '联系电话',
    email       varchar(50)             null comment '邮箱',
    status      char        default '0' null comment '部门状态（0正常 1停用）',
    del_flag    char        default '0' null comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64) default ''  null comment '创建者',
    create_time datetime                null comment '创建时间',
    update_by   varchar(64) default ''  null comment '更新者',
    update_time datetime                null comment '更新时间'
)
    comment '部门表' charset = utf8;

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

create table `ry-vue`.sys_job
(
    job_id          bigint auto_increment comment '任务ID',
    job_name        varchar(64)  default ''        not null comment '任务名称',
    job_group       varchar(64)  default 'DEFAULT' not null comment '任务组名',
    invoke_target   varchar(500)                   not null comment '调用目标字符串',
    cron_expression varchar(255) default ''        null comment 'cron执行表达式',
    misfire_policy  varchar(20)  default '3'       null comment '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
    concurrent      char         default '1'       null comment '是否并发执行（0允许 1禁止）',
    status          char         default '0'       null comment '状态（0正常 1暂停）',
    create_by       varchar(64)  default ''        null comment '创建者',
    create_time     datetime                       null comment '创建时间',
    update_by       varchar(64)  default ''        null comment '更新者',
    update_time     datetime                       null comment '更新时间',
    remark          varchar(500) default ''        null comment '备注信息',
    primary key (job_id, job_name, job_group)
)
    comment '定时任务调度表' charset = utf8;

create table `ry-vue`.sys_job_log
(
    job_log_id     bigint auto_increment comment '任务日志ID'
        primary key,
    job_name       varchar(64)               not null comment '任务名称',
    job_group      varchar(64)               not null comment '任务组名',
    invoke_target  varchar(500)              not null comment '调用目标字符串',
    job_message    varchar(500)              null comment '日志信息',
    status         char          default '0' null comment '执行状态（0正常 1失败）',
    exception_info varchar(2000) default ''  null comment '异常信息',
    create_time    datetime                  null comment '创建时间'
)
    comment '定时任务调度日志表' charset = utf8;

create table `ry-vue`.sys_logininfor
(
    info_id        bigint auto_increment comment '访问ID'
        primary key,
    user_name      varchar(50)  default ''  null comment '用户账号',
    ipaddr         varchar(128) default ''  null comment '登录IP地址',
    login_location varchar(255) default ''  null comment '登录地点',
    browser        varchar(50)  default ''  null comment '浏览器类型',
    os             varchar(50)  default ''  null comment '操作系统',
    status         char         default '0' null comment '登录状态（0成功 1失败）',
    msg            varchar(255) default ''  null comment '提示消息',
    login_time     datetime                 null comment '访问时间'
)
    comment '系统访问记录' charset = utf8;

create table `ry-vue`.sys_menu
(
    menu_id     bigint auto_increment comment '菜单ID'
        primary key,
    menu_name   varchar(50)              not null comment '菜单名称',
    parent_id   bigint       default 0   null comment '父菜单ID',
    order_num   int(4)       default 0   null comment '显示顺序',
    path        varchar(200) default ''  null comment '路由地址',
    component   varchar(255)             null comment '组件路径',
    query       varchar(255)             null comment '路由参数',
    is_frame    int(1)       default 1   null comment '是否为外链（0是 1否）',
    is_cache    int(1)       default 0   null comment '是否缓存（0缓存 1不缓存）',
    menu_type   char         default ''  null comment '菜单类型（M目录 C菜单 F按钮）',
    visible     char         default '0' null comment '菜单状态（0显示 1隐藏）',
    status      char         default '0' null comment '菜单状态（0正常 1停用）',
    perms       varchar(100)             null comment '权限标识',
    icon        varchar(100) default '#' null comment '菜单图标',
    create_by   varchar(64)  default ''  null comment '创建者',
    create_time datetime                 null comment '创建时间',
    update_by   varchar(64)  default ''  null comment '更新者',
    update_time datetime                 null comment '更新时间',
    remark      varchar(500) default ''  null comment '备注'
)
    comment '菜单权限表' charset = utf8;

create table `ry-vue`.sys_notice
(
    notice_id      int(4) auto_increment comment '公告ID'
        primary key,
    notice_title   varchar(50)             not null comment '公告标题',
    notice_type    char                    not null comment '公告类型（1通知 2公告）',
    notice_content longblob                null comment '公告内容',
    status         char        default '0' null comment '公告状态（0正常 1关闭）',
    create_by      varchar(64) default ''  null comment '创建者',
    create_time    datetime                null comment '创建时间',
    update_by      varchar(64) default ''  null comment '更新者',
    update_time    datetime                null comment '更新时间',
    remark         varchar(255)            null comment '备注'
)
    comment '通知公告表' charset = utf8;

create table `ry-vue`.sys_oper_log
(
    oper_id        bigint auto_increment comment '日志主键'
        primary key,
    title          varchar(50)   default '' null comment '模块标题',
    business_type  int(2)        default 0  null comment '业务类型（0其它 1新增 2修改 3删除）',
    method         varchar(100)  default '' null comment '方法名称',
    request_method varchar(10)   default '' null comment '请求方式',
    operator_type  int(1)        default 0  null comment '操作类别（0其它 1后台用户 2手机端用户）',
    oper_name      varchar(50)   default '' null comment '操作人员',
    dept_name      varchar(50)   default '' null comment '部门名称',
    oper_url       varchar(255)  default '' null comment '请求URL',
    oper_ip        varchar(128)  default '' null comment '主机地址',
    oper_location  varchar(255)  default '' null comment '操作地点',
    oper_param     varchar(2000) default '' null comment '请求参数',
    json_result    varchar(2000) default '' null comment '返回参数',
    status         int(1)        default 0  null comment '操作状态（0正常 1异常）',
    error_msg      varchar(2000) default '' null comment '错误消息',
    oper_time      datetime                 null comment '操作时间'
)
    comment '操作日志记录' charset = utf8;

create table `ry-vue`.sys_post
(
    post_id     bigint auto_increment comment '岗位ID'
        primary key,
    post_code   varchar(64)            not null comment '岗位编码',
    post_name   varchar(50)            not null comment '岗位名称',
    post_sort   int(4)                 not null comment '显示顺序',
    status      char                   not null comment '状态（0正常 1停用）',
    create_by   varchar(64) default '' null comment '创建者',
    create_time datetime               null comment '创建时间',
    update_by   varchar(64) default '' null comment '更新者',
    update_time datetime               null comment '更新时间',
    remark      varchar(500)           null comment '备注'
)
    comment '岗位信息表' charset = utf8;

create table `ry-vue`.sys_role
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

create table `ry-vue`.sys_role_dept
(
    role_id bigint not null comment '角色ID',
    dept_id bigint not null comment '部门ID',
    primary key (role_id, dept_id)
)
    comment '角色和部门关联表' charset = utf8;

create table `ry-vue`.sys_role_menu
(
    role_id bigint not null comment '角色ID',
    menu_id bigint not null comment '菜单ID',
    primary key (role_id, menu_id)
)
    comment '角色和菜单关联表' charset = utf8;

create table `ry-vue`.sys_user
(
    user_id     bigint auto_increment comment '用户ID'
        primary key,
    dept_id     bigint                    null comment '部门ID',
    user_name   varchar(30)               not null comment '用户账号',
    nick_name   varchar(30)               not null comment '用户昵称',
    user_type   varchar(2)   default '00' null comment '用户类型（00系统用户）',
    email       varchar(50)  default ''   null comment '用户邮箱',
    phonenumber varchar(11)  default ''   null comment '手机号码',
    sex         char         default '0'  null comment '用户性别（0男 1女 2未知）',
    avatar      varchar(100) default ''   null comment '头像地址',
    password    varchar(100) default ''   null comment '密码',
    status      char         default '0'  null comment '帐号状态（0正常 1停用）',
    del_flag    char         default '0'  null comment '删除标志（0代表存在 2代表删除）',
    login_ip    varchar(128) default ''   null comment '最后登录IP',
    login_date  datetime                  null comment '最后登录时间',
    create_by   varchar(64)  default ''   null comment '创建者',
    create_time datetime                  null comment '创建时间',
    update_by   varchar(64)  default ''   null comment '更新者',
    update_time datetime                  null comment '更新时间',
    remark      varchar(500)              null comment '备注'
)
    comment '用户信息表' charset = utf8;

create table `ry-vue`.sys_user_post
(
    user_id bigint not null comment '用户ID',
    post_id bigint not null comment '岗位ID',
    primary key (user_id, post_id)
)
    comment '用户与岗位关联表' charset = utf8;

create table `ry-vue`.sys_user_role
(
    user_id bigint not null comment '用户ID',
    role_id bigint not null comment '角色ID',
    primary key (user_id, role_id)
)
    comment '用户和角色关联表' charset = utf8;

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
    car_num               tinyint(1) default 1                     not null comment '1-10的循环',
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

create table `ry-vue`.t_dtc_report
(
    uid                    bigint(32) auto_increment comment '序列号，唯一标志主键'
        primary key,
    Adresse                varchar(255) null comment 'Adresse',
    Systembezeichnung      varchar(255) null comment 'Systembezeichnung',
    SearchedOdxFileVersion varchar(255) null comment 'SearchedOdxFileVersion',
    componentType          varchar(255) null comment 'componentType'
)
    comment '`varchar(255)`' charset = utf8;

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

