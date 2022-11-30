package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TSysLogininforMapper;
import com.ruoyi.system.domain.po.TSysLogininfor;
import com.ruoyi.system.service.ITSysLogininforService;

/**
 * `系统访问记录`Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@Service
public class TSysLogininforServiceImpl implements ITSysLogininforService 
{
    @Autowired
    private TSysLogininforMapper tSysLogininforMapper;

    /**
     * 查询`系统访问记录`
     * 
     * @param infoId `系统访问记录`主键
     * @return `系统访问记录`
     */
    @Override
    public TSysLogininfor selectTSysLogininforByInfoId(Long infoId)
    {
        return tSysLogininforMapper.selectTSysLogininforByInfoId(infoId);
    }

    /**
     * 查询`系统访问记录`列表
     * 
     * @param tSysLogininfor `系统访问记录`
     * @return `系统访问记录`
     */
    @Override
    public List<TSysLogininfor> selectTSysLogininforList(TSysLogininfor tSysLogininfor)
    {
        return tSysLogininforMapper.selectTSysLogininforList(tSysLogininfor);
    }

    /**
     * 新增`系统访问记录`
     * 
     * @param tSysLogininfor `系统访问记录`
     * @return 结果
     */
    @Override
    public int insertTSysLogininfor(TSysLogininfor tSysLogininfor)
    {
        return tSysLogininforMapper.insertTSysLogininfor(tSysLogininfor);
    }

    /**
     * 修改`系统访问记录`
     * 
     * @param tSysLogininfor `系统访问记录`
     * @return 结果
     */
    @Override
    public int updateTSysLogininfor(TSysLogininfor tSysLogininfor)
    {
        return tSysLogininforMapper.updateTSysLogininfor(tSysLogininfor);
    }

    /**
     * 批量删除`系统访问记录`
     * 
     * @param infoIds 需要删除的`系统访问记录`主键
     * @return 结果
     */
    @Override
    public int deleteTSysLogininforByInfoIds(Long[] infoIds)
    {
        return tSysLogininforMapper.deleteTSysLogininforByInfoIds(infoIds);
    }

    /**
     * 删除`系统访问记录`信息
     * 
     * @param infoId `系统访问记录`主键
     * @return 结果
     */
    @Override
    public int deleteTSysLogininforByInfoId(Long infoId)
    {
        return tSysLogininforMapper.deleteTSysLogininforByInfoId(infoId);
    }
}
