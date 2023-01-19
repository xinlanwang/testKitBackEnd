package com.ruoyi.system.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.param.DesktopLoginParam;
import com.ruoyi.system.domain.param.DesktopRegisterParam;
import com.ruoyi.system.domain.param.DesktopSubmitParam;
import com.ruoyi.system.domain.param.DeviceListParam;
import com.ruoyi.system.domain.vo.DeviceInfoVo;
import com.ruoyi.system.domain.vo.UserInfoVO;
import com.ruoyi.system.service.DesktopService;
import com.ruoyi.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("用户管理接口")
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    @GetMapping("/cntest")
    @ApiOperation("提交")
    public AjaxResult voidTest() throws Exception {
        return AjaxResult.success("这是一句中文测试");
    }
    @PostMapping("/getRoles")
    @ApiOperation("提交")
    public AjaxResult getRoles(@Validated @RequestBody DesktopLoginParam desktopLoginParam) throws Exception {
        return userService.getRoles(desktopLoginParam);
    }

    @PostMapping("/register")
    @ApiOperation("提交")
    public AjaxResult register(@Validated @RequestBody DesktopRegisterParam desktopRegisterParam) throws Exception {
        return userService.register(desktopRegisterParam);
    }


    @PutMapping("/update")
    @ApiOperation("修改车型详细信息")
    public R<String> update(@RequestBody DesktopRegisterParam desktopRegisterParam){
        Long i = userService.updateUser(desktopRegisterParam);
        if (i <= 0){
            return R.fail("User doesn't exist");
        }
        return R.ok("Operation successful");
    }

    /**
     * 删除`BIGINT(32)`
     */
    @DeleteMapping("/delete/{uids}")
    @ApiOperation("删除用户列表")
    public AjaxResult removeMatrix(@PathVariable Long[] uids)
    {
        return toAjax(userService.deleteUsersByUserIds(uids));
    }

    /**
     * 根据用户Id获取用户详细信息
     */
    @GetMapping(value = "/info/{userId}")
    public AjaxResult getInfo(@PathVariable Long userId)
    {
        UserInfoVO userInfoVO = userService.selectByUserId(userId);
        if (userInfoVO == null){
            return AjaxResult.error("该用户不存在");
        }
        return success(userInfoVO);
    }

    /**
     * 查询`用户基本数据`列表
     */
    @ApiOperation("用户基本数据")
//    @PreAuthorize("@ss.hasPermi('device:devicelist:list')")
    @PostMapping("/queryList")
    public TableDataInfo list() {
        startPage();
        List list = userService.queryUserList();
        return getDataTable(list);
    }
}
