package com.ruoyi.system.service;

import com.ruoyi.system.domain.param.BasicListParam;
import com.ruoyi.system.domain.po.TSysDictData;
import com.ruoyi.system.domain.vo.SysDictTypeVO;

import java.util.List;

/**
 * 字典 业务层
 * 
 * @author ruoyi
 */
public interface ValidateAllService
{
    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    public List<SysDictTypeVO> selectDictDataList(BasicListParam dictData);

    public List<SysDictTypeVO> selectDictTypeAll();

    public List<TSysDictData> selectDictDataByType(String dictType);

    public int insertDictData(TSysDictData sysDictData);

    public int updateDictData(TSysDictData sysDictData);

    public void deleteDictDataByIds(Long[] dictUid);

}
