package com.ruoyi.system.domain.dto;

import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ParseDTCReportDTO {
    private String variant;
    private String SWVersion;
    private String HWVersion;
    private String HWTeilenummer;
    private String ZDVersion;
    private String ZDName;
}
