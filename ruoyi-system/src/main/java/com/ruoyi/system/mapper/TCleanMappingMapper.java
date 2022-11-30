package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.po.TCleanMapping;
import com.ruoyi.system.domain.po.TMatrix;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * `BIGINT(32)`Mapper接口
 * 
 * @author ruoyi
 * @date 2022-11-21
 */
@Mapper
public interface TCleanMappingMapper  extends BaseMapper<TCleanMapping>
{
    /**
     * 查询`BIGINT(32)`
     *
     * @param uid `BIGINT(32)`主键
     * @return `BIGINT(32)`
     */
    public TCleanMapping selectTCleanMappingByUid(Long uid);

    /**
     * 查询`BIGINT(32)`列表
     *
     * @param tCleanMapping `BIGINT(32)`
     * @return `BIGINT(32)`集合
     */
    public List<TCleanMapping> selectTCleanMappingList(TCleanMapping tCleanMapping);

    /**
     * 新增`BIGINT(32)`
     *
     * @param tCleanMapping `BIGINT(32)`
     * @return 结果
     */
    public int insertTCleanMapping(TCleanMapping tCleanMapping);

    /**
     * 修改`BIGINT(32)`
     *
     * @param tCleanMapping `BIGINT(32)`
     * @return 结果
     */
    public int updateTCleanMapping(TCleanMapping tCleanMapping);

    /**
     * 删除`BIGINT(32)`
     *
     * @param uid `BIGINT(32)`主键
     * @return 结果
     */
    public int deleteTCleanMappingByUid(Long uid);

    /**
     * 批量删除`BIGINT(32)`
     *
     * @param uids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTCleanMappingByUids(Long[] uids);
}
