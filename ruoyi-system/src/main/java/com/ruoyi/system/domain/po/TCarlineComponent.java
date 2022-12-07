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
    private String zdcName;
    private String zdcVersion;

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

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getCarlineInfoUid() {
        return carlineInfoUid;
    }

    public void setCarlineInfoUid(Long carlineInfoUid) {
        this.carlineInfoUid = carlineInfoUid;
    }

    public Long getSwVersionUid() {
        return swVersionUid;
    }

    public void setSwVersionUid(Long swVersionUid) {
        this.swVersionUid = swVersionUid;
    }

    public Long getHwVersionUid() {
        return hwVersionUid;
    }

    public void setHwVersionUid(Long hwVersionUid) {
        this.hwVersionUid = hwVersionUid;
    }

    public String getZdcName() {
        return zdcName;
    }

    public void setZdcName(String zdcName) {
        this.zdcName = zdcName;
    }

    public String getZdcVersion() {
        return zdcVersion;
    }

    public void setZdcVersion(String zdcVersion) {
        this.zdcVersion = zdcVersion;
    }

    public String getMinimalHw() {
        return minimalHw;
    }

    public void setMinimalHw(String minimalHw) {
        this.minimalHw = minimalHw;
    }

    public String getTemporaryVariable() {
        return temporaryVariable;
    }

    public void setTemporaryVariable(String temporaryVariable) {
        this.temporaryVariable = temporaryVariable;
    }

    public String getCreateByUid() {
        return createByUid;
    }

    public void setCreateByUid(String createByUid) {
        this.createByUid = createByUid;
    }

    public String getUpdateByUid() {
        return updateByUid;
    }

    public void setUpdateByUid(String updateByUid) {
        this.updateByUid = updateByUid;
    }
}
