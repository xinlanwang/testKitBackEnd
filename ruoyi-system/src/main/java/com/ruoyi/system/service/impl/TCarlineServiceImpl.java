package com.ruoyi.system.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.system.domain.vo.DeviceListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TCarlineMapper;
import com.ruoyi.system.domain.po.TCarline;
import com.ruoyi.system.service.ITCarlineService;

/**
 * `车型基本数据`Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@Service
public class TCarlineServiceImpl implements ITCarlineService 
{
    @Autowired
    private TCarlineMapper tCarlineMapper;

    /**
     * 查询`车型基本数据`
     *
     * @return `车型基本数据`
     */
    @Override
    public List queryDeviceList(TCarline tCarline)
    {
        QueryWrapper<DeviceListVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("","");
        /*List<DeviceListVo> deviceListVos = tCarlineMapper.queryDeviceList(basicInfo,deviceType);
        deviceListVos.forEach(System.out::println);
        return deviceListVos;*/
        return null;
    }

    /**
     * 查询`车型基本数据`
     * 
     * @param uid `车型基本数据`主键
     * @return `车型基本数据`
     */
    @Override
    public TCarline selectTCarlineByUid(String uid)
    {
        return tCarlineMapper.selectById(uid);
    }

    /**
     * 查询`车型基本数据`列表
     * 
     * @param tCarline `车型基本数据`
     * @return `车型基本数据`
     */
    @Override
    public List<TCarline> selectTCarlineList(TCarline tCarline)
    {

//        return tCarlineMapper.queryDeviceList(new QueryWrapper().);
        return null;
    }

    /**
     * 新增`车型基本数据`
     * 
     * @param tCarline `车型基本数据`
     * @return 结果
     */
    @Override
    public int insertTCarline(TCarline tCarline)
    {
//        tCarline.setUid(100l);
        tCarline.setgoldenCarName("2");
        tCarline.setSort(1L);
        tCarline.setIsShow(1);
        tCarline.setCarlineName("2");
        tCarline.setStatus(1);
        tCarline.setCreateByUid("1");
        tCarline.setUpdateByUid("2");
//        tCarline.setCreateTime(DateUtils.getNowDate());
        int insert = tCarlineMapper.insert(tCarline);
        System.out.println(1);
        return 1;
    }

    /**
     * 修改`车型基本数据`
     * 
     * @param tCarline `车型基本数据`
     * @return 结果
     */
    @Override
    public int updateTCarline(TCarline tCarline)
    {
//        tCarline.setUpdateTime(DateUtils.getNowDate());
        return tCarlineMapper.updateById(tCarline);
    }

    /**
     * 批量删除`车型基本数据`
     * 
     * @param uids 需要删除的`车型基本数据`主键
     * @return 结果
     */
    @Override
    public int deleteTCarlineByUids(Long[] uids)
    {

        for (Long uid:uids){
            tCarlineMapper.deleteById(uid);
        }

        return 1;
    }

    /**
     * 删除`车型基本数据`信息
     * 
     * @param uid `车型基本数据`主键
     * @return 结果
     */
    @Override
    public int deleteTCarlineByUid(String uid)
    {
        return tCarlineMapper.deleteById(uid);
    }
}
