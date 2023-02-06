package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.annotation.Log;
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


    @Override
    public List list(RecordListParam recordListParam) {
        List<TDesktopRecord> tDesktopRecords = new ArrayList<>();
        String singleOrder = recordListParam.getSingleOrderStr();
        recordListParam.setDictTypeHump(singleOrder);
        recordListParam.setDictTypeUnderLine(hump2underline(singleOrder));

        //是单项排序还是默认排序
        if (recordListParam == null || StringUtils.isEmpty(singleOrder) || recordListParam.getSingleOrderByASC() == null){
            logger.info("本次排序无其他参数，为默认排序");
            tDesktopRecords = desktopRecordMapper.selectDefaultRecordList(recordListParam);
        }else {
            //1.record字段直接排序
            TDesktopRecord tDesktopRecord = new TDesktopRecord();
            Field[] fields = tDesktopRecord.getClass().getDeclaredFields();
            for (Field field:fields){
                field.setAccessible(true);
                if(singleOrder.equals(field.getName())){
                    recordListParam.setSingleOrderStr(hump2underline(singleOrder));
                    logger.info("本次排序依据record里的某一字段进行排序，该字段为{}，经过驼峰转换下划线形式为{}",singleOrder,hump2underline(singleOrder));
                    tDesktopRecords = desktopRecordMapper.selectListBySingleOrderByRecord(recordListParam);
                    return tDesktopRecords;
                }
            }

            //2.非value的dbVersion与vinCode（为非字典值的）
            if ("dbVersion".equals(singleOrder) || "vincode".equals(singleOrder)){
                logger.info("本次排序依据device里非字典值的某一字段进行排序，该字段为{}，经过驼峰转换下划线形式为{}",singleOrder,hump2underline(singleOrder));
                return desktopRecordMapper.selectListBySingleOrderNonDict(recordListParam);
            }

            //3.device value项目比较（为字典值的）
            if ("deviceType".equals(singleOrder) || "clusterName".equals(singleOrder) || "projectType".equals(singleOrder)){
                recordListParam.setDictTypeTable("tc");
            }else {
                recordListParam.setDictTypeTable("tci");
            }
            logger.info("本次排序依据device里字典值中的某一字段进行排序，该字段为{}，存在表为{},经过驼峰转换下划线形式为{}",singleOrder,recordListParam.getDictTypeTable(),hump2underline(singleOrder));
            tDesktopRecords = desktopRecordMapper.selectListBySingleOrderDict(recordListParam);
        }

        return tDesktopRecords;
    }

    private void gettDesktopRecords(RecordListParam recordListParam, QueryWrapper<TDesktopRecord> qw) {
        if (StringUtils.isNotEmpty(recordListParam.getDeviceType())){
            List<Long> carlineInfoUids = carlineInfoMapper.selectCarlineInfoUidByDeviceType(recordListParam.getDeviceType());
            qw.in("data_uid",carlineInfoUids.toArray(new Long[carlineInfoUids.size()]));
        }
        if (StringUtils.isNotEmpty(recordListParam.getDeviceName())){
            List<Long> carlineInfoUids = carlineInfoMapper.selectCarlineInfoUidByDeviceName(recordListParam.getDeviceName());
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



















