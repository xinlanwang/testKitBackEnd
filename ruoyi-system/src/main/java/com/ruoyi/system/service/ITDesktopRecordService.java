package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.po.TDesktopRecord;

/**
 * `BIGINT(32) auto_increment`Service接口
 * 
 * @author ruoyi
 * @date 2022-12-07
 */
public interface ITDesktopRecordService 
{
    /**
     * 查询`BIGINT(32) auto_increment`
     * 
     * @param uid `BIGINT(32) auto_increment`主键
     * @return `BIGINT(32) auto_increment`
     */
    public TDesktopRecord selectTDesktopRecordByUid(Long uid);

    /**
     * 查询`BIGINT(32) auto_increment`列表
     * 
     * @param tDesktopRecord `BIGINT(32) auto_increment`
     * @return `BIGINT(32) auto_increment`集合
     */
    public List<TDesktopRecord> selectTDesktopRecordList(TDesktopRecord tDesktopRecord);

    /**
     * 新增`BIGINT(32) auto_increment`
     * 
     * @param tDesktopRecord `BIGINT(32) auto_increment`
     * @return 结果
     */
    public int insertTDesktopRecord(TDesktopRecord tDesktopRecord);

    /**
     * 修改`BIGINT(32) auto_increment`
     * 
     * @param tDesktopRecord `BIGINT(32) auto_increment`
     * @return 结果
     */
    public int updateTDesktopRecord(TDesktopRecord tDesktopRecord);

    /**
     * 批量删除`BIGINT(32) auto_increment`
     * 
     * @param uids 需要删除的`BIGINT(32) auto_increment`主键集合
     * @return 结果
     */
    public int deleteTDesktopRecordByUids(Long[] uids);

    /**
     * 删除`BIGINT(32) auto_increment`信息
     * 
     * @param uid `BIGINT(32) auto_increment`主键
     * @return 结果
     */
    public int deleteTDesktopRecordByUid(Long uid);
}
