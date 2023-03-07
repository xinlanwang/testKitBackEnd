package com.ruoyi.system.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.LocalBaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * `车型基本数据`对象 t_carline
 * 
 * @author xinlan.wang
 * @date 2023-03-07
 */


@TableName()
@Data
public class TRefreshLog
{
    private static final long serialVersionUID = 1L;

    /** 唯一uid */
    @TableId(type = IdType.AUTO)
    private Long uid;
    /** 汽车名称 */
    @Excel(name = "汽车名称")
    private String deviceName;
    @Excel(name = "更新时间")
    /** 更新时间  */
    private Date updateTime;
    /** 当前车辆状态  */
    @Excel(name = "更新状态")
    private String updateStatus;
    /** 更新者  */
    private String updateUser;
    /** 刷新文件名称 */
    @Excel(name = "刷新文件名称")
    private String refreshFileName;
    /** 失败原因 */
    @Excel(name = "失败原因")
    private String failLog;
    /** 刷新方式 */
    @Excel(name = "刷新方式")
    private String refreshWay;

}
