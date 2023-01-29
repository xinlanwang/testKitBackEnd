package com.ruoyi.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.AutoSaveVersionVO;
import com.ruoyi.system.domain.dto.CorrentVersionDeviceDTO;
import com.ruoyi.system.domain.dto.RefreshDeviceDTO;
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
            "where uid in (select uid from t_cluster where device_type = '3' order by create_time desc) \n" ;
    @Select(queryGoldenInfoListSql)
    List<TCluster> selectUniqueClusterNames();


    String selectAutoSaveVersionListSql = "select tci.carline_info_uid as carlineInfoUid,t_cluster.auto_save_version_name as AutoSaveVersionName\n" +
            "from t_cluster\n" +
            "left join t_carline_info tci on tci.cluster_uid = t_cluster.uid\n" +
            "where carline_uid = (select carline_uid\n" +
            "from t_cluster tc\n" +
            "where tc.uid = (select cluster_uid\n" +
            "from t_carline_info\n" +
            "where carline_info_uid = #{carlineInfoUid}))\n" +
            "order by t_cluster.update_time desc" ;
    @Select(selectAutoSaveVersionListSql)
    List<AutoSaveVersionVO> selectAutoSaveVersionList(@Param("carlineInfoUid") String carlineInfoUid);

    String selectClusterNameSql = "select dict_label\n" +
            "from sys_dict_data\n" +
            "where dict_type = 'clusterName'\n" +
            "and dict_value = #{clusterName}" ;
    @Select(selectClusterNameSql)
    String selectClusterName(@Param("clusterName") String clusterName);


    String selectCarlineInfoIdsByClusterNameSql = "select distinct tci.carline_info_uid\n" +
            "from t_cluster tc\n" +
            "left join t_carline_info tci on tc.uid = tci.cluster_uid\n" +
            "where 1 = 1\n" +
            "and cluster_name = #{clusterName}\n" +
            "and device_type = '3'" ;
    @Select(selectCarlineInfoIdsByClusterNameSql)
    Long[] selectCarlineInfoIdsByClusterName(@Param("clusterName") String clusterName);

    String selectLastestClusterSql = "select tci.carline_info_uid as carlineInfoUid,tc.update_time as updateTime\n" +
            "from t_carline_info tci\n" +
            "left join t_cluster tc on tc.uid = tci.cluster_uid\n" +
            "left join t_carline tcl on tcl.uid = tc.carline_uid\n" +
            "where tcl.uid = (select distinct tcl.uid\n" +
            "from t_carline_info tci\n" +
            "left join t_cluster tc on tc.uid = tci.cluster_uid\n" +
            "left join t_carline tcl on tcl.uid = tc.carline_uid\n" +
            "where device_name = #{deviceName} and tcl.uid <> '')\n" +
            "and tc.update_time = (select MAX(tc.update_time)\n" +
            "from t_carline_info tci\n" +
            "left join t_cluster tc on tc.uid = tci.cluster_uid\n" +
            "left join t_carline tcl on tcl.uid = tc.carline_uid\n" +
            "where tcl.uid in (select distinct tcl.uid\n" +
            "from t_carline_info tci\n" +
            "left join t_cluster tc on tc.uid = tci.cluster_uid\n" +
            "left join t_carline tcl on tcl.uid = tc.carline_uid\n" +
            "where device_name = #{deviceName} and tcl.uid <> ''))\n" +
            "and tc.device_type = #{deviceType}" ;
    @Select(selectLastestClusterSql)
    RefreshDeviceDTO selectLastestCluster(@Param("deviceName")String deviceName, @Param("deviceType")String deviceType);

    String selectCorrentVersionDeviceDTOSql = "select tci.carline_info_uid as carlineInfoUid,t_cluster.uid as clusterUid\n" +
            "from t_cluster\n" +
            "left join t_carline_info tci on tci.cluster_uid = t_cluster.uid\n" +
            "where carline_uid = (select carline_uid\n" +
            "from t_cluster tc\n" +
            "where tc.uid = (select cluster_uid\n" +
            "from t_carline_info\n" +
            "where carline_info_uid = #{carlineInfoUid}))\n" +
            "order by t_cluster.update_time asc" ;
    @Select(selectCorrentVersionDeviceDTOSql)
    List<CorrentVersionDeviceDTO> selectCorrentVersionDeviceDTO(@Param("carlineInfoUid")Long carlineInfoUid);
}
