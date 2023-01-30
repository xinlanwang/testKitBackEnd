package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.dto.CheckDeviceIntegralityDTO;
import com.ruoyi.system.domain.param.DesktopGetDBParam;
import com.ruoyi.system.domain.param.DesktopLoginParam;
import com.ruoyi.system.domain.param.DesktopSubmitParam;

import java.util.Map;

/**
 * 字典 业务层
 * 
 * @author ruoyi
 */
public interface DesktopService
{


    public AjaxResult submit(DesktopSubmitParam desktopSubmitParam);

    public AjaxResult login(DesktopLoginParam desktopLoginParam);

    public Object getdb(DesktopGetDBParam desktopGetDBParam);

    public CheckDeviceIntegralityDTO checkDeviceIntegrality(DesktopSubmitParam desktopSubmitParam);
}
