package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.dto.CheckDeviceIntegralityDTO;
import com.ruoyi.system.domain.param.DashboardParam;
import com.ruoyi.system.domain.param.DesktopGetDBParam;
import com.ruoyi.system.domain.param.DesktopLoginParam;
import com.ruoyi.system.domain.param.DesktopSubmitParam;

import java.util.List;
import java.util.Map;

/**
 * 字典 业务层
 * 
 * @author ruoyi
 */
public interface DashboardService
{

    public List<Map> getDeviceUse(DashboardParam dashboardParam);

    public List deviceUsageStatistics(DashboardParam dashboardParam);

    public Map<String, List>  deviceUsageTrend(DashboardParam dashboardParam);
    public List staticRecordGroupByFunctionGroup(DashboardParam dashboardParam);

    public Map<String, List> deviceStatisticTicket(DashboardParam dashboardParam);
}
