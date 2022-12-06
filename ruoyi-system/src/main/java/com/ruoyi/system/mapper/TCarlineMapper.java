package com.ruoyi.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.po.TCarline;
import com.ruoyi.system.domain.vo.DeviceListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * `车型基本数据`Mapper接口
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@Mapper
public interface TCarlineMapper extends BaseMapper<TCarline>
{
    /**
     * 查询`车型基本数据` 列表
     *
     * @param uid `车型基本数据`主键
     * @return `车型基本数据`
     */
    String queryDeviceListSql = "<script>\n" +
            "select t.carline_icon,tci.device_name as deviceName,tc.cluster_name as clusterName,tc.project_type as projectType,\n" +
            "tci.market_type as marketType,t.carline_model_type as carlineModelType,tci.variant_type as variant_type,tci.vin_code as vinCode,tci.carline_info_uid\n" +
            "from t_carline t\n" +
            "left join t_cluster tc on t.uid = tc.carline_uid\n" +
            "left join t_carline_info tci on tc.uid = tci.cluster_uid\n" +
            "where 1= 1\n" +
            /*"<when test='basicInfo !=null'>\n" +
            "    and (t.golden_car_name like '%#{basicInfo}%' or tc.cluster_name like '%#{basicInfo}%' or tc.project_type like '%#{basicInfo}%'\n" +
            "    or tci.market_type like '%#{basicInfo}%'or t.carline_model_type like '%#{basicInfo}%' or tci.variant like '%#{basicInfo}%'\n" +
            "         or tci.vin_code like '%#{basicInfo}%')\n" +
            "</when>\n" +*/
            "<when test='deviceType !=null'>\n" +
            "and tc.device_type = #{deviceType}\n" +
            "</when>\n" +
            "</script>";
    @Select(queryDeviceListSql)
    List<DeviceListVo> queryDeviceList(@Param("basicInfo") String basicInfo,@Param("deviceType") String deviceType);







}
