package com.ruoyi.system.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.dto.CheckDeviceIntegralityDTO;
import com.ruoyi.system.domain.param.DashboardParam;
import com.ruoyi.system.domain.param.DesktopGetDBParam;
import com.ruoyi.system.domain.param.DesktopLoginParam;
import com.ruoyi.system.domain.param.DesktopSubmitParam;
import com.ruoyi.system.service.DashboardService;
import com.ruoyi.system.service.DesktopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * `devicelist`Controller
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@Api("桌面端接口")
@RestController
@RequestMapping("/dashboard")
public class DashboardController extends BaseController
{
    @Autowired
    private DashboardService dashboardService;

    /**
     * @return
     * @throws Exception
     */
    @PostMapping("/getDeviceUse")
    @ApiOperation("获取台架车辆使用概况")
    public AjaxResult getDeviceUse(@RequestBody DashboardParam dashboardParam) {
        return AjaxResult.success(dashboardService.getDeviceUse(dashboardParam));
    }

    @PostMapping("/deviceUsageStatistics")
    @ApiOperation("台架/车辆使用时间统计")
    public AjaxResult deviceUsageStatistics(@RequestBody DashboardParam dashboardParam) {
        return AjaxResult.success(dashboardService.deviceUsageStatistics(dashboardParam));
    }

    @PostMapping("/deviceUsageTrend")
    @ApiOperation("台架/车辆使用趋势")
    public AjaxResult deviceUsageTrend(@RequestBody DashboardParam dashboardParam) {
        return AjaxResult.success(dashboardService.deviceUsageTrend(dashboardParam));
    }

    @PostMapping("/deviceUsageByGroup")
    @ApiOperation("台架/车辆使用趋势")
    public AjaxResult staticRecordGroupByFunctionGroup(@RequestBody DashboardParam dashboardParam) {
        return AjaxResult.success(dashboardService.staticRecordGroupByFunctionGroup(dashboardParam));
    }

    @PostMapping("/deviceStatisticTicket")
    @ApiOperation("台架/车辆使用趋势")
    public AjaxResult deviceStatisticTicket(@RequestBody DashboardParam dashboardParam) {
        return AjaxResult.success(dashboardService.deviceStatisticTicket(dashboardParam));
    }

}
