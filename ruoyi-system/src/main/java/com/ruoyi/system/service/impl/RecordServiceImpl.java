package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.param.DeviceListParam;
import com.ruoyi.system.domain.po.TDataLog;
import com.ruoyi.system.domain.po.TDesktopRecord;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List historyList(Long recordUid) {
        List<TDataLog> tDataLogs = desktopLogMapper.selectList(new QueryWrapper<TDataLog>());
        if (tDataLogs == null || tDataLogs.size() == 0){
            return null;
        }
        Map<Integer,Map<String, String>> deskTopMaps = new HashMap();
        for (TDataLog tDataLog : tDataLogs){
            Integer dataIndex = tDataLog.getDataIndex();
            Map<String, String> deskTopMap;
            if (deskTopMaps.get(dataIndex) == null){
                deskTopMap = new HashMap<>();
            }else {
                deskTopMap = deskTopMaps.get(dataIndex);
            }
            deskTopMap.put("operTime", DateUtils.parseDateToStr("yyyy-MM-dd HH:MM:ss", tDataLog.getOperTime()));
            deskTopMap.put("operUserName", tDataLog.getOperUserName());
            deskTopMap.put("operationType", tDataLog.getOperationType());
            deskTopMap.put(tDataLog.getOperationFiled(), tDataLog.getAfterOperationValue());
            deskTopMaps.put(dataIndex,deskTopMap);
        }
        List<Map<String, String>> historyList = new ArrayList<>();
        for (Integer dataIndex:deskTopMaps.keySet()){
            Map<String, String> stringStringMap = deskTopMaps.get(dataIndex);
            historyList.add(stringStringMap);
        }
        return historyList;
    }
}



















