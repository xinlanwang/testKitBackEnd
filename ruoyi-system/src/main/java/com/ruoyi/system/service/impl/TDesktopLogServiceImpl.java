package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.system.domain.po.TDesktopLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TDesktopLogMapper;
import com.ruoyi.system.service.ITDesktopLogService;

/**
 * `BIGINT(32) auto_increment`Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-08
 */
@Service
public class TDesktopLogServiceImpl implements ITDesktopLogService 
{
    @Autowired
    private TDesktopLogMapper tDesktopLogMapper;

    /**
     * 查询`BIGINT(32) auto_increment`
     * 
     * @param uid `BIGINT(32) auto_increment`主键
     * @return `BIGINT(32) auto_increment`
     */
    @Override
    public TDesktopLog selectTDesktopLogByUid(Long uid)
    {
        return tDesktopLogMapper.selectTDesktopLogByUid(uid);
    }

    /**
     * 查询`BIGINT(32) auto_increment`列表
     * 
     * @param tDesktopLog `BIGINT(32) auto_increment`
     * @return `BIGINT(32) auto_increment`
     */
    @Override
    public List<TDesktopLog> selectTDesktopLogList(TDesktopLog tDesktopLog)
    {
        return tDesktopLogMapper.selectTDesktopLogList(tDesktopLog);
    }

    /**
     * 新增`BIGINT(32) auto_increment`
     * 
     * @param tDesktopLog `BIGINT(32) auto_increment`
     * @return 结果
     */
    @Override
    public int insertTDesktopLog(TDesktopLog tDesktopLog)
    {
        return tDesktopLogMapper.insertTDesktopLog(tDesktopLog);
    }

    /**
     * 修改`BIGINT(32) auto_increment`
     * 
     * @param tDesktopLog `BIGINT(32) auto_increment`
     * @return 结果
     */
    @Override
    public int updateTDesktopLog(TDesktopLog tDesktopLog)
    {
        return tDesktopLogMapper.updateTDesktopLog(tDesktopLog);
    }

    /**
     * 批量删除`BIGINT(32) auto_increment`
     * 
     * @param uids 需要删除的`BIGINT(32) auto_increment`主键
     * @return 结果
     */
    @Override
    public int deleteTDesktopLogByUids(Long[] uids)
    {
        return tDesktopLogMapper.deleteTDesktopLogByUids(uids);
    }

    /**
     * 删除`BIGINT(32) auto_increment`信息
     * 
     * @param uid `BIGINT(32) auto_increment`主键
     * @return 结果
     */
    @Override
    public int deleteTDesktopLogByUid(Long uid)
    {
        return tDesktopLogMapper.deleteTDesktopLogByUid(uid);
    }
}
