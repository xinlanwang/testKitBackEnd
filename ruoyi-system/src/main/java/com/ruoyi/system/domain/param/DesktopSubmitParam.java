package com.ruoyi.system.domain.param;

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
        Date submitDate;


       List<DesktopRecordParam> desktopRecordParams;
}
