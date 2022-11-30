package com.ruoyi.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TCarlineInfoMapper;
import com.ruoyi.system.domain.po.TCarlineInfo;
import com.ruoyi.system.service.ITCarlineInfoService;

/**
 * `虚拟设备参数`Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@Service
public class TCarlineInfoServiceImpl implements ITCarlineInfoService 
{
    @Autowired
    private TCarlineInfoMapper tCarlineInfoMapper;

    /**
     * 查询`虚拟设备参数`
     * 
     * @param uid `虚拟设备参数`主键
     * @return `虚拟设备参数`
     */
    @Override
    public TCarlineInfo selectTCarlineInfoByUid(String uid)
    {
        return tCarlineInfoMapper.selectTCarlineInfoByUid(uid);
    }

    /**
     * 查询`虚拟设备参数`列表
     * 
     * @param tCarlineInfo `虚拟设备参数`
     * @return `虚拟设备参数`
     */
    @Override
    public List<TCarlineInfo> selectTCarlineInfoList(TCarlineInfo tCarlineInfo)
    {
        return tCarlineInfoMapper.selectTCarlineInfoList(tCarlineInfo);
    }

    /**
     * 新增`虚拟设备参数`
     * 
     * @param tCarlineInfo `虚拟设备参数`
     * @return 结果
     */
    @Override
    public int insertTCarlineInfo(TCarlineInfo tCarlineInfo)
    {
//        tCarlineInfo.setCreateTime(DateUtils.getNowDate());
        return tCarlineInfoMapper.insertTCarlineInfo(tCarlineInfo);
    }

    /**
     * 修改`虚拟设备参数`
     * 
     * @param tCarlineInfo `虚拟设备参数`
     * @return 结果
     */
    @Override
    public int updateTCarlineInfo(TCarlineInfo tCarlineInfo)
    {
//        tCarlineInfo.setUpdateTime(DateUtils.getNowDate());
        return tCarlineInfoMapper.updateTCarlineInfo(tCarlineInfo);
    }

    /**
     * 批量删除`虚拟设备参数`
     * 
     * @param uids 需要删除的`虚拟设备参数`主键
     * @return 结果
     */
    @Override
    public int deleteTCarlineInfoByUids(String[] uids)
    {
        return tCarlineInfoMapper.deleteTCarlineInfoByUids(uids);
    }

    /**
     * 删除`虚拟设备参数`信息
     * 
     * @param uid `虚拟设备参数`主键
     * @return 结果
     */
    @Override
    public int deleteTCarlineInfoByUid(String uid)
    {
        return tCarlineInfoMapper.deleteTCarlineInfoByUid(uid);
    }
}
