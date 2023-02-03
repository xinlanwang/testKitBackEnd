package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.enums.DeviceSelectMapping;
import com.ruoyi.system.domain.param.RecordListParam;
import com.ruoyi.system.domain.po.TDataLog;
import com.ruoyi.system.domain.po.TDesktopRecord;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.RecordService;
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

    @Autowired
    private TDataLogMapper desktopLogMapper;
    @Autowired
    private TDesktopRecordMapper desktopRecordMapper;
    @Autowired
    private TCarlineInfoMapper carlineInfoMapper;


    @Override
    public List list(RecordListParam recordListParam) {
        List<TDesktopRecord> tDesktopRecords = new ArrayList<>();
        String singleOrder = recordListParam.getSingleOrderStr();
        String deviceType = recordListParam.getDeviceType();

        //wq
        QueryWrapper<TDesktopRecord> qw = new QueryWrapper<>();
        gettDesktopRecords( deviceType, qw);
        if(recordListParam.getTestDate() != null){
            qw.eq("oper_time",recordListParam.getTestDate());
        }
        if (StringUtils.isNotEmpty(recordListParam.getTester())){
            qw.eq("oper_user_name",recordListParam.getTester());
        }
        if (StringUtils.isNotEmpty(recordListParam.getFunctionGroupType())){
            qw.eq("function_group_type",recordListParam.getFunctionGroupType());
        }

        //是单项排序还是默认排序
        if (recordListParam == null || StringUtils.isEmpty(singleOrder) || recordListParam.getSingleOrderByASC() == null){
            qw.orderByDesc("oper_time");
            qw.orderByDesc("update_time");
            qw.orderByAsc("oper_user_name");
            tDesktopRecords = desktopRecordMapper.selectList(qw);
        }else {
            //1.record字段直接排序
            TDesktopRecord tDesktopRecord = new TDesktopRecord();
            Field[] fields = tDesktopRecord.getClass().getDeclaredFields();
            for (Field field:fields){
                field.setAccessible(true);
                System.out.println(field.getName());
                if(singleOrder.equals(field.getName())){
                    singleOrder = hump2underline(singleOrder);
                    if (recordListParam.getSingleOrderByASC()){
                        qw.orderByAsc(singleOrder);
                    }else {
                        qw.orderByDesc(singleOrder);
                    }
                    tDesktopRecords = desktopRecordMapper.selectList(qw);
                    return tDesktopRecords;
                }
            }


            recordListParam.setDictTypeHump(singleOrder);
            recordListParam.setDictTypeUnderLine(hump2underline(singleOrder));

            //2.非value的dbVersion与vinCode（为非字典值的）
            if ("dbVersion".equals(singleOrder) || "vincode".equals(singleOrder)){
                return desktopRecordMapper.selectListBySingleOrderNonDict(recordListParam);
            }

            //3.device value项目比较（为字典值的）
            if ("deviceType".equals(singleOrder) || "clusterName".equals(singleOrder)){
                recordListParam.setDictTypeTable("tc");
            }else {
                recordListParam.setDictTypeTable("tci");
            }
            tDesktopRecords = desktopRecordMapper.selectListBySingleOrderDict(recordListParam);
        }

        return tDesktopRecords;
    }

    private void gettDesktopRecords(String deviceType, QueryWrapper<TDesktopRecord> qw) {
        if (StringUtils.isNotEmpty(deviceType)){
            List<Long> carlineInfoUids = carlineInfoMapper.selectCarlineInfoUidByDeviceType(deviceType);
            qw.in("data_uid",carlineInfoUids.toArray(new Long[carlineInfoUids.size()]));
        }

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



















