package com.ruoyi.system.domain.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.system.domain.vo.DeviceInfoVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@ApiModel(value = "DesktopRecordParam",description = "桌面云端同步参数")
@Data
public class DesktopRecordParam {
        private static final long serialVersionUID = 1L;
        @ApiModelProperty
        String recordUid;
        @ApiModelProperty
        @NotNull
        String indexUid;
        /** 操作时间 */
        @ApiModelProperty
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @NotNull
        private Date operTime;
        @ApiModelProperty
        String functionGroupType;
        @ApiModelProperty
        @JsonFormat(pattern = "yyyy-MM-dd")
        Date testDate;
        @ApiModelProperty
        String taskType;
        @ApiModelProperty
        Integer mileacge;
        @ApiModelProperty
        Integer testHour;
        @ApiModelProperty
        String location;
        @ApiModelProperty
        String systemReset;
        @ApiModelProperty
        String naviReset;
        @ApiModelProperty
        String blackMap;
        @ApiModelProperty
        String initializingOccurred;
        @ApiModelProperty
        String fallBackScreen;
        @ApiModelProperty
        String bussleep;
        @ApiModelProperty
        String plannedTicket;
        @ApiModelProperty
        String comment;
        @ApiModelProperty
        String operationType;
        @ApiModelProperty
        String tester;
        @ApiModelProperty
        @NotNull
        private List<DesktopLogParam> desktopLogParams;
        @ApiModelProperty
        @NotNull
        private DeviceInfoVo desktopDevice;
}
