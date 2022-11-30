package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.dto.ImportDeviceDTO;
import com.ruoyi.system.domain.dto.ImportPartComponentDTO;
import com.ruoyi.system.domain.param.DeviceListParam;
import com.ruoyi.system.domain.po.*;
import com.ruoyi.system.domain.vo.DeviceInfoComponent;
import com.ruoyi.system.domain.vo.DeviceInfoVo;
import com.ruoyi.system.domain.vo.DeviceListVo;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.DeviceListService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ruoyi.common.constant.TestKitConstants.*;
import static com.ruoyi.common.utils.PageUtils.startPage;

/**
 * 字典 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class DeviceListServiceImpl implements DeviceListService {
    @Autowired
    private TCarlineMapper tCarlineMapper;
    @Autowired
    private TClusterMapper tClusterMapper;
    @Autowired
    private TCarlineInfoMapper tCarlineInfoMapper;
    @Autowired
    private TComponentDataMapper tComponentDataMapper;
    @Autowired
    private TCarlineComponentMapper tCarlineComponentMapper;
    @Autowired
    private SysDictDataMapper dictDataMapper;
    @Autowired
    private SysDictDataServiceImpl sysDictDataService;
    /**
     * 查询车型基本数据列表
     *
     * @return `车型基本数据`
     */
    @Override
    public List queryDeviceList(DeviceListParam deviceListParam) {
        if (deviceListParam == null){
            return tCarlineInfoMapper.selectList(new QueryWrapper<>());
        }
        List<DeviceListVo> deviceListVos = tCarlineInfoMapper.queryDeviceList(deviceListParam);
        return deviceListVos;
    }

    @Override
    @Transactional
    public int updateDeviceInfo(DeviceInfoVo deviceInfoVo) {
        if (deviceInfoVo == null || deviceInfoVo.getCarlineInfoUid() == null){
            return -1;
        }
        //相关表字段获取
        Long carlineInfoUid = deviceInfoVo.getCarlineInfoUid();
        TCarlineInfo tCarlineInfo = tCarlineInfoMapper.selectById(carlineInfoUid);
        if (tCarlineInfo == null){
            return -1;
        }
        Long clusterUid = tCarlineInfo.getClusterUid();
        TCluster tCluster = tClusterMapper.selectById(clusterUid);
        Long carlineUid = tCluster.getCarlineUid();
        TCarline tCarline = tCarlineMapper.selectById(carlineUid);


        //tCluster 筛选有无额外版本，并删除相关版本与配件信息
        List<TCluster> tClusters = tClusterMapper.selectList(new QueryWrapper<TCluster>()
                .eq("carline_uid",tCarline.getUid())
                .eq("cluster_name",tCluster.getClusterName())
                .orderByDesc("car_num"));
        if (tClusters == null && tClusters.size() < 0) {
            return -1;
        }
        //t_cluster 1-10的版本循环,如果版本为10，则删去，存在则删，重新存储
        Integer deviceNum = tClusters.get(0).getCarNum();//从0开始到9循环，总计保存共10副本
        Integer nextDeviceNum = (deviceNum + 1) % 10;
        cascadeDeleteVersion(carlineInfoUid, tClusters.get(0));

        //save -tCluster
        tCluster.setCarNum(nextDeviceNum);
        buildTClusterPO(deviceInfoVo, tCluster);
        tClusterMapper.insert(tCluster);
        //t_carline_info
        tCarlineInfo.setClusterUid(tCluster.getUid());
        buildTCarlineInfoPO(deviceInfoVo, tCarlineInfo);
        tCarlineInfoMapper.insert(tCarlineInfo);
        //t_carline
        tCarline.setUpdateTime(new Date());
        buildCarlinePO(deviceInfoVo, tCarline);
        tCarlineMapper.updateById(tCarline);
        //t_component_data与其连接表的保存
        saveComponent(deviceInfoVo, tCarlineInfo.getCarlineInfoUid());

        //保存
        return 1;
    }

    /**
     * 批量删除
     * @param carlineInfoUids
     * @return
     */
    @Override
    public int deleteTCarlineByUids(Long[] carlineInfoUids) {
        //相关表字段
        for (Long carlineInfoUid:carlineInfoUids){
            TCarlineInfo tCarlineInfo = tCarlineInfoMapper.selectById(carlineInfoUid);
            if (tCarlineInfo == null){
                continue;
            }
            TCluster tCluster = tClusterMapper.selectById(tCarlineInfo.getClusterUid());
            cascadeDeleteVersion(carlineInfoUid,tCluster);
        }
        return 1;
    }

    @Override
    public DeviceInfoVo getInfo(Long carlineInfoUid) {
        DeviceInfoVo deviceInfoVo = tCarlineInfoMapper.queryDeviceInfo(carlineInfoUid);
        List<DeviceInfoComponent> deviceInfoComponents = tCarlineInfoMapper.queryDeviceComponent(carlineInfoUid);
        if (deviceInfoComponents == null || deviceInfoComponents.size() == 0){
            return null;
        }
        deviceInfoVo.setDeviceInfoComponents(deviceInfoComponents);
        return deviceInfoVo;

    }

    /**
     * 导入设备信息
     * @param deviceInfoVoList
     * @param b
     * @param operName
     * @return
     */
    @Override
    public String importDevice(Map<String,List<ImportDeviceDTO>> deviceInfoVoList, boolean b, String operName) {
        String[] dictTypes = new String[] {"clusterName", "projectType","platformType","marketType","functionGroupType",
                "variantType","taskType","carlineModelType","goldenCarType","goldenClusterNameType"};
        Map<String, Map<String,String>> dictMap = getDictMap(dictTypes);
        for (String key:deviceInfoVoList.keySet()){
            if (StringUtils.isEmpty(key) || !(key.equals("Devices") || key.equals("Benches"))){
                continue;
            }
            List<ImportDeviceDTO> importDeviceDTOS = deviceInfoVoList.get(key);
            if (importDeviceDTOS == null || importDeviceDTOS.size() == 0){
                continue;
            }
            for (ImportDeviceDTO importDeviceDTO:importDeviceDTOS){
                if (importDeviceDTO == null || StringUtils.isEmpty(importDeviceDTO.getCarline())){
                    continue;
                }
                TCarline tCarline = new TCarline();
                TCluster tCluster = new TCluster();
                TCarlineInfo tCarlineInfo = new TCarlineInfo();
                if (key.equals(DEVICE_TYPE_CAR)){//这里的Device其实是Car
                    tCluster.setDeviceType("2");
                }else {
                    tCluster.setDeviceType(DEVICE_TYPE_BENCH);
                }
                if (StringUtils.isNotEmpty(importDeviceDTO.getDeviceName())){
//                    tCarline.setgoldenCarName(importDeviceDTO.getDeviceName());
                    tCarlineInfo.setDeviceName(importDeviceDTO.getDeviceName());
                }
                if (StringUtils.isNotEmpty(importDeviceDTO.getCLU())){
                    String clusterName = getDictValue("clusterName", dictMap, importDeviceDTO.getCLU(), "0");
                    tCluster.setClusterName(clusterName);
                }
                if (StringUtils.isNotEmpty(importDeviceDTO.getProject())){
                    String projectType = getDictValue("projectType", dictMap, importDeviceDTO.getProject(), "0");
                    tCluster.setprojectType(projectType);
                }
                if (StringUtils.isNotEmpty(importDeviceDTO.getPlatform())){
                    String platformType = getDictValue("platformType", dictMap, importDeviceDTO.getPlatform(), "0");
                    tCarlineInfo.setPlatformType(platformType);
                }
                if (StringUtils.isNotEmpty(importDeviceDTO.getMarket())){
                    String marketType = getDictValue("marketType", dictMap, importDeviceDTO.getMarket(), "0");
                    tCarlineInfo.setMarketType(marketType);
                }
                if (StringUtils.isNotEmpty(importDeviceDTO.getCarline())){
                    String carlineModelType = getDictValue("carlineModelType", dictMap, importDeviceDTO.getCarline(), "0");
                    tCarline.setCarlineName(carlineModelType);
                }
                if (StringUtils.isNotEmpty(importDeviceDTO.getVariantType())){
                    String variantType = getDictValue("variantType", dictMap, importDeviceDTO.getVariantType(), "0");
                    tCarlineInfo.setVariantType(variantType);
                }
                if (StringUtils.isNotEmpty(importDeviceDTO.getVIN())){
                    String vin = importDeviceDTO.getVIN();
                    tCarlineInfo.setVinCode(vin);
                }
                if (StringUtils.isNotEmpty(importDeviceDTO.getDB())){
                    String db = importDeviceDTO.getDB();
                    tCarlineInfo.setDbVersion(db);
                }
                if (StringUtils.isNotEmpty(importDeviceDTO.getLastUpdated())){
                    String lastUpdated = importDeviceDTO.getLastUpdated();
                    tCluster.setLastUpdated(lastUpdated);
                }
                if (StringUtils.isNotEmpty(importDeviceDTO.getResp())){
                    String resp = importDeviceDTO.getResp();
                    tCarlineInfo.setResp(resp);
                }
                tCarlineMapper.insert(tCarline);
                tCluster.setCarlineUid(tCarline.getUid());
                tClusterMapper.insert(tCluster);
                tCarlineInfo.setClusterUid(tCluster.getUid());
                tCarlineInfoMapper.insert(tCarlineInfo);
                Long carlineInfoUid = tCarlineInfo.getCarlineInfoUid();

                
                TComponentData muhwData = new TComponentData();
                if (StringUtils.isNotEmpty(importDeviceDTO.getMUHW())){
                    String muhw = importDeviceDTO.getMUHW();
                    muhwData.setComponentType("mu");
                    muhwData.setcomponentVersion(muhw);
                    muhwData.setWareType("hw");
                    TCarlineComponent tCarlineComponent = new TCarlineComponent();
                    tCarlineComponent.setCarlineInfoUid(carlineInfoUid);
                    TComponentData tComponentData = tComponentDataMapper.selectOne(new QueryWrapper<TComponentData>().eq("component_type", muhwData.getComponentType())
                            .eq("component_version", muhwData.getcomponentVersion())
                            .eq("ware_type", muhwData.getWareType()));
                    if (tComponentData != null){
                        tCarlineComponent.setComponentUid(tComponentData.getUid());
                        tCarlineComponentMapper.insert(tCarlineComponent);
                    }else {
                        tComponentDataMapper.insert(muhwData);
                        tCarlineComponent.setComponentUid(muhwData.getUid());
                        tCarlineComponentMapper.insert(tCarlineComponent);
                    }
                }
                TComponentData muswData = new TComponentData();
                if (StringUtils.isNotEmpty(importDeviceDTO.getMUSW())){
                    String musw = importDeviceDTO.getMUSW();
                    muswData.setComponentType("mu");
                    muswData.setcomponentVersion(musw);
                    muswData.setWareType("sw");
                    TCarlineComponent tCarlineComponent = new TCarlineComponent();
                    tCarlineComponent.setCarlineInfoUid(carlineInfoUid);
                    TComponentData tComponentData = tComponentDataMapper.selectOne(new QueryWrapper<TComponentData>().eq("component_type", muswData.getComponentType())
                            .eq("component_version", muswData.getcomponentVersion())
                            .eq("ware_type", muswData.getWareType()));
                    if (tComponentData != null){
                        tCarlineComponent.setComponentUid(tComponentData.getUid());
                        tCarlineComponentMapper.insert(tCarlineComponent);
                    }else {
                        tComponentDataMapper.insert(muswData);
                        tCarlineComponent.setComponentUid(muswData.getUid());
                        tCarlineComponentMapper.insert(tCarlineComponent);
                    }
                }
                TComponentData asterixHWData = new TComponentData();
                if (StringUtils.isNotEmpty(importDeviceDTO.getAsterixHW())){
                    String asterixHW = importDeviceDTO.getAsterixHW();
                    asterixHWData.setComponentType("asterix");
                    asterixHWData.setcomponentVersion(asterixHW);
                    asterixHWData.setWareType("hw");
                    TCarlineComponent tCarlineComponent = new TCarlineComponent();
                    tCarlineComponent.setCarlineInfoUid(carlineInfoUid);
                    TComponentData tComponentData = tComponentDataMapper.selectOne(new QueryWrapper<TComponentData>().eq("component_type", asterixHWData.getComponentType())
                            .eq("component_version", asterixHWData.getcomponentVersion())
                            .eq("ware_type", asterixHWData.getWareType()));
                    if (tComponentData != null){
                        tCarlineComponent.setComponentUid(tComponentData.getUid());
                        tCarlineComponentMapper.insert(tCarlineComponent);
                    }else {
                        tComponentDataMapper.insert(asterixHWData);
                        tCarlineComponent.setComponentUid(asterixHWData.getUid());
                        tCarlineComponentMapper.insert(tCarlineComponent);
                    }
                }
                TComponentData asterixSWData = new TComponentData();
                if (StringUtils.isNotEmpty(importDeviceDTO.getAsterixSW())){
                    String asterixSW = importDeviceDTO.getAsterixSW();
                    asterixSWData.setComponentType("asterix");
                    asterixSWData.setcomponentVersion(asterixSW);
                    asterixSWData.setWareType("sw");
                    TCarlineComponent tCarlineComponent = new TCarlineComponent();
                    tCarlineComponent.setCarlineInfoUid(carlineInfoUid);
                    TComponentData tComponentData = tComponentDataMapper.selectOne(new QueryWrapper<TComponentData>().eq("component_type", asterixSWData.getComponentType())
                            .eq("component_version", asterixSWData.getcomponentVersion())
                            .eq("ware_type", asterixSWData.getWareType()));
                    if (tComponentData != null){
                        tCarlineComponent.setComponentUid(tComponentData.getUid());
                        tCarlineComponentMapper.insert(tCarlineComponent);
                    }else {
                        tComponentDataMapper.insert(asterixSWData);
                        tCarlineComponent.setComponentUid(asterixSWData.getUid());
                        tCarlineComponentMapper.insert(tCarlineComponent);
                    }
                }
                TComponentData kombiHWData = new TComponentData();
                if (StringUtils.isNotEmpty(importDeviceDTO.getKombiHW())){
                    String kombiHW = importDeviceDTO.getKombiHW();
                    kombiHWData.setComponentType("kombi");
                    kombiHWData.setcomponentVersion(kombiHW);
                    kombiHWData.setWareType("hw");
                    TCarlineComponent tCarlineComponent = new TCarlineComponent();
                    tCarlineComponent.setCarlineInfoUid(carlineInfoUid);
                    TComponentData tComponentData = tComponentDataMapper.selectOne(new QueryWrapper<TComponentData>().eq("component_type", kombiHWData.getComponentType())
                            .eq("component_version", kombiHWData.getcomponentVersion())
                            .eq("ware_type", kombiHWData.getWareType()));
                    if (tComponentData != null){
                        tCarlineComponent.setComponentUid(tComponentData.getUid());
                        tCarlineComponentMapper.insert(tCarlineComponent);
                    }else {
                        tComponentDataMapper.insert(kombiHWData);
                        tCarlineComponent.setComponentUid(kombiHWData.getUid());
                        tCarlineComponentMapper.insert(tCarlineComponent);
                    }
                }
                TComponentData kombiSWData = new TComponentData();
                if (StringUtils.isNotEmpty(importDeviceDTO.getKombiSW())){
                    String kombiSW = importDeviceDTO.getKombiSW();
                    kombiSWData.setComponentType("kombi");
                    kombiSWData.setcomponentVersion(kombiSW);
                    kombiSWData.setWareType("sw");
                    TCarlineComponent tCarlineComponent = new TCarlineComponent();
                    tCarlineComponent.setCarlineInfoUid(carlineInfoUid);
                    TComponentData tComponentData = tComponentDataMapper.selectOne(new QueryWrapper<TComponentData>().eq("component_type", kombiSWData.getComponentType())
                            .eq("component_version", kombiSWData.getcomponentVersion())
                            .eq("ware_type", kombiSWData.getWareType()));
                    if (tComponentData != null){
                        tCarlineComponent.setComponentUid(tComponentData.getUid());
                        tCarlineComponentMapper.insert(tCarlineComponent);
                    }else {
                        tComponentDataMapper.insert(kombiSWData);
                        tCarlineComponent.setComponentUid(kombiSWData.getUid());
                        tCarlineComponentMapper.insert(tCarlineComponent);
                    }
                }
                TComponentData gatewayHWData = new TComponentData();
                if (StringUtils.isNotEmpty(importDeviceDTO.getGatewayHW())){
                    String gatewayHW = importDeviceDTO.getGatewayHW();
                    gatewayHWData.setComponentType("Gateway");
                    gatewayHWData.setcomponentVersion(gatewayHW);
                    gatewayHWData.setWareType("hw");
                    TCarlineComponent tCarlineComponent = new TCarlineComponent();
                    tCarlineComponent.setCarlineInfoUid(carlineInfoUid);
                    TComponentData tComponentData = tComponentDataMapper.selectOne(new QueryWrapper<TComponentData>().eq("component_type", gatewayHWData.getComponentType())
                            .eq("component_version", gatewayHWData.getcomponentVersion())
                            .eq("ware_type", gatewayHWData.getWareType()));
                    if (tComponentData != null){
                        tCarlineComponent.setComponentUid(tComponentData.getUid());
                        tCarlineComponentMapper.insert(tCarlineComponent);
                    }else {
                        tComponentDataMapper.insert(gatewayHWData);
                        tCarlineComponent.setComponentUid(gatewayHWData.getUid());
                        tCarlineComponentMapper.insert(tCarlineComponent);
                    }
                }
                TComponentData gatewaySWData = new TComponentData();
                if (StringUtils.isNotEmpty(importDeviceDTO.getGatewaySW())){
                    String gatewaySW = importDeviceDTO.getGatewaySW();
                    gatewaySWData.setComponentType("Gateway");
                    gatewaySWData.setcomponentVersion(gatewaySW);
                    gatewaySWData.setWareType("sw");
                    TCarlineComponent tCarlineComponent = new TCarlineComponent();
                    tCarlineComponent.setCarlineInfoUid(carlineInfoUid);
                    TComponentData tComponentData = tComponentDataMapper.selectOne(new QueryWrapper<TComponentData>().eq("component_type", gatewaySWData.getComponentType())
                            .eq("component_version", gatewaySWData.getcomponentVersion())
                            .eq("ware_type", gatewaySWData.getWareType()));
                    if (tComponentData != null){
                        tCarlineComponent.setComponentUid(tComponentData.getUid());
                        tCarlineComponentMapper.insert(tCarlineComponent);
                    }else {
                        tComponentDataMapper.insert(gatewaySWData);
                        tCarlineComponent.setComponentUid(gatewaySWData.getUid());
                        tCarlineComponentMapper.insert(tCarlineComponent);
                    }
                }
                TComponentData conboxOCUHWData = new TComponentData();
                if (StringUtils.isNotEmpty(importDeviceDTO.getConboxOCUHW())){
                    String conboxOCUHW = importDeviceDTO.getConboxOCUHW();
                    conboxOCUHWData.setComponentType("Conbox/OCU");
                    conboxOCUHWData.setcomponentVersion(conboxOCUHW);
                    conboxOCUHWData.setWareType("hw");
                    TCarlineComponent tCarlineComponent = new TCarlineComponent();
                    tCarlineComponent.setCarlineInfoUid(carlineInfoUid);
                    TComponentData tComponentData = tComponentDataMapper.selectOne(new QueryWrapper<TComponentData>().eq("component_type", conboxOCUHWData.getComponentType())
                            .eq("component_version", conboxOCUHWData.getcomponentVersion())
                            .eq("ware_type", conboxOCUHWData.getWareType()));
                    if (tComponentData != null){
                        tCarlineComponent.setComponentUid(tComponentData.getUid());
                        tCarlineComponentMapper.insert(tCarlineComponent);
                    }else {
                        tComponentDataMapper.insert(conboxOCUHWData);
                        tCarlineComponent.setComponentUid(conboxOCUHWData.getUid());
                        tCarlineComponentMapper.insert(tCarlineComponent);
                    }
                }
                TComponentData conboxOCUSWData = new TComponentData();
                if (StringUtils.isNotEmpty(importDeviceDTO.getConboxOCUSW())){
                    String conboxOCUSW = importDeviceDTO.getConboxOCUSW();
                    conboxOCUSWData.setComponentType("Conbox/OCU");
                    conboxOCUSWData.setcomponentVersion(conboxOCUSW);
                    conboxOCUSWData.setWareType("sw");
                    TCarlineComponent tCarlineComponent = new TCarlineComponent();
                    tCarlineComponent.setCarlineInfoUid(carlineInfoUid);
                    TComponentData tComponentData = tComponentDataMapper.selectOne(new QueryWrapper<TComponentData>().eq("component_type", conboxOCUSWData.getComponentType())
                            .eq("component_version", conboxOCUSWData.getcomponentVersion())
                            .eq("ware_type", conboxOCUSWData.getWareType()));
                    if (tComponentData != null){
                        tCarlineComponent.setComponentUid(tComponentData.getUid());
                        tCarlineComponentMapper.insert(tCarlineComponent);
                    }else {
                        tComponentDataMapper.insert(conboxOCUSWData);
                        tCarlineComponent.setComponentUid(conboxOCUSWData.getUid());
                        tCarlineComponentMapper.insert(tCarlineComponent);
                    }
                }
            }
        }
        return "Sucess";
    }

    /**
     * 查询字典值
     * @param dictTypeName 查询的字典type名
     * @param dictMap 预设的Map
     * @param dictLabel label值
     * @param matrixType matrix类型
     * @return
     */
    private String getDictValue(String dictTypeName, Map<String, Map<String,String>> dictMap, String dictLabel,String matrixType) {
        String dictValue;
        Map<String,String> dictLabelMap = dictMap.get(dictTypeName);
        if (StringUtil.isNullOrEmpty(dictLabel)){
            return null;
        }
        if (dictLabelMap == null){
            dictLabelMap = new HashMap<String,String>();
        }
        //假设加入的值字典中不存在
        if (StringUtil.isNullOrEmpty(dictLabelMap.get(dictLabel))){
            SysDictData sysDictData = new SysDictData();
            sysDictData.setDictType(dictTypeName);
            sysDictData.setMatrixType(matrixType);
            sysDictData.setStatus(DICT_STATUS_NORMAL);
            sysDictData.setDictLabel(dictLabel);
            Integer dictValueNum;
            if (dictDataMapper.selectMaxDictType(dictTypeName) != null) {
                dictValueNum = dictDataMapper.selectMaxDictType(dictTypeName) + 1;
            }else {
                dictValueNum = 1;
            }
            dictValue = dictValueNum.toString();
            sysDictData.setDictValue(dictValue);
            sysDictDataService.insertMatrixDictData(sysDictData);
            dictLabelMap.put(dictLabel,sysDictData.getDictValue());
            dictMap.put(dictTypeName,dictLabelMap);
        }else {
            //假如存在则刷为将状态刷为0
            dictValue = dictLabelMap.get(dictLabel);
            dictDataMapper.updateDictDataStatus(dictTypeName,dictLabel,dictValue,"0");
        }
        return dictValue;
    }

    /**
     * 手写字典映射，此处可公共或者通过反射去优化
     */
    private Map<String, Map<String,String>> getDictMap(String[] dictTypes) {
        QueryWrapper<SysDictData> dictMappingWrapper = new QueryWrapper<>();
        dictMappingWrapper.in("dict_type" , dictTypes);
        Map<String, Map<String,String>> dictMap = new HashMap<>();
        for (SysDictData sysDictData : dictDataMapper.selectList(dictMappingWrapper)) {
            Map<String,String> DictDataMap;
            if (dictMap.get(sysDictData.getDictType()) != null){
                DictDataMap = dictMap.get(sysDictData.getDictType());
            }else {
                DictDataMap = new HashMap<>();
            }
            DictDataMap.put(sysDictData.getDictLabel(),sysDictData.getDictValue());
            dictMap.put(sysDictData.getDictType(),DictDataMap);
        }
        return dictMap;
    }

    /**
     * 级联删除某一副本关联内容
     * @param carlineInfoUid
     * @param exClusterVersion
     */
    private void cascadeDeleteVersion(Long carlineInfoUid, TCluster exClusterVersion) {
        List<TCarlineComponent> tCarlineComponents = tCarlineComponentMapper.selectList(new QueryWrapper<TCarlineComponent>()
                .eq("carline_info_uid", carlineInfoUid));
        if (tCarlineComponents != null && tCarlineComponents.size() > 0) {
            for (TCarlineComponent tCarlineComponent : tCarlineComponents) {
                List<TCarlineComponent> ifDeleteTCarlineComponents = tCarlineComponentMapper.selectList(new QueryWrapper<TCarlineComponent>()
                        .eq("component_uid", tCarlineComponent.getComponentUid()));
                if (ifDeleteTCarlineComponents != null && ifDeleteTCarlineComponents.size() == 1) {
                    tComponentDataMapper.deleteById(ifDeleteTCarlineComponents.get(0).getComponentUid());
                }
            }
        }
        tCarlineComponentMapper.delete(new QueryWrapper<TCarlineComponent>()
                .eq("carline_info_uid", carlineInfoUid));
        tCarlineInfoMapper.deleteById(carlineInfoUid);
        tClusterMapper.deleteById(exClusterVersion);
    }

    @Override
    @Transactional
    public int insertDeviceInfo(DeviceInfoVo deviceInfoVo) {
        if (deviceInfoVo == null){
            return -1;
        }
        //t_carline
        QueryWrapper<TCarline> tcarlineWrapper = new QueryWrapper<>();
//        tcarlineWrapper.eq("golden_car_name", deviceInfoVo.getGoldenCarName());
        tcarlineWrapper.eq("carline_model_type", deviceInfoVo.getCarlineModelType());
        TCarline tCarline = tCarlineMapper.selectOne(tcarlineWrapper);
        if (null == tCarline) {
            //创建tcarline
            tCarline = new TCarline();
            tCarline.setCreateTime(new Date());
            buildCarlinePO(deviceInfoVo, tCarline);
            tCarlineMapper.insert(tCarline);
        }

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
        saveComponent(deviceInfoVo, tCarlineInfo.getCarlineInfoUid());
        //保存
        return 1;
    }

    private void saveComponent(DeviceInfoVo deviceInfoVo, Long tCarlineInfoUid) {
        if (null != deviceInfoVo.getDeviceInfoComponents() && deviceInfoVo.getDeviceInfoComponents().size() > 0){
            for (DeviceInfoComponent deviceInfoComponent : deviceInfoVo.getDeviceInfoComponents()) {
                List<TComponentData> tComponentDatas = tComponentDataMapper.selectList(new QueryWrapper<TComponentData>()
                        .eq("component_type", deviceInfoComponent.getComponentType())
                        .eq("ware_type", deviceInfoComponent.getWareType())
                        .eq("component_version", deviceInfoComponent.getComponentModel()));
                TComponentData tComponentData = new TComponentData();
                if (null != tComponentDatas && tComponentDatas.size() > 0){
                    tComponentData = tComponentDatas.get(0);
                }else {
                    tComponentData.setComponentType(deviceInfoComponent.getComponentType());
                    tComponentData.setWareType(deviceInfoComponent.getWareType());
                    tComponentData.setcomponentVersion(deviceInfoComponent.getComponentModel());
                    tComponentDataMapper.insert(tComponentData);
                }

                //t_carline_component 关系表
                TCarlineComponent tCarlineComponent = new TCarlineComponent();
                tCarlineComponent.setComponentUid(tComponentData.getUid());
                tCarlineComponent.setCarlineInfoUid(tCarlineInfoUid);
                tCarlineComponent.setCreateTime(new Date());
                tCarlineComponent.setUpdateTime(new Date());
                tCarlineComponentMapper.insert(tCarlineComponent);
            }

        }
    }

    Map<String,String> cleanMap;

    private static void buildCarlinePO(DeviceInfoVo deviceInfoVo, TCarline tCarline) {
        tCarline.setCarlineName(deviceInfoVo.getCarlineModelType());
//        tCarline.setgoldenCarName(deviceInfoVo.getGoldenCarName());
        tCarline.setStatus(1);
        tCarline.setUpdateTime(new Date());
        tCarline.setIsShow(1);
    }

    private static void buildTCarlineInfoPO(DeviceInfoVo deviceInfoVo, TCarlineInfo tCarlineInfo) {
        tCarlineInfo.setPlatformType(deviceInfoVo.getPlatformType());
        tCarlineInfo.setMarketType(deviceInfoVo.getMarketType());
        tCarlineInfo.setVinCode(deviceInfoVo.getVinCode());
        tCarlineInfo.setDeviceName(deviceInfoVo.getGoldenCarName());
        //todo：t_carline_info basic_type考虑删除
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

}



















