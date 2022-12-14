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
 * `设备-组件关联（n2n）`对象 t_carline_component
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@TableName
@Data
public class TCarlineComponent extends LocalBaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 自增唯一值 */
    @Excel(name = "自增唯一值")
    @TableId(type = IdType.AUTO)
    private Long uid;

    /** 车型参数主键 */
    @Excel(name = "车型参数主键")
    private Long carlineInfoUid;

    private Long swVersionUid;
    private Long hwVersionUid;
    private Long otherVersionUid;

    /** 最低配置 */
    @Excel(name = "最低配置")
    private String minimalHw;

    private String temporaryVariable;


    /** 创建者 */
    @Excel(name = "创建者")
    private String createByUid;

    /** 更新者 */
    @Excel(name = "更新者")
    private String updateByUid;

}
