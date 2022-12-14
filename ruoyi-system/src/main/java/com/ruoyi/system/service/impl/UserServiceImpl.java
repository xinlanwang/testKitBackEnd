package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.domain.param.*;
import com.ruoyi.system.domain.po.TCarlineInfo;
import com.ruoyi.system.domain.po.TDataLog;
import com.ruoyi.system.domain.po.TDesktopRecord;
import com.ruoyi.system.domain.vo.DeviceInfoVo;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.token.TokenService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ruoyi.common.constant.TestKitConstants.*;

/**
 * 字典 业务层处理
 *
 * @author ruoyi
 */
@Service
public class UserServiceImpl implements UserService {

//    @Autowired
//    private TokenService tokenService;
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysRoleMapper roleMapper;
    @Autowired
    private SysUserRoleMapper userRoleMapper;
    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    //com.ruoyi.framework.web.service.SysLoginService
    @Override
    public AjaxResult getRoles(DesktopLoginParam desktopLoginParam) {

        if (desktopLoginParam == null || StringUtils.isEmpty(desktopLoginParam.getLocalHostAcoount()) || StringUtils.isEmpty(desktopLoginParam.getLocalHostPassword())) {
            return AjaxResult.error("用户或者参数列表为空");
        }
        String localHostAcoount = desktopLoginParam.getLocalHostAcoount();
        String localHostPassword = desktopLoginParam.getLocalHostPassword();
        SysUser sysUser = userMapper.selectUserByUserName(localHostAcoount);
        if (sysUser == null || StringUtils.isEmpty(localHostPassword) || !SecurityUtils.matchesPassword(localHostPassword, sysUser.getPassword())) {
            return AjaxResult.error("用户信息错误");
        }
        List<Long> roles = new ArrayList<>();
        for (SysUserRole sysUserRole : userRoleMapper.selectList(new QueryWrapper<SysUserRole>().eq("user_id", sysUser.getUserId()))) {
            roles.add(sysUserRole.getRoleId());
        }

        return AjaxResult.success(roles);
    }



    @Override
    public AjaxResult register(DesktopRegisterParam desktopRegisterParam) {
        if (userMapper.selectUserByUserName(desktopRegisterParam.getUserName()) != null)
        {
            return AjaxResult.error("新增用户'" + desktopRegisterParam.getUserName() + "'失败，登录账号已存在");
        }
        desktopRegisterParam.setPassword(SecurityUtils.encryptPassword(desktopRegisterParam.getPassword()));
        SysUser user = new SysUser();
        buildRegisterUser(desktopRegisterParam, user);
        userMapper.insert(user);
        Long[] roleIds = desktopRegisterParam.getRoleIds();
        if (StringUtils.isNotEmpty(roleIds))
        {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>(roleIds.length);
            for (Long roleId : roleIds)
            {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getUserId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            userRoleMapper.batchUserRole(list);
        }
        return AjaxResult.success(user.getUserId());
    }

    private static void buildRegisterUser(DesktopRegisterParam desktopRegisterParam, SysUser user) {
        user.setTestGroupId(desktopRegisterParam.getTestGroupId());
        user.setUserName(desktopRegisterParam.getUserName());
        user.setNickName(desktopRegisterParam.getNickName());
        user.setEmail(desktopRegisterParam.getEmail());
        user.setPhonenumber(desktopRegisterParam.getPhonenumber());
        user.setSex(desktopRegisterParam.getSex());
        user.setPassword(desktopRegisterParam.getPassword());
        user.setStatus("0");
        user.setLoginIp(desktopRegisterParam.getLoginIp());
    }
}



















