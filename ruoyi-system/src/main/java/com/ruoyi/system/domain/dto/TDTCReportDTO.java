package com.ruoyi.system.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.LocalBaseEntity;
import lombok.Data;

/**
 * `车型基本数据`对象 t_carline
 * 
 * @author ruoyi
 * @date 2022-11-03
 */


@TableName()
@Data
public class TDTCReportDTO extends LocalBaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 唯一uid */
    private String Systembezeichnung;
    private String Adresse;
    private String SubtName;
    private String HWTeilenummer;
    private String Fahrgestellnummer;
    private String SearchedOdxFileVersion;
    private String SWVersion;
    private String HWVersion;
    private String ZdcName;
    private String ZdcVersion;
    private String regularString;
    private String componentType;

}
