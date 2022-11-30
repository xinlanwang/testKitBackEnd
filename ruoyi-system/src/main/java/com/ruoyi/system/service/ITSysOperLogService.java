package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.po.TSysOperLog;

/**
 * `操作日志记录`Service接口
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
public interface ITSysOperLogService 
{
    /**
     * 查询`操作日志记录`
     * 
     * @param operId `操作日志记录`主键
     * @return `操作日志记录`
     */
    public TSysOperLog selectTSysOperLogByOperId(Long operId);

    /**
     * 查询`操作日志记录`列表
     * 
     * @param tSysOperLog `操作日志记录`
     * @return `操作日志记录`集合
     */
    public List<TSysOperLog> selectTSysOperLogList(TSysOperLog tSysOperLog);

    /**
     * 新增`操作日志记录`
     * 
     * @param tSysOperLog `操作日志记录`
     * @return 结果
     */
    public int insertTSysOperLog(TSysOperLog tSysOperLog);

    /**
     * 修改`操作日志记录`
     * 
     * @param tSysOperLog `操作日志记录`
     * @return 结果
     */
    public int updateTSysOperLog(TSysOperLog tSysOperLog);

    /**
     * 批量删除`操作日志记录`
     * 
     * @param operIds 需要删除的`操作日志记录`主键集合
     * @return 结果
     */
    public int deleteTSysOperLogByOperIds(Long[] operIds);

    /**
     * 删除`操作日志记录`信息
     * 
     * @param operId `操作日志记录`主键
     * @return 结果
     */
    public int deleteTSysOperLogByOperId(Long operId);
}
