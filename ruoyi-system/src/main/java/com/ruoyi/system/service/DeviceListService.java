package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.AutoSaveVersionVO;
import com.ruoyi.system.domain.dto.ImportDeviceDTO;
import com.ruoyi.system.domain.param.DeviceCompareParam;
import com.ruoyi.system.domain.param.DeviceListParam;
import com.ruoyi.system.domain.vo.DeviceInfoVo;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 字典 业务层
 * 
 * @author ruoyi
 */
public interface DeviceListService
{

    public List queryDeviceList(DeviceListParam deviceListParam);

    public AjaxResult compareOneComponent(DeviceCompareParam deviceCompareParam);

    public Long insertDeviceInfo(DeviceInfoVo deviceInfoVo);

    public Long updateDeviceInfo(DeviceInfoVo deviceInfoVo);

    public int deleteTCarlineByUids(Long[] carlineInfoUids);

    public DeviceInfoVo getInfo(Long carlineInfoUid);

    public String importDevice(Map<String,List<ImportDeviceDTO>> deviceInfoVoList, boolean b, String operName);

    public AjaxResult importDTCReport(InputStream io, boolean b, String operName);
    public void quarzImportDTCReport() throws ClassNotFoundException;

    public List<AutoSaveVersionVO> autoSaveVersionList(String carlineInfoUid);
}
