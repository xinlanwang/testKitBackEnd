package com.ruoyi.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.po.TSysDictType;

/**
 * `字典类型`Mapper接口
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
public interface TSysDictTypeMapper extends BaseMapper<TSysDictType>
{
    /**
     * 查询`字典类型`
     * 
     * @param uid `字典类型`主键
     * @return `字典类型`
     */
    public TSysDictType selectTSysDictTypeByUid(String uid);

    /**
     * 查询`字典类型`列表
     * 
     * @param tSysDictType `字典类型`
     * @return `字典类型`集合
     */
    public List<TSysDictType> selectTSysDictTypeList(TSysDictType tSysDictType);

    /**
     * 新增`字典类型`
     * 
     * @param tSysDictType `字典类型`
     * @return 结果
     */
    public int insertTSysDictType(TSysDictType tSysDictType);

    /**
     * 修改`字典类型`
     * 
     * @param tSysDictType `字典类型`
     * @return 结果
     */
    public int updateTSysDictType(TSysDictType tSysDictType);

    /**
     * 删除`字典类型`
     * 
     * @param uid `字典类型`主键
     * @return 结果
     */
    public int deleteTSysDictTypeByUid(String uid);

    /**
     * 批量删除`字典类型`
     * 
     * @param uids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTSysDictTypeByUids(String[] uids);
}
