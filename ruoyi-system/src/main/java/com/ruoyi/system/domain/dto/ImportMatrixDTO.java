package com.ruoyi.system.domain.dto;

import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ImportMatrixDTO {

    /** 版本号名称 */
    @Excel(name = "CLU")
    @ApiModelProperty("版本号名称")
    private String clusterName;

    /** 平台类型 */
    @Excel(name = "Platform")
    @ApiModelProperty("平台类型")
    private String platformType;

    /** 工程id */
    @Excel(name = "Project")
    @ApiModelProperty("工程id")
    private String projectType;

    /** variant */
    @Excel(name = "Variant")
    @ApiModelProperty("variantType")
    private String variantType;

    /** 汽车名称 */
    @Excel(name = "Carline")
    @ApiModelProperty("汽车名称")
    private String carlineModelType;

    /** 区域中国（zn）台湾(z1)香港(z2)北美(na0)东南亚(as0)，逗号分割 */
    @Excel(name = "Market")
    @ApiModelProperty("区域")
    private String marketType;

    /** ocu_cbox */
    @Excel(name = "OCU/cBox")
    @ApiModelProperty("ocu_cbox")
    private String ocuCboxType;

    /** gateway */
    @Excel(name = "Gateway/ICAS1")
    @ApiModelProperty("gateway")
    private String gatewayType;
}
