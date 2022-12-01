package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.po.TComponentData;
import com.ruoyi.system.domain.po.TMatrix;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * `BIGINT(32)`Mapper接口
 * 
 * @author ruoyi
 * @date 2022-11-21
 */
@Mapper
public interface TMatrixMapper extends BaseMapper<TMatrix>
{

    String queryDeviceInfoSql = "select golden_car_type\n" +
            "from t_matrix\n" +
            "where matrix_type = 1\n" +
            "and carline_model_type = #{carlineModelType}\n" +
            "and golden_cluster_name_type =#{goldenClusterNameType}";
    @Select(queryDeviceInfoSql)
    public List<Long> selectGoldenCarType(@Param("carlineModelType") String carlineModelType,@Param("goldenClusterNameType") String goldenClusterNameType);


    String selectGoldenClusterNameTypeSql = "select dict_value,dict_type\n" +
            "from sys_dict_data\n" +
            "where dict_label = (select dict_label\n" +
            "from sys_dict_data\n" +
            "where dict_type = 'clusterName'\n" +
            "and dict_value = #{clusterNameType})\n" +
            "and dict_type = 'goldenClusterNameType'";
    @Select(selectGoldenClusterNameTypeSql)
    public String selectGoldenClusterNameType(@Param("clusterNameType") String clusterNameType);

    /**
     * 查询`BIGINT(32)`
     * 
     * @param uid `BIGINT(32)`主键
     * @return `BIGINT(32)`
     */
    public TMatrix selectTMatrixByUid(Long uid);

    /**
     * 查询`BIGINT(32)`列表
     * 
     * @param tMatrix `BIGINT(32)`
     * @return `BIGINT(32)`集合
     */
    public List<TMatrix> selectTMatrixList(TMatrix tMatrix);

    /**
     * 新增`BIGINT(32)`
     * 
     * @param tMatrix `BIGINT(32)`
     * @return 结果
     */
    public int insertTMatrix(TMatrix tMatrix);

    /**
     * 修改`BIGINT(32)`
     * 
     * @param tMatrix `BIGINT(32)`
     * @return 结果
     */
    public int updateTMatrix(TMatrix tMatrix);

    /**
     * 删除`BIGINT(32)`
     * 
     * @param uid `BIGINT(32)`主键
     * @return 结果
     */
    public int deleteTMatrixByUid(Long uid);

    /**
     * 批量删除`BIGINT(32)`
     * 
     * @param uids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMatrixByUids(Long[] uids);


}
