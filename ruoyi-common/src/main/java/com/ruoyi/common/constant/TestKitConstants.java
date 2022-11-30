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
     * matrix规则类型
     */
    public static final String MATRIXTYPE_VALIDATE = "1";
    public static final String MATRIXTYPE_MATRIX = "2";


    public static final String DEVICE_TYPE_BENCH = "1";
    public static final String DEVICE_TYPE_CAR = "2";
    public static final String DEVICE_TYPE_GOLDENCAR = "3";

}
