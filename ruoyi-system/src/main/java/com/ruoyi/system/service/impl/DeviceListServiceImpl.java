package com.ruoyi.system.service.impl;
import java.util.Date;

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
import com.ruoyi.system.domain.param.DeviceCompareParam;
import com.ruoyi.system.domain.param.DeviceListParam;
import com.ruoyi.system.domain.po.*;
import com.ruoyi.system.domain.vo.DeviceInfoComponent;
import com.ruoyi.system.domain.vo.DeviceInfoVo;
import com.ruoyi.system.domain.vo.DeviceListVo;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.DeviceListService;
import io.netty.util.internal.StringUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.*;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private TMatrixMapper tMatrixMapper;
    @Autowired
    private TComponentDataMapper tComponentDataMapper;
    @Autowired
    private TCarlineComponentMapper tCarlineComponentMapper;
    @Autowired
    private SysDictDataMapper dictDataMapper;

    @Autowired
    private TDtcReportMapper tdtcReportMapper;
    @Autowired
    private SysDictDataServiceImpl sysDictDataService;
    /**
     * 查询车型基本数据列表
     *
     * @return `车型基本数据`
     */
    @Override
    public List queryDeviceList(DeviceListParam deviceListParam) {
        /*if (deviceListParam == null){
            return tCarlineInfoMapper.selectList(new QueryWrapper<>());
        }*/
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
        buildUpdateComponent(deviceInfoVo, tCarlineInfo.getCarlineInfoUid());

        //保存
        return 1;
    }

    @Override
    public AjaxResult importDTCReport(MultipartFile file, boolean b, String operName){
        try {
            List<Object> tdtcReportDTOS = new ArrayList<>();
            Document docResult=XmlUtil.readXML(file.getInputStream());
            String VIN = XmlUtil.getNodeListByXPath("//Fahrgestellnummer", docResult).item(0).getTextContent();
            Map<Object, Object> VINMap = new HashMap<>();
            VINMap.put("ecuId","Vehicle");
            VINMap.put("VIN",VIN);
            tdtcReportDTOS.add(VINMap);
    NodeList nodeListByXPath = XmlUtil.getNodeListByXPath("//Diagnosebloecke/Diagnoseblock", docResult);
            for (int i = 0 ; i < nodeListByXPath.getLength();i ++){
        Node item = nodeListByXPath.item(i);
        TDTCReportDTO reportDTO = XmlUtil.xmlToBean(item, TDTCReportDTO.class);
        if (reportDTO == null){
            continue;
        }
        String adresse = reportDTO.getAdresse();
        TDTCReport tdtcReport = tdtcReportMapper.selectOne(new QueryWrapper<TDTCReport>().eq("adresse", adresse));
        Map<String, String> componentMap = new HashMap<>();
        componentMap.put("SWVersion",reportDTO.getSWVersion());
        componentMap.put("HWVersion",reportDTO.getHWVersion());
        componentMap.put("PN",reportDTO.getHWTeilenummer());
        if (tdtcReport != null) {
            componentMap.put("ecuId", tdtcReport.getEcuId());
            String componentName = tdtcReport.getComponentName();
            if (StringUtils.isNotEmpty(componentName) && componentName.contains("::")){
                for (String s : componentName.split(",,")) {
                    String[] split = s.split("::");
                    if (split != null && split.length >= 2){
                        componentName = regexStr(ReflectUtils.invokeGetter(reportDTO, split[0]).toString(), split[1]);
                    }
                }
            }
            componentMap.put("componentName",componentName);
            componentMap.put("componentType",tdtcReport.getComponentType());
            reportDTO.setComponentType(componentName);
            if (StringUtils.isNotEmpty(tdtcReport.getVariant())){
                String variant = null;
                for (String s : tdtcReport.getVariant().split(",,")) {
                    String[] split = s.split("::");
                    if (split != null && split.length >= 2){
                        variant = regexStr(ReflectUtils.invokeGetter(reportDTO, split[0]).toString(), split[1]);
                    }
                }
                if (StringUtils.isNotEmpty(variant)){
                    //MIB3
                    if ("B".equals(variant) || "H".equals(variant) || "P".equals(variant)){
                        String swVersion = componentMap.get("SWVersion");
                        char[] chars = swVersion.toUpperCase().toCharArray();
                        Boolean isPureNum = true;
                        for (int j = 0; j < chars.length;j++){
                            if (chars[j] < 48 && chars[j] > 57){
                                isPureNum = false;
                            }
                        }
                        if (isPureNum){
                            componentMap.put("SWVersion",new String("P" + swVersion));
                        }else {
                            if (chars.length == 4 && "Z".equals(chars[0]) && chars[1] >=48 && chars[1] <=57
                                    && chars[2] >=48 && chars[2] <=57 && chars[3] >=48 && chars[3] <=57){
                                if (chars[1] >= 53){
                                    componentMap.put("SWVersion",new String("E3" + chars[1] + chars[2] + chars[3]));
                                }else {
                                    componentMap.put("SWVersion",new String("E4" + chars[1] + chars[2] + chars[3]));
                                }
                            }
                        }
                    }
                }
            }
            if ("005F".equals(adresse)){
                Map<String, String> zdcMap = new HashMap<>();
                zdcMap.put("componentName",componentName);
                zdcMap.put("componentType",componentName);
                zdcMap.put("zdcName",reportDTO.getZdcName());
                zdcMap.put("zdcVersion",reportDTO.getZdcVersion());
                zdcMap.put("ecuId","005F-ZDC");
                tdtcReportDTOS.add(zdcMap);
            }
            tdtcReportDTOS.add(componentMap);
        }
    }
    NodeList nodeListByXPath1 = XmlUtil.getNodeListByXPath("//Diagnosebloecke/Diagnoseblock/SubTeilnehmer/Sub",docResult);
            for (int j = 0;j < nodeListByXPath1.getLength();j++){
        Node item = nodeListByXPath1.item(j);
        TDTCReportDTO reportDTO = XmlUtil.xmlToBean(item, TDTCReportDTO.class);
        if (StringUtils.isNotEmpty(reportDTO.getSystembezeichnung()) && reportDTO.getSystembezeichnung().contains("AED")){
            reportDTO = XmlUtil.xmlToBean(nodeListByXPath1.item(j), TDTCReportDTO.class);
            Map<String, String> aedMap = new HashMap<>();
            aedMap.put("ecuId","005F-AED");
            aedMap.put("componentName","AED");
            aedMap.put("componentType","AED");
            aedMap.put("SWVersion",reportDTO.getSWVersion());
            aedMap.put("HWVersion",reportDTO.getHWVersion());
            aedMap.put("PN",reportDTO.getHWTeilenummer());
            tdtcReportDTOS.add(aedMap);
        }
        if ("Data Medium".equals(reportDTO.getSubtName())){
            Map<String, String> aedMap = new HashMap<>();
            aedMap.put("ecuId","005F-Data Medium");
            aedMap.put("DBVersion",reportDTO.getSWVersion());
            tdtcReportDTOS.add(aedMap);
        }
    }
            return AjaxResult.success(tdtcReportDTOS);
} catch (IOException e) {
        throw new RuntimeException(e);
        }
        }

    private static String regexStr(String content, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()){
            return  matcher.group(1);
        }
        return null;
    }

    @Test
    public void test1(){
//        String regex = "EV_HCP3([\\S]*?)Node[\\s\\S]*?";
//        String content = "EV_HCP3BaseNodeAU41X_001001";
        String content = "MU-TH-N-CN   ";
        String regex = "[ ]*MU-T{0,1}([BHP])[\\\\s\\\\S]*?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        if(matcher.find()){
            System.out.println(matcher.group(1));
        }else {
            System.out.println("false");
        }
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
        Map<String,DeviceInfoComponent> deviceInfoComponentMap = new HashMap<>();
        for (DeviceInfoComponent deviceInfoComponent:deviceInfoComponents){
            String componentType = deviceInfoComponent.getComponentType();
            if (StringUtils.isEmpty(componentType)){
                continue;
            }
            DeviceInfoComponent deviceMap;
            if (deviceInfoComponentMap.get(componentType) == null){
                deviceMap = new DeviceInfoComponent();
            }else {
                deviceMap = deviceInfoComponentMap.get(componentType);
            }
            if (deviceInfoComponent.getCarlineInfoUid() != null){
                deviceMap.setCarlineInfoUid(deviceInfoComponent.getCarlineInfoUid());
            }
            if (StringUtils.isNotEmpty(deviceInfoComponent.getComponentType())){
                deviceMap.setComponentType(deviceInfoComponent.getComponentType());
            }
            if (StringUtils.isNotEmpty(deviceInfoComponent.getComponentName())){
                deviceMap.setComponentName(deviceInfoComponent.getComponentName());
            }if (StringUtils.isNotEmpty(deviceInfoComponent.getPartNumber())){
                deviceMap.setPartNumber(deviceInfoComponent.getPartNumber());
            }if (StringUtils.isNotEmpty(deviceInfoComponent.getComponentVersion()) && StringUtils.isNotEmpty(deviceInfoComponent.getWareType())){
                if ("SW".equals(deviceInfoComponent.getWareType())){
                    deviceMap.setSwVersion(deviceInfoComponent.getComponentVersion());
                }
                if ("HW".equals(deviceInfoComponent.getWareType())){
                    deviceMap.setHwVersion(deviceInfoComponent.getComponentVersion());
                }
            }if (StringUtils.isNotEmpty(deviceInfoComponent.getZdcName())){
                deviceMap.setZdcName(deviceInfoComponent.getZdcName());
            }
            if (StringUtils.isNotEmpty(deviceInfoComponent.getZdcVersion())){
                deviceMap.setZdcVersion(deviceInfoComponent.getZdcVersion());
            }
            deviceInfoComponentMap.put(componentType,deviceMap);
        }
        deviceInfoVo.setDeviceInfoComponentMap(deviceInfoComponentMap);
        return deviceInfoVo;
    }



    public List<GoldenInfoComponentDTO> getGoldenInfoComponents( String carlineModelType,String ClusterNameType,String marketType){
        //虽然都是CLU，但名称相同，映射的字典表不同，这里需要做个转换
        String goldenClusterNameType = tMatrixMapper.selectGoldenClusterNameType(ClusterNameType);
        List<Long> goldenCarTypes = tMatrixMapper.selectGoldenCarType(carlineModelType, goldenClusterNameType);
        //理论上对应的有且仅有一个，但目前有重复的 todo
        //https://sumomoriaty.oss-cn-beijing.aliyuncs.com/zdcar/202212011111630.png
        if (goldenCarTypes == null || goldenCarTypes.size() == 0){
            return null;
        }
        Long goldenCarType = goldenCarTypes.get(0);
        //因为carline/sop字段混乱，carlineModelType暂不参与比较
        return tCarlineInfoMapper.selectGoldenCarlineInfo(carlineModelType, ClusterNameType, marketType, goldenCarType);
        //结果：https://sumomoriaty.oss-cn-beijing.aliyuncs.com/zdcar/202212011422541.png
    }

    public AjaxResult compareComponent(DeviceCompareParam deviceCompareParam){
        String carlineModelType = deviceCompareParam.getCarlineModelType();
        String clusterName = deviceCompareParam.getClusterName();
        String marketType = deviceCompareParam.getMarketType();
        String componentType = deviceCompareParam.getComponentType();
        String swVersion = deviceCompareParam.getSwVersion();
        String hwVersion = deviceCompareParam.getHwVersion();
        String partNumber = deviceCompareParam.getPartNumber();
        if (StringUtils.isEmpty(carlineModelType) || StringUtils.isEmpty(clusterName) || StringUtils.isEmpty(marketType)
                || StringUtils.isEmpty(componentType) || !(StringUtils.isNotEmpty(hwVersion) || StringUtils.isNotEmpty(swVersion))){
            return AjaxResult.error("参数不得为空");
        }
        List<GoldenInfoComponentDTO> goldenInfoComponentDTOS = getGoldenInfoComponents(carlineModelType, clusterName, marketType);
        if (goldenInfoComponentDTOS == null || goldenInfoComponentDTOS.size() == 0){
            return AjaxResult.error("GoldenInfo里并没有对应取值");
        }
        Map<String,GoldenInfoComponentDTO> goldenInfoComponentDTOMap = buildCompareMap(goldenInfoComponentDTOS);
        for (String goldenComponentType:goldenInfoComponentDTOMap.keySet()){
            String cleanStr = cleanStr(goldenComponentType);
            componentType = new String(cleanStr(componentType));
            if (componentType.contains(cleanStr) ||cleanStr.contains(componentType) || componentType.equals(cleanStr)){
                DeviceCompareVO deviceCompareVO = new DeviceCompareVO();
                String componentModel = null;
                if (StringUtils.isNotEmpty(hwVersion)){
                    componentModel = hwVersion;
                    Integer compareNum = compareHWComponent(componentModel, goldenInfoComponentDTOMap.get(goldenComponentType));
                    if (compareNum.equals(0)){
                        continue;
                    }
                    deviceCompareVO.setCompareNum(compareNum);
                    deviceCompareVO.setMinimalHW(goldenInfoComponentDTOMap.get(goldenComponentType).getMinimalHW());
                    deviceCompareVO.setCurrentVersion(goldenInfoComponentDTOMap.get(goldenComponentType).getHwComponentVersion());
                    return AjaxResult.success(deviceCompareVO);
                }else {
                    componentModel = swVersion;
                    Integer compareNum = compareSWComponent(componentModel, goldenInfoComponentDTOMap.get(goldenComponentType));
                    if (compareNum.equals(0)){
                        continue;
                    }
                    deviceCompareVO.setCompareNum(compareNum);
                    deviceCompareVO.setCurrentVersion(goldenInfoComponentDTOMap.get(goldenComponentType).getSwComponentVersion());
                    return AjaxResult.success(deviceCompareVO);
                }
            }
        }
        return AjaxResult.success("");
    }


    private int compareSWComponent(String componentModel, GoldenInfoComponentDTO goldenInfoComponentDTO) {
        if (StringUtils.isEmpty(goldenInfoComponentDTO.getSwComponentVersion())){
            return 0;
        }
        Integer normalVersion = cleanNum(goldenInfoComponentDTO.getSwComponentVersion(),MIN);
        Integer correnVersion = cleanNum(componentModel,MIN);
        if (correnVersion >= normalVersion){
            return 3;
        }else {
            return 1;
        }
    }
    private int compareHWComponent(String componentModel, GoldenInfoComponentDTO goldenInfoComponentDTO) {
        if (StringUtils.isEmpty(goldenInfoComponentDTO.getHwComponentVersion())){
            return 0;
        }
        Integer minimalVersion = 0;
        if (StringUtils.isNotEmpty(goldenInfoComponentDTO.getMinimalHW())){
            minimalVersion = cleanNum(goldenInfoComponentDTO.getMinimalHW(),MIN);
        }
        Integer normalVersion = cleanNum(goldenInfoComponentDTO.getHwComponentVersion(),MIN);
        Integer correnVersion = cleanNum(componentModel,MIN);
        if (correnVersion >= normalVersion){
            return 3;
        }else if (correnVersion >= minimalVersion){
            return 2;
        }else {
            return 1;
        }
    }

    Integer MIN = -1;
    Integer MAX = 1;
    private Integer cleanNum(String deviceComponent,Integer getNum) {
        String[] split = deviceComponent.split("/");
        if (MAX.equals(split)) {
            deviceComponent = new String(split[split.length - 1]);
        }else {
            deviceComponent = new String(split[0]);
        }
        split = deviceComponent.split("-");
        if (MAX.equals(split)) {
            deviceComponent = new String(split[split.length - 1]);
        }else {
            deviceComponent = new String(split[0]);
        }
        char[] chars = deviceComponent.toCharArray();
        StringBuffer buffer=new StringBuffer();
        for(int i = 0; i < chars.length; i ++) {
            if((chars[i] >= 48 && chars[i] <= 57)) {
                buffer.append(chars[i]);//去除特殊格式
            }
        }
        return Integer.valueOf(buffer.toString());
    }
    private String cleanStr(String deviceComponent) {
        char[] chars = deviceComponent.toCharArray();
        StringBuffer buffer=new StringBuffer();
        for(int i = 0; i < chars.length; i ++) {
            if((chars[i] >= 19968 && chars[i] <= 40869) || (chars[i] >= 97 && chars[i] <= 122) || (chars[i] >= 65 && chars[i] <= 90)) {
                buffer.append(chars[i]);//去除特殊格式
            }
        }
        return new String(buffer.toString().toUpperCase());
    }

    private Map<String, GoldenInfoComponentDTO> buildCompareMap(List<GoldenInfoComponentDTO> goldenInfoComponentDTOS) {
        Map<String, GoldenInfoComponentDTO> componentDTOMap = new HashMap<>();
        for (GoldenInfoComponentDTO goldenInfoComponentDTO:goldenInfoComponentDTOS){
            String componentType = goldenInfoComponentDTO.getComponentType();
            componentDTOMap.put(componentType,goldenInfoComponentDTO);
        }
        return componentDTOMap;
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
                if (key.equals("Devices")){//这里的Device其实是Car
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
                    tCarline.setCarlineModelType(carlineModelType);
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

                String componentType = "MU";
                String hwVersion = importDeviceDTO.getMUHW();
                String swVersion = importDeviceDTO.getMUSW();
                buildImportComponent(carlineInfoUid, componentType, hwVersion, swVersion);

                componentType = new String("ASTERIX");
                hwVersion = new String(importDeviceDTO.getAsterixHW());
                swVersion = new String(importDeviceDTO.getAsterixSW());
                buildImportComponent(carlineInfoUid, componentType, hwVersion, swVersion);

                componentType = new String("KOMBI");
                hwVersion = new String(importDeviceDTO.getKombiHW());
                swVersion = new String(importDeviceDTO.getKombiSW());
                buildImportComponent(carlineInfoUid, componentType, hwVersion, swVersion);

                componentType = new String("GATEWAY");
                hwVersion = new String(importDeviceDTO.getGatewayHW());
                swVersion = new String(importDeviceDTO.getGatewaySW());
                buildImportComponent(carlineInfoUid, componentType, hwVersion, swVersion);

                componentType = new String("CONBOX/OCU");
                hwVersion = new String(importDeviceDTO.getConboxOCUHW());
                swVersion = new String(importDeviceDTO.getConboxOCUSW());
                buildImportComponent(carlineInfoUid, componentType, hwVersion, swVersion);
            }
        }
        return "SUCCESS!";
    }

    private void buildImportComponent(Long carlineInfoUid, String componentType, String hwVersion, String swVersion) {
        TComponentData componentData = new TComponentData();
        componentData.setComponentType(componentType);
        componentData.setComponentName(componentType);
        componentData.setIsAvaliabel(1);
        componentData.setPartNumber(null);
        componentData.setSort(0);
        TCarlineComponent tCarlineComponent = new TCarlineComponent();
        tCarlineComponent.setCarlineInfoUid(carlineInfoUid);
        if (StringUtils.isNotEmpty(hwVersion)){
            String wareType = "HW";
            componentData.setWareType("HW");
            componentData.setComponentVersion(hwVersion);
            insertComponent(tCarlineComponent, wareType, componentData);
        }
        if (StringUtils.isNotEmpty(swVersion)){
            componentData.setWareType("SW");
            String wareType = "SW";
            componentData.setComponentVersion(swVersion);
            insertComponent(tCarlineComponent, wareType, componentData);
        }
        tCarlineComponentMapper.insert(tCarlineComponent);
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
                List<TCarlineComponent> ifDeleteTCarlineSWComponents = tCarlineComponentMapper.selectList(new QueryWrapper<TCarlineComponent>()
                        .eq("sw_version_uid", tCarlineComponent.getSwVersionUid()));
                if (ifDeleteTCarlineSWComponents != null && ifDeleteTCarlineSWComponents.size() == 1) {
                    tComponentDataMapper.deleteById(ifDeleteTCarlineSWComponents.get(0).getSwVersionUid());
                }
                List<TCarlineComponent> ifDeleteTCarlineHWComponents = tCarlineComponentMapper.selectList(new QueryWrapper<TCarlineComponent>()
                        .eq("hw_version_uid", tCarlineComponent.getHwVersionUid()));
                if (ifDeleteTCarlineHWComponents != null && ifDeleteTCarlineHWComponents.size() == 1) {
                    tComponentDataMapper.deleteById(ifDeleteTCarlineHWComponents.get(0).getHwVersionUid());
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
        //deviceName不得重复
        /*QueryWrapper<TCarlineInfo> tcarlineWrapper = new QueryWrapper<>();
        tcarlineWrapper.eq("device_name", deviceInfoVo.getGoldenCarName());
        List<TCarlineInfo> tCarlineInfos = tCarlineInfoMapper.selectList(tcarlineWrapper);
        if (null != tCarlineInfos) {
            return -1;
        }*/
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
        return 1;
    }



    Map<String,String> cleanMap;

    private static void buildCarlinePO(DeviceInfoVo deviceInfoVo, TCarline tCarline) {
        tCarline.setCarlineModelType(deviceInfoVo.getCarlineModelType());
//        tCarline.setgoldenCarName(deviceInfoVo.getGoldenCarName());
        tCarline.setStatus(1);
        tCarline.setUpdateTime(new Date());
        tCarline.setIsShow(1);
    }

    private static void buildTCarlineInfoPO(DeviceInfoVo deviceInfoVo, TCarlineInfo tCarlineInfo) {
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

}



















