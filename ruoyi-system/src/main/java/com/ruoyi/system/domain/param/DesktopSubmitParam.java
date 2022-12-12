package com.ruoyi.system.domain.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@ApiModel(value = "DesktopSubmitParam",description = "桌面云端同步参数")
@Data
public class DesktopSubmitParam  {
        private static final long serialVersionUID = 1L;
        @ApiModelProperty
        @NotNull
        String localHostAcoount;
        @ApiModelProperty
        @NotNull
        String localHostPassword;

        @ApiModelProperty
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        Date submitDate;
        @ApiModelProperty
        private String operIp;
        @NotNull
        List<DesktopRecordParam> desktopRecordParams;
}
