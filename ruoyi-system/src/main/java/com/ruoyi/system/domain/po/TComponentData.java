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

    private String partNumber;
    private String swVersion;
    private String hwVersion;
    private String minimalHw;
    private String temporaryVariable;

    /** mu代表mu，gw代表gw，hud代表hud，km代表kombi等 */
    @Excel(name = "mu代表mu，gw代表gw，hud代表hud，km代表kombi等")
    private String componentType;

    /** 配件名称 */
    @Excel(name = "配件名称")
    private String componentName;

    /** hw代表硬件，sw软件，ot代表其他 */
    @Excel(name = "hw代表硬件，sw软件，ot代表其他")
    private String wareType;

    /** 创建者 */
    @Excel(name = "创建者")
    private String createByUid;

    /** 更新者 */
    @Excel(name = "更新者")
    private String updateByUid;

    /** 软硬件版本号 */
    @Excel(name = "配件版本")
    private String componentVersion;


    public void setUid(Long uid)
    {
        this.uid = uid;
    }

    public Long getUid()
    {
        return uid;
    }
    public void setComponentType(String componentType) 
    {
        this.componentType = componentType;
    }

    public String getComponentType() 
    {
        return componentType;
    }
    public void setcomponentVersion(String componentVersion)
    {
        this.componentVersion = componentVersion;
    }

    public String getcomponentVersion()
    {
        return componentVersion;
    }
    public void setWareType(String wareType) 
    {
        this.wareType = wareType;
    }

    public String getWareType() 
    {
        return wareType;
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
    public void setcomponentName(String componentName)
    {
        this.componentName = componentName;
    }

    public String getcomponentName()
    {
        return componentName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("uid", getUid())
            .append("componentType", getComponentType())
            .append("componentVersion", getcomponentVersion())
            .append("wareType", getWareType())
            .append("createByUid", getCreateByUid())
            .append("updateByUid", getUpdateByUid())
            .append("componentName", getcomponentName())
            .toString();
    }
}
