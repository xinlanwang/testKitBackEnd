package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.po.TCluster;

/**
 * `版本管控`Service接口
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
public interface ITClusterService 
{
    /**
     * 查询`版本管控`
     * 
     * @param uid `版本管控`主键
     * @return `版本管控`
     */
    public TCluster selectTClusterByUid(String uid);

    /**
     * 查询`版本管控`列表
     * 
     * @param tCluster `版本管控`
     * @return `版本管控`集合
     */
    public List<TCluster> selectTClusterList(TCluster tCluster);

    /**
     * 新增`版本管控`
     * 
     * @param tCluster `版本管控`
     * @return 结果
     */
    public int insertTCluster(TCluster tCluster);

    /**
     * 修改`版本管控`
     * 
     * @param tCluster `版本管控`
     * @return 结果
     */
    public int updateTCluster(TCluster tCluster);

    /**
     * 批量删除`版本管控`
     * 
     * @param uids 需要删除的`版本管控`主键集合
     * @return 结果
     */
    public int deleteTClusterByUids(String[] uids);

    /**
     * 删除`版本管控`信息
     * 
     * @param uid `版本管控`主键
     * @return 结果
     */
    public int deleteTClusterByUid(String uid);
}
