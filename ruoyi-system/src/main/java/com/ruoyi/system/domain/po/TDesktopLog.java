package com.ruoyi.system.domain.po;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * `BIGINT(32) auto_increment`对象 t_desktop_log
 * 
 * @author ruoyi
 * @date 2022-12-08
 */
@Data
@ApiModel
public class TDesktopLog
{
    private static final long serialVersionUID = 1L;

    /** auto_increment */
    @ApiModelProperty
    private Long uid;

    /** 登录人员id */
    @Excel(name = "登录人员id")
    @ApiModelProperty
    private Long localhostUserId;

    /** 操作人员id */
    @Excel(name = "操作人员id")
    @ApiModelProperty
    private String operUserName;

    /** 操作数据id */
    @Excel(name = "操作数据id")
    @ApiModelProperty
    private String operationUid;

    /** 方法名称(1为创建，2为修改，3为删除） */
    @Excel(name = "方法名称(1为创建，2为修改，3为删除）")
    @ApiModelProperty
    private String operationType;

    /** 修改属性 */
    @Excel(name = "修改属性")
    @ApiModelProperty
    private String operationFiled;

    /** 修改前数值 */
    @Excel(name = "修改前数值")
    @ApiModelProperty
    private String beforeOperationValue;

    /** 修改后数值 */
    @Excel(name = "修改后数值")
    @ApiModelProperty
    private String afterOperationValue;

    /** 请求方式 */
    @Excel(name = "请求方式")
    @ApiModelProperty
    private String requestMethod;

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date operTime;

    /** 请求URL */
    @Excel(name = "请求URL")
    @ApiModelProperty
    private String operUrl;

    /** 主机地址 */
    @Excel(name = "主机地址")
    @ApiModelProperty
    private String operIp;

}
