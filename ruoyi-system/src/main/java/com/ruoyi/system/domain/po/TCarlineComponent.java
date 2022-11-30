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

    /** 组件主键 */
    private Long componentUid;

    /** 车型参数主键 */
    @Excel(name = "车型参数主键")
    private Long carlineInfoUid;

    /** 最低配置 */
    @Excel(name = "最低配置")
    private String minimalHw;

    private String partNumber;

    /** 创建者 */
    @Excel(name = "创建者")
    private String createByUid;

    /** 更新者 */
    @Excel(name = "更新者")
    private String updateByUid;
    private Integer sort;

}
