package com.ruoyi.system.service;

import com.ruoyi.system.domain.param.DesktopSubmitParam;
import com.ruoyi.system.domain.param.DeviceListParam;
import com.ruoyi.system.domain.param.RecordListParam;

import java.util.List;

/**
 * 字典 业务层
 * 
 * @author ruoyi
 */
public interface RecordService
{


    public List list(RecordListParam deviceListParam);

    public List historyList(Long recordUid);
}
