package com.ruoyi.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TCarlineComponentMapper;
import com.ruoyi.system.domain.po.TCarlineComponent;
import com.ruoyi.system.service.ITCarlineComponentService;

/**
 * `设备-组件关联（n2n）`Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@Service
public class TCarlineComponentServiceImpl implements ITCarlineComponentService 
{
    @Autowired
    private TCarlineComponentMapper tCarlineComponentMapper;

    /**
     * 查询`设备-组件关联（n2n）`
     * 
     * @param componentUid `设备-组件关联（n2n）`主键
     * @return `设备-组件关联（n2n）`
     */
    @Override
    public TCarlineComponent selectTCarlineComponentByComponentUid(String componentUid)
    {
        return tCarlineComponentMapper.selectTCarlineComponentByComponentUid(componentUid);
    }

    /**
     * 查询`设备-组件关联（n2n）`列表
     * 
     * @param tCarlineComponent `设备-组件关联（n2n）`
     * @return `设备-组件关联（n2n）`
     */
    @Override
    public List<TCarlineComponent> selectTCarlineComponentList(TCarlineComponent tCarlineComponent)
    {
        return tCarlineComponentMapper.selectTCarlineComponentList(tCarlineComponent);
    }

    /**
     * 新增`设备-组件关联（n2n）`
     * 
     * @param tCarlineComponent `设备-组件关联（n2n）`
     * @return 结果
     */
    @Override
    public int insertTCarlineComponent(TCarlineComponent tCarlineComponent)
    {
//        tCarlineComponent.setCreateTime(DateUtils.getNowDate());
        return tCarlineComponentMapper.insertTCarlineComponent(tCarlineComponent);
    }

    /**
     * 修改`设备-组件关联（n2n）`
     * 
     * @param tCarlineComponent `设备-组件关联（n2n）`
     * @return 结果
     */
    @Override
    public int updateTCarlineComponent(TCarlineComponent tCarlineComponent)
    {
//        tCarlineComponent.setUpdateTime(DateUtils.getNowDate());
        return tCarlineComponentMapper.updateTCarlineComponent(tCarlineComponent);
    }

    /**
     * 批量删除`设备-组件关联（n2n）`
     * 
     * @param componentUids 需要删除的`设备-组件关联（n2n）`主键
     * @return 结果
     */
    @Override
    public int deleteTCarlineComponentByComponentUids(String[] componentUids)
    {
        return tCarlineComponentMapper.deleteTCarlineComponentByComponentUids(componentUids);
    }

    /**
     * 删除`设备-组件关联（n2n）`信息
     * 
     * @param componentUid `设备-组件关联（n2n）`主键
     * @return 结果
     */
    @Override
    public int deleteTCarlineComponentByComponentUid(String componentUid)
    {
        return tCarlineComponentMapper.deleteTCarlineComponentByComponentUid(componentUid);
    }
}
