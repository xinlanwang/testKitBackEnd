package com.ruoyi.system.service.impl;

import java.util.*;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.reflect.ReflectUtils;
import com.ruoyi.system.domain.dto.ImportMatrixDTO;
import com.ruoyi.system.domain.dto.ImportValidateDTO;
import com.ruoyi.system.domain.enums.MatrixSelectMapping;
import com.ruoyi.system.domain.param.MatrixMappingParam;
import com.ruoyi.system.domain.po.TMatrix;
import com.ruoyi.system.mapper.SysDictDataMapper;
import io.netty.util.internal.StringUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TMatrixMapper;
import com.ruoyi.system.service.ITMatrixService;
import org.springframework.util.CollectionUtils;

import static com.ruoyi.common.constant.TestKitConstants.*;

/**
 * `BIGINT(32)`Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-21
 */
@Service
public class TMatrixServiceImpl implements ITMatrixService 
{
    @Autowired
    private TMatrixMapper tMatrixMapper;
    @Autowired
    private SysDictDataMapper dictDataMapper;
    @Autowired
    private SysDictDataServiceImpl sysDictDataService;


    @Override
    public List<TMatrix> selectMatrixList( String matrixType) {
        QueryWrapper<TMatrix> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("matrix_type",matrixType);
        //一种规则是MatrixTab里的，另外一种规则是在Validate里的，因为只有三个字段不另外建表，逻辑相似，功能区相同故都放Matrix表里
        queryWrapper.orderByDesc("update_time");
        List<TMatrix> tMatrixList = tMatrixMapper.selectList(queryWrapper);
        return tMatrixList;
    }

    @Override
    public AjaxResult selectMatrixMappingVlalue(MatrixMappingParam matrixListParam) {
        QueryWrapper<TMatrix> queryWrapper = new QueryWrapper<>();
        String selectProperty = matrixListParam.getSelectProperty();
        if (StringUtil.isNullOrEmpty(selectProperty)){
            return AjaxResult.error("取参不得为空");
        }
        if (null == MatrixSelectMapping.valueOf(selectProperty)){
            return AjaxResult.error("请在指定参数列表里进行选择");
        }
        queryWrapper.select("distinct " + MatrixSelectMapping.valueOf(selectProperty).getName());
        if (matrixListParam.getGoldenCarTypes() != null && matrixListParam.getGoldenCarTypes().length > 0) {
            queryWrapper.in(MatrixSelectMapping.goldenCarTypes.getName(), matrixListParam.getGoldenCarTypes());
        }
        if (matrixListParam.getGoldenClusterNameTypes() != null && matrixListParam.getGoldenClusterNameTypes().length > 0) {
            queryWrapper.in(MatrixSelectMapping.goldenClusterNameTypes.getName(), matrixListParam.getGoldenClusterNameTypes());
        }
        if (matrixListParam.getClusterNames() != null && matrixListParam.getClusterNames().length > 0) {
            queryWrapper.in(MatrixSelectMapping.clusterNames.getName(), matrixListParam.getClusterNames());
        }
        if (matrixListParam.getPlatformTypes() != null && matrixListParam.getPlatformTypes().length > 0) {
            queryWrapper.in(MatrixSelectMapping.platformTypes.getName(), matrixListParam.getPlatformTypes());
        }
        if (matrixListParam.getProjectTypes() != null && matrixListParam.getProjectTypes().length > 0) {
            queryWrapper.in(MatrixSelectMapping.projectTypes.getName(), matrixListParam.getProjectTypes());
        }
        if (matrixListParam.getVariantTypes() != null && matrixListParam.getVariantTypes().length > 0) {
            queryWrapper.in(MatrixSelectMapping.variantTypes.getName(), matrixListParam.getVariantTypes());
        }
        if (matrixListParam.getCarlineModelTypes() != null && matrixListParam.getCarlineModelTypes().length > 0) {
            queryWrapper.in(MatrixSelectMapping.carlineModelTypes.getName(), matrixListParam.getCarlineModelTypes());
        }

        if (matrixListParam.getOcuCboxTypes() != null && matrixListParam.getOcuCboxTypes().length > 0) {
            queryWrapper.in(MatrixSelectMapping.ocuCboxTypes.getName(), matrixListParam.getOcuCboxTypes());
        }
        if (matrixListParam.getGatewayTypes() != null && matrixListParam.getGatewayTypes().length > 0) {
            queryWrapper.in(MatrixSelectMapping.gatewayTypes.getName(), matrixListParam.getGatewayTypes());
        }
        if (matrixListParam.getMarketTypes() != null && matrixListParam.getMarketTypes().length > 0) {
            Map<String,String> marketMap = new HashMap();
            for (String marketType : matrixListParam.getMarketTypes()) {
                if (StringUtils.isNotEmpty(marketType)) {
                    marketMap.put(marketType, "exist");
                }
            }
            List<Long> tMatrixUids = new ArrayList<>();
            for (TMatrix tMatrix : tMatrixMapper.selectList(new QueryWrapper<TMatrix>())) {
                if (StringUtils.isNotEmpty(tMatrix.getMarketTypes())){
                    String matrixTypes = tMatrix.getMarketTypes();
                    for (String str : matrixTypes.split(",")) {
                        if ("exist".equals(marketMap.get(str))){
                            tMatrixUids.add(tMatrix.getUid());
                            continue;
                        }
                    }
                }
            }
            Long[] uids = tMatrixUids.toArray(new Long[(tMatrixUids.size())]);
            queryWrapper.in("uid", uids);
        }
        List<TMatrix> tMatrixList = tMatrixMapper.selectList(queryWrapper);


        //
        if (CollectionUtils.isEmpty(tMatrixList)){
//            return AjaxResult.warn("没有对应取值");
            return AjaxResult.success(new ArrayList<>());
        }
        List<String> selectValues = new ArrayList<>();
        for (TMatrix tMatrix:tMatrixList){
            Object o = ReflectUtils.invokeGetter(tMatrix, MatrixSelectMapping.valueOf(selectProperty).getSingleName());
            if (o != null){
                selectValues.add(o.toString());
            }
        }
        if ("marketTypes".equals(matrixListParam.getSelectProperty()) && selectValues != null && selectValues.size() > 0){
            Map<String,String> mergemarket = new HashMap<>();
            List<String> tempSelectValues = new ArrayList<>();
            for (String str:selectValues){
                for (String s : str.split(",")) {
                    if (StringUtils.isNotEmpty(s) && mergemarket.get(s) == null){
                        tempSelectValues.add(s);
                    }
                    mergemarket.put(s,"merge");

                }
            }
            selectValues = tempSelectValues;
        }
        AjaxResult ajax = AjaxResult.success(selectValues);

        return ajax;
    }

