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
 * `菜单权限`对象 t_category_menu
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@TableName
public class TCategoryMenu extends LocalBaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private String uid;

    /** 菜单名称 */
    @Excel(name = "菜单名称")
    private String menuName;

    /** 菜单级别 */
    @Excel(name = "菜单级别")
    private Integer menuLevel;

    /** 简介 */
    @Excel(name = "简介")
    private String summary;

    /** 父uid */
    @Excel(name = "父uid")
    private String parentUid;

    /** url地址 */
    @Excel(name = "url地址")
    private String url;

    /** 图标 */
    @Excel(name = "图标")
    private String icon;

    /** 排序字段越大越靠前 */
    @Excel(name = "排序字段越大越靠前")
    private Long sort;

    /** 状态（1正常0停用） */
    @Excel(name = "状态", readConverterExp = "1=正常0停用")
    private Integer status;

    /** 是否显示 1:是 0:否 */
    @Excel(name = "是否显示 1:是 0:否")
    private Integer isShow;

    /** 菜单类型 0: 菜单   1: 按钮 */
    @Excel(name = "菜单类型 0: 菜单   1: 按钮")
    private Integer menuType;

    /** 是否跳转外部链接 0：否1：是 */
    @Excel(name = "是否跳转外部链接 0：否1：是")
    private Integer isJumpExternalUrl;

    /** 创建者 */
    @Excel(name = "创建者")
    private String createByUid;

    /** 更新者 */
    @Excel(name = "更新者")
    private String updateByUid;

    /** 路由地址 */
    @Excel(name = "路由地址")
    private String path;

    /** 组件路径 */
    @Excel(name = "组件路径")
    private String component;

    /** 路由参数 */
    @Excel(name = "路由参数")
    private String query;

    /** 是否缓存（1缓存0不缓存） */
    @Excel(name = "是否缓存", readConverterExp = "1=缓存0不缓存")
    private Integer isCache;

    /** 权限标识 */
    @Excel(name = "权限标识")
    private String perms;

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getUid()
    {
        return uid;
    }
    public void setMenuName(String menuName) 
    {
        this.menuName = menuName;
    }

    public String getMenuName() 
    {
        return menuName;
    }
    public void setMenuLevel(Integer menuLevel) 
    {
        this.menuLevel = menuLevel;
    }

    public Integer getMenuLevel() 
    {
        return menuLevel;
    }
    public void setSummary(String summary) 
    {
        this.summary = summary;
    }

    public String getSummary() 
    {
        return summary;
    }
    public void setParentUid(String parentUid) 
    {
        this.parentUid = parentUid;
    }

    public String getParentUid() 
    {
        return parentUid;
    }
    public void setUrl(String url) 
    {
        this.url = url;
    }

    public String getUrl() 
    {
        return url;
    }
    public void setIcon(String icon) 
    {
        this.icon = icon;
    }

    public String getIcon() 
    {
        return icon;
    }
    public void setSort(Long sort) 
    {
        this.sort = sort;
    }

    public Long getSort() 
    {
        return sort;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setIsShow(Integer isShow) 
    {
        this.isShow = isShow;
    }

    public Integer getIsShow() 
    {
        return isShow;
    }
    public void setMenuType(Integer menuType) 
    {
        this.menuType = menuType;
    }

    public Integer getMenuType() 
    {
        return menuType;
    }
    public void setIsJumpExternalUrl(Integer isJumpExternalUrl) 
    {
        this.isJumpExternalUrl = isJumpExternalUrl;
    }

    public Integer getIsJumpExternalUrl() 
    {
        return isJumpExternalUrl;
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
    public void setPath(String path) 
    {
        this.path = path;
    }

    public String getPath() 
    {
        return path;
    }
    public void setComponent(String component) 
    {
        this.component = component;
    }

    public String getComponent() 
    {
        return component;
    }
    public void setQuery(String query) 
    {
        this.query = query;
    }

    public String getQuery() 
    {
        return query;
    }
    public void setIsCache(Integer isCache) 
    {
        this.isCache = isCache;
    }

    public Integer getIsCache() 
    {
        return isCache;
    }
    public void setPerms(String perms) 
    {
        this.perms = perms;
    }

    public String getPerms() 
    {
        return perms;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("uid", getUid())
            .append("menuName", getMenuName())
            .append("menuLevel", getMenuLevel())
            .append("summary", getSummary())
            .append("parentUid", getParentUid())
            .append("url", getUrl())
            .append("icon", getIcon())
            .append("sort", getSort())
            .append("status", getStatus())
            .append("isShow", getIsShow())
            .append("menuType", getMenuType())
            .append("isJumpExternalUrl", getIsJumpExternalUrl())
            .append("createByUid", getCreateByUid())
            .append("updateByUid", getUpdateByUid())
            .append("path", getPath())
            .append("component", getComponent())
            .append("query", getQuery())
            .append("isCache", getIsCache())
            .append("perms", getPerms())
            .toString();
    }
}
