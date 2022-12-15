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
 * `组件参数`对象 t_component_data
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@TableName
@Data
public class TComponentData extends LocalBaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 自增唯一值 */
    @Excel(name = "自增唯一值")
    @TableId(type = IdType.AUTO)
    private Long uid;
    private String componentInstanceName;
    private String partNumber;
    private Integer isAvaliabel;
    private String wareType;
    private String componentVersion;
    @Excel(name = "mu代表mu，gw代表gw，hud代表hud，km代表kombi等")
    private String componentType;
    /** 配件名称 */
    @Excel(name = "配件名称")
    private String componentName;
    private Integer sort;


    @Excel(name = "创建者")
    private String createByUid;
    /** 更新者 */
    @Excel(name = "更新者")
    private String updateByUid;


}
