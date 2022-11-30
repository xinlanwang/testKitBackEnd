package com.ruoyi.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TSysDictDataMapper;
import com.ruoyi.system.domain.po.TSysDictData;
import com.ruoyi.system.service.ITSysDictDataService;

/**
 * `字典数据`Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
@Service
public class TSysDictDataServiceImpl implements ITSysDictDataService 
{
    @Autowired
    private TSysDictDataMapper tSysDictDataMapper;

    /**
     * 查询`字典数据`
     * 
     * @param uid `字典数据`主键
     * @return `字典数据`
     */
    @Override
    public TSysDictData selectTSysDictDataByUid(String uid)
    {
        return tSysDictDataMapper.selectById(uid);
    }

    /**
     * 查询`字典数据`列表
     * 
     * @param tSysDictData `字典数据`
     * @return `字典数据`
     */
    @Override
    public List<TSysDictData> selectTSysDictDataList(TSysDictData tSysDictData)
    {
        return tSysDictDataMapper.selectTSysDictDataList(tSysDictData);
    }

    /**
     * 新增`字典数据`
     * 
     * @param tSysDictData `字典数据`
     * @return 结果
     */
    @Override
    public int insertTSysDictData(TSysDictData tSysDictData)
    {
//        tSysDictData.setCreateTime(DateUtils.getNowDate());
        return tSysDictDataMapper.insertTSysDictData(tSysDictData);
    }

    /**
     * 修改`字典数据`
     * 
     * @param tSysDictData `字典数据`
     * @return 结果
     */
    @Override
    public int updateTSysDictData(TSysDictData tSysDictData)
    {
//        tSysDictData.setUpdateTime(DateUtils.getNowDate());
        return tSysDictDataMapper.updateTSysDictData(tSysDictData);
    }

    /**
     * 批量删除`字典数据`
     * 
     * @param uids 需要删除的`字典数据`主键
     * @return 结果
     */
    @Override
    public int deleteTSysDictDataByUids(String[] uids)
    {
        return tSysDictDataMapper.deleteTSysDictDataByUids(uids);
    }

    /**
     * 删除`字典数据`信息
     * 
     * @param uid `字典数据`主键
     * @return 结果
     */
    @Override
    public int deleteTSysDictDataByUid(String uid)
    {
        return tSysDictDataMapper.deleteTSysDictDataByUid(uid);
    }
}
