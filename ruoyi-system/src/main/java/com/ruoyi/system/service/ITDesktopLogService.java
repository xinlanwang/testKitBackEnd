package com.ruoyi.system.service;

import com.ruoyi.system.domain.po.TDesktopLog;

import java.util.List;

/**
 * `BIGINT(32) auto_increment`Service接口
 * 
 * @author ruoyi
 * @date 2022-12-08
 */
public interface ITDesktopLogService 
{
    /**
     * 查询`BIGINT(32) auto_increment`
     * 
     * @param uid `BIGINT(32) auto_increment`主键
     * @return `BIGINT(32) auto_increment`
     */
    public TDesktopLog selectTDesktopLogByUid(Long uid);

    /**
     * 查询`BIGINT(32) auto_increment`列表
     * 
     * @param tDesktopLog `BIGINT(32) auto_increment`
     * @return `BIGINT(32) auto_increment`集合
     */
    public List<TDesktopLog> selectTDesktopLogList(TDesktopLog tDesktopLog);

    /**
     * 新增`BIGINT(32) auto_increment`
     * 
     * @param tDesktopLog `BIGINT(32) auto_increment`
     * @return 结果
     */
    public int insertTDesktopLog(TDesktopLog tDesktopLog);

    /**
     * 修改`BIGINT(32) auto_increment`
     * 
     * @param tDesktopLog `BIGINT(32) auto_increment`
     * @return 结果
     */
    public int updateTDesktopLog(TDesktopLog tDesktopLog);

    /**
     * 批量删除`BIGINT(32) auto_increment`
     * 
     * @param uids 需要删除的`BIGINT(32) auto_increment`主键集合
     * @return 结果
     */
    public int deleteTDesktopLogByUids(Long[] uids);

    /**
     * 删除`BIGINT(32) auto_increment`信息
     * 
     * @param uid `BIGINT(32) auto_increment`主键
     * @return 结果
     */
    public int deleteTDesktopLogByUid(Long uid);
}
