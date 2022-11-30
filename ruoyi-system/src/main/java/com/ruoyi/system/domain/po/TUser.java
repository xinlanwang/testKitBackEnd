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
 * `用户`对象 t_user
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
@TableName
public class TUser  extends LocalBaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 唯一uid主键 */
    @TableId(type = IdType.AUTO)
    private String uid;

    /** 用户名 */
    @Excel(name = "用户名")
    private String userName;

    /** 密码 */
    @Excel(name = "密码")
    private String passWord;

    /** 昵称 */
    @Excel(name = "昵称")
    private String nickName;

    /** 性别(1:男2:女) */
    @Excel(name = "性别(1:男2:女)")
    private Integer gender;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 出生年月日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出生年月日", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birthday;

    /** 手机 */
    @Excel(name = "手机")
    private String mobile;

    /** 邮箱验证码 */
    @Excel(name = "邮箱验证码")
    private String validCode;

    /** 登录次数 */
    @Excel(name = "登录次数")
    private Long loginCount;

    /** 最后登录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastLoginTime;

    /** 最后登录IP */
    @Excel(name = "最后登录IP")
    private String lastLoginIp;

    /** 用户标签：0：普通用户1：管理员2：博主 */
    @Excel(name = "用户标签：0：普通用户1：管理员2：博主")
    private Integer userType;

    /** 是否通过加载校验【0未通过1通过） */
    @Excel(name = "是否通过加载校验【0未通过1通过）")
    private Integer loadingValid;

    /** 帐号状态（1正常0停用） */
    @Excel(name = "帐号状态", readConverterExp = "1=正常0停用")
    private String status;

    /** 删除标志（1代表存在0代表删除） */
    private String delFlag;

    /** 创建者 */
    @Excel(name = "创建者")
    private String createByUid;

    /** 更新者 */
    @Excel(name = "更新者")
    private String updateByUid;

    /** 职业 */
    @Excel(name = "职业")
    private String occupation;

    /** 拥有的角色uid */
    @Excel(name = "拥有的角色uid")
    private String roleUid;

    public void setUid(String uid) 
    {
        this.uid = uid;
    }

    public String getUid() 
    {
        return uid;
    }
    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }
    public void setPassWord(String passWord) 
    {
        this.passWord = passWord;
    }

    public String getPassWord() 
    {
        return passWord;
    }
    public void setNickName(String nickName) 
    {
        this.nickName = nickName;
    }

    public String getNickName() 
    {
        return nickName;
    }
    public void setGender(Integer gender) 
    {
        this.gender = gender;
    }

    public Integer getGender() 
    {
        return gender;
    }
    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }
    public void setBirthday(Date birthday) 
    {
        this.birthday = birthday;
    }

    public Date getBirthday() 
    {
        return birthday;
    }
    public void setMobile(String mobile) 
    {
        this.mobile = mobile;
    }

    public String getMobile() 
    {
        return mobile;
    }
    public void setValidCode(String validCode) 
    {
        this.validCode = validCode;
    }

    public String getValidCode() 
    {
        return validCode;
    }
    public void setLoginCount(Long loginCount) 
    {
        this.loginCount = loginCount;
    }

    public Long getLoginCount() 
    {
        return loginCount;
    }
    public void setLastLoginTime(Date lastLoginTime) 
    {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getLastLoginTime() 
    {
        return lastLoginTime;
    }
    public void setLastLoginIp(String lastLoginIp) 
    {
        this.lastLoginIp = lastLoginIp;
    }

    public String getLastLoginIp() 
    {
        return lastLoginIp;
    }
    public void setUserType(Integer userType) 
    {
        this.userType = userType;
    }

    public Integer getUserType() 
    {
        return userType;
    }
    public void setLoadingValid(Integer loadingValid) 
    {
        this.loadingValid = loadingValid;
    }

    public Integer getLoadingValid() 
    {
        return loadingValid;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
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
    public void setOccupation(String occupation) 
    {
        this.occupation = occupation;
    }

    public String getOccupation() 
    {
        return occupation;
    }
    public void setRoleUid(String roleUid) 
    {
        this.roleUid = roleUid;
    }

    public String getRoleUid() 
    {
        return roleUid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("uid", getUid())
            .append("userName", getUserName())
            .append("passWord", getPassWord())
            .append("nickName", getNickName())
            .append("gender", getGender())
            .append("email", getEmail())
            .append("birthday", getBirthday())
            .append("mobile", getMobile())
            .append("validCode", getValidCode())
            .append("loginCount", getLoginCount())
            .append("lastLoginTime", getLastLoginTime())
            .append("lastLoginIp", getLastLoginIp())
            .append("userType", getUserType())
            .append("loadingValid", getLoadingValid())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createByUid", getCreateByUid())
            .append("updateByUid", getUpdateByUid())
            .append("occupation", getOccupation())
            .append("roleUid", getRoleUid())
            .toString();
    }
}
