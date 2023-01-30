package com.ruoyi.system.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.dto.CheckDeviceIntegralityDTO;
import com.ruoyi.system.domain.param.DesktopGetDBParam;
import com.ruoyi.system.domain.param.DesktopLoginParam;
import com.ruoyi.system.domain.param.DesktopSubmitParam;
import com.ruoyi.system.service.DesktopService;
import com.ruoyi.system.service.DeviceListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

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
    private DesktopService desktopService;

    /**
     * TODO:为桌面端同步db故有此接口,绕过ORM框架使用JDBC直接访问数据库获取，但这并不是安全的处理方式，以后要想办法优化
     * @return
     * @throws Exception
     */
    @PostMapping("/getdb")
    @ApiOperation("提交")
    public AjaxResult getdb(@RequestBody DesktopGetDBParam desktopGetDBParam) {
        return AjaxResult.success(desktopService.getdb(desktopGetDBParam));
    }

    @PostMapping("/submit")
    @ApiOperation("提交")
    public AjaxResult submit(@Validated @RequestBody DesktopSubmitParam desktopSubmitParam) throws Exception {
        CheckDeviceIntegralityDTO checkDeviceIntegralityDTO = desktopService.checkDeviceIntegrality(desktopSubmitParam);
        if (checkDeviceIntegralityDTO.getIsIntegrated()){
            return desktopService.submit(desktopSubmitParam);
        }else {
            return AjaxResult.error(checkDeviceIntegralityDTO.getMessage());
        }
    }


    @PostMapping("/login")
    @ApiOperation("提交")
    public AjaxResult login(@Validated @RequestBody DesktopLoginParam desktopLoginParam) throws Exception {
        return desktopService.login(desktopLoginParam);
    }



}
