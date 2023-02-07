package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.Threads;
import com.ruoyi.system.domain.enums.DeviceSelectMapping;
import com.ruoyi.system.domain.param.RecordListParam;
import com.ruoyi.system.domain.po.TDataLog;
import com.ruoyi.system.domain.po.TDesktopRecord;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.RecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private TCarlineInfoMapper carlineInfoMapper;
    @Autowired
    private SysDictDataMapper dictDataMapper;


    @Override
    public List list(RecordListParam recordListParam) {
        List<TDesktopRecord> tDesktopRecords = new ArrayList<>();

        /*for (SysDictData dictType : dictDataMapper.selectList(new QueryWrapper<SysDictData>().select("distinct dict_type"))) {
            dictTypes.add(dictType.getDictType());
        }*/
        String[] dictTypestr = {"clusterName","goldenCarType","marketType","variantType","carlineModelType","projectType",
                "platformType","functionGroupType","taskType","goldenClusterNameType","ocuCboxType","gatewayType"};
        Set<String> dictTypes = new HashSet<>(Arrays.asList(dictTypestr));



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
        logger.info("————————————————————select record begin——————————————————————————");
        tDesktopRecords = desktopRecordMapper.selectRecordList(recordListParam);
        logger.info("————————————————————select record end——————————————————————————");
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
}



















