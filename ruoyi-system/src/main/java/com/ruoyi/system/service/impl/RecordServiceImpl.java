package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.param.DeviceListParam;
import com.ruoyi.system.domain.po.TCluster;
import com.ruoyi.system.domain.po.TDataLog;
import com.ruoyi.system.domain.po.TDesktopRecord;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.ruoyi.common.constant.TestKitConstants.DEVICE_TYPE_GOLDENCAR;

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


    @Override
    public List list(DeviceListParam deviceListParam) {
        List<TDesktopRecord> tDesktopRecords = desktopRecordMapper.selectList(new QueryWrapper<TDesktopRecord>());
        return tDesktopRecords;
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



















