package com.ruoyi.system.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.param.DesktopLoginParam;
import com.ruoyi.system.domain.param.DesktopRegisterParam;
import com.ruoyi.system.domain.param.DesktopSubmitParam;
import com.ruoyi.system.domain.param.DeviceListParam;
import com.ruoyi.system.service.DesktopService;
import com.ruoyi.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("用户管理接口")
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

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
