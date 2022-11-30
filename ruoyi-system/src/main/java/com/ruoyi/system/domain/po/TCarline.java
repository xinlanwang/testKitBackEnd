package com.ruoyi.system.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.LocalBaseEntity;
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

    public void setUid(Long uid)
    {
        this.uid = uid;
    }

    public Long getUid()
    {
        return uid;
    }
    public void setCarlineName(String carlineModelType) 
    {
        this.carlineModelType = carlineModelType;
    }

    public String getCarlineName() 
    {
        return carlineModelType;
    }
    public void setgoldenCarName(String goldenCarName) 
    {
        this.goldenCarName = goldenCarName;
    }

    public String getgoldenCarName() 
    {
        return goldenCarName;
    }

    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setCreateByUid(String createByUid) 
    {
        this.createByUid = createByUid;
    }

    public String getCreateByUid() 
    {
        return createByUid;
    }
    public void setUpdateByUid(String updateByUid) 
    {
        this.updateByUid = updateByUid;
    }

    public String getUpdateByUid() 
    {
        return updateByUid;
    }
    public void setSort(Long sort) 
    {
        this.sort = sort;
    }

    public Long getSort() 
    {
        return sort;
    }
    public void setIsShow(Integer isShow) 
    {
        this.isShow = isShow;
    }

    public Integer getIsShow() 
    {
        return isShow;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("uid", getUid())
            .append("carlineModelType", getCarlineName())
            .append("goldenCarName", getgoldenCarName())
            .append("status", getStatus())
            .append("createByUid", getCreateByUid())
            .append("updateByUid", getUpdateByUid())
            .append("sort", getSort())
            .append("isShow", getIsShow())
            .toString();
    }
}
