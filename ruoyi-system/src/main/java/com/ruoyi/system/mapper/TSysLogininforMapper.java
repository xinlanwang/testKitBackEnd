package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.po.TSysLogininfor;

/**
 * `系统访问记录`Mapper接口
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
public interface TSysLogininforMapper 
{
    /**
     * 查询`系统访问记录`
     * 
     * @param infoId `系统访问记录`主键
     * @return `系统访问记录`
     */
    public TSysLogininfor selectTSysLogininforByInfoId(Long infoId);

    /**
     * 查询`系统访问记录`列表
     * 
     * @param tSysLogininfor `系统访问记录`
     * @return `系统访问记录`集合
     */
    public List<TSysLogininfor> selectTSysLogininforList(TSysLogininfor tSysLogininfor);

    /**
     * 新增`系统访问记录`
     * 
     * @param tSysLogininfor `系统访问记录`
     * @return 结果
     */
    public int insertTSysLogininfor(TSysLogininfor tSysLogininfor);

    /**
     * 修改`系统访问记录`
     * 
     * @param tSysLogininfor `系统访问记录`
     * @return 结果
     */
    public int updateTSysLogininfor(TSysLogininfor tSysLogininfor);

    /**
     * 删除`系统访问记录`
     * 
     * @param infoId `系统访问记录`主键
     * @return 结果
     */
    public int deleteTSysLogininforByInfoId(Long infoId);

    /**
     * 批量删除`系统访问记录`
     * 
     * @param infoIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTSysLogininforByInfoIds(Long[] infoIds);
}
