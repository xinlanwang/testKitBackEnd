package com.ruoyi.system.domain.po;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * `BIGINT(32) auto_increment`对象 t_desktop_record
 * 
 * @author ruoyi
 * @date 2022-12-07
 */
@Data
public class TDesktopRecord
{
    private static final long serialVersionUID = 1L;

    /** auto_increment */
    @Excel(name = "自增唯一值")
    @TableId(type = IdType.AUTO)
    private Long uid;
    private String localHostAcoount;

    /** 登录人员id */
    @Excel(name = "登录人员id")
    private String localhostUserId;

    /** 操作人员id */
    @Excel(name = "操作人员id")
    private String operUserName;

    /** 操作数据id */
    @Excel(name = "操作数据id")
    private Long dataUid;

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date operTime;

    /** 操作状态 */
    @Excel(name = "操作状态")
    private Integer status;

    /** 主机地址 */
    @Excel(name = "主机地址")
    private String operIp;

    /** 操作地点 */
    @Excel(name = "操作地点")
    private String operLocation;

    /** functionGroupType */
    @Excel(name = "functionGroupType")
    private String functionGroupType;

    /** taskType */
    @Excel(name = "taskType")
    private String taskType;

    /** mileacge */
    @Excel(name = "mileacge")
    private Integer mileacge;

    /** testHour */
    @Excel(name = "testHour")
    private Integer testHour;

    /** location */
    @Excel(name = "location")
    private String location;

    /** systemReset */
    @Excel(name = "systemReset")
    private String systemReset;

    /** naviReset */
    @Excel(name = "naviReset")
    private String naviReset;

    /** blackMap */
    @Excel(name = "blackMap")
    private String blackMap;

    /** initializingOccurred */
    @Excel(name = "initializingOccurred")
    private String initializingOccurred;

    /** fallBackScreen */
    @Excel(name = "fallBackScreen")
    private String fallBackScreen;

    /** bussleep */
    @Excel(name = "bussleep")
    private String bussleep;

    /** plannedTicket */
    @Excel(name = "plannedTicket")
    private String plannedTicket;

    /** comment */
    @Excel(name = "comment")
    private String comment;

}
