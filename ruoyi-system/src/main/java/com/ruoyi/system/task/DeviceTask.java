package com.ruoyi.system.task;

//import com.ruoyi.system.service.DeviceListService;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.DeviceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

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
        try {
            deviceListService.quarzImportAllDTCReport();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
