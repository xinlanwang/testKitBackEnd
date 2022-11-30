package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TSysOperLogMapper;
import com.ruoyi.system.domain.po.TSysOperLog;
import com.ruoyi.system.service.ITSysOperLogService;

/**
 * `操作日志记录`Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
@Service
public class TSysOperLogServiceImpl implements ITSysOperLogService 
{
    @Autowired
    private TSysOperLogMapper tSysOperLogMapper;

    /**
     * 查询`操作日志记录`
     * 
     * @param operId `操作日志记录`主键
     * @return `操作日志记录`
     */
    @Override
    public TSysOperLog selectTSysOperLogByOperId(Long operId)
    {
        return tSysOperLogMapper.selectTSysOperLogByOperId(operId);
    }

    /**
     * 查询`操作日志记录`列表
     * 
     * @param tSysOperLog `操作日志记录`
     * @return `操作日志记录`
     */
    @Override
    public List<TSysOperLog> selectTSysOperLogList(TSysOperLog tSysOperLog)
    {
        return tSysOperLogMapper.selectTSysOperLogList(tSysOperLog);
    }

    /**
     * 新增`操作日志记录`
     * 
     * @param tSysOperLog `操作日志记录`
     * @return 结果
     */
    @Override
    public int insertTSysOperLog(TSysOperLog tSysOperLog)
    {
        return tSysOperLogMapper.insertTSysOperLog(tSysOperLog);
    }

    /**
     * 修改`操作日志记录`
     * 
     * @param tSysOperLog `操作日志记录`
     * @return 结果
     */
    @Override
    public int updateTSysOperLog(TSysOperLog tSysOperLog)
    {
        return tSysOperLogMapper.updateTSysOperLog(tSysOperLog);
    }

    /**
     * 批量删除`操作日志记录`
     * 
     * @param operIds 需要删除的`操作日志记录`主键
     * @return 结果
     */
    @Override
    public int deleteTSysOperLogByOperIds(Long[] operIds)
    {
        return tSysOperLogMapper.deleteTSysOperLogByOperIds(operIds);
    }

    /**
     * 删除`操作日志记录`信息
     * 
     * @param operId `操作日志记录`主键
     * @return 结果
     */
    @Override
    public int deleteTSysOperLogByOperId(Long operId)
    {
        return tSysOperLogMapper.deleteTSysOperLogByOperId(operId);
    }
}
