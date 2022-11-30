package com.ruoyi.system.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ImportValidateDTO {

    /** 版本号名称 */
    @Excel(name = "Cluster")
    @ApiModelProperty("版本号名称")
    private String clusterName;

    /** 工程id */
    @Excel(name = "Project")
    @ApiModelProperty("工程id")
    private String projectType;

    /** 平台类型 */
    @Excel(name = "Platform")
    @ApiModelProperty("平台类型")
    private String platformType;

    /** 区域中国（zn）台湾(z1)香港(z2)北美(na0)东南亚(as0)，逗号分割 */
    @Excel(name = "Market")
    @ApiModelProperty("区域")
    private String marketType;

    /** Function Group */
    @Excel(name = "Function Group")
    @ApiModelProperty("Function Group")
    private String functionGroupType;


    /** 汽车名称 */
    @Excel(name = "Carline")
    @ApiModelProperty("汽车名称")
    private String carlineModelType;


    /** variant */
    @Excel(name = "Variant")
    @ApiModelProperty("variantType")
    private String variantType;


    /** Task */
    @Excel(name = "Task")
    @ApiModelProperty("Task")
    private String taskType;

    /** Golden Car */
    @Excel(name = "Golden Car")
    @ApiModelProperty("Golden Car")
    private String goldenCarType;

    /** Golden CLU */
    @Excel(name = "Golden CLU")
    @ApiModelProperty("Golden CLU")
    private String goldenClusterNameType;

    /** 创建者 */
    @ApiModelProperty("创建者")
    private String createByUid;

    /** 更新者 */
    @ApiModelProperty("更新者")
    private String updateByUid;
}
