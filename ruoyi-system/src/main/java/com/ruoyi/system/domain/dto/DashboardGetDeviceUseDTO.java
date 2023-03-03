package com.ruoyi.system.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@ApiModel(value = "DashboardGetDeviceUseDTO",description = "DeviceUse")
@Data
public class DashboardGetDeviceUseDTO {
    private Long recordUid;
    private String deviceName;
    private Double testHour;
    private Double mileacge;
    private Date operTime;
}
