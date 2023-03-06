package com.ruoyi.system.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@ApiModel(value = "DashboardGetDeviceUseDTO",description = "DeviceUse")
@Data
public class DashboardGetDeviceTestHourDTO {
    private Double testHour;
    private Double mileacge;
    private Date operTime;
    private Integer plannedTicket;
    private Double benchTestHour;
    private Double carTestHour;
}
