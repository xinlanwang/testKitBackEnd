package com.ruoyi.system.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * `车型基本数据`对象 t_carline
 * 
 * @author ruoyi
 * @date 2022-11-03
 */


@TableName("t_dtc_report")
@Data
public class TDTCReport
{
    private static final long serialVersionUID = 1L;

    /** 唯一uid */
    @TableId(type = IdType.AUTO)
    private Long uid;
    @TableField("Adresse")
    private String Adresse;
    @TableField("ecu_id")
    private String ecuId;
    @TableField("componentType")
    private String componentType;
    private String variant;
}
