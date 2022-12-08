package com.ruoyi.system.service.impl;

import java.util.List;

import cn.hutool.core.date.DateUtil;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.param.*;

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

import static com.ruoyi.common.constant.TestKitConstants.*;

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
    @Autowired
    private TDesktopLogMapper desktopLogMapper;


    @Override
    public String submit(DesktopSubmitParam desktopSubmitParam) {
        List<DesktopRecordParam> desktopRecordParams = desktopSubmitParam.getDesktopRecordParams();
        List<TDesktopLog> allDesktopLogs = new ArrayList<>();
        if (desktopRecordParams == null || desktopRecordParams.size() == 0 || StringUtils.isEmpty(desktopSubmitParam.getLocalHostAcoount()) || StringUtils.isEmpty(desktopSubmitParam.getLocalHostPassword())) {
            return "用户或者参数列表为空";
        }
        String localHostAcoount = desktopSubmitParam.getLocalHostAcoount();
        SysUser sysUser = sysUserMapper.selectUserByUserName(localHostAcoount);
        String localHostPassword = desktopSubmitParam.getLocalHostPassword();
        if (sysUser == null || StringUtils.isEmpty(localHostPassword) || !sysUser.getPassword().equals(localHostPassword)) {
            return "用户信息错误";
        }
        String LocalHostUserId = sysUser.getUserId().toString();
        Map<String, TDesktopRecord> insertDesktopRecords = new HashMap<>();
        Map<String, TDesktopRecord> updateDesktopRecords = new HashMap<>();
        Map<String, DesktopRecordParam> desktopRecordParamHashMap = new HashMap<>();
        List<Long> deleteDesktopRecordUids = new ArrayList<Long>();
        for (DesktopRecordParam desktopRecordParam : desktopRecordParams) {
            TDesktopRecord tDesktopRecord = new TDesktopRecord();
            buildDesktopRecord(tDesktopRecord, LocalHostUserId, desktopRecordParam);
            String indexUid = desktopRecordParam.getIndexUid();
            desktopRecordParamHashMap.put(indexUid,desktopRecordParam);
            DeviceInfoVo desktopDevice = desktopRecordParam.getDesktopDevice();
            if (StringUtils.isEmpty(indexUid) || desktopDevice == null || desktopDevice.getCarlineInfoUid() == null) {
                continue;
            }
            if (OPERATION_TYPE_INSERT.equals(tDesktopRecord.getOperationType())) {
                autoInstertDesktopDevice(tDesktopRecord, desktopDevice);
                insertDesktopRecords.put(indexUid, tDesktopRecord);
            } else if (OPERATION_TYPE_UPDATE.equals(tDesktopRecord.getOperationType())) {
                autoInstertDesktopDevice(tDesktopRecord, desktopDevice);
                updateDesktopRecords.put(indexUid, tDesktopRecord);
            } else if (OPERATION_TYPE_DELETE.equals(tDesktopRecord.getOperationType())) {
                deleteDesktopRecordUids.add(Long.valueOf(desktopRecordParam.getRecordUid()));
            }
        }
        for (String indexUid : insertDesktopRecords.keySet()) {
            TDesktopRecord tDesktopRecord = insertDesktopRecords.get(indexUid);
            tDesktopRecordMapper.insert(tDesktopRecord);
            DesktopRecordParam desktopRecordParam = desktopRecordParamHashMap.get(indexUid);
            buildOperationLog(allDesktopLogs, LocalHostUserId, desktopRecordParam, tDesktopRecord, OPERATION_TYPE_INSERT);
        }
        for (String indexUid : updateDesktopRecords.keySet()) {
            TDesktopRecord tDesktopRecord = updateDesktopRecords.get(indexUid);
            tDesktopRecordMapper.updateById(tDesktopRecord);
            DesktopRecordParam desktopRecordParam = desktopRecordParamHashMap.get(indexUid);
            buildOperationLog(allDesktopLogs, LocalHostUserId, desktopRecordParam, tDesktopRecord, OPERATION_TYPE_UPDATE);
        }
        for (TDesktopLog tDesktopLog:allDesktopLogs){
            desktopLogMapper.insert(tDesktopLog);
        }
        tDesktopRecordMapper.deleteTDesktopRecordByUids(deleteDesktopRecordUids.toArray(new Long[deleteDesktopRecordUids.size()]));
        return "success~!";
    }

    private static void buildOperationLog(List<TDesktopLog> allDesktopLogs, String LocalHostUserId, DesktopRecordParam desktopRecordParam, TDesktopRecord tDesktopRecord, String operationType) {
        List<DesktopLogParam> tDesktopLogs = desktopRecordParam.getDesktopLogParams();
        if (tDesktopLogs != null && tDesktopLogs.size() > 0){
            for (DesktopLogParam desktopLogParam:tDesktopLogs){
                TDesktopLog tDesktopLogPO = new TDesktopLog();
                tDesktopLogPO.setLocalhostUserId(Long.valueOf(LocalHostUserId));
                tDesktopLogPO.setOperUserName(desktopRecordParam.getTester());
                tDesktopLogPO.setOperationUid(tDesktopRecord.getUid().toString());
                tDesktopLogPO.setOperationType(operationType);
                tDesktopLogPO.setOperationFiled(desktopLogParam.getOperationFiled());
                tDesktopLogPO.setBeforeOperationValue(desktopLogParam.getBeforeOperationValue());
                tDesktopLogPO.setAfterOperationValue(desktopLogParam.getAfterOperationValue());
                tDesktopLogPO.setRequestMethod("POST");
                tDesktopLogPO.setOperTime(desktopRecordParam.getOperTime());
                tDesktopLogPO.setOperUrl("/desktop/submit");
                tDesktopLogPO.setOperIp(desktopRecordParam.getOperIp());
                allDesktopLogs.add(tDesktopLogPO);
            }
        }
    }


    private void autoInstertDesktopDevice(TDesktopRecord tDesktopRecord, DeviceInfoVo desktopDevice) {
        List<TCarlineInfo> tCarlineInfos = tCarlineInfoMapper.selectList(new QueryWrapper<TCarlineInfo>()
                .eq("original_carline_info_uid", desktopDevice.getCarlineInfoUid())
                .eq("version_code", desktopDevice.getVersionCode()));
        Long carlineInfoUid = null;
        if (tCarlineInfos != null && tCarlineInfos.size() > 0) {
            carlineInfoUid = tCarlineInfos.get(0).getCarlineInfoUid();
            tDesktopRecord.setCarlineInfoUid(carlineInfoUid);
        } else {
            carlineInfoUid = insertDeviceInfo(desktopDevice);
        }
        tDesktopRecord.setCarlineInfoUid(carlineInfoUid);
    }

    public Long insertDeviceInfo(DeviceInfoVo deviceInfoVo) {
        if (deviceInfoVo == null){
            return -1L;
        }
        //创建tcarline
        TCarline tCarline = new TCarline();
        tCarline.setCreateTime(new Date());
        buildCarlinePO(deviceInfoVo, tCarline);
        tCarlineMapper.insert(tCarline);

        //t_cluster
        TCluster tCluster = new TCluster();
        tCluster.setCarNum(1);
        tCluster.setCarlineUid(tCarline.getUid());
        tCluster.setCreateTime(new Date());
        buildTClusterPO(deviceInfoVo, tCluster);
        tClusterMapper.insert(tCluster);

        //t_carline_info
        TCarlineInfo tCarlineInfo = new TCarlineInfo();
        tCarlineInfo.setClusterUid(tCluster.getUid());
        tCarlineInfo.setCreateTime(new Date());
        buildTCarlineInfoPO(deviceInfoVo, tCarlineInfo);
        tCarlineInfoMapper.insert(tCarlineInfo);

        //t_component_data与其连接表的保存
        buildUpdateComponent(deviceInfoVo, tCarlineInfo.getCarlineInfoUid());
        //保存
        return tCarlineInfo.getCarlineInfoUid();
    }

    private void buildUpdateComponent(DeviceInfoVo deviceInfoVo, Long carlineInfoUid) {
        if (null != deviceInfoVo.getDeviceInfoComponents() && deviceInfoVo.getDeviceInfoComponents().size() > 0){
            for (DeviceInfoComponent deviceInfoComponent : deviceInfoVo.getDeviceInfoComponents()) {
                TComponentData componentData = new TComponentData();
                componentData.setComponentType(deviceInfoComponent.getComponentType());
                componentData.setComponentName(deviceInfoComponent.getComponentName());
                componentData.setIsAvaliabel(1);
                componentData.setPartNumber(deviceInfoComponent.getPartNumber());
                componentData.setSort(0);
                TCarlineComponent tCarlineComponent = new TCarlineComponent();
                tCarlineComponent.setZdcName(deviceInfoComponent.getZdcName());
                tCarlineComponent.setZdcVersion(deviceInfoComponent.getZdcVersion());
                tCarlineComponent.setCarlineInfoUid(carlineInfoUid);
                if (StringUtils.isNotEmpty(deviceInfoComponent.getHwVersion())){
                    String wareType = "HW";
                    componentData.setWareType("HW");
                    componentData.setComponentVersion(deviceInfoComponent.getHwVersion());
                    insertComponent(tCarlineComponent, wareType, componentData);
                }
                if (StringUtils.isNotEmpty(deviceInfoComponent.getSwVersion())){
                    componentData.setWareType("SW");
                    String wareType = "SW";
                    componentData.setComponentVersion(deviceInfoComponent.getSwVersion());
                    insertComponent(tCarlineComponent, wareType, componentData);
                }
                tCarlineComponentMapper.insert(tCarlineComponent);
            }

        }
    }
    private void insertComponent(TCarlineComponent tCarlineComponent,String wareType, TComponentData componentData) {
        tCarlineComponent.setUid(null);
        TComponentData tComponentData = tComponentDataMapper.selectOne(new QueryWrapper<TComponentData>()
                .eq("component_type", componentData.getComponentType())
                .eq("component_name", componentData.getComponentName())
                .eq("ware_type", componentData.getWareType())
                .eq("component_version", componentData.getComponentVersion())
                .eq("part_number", componentData.getPartNumber()));
        if (tComponentData != null){
            if (StringUtils.isNotEmpty(wareType) && "SW".equals(wareType)){
                tCarlineComponent.setSwVersionUid(tComponentData.getUid());
            }else if (StringUtils.isNotEmpty(wareType) && "HW".equals(wareType)){
                tCarlineComponent.setHwVersionUid(tComponentData.getUid());
            }
        }else {
            componentData.setUid(null);
            tComponentDataMapper.insert(componentData);
            if (StringUtils.isNotEmpty(wareType) && "SW".equals(wareType)){
                tCarlineComponent.setSwVersionUid(componentData.getUid());
            }
            if (StringUtils.isNotEmpty(wareType) && "HW".equals(wareType)){
                tCarlineComponent.setHwVersionUid(componentData.getUid());
            }
        }

    }
    private static void buildCarlinePO(DeviceInfoVo deviceInfoVo, TCarline tCarline) {
        tCarline.setCarlineModelType(deviceInfoVo.getCarlineModelType());
//        tCarline.setgoldenCarName(deviceInfoVo.getGoldenCarName());
        tCarline.setStatus(1);
        tCarline.setUpdateTime(new Date());
        tCarline.setIsShow(1);
    }

    private static void buildTCarlineInfoPO(DeviceInfoVo deviceInfoVo, TCarlineInfo tCarlineInfo) {
        tCarlineInfo.setBasicType(deviceInfoVo.getBasicType());
        String versionCode = DateUtil.format(new Date(), "yyMMddHHmmss");
        tCarlineInfo.setVersionCode(versionCode);
        tCarlineInfo.setPlatformType(deviceInfoVo.getPlatformType());
        tCarlineInfo.setMarketType(deviceInfoVo.getMarketType());
        tCarlineInfo.setVinCode(deviceInfoVo.getVinCode());
        tCarlineInfo.setDeviceName(deviceInfoVo.getDeviceName());
        tCarlineInfo.setVariantType(deviceInfoVo.getVariantType());
        tCarlineInfo.setDbVersion(deviceInfoVo.getDbVersion());
        tCarlineInfo.setUpdateTime(new Date());
    }

    private static void buildTClusterPO(DeviceInfoVo deviceInfoVo, TCluster tCluster) {
        tCluster.setprojectType(deviceInfoVo.getProjectType());//暂定工程项空缺不单独建表直接以名称替代
        tCluster.setLastUpdated(deviceInfoVo.getClusterLastUpdateName());//用户项暂时亦不单独建表以名称替代
        tCluster.setDeviceType(deviceInfoVo.getCarlineType());
        tCluster.setClusterName(deviceInfoVo.getClusterName());
        tCluster.setStatus(1);
        tCluster.setUpdateTime(new Date());
        tCluster.setIsShow(1);
    }

    private static void buildDesktopRecord(TDesktopRecord tDesktopRecord, String LocalHostUserId, DesktopRecordParam desktopRecordParam) {
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



















