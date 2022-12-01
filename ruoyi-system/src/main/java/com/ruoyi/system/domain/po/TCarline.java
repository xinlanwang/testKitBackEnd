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
import org.springframework.data.annotation.Id;

/**
 * `车型基本数据`对象 t_carline
 * 
 * @author ruoyi
 * @date 2022-11-03
 */


@TableName()
@Data
public class TCarline  extends LocalBaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 唯一uid */
    @TableId(type = IdType.AUTO)
    private Long uid;

    /** 汽车名称 */
    @Excel(name = "汽车名称")
    private String carlineModelType;

    /** 版本规范车名称 */
    @Excel(name = "版本规范车名称")
    private String goldenCarName;


    /** 当前车辆状态  */
    @Excel(name = "当前车辆状态 ")
    private Integer status;

    /** 创建者 */
    @Excel(name = "创建者")
    private String createByUid;

    /** 更新者 */
    @Excel(name = "更新者")
    private String updateByUid;

    /** 排序字段越大越靠前 */
    @Excel(name = "排序字段越大越靠前")
    private Long sort;

    /** 是否显示：1显示0 不显示 */
    @Excel(name = "是否显示：1显示0 不显示")
    private Integer isShow;

}
