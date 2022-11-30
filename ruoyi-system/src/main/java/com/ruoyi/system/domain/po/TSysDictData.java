package com.ruoyi.system.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.LocalBaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * `字典数据`对象 t_sys_dict_data
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
@TableName
public class TSysDictData extends LocalBaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long uid;

    /** 自增键oid */
    @Excel(name = "自增键oid")
    private Long oid;

    /** 字典类型UID */
    @Excel(name = "字典类型UID")
    private Long dictTypeUid;

    /** 字典标签 */
    @Excel(name = "字典标签")
    private String dictLabel;

    /** 字典键值 */
    @Excel(name = "字典键值")
    private String dictValue;

    /** 样式属性（其他样式扩展） */
    @Excel(name = "样式属性", readConverterExp = "其=他样式扩展")
    private String cssClass;

    /** 表格回显样式 */
    @Excel(name = "表格回显样式")
    private String listClass;

    /** 是否默认（1是0否） */
    @Excel(name = "是否默认", readConverterExp = "1=是0否")
    private Integer isDefault;

    /** 创建人UID */
    @Excel(name = "创建人UID")
    private String createByUid;

    /** 最后更新人UID */
    @Excel(name = "最后更新人UID")
    private String updateByUid;

    /** 状态 */
    @Excel(name = "状态")
    private Integer status;

    /** 是否发布(1:是0:否) */
    @Excel(name = "是否发布(1:是0:否)")
    private Integer isPublish;

    /** 排序字段 */
    @Excel(name = "排序字段")
    private Long sort;

    public void setUid(Long uid)
    {
        this.uid = uid;
    }

    public Long getUid()
    {
        return uid;
    }
    public void setOid(Long oid) 
    {
        this.oid = oid;
    }

    public Long getOid() 
    {
        return oid;
    }
    public void setDictTypeUid(Long dictTypeUid)
    {
        this.dictTypeUid = dictTypeUid;
    }

    public Long getDictTypeUid()
    {
        return dictTypeUid;
    }
    public void setDictLabel(String dictLabel) 
    {
        this.dictLabel = dictLabel;
    }

    public String getDictLabel() 
    {
        return dictLabel;
    }
    public void setDictValue(String dictValue) 
    {
        this.dictValue = dictValue;
    }

    public String getDictValue() 
    {
        return dictValue;
    }
    public void setCssClass(String cssClass) 
    {
        this.cssClass = cssClass;
    }

    public String getCssClass() 
    {
        return cssClass;
    }
    public void setListClass(String listClass) 
    {
        this.listClass = listClass;
    }

    public String getListClass() 
    {
        return listClass;
    }
    public void setIsDefault(Integer isDefault) 
    {
        this.isDefault = isDefault;
    }

    public Integer getIsDefault() 
    {
        return isDefault;
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
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setIsPublish(Integer isPublish) 
    {
        this.isPublish = isPublish;
    }

    public Integer getIsPublish() 
    {
        return isPublish;
    }
    public void setSort(Long sort) 
    {
        this.sort = sort;
    }

    public Long getSort() 
    {
        return sort;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("uid", getUid())
            .append("oid", getOid())
            .append("dictTypeUid", getDictTypeUid())
            .append("dictLabel", getDictLabel())
            .append("dictValue", getDictValue())
            .append("cssClass", getCssClass())
            .append("listClass", getListClass())
            .append("isDefault", getIsDefault())
            .append("createByUid", getCreateByUid())
            .append("updateByUid", getUpdateByUid())
            .append("status", getStatus())
            .append("isPublish", getIsPublish())
            .append("sort", getSort())
            .toString();
    }
}
