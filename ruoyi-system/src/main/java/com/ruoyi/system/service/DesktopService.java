package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.dto.ImportDeviceDTO;
import com.ruoyi.system.domain.param.DesktopSubmitParam;
import com.ruoyi.system.domain.param.DeviceCompareParam;
import com.ruoyi.system.domain.param.DeviceListParam;
import com.ruoyi.system.domain.vo.DeviceInfoVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 字典 业务层
 * 
 * @author ruoyi
 */
public interface DesktopService
{

    public String submit(DesktopSubmitParam desktopSubmitParam);
}
