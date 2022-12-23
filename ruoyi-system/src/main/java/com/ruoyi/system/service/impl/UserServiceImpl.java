package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.domain.dto.UserInfoDTO;
import com.ruoyi.system.domain.param.*;
import com.ruoyi.system.domain.po.TCarlineInfo;
import com.ruoyi.system.domain.po.TDataLog;
import com.ruoyi.system.domain.po.TDesktopRecord;
import com.ruoyi.system.domain.vo.DeviceInfoVo;
import com.ruoyi.system.domain.vo.UserInfoVO;
import com.ruoyi.system.domain.vo.UserListVO;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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

    @Override
    public List queryUserList() {
        return userMapper.selectWebUserList();
    }

    @Override
    public Long updateUser(DesktopRegisterParam desktopRegisterParam) {
        if (desktopRegisterParam == null || desktopRegisterParam.getUserId() == null){
            return -1L;
        }
        SysUser user = new SysUser();
        desktopRegisterParam.setPassword(SecurityUtils.encryptPassword(desktopRegisterParam.getPassword()));
        buildRegisterUser(desktopRegisterParam, user);
        user.setUserId(desktopRegisterParam.getUserId());
        userMapper.updateUser(user);
        userRoleMapper.deleteUserRoleByUserId(desktopRegisterParam.getUserId());
        if (StringUtils.isNotEmpty(desktopRegisterParam.getRoleIds())){
            for (Long roleId : desktopRegisterParam.getRoleIds()) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(desktopRegisterParam.getUserId());
                sysUserRole.setRoleId(roleId);
                userRoleMapper.insert(sysUserRole);
            }
        }
        return 1L;
    }

    @Override
    public int deleteUsersByUserIds(Long[] uids) {
        if (StringUtils.isEmpty(uids)){
            return -1;
        }
        for (Long uid : uids){
            userRoleMapper.deleteUserRoleByUserId(uid);
           userMapper.deleteById(uid);
        }
        return 1;
    }

    @Override
    public UserInfoVO selectByUserId(Long userId) {
        List<UserInfoDTO> userListVOS = userMapper.selectUserInfoByUserId(userId);
        if (StringUtils.isEmpty(userListVOS)){
            return null;
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        List<Long> roleIds = new ArrayList<>();
        for (int i = 0;i < userListVOS.size();i++){
            if (i == 0){
                BeanUtils.copyProperties(userListVOS.get(0),userInfoVO);
            }
            roleIds.add(userListVOS.get(i).getRoleId());
        }
        userInfoVO.setRoleIds(roleIds.toArray(new Long[roleIds.size()]));
        return userInfoVO;
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
        user.setCreateTime(new Date());
    }
}



















