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
    private String componentName;
    private String partNumber;
    private String hwVersion;
    private String swVersion;
    private String componentVersion;
    private String wareType;
    private String zdcName;
    private String zdcVersion;
}
