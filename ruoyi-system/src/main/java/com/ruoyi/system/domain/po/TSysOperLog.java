package com.ruoyi.system.domain.po;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.LocalBaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * `操作日志记录`对象 t_sys_oper_log
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
@TableName
public class TSysOperLog extends LocalBaseEntity
{
    private static final long serialVersionUID = 1L;

    /** auto_increment */
    @TableId(type = IdType.AUTO)
    private Long operId;

    /** 模块标题 */
    @Excel(name = "模块标题")
    private String title;

    /** 方法名称 */
    @Excel(name = "方法名称")
    private String method;

    /** 请求方式 */
    @Excel(name = "请求方式")
    private String requestMethod;

    /** 操作人员id */
    @Excel(name = "操作人员id")
    private String operUserId;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date operTime;

    /** 请求URL */
    @Excel(name = "请求URL")
    private String operUrl;

    /** 主机地址 */
    @Excel(name = "主机地址")
    private String operIp;

    /** 操作地点 */
    @Excel(name = "操作地点")
    private String operLocation;

    /** 请求参数 */
    @Excel(name = "请求参数")
    private String operParam;

    /** 返回参数 */
    @Excel(name = "返回参数")
    private String jsonResult;

    /** 操作状态 */
    @Excel(name = "操作状态")
    private Long status;

    /** 错误消息 */
    @Excel(name = "错误消息")
    private String errorMsg;

    public void setOperId(Long operId) 
    {
        this.operId = operId;
    }

    public Long getOperId() 
    {
        return operId;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setMethod(String method) 
    {
        this.method = method;
    }

    public String getMethod() 
    {
        return method;
    }
    public void setRequestMethod(String requestMethod) 
    {
        this.requestMethod = requestMethod;
    }

    public String getRequestMethod() 
    {
        return requestMethod;
    }
    public void setOperUserId(String operUserId) 
    {
        this.operUserId = operUserId;
    }

    public String getOperUserId() 
    {
        return operUserId;
    }
    public void setDeptName(String deptName) 
    {
        this.deptName = deptName;
    }

    public String getDeptName() 
    {
        return deptName;
    }
    public void setOperTime(Date operTime) 
    {
        this.operTime = operTime;
    }

    public Date getOperTime() 
    {
        return operTime;
    }
    public void setOperUrl(String operUrl) 
    {
        this.operUrl = operUrl;
    }

    public String getOperUrl() 
    {
        return operUrl;
    }
    public void setOperIp(String operIp) 
    {
        this.operIp = operIp;
    }

    public String getOperIp() 
    {
        return operIp;
    }
    public void setOperLocation(String operLocation) 
    {
        this.operLocation = operLocation;
    }

    public String getOperLocation() 
    {
        return operLocation;
    }
    public void setOperParam(String operParam) 
    {
        this.operParam = operParam;
    }

    public String getOperParam() 
    {
        return operParam;
    }
    public void setJsonResult(String jsonResult) 
    {
        this.jsonResult = jsonResult;
    }

    public String getJsonResult() 
    {
        return jsonResult;
    }
    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }
    public void setErrorMsg(String errorMsg) 
    {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() 
    {
        return errorMsg;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("operId", getOperId())
            .append("title", getTitle())
            .append("method", getMethod())
            .append("requestMethod", getRequestMethod())
            .append("operUserId", getOperUserId())
            .append("deptName", getDeptName())
            .append("operTime", getOperTime())
            .append("operUrl", getOperUrl())
            .append("operIp", getOperIp())
            .append("operLocation", getOperLocation())
            .append("operParam", getOperParam())
            .append("jsonResult", getJsonResult())
            .append("status", getStatus())
            .append("errorMsg", getErrorMsg())
            .toString();
    }
}
