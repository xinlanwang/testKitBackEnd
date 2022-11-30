package com.ruoyi.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class DeviceInfoComponent {
    private Long carlineInfoUid;
    @ApiModelProperty("组件类型：mu代表mu，gw代表gw，hud代表hud，km代表kombi等")
    private String componentType;
    @ApiModelProperty("物品类型，hw代表硬件，sw软件，ot代表其他")
    private String wareType;
    @ApiModelProperty("配件版本")
    private String componentModel;
}
