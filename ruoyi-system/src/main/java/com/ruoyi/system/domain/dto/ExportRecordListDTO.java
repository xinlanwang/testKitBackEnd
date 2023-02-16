package com.ruoyi.system.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.system.domain.vo.DeviceInfoVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ExportRecordListDTO {
    /**
     * record
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Test Date", width = 30, dateFormat = "yyyy-MM-dd")
    private Date operTime;
    @Excel(name = "Tester")
    private String operUserName;
    @Excel(name = "Car/Bench")
    private String deviceType;
    @Excel(name = "DeviceName")
    private String deviceName;
    @Excel(name = "functionGroup")
    private String functionGroupType;
    @Excel(name = "Clu")
    private String clusterName;
    @Excel(name = "Platform")
    private String platformType;
    @Excel(name = "Project")
    private String projectType;
    @Excel(name = "Variant")
    private String variantType;
    @Excel(name = "Carline")
    private String carlineModelType;
    @Excel(name = "Market")
    private String marketType;
    @Excel(name = "DB")
    private String dbVersion;
    @Excel(name = "VIN")
    private String vinCode;
    @Excel(name = "Task")
    private String taskType;
    @Excel(name = "Mileacge")
    private Integer mileacge;
    @Excel(name = "Test Hour")
    private Integer testHour;
    @Excel(name = "Location")
    private String location;
    @Excel(name = "System Reset")
    private String systemReset;
    @Excel(name = "Navi Reset")
    private String naviReset;
    @Excel(name = "Black Map")
    private String blackMap;
    @Excel(name = "Initializing Occurred")
    private String initializingOccurred;
    @Excel(name = "Fall Back Screen")
    private String fallBackScreen;
    @Excel(name = "Bussleep")
    private String bussleep;
    @Excel(name = "Planned Ticket")
    private String plannedTicket;
    @Excel(name = "Comment")
    private String comment;

    //component
    @Excel(name = "0075 CONBOX/OCU")
    private String CONBOXOROCU;
    @Excel(name = "0019 GATEWAY")
    private String GATEWAY;
    @Excel(name = "005F-Data Medium DBVERSION")
    private String dataMedium;
    @Excel(name = "0017 KOMBI")
    private String KOMBI;
    @Excel(name = "005F MU")
    private String MU;
    @Excel(name = "0046 BCM2")
    private String BCM2;
    @Excel(name = "005F-AED ASTERIX")
    private String ASTERIX;
    @Excel(name = "0009 BCM1")
    private String BCM1;
    @Excel(name = "005F-ZDC MU-ZDC")
    private String MUZDC;

}
