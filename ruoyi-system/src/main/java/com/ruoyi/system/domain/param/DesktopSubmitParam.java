package com.ruoyi.system.domain.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@ApiModel(value = "DesktopSubmitParam",description = "桌面云端同步参数")
@Data
public class DesktopSubmitParam  {
        private static final long serialVersionUID = 1L;
        @ApiModelProperty
        String localHostAcoount;
        @ApiModelProperty
        String localHostPassword;
        @ApiModelProperty
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        Date submitDate;
        @ApiModelProperty
        private String operIp;
       List<DesktopRecordParam> desktopRecordParams;
}
