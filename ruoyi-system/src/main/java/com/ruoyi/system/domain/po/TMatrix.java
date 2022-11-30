package com.ruoyi.system.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.LocalBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * `BIGINT(32)`对象 t_matrix
 * 
 * @author ruoyi
 * @date 2022-11-21
 */
@TableName
@ApiModel
@Data
public class TMatrix  extends LocalBaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Long uid;

    @ApiModelProperty("matrix类型，1为matrix下的，2为validateAll下的")
    private String matrixType;
    @ApiModelProperty("目标车类型")
    private String goldenCarType;
    @ApiModelProperty("目标车版本名称")
    private String goldenClusterNameType;


    /** 版本号名称 */
    @Excel(name = "CLU")
    @ApiModelProperty("版本号名称")
    private String clusterName;

    /** 平台类型 */
    @Excel(name = "Platform")
    @ApiModelProperty("平台类型")
    private String platformType;

    /** 工程id */
    @Excel(name = "Project")
    @ApiModelProperty("工程id")
    private String projectType;

    /** variant */
    @Excel(name = "Variant")
    @ApiModelProperty("variantType")
    private String variantType;

    /** 汽车名称 */
    @Excel(name = "Carline")
    @ApiModelProperty("汽车名称")
    private String carlineModelType;

    /** 区域中国（zn）台湾(z1)香港(z2)北美(na0)东南亚(as0)，逗号分割 */
    @Excel(name = "Market")
    @ApiModelProperty("区域")
    private String marketTypes;

    /** ocu_cbox */
    @Excel(name = "OCU/cBox")
    @ApiModelProperty("ocu_cbox")
    private String ocuCboxType;

    /** gateway */
    @Excel(name = "Gateway/ICAS1")
    @ApiModelProperty("gatewayType")
    private String gatewayType;

    /** 创建者 */
    @ApiModelProperty("创建者")
    private String createByUid;

    /** 更新者 */
    @ApiModelProperty("更新者")
    private String updateByUid;
}
