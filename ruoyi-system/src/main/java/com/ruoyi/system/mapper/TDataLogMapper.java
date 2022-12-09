package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.po.TDataLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * `BIGINT(32) auto_increment`Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-08
 */
public interface TDataLogMapper extends BaseMapper<TDataLog>
{
    /**
     * 查询`BIGINT(32) auto_increment`
     * 
     * @param uid `BIGINT(32) auto_increment`主键
     * @return `BIGINT(32) auto_increment`
     */
    public TDataLog selectTDataLogByUid(Long uid);

    /**
     * 查询`BIGINT(32) auto_increment`列表
     * 
     * @param tDataLog `BIGINT(32) auto_increment`
     * @return `BIGINT(32) auto_increment`集合
     */
    public List<TDataLog> selectTDataLogList(TDataLog tDataLog);

    /**
     * 新增`BIGINT(32) auto_increment`
     * 
     * @param tDataLog `BIGINT(32) auto_increment`
     * @return 结果
     */
    public int insertTDataLog(TDataLog tDataLog);

    /**
     * 修改`BIGINT(32) auto_increment`
     * 
     * @param tDataLog `BIGINT(32) auto_increment`
     * @return 结果
     */
    public int updateTDataLog(TDataLog tDataLog);

    /**
     * 删除`BIGINT(32) auto_increment`
     * 
     * @param uid `BIGINT(32) auto_increment`主键
     * @return 结果
     */
    public int deleteTDataLogByUid(Long uid);

    /**
     * 批量删除`BIGINT(32) auto_increment`
     * 
     * @param uids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTDataLogByUids(Long[] uids);



    String queryDeviceInfoSql = "select max(distinct record_index)\n" +
            "from t_desktop_log \n" +
            "where operation_uid = #{dataUid}";
    @Select(queryDeviceInfoSql)
    public Integer selectMaxRecordIndex(@Param("dataUid") Long dataUid);
}
