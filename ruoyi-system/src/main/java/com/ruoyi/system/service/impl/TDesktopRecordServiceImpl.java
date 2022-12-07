package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.system.domain.po.TDesktopRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TDesktopRecordMapper;
import com.ruoyi.system.service.ITDesktopRecordService;

/**
 * `BIGINT(32) auto_increment`Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-12-07
 */
@Service
public class TDesktopRecordServiceImpl implements ITDesktopRecordService 
{
    @Autowired
    private TDesktopRecordMapper tDesktopRecordMapper;

    /**
     * 查询`BIGINT(32) auto_increment`
     * 
     * @param uid `BIGINT(32) auto_increment`主键
     * @return `BIGINT(32) auto_increment`
     */
    @Override
    public TDesktopRecord selectTDesktopRecordByUid(Long uid)
    {
        return tDesktopRecordMapper.selectTDesktopRecordByUid(uid);
    }

    /**
     * 查询`BIGINT(32) auto_increment`列表
     * 
     * @param tDesktopRecord `BIGINT(32) auto_increment`
     * @return `BIGINT(32) auto_increment`
     */
    @Override
    public List<TDesktopRecord> selectTDesktopRecordList(TDesktopRecord tDesktopRecord)
    {
        return tDesktopRecordMapper.selectTDesktopRecordList(tDesktopRecord);
    }

    /**
     * 新增`BIGINT(32) auto_increment`
     * 
     * @param tDesktopRecord `BIGINT(32) auto_increment`
     * @return 结果
     */
    @Override
    public int insertTDesktopRecord(TDesktopRecord tDesktopRecord)
    {
        return tDesktopRecordMapper.insertTDesktopRecord(tDesktopRecord);
    }

    /**
     * 修改`BIGINT(32) auto_increment`
     * 
     * @param tDesktopRecord `BIGINT(32) auto_increment`
     * @return 结果
     */
    @Override
    public int updateTDesktopRecord(TDesktopRecord tDesktopRecord)
    {
        return tDesktopRecordMapper.updateTDesktopRecord(tDesktopRecord);
    }

    /**
     * 批量删除`BIGINT(32) auto_increment`
     * 
     * @param uids 需要删除的`BIGINT(32) auto_increment`主键
     * @return 结果
     */
    @Override
    public int deleteTDesktopRecordByUids(Long[] uids)
    {
        return tDesktopRecordMapper.deleteTDesktopRecordByUids(uids);
    }

    /**
     * 删除`BIGINT(32) auto_increment`信息
     * 
     * @param uid `BIGINT(32) auto_increment`主键
     * @return 结果
     */
    @Override
    public int deleteTDesktopRecordByUid(Long uid)
    {
        return tDesktopRecordMapper.deleteTDesktopRecordByUid(uid);
    }
}
