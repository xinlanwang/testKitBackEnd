package com.ruoyi.system.domain.param;


import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "MatrixListParam",description = "Matrix列表查询参数")
@Data
public class MatrixMappingParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("选择属性名称，可选字段有：clusterNames，platformTypes，projectTypes，variantTypes,carlineModelTypes,marketTypes,ocuCboxTypes,gatewayTypes")
    private String selectProperty;
    /** 版本号名称 */
    @ApiModelProperty("版本号名称")
    private String[] clusterNames;

    /** 平台类型 */
    @ApiModelProperty("平台类型")
    private String[] platformTypes;

    /** 工程id */
    @ApiModelProperty("工程id")
    private String[] projectTypes;

    /** variant */
    @ApiModelProperty("variantTypes")
    private String[] variantTypes;

    /** 汽车名称 */
    @ApiModelProperty("汽车模型名称类型")
    private String[] carlineModelTypes;

    /** 区域中国（zn）台湾(z1)香港(z2)北美(na0)东南亚(as0)，逗号分割 */
    @Excel(name = "区域中国", readConverterExp = "z=n")
    @ApiModelProperty("区域名称")
    private String[] marketTypes;

    /** ocu_cbox */
    @Excel(name = "ocu_cbox")
    @ApiModelProperty("ocuCboxTypes")
    private String[] ocuCboxTypes;

    /** gateway */
    @Excel(name = "gateway")
    @ApiModelProperty("网关")
    private String[] gatewayTypes;

    @ApiModelProperty("目标车类型")
    private String[] goldenCarTypes;
    @ApiModelProperty("目标车版本名称")
    private String[] goldenClusterNameTypes;

}
