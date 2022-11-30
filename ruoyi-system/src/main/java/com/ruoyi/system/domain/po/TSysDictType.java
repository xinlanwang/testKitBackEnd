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
 * `字典类型`对象 t_sys_dict_type
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@TableName
public class TSysDictType extends LocalBaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private String uid;

    /** 自增键oid */
    @Excel(name = "自增键oid")
    private Long oid;

    /** 字典名称 */
    @Excel(name = "字典名称")
    private String dictName;

    /** 字典类型 */
    @Excel(name = "字典类型")
    private String dictType;

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

    public void setUid(String uid) 
    {
        this.uid = uid;
    }

    public String getUid() 
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
    public void setDictName(String dictName) 
    {
        this.dictName = dictName;
    }

    public String getDictName() 
    {
        return dictName;
    }
    public void setDictType(String dictType) 
    {
        this.dictType = dictType;
    }

    public String getDictType() 
    {
        return dictType;
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
            .append("dictName", getDictName())
            .append("dictType", getDictType())
            .append("createByUid", getCreateByUid())
            .append("updateByUid", getUpdateByUid())
            .append("status", getStatus())
            .append("isPublish", getIsPublish())
            .append("sort", getSort())
            .toString();
    }
}
