package com.ruoyi.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ruoyi.system.domain.param.RecordListParam;
import com.ruoyi.system.domain.po.TDesktopRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * `BIGINT(32) auto_increment`Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-07
 */
public interface TDesktopRecordMapper  extends BaseMapper<TDesktopRecord>
{
    /**
     * 查询`BIGINT(32) auto_increment`
     * 
     * @param uid `BIGINT(32) auto_increment`主键
     * @return `BIGINT(32) auto_increment`
     */
    public TDesktopRecord selectTDesktopRecordByUid(Long uid);

    /**
     * 查询`BIGINT(32) auto_increment`列表
     * 
     * @param tDesktopRecord `BIGINT(32) auto_increment`
     * @return `BIGINT(32) auto_increment`集合
     */
    public List<TDesktopRecord> selectTDesktopRecordList(TDesktopRecord tDesktopRecord);

    /**
     * 新增`BIGINT(32) auto_increment`
     * 
     * @param tDesktopRecord `BIGINT(32) auto_increment`
     * @return 结果
     */
    public int insertTDesktopRecord(TDesktopRecord tDesktopRecord);

    /**
     * 修改`BIGINT(32) auto_increment`
     * 
     * @param tDesktopRecord `BIGINT(32) auto_increment`
     * @return 结果
     */
    public int updateTDesktopRecord(TDesktopRecord tDesktopRecord);

    /**
     * 删除`BIGINT(32) auto_increment`
     * 
     * @param uid `BIGINT(32) auto_increment`主键
     * @return 结果
     */
    public int deleteTDesktopRecordByUid(Long uid);

    /**
     * 批量删除`BIGINT(32) auto_increment`
     * 
     * @param uids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTDesktopRecordByUids(Long[] uids);


    public List<TDesktopRecord> selectListBySingleOrderDict(RecordListParam recordListParam);

    public List<TDesktopRecord> selectListBySingleOrderNonDict(RecordListParam recordListParam);

    public List<TDesktopRecord> selectDefaultRecordList(RecordListParam recordListParam);

    public List<TDesktopRecord> selectListBySingleOrderByRecord(RecordListParam recordListParam);
}
