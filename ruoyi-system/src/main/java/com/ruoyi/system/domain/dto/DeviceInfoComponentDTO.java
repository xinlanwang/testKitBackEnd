package com.ruoyi.system.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class DeviceInfoComponentDTO {
    @ApiModelProperty("组件类型：mu代表mu，gw代表gw，hud代表hud，km代表kombi等")
    private String componentType;
    @ApiModelProperty("序列号")
    private String partNumber;
    @ApiModelProperty("版本名称，对应SOP_Carline")
    private String clusterName;
    @ApiModelProperty("最低配置")
    private String minimalHW;

    private String componentName;

    private String wareType;

}
