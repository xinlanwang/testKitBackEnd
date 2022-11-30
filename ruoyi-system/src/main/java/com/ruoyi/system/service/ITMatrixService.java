package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.dto.ImportMatrixDTO;
import com.ruoyi.system.domain.dto.ImportValidateDTO;
import com.ruoyi.system.domain.param.MatrixMappingParam;
import com.ruoyi.system.domain.po.TMatrix;

import java.util.List;

/**
 * `BIGINT(32)`Service接口
 * 
 * @author ruoyi
 * @date 2022-11-21
 */
public interface ITMatrixService 
{
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
     * 批量删除`BIGINT(32)`
     * 
     * @param uids 需要删除的`BIGINT(32)`主键集合
     * @return 结果
     */
    public int deleteTMatrixByUids(Long[] uids);

    /**
     * 删除`BIGINT(32)`信息
     * 
     * @param uid `BIGINT(32)`主键
     * @return 结果
     */
    public int deleteTMatrixByUid(Long uid);

    public List<TMatrix> selectMatrixList(String matrixType);

    public AjaxResult selectMatrixMappingVlalue(MatrixMappingParam matrixListParam);

    public String importMatrix(List<ImportMatrixDTO> tMatrixList, boolean b, String operName);

    public String imporValidate(List<ImportValidateDTO> importValidateDTOS, boolean b, String operName);
}
