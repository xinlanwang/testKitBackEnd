package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.po.TCarlineComponent;

/**
 * `设备-组件关联（n2n）`Service接口
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
public interface ITCarlineComponentService 
{
    /**
     * 查询`设备-组件关联（n2n）`
     * 
     * @param componentUid `设备-组件关联（n2n）`主键
     * @return `设备-组件关联（n2n）`
     */
    public TCarlineComponent selectTCarlineComponentByComponentUid(String componentUid);

    /**
     * 查询`设备-组件关联（n2n）`列表
     * 
     * @param tCarlineComponent `设备-组件关联（n2n）`
     * @return `设备-组件关联（n2n）`集合
     */
    public List<TCarlineComponent> selectTCarlineComponentList(TCarlineComponent tCarlineComponent);

    /**
     * 新增`设备-组件关联（n2n）`
     * 
     * @param tCarlineComponent `设备-组件关联（n2n）`
     * @return 结果
     */
    public int insertTCarlineComponent(TCarlineComponent tCarlineComponent);

    /**
     * 修改`设备-组件关联（n2n）`
     * 
     * @param tCarlineComponent `设备-组件关联（n2n）`
     * @return 结果
     */
    public int updateTCarlineComponent(TCarlineComponent tCarlineComponent);

    /**
     * 批量删除`设备-组件关联（n2n）`
     * 
     * @param componentUids 需要删除的`设备-组件关联（n2n）`主键集合
     * @return 结果
     */
    public int deleteTCarlineComponentByComponentUids(String[] componentUids);

    /**
     * 删除`设备-组件关联（n2n）`信息
     * 
     * @param componentUid `设备-组件关联（n2n）`主键
     * @return 结果
     */
    public int deleteTCarlineComponentByComponentUid(String componentUid);
}
