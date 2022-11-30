package com.ruoyi.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TSysDictTypeMapper;
import com.ruoyi.system.domain.po.TSysDictType;
import com.ruoyi.system.service.ITSysDictTypeService;

/**
 * `字典类型`Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@Service
public class TSysDictTypeServiceImpl implements ITSysDictTypeService 
{
    @Autowired
    private TSysDictTypeMapper tSysDictTypeMapper;

    /**
     * 查询`字典类型`
     * 
     * @param uid `字典类型`主键
     * @return `字典类型`
     */
    @Override
    public TSysDictType selectTSysDictTypeByUid(String uid)
    {
        return tSysDictTypeMapper.selectTSysDictTypeByUid(uid);
    }

    /**
     * 查询`字典类型`列表
     * 
     * @param tSysDictType `字典类型`
     * @return `字典类型`
     */
    @Override
    public List<TSysDictType> selectTSysDictTypeList(TSysDictType tSysDictType)
    {
        return tSysDictTypeMapper.selectTSysDictTypeList(tSysDictType);
    }

    /**
     * 新增`字典类型`
     * 
     * @param tSysDictType `字典类型`
     * @return 结果
     */
    @Override
    public int insertTSysDictType(TSysDictType tSysDictType)
    {
//        tSysDictType.setCreateTime(DateUtils.getNowDate());
        return tSysDictTypeMapper.insertTSysDictType(tSysDictType);
    }

    /**
     * 修改`字典类型`
     * 
     * @param tSysDictType `字典类型`
     * @return 结果
     */
    @Override
    public int updateTSysDictType(TSysDictType tSysDictType)
    {
//        tSysDictType.setUpdateTime(DateUtils.getNowDate());
        return tSysDictTypeMapper.updateTSysDictType(tSysDictType);
    }

    /**
     * 批量删除`字典类型`
     * 
     * @param uids 需要删除的`字典类型`主键
     * @return 结果
     */
    @Override
    public int deleteTSysDictTypeByUids(String[] uids)
    {
        return tSysDictTypeMapper.deleteTSysDictTypeByUids(uids);
    }

    /**
     * 删除`字典类型`信息
     * 
     * @param uid `字典类型`主键
     * @return 结果
     */
    @Override
    public int deleteTSysDictTypeByUid(String uid)
    {
        return tSysDictTypeMapper.deleteTSysDictTypeByUid(uid);
    }
}
