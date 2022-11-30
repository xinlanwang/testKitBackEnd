package com.ruoyi.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.param.BasicListParam;
import com.ruoyi.system.domain.po.TSysDictData;
import com.ruoyi.system.domain.vo.SysDictTypeVO;

/**
 * `字典数据`Mapper接口
 * 
 * @author ruoyi
 * @date 2022-11-04
 */

public interface TSysDictDataMapper extends BaseMapper<TSysDictData>
{


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
     * 删除`字典数据`
     * 
     * @param uid `字典数据`主键
     * @return 结果
     */
    public int deleteTSysDictDataByUid(String uid);

    /**
     * 批量删除`字典数据`
     * 
     * @param uids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTSysDictDataByUids(String[] uids);

    /**
     * 根据条件分页查询字典数据
     *
     * @param matrixDictListParam 字典数据信息
     * @return 字典数据集合信息
     */
    public List<SysDictTypeVO> selectDictDataList(BasicListParam matrixDictListParam);
}
