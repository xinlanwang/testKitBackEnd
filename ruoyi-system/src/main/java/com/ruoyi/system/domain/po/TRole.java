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
 * `角色`对象 t_role
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
@TableName
public class TRole extends LocalBaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 角色id主键 */
    @TableId(type = IdType.AUTO)
    private String uid;

    /** 角色名 */
    @Excel(name = "角色名")
    private String roleName;

    /** 角色权限字符串 */
    @Excel(name = "角色权限字符串")
    private String roleKey;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Long roleSort;

    /** 创建者 */
    @Excel(name = "创建者")
    private String createByUid;

    /** 更新者 */
    @Excel(name = "更新者")
    private String updateByUid;

    /** 角色状态（1正常0停用） */
    @Excel(name = "角色状态", readConverterExp = "1=正常0停用")
    private Integer status;

    /** 删除标志（1代表存在0代表删除） */
    private String delFlag;

    /** 角色介绍 */
    @Excel(name = "角色介绍")
    private String summary;

    /** 角色管辖的菜单的UID */
    @Excel(name = "角色管辖的菜单的UID")
    private String categoryMenuUids;

    public void setUid(String uid) 
    {
        this.uid = uid;
    }

    public String getUid() 
    {
        return uid;
    }
    public void setRoleName(String roleName) 
    {
        this.roleName = roleName;
    }

    public String getRoleName() 
    {
        return roleName;
    }
    public void setRoleKey(String roleKey) 
    {
        this.roleKey = roleKey;
    }

    public String getRoleKey() 
    {
        return roleKey;
    }
    public void setRoleSort(Long roleSort) 
    {
        this.roleSort = roleSort;
    }

    public Long getRoleSort() 
    {
        return roleSort;
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
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }
    public void setSummary(String summary) 
    {
        this.summary = summary;
    }

    public String getSummary() 
    {
        return summary;
    }
    public void setCategoryMenuUids(String categoryMenuUids) 
    {
        this.categoryMenuUids = categoryMenuUids;
    }

    public String getCategoryMenuUids() 
    {
        return categoryMenuUids;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("uid", getUid())
            .append("roleName", getRoleName())
            .append("roleKey", getRoleKey())
            .append("roleSort", getRoleSort())
            .append("createByUid", getCreateByUid())
            .append("updateByUid", getUpdateByUid())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("summary", getSummary())
            .append("categoryMenuUids", getCategoryMenuUids())
            .toString();
    }
}
