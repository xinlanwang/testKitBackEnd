package com.ruoyi.system.task;

//import com.ruoyi.system.service.DeviceListService;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.DeviceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.ruoyi.common.constant.TestKitConstants.REFRESH_WAY_AUTO;
import static com.ruoyi.common.constant.TestKitConstants.REFRESH_WAY_MANUAL;

/**
 * 定时任务调度测试
 * 
 * @author ruoyi
 */
@Component("deviceTask")
public class DeviceTask
{
    @Autowired
    private DeviceListService deviceListService;



    public void test(){
        System.out.println("21111111111");
    }
    /**
     * 定时从idexReport刷新device数据
     */
    public void quarzImportAllDTCReport()
    {
        AjaxResult ajaxResult = deviceListService.quarzImportAllDTCReport(REFRESH_WAY_AUTO);
    }


}
