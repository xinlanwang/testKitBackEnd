package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.param.DesktopLoginParam;
import com.ruoyi.system.domain.param.DesktopRegisterParam;
import com.ruoyi.system.domain.vo.UserInfoVO;

import java.util.List;

/**
 * 字典 业务层
 * 
 * @author ruoyi
 */
public interface UserService
{



    public AjaxResult getRoles(DesktopLoginParam desktopLoginParam);

    public AjaxResult register(DesktopRegisterParam desktopRegisterParam);

    public List queryUserList();

    public Long updateUser(DesktopRegisterParam desktopRegisterParam);

    public int deleteUsersByUserIds(Long[] uids);

    public UserInfoVO selectByUserId(Long userId);
}
