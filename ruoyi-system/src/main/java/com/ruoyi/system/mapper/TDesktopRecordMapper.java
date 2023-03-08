package com.ruoyi.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.dto.DashboardGetDeviceTestHourDTO;
import com.ruoyi.system.domain.dto.DashboardGetDeviceUseDTO;
import com.ruoyi.system.domain.param.DashboardParam;
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




    public List<TDesktopRecord> selectRecordList(RecordListParam recordListParam);


    String getDeviceUseSql = "select tdr.uid as recordUid,tci.device_name as deviceName,sum(tdr.test_hour) as testHour,sum(tdr.mileacge) as mileacge,\n" +
            "                   DATE_FORMAT( tdr.oper_time, '%Y-%m-%d') as operTime,sum(tdr.planned_ticket) as plannedTicket\n" +
            "            from t_desktop_record tdr \n" +
            "                        left join t_carline_info tci on tci.carline_info_uid = tdr.data_uid\n" +
            "                        left join t_cluster tc on tci.cluster_uid = tc.uid\n";
    @Select("<script>" + getDeviceUseSql + dysql + " group by recordUid " + "</script>")
    public List<DashboardGetDeviceUseDTO> staticRecordGroupByRecordId(@Param("dashboardParam") DashboardParam dashboardParam);

    public String dysql = " where 1 = 1\n " +
            "        <if test=\"dashboardParam.deviceType != null and dashboardParam.deviceType != ''\">\n" +
            "            and tc.device_type = #{dashboardParam.deviceType}\n" +
            "        </if>\n" +
            "       <if test=\"dashboardParam.marketTypes != null and dashboardParam.marketTypes.length > 0\">\n" +
            "            and tci.market_type in\n" +
            "            <foreach item=\"item\" index=\"index\" collection=\"dashboardParam.marketTypes\" open=\"(\" separator=\",\" close=\")\">\n" +
            "                #{item}\n" +
            "            </foreach>\n" +
            "        </if>\n" +
            "        <if test=\"dashboardParam.variantTypes != null and dashboardParam.variantTypes.length > 0\">\n" +
            "            and tci.variant_type in\n" +
            "            <foreach collection=\"dashboardParam.variantTypes\" item=\"item\" index=\"index\" open=\"(\" separator=\",\" close=\")\">\n" +
            "                #{item}\n" +
            "            </foreach>\n" +
            "        </if>\n" +
            "        <if test=\"dashboardParam.projectTypes != null and dashboardParam.projectTypes.length > 0\">\n" +
            "            and tc.project_type in\n" +
            "            <foreach collection=\"dashboardParam.projectTypes\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\">\n" +
            "                #{item}\n" +
            "            </foreach>\n" +
            "        </if>\n" +
            "        <if test=\"dashboardParam.platformTypes != null and dashboardParam.platformTypes.length > 0\">\n" +
            "            and tci.platform_type in\n" +
            "            <foreach collection=\"dashboardParam.platformTypes\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\">\n" +
            "                #{item}\n" +
            "            </foreach>\n" +
            "        </if>\n" +
            "        <if test=\"dashboardParam.clusterNames != null and dashboardParam.clusterNames.length > 0\">\n" +
            "            and tc.cluster_name in\n" +
            "            <foreach collection=\"dashboardParam.clusterNames\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\">\n" +
            "                #{item}\n" +
            "            </foreach>\n" +
            "        </if>\n" +
            "        <if test=\"dashboardParam.carlineModelTypes != null and dashboardParam.carlineModelTypes.length > 0\">\n" +
            "            and tci.carline_model_type in\n" +
            "            <foreach collection=\"dashboardParam.carlineModelTypes\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\">\n" +
            "                #{item}\n" +
            "            </foreach>\n" +
            "        </if>" +
            "       <if test=\"dashboardParam.functionGroupTypes != null and dashboardParam.functionGroupTypes.length > 0\">\n" +
            "            and tdr.function_group_type in\n" +
            "            <foreach collection=\"dashboardParam.functionGroupTypes\" item=\"item\" index=\"index\" open=\"(\" separator=\",\" close=\")\">\n" +
            "                #{item}\n" +
            "            </foreach>\n" +
            "        </if>" +
            "       <if test=\"dashboardParam.testStartDate != null \">\n" +
            "            and tdr.oper_time &gt;= DATE_FORMAT(#{dashboardParam.testStartDate}, '%Y-%m-%d')\n" +
            "        </if>\n" +
            "        <if test=\"dashboardParam.testEndDate != null \">\n" +
            "            and tdr.oper_time &lt;= DATE_FORMAT(DATE_ADD(#{dashboardParam.testEndDate},INTERVAL 1 DAY), '%Y-%m-%d')\n" +
            "        </if>" +
            "       <if test=\"dashboardParam.deviceName != null and dashboardParam.deviceName != ''\">\n" +
            "            AND lower(tci.device_name) like lower(concat('%', #{dashboardParam.deviceName}, '%'))\n" +
            "        </if>";

    String staticRecordByDeviceNameSql = "select tci.device_name as deviceName,sum(tdr.test_hour) as testHour,sum(tdr.mileacge) as mileacge\n" +
            "            from t_desktop_record tdr \n" +
            "                        left join t_carline_info tci on tci.carline_info_uid = tdr.data_uid\n" +
            "                        left join t_cluster tc on tci.cluster_uid = tc.uid\n" ;
    @Select("<script>" + staticRecordByDeviceNameSql + dysql +" group by deviceName " + "</script>")
    public List<DashboardGetDeviceUseDTO> staticRecordGroupByDeviceName(@Param("dashboardParam") DashboardParam dashboardParam);


    String staticRecordGroupByFunctionGroupSql = "select tdr.function_group_type as functionGroupType,sum(tdr.test_hour) as testHour,sum(tdr.mileacge) as mileacge,\n" +
            "                                           sum(tdr.planned_ticket) as plannedTicket,\n" +
            "                            sum(case when tc.device_type = 1 then tdr.test_hour else 0 end ) as benchTestHour, \n" +
            "                            sum(case when tc.device_type = 2 then tdr.test_hour else 0 end ) as carTestHour \n" +
            "                            from t_desktop_record tdr \n" +
            "                        left join t_carline_info tci on tci.carline_info_uid = tdr.data_uid\n" +
            "                        left join t_cluster tc on tci.cluster_uid = tc.uid\n" ;
    @Select("<script>" + staticRecordGroupByFunctionGroupSql + dysql + " group by tdr.function_group_type " + "</script>")
    public List<DashboardGetDeviceTestHourDTO> staticRecordGroupByFunctionGroup(@Param("dashboardParam") DashboardParam dashboardParam);


    String deviceStatisticTicketSql = "select sum(tdr.test_hour) as testHour,sum(tdr.mileacge) as mileacge,\n" +
            "                               DATE_FORMAT( tdr.oper_time, '%Y-%m-%d') as operTime,sum(tdr.planned_ticket) as plannedTicket\n" +
            "            from t_desktop_record tdr \n" +
            "                        left join t_carline_info tci on tci.carline_info_uid = tdr.data_uid\n" +
            "                        left join t_cluster tc on tci.cluster_uid = tc.uid\n";
    @Select("<script>" + deviceStatisticTicketSql + dysql + " group by oper_time order by oper_time asc " + "</script>")
    public List<DashboardGetDeviceUseDTO> deviceStatisticTicket(@Param("dashboardParam") DashboardParam dashboardParam);
}
