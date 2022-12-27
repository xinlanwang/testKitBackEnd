package com.ruoyi.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.dto.GoldenInfoComponentDTO;
import com.ruoyi.system.domain.param.DeviceListParam;
import com.ruoyi.system.domain.po.TCarlineInfo;
import com.ruoyi.system.domain.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * `虚拟设备参数`Mapper接口
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@Mapper
public interface TCarlineInfoMapper extends BaseMapper<TCarlineInfo>
{
    /**
     * 设备详情A基本信息
     */
    String queryDeviceInfoSql = "select tci.carline_info_uid,tc.device_type as carlineType,tci.device_name as deviceName,\n" +
            "                               tc.cluster_name as clusterName,tc.project_type as projectType,tci.platform_type as platformType, \n" +
            "                               tci.market_type as marketType,tl.carline_model_type as carlineModelType,tci.variant_type as variant_type,tci.vin_code as vinCode, \n" +
            "                                    tc.last_updated as clusterLastUpdateName,tci.db_version as dbVersion\n" +
            "                                    from t_carline_info tci \n" +
            "                                    left join t_cluster tc on tci.cluster_uid = tc.uid\n" +
            "                                    left join t_carline tl on tc.carline_uid = tl.uid \n" +
            "                                    where tci.carline_info_uid = #{carlineInfoUid}";
    @Select(queryDeviceInfoSql)
    DeviceInfoVo queryDeviceInfo(@Param("carlineInfoUid") Long carlineInfoUid);

    /**
     * 设备详情B配件基本信息
     */
    String queryDeviceComponentSql = "select tci.carline_info_uid as carlineInfoUid,tcd.component_type as componentType,tdr.ecu_id as ecuId,tcd.component_instance_name as componentInstanceName,\n" +
            "                        tcd.component_version as componentVersion,tcd.ware_type as wareType,tcd.part_number as partNumber,tcd.component_name as componentName\n" +
            "                                    from t_carline_info tci \n" +
            "                                    left join t_carline_component tcc on tci.carline_info_uid = tcc.carline_info_uid \n" +
            "                                    left join t_component_data tcd on (tcd.uid = tcc.hw_version_uid or tcd.uid = tcc.sw_version_uid or tcd.uid = tcc.other_version_uid)\n" +
            "left join t_dtc_report tdr on tcd.component_type = tdr.component_type" +
            "            where tci.carline_info_uid = #{carlineInfoUid}";
    @Select(queryDeviceComponentSql)
    List<DeviceInfoComponent> queryDeviceComponent(@Param("carlineInfoUid") Long carlineInfoUid);

    /**
     * GoldenInfo设备详情基本信息
     */
    String queryGoldenInfoListSql = "select tc.golden_car_name as carlineModelType,tci.carline_info_uid as carlineInfoUid,tci.market_type as marketType,\n" +
            "       tl.cluster_name as clusterName ,tl.uid as clusterUid\n" +
            "from t_carline tc\n" +
            "left join t_cluster tl on tl.carline_uid = tc.uid\n" +
            "left join t_carline_info tci on tci.cluster_uid = tl.uid\n" +
            "where 1 = 1 " +
            "and tl.device_type = 3 " +
            "and tl.cluster_name = #{clusterName}\n" ;
    @Select(queryGoldenInfoListSql)
    List<GoldenListPlatfromVO> queryGoldenInfoList(@Param("clusterName") String clusterName);

    /**
     * GoldenInfo详情列表
     */
    String queryGoldenInfoDetailSql = "select cd.component_name as componentType,cd.part_number as partNumber,\n" +
            "               tcs.cluster_name as clusterName,cd.component_version as componentVersion,cd.ware_type as wareType,tcc.temporary_variable as temporaryVariable,\n" +
            "                   cd.component_version as componentModel,cd.ware_type as wareType,tcc.minimal_hw as minimalHW\n" +
            "            from t_component_data cd\n" +
            "            left join t_carline_component tcc on  (cd.uid = tcc.hw_version_uid or cd.uid = tcc.sw_version_uid or cd.uid = tcc.other_version_uid)\n" +
            "            left join t_carline_info tci on tci.carline_info_uid = tcc.carline_info_uid\n" +
            "            left join t_cluster tcs on tci.cluster_uid = tcs.uid\n" +
            "            left join t_carline tcl on tcl.uid = tcs.carline_uid\n" +
            "where tcs.device_type = 3\n" +
            " and tcs.cluster_name = #{clusterName}\n" +
            " and tcl.golden_car_name = #{goldenCarName}\n" +
            " and market_type = #{marketType};" ;
    @Select(queryGoldenInfoDetailSql)
    List<GoldenInfoComponentDTO> queryGoldenInfoDetail(@Param("clusterName") String clusterName, @Param("goldenCarName") String goldenCarName, @Param("marketType") String marketType);

