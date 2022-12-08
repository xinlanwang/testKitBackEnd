package com.ruoyi.system.domain.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * `BIGINT(32) auto_increment`对象 t_desktop_log
 * 
 * @author ruoyi
 * @date 2022-12-08
 */
@Data
@ApiModel
public class DesktopLogParam
{
    private static final long serialVersionUID = 1L;


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


}
