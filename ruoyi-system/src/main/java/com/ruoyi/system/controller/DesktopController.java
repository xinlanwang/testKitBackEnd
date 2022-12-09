package com.ruoyi.system.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.param.DesktopSubmitParam;
import com.ruoyi.system.service.DesktopService;
import com.ruoyi.system.service.DeviceListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * `devicelist`Controller
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@Api("桌面端接口")
@RestController
@RequestMapping("/desktop")
public class DesktopController extends BaseController
{
    @Autowired
    private DeviceListService deviceListService;
    @Autowired
    private DesktopService desktopService;

    @PostMapping("/submit")
    @ApiOperation("提交")
    public AjaxResult submit( @RequestBody DesktopSubmitParam desktopSubmitParam) throws Exception {
        String message = desktopService.submit(desktopSubmitParam);
        return success(message);
    }






}
