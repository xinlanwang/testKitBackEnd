package com.ruoyi.system.domain.dto;

import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserInfoDTO {
    private Long userId;
    @ApiModelProperty
    private Long testGroupId;
    /** 用户账号 */
    @Excel(name = "登录名称")
    @ApiModelProperty
    private String userName;
    /** 用户昵称 */
    @Excel(name = "用户名称")
    @ApiModelProperty
    private String nickName;
    /** 用户邮箱 */
    @Excel(name = "用户邮箱")
    @ApiModelProperty
    private String email;
    /** 手机号码 */
    @Excel(name = "手机号码")
    @ApiModelProperty
    private String phonenumber;
    /** 用户性别 */
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    @ApiModelProperty
    private String sex;
    /** 密码 */
    private String password;
    /** 最后登录IP */
    @Excel(name = "最后登录IP", type = Excel.Type.EXPORT)
    private String loginIp;
    /** 角色组 */
    private Long roleId;
}
