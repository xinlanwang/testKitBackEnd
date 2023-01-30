package com.ruoyi.system.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class CheckDeviceIntegralityDTO {
    Boolean isIntegrated = true;
    String message = "";
}
