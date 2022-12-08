package com.ruoyi.system.service.impl;
import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.param.DesktopRecordParam;
import java.util.Date;
import java.util.Map;

import cn.hutool.core.util.XmlUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.reflect.ReflectUtils;
import com.ruoyi.system.domain.DeviceCompareVO;
import com.ruoyi.system.domain.dto.GoldenInfoComponentDTO;
import com.ruoyi.system.domain.dto.ImportDeviceDTO;
import com.ruoyi.system.domain.dto.TDTCReportDTO;
import com.ruoyi.system.domain.param.DesktopSubmitParam;
import com.ruoyi.system.domain.param.DeviceCompareParam;
import com.ruoyi.system.domain.param.DeviceListParam;
import com.ruoyi.system.domain.po.*;
import com.ruoyi.system.domain.vo.DeviceInfoComponent;
import com.ruoyi.system.domain.vo.DeviceInfoVo;
import com.ruoyi.system.domain.vo.DeviceListVo;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.DesktopService;
import com.ruoyi.system.service.DeviceListService;
import io.netty.util.internal.StringUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ruoyi.common.constant.TestKitConstants.DEVICE_TYPE_BENCH;
import static com.ruoyi.common.constant.TestKitConstants.DICT_STATUS_NORMAL;

/**
 * 字典 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class DesktopServiceImpl implements DesktopService {
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
    private TDesktopRecordMapper tDesktopRecordMapper;
    @Autowired
    private DeviceListService deviceListService;



    @Override
    public String submit(DesktopSubmitParam desktopSubmitParam) {
        List<DesktopRecordParam> desktopRecordParams = desktopSubmitParam.getDesktopRecordParams();
        if (desktopRecordParams == null || desktopRecordParams.size() == 0|| StringUtils.isEmpty(desktopSubmitParam.getLocalHostAcoount()) || StringUtils.isEmpty(desktopSubmitParam.getLocalHostPassword())){
            return "用户或者参数列表为空";
        }
        String localHostAcoount = desktopSubmitParam.getLocalHostAcoount();
        SysUser sysUser = sysUserMapper.selectUserByUserName(localHostAcoount);
        String localHostPassword = desktopSubmitParam.getLocalHostPassword();
        if (sysUser == null || StringUtils.isEmpty(localHostPassword)  || !sysUser.getPassword().equals(localHostPassword)){
            return "用户信息错误";
        }
        String LocalHostUserId = sysUser.getUserId().toString();
        Map<String,DesktopRecordParam> insertDesktopDevices = new HashMap<>();
        Map<String,TDesktopRecord> insertDesktopRecords = new HashMap<>();
        Map<String,TDesktopRecord> updateDesktopRecords = new HashMap<>();
        Map<String,DesktopRecordParam> updateDesktopDevices = new HashMap<>();
        List<Long> deleteDeviceInfoUids = new ArrayList<Long>();
        List<Long> deleteDesktopRecordUids = new ArrayList<Long>();
        for (DesktopRecordParam desktopRecordParam:desktopRecordParams){
                TDesktopRecord tDesktopRecord = new TDesktopRecord();
                buildDesktopRecord(tDesktopRecord,LocalHostUserId, desktopRecordParam);
                String indexUid = desktopRecordParam.getIndexUid();
                if (StringUtils.isEmpty(indexUid)){
                    continue;
                }
                if ("1".equals(tDesktopRecord.getOperationType())) {
                    insertDesktopDevices.put(indexUid,desktopRecordParam);
                    insertDesktopRecords.put(indexUid,tDesktopRecord);
                }else if ("2".equals(tDesktopRecord.getOperationType())){
                    updateDesktopDevices.put(indexUid,desktopRecordParam);
                    updateDesktopRecords.put(indexUid,tDesktopRecord);
                }else if ("3".equals(tDesktopRecord.getOperationType())){
                    deleteDesktopRecordUids.add(Long.valueOf(desktopRecordParam.getDesktopDeviceUid()));
                    deleteDeviceInfoUids.add(Long.valueOf(desktopRecordParam.getDesktopDevice().getCarlineInfoUid()));
                }
        }
        for (String indexUid:insertDesktopDevices.keySet()){
            DesktopRecordParam desktopRecordParam = insertDesktopDevices.get(indexUid);
            TDesktopRecord tDesktopRecord = insertDesktopRecords.get(indexUid);
            DeviceInfoVo desktopDevice = desktopRecordParam.getDesktopDevice();
            Long carlineInfoUid = deviceListService.insertDeviceInfo(desktopDevice);
            tDesktopRecord.setCarlineInfoUid(carlineInfoUid);
            tDesktopRecordMapper.insert(tDesktopRecord);
        }
        for (String indexUid:updateDesktopDevices.keySet()){
            DesktopRecordParam desktopRecordParam = updateDesktopDevices.get(indexUid);
            TDesktopRecord tDesktopRecord = updateDesktopRecords.get(indexUid);
            DeviceInfoVo desktopDevice = desktopRecordParam.getDesktopDevice();
            Long carlineInfoUid = deviceListService.updateDeviceInfo(desktopDevice);
            tDesktopRecord.setCarlineInfoUid(carlineInfoUid);
            tDesktopRecordMapper.updateById(tDesktopRecord);
        }
        deviceListService.deleteTCarlineByUids(deleteDeviceInfoUids.toArray(new Long[deleteDeviceInfoUids.size()]));
        deviceListService.deleteTCarlineByUids(deleteDesktopRecordUids.toArray(new Long[deleteDesktopRecordUids.size()]));

        return null;
    }

    private static void buildDesktopRecord(TDesktopRecord tDesktopRecord,String LocalHostUserId, DesktopRecordParam desktopRecordParam) {
        tDesktopRecord.setLocalhostUserId(LocalHostUserId);
        tDesktopRecord.setOperUserName(desktopRecordParam.getTester());
        tDesktopRecord.setOperLocation("there");
        tDesktopRecord.setStatus(1);
        tDesktopRecord.setOperIp("192.168.0.1");
        tDesktopRecord.setOperTime(desktopRecordParam.getTestDate());
        tDesktopRecord.setFunctionGroupType(desktopRecordParam.getFunctionGroupType());
        tDesktopRecord.setTaskType(desktopRecordParam.getTaskType());
        tDesktopRecord.setMileacge(desktopRecordParam.getMileacge());
        tDesktopRecord.setFallBackScreen(desktopRecordParam.getFallBackScreen());
        tDesktopRecord.setTestHour(desktopRecordParam.getTestHour());
        tDesktopRecord.setLocation(desktopRecordParam.getLocation());
        tDesktopRecord.setSystemReset(desktopRecordParam.getSystemReset());
        tDesktopRecord.setNaviReset(desktopRecordParam.getNaviReset());
        tDesktopRecord.setBlackMap(desktopRecordParam.getBlackMap());
        tDesktopRecord.setInitializingOccurred(desktopRecordParam.getInitializingOccurred());
        tDesktopRecord.setFunctionGroupType(desktopRecordParam.getFallBackScreen());
        tDesktopRecord.setBussleep(desktopRecordParam.getBussleep());
        tDesktopRecord.setPlannedTicket(desktopRecordParam.getPlannedTicket());
        tDesktopRecord.setComment(desktopRecordParam.getComment());
        tDesktopRecord.setOperationType(desktopRecordParam.getOperationType());
    }
}



















