package com.ruoyi.system.service.impl;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ruoyi.common.utils.reflect.ReflectUtils;
import com.ruoyi.system.domain.po.TComponentData;
import com.ruoyi.system.domain.vo.DeviceInfoComponent;
import com.ruoyi.system.domain.vo.DeviceInfoVo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.Threads;
import com.ruoyi.system.domain.dto.ExportRecordListDTO;
import com.ruoyi.system.domain.dto.ImportPartComponentDTO;
import com.ruoyi.system.domain.enums.DeviceSelectMapping;
import com.ruoyi.system.domain.param.RecordListParam;
import com.ruoyi.system.domain.po.TDataLog;
import com.ruoyi.system.domain.po.TDesktopRecord;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.RecordService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ruoyi.common.constant.TestKitConstants.DEVICE_TYPE_BENCH;
import static com.ruoyi.common.constant.TestKitConstants.DEVICE_TYPE_CAR;

/**
 * 字典 业务层处理
 *
 * @author ruoyi
 */
@Service
public class RecordServiceImpl implements RecordService {

    private static final Logger logger = LoggerFactory.getLogger(RecordServiceImpl.class);

    @Autowired
    private TDataLogMapper desktopLogMapper;
    @Autowired
    private TDesktopRecordMapper desktopRecordMapper;
    @Autowired
    private SysDictDataMapper dictDataMapper;
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
    private TDtcReportMapper tdtcReportMapper;


    @Override
    public List list(RecordListParam recordListParam) {
        String[] dictTypestr = {"clusterName","goldenCarType","marketType","variantType","carlineModelType","projectType",
                "platformType","functionGroupType","taskType","goldenClusterNameType","ocuCboxType","gatewayType"};
        Set<String> dictTypes = new HashSet<>(Arrays.asList(dictTypestr));

        List<TDesktopRecord> tDesktopRecords = gettDesktopRecords(recordListParam, dictTypes);
        return tDesktopRecords;
    }

    private List<TDesktopRecord> gettDesktopRecords(RecordListParam recordListParam, Set<String> dictTypes) {
        List<TDesktopRecord> tDesktopRecords = new ArrayList<>();
        //single
        String singleOrder = recordListParam.getSingleOrderStr();
        if (StringUtils.isNotEmpty(singleOrder)){
            recordListParam.setDictTypeUnderLine(hump2underline(singleOrder));
            if (!dictTypes.contains(singleOrder)){
                recordListParam.setIsDictValue(false);
            }else {
                recordListParam.setIsDictValue(true);
            }
            //
            if ("deviceType".equals(singleOrder) || "clusterName".equals(singleOrder) || "projectType".equals(singleOrder)){
                recordListParam.setDictTypeTable("tc");
            }else {
                recordListParam.setDictTypeTable("tci");
            }
            //record字段
            Field[] fields = new TDesktopRecord().getClass().getDeclaredFields();
            for (Field field:fields){
                field.setAccessible(true);
                if(singleOrder.equals(field.getName())){
                    recordListParam.setDictTypeTable("tdc");
                }
            }
        }
        tDesktopRecords = desktopRecordMapper.selectRecordList(recordListParam);
        return tDesktopRecords;
    }

