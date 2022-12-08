package com.ruoyi.system.domain.param;

import com.ruoyi.system.domain.po.TDesktopLog;
import com.ruoyi.system.domain.vo.DeviceInfoComponent;
import com.ruoyi.system.domain.vo.DeviceInfoVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@ApiModel(value = "DesktopRecordParam",description = "桌面云端同步参数")
@Data
public class DesktopRecordParam {
        private static final long serialVersionUID = 1L;
        @ApiModelProperty
        String recordUid;

        @ApiModelProperty
        String indexUid;
        /** 操作时间 */
        @ApiModelProperty
        private Date operTime;

        /** 主机地址 */
        @ApiModelProperty
        private String operIp;

        @ApiModelProperty
        String functionGroupType;
        @ApiModelProperty
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
        private List<DesktopLogParam> desktopLogParams;
        @ApiModelProperty
        private DeviceInfoVo desktopDevice;
}