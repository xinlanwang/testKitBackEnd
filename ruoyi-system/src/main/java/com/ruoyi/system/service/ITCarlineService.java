package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.system.domain.po.TCarline;

/**
 * `车型基本数据`Service接口
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
public interface ITCarlineService 
{

    public List queryDeviceList(TCarline tCarline);
    /**
     * 查询`车型基本数据`
     * 
     * @param uid `车型基本数据`主键
     * @return `车型基本数据`
     */
    public TCarline selectTCarlineByUid(String uid);

    /**
     * 查询`车型基本数据`列表
     * 
     * @param tCarline `车型基本数据`
     * @return `车型基本数据`集合
     */
    public List<TCarline> selectTCarlineList(TCarline tCarline);

    /**
     * 新增`车型基本数据`
     * 
     * @param tCarline `车型基本数据`
     * @return 结果
     */
    public int insertTCarline(TCarline tCarline);

    /**
     * 修改`车型基本数据`
     * 
     * @param tCarline `车型基本数据`
     * @return 结果
     */
    public int updateTCarline(TCarline tCarline);

    /**
     * 批量删除`车型基本数据`
     * 
     * @param uids 需要删除的`车型基本数据`主键集合
     * @return 结果
     */
    public int deleteTCarlineByUids(Long[] uids);

    /**
     * 删除`车型基本数据`信息
     * 
     * @param uid `车型基本数据`主键
     * @return 结果
     */
    public int deleteTCarlineByUid(String uid);
}
