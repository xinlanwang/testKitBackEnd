package com.ruoyi.system.domain.param;

import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "DeviceCompareParam",description = "设备比较参数")
@Data
public class DeviceCompareParam extends BaseEntity {
        private static final long serialVersionUID = 1L;


        String carlineModelType;
        String clusterName;
        String marketType;
        String wareType;
        String componentType;
        String componentModel;
}