    @Override
    public String importMatrix(List<ImportMatrixDTO> importMatrixDTOS, boolean b, String operName) {
        if (CollectionUtils.isEmpty(importMatrixDTOS)){
            return "导入文件为空";
        }
        String[] dictTypes = new String[] {"platformType" , "marketType" , "clusterName" , "projectType" , "variantType" , "carlineModelType" , "ocuCboxType" , "gatewayType"};
        Map<String, Map<String,String>> dictMap = getDictMap(dictTypes);

        //清除
        tMatrixMapper.delete(new QueryWrapper<TMatrix>().eq("matrix_type",MATRIXTYPE_MATRIX));//规则清除
        //build
        List<TMatrix> saveMatrix = new ArrayList<>();
        for (ImportMatrixDTO importMatrixDTO:importMatrixDTOS){
            if (StringUtil.isNullOrEmpty(importMatrixDTO.getClusterName())){
                continue;
            }
            if (!StringUtil.isNullOrEmpty(importMatrixDTO.getMarketType()) || importMatrixDTO.getMarketType().toUpperCase().split(";").length > 1){
                TMatrix newMatrixDO = new TMatrix();
                buildNewMatrixDO(dictMap, importMatrixDTO, newMatrixDO,dictTypes,MATRIXTYPE_MATRIX);
                StringBuffer marketTypeBuffer = new StringBuffer();
                for (String marketType : importMatrixDTO.getMarketType().toUpperCase().split(";")) {// 存在：CN;TW;KR;JP;HK
                    String marketTypeValue = getDictValue("marketType", dictMap, marketType,MATRIXTYPE_MATRIX);
                    marketTypeBuffer.append(marketTypeValue + ",");
                }
                newMatrixDO.setMarketTypes(marketTypeBuffer.deleteCharAt(marketTypeBuffer.length() - 1).toString());
                newMatrixDO.setMatrixType(MATRIXTYPE_MATRIX);
                saveMatrix.add(newMatrixDO);
            }else {
                TMatrix newMatrixDO = new TMatrix();
                buildNewMatrixDO(dictMap, importMatrixDTO, newMatrixDO,dictTypes,MATRIXTYPE_MATRIX);
                String marketTypeValue = getDictValue("marketType", dictMap, importMatrixDTO.getMarketType(),MATRIXTYPE_MATRIX);
                newMatrixDO.setMarketTypes(marketTypeValue);
                newMatrixDO.setMatrixType(MATRIXTYPE_MATRIX);
                saveMatrix.add(newMatrixDO);
            }
        }
        for (TMatrix tMatrix:saveMatrix){
            tMatrixMapper.insert(tMatrix);
        }
        return "导入成功";
    }

