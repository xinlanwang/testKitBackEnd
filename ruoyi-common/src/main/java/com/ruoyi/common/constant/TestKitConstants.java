package com.ruoyi.common.constant;

/**
 * 通用常量信息
 * 
 * @author ruoyi
 */
public class TestKitConstants
{
    /**
     * 字典状态（0正常 1停用）
     */
    public static final String DICT_STATUS_NORMAL = "0";
    public static final String DICT_STATUS_HIDE = "1";


    /**
     * 字典Matrix字典 （只能建立matrix的索引项，validateAll只能显示MATRIXTYPE_VALIDATE字段，DICT_MATRIXTYPE_OTHER查不到，其他的只可查，不可显示于前台（逻辑删除）
     */
    public static final String DICT_MATRIXTYPE_NON = "0";
    public static final String DICT_MATRIXTYPE_VALIDATE = "1";
    public static final String DICT_MATRIXTYPE_OTHER = "2";

    /**
     * OPERATION_TYPE
     */
    public static final String OPERATION_TYPE_INSERT = "1";
    public static final String OPERATION_TYPE_UPDATE = "2";
    public static final String OPERATION_TYPE_DELETE = "3";

    /**
     * BasicType
     */
    public static final String BASIC_TYPE_NORMAL = "1";
    public static final String BASIC_TYPE_WEB_DEVICE = "1";
    public static final String BASIC_TYPE_DESKTOP_DEVICE = "2";

    /**
     * matrix规则类型
     */
    public static final String MATRIXTYPE_VALIDATE = "1";
    public static final String MATRIXTYPE_MATRIX = "2";


    public static final String DEVICE_TYPE_BENCH = "1";
    public static final String DEVICE_TYPE_CAR = "2";
    public static final String DEVICE_TYPE_GOLDENCAR = "3";


    /**
     * 用户权限
     */
    public static final String SYS_ROLE_ADMIN = "1";//超管
    public static final String SYS_ROLE_MANAGER = "2";//普通管理员
    public static final String SYS_ROLE_NORMAL = "3";//普通用户


    /**
     * refresh路径
     */
    public static final String AUTO_IMPORT_DTC_PATH = "/home/data/testKit/data/auto_import/dtc";
//    public static final String AUTO_IMPORT_DTC_PATH = "C:\\Users\\10849\\Documents\\jianguo\\Work\\需求\\idex\\idex";
//    public static final String AUTO_IMPORT_GOLDEN_PATH = "C:\\Users\\10849\\Nutstore\\1\\Notebook\\EveryDay\\23-01\\23-01-16.assets\\test\\golden";
    public static final String AUTO_IMPORT_GOLDEN_PATH = "/home/data/testKit/data/auto_import/golden";
}
