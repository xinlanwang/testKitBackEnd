package com.ruoyi.system.service;

import com.ruoyi.system.domain.po.TDataLog;

import java.util.List;

/**
 * `BIGINT(32) auto_increment`Service接口
 * 
 * @author ruoyi
 * @date 2022-12-08
 */
public interface ITDataLogService 
{
    /**
     * 查询`BIGINT(32) auto_increment`
     * 
     * @param uid `BIGINT(32) auto_increment`主键
     * @return `BIGINT(32) auto_increment`
     */
    public TDataLog selectTDataLogByUid(Long uid);

    /**
     * 查询`BIGINT(32) auto_increment`列表
     * 
     * @param tDataLog `BIGINT(32) auto_increment`
     * @return `BIGINT(32) auto_increment`集合
     */
    public List<TDataLog> selectTDataLogList(TDataLog tDataLog);

    /**
     * 新增`BIGINT(32) auto_increment`
     * 
     * @param tDataLog `BIGINT(32) auto_increment`
     * @return 结果
     */
    public int insertTDataLog(TDataLog tDataLog);

    /**
     * 修改`BIGINT(32) auto_increment`
     * 
     * @param tDataLog `BIGINT(32) auto_increment`
     * @return 结果
     */
    public int updateTDataLog(TDataLog tDataLog);

    /**
     * 批量删除`BIGINT(32) auto_increment`
     * 
     * @param uids 需要删除的`BIGINT(32) auto_increment`主键集合
     * @return 结果
     */
    public int deleteTDataLogByUids(Long[] uids);

    /**
     * 删除`BIGINT(32) auto_increment`信息
     * 
     * @param uid `BIGINT(32) auto_increment`主键
     * @return 结果
     */
    public int deleteTDataLogByUid(Long uid);
}