    @Override
    public String imporValidate(List<ImportValidateDTO> importValidateDTOS, boolean b, String operName) {
        if (CollectionUtils.isEmpty(importValidateDTOS)){
            return "导入文件为空";
        }
        String[] dictTypes = new String[] {"clusterName", "projectType","platformType","marketType","functionGroupType",
                "variantType","taskType","carlineModelType","goldenCarType","goldenClusterNameType"};
        Map<String, Map<String,String>> dictMap = getDictMap(dictTypes);
        //清除
        tMatrixMapper.delete(new QueryWrapper<TMatrix>().eq("matrix_type",MATRIXTYPE_VALIDATE));
        dictDataMapper.logicDelectDictDataByMatrixType(DICT_MATRIXTYPE_VALIDATE);//字典清除

        //作为字典导入
        for (ImportValidateDTO importValidateDTO:importValidateDTOS){
            TMatrix newMatrixDO = new TMatrix();
            buildNewMatrixDO(dictMap, importValidateDTO, newMatrixDO,dictTypes, DICT_MATRIXTYPE_VALIDATE);
        }

        //作为规则导入
        List<TMatrix> saveMatrix = new ArrayList<>();
        for (ImportValidateDTO importValidateDTO:importValidateDTOS){
            TMatrix newMatrixDO = new TMatrix();
            if (importValidateDTO == null || StringUtil.isNullOrEmpty(importValidateDTO.getCarlineModelType())){
                continue;
            }
            String[] subDictTypes = new String[] {"carlineModelType","goldenCarType","goldenClusterNameType"};
            buildNewMatrixDO(dictMap, importValidateDTO, newMatrixDO,subDictTypes,MATRIXTYPE_VALIDATE);
            newMatrixDO.setMatrixType(MATRIXTYPE_VALIDATE);
            saveMatrix.add(newMatrixDO);
        }
        for (TMatrix tMatrix:saveMatrix){
            tMatrixMapper.insert(tMatrix);
        }
        return "导入成功";
    }

