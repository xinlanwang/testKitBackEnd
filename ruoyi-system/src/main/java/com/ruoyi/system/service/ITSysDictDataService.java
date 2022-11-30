package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.po.TSysDictData;

/**
 * `字典数据`Service接口
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
public interface ITSysDictDataService 
{
    /**
     * 查询`字典数据`
     * 
     * @param uid `字典数据`主键
     * @return `字典数据`
     */
    public TSysDictData selectTSysDictDataByUid(String uid);

    /**
     * 查询`字典数据`列表
     * 
     * @param tSysDictData `字典数据`
     * @return `字典数据`集合
     */
    public List<TSysDictData> selectTSysDictDataList(TSysDictData tSysDictData);

    /**
     * 新增`字典数据`
     * 
     * @param tSysDictData `字典数据`
     * @return 结果
     */
    public int insertTSysDictData(TSysDictData tSysDictData);

    /**
     * 修改`字典数据`
     * 
     * @param tSysDictData `字典数据`
     * @return 结果
     */
    public int updateTSysDictData(TSysDictData tSysDictData);

    /**
     * 批量删除`字典数据`
     * 
     * @param uids 需要删除的`字典数据`主键集合
     * @return 结果
     */
    public int deleteTSysDictDataByUids(String[] uids);

    /**
     * 删除`字典数据`信息
     * 
     * @param uid `字典数据`主键
     * @return 结果
     */
    public int deleteTSysDictDataByUid(String uid);
}
