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
 * `版本管控`对象 t_cluster
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@TableName
public class TCluster extends LocalBaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 唯一uid */
    @TableId(type = IdType.AUTO)
    private Long uid;

    /** 车基础信息id */
    @Excel(name = "车基础信息id")
    private Long carlineUid;

    /** 工程id */
    @Excel(name = "工程id")
    private String projectType;

    /** 1-10的循环 */
    @Excel(name = "1-10的循环")
    private Integer carNum;

    /** 更新点 */
    @Excel(name = "更新点")
    private String clusterUpdatePoint;

    /** 更新者主键 */
    @Excel(name = "更新者主键")
    private String clusterUpdateByUid;

    /** bc代表bench，car代表car，gd代表目标goldeninfo */
    @Excel(name = "bc代表bench，car代表car，gd代表目标goldeninfo")
    private String deviceType;

    /** 版本名称 */
    @Excel(name = "版本名称")
    private String clusterName;

    /** 最近更新 */
    @Excel(name = "最近更新")
    private String lastUpdated;

    /** 当前版本状态  */
    @Excel(name = "当前版本状态 ")
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

    public TCluster() {
    }

    public TCluster(Long uid, Long carlineUid, String projectType, Integer carNum, String clusterUpdatePoint, String clusterUpdateByUid, String deviceType, String clusterName, String lastUpdated, Integer status, String createByUid, String updateByUid, Long sort, Integer isShow) {
        this.uid = uid;
        this.carlineUid = carlineUid;
        this.projectType = projectType;
        this.carNum = carNum;
        this.clusterUpdatePoint = clusterUpdatePoint;
        this.clusterUpdateByUid = clusterUpdateByUid;
        this.deviceType = deviceType;
        this.clusterName = clusterName;
        this.lastUpdated = lastUpdated;
        this.status = status;
        this.createByUid = createByUid;
        this.updateByUid = updateByUid;
        this.sort = sort;
        this.isShow = isShow;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getCarlineUid() {
        return carlineUid;
    }

    public void setCarlineUid(Long carlineUid) {
        this.carlineUid = carlineUid;
    }

    public String getprojectType() {
        return projectType;
    }

    public void setprojectType(String projectType) {
        this.projectType = projectType;
    }

    public Integer getCarNum() {
        return carNum;
    }

    public void setCarNum(Integer carNum) {
        this.carNum = carNum;
    }

    public String getClusterUpdatePoint() {
        return clusterUpdatePoint;
    }

    public void setClusterUpdatePoint(String clusterUpdatePoint) {
        this.clusterUpdatePoint = clusterUpdatePoint;
    }

    public String getClusterUpdateByUid() {
        return clusterUpdateByUid;
    }

    public void setClusterUpdateByUid(String clusterUpdateByUid) {
        this.clusterUpdateByUid = clusterUpdateByUid;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }
}
