package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.po.TComponentData;
import com.ruoyi.system.domain.po.TDesktopLog;

import java.util.List;

/**
 * `BIGINT(32) auto_increment`Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-08
 */
public interface TDesktopLogMapper extends BaseMapper<TDesktopLog>
{
    /**
     * 查询`BIGINT(32) auto_increment`
     * 
     * @param uid `BIGINT(32) auto_increment`主键
     * @return `BIGINT(32) auto_increment`
     */
    public TDesktopLog selectTDesktopLogByUid(Long uid);

    /**
     * 查询`BIGINT(32) auto_increment`列表
     * 
     * @param tDesktopLog `BIGINT(32) auto_increment`
     * @return `BIGINT(32) auto_increment`集合
     */
    public List<TDesktopLog> selectTDesktopLogList(TDesktopLog tDesktopLog);

    /**
     * 新增`BIGINT(32) auto_increment`
     * 
     * @param tDesktopLog `BIGINT(32) auto_increment`
     * @return 结果
     */
    public int insertTDesktopLog(TDesktopLog tDesktopLog);

    /**
     * 修改`BIGINT(32) auto_increment`
     * 
     * @param tDesktopLog `BIGINT(32) auto_increment`
     * @return 结果
     */
    public int updateTDesktopLog(TDesktopLog tDesktopLog);

    /**
     * 删除`BIGINT(32) auto_increment`
     * 
     * @param uid `BIGINT(32) auto_increment`主键
     * @return 结果
     */
    public int deleteTDesktopLogByUid(Long uid);

    /**
     * 批量删除`BIGINT(32) auto_increment`
     * 
     * @param uids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTDesktopLogByUids(Long[] uids);
}
