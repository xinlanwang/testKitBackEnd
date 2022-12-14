package com.ruoyi.system.domain.param;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@ApiModel(value = "DesktopSubmitParam",description = "桌面云端同步参数")
@Data
public class DesktopRegisterParam {
        private static final long serialVersionUID = 1L;
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
        private Long[] roleIds;

}
