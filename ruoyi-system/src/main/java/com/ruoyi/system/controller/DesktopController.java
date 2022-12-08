package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.dto.ImportDeviceDTO;
import com.ruoyi.system.domain.param.DesktopDeviceParam;
import com.ruoyi.system.domain.param.DesktopSubmitParam;
import com.ruoyi.system.domain.param.DeviceCompareParam;
import com.ruoyi.system.domain.param.DeviceListParam;
import com.ruoyi.system.domain.po.TDesktopLog;
import com.ruoyi.system.domain.vo.DeviceInfoVo;
import com.ruoyi.system.service.DesktopService;
import com.ruoyi.system.service.DeviceListService;
import com.ruoyi.system.service.impl.DeviceExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
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