    /**
     * build
     * @param dictMap
     * @param <></>
     * @param newMatrixDO
     */
    private void buildNewMatrixDO(Map<String, Map<String, String>> dictMap, Object entity, TMatrix newMatrixDO,String[] dictTypes,String matrixType) {
        for (String dictType:dictTypes) {
            if ("marketType".equals(dictType) && MATRIXTYPE_MATRIX.equals(matrixType)){
                continue;
            }
            Object getter = ReflectUtils.invokeGetter(entity, dictType);
            String getterStr = null;
            if (getter != null){
                getterStr = getter.toString().toUpperCase();
            }
            String dictValue = getDictValue(dictType, dictMap,getterStr,matrixType);
            ReflectUtils.invokeSetter(newMatrixDO,dictType,dictValue);
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
    private String getDictValue(String dictTypeName, Map<String, Map<String, String>> dictMap, String dictLabel, String matrixType) {
        String dictValue;
        Map<String, String> dictLabelMap = dictMap.get(dictTypeName);
        if (StringUtil.isNullOrEmpty(dictLabel)) {
            return null;
        }
        if (dictLabelMap == null) {
            dictLabelMap = new HashMap<String, String>();
        }
        if (!("carlineModelType".equals(dictTypeName) ||"goldenCarType".equals(dictTypeName))){
            dictLabel = StringUtils.getCleanStr(dictLabel);
        }else {
            dictLabel = dictLabel.toUpperCase();
        }
        //假设加入的值字典中不存在
        if (StringUtil.isNullOrEmpty(dictLabelMap.get(dictLabel))) {
            SysDictData sysDictData = new SysDictData();
            sysDictData.setDictType(dictTypeName);
            sysDictData.setMatrixType(matrixType);
            sysDictData.setStatus(DICT_STATUS_NORMAL);
            sysDictData.setDictLabel(dictLabel);
            Integer dictValueNum;
            if (dictDataMapper.selectMaxDictType(dictTypeName) != null) {
                dictValueNum = dictDataMapper.selectMaxDictType(dictTypeName) + 1;
            } else {
                dictValueNum = 1;
            }
            dictValue = dictValueNum.toString();
            sysDictData.setDictValue(dictValue);
            dictDataMapper.insert(sysDictData);
//            sysDictDataService.insertMatrixDictData(sysDictData);
            dictLabelMap.put(dictLabel, sysDictData.getDictValue());
            dictMap.put(dictTypeName, dictLabelMap);
        } else {
            //假如存在则刷为将状态刷为0
            dictValue = dictLabelMap.get(dictLabel);
            dictDataMapper.updateDictDataStatus(dictTypeName, dictLabel, dictValue, "0",matrixType);
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
     * 查询`BIGINT(32)`
     * 
     * @param uid `BIGINT(32)`主键
     * @return `BIGINT(32)`
     */
    @Override
    public TMatrix selectTMatrixByUid(Long uid)
    {
        return tMatrixMapper.selectTMatrixByUid(uid);
    }

    /**
     * 查询`BIGINT(32)`列表
     * 
     * @param tMatrix `BIGINT(32)`
     * @return `BIGINT(32)`
     */
    @Override
    public List<TMatrix> selectTMatrixList(TMatrix tMatrix)
    {
        return tMatrixMapper.selectTMatrixList(tMatrix);
    }

    /**
     * 新增`BIGINT(32)`
     * 
     * @param tMatrix `BIGINT(32)`
     * @return 结果
     */
    @Override
    public int insertTMatrix(TMatrix tMatrix)
    {
        tMatrix.setCreateTime(DateUtils.getNowDate());
        return tMatrixMapper.insertTMatrix(tMatrix);
    }

    /**
     * 修改`BIGINT(32)`
     * 
     * @param tMatrix `BIGINT(32)`
     * @return 结果
     */
    @Override
    public int updateTMatrix(TMatrix tMatrix)
    {
        //查重
        /*
        market_types
        */
        QueryWrapper<TMatrix> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(tMatrix.getMatrixType())){
            queryWrapper.eq("matrix_type",tMatrix.getMatrixType());
        }
        if (StringUtils.isNotEmpty(tMatrix.getGoldenCarType())){
            queryWrapper.eq("golden_car_type",tMatrix.getGoldenCarType());
        }
        if (StringUtils.isNotEmpty(tMatrix.getGoldenClusterNameType())){
            queryWrapper.eq("golden_cluster_name_type",tMatrix.getGoldenClusterNameType());
        }
        if (StringUtils.isNotEmpty(tMatrix.getCarlineModelType())){
            queryWrapper.eq("carline_model_type",tMatrix.getCarlineModelType());
        }
        if (StringUtils.isNotEmpty(tMatrix.getClusterName())){
            queryWrapper.eq("cluster_name",tMatrix.getClusterName());
        }
        if (StringUtils.isNotEmpty(tMatrix.getPlatformType())){
            queryWrapper.eq("platform_type",tMatrix.getPlatformType());
        }
        if (StringUtils.isNotEmpty(tMatrix.getProjectType())){
            queryWrapper.eq("project_type",tMatrix.getProjectType());
        }
        if (StringUtils.isNotEmpty(tMatrix.getVariantType())){
            queryWrapper.eq("variant_type",tMatrix.getVariantType());
        }
        if (StringUtils.isNotEmpty(tMatrix.getOcuCboxType())){
            queryWrapper.eq("ocu_cbox_type",tMatrix.getOcuCboxType());
        }
        if (StringUtils.isNotEmpty(tMatrix.getGatewayType())){
            queryWrapper.eq("gateway_type",tMatrix.getGatewayType());
        }
        List<TMatrix> tMatrixList = tMatrixMapper.selectList(queryWrapper);
        if (StringUtils.isNotEmpty(tMatrixList) && "1".equals(tMatrix.getMatrixType())){
            return -1;
        }
        if (StringUtils.isNotEmpty(tMatrixList) && "2".equals(tMatrix.getMatrixType())){
            for (TMatrix tMatrixPO:tMatrixList){
                if (tMatrixPO.getUid().equals(tMatrix.getUid())) {
                    continue;
                }
                if (StringUtils.isNotEmpty(tMatrixPO.getMarketTypes())) {
                    for (String str : tMatrixPO.getMarketTypes().split(",")) {
                        if (str.equals(tMatrix.getMatrixType())) {
                            return -1;
                        }
                    }
                } else {
                    if (StringUtils.isEmpty(tMatrix.getMarketTypes())) {
                        return -1;
                    }
                }
            }
        }
        //
        tMatrix.setUpdateTime(DateUtils.getNowDate());
        return tMatrixMapper.updateTMatrix(tMatrix);
    }

    /**
     * 批量删除`BIGINT(32)`
     * 
     * @param uids 需要删除的`BIGINT(32)`主键
     * @return 结果
     */
    @Override
    public int deleteTMatrixByUids(Long[] uids)
    {
        return tMatrixMapper.deleteTMatrixByUids(uids);
    }

    /**
     * 删除`BIGINT(32)`信息
     * 
     * @param uid `BIGINT(32)`主键
     * @return 结果
     */
    @Override
    public int deleteTMatrixByUid(Long uid)
    {
        return tMatrixMapper.deleteTMatrixByUid(uid);
    }
}