    List<DeviceListVo> queryDeviceList(DeviceListParam deviceListParam);

    /**
     * 查询`虚拟设备参数`
     * 
     * @param uid `虚拟设备参数`主键
     * @return `虚拟设备参数`
     */
    public TCarlineInfo selectTCarlineInfoByUid(String uid);

    /**
     * 查询`虚拟设备参数`列表
     * 
     * @param tCarlineInfo `虚拟设备参数`
     * @return `虚拟设备参数`集合
     */
    public List<TCarlineInfo> selectTCarlineInfoList(TCarlineInfo tCarlineInfo);

    /**
     * 新增`虚拟设备参数`
     * 
     * @param tCarlineInfo `虚拟设备参数`
     * @return 结果
     */
    public int insertTCarlineInfo(TCarlineInfo tCarlineInfo);

    /**
     * 修改`虚拟设备参数`
     * 
     * @param tCarlineInfo `虚拟设备参数`
     * @return 结果
     */
    public int updateTCarlineInfo(TCarlineInfo tCarlineInfo);

    /**
     * 删除`虚拟设备参数`
     * 
     * @param uid `虚拟设备参数`主键
     * @return 结果
     */
    public int deleteTCarlineInfoByUid(String uid);

    /**
     * 批量删除`虚拟设备参数`
     * 
     * @param uids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTCarlineInfoByUids(String[] uids);


    String selectDeviceComponentsListSql = "select tci.market_type as marketType,tci.db_version as dbVersion,tci.carline_info_uid as tCarlineInfoUid\n" +
            "            from t_carline_info tci\n" +
            "            left join t_cluster tc on tc.uid = tci.cluster_uid\n" +
            "            left join t_carline tcl on tcl.uid = tc.carline_uid\n" +
            "            where tci.market_type in (select distinct tci.market_type as marketType\n" +
            "            from t_carline_info tci\n" +
            "            left join t_cluster tc on tc.uid = tci.cluster_uid\n" +
            "            left join t_carline tcl on tcl.uid = tc.carline_uid\n" +
            "            where tc.cluster_name = #{clusterName} \n" +
            "            and tcl.golden_car_name = #{carlineModelType} \n" +
            "            and tc.device_type = '3'\n" +
            "            )\n" +
            "and tc.cluster_name = #{clusterName} \n" +
            "            and tcl.golden_car_name = #{carlineModelType} \n" +
            "            and tc.device_type = '3'\n" ;
//            "group by market_type";
    @Select(selectDeviceComponentsListSql)
    public List<GoldenInfoVO> selectDeviceComponentsList(@Param("clusterName") String clusterName, @Param("carlineModelType") String carlineModelType);



    String selectGoldenCarlineInfoSql = "select tcd.component_type as componentType,tcd.component_version as componentVersion,tcd.part_number as partNumber,\n" +
            "       tcc.temporary_variable as temporaryVariable,tcc.minimal_hw as minimalHW\n" +
            "            from t_component_data tcd\n" +
            "            left join t_carline_component tcc on (tcd.uid = tcc.sw_version_uid or tcd.uid = tcc.hw_version_uid or tcd.uid = tcc.other_version_uid)\n" +
            "            left join t_carline_info tci on tci.carline_info_uid = tcc.carline_info_uid\n" +
            "            left join t_cluster tc on tci.cluster_uid = tc.uid\n" +
            "            left join t_carline tca on tc.carline_uid = tca.uid\n" +
            "            where tc.device_type = '3'\n" +
            "and tc.cluster_name = #{goldenClusterNameType}\n" +
            "and tca.golden_car_name = #{goldenCarType}\n" +
            "and tci.market_type = #{marketType}";
    @Select(selectGoldenCarlineInfoSql)
    List<GoldenInfoComponentDTO> selectGoldenCarlineInfo(String carlineModelType,  @Param("goldenClusterNameType") String goldenClusterNameType,  @Param("marketType") String marketType,  @Param("goldenCarType") Long goldenCarType);
}
