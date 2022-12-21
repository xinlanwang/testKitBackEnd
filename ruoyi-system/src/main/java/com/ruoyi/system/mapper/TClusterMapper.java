package com.ruoyi.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.po.TCluster;
import com.ruoyi.system.domain.vo.GoldenListPlatfromVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * `版本管控`Mapper接口
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@Mapper
public interface TClusterMapper extends BaseMapper<TCluster>
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
     * 删除`版本管控`
     * 
     * @param uid `版本管控`主键
     * @return 结果
     */
    public int deleteTClusterByUid(String uid);

    /**
     * 批量删除`版本管控`
     * 
     * @param uids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTClusterByUids(String[] uids);

    /**
     * GoldenInfo设备详情基本信息
     */
    String queryGoldenInfoListSql = "select distinct cluster_name as clusterName\n" +
            "from t_cluster\n" +
            "where uid in (select uid from t_cluster where device_type = '3' order by create_time desc)\n" ;
    @Select(queryGoldenInfoListSql)
    List<TCluster> selectUniqueClusterNames();


}
