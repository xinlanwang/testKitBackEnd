package com.ruoyi.system.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.LocalBaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * `虚拟设备参数`对象 t_carline_info
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@TableName
@Data
public class TCarlineInfo extends LocalBaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long carlineInfoUid;

    private String deviceName;

    /** 版本号id */
    @Excel(name = "版本号id")
    private Long clusterUid;

    /** 平台类型 */
    @Excel(name = "平台类型")
    private String platformType;

    /** 区域中国（zn）台湾(z1)香港(z2)北美(na0)东南亚(as0)，逗号分割 */
    @Excel(name = "区域中国", readConverterExp = "z=n")
    private String marketType;

    /** 类型 */
    @Excel(name = "类型")
    private String basicType;

    /** variant */
    @Excel(name = "variantType")
    private String variantType;

    /** vin码 */
    @Excel(name = "vin码")
    private String vinCode;

    /** 数据库版本 */
    @Excel(name = "数据库版本")
    private String dbVersion;

    /** 创建者 */
    @Excel(name = "创建者")
    private String createByUid;

    /** 更新者 */
    @Excel(name = "更新者")
    private String updateByUid;

    /** 反馈 */
    @Excel(name = "反馈")
    private String resp;

}