    public static String hump2underline(String str) {
        Pattern compile = Pattern.compile("[A-Z]");
        Matcher matcher = compile.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()) {
            matcher.appendReplacement(sb,  "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    @Override
    public List historyList(Long operationUid) {
        Map<String, Object> result = new HashMap<>();
        //根据分页
        Integer pageNum = 1;
        Integer pageSize = 10;

        QueryWrapper<TDataLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("distinct oper_time");
        queryWrapper.eq("operation_uid",operationUid);
        Page<TDataLog> page = new Page<>(pageNum, pageSize);
        Page<TDataLog> tClusterPage = desktopLogMapper.selectPage(page, queryWrapper);
        if (tClusterPage == null || tClusterPage.getSize() == 0 ){
            return new ArrayList();
        }
        Map<Date,Map<String, Object>> deskTopMaps = new HashMap();
        for (TDataLog dataLog:tClusterPage.getRecords()){
            for (TDataLog tDataLog : desktopLogMapper.selectList(new QueryWrapper<TDataLog>().eq("oper_time", dataLog.getOperTime()).eq("operation_uid", operationUid))) {
                Map<String, Object> deskTopMap = null;
                if (deskTopMaps.get(dataLog.getOperTime()) == null){
                    deskTopMap = new HashMap<>();
                }else {
                    deskTopMap = deskTopMaps.get(dataLog.getOperTime());
                }
                deskTopMap.put("operTime",  tDataLog.getOperTime());
                deskTopMap.put("operUserName", tDataLog.getOperUserName());
                deskTopMap.put("operationType", tDataLog.getOperationType());
                String operationFiled = tDataLog.getOperationFiled();
                String afterOperationValue = tDataLog.getAfterOperationValue();
                deskTopMap.put(operationFiled, afterOperationValue);
                deskTopMaps.put(tDataLog.getOperTime(),deskTopMap);
            }
        }

        List<Map<String, Object>> historyList = new ArrayList<>();
        for (Date dataIndex:deskTopMaps.keySet()){
            Map<String, Object> stringStringMap = deskTopMaps.get(dataIndex);
            historyList.add(stringStringMap);
        }
        return historyList;
    }

    @Test
    public void test23() throws ParseException {
        String fileName = "EE327_LFV3A28W3L3652105_20210909_044125PM.xml";
        fileName = fileName.replace(".xml","");
        if ("AM".equals(fileName.substring(fileName.replace(".xml","").length() - 2,fileName.length())) ||
                "PM".equals(fileName.substring(fileName.replace(".xml","").length() - 2,fileName.length())) ){
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(fileName.split("_")[fileName.split("_").length - 2]);
            stringBuffer.append(fileName.split("_")[fileName.split("_").length - 1]);
            stringBuffer.delete(stringBuffer.length() - 2,stringBuffer.length());
            Date fileDate = new SimpleDateFormat("yyyyMMddhhmmss").parse(stringBuffer.toString());
            System.out.println(fileDate);
        }else {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(fileName.split("_")[fileName.split("_").length - 2].replaceAll("\\.",""));
            stringBuffer.append(fileName.split("_")[fileName.split("_").length - 1].replace(".xml","").replaceAll("\\.",""));
            Date fileDate = new SimpleDateFormat("ddMMyyyyhhmmss").parse(stringBuffer.toString());
            System.out.println(fileDate);
        }
    }

    @Override
    public List<ExportRecordListDTO> selectFullRecordList(RecordListParam recordListParam) {
        List<ExportRecordListDTO> exportRecordListDTOS = new ArrayList<>();
        //
        String[] dictTypestr = {"clusterName","marketType","variantType","carlineModelType","projectType",
                "platformType","functionGroupType","taskType"};
        Set<String> dictTypes = new HashSet<>(Arrays.asList(dictTypestr));
        Map<String, Map<String, String>> dictMap = getDictMap(dictTypestr);

        List<TDesktopRecord> tDesktopRecords = gettDesktopRecords(recordListParam, dictTypes);

        //
        if (CollectionUtils.isEmpty(tDesktopRecords)){
            return exportRecordListDTOS;
        }
        StringBuffer carlineInfoUids = new StringBuffer();
        for (TDesktopRecord tDesktopRecord:tDesktopRecords){
            if (tDesktopRecord.getDataUid() == null){
                continue;
            }
            carlineInfoUids.append(tDesktopRecord.getDataUid() + ",");
        }
        carlineInfoUids = carlineInfoUids.delete(carlineInfoUids.length() - 1,carlineInfoUids.length());

        //
        List<DeviceInfoVo> deviceInfoVoList = tCarlineInfoMapper.queryDeviceInfos(carlineInfoUids.toString());
        List<DeviceInfoComponent> deviceInfoComponents = tCarlineInfoMapper.queryDeviceComponents(carlineInfoUids.toString());
        if (CollectionUtils.isEmpty(deviceInfoVoList) || CollectionUtils.isEmpty(deviceInfoComponents)){
            return exportRecordListDTOS;
        }
        Map<Long, Map<String, String>> componentMap = new HashMap<>();
        for (DeviceInfoComponent deviceInfoComponent:deviceInfoComponents){
            Long carlineInfoUid = deviceInfoComponent.getCarlineInfoUid();
            Map<String, String> componentTypeMap;
            if (componentMap.get(carlineInfoUid) == null){
                componentTypeMap = new HashMap<>();
            }else {
                componentTypeMap = componentMap.get(carlineInfoUid);
            }
            String componentType = deviceInfoComponent.getComponentType();
            String componentStr;
            if ("MU-ZDC".equals(componentType)){
                componentStr = "Version :" + deviceInfoComponent.getComponentVersion()
                        + ",ZDC Name :" + deviceInfoComponent.getComponentInstanceName();
            }else {
                componentStr = deviceInfoComponent.getWareType() + ":" + deviceInfoComponent.getComponentVersion();
                if (componentTypeMap.get(componentType) != null){
                    String partNumberStr = ",PN:" + deviceInfoComponent.getPartNumber();
                    componentStr = new StringBuffer(componentTypeMap.get(componentType)).append(",").append(componentStr).append(partNumberStr).toString();
                }
            }
            componentTypeMap.put(componentType,componentStr);
            componentMap.put(carlineInfoUid,componentTypeMap);
        }

        Map<Long, DeviceInfoVo> deviceInfoVoMap = new HashMap<>();
        for (DeviceInfoVo deviceInfoVo:deviceInfoVoList){
            Long carlineInfoUid = deviceInfoVo.getCarlineInfoUid();
            if (carlineInfoUid == null){
                continue;
            }
            deviceInfoVoMap.put(carlineInfoUid,deviceInfoVo);
        }

        for (TDesktopRecord desktopRecord:tDesktopRecords){
            Long carlineInfoUid = desktopRecord.getDataUid();
            if (carlineInfoUid == null){
                continue;
            }
            ExportRecordListDTO exportRecordListDTO = new ExportRecordListDTO();
            exportRecordListDTO.setOperTime(desktopRecord.getOperTime());
            exportRecordListDTO.setOperUserName(desktopRecord.getOperUserName());
            exportRecordListDTO.setFunctionGroupType(desktopRecord.getFunctionGroupType());
            exportRecordListDTO.setTaskType(desktopRecord.getTaskType());
            exportRecordListDTO.setMileacge(desktopRecord.getMileacge());
            exportRecordListDTO.setTestHour(desktopRecord.getTestHour());
            exportRecordListDTO.setLocation(desktopRecord.getLocation());
            exportRecordListDTO.setSystemReset(desktopRecord.getSystemReset());
            exportRecordListDTO.setNaviReset(desktopRecord.getNaviReset());
            exportRecordListDTO.setBlackMap(desktopRecord.getBlackMap());
            exportRecordListDTO.setInitializingOccurred(desktopRecord.getInitializingOccurred());
            exportRecordListDTO.setFallBackScreen(desktopRecord.getFallBackScreen());
            exportRecordListDTO.setBussleep(desktopRecord.getBussleep());
            exportRecordListDTO.setPlannedTicket(desktopRecord.getPlannedTicket());
            exportRecordListDTO.setComment(desktopRecord.getComment());

            //device
            if (deviceInfoVoMap.get(carlineInfoUid) != null){
                DeviceInfoVo deviceInfoVo = deviceInfoVoMap.get(carlineInfoUid);
                if (DEVICE_TYPE_BENCH.equals(deviceInfoVo.getCarlineType())){
                    exportRecordListDTO.setDeviceType("BENCH");
                }else if (DEVICE_TYPE_CAR.equals(deviceInfoVo.getCarlineType())){
                    exportRecordListDTO.setDeviceType("CAR");
                }
                exportRecordListDTO.setDeviceName(deviceInfoVo.getDeviceName());
                exportRecordListDTO.setClusterName(deviceInfoVo.getClusterName());
                exportRecordListDTO.setPlatformType(deviceInfoVo.getPlatformType());
                exportRecordListDTO.setProjectType(deviceInfoVo.getProjectType());
                exportRecordListDTO.setVariantType(deviceInfoVo.getVariantType());
                exportRecordListDTO.setCarlineModelType(deviceInfoVo.getCarlineModelType());
                exportRecordListDTO.setMarketType(deviceInfoVo.getMarketType());
                exportRecordListDTO.setDbVersion(deviceInfoVo.getDbVersion());
                exportRecordListDTO.setDataMedium(deviceInfoVo.getDbVersion());
                exportRecordListDTO.setVinCode(deviceInfoVo.getVinCode());
            }

            //dictMap
           for (String dictType:dictTypestr){
               Map<String, String> dictLabelMap = dictMap.get(dictType);
               Object dictValue = ReflectUtils.invokeGetter(exportRecordListDTO, dictType);
               if (dictValue == null){
                   continue;
               }
               for (String dictLabel : dictLabelMap.keySet()){
                   String dictValueStr = dictLabelMap.get(dictLabel);
                   if (dictValue.toString().equals(dictValueStr)){
                       ReflectUtils.invokeSetter(exportRecordListDTO,dictType,dictLabel);
                   }
               }
           }

            //component
            Map<String, String> componentTypeMap = componentMap.get(carlineInfoUid);
            if (componentTypeMap == null){
                continue;
            }

            for (String componentType:componentTypeMap.keySet()){
                if (StringUtils.isEmpty(componentType) || "DBVERSION".equals(componentType)){
                    continue;
                }
                if ("CONBOX/OCU".equals(componentType)){
                    ReflectUtils.invokeSetter(exportRecordListDTO,"CONBOXOROCU",componentTypeMap.get(componentType));
                }else if ("MU-ZDC".equals(componentType)){
                    ReflectUtils.invokeSetter(exportRecordListDTO,"MUZDC",componentTypeMap.get(componentType));
                }else {
                    ReflectUtils.invokeSetter(exportRecordListDTO,componentType,componentTypeMap.get(componentType));
                }
            }
            exportRecordListDTOS.add(exportRecordListDTO);
        }
        return exportRecordListDTOS;
    }


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
}



















