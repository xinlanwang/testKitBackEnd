package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.system.domain.po.TDataLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TDataLogMapper;
import com.ruoyi.system.service.ITDataLogService;

/**
 * `BIGINT(32) auto_increment`Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-08
 */
@Service
public class TDataLogServiceImpl implements ITDataLogService 
{
    @Autowired
    private TDataLogMapper TDataLogMapper;

    /**
     * 查询`BIGINT(32) auto_increment`
     * 
     * @param uid `BIGINT(32) auto_increment`主键
     * @return `BIGINT(32) auto_increment`
     */
    @Override
    public TDataLog selectTDataLogByUid(Long uid)
    {
        return TDataLogMapper.selectTDataLogByUid(uid);
    }

    /**
     * 查询`BIGINT(32) auto_increment`列表
     * 
     * @param tDataLog `BIGINT(32) auto_increment`
     * @return `BIGINT(32) auto_increment`
     */
    @Override
    public List<TDataLog> selectTDataLogList(TDataLog tDataLog)
    {
        return TDataLogMapper.selectTDataLogList(tDataLog);
    }

    /**
     * 新增`BIGINT(32) auto_increment`
     * 
     * @param tDataLog `BIGINT(32) auto_increment`
     * @return 结果
     */
    @Override
    public int insertTDataLog(TDataLog tDataLog)
    {
        return TDataLogMapper.insertTDataLog(tDataLog);
    }

    /**
     * 修改`BIGINT(32) auto_increment`
     * 
     * @param tDataLog `BIGINT(32) auto_increment`
     * @return 结果
     */
    @Override
    public int updateTDataLog(TDataLog tDataLog)
    {
        return TDataLogMapper.updateTDataLog(tDataLog);
    }

    /**
     * 批量删除`BIGINT(32) auto_increment`
     * 
     * @param uids 需要删除的`BIGINT(32) auto_increment`主键
     * @return 结果
     */
    @Override
    public int deleteTDataLogByUids(Long[] uids)
    {
        return TDataLogMapper.deleteTDataLogByUids(uids);
    }

    /**
     * 删除`BIGINT(32) auto_increment`信息
     * 
     * @param uid `BIGINT(32) auto_increment`主键
     * @return 结果
     */
    @Override
    public int deleteTDataLogByUid(Long uid)
    {
        return TDataLogMapper.deleteTDataLogByUid(uid);
    }
}
