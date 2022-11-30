package com.ruoyi.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TClusterMapper;
import com.ruoyi.system.domain.po.TCluster;
import com.ruoyi.system.service.ITClusterService;

/**
 * `版本管控`Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@Service
public class TClusterServiceImpl implements ITClusterService 
{
    @Autowired
    private TClusterMapper tClusterMapper;

    /**
     * 查询`版本管控`
     * 
     * @param uid `版本管控`主键
     * @return `版本管控`
     */
    @Override
    public TCluster selectTClusterByUid(String uid)
    {
        return tClusterMapper.selectTClusterByUid(uid);
    }

    /**
     * 查询`版本管控`列表
     * 
     * @param tCluster `版本管控`
     * @return `版本管控`
     */
    @Override
    public List<TCluster> selectTClusterList(TCluster tCluster)
    {
        return tClusterMapper.selectTClusterList(tCluster);
    }

    /**
     * 新增`版本管控`
     * 
     * @param tCluster `版本管控`
     * @return 结果
     */
    @Override
    public int insertTCluster(TCluster tCluster)
    {
//        tCluster.setCreateTime(DateUtils.getNowDate());
        return tClusterMapper.insertTCluster(tCluster);
    }

    /**
     * 修改`版本管控`
     * 
     * @param tCluster `版本管控`
     * @return 结果
     */
    @Override
    public int updateTCluster(TCluster tCluster)
    {
//        tCluster.setUpdateTime(DateUtils.getNowDate());
        return tClusterMapper.updateTCluster(tCluster);
    }

    /**
     * 批量删除`版本管控`
     * 
     * @param uids 需要删除的`版本管控`主键
     * @return 结果
     */
    @Override
    public int deleteTClusterByUids(String[] uids)
    {
        return tClusterMapper.deleteTClusterByUids(uids);
    }

    /**
     * 删除`版本管控`信息
     * 
     * @param uid `版本管控`主键
     * @return 结果
     */
    @Override
    public int deleteTClusterByUid(String uid)
    {
        return tClusterMapper.deleteTClusterByUid(uid);
    }
}
