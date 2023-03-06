package com.ruoyi.system.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.dto.CheckDeviceIntegralityDTO;
import com.ruoyi.system.domain.dto.DashboardGetDeviceUseDTO;
import com.ruoyi.system.domain.param.*;
import com.ruoyi.system.domain.po.TCarlineInfo;
import com.ruoyi.system.domain.po.TDataLog;
import com.ruoyi.system.domain.po.TDesktopRecord;
import com.ruoyi.system.domain.vo.DeviceInfoVo;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.DashboardService;
import com.ruoyi.system.service.DesktopService;
import com.ruoyi.system.service.DeviceListService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;

import static com.ruoyi.common.constant.TestKitConstants.*;

/**
 * 字典 业务层处理
 *
 * @author ruoyi
 */
@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    private TCarlineMapper tCarlineMapper;
    @Autowired
    private TClusterMapper tClusterMapper;
    @Autowired
    private TCarlineInfoMapper tCarlineInfoMapper;
    @Autowired
    private TMatrixMapper tMatrixMapper;
    @Autowired
    private TComponentDataMapper tComponentDataMapper;
    @Autowired
    private TCarlineComponentMapper tCarlineComponentMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private TDesktopRecordMapper desktopRecordMapper;
    @Autowired
    private DeviceListService deviceListService;
    @Autowired
    private TDataLogMapper desktopLogMapper;

    private static final Logger log = LoggerFactory.getLogger(DashboardServiceImpl.class);


    @Override
    public List<Map> getDeviceUse(DashboardParam dashboardParam) {
        List<DashboardGetDeviceUseDTO> deviceUseDTOList = desktopRecordMapper.getDeviceUse(dashboardParam);
        if (CollectionUtils.isEmpty(deviceUseDTOList)){
            return null;
        }
        DashboardGetDeviceUseDTO[] dashboardGetDeviceUseDTOS = deviceUseDTOList.toArray(new DashboardGetDeviceUseDTO[deviceUseDTOList.size()]);
        //1.对全部时间进行排序
        dashboardGetDeviceUseDTOS = quickSort(dashboardGetDeviceUseDTOS,0, dashboardGetDeviceUseDTOS.length - 1);

        //2.用linkedMap各领各的device
        Map<String, List<DashboardGetDeviceUseDTO>> deviceLickedMap = new LinkedHashMap<>();
        for (DashboardGetDeviceUseDTO dashboardGetDeviceUseDTO:dashboardGetDeviceUseDTOS){
            List<DashboardGetDeviceUseDTO> lickedDeviceDTO;
            String deviceName = dashboardGetDeviceUseDTO.getDeviceName();
            if (deviceLickedMap.get(deviceName) == null){
                lickedDeviceDTO = new ArrayList<>();
            }else {
                lickedDeviceDTO = deviceLickedMap.get(deviceName);
            }
            lickedDeviceDTO.add(dashboardGetDeviceUseDTO);
            deviceLickedMap.put(deviceName,lickedDeviceDTO);
        }

        //3.合并这个Map的star与end为新的list并输出给前端
        List<Map> resultMapList = new ArrayList<>();
        Integer index = 0;
        for (String deviceName : deviceLickedMap.keySet()){
            List<DashboardGetDeviceUseDTO> thisDeviceDTOList = deviceLickedMap.get(deviceName);
            Boolean isEnd = false;
            index++;
            for (int i = 0;i < thisDeviceDTOList.size();i++){
                Date curDate = thisDeviceDTOList.get(i).getOperTime();
                //3.1 第一天为startDate，毋庸置疑的
                if (i == 0){
                    Map resultMap = new HashMap<>();
                    resultMap.put("startTime",curDate);
                    resultMapList.add(resultMap);
                }
                //3.2.1 如果当前天的后继不为次天，则end为当前天
                if (i < thisDeviceDTOList.size() - 1) {
                    Date afterDate = thisDeviceDTOList.get(i + 1).getOperTime();
                    if (!DateUtils.addDays(curDate, 1).equals(afterDate)) {
                        isEnd = true;
                    }
                }
                //3.2.2 如果当前结点不存在后继，则end为当前
                if (i == thisDeviceDTOList.size() - 1){
                    isEnd = true;
                }

                //3.3 仅有结束结点才存在endDate || 单结点
                if (isEnd ||  (thisDeviceDTOList.size() == 1 && i == 0)){
                    Map resultMap = resultMapList.get(resultMapList.size() - 1);
                    resultMap.put("endTime",curDate);
                    resultMap.put("index",index);
                    resultMap.put("deviceName",deviceName);
                    //webfront start
                    List value = new ArrayList<>();
                    value.add(index);
                    value.add(DateUtil.format((Date) resultMap.get("startTime"),"yyyy-MM-dd"));
                    value.add(DateUtil.format((Date) resultMap.get("endTime"),"yyyy-MM-dd"));
                    resultMap.put("value",value);
                    //webfront end
                    if (i < thisDeviceDTOList.size() - 1){
                        Map nextResultMap = new HashMap<>();
                        nextResultMap.put("startTime",thisDeviceDTOList.get(i + 1).getOperTime());
                        resultMapList.add(nextResultMap);
                        isEnd = false;
                    }
                }
            }
        }


        return resultMapList;
    }
    public DashboardGetDeviceUseDTO[] quickSort(DashboardGetDeviceUseDTO[] arr,int low,int high){
        int i,j;
        DashboardGetDeviceUseDTO t,temp;
        if(low>high){
            return arr;
        }
        i=low;
        j=high;
        //temp就是基准位
        temp = arr[low];

        while (i<j) {
            while (DateUtil.compare(temp.getOperTime(),arr[j].getOperTime()) <= 0&&i<j) {
                j--;
            }
            while (DateUtil.compare(temp.getOperTime(),arr[i].getOperTime()) >= 0&&i<j) {
                i++;
            }
            if (i<j) {
                t = arr[j];
                arr[j] = arr[i];
                arr[i] = t;
            }

        }
        arr[low] = arr[i];
        arr[i] = temp;
        quickSort(arr, low, j-1);
        quickSort(arr, j+1, high);
        return arr;
    }


    @Test
    public  void main1(){
        Date mydate1 = new Date(2018,1,1);
        Date mydate2 = new Date(2018,1,3);
        System.out.println(mydate1);
        System.out.println(DateUtils.addDays(mydate1,1));
        System.out.println(DateUtils.addDays(mydate1,1).equals(mydate2));
        Date mydate3 = new Date(2066,5,3);
        Date mydate4 = new Date(2023,5,3);
        Date mydate5 = new Date(2023,7,13);
        Date mydate6 = new Date(2008,5,3);
        DashboardGetDeviceUseDTO date1 = new DashboardGetDeviceUseDTO();
        DashboardGetDeviceUseDTO date2 = new DashboardGetDeviceUseDTO();
        DashboardGetDeviceUseDTO date3 = new DashboardGetDeviceUseDTO();
        DashboardGetDeviceUseDTO date4 = new DashboardGetDeviceUseDTO();
        DashboardGetDeviceUseDTO date5 = new DashboardGetDeviceUseDTO();
        DashboardGetDeviceUseDTO date6 = new DashboardGetDeviceUseDTO();
        date1.setOperTime(mydate1);
        date2.setOperTime(mydate2);
        date3.setOperTime(mydate3);
        date4.setOperTime(mydate4);
        date5.setOperTime(mydate5);
        date6.setOperTime(mydate6);
//        System.out.println(DateUtil.compare(date1,date2));
        DashboardGetDeviceUseDTO[] arr = {date3,date4,date1,date2,date5,date6};
        quickSort(arr, 0, arr.length-1);
        for (int i = 0; i < arr.length; i++) {
//            System.out.println(arr[i]);
        }
    }
}



















