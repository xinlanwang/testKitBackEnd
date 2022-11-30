package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.po.TCarlineInfo;

/**
 * `虚拟设备参数`Service接口
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
public interface ITCarlineInfoService 
{
    /**
     * 查询`虚拟设备参数`
     * 
     * @param uid `虚拟设备参数`主键
     * @return `虚拟设备参数`
     */
    public TCarlineInfo selectTCarlineInfoByUid(String uid);

    /**
     * 查询`虚拟设备参数`列表
     * 
     * @param tCarlineInfo `虚拟设备参数`
     * @return `虚拟设备参数`集合
     */
    public List<TCarlineInfo> selectTCarlineInfoList(TCarlineInfo tCarlineInfo);

    /**
     * 新增`虚拟设备参数`
     * 
     * @param tCarlineInfo `虚拟设备参数`
     * @return 结果
     */
    public int insertTCarlineInfo(TCarlineInfo tCarlineInfo);

    /**
     * 修改`虚拟设备参数`
     * 
     * @param tCarlineInfo `虚拟设备参数`
     * @return 结果
     */
    public int updateTCarlineInfo(TCarlineInfo tCarlineInfo);

    /**
     * 批量删除`虚拟设备参数`
     * 
     * @param uids 需要删除的`虚拟设备参数`主键集合
     * @return 结果
     */
    public int deleteTCarlineInfoByUids(String[] uids);

    /**
     * 删除`虚拟设备参数`信息
     * 
     * @param uid `虚拟设备参数`主键
     * @return 结果
     */
    public int deleteTCarlineInfoByUid(String uid);
}
