package com.ruoyi.system.service.impl;
import java.io.File;
import java.util.Date;
import java.util.List;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.dto.GoldenInfoComponentDTO;
import com.ruoyi.system.domain.dto.ImportGoldenInfoDTO;
import com.ruoyi.system.domain.dto.ImportPartComponentDTO;
import com.ruoyi.system.domain.enums.ComponentTypeMapping;
import com.ruoyi.system.domain.enums.VariantTypeMapping;
import com.ruoyi.system.domain.po.*;
import com.ruoyi.system.domain.vo.*;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.GoldenInfoService;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static cn.hutool.core.text.StrPool.C_SPACE;
import static com.ruoyi.common.constant.TestKitConstants.*;

/**
 * 字典 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class GoldenInfoServiceImpl implements GoldenInfoService
{
    @Autowired
    private TCarlineInfoMapper tCarlineInfoMapper;
    @Autowired
    private TClusterMapper tClusterMapper;
    @Autowired
    private TCarlineMapper tCarlineMapper;
    @Autowired
    private SysDictDataMapper dictDataMapper;
    @Autowired
    private SysDictDataServiceImpl sysDictDataService;
    @Autowired
    private TComponentDataMapper tComponentDataMapper;
    @Autowired
    private TCarlineComponentMapper tCarlineComponentMapper;
    private static final Logger log = LoggerFactory.getLogger(GoldenInfoServiceImpl.class);


    @Override
    public Map<String, Object> querygoldenList() {
        Map<String, Object> result = new HashMap<>();
        List<TCluster> tClusterPage = tClusterMapper.selectUniqueClusterNames();
        log.info("GoldenInfo执行，总共{}个元素",tClusterPage.size());

        //平台列表装配
        List<GoldenListVo> goldenListVos = new ArrayList<>();
        if (CollectionUtils.isEmpty(tClusterPage)){
            result.put("total",0);
            result.put("goldenListVos",goldenListVos);
            return result;
        }

        for (TCluster tCluster : tClusterPage) {
            GoldenListVo goldenListVo = new GoldenListVo();
            goldenListVo.setClusterName(tCluster.getClusterName());
            //platformList start
            Map<String,GoldenListPlatfromVO> plantForm = new HashMap<>();
            for (GoldenListPlatfromVO goldenListPlatfromVO : tCarlineInfoMapper.queryGoldenInfoList(tCluster.getClusterName())) {
                goldenListVo.setClusterUid(goldenListPlatfromVO.getClusterUid());//这里的ClusterId已经失去当时语义失效，感觉没什么实际意义，但避免出错还是放着
                Integer marketType = goldenListPlatfromVO.getMarketType();
                String carlineModelType = goldenListPlatfromVO.getCarlineModelType();
                LinkedHashSet<Integer> marketTypes = null;
                if (plantForm.get(carlineModelType) != null){
                    goldenListPlatfromVO = plantForm.get(carlineModelType);
                    marketTypes = goldenListPlatfromVO.getMarketTypes();
                }else {
                    marketTypes = new LinkedHashSet<Integer>();
                }
                marketTypes.add(marketType);
                goldenListPlatfromVO.setMarketTypes(marketTypes);
                plantForm.put(carlineModelType,goldenListPlatfromVO);
            }
            if(StringUtils.isNotEmpty(plantForm)){
                goldenListVo.setPlatfromList(plantForm);
            }
            //platformList end
            goldenListVos.add(goldenListVo);
        }

        //页码装配
        result.put("total",tClusterMapper.selectList(new QueryWrapper<TCluster>().select("distinct cluster_name").eq("device_type",DEVICE_TYPE_GOLDENCAR)).size());
        result.put("goldenListVos",goldenListVos);
        return result;
    }

    @Override
    public List<GoldenInfoVO> queryClusterInfo(String clusterName, String goldenCarName) {
        List<GoldenInfoVO> goldenInfoVOS = tCarlineInfoMapper.selectDeviceComponentsList(clusterName,goldenCarName);
        if (CollectionUtils.isEmpty(goldenInfoVOS)){
            return null;
        }
        for (GoldenInfoVO goldenInfoVO : goldenInfoVOS) {
            List<GoldenInfoComponentDTO> goldenInfoComponents = tCarlineInfoMapper.queryGoldenInfoDetail(clusterName,goldenCarName,goldenInfoVO.getMarketType());
            if (CollectionUtils.isEmpty(goldenInfoComponents)&& StringUtils.isEmpty(goldenInfoVO.getMarketType())) {
                continue;
            }
            //build
            Map<String, GoldenInfoComponentVO> partTagComponents = new HashMap<>();
            for (GoldenInfoComponentDTO goldenInfoComponent : goldenInfoComponents) {
                GoldenInfoComponentVO componentVO = null;
                if (partTagComponents.get(goldenInfoComponent.getPartNumber()) != null){
                    componentVO = partTagComponents.get(goldenInfoComponent.getPartNumber());
                }else {
                    componentVO = new GoldenInfoComponentVO();
                }
                if ("HW".equals(goldenInfoComponent.getWareType())){
                    componentVO.setHwComponentVersion(goldenInfoComponent.getComponentVersion());
                }
                if ("SW".equals(goldenInfoComponent.getWareType())){
                    componentVO.setSwComponentVersion(goldenInfoComponent.getComponentVersion());
                }
                componentVO.setComponentType(goldenInfoComponent.getComponentType());
                String partnumber = goldenInfoComponent.getPartNumber();
                if (StringUtils.isNotEmpty(partnumber)){
//                    partnumber.replaceAll("\\u00A0+", "");
                    partnumber = StrUtil.removeAll(partnumber, C_SPACE);
                }
                componentVO.setPartNumber(partnumber);
                componentVO.setClusterName(goldenInfoComponent.getTemporaryVariable());
                componentVO.setMinimalHW(goldenInfoComponent.getMinimalHW());
                partTagComponents.put(goldenInfoComponent.getPartNumber(), componentVO);
            }

            //分配
            Map<String, List<GoldenInfoComponentVO>> componentMap = new HashMap<>();
            for (GoldenInfoComponentDTO goldenInfoComponent : goldenInfoComponents) {
                GoldenInfoComponentVO newComponent = partTagComponents.get(goldenInfoComponent.getPartNumber());
                //放置
                String componentType = goldenInfoComponent.getComponentType();
                if (StringUtils.isEmpty(componentType)){
                    continue;
                }
                if (StringUtils.isNotEmpty(componentMap.get(componentType))) {
                    componentMap.get(componentType).add(newComponent);
                } else {
                    List<GoldenInfoComponentVO> newGoldenInfoComponents = new ArrayList<>();
                    newGoldenInfoComponents.add(newComponent);
                    componentMap.put(componentType, newGoldenInfoComponents);
                }
            }
            goldenInfoVO.setGoldenInfoComponentMap(componentMap);
        }
        return goldenInfoVOS;
    }

    @Override
    public Long[] selectCarlineInfoIdsByClusterName(String clusterName){
        if (StringUtils.isEmpty(clusterName)){
            return null;
        }
        return tClusterMapper.selectCarlineInfoIdsByClusterName(clusterName);
    }
    @Override
    public String importGoldenInfoDevice(List<ImportGoldenInfoDTO> importGoldenInfoDTOS, boolean b, String originalFilename) {
        if (CollectionUtils.isEmpty(importGoldenInfoDTOS)){
            return "值为空";
        }
        String[] dictTypes = new String[] {"clusterName", "projectType","platformType","marketType","functionGroupType",
                "variantType","taskType","carlineModelType","goldenCarType","goldenClusterNameType"};
        Map<String, Map<String,String>> dictMap = getDictMap(dictTypes);

        //getClusterName
        String clusterNameValue = getUnsureValue("clusterName",originalFilename, dictMap);

        Map<String,Map<String, List<ImportPartComponentDTO>>> sheetMap = new HashMap<>();
        for (ImportGoldenInfoDTO importGoldenInfoDTO:importGoldenInfoDTOS) {
            List<ImportPartComponentDTO> componentDTOS = importGoldenInfoDTO.getComponentDTOS();
            if (componentDTOS == null || componentDTOS.size() <= 3. || StringUtils.isEmpty(importGoldenInfoDTO.getMarketDetail())|| StringUtils.isEmpty(importGoldenInfoDTO.getSheetName())){
                continue;
            }
            if (sheetMap == null || sheetMap.get(importGoldenInfoDTO.getSheetName()) == null){
                Map<String, List<ImportPartComponentDTO>> regionMap = new HashMap<>();
                regionMap.put(importGoldenInfoDTO.getMarketDetail(),importGoldenInfoDTO.getComponentDTOS());
                sheetMap.put(importGoldenInfoDTO.getSheetName(),regionMap);
            }else {
                Map<String, List<ImportPartComponentDTO>> regionMap = sheetMap.get(importGoldenInfoDTO.getSheetName());
                regionMap.put(importGoldenInfoDTO.getMarketDetail(),importGoldenInfoDTO.getComponentDTOS());
                sheetMap.put(importGoldenInfoDTO.getSheetName(),regionMap);
            }
        }

        for (String sheetName:sheetMap.keySet()){
            Map<String, List<ImportPartComponentDTO>> regionMap = sheetMap.get(sheetName);
            String goldenCarTypeValue = getUnsureValue("goldenCarType",sheetName, dictMap);
            TCarline tCarline = new TCarline();
            tCarline.setGoldenCarName(goldenCarTypeValue);
            tCarlineMapper.insert(tCarline);
            for (String marketType:regionMap.keySet()){
                if (CollectionUtils.isEmpty(regionMap.get(marketType))){
                    continue;
                }
                List<ImportPartComponentDTO> importPartComponentDTOS = regionMap.get(marketType);
                String marketTypeValue = getUnsureValue("marketType",marketType, dictMap);
                TCluster tCluster = new TCluster();
                TCarlineInfo tCarlineInfo = new TCarlineInfo();
                tCluster.setDeviceType(DEVICE_TYPE_GOLDENCAR);
                tCluster.setClusterName(clusterNameValue);
                tCluster.setCreateTime(new Date());
                tCluster.setUpdateTime(new Date());
                tCarlineInfo.setMarketType(marketTypeValue);
                tCluster.setCarlineUid(tCarline.getUid());
                tClusterMapper.insert(tCluster);
                ImportPartComponentDTO firstRegionDTO = regionMap.get(marketType).get(0);
                if (StringUtils.isNotEmpty(firstRegionDTO.getDBVERSION())) {
                    String dbVersion = firstRegionDTO.getDBVERSION();
                    tCarlineInfo.setDbVersion(dbVersion);
                }
                if (StringUtils.isNotEmpty(firstRegionDTO.getNOTESCOMMENTS())) {
                    String notesComments = firstRegionDTO.getNOTESCOMMENTS();
                    tCarlineInfo.setResp(notesComments);
                }
                tCarlineInfo.setClusterUid(tCluster.getUid());
                tCarlineInfo.setBasicType(BASIC_TYPE_NORMAL);
                tCarlineInfo.setCreateTime(new Date());
                tCarlineInfo.setUpdateTime(new Date());
                tCarlineInfoMapper.insert(tCarlineInfo);
                Long carlineInfoUid = tCarlineInfo.getCarlineInfoUid();
                for (ImportPartComponentDTO importPartComponentDTO:importPartComponentDTOS) {
                    if (importPartComponentDTO == null) {
                        continue;
                    }
                    if (StringUtils.isNotEmpty(importPartComponentDTO.getPARTNUMBER()) || StringUtils.isNotEmpty(importPartComponentDTO.getHWVERSION()) || StringUtils.isNotEmpty(importPartComponentDTO.getSOPCARLINE())
                            || StringUtils.isNotEmpty(importPartComponentDTO.getSWVERSION()) || StringUtils.isNotEmpty(importPartComponentDTO.getMINIMALHW())) {
                    TComponentData componentData = new TComponentData();
                    TCarlineComponent deviceInfoComponent = new TCarlineComponent();
                    componentData.setComponentName(importPartComponentDTO.getCOMPONENTS());
                    componentData.setComponentType(getComponentType(importPartComponentDTO.getCOMPONENTS()));
                    String partnumber = importPartComponentDTO.getPARTNUMBER();
                    if (StringUtils.isNotEmpty(partnumber)){
//                        partnumber.replaceAll("\\u00A0+", "");
                        partnumber = StrUtil.removeAll(partnumber, C_SPACE);
                    }
                    componentData.setPartNumber(partnumber);
                    deviceInfoComponent.setMinimalHw(importPartComponentDTO.getMINIMALHW());
                    String temporaryVariable = null;
                    if (StringUtils.isNotEmpty(importPartComponentDTO.getSOPCARLINE())) {
                        temporaryVariable = new String(importPartComponentDTO.getSOPCARLINE());
                    } else {
                        if (StringUtils.isNotEmpty(importPartComponentDTO.getSOP())) {
                            temporaryVariable = new String(importPartComponentDTO.getSOPCARLINE());
                        }
                        if (StringUtils.isNotEmpty(importPartComponentDTO.getCARLINE())) {
                            temporaryVariable = new String(importPartComponentDTO.getCARLINE());
                        }
                    }
                    deviceInfoComponent.setTemporaryVariable(temporaryVariable);
                    //TCarlineComponent
                    deviceInfoComponent.setCarlineInfoUid(carlineInfoUid);
                    deviceInfoComponent.setMinimalHw(importPartComponentDTO.getMINIMALHW());
                    deviceInfoComponent.setVariantType(getVariantType(importPartComponentDTO.getCOMPONENTS(),dictMap));

                    String wareType = "HW";
                    componentData.setWareType(wareType);
                    if (StringUtils.isNotEmpty(importPartComponentDTO.getHWVERSION())){
                        componentData.setComponentVersion(importPartComponentDTO.getHWVERSION());
                    }else {
                        componentData.setComponentVersion("");
                    }
                    insertComponent(deviceInfoComponent, wareType, componentData);

                    //SW
                    wareType = "SW";
                    componentData.setWareType(wareType);
                    if (StringUtils.isNotEmpty(importPartComponentDTO.getSWVERSION())){
                        componentData.setComponentVersion(importPartComponentDTO.getSWVERSION());
                    }else {
                        componentData.setComponentVersion("");
                    }
                    insertComponent(deviceInfoComponent, wareType, componentData);

                    tCarlineComponentMapper.insert(deviceInfoComponent);
                }
                    }
            }
        }

        return null;
    }

    @Override
    public String refreshGoldenByCarlineInfoUid(String clusterName) {
        return tClusterMapper.selectClusterName(clusterName).replace("CLU","CL");
    }

    @Override
    public Map<String, File> getAutoImportGoldenFildMap(){
        Map<String, File> goldenMaxDateFileMap = getGoldenMaxDateFileMap();
        if (goldenMaxDateFileMap == null){
            return null;
        }
        Map<String, File> autoImportGoldenFileMap = new HashMap<>();
        for (String fileName : goldenMaxDateFileMap.keySet()){
            String[] strs = fileName.split(" ");
            if (StringUtils.isNotEmpty(strs) && strs.length > 2){
                String clusterName = strs[0];
                if (null == autoImportGoldenFileMap.get(clusterName)){
                    autoImportGoldenFileMap.put(clusterName,goldenMaxDateFileMap.get(fileName));
                }else{
                    Double goldenNum = getGoldenCWNumByFileName(fileName);
                    Double contantMapNum = getGoldenCWNumByFileName(autoImportGoldenFileMap.get(clusterName).getName());
                    if (contantMapNum < goldenNum){
                        autoImportGoldenFileMap.put(clusterName,goldenMaxDateFileMap.get(fileName));
                    }
                }
            }
        }
        return autoImportGoldenFileMap;
    }

    private Double getGoldenCWNumByFileName(String fileName) {
        String str = fileName.toUpperCase().replace(".XLSX","");
        try {
            if (str.contains("_")){
                str = str.split("_")[str.split("_").length - 1].toUpperCase();
                if (str.contains("CW")){
                    str = str.replace("CW.","");
                    str = str.replace("CW","");
                }
                Double cwVersionNum = Double.valueOf(str);
                return cwVersionNum;
            }
        } catch (Exception e) {
            log.error("解析错误，该字符串为：{}",fileName);
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, File> getGoldenMaxDateFileMap() {
        File[] files =  new File(AUTO_IMPORT_GOLDEN_PATH).listFiles();
        if (files == null || files.length == 0){
            return null;
        }
        Integer maxDateNum = 0;
        String maxDateFileName = null;
        for (File subfile:files){
            if (subfile.isDirectory()){
                String name = subfile.getName();
                name = new StringBuffer().append(name.substring(name.length() - 2))
                        .append(name.substring(name.length() - 4, name.length() - 2)).toString();
                if (maxDateNum < Integer.valueOf(name)){
                    maxDateNum = Integer.valueOf(name);
                    maxDateFileName = subfile.getName();
                }
            }
        }
        List<File> allfiles = FileUtil.loopFiles(AUTO_IMPORT_GOLDEN_PATH);
        Map<String, File> fileMap = new HashMap<>();
        for (File file:allfiles){
            String substring = file.getParent().substring(file.getParent().length() - 6);
            if (maxDateFileName != null && maxDateFileName.equals(substring)){
                fileMap.put(file.getName(),file);
            }
        }
        return fileMap;
    }

    private String getComponentType(String componentName) {
        for (ComponentTypeMapping typeMapping : ComponentTypeMapping.values()) {
            if (componentName.toUpperCase().contains(typeMapping.getCode()) || componentName.equals(typeMapping.getCode())){
                return typeMapping.getName();
            }
        }
        return "-";
    }

    private String getVariantType(String componentName,Map dictMap) {
        for (VariantTypeMapping typeMapping : VariantTypeMapping.values()) {
            if (componentName.toUpperCase().contains(typeMapping.getCode()) || componentName.equals(typeMapping.getCode())){
                String variantTypeValue = getUnsureValue("variantType",typeMapping.getName(), dictMap);
                return variantTypeValue;
            }
        }
        return "-";
    }


    private void insertComponent(TCarlineComponent tCarlineComponent,String wareType, TComponentData componentData) {
        componentData.setUid(null);
        TComponentData tComponentData = null;
        /*TComponentData tComponentData = tComponentDataMapper.selectOne(new QueryWrapper<TComponentData>()
                .eq("component_type", componentData.getComponentType())
                .eq("component_name", componentData.getComponentName())
                .eq("ware_type", componentData.getWareType())
                .eq("component_version", componentData.getComponentVersion())
                .eq("part_number", componentData.getPartNumber()));*/
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
     * 查询字典值
     * @param dictTypeName 查询的字典type名
     * @param dictMap 预设的Map
     * @param dictLabel label值
     * @return
     */
    private String getUnsureValue(String dictTypeName, String dictLabel, Map<String, Map<String,String>> dictMap) {
        String dictValue;
        if (StringUtil.isNullOrEmpty(dictLabel)){
            return null;
        }
        Map<String,String> dictLabelMap = dictMap.get(dictTypeName);
        if (dictLabelMap == null){
            dictLabelMap = new HashMap<String,String>();
        }

        //匹配名称CLU后匹配字典对应字段方便映射
        if ("clusterName".equals(dictTypeName)){
            dictLabel = new String(dictLabel.split(" ")[0]);
            if (dictLabel.toUpperCase().contains("CL") && !dictLabel.toUpperCase().contains("CLU")){
                dictLabel = new String(dictLabel.replace("CL", "CLU"));
            }
        }
        if (!StringUtil.isNullOrEmpty(dictLabelMap.get(dictLabel))){
            return dictLabelMap.get(dictLabel);
        }

        //猜想匹配最近的
        String cleanOriginName = getCleanStr(dictLabel);
        if (dictLabelMap != null) {
            for (String dictLable : dictLabelMap.keySet()) {
                String cleanDictLable = getCleanStr(dictLable);
                if (cleanOriginName.contains(cleanDictLable)  || cleanDictLable.equals(cleanOriginName)) {
                    return dictLabelMap.get(dictLable);
                }
            }
        }
        //假设加入的值字典中不存在
        SysDictData sysDictData = new SysDictData();
        sysDictData.setDictType(dictTypeName);
        sysDictData.setMatrixType(DICT_MATRIXTYPE_NON);
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
        dictDataMapper.insert(sysDictData);
//        sysDictDataService.insertMatrixDictData(sysDictData);
        dictLabelMap.put(cleanOriginName,sysDictData.getDictValue());
        dictMap.put(dictTypeName,dictLabelMap);
        return dictValue;
    }

    private static String getCleanStr(String dictLabel) {
        char[] chars = dictLabel.toCharArray();
        StringBuffer buffer=new StringBuffer();
        for(int i = 0; i < chars.length; i ++) {
            if((chars[i] >= 19968 && chars[i] <= 40869) || (chars[i] >= 97 && chars[i] <= 122)|| (chars[i] >= 48 && chars[i] <= 57) || (chars[i] >= 65 && chars[i] <= 90)) {
                buffer.append(chars[i]);//去除特殊格式
            }
        }
        String cleanOriginName = new String(buffer.toString().toUpperCase());
        return cleanOriginName;
    }

}
