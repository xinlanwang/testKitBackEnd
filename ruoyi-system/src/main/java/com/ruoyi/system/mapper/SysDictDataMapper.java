package com.ruoyi.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.po.TMatrix;
import com.ruoyi.system.domain.vo.DeviceInfoVo;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.common.core.domain.entity.SysDictData;
import org.apache.ibatis.annotations.Select;

/**
 * 字典表 数据层
 * 
 * @author ruoyi
 */
public interface SysDictDataMapper extends BaseMapper<SysDictData>
{

    /**
     * 批量逻辑删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     * @return 结果
     */
    public int logicDelectDictDataById(Long dictCodes);
    /**
     * 批量逻辑删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     * @return 结果
     */
    public int logicDelectDictDataByIds(Long[] dictCodes);
    public int logicDelectDictDataByMatrixType(@Param("matrixType") String matrixType);

    String queryDeviceInfoSql = "select max(cast(dict_value as unsigned integer ))\n" +
            "from sys_dict_data\n" +
            "where dict_type = #{dictType}";
//    @Select(queryDeviceInfoSql)
    public Integer selectMaxDictType(@Param("dictType") String dictType);
    /**
     * 根据条件分页查询字典数据
     * 
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    public List<SysDictData> selectDictDataList(SysDictData dictData);

    /**
     * 根据字典类型查询字典数据
     * 
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    public List<SysDictData> selectDictDataByType(String dictType);

    /**
     * 根据字典类型和字典键值查询字典数据信息
     * 
     * @param dictType 字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    public String selectDictLabel(@Param("dictType") String dictType, @Param("dictValue") String dictValue);

    /**
     * 根据字典数据ID查询信息
     * 
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    public SysDictData selectDictDataById(Long dictCode);

    /**
     * 查询字典数据
     * 
     * @param dictType 字典类型
     * @return 字典数据
     */
    public int countDictDataByType(String dictType);

    /**
     * 通过字典ID删除字典数据信息
     * 
     * @param dictCode 字典数据ID
     * @return 结果
     */
    public int deleteDictDataById(Long dictCode);

    /**
     * 批量删除字典数据信息
     * 
     * @param dictCodes 需要删除的字典数据ID
     * @return 结果
     */
    public int deleteDictDataByIds(Long[] dictCodes);

    /**
     * 新增字典数据信息
     * 
     * @param dictData 字典数据信息
     * @return 结果
     */
    public int insertDictData(SysDictData dictData);

    /**
     * 修改字典数据信息
     * 
     * @param dictData 字典数据信息
     * @return 结果
     */
    public int updateDictData(SysDictData dictData);

    /**
     * 同步修改字典类型
     * 
     * @param oldDictType 旧字典类型
     * @param newDictType 新旧字典类型
     * @return 结果
     */
    public int updateDictDataType(@Param("oldDictType") String oldDictType, @Param("newDictType") String newDictType);

    public int insertMatrixDictData(SysDictData data);

    public List<SysDictData> selectMatrixDictDataList(SysDictData dictData);

    /**
     * 将字典date的状态置为正常
     * @param dictTypeName
     * @param dictLabel
     * @param dictValue
     * @return
     */
    public int updateDictDataStatus(@Param("dictTypeName") String dictTypeName, @Param("dictLabel") String dictLabel, @Param("dictValue") String dictValue, @Param("status") String status);

    /**
     * 改变其matrixType属性
     * @param dictTypeName
     * @param dictLabel
     * @param dictValue
     * @param matrixType
     * @return
     */
    public int updateDictDataMatrixType(@Param("dictTypeName") String dictTypeName, @Param("dictLabel") String dictLabel, @Param("dictValue") String dictValue,@Param("matrixType") String matrixType);
}
