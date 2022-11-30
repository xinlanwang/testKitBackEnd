package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.system.domain.param.BasicListParam;
import com.ruoyi.system.domain.po.TSysDictData;
import com.ruoyi.system.domain.vo.SysDictTypeVO;
import com.ruoyi.system.mapper.TSysDictDataMapper;
import com.ruoyi.system.service.ValidateAllService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典 业务层处理
 *  //未防止用户误操作影响页面回显，字典无物理删除，只有对应是否弃用，删除则视为弃用，但还能查询，下拉列表也仅能显示未被弃用的结果
 * @author ruoyi
 */
@Service
public class ValidateAllServiceImpl implements ValidateAllService
{
    @Autowired
    private TSysDictDataMapper tSysDictDataMapper;

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictTypeVO> selectDictDataList(BasicListParam dictData)
    {
        return tSysDictDataMapper.selectDictDataList(dictData);
    }

    @Override
    public List<SysDictTypeVO> selectDictTypeAll() {
        QueryWrapper<TSysDictData> wrapper = new QueryWrapper<>();
        List<SysDictTypeVO> sysDictTypeVOS = new ArrayList<>();
        for (TSysDictData tSysDictData : tSysDictDataMapper.selectList(wrapper)) {
            SysDictTypeVO sysDictTypeVO = new SysDictTypeVO();
            sysDictTypeVO.setDictTypeUid(tSysDictData.getDictTypeUid());
            sysDictTypeVO.setDictType(tSysDictData.getDictLabel());

        }

        return sysDictTypeVOS;
    }

    @Override
    public List<TSysDictData> selectDictDataByType(String dictType) {
        return null;
    }

    @Override
    public int insertDictData(TSysDictData sysDictData) {
        tSysDictDataMapper.insert(sysDictData);
        return 0;
    }

    @Override
    public int updateDictData(TSysDictData sysDictData) {
        tSysDictDataMapper.updateTSysDictData(sysDictData);
        return 0;
    }


    @Override
    public void deleteDictDataByIds(Long[] dictUids) {
        for (Long dictCode : dictUids)
        {
            TSysDictData data = selectDictDataById(dictCode);
            tSysDictDataMapper.deleteTSysDictDataByUid(dictCode.toString());
        }
    }

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictUid 字典数据ID
     * @return 字典数据
     */
    public TSysDictData selectDictDataById(Long dictUid)
    {
        return tSysDictDataMapper.selectById(dictUid);
    }
}
