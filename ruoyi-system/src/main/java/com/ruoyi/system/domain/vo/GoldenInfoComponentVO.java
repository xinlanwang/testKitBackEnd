package com.ruoyi.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("goldenInfo配件列表")
@Data
public class GoldenInfoComponentVO {


    @ApiModelProperty("序列号")
    private String partNumber;
    @ApiModelProperty("版本名称，对应SOP_Carline")
    private String clusterName;
    @ApiModelProperty("软件配件名称，对应SW version")
    private String hwComponentVersion;
    @ApiModelProperty("硬件配件名称，对应HW version")
    private String swComponentVersion;
    private String temporaryVariable;
    @ApiModelProperty("最低配置")
    private String minimalHW;

    private String componentType;


}
