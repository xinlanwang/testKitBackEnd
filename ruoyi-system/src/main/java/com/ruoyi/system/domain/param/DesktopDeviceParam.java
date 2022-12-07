package com.ruoyi.system.domain.param;

import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value = "DesktopDeviceParam",description = "设备同步参数")
@Data
public class DesktopDeviceParam extends BaseEntity {
        private static final long serialVersionUID = 1L;
        @ApiModelProperty
        Date updateDate;
        @ApiModelProperty
        String carlineInfoUid;
        @ApiModelProperty
        String localHostAcoount;
        @ApiModelProperty
        String localHostPassword;
        @ApiModelProperty
        String desktopUid;
        @ApiModelProperty
        String desktopDeviceInformation;
}
