package com.ruoyi.system.service.impl;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.dto.GoldenInfoComponentDTO;
import com.ruoyi.system.domain.dto.ImportGoldenInfoDTO;
import com.ruoyi.system.domain.dto.ImportPartComponentDTO;
import com.ruoyi.system.domain.po.*;
import com.ruoyi.system.domain.vo.*;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.GoldenInfoService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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


    @Override
    public Map<String, Object> querygoldenList() {
        Map<String, Object> result = new HashMap<>();
        //根据分页
        Integer pageNum = 1;
        Integer pageSize = 10;

        QueryWrapper<TCluster> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_type",DEVICE_TYPE_GOLDENCAR);
        queryWrapper.groupBy("cluster_name");
        queryWrapper.orderByDesc("update_time");
        Page<TCluster> page = new Page<>(pageNum, pageSize);
        Page<TCluster> tClusterPage = tClusterMapper.selectPage(page, queryWrapper);

        //平台列表装配
        List<GoldenListVo> goldenListVos = new ArrayList<>();
        for (TCluster tCluster : tClusterPage.getRecords()) {
            GoldenListVo goldenListVo = new GoldenListVo();
            goldenListVo.setClusterUid(tCluster.getUid());
            goldenListVo.setClusterName(tCluster.getClusterName());
            //platformList start
            Map<String,GoldenListPlatfromVO> plantForm = new HashMap<>();
            for (GoldenListPlatfromVO goldenListPlatfromVO : tCarlineInfoMapper.queryGoldenInfoList(tCluster.getClusterName())) {
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
            goldenListVo.setPlatfromList(plantForm);
            //platformList end
            goldenListVos.add(goldenListVo);
        }


        //页码装配
        result.put("total",tClusterMapper.selectList(new QueryWrapper<TCluster>().eq("device_type",DEVICE_TYPE_GOLDENCAR).groupBy("cluster_name")).size());
        result.put("goldenListVos",goldenListVos);
        return result;
    }

    @Override
    public List<GoldenInfoVO> queryClusterInfo(String clusterName, String carlineModelType) {
        List<GoldenInfoVO> goldenInfoVOS = tCarlineInfoMapper.selectDeviceComponentsList(clusterName,carlineModelType);
        if (goldenInfoVOS == null || goldenInfoVOS.size() == 0){
            return null;
        }
        for (GoldenInfoVO goldenInfoVO : goldenInfoVOS) {
            List<GoldenInfoComponentDTO> goldenInfoComponents = tCarlineInfoMapper.queryGoldenInfoDetail(clusterName,carlineModelType,goldenInfoVO.getMarketType());
            if (goldenInfoComponents == null && goldenInfoComponents.size() == 0 && StringUtils.isEmpty(goldenInfoVO.getMarketType())) {
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
                componentVO.setHwComponentVersion(goldenInfoComponent.getHwComponentVersion());
                componentVO.setSwComponentVersion(goldenInfoComponent.getSwComponentVersion());
                componentVO.setComponentType(goldenInfoComponent.getComponentType());
                componentVO.setPartNumber(goldenInfoComponent.getPartNumber());
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
    public String importGoldenInfoDevice(List<ImportGoldenInfoDTO> importGoldenInfoDTOS, boolean b, String originalFilename) {
        if (importGoldenInfoDTOS == null || importGoldenInfoDTOS.size() == 0){
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
            if (componentDTOS == null || componentDTOS.size() == 0 || StringUtils.isEmpty(importGoldenInfoDTO.getMarketDetail())|| StringUtils.isEmpty(importGoldenInfoDTO.getSheetName())){
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
            String carlineModelTypeValue = getUnsureValue("carlineModelType",sheetName, dictMap);
            TCarline tCarline = new TCarline();
            tCarline.setCarlineName(carlineModelTypeValue);
            tCarlineMapper.insert(tCarline);
            for (String marketType:regionMap.keySet()){
                if (regionMap.get(marketType) == null || regionMap.get(marketType).size() == 0){
                    continue;
                }
                List<ImportPartComponentDTO> importPartComponentDTOS = regionMap.get(marketType);
                String marketTypeValue = getUnsureValue("marketType",marketType, dictMap);
                TCluster tCluster = new TCluster();
                TCarlineInfo tCarlineInfo = new TCarlineInfo();
                tCluster.setDeviceType("3");
                tCluster.setClusterName(clusterNameValue);
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
                tCarlineInfoMapper.insert(tCarlineInfo);
                Long carlineInfoUid = tCarlineInfo.getCarlineInfoUid();
                for (ImportPartComponentDTO importPartComponentDTO:importPartComponentDTOS) {
                    if (importPartComponentDTO == null) {
                        continue;
                    }
                    if (StringUtils.isNotEmpty(importPartComponentDTO.getPARTNUMBER()) && StringUtils.isNotEmpty(importPartComponentDTO.getHWVERSION())
                            && StringUtils.isNotEmpty(importPartComponentDTO.getSWVERSION()) && StringUtils.isNotEmpty(importPartComponentDTO.getMINIMALHW())) {
                        TComponentData tComponentData = new TComponentData();
                    tComponentData.setComponentType(importPartComponentDTO.getCOMPONENTS());
                    tComponentData.setPartNumber(importPartComponentDTO.getPARTNUMBER());
                    tComponentData.setSwVersion(importPartComponentDTO.getSWVERSION());
                    tComponentData.setHwVersion(importPartComponentDTO.getHWVERSION());
                    tComponentData.setMinimalHw(importPartComponentDTO.getMINIMALHW());
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
                    tComponentData.setTemporaryVariable(temporaryVariable);
                    //TCarlineComponent
                    TCarlineComponent carlineComponent = new TCarlineComponent();
                    carlineComponent.setCarlineInfoUid(carlineInfoUid);
                    carlineComponent.setMinimalHw(importPartComponentDTO.getMINIMALHW());
                    carlineComponent.setPartNumber(importPartComponentDTO.getPARTNUMBER());
                    TComponentData tComponentDataSW = tComponentDataMapper.selectOne(new QueryWrapper<TComponentData>()
                            .eq("sw_version", tComponentData.getSwVersion())
                            .eq("hw_version", tComponentData.getHwVersion())
                            .eq("part_number", tComponentData.getPartNumber())
                            .eq("component_type", tComponentData.getComponentType()));
                    if (tComponentDataSW != null) {
                        carlineComponent.setComponentUid(tComponentDataSW.getUid());
                        tCarlineComponentMapper.insert(carlineComponent);
                    } else {
                        tComponentDataMapper.insert(tComponentData);
                        carlineComponent.setComponentUid(tComponentData.getUid());
                        tCarlineComponentMapper.insert(carlineComponent);
                    }
                }
                    }
            }
        }

        return null;
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
        sysDictData.setMatrixType(DICT_MATRIXTYPE_OTHER);
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