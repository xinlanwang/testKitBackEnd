package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.SysDictType;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.dto.ImportMatrixDTO;
import com.ruoyi.system.domain.dto.ImportValidateDTO;
import com.ruoyi.system.domain.param.MatrixMappingParam;
import com.ruoyi.system.domain.po.TMatrix;
import com.ruoyi.system.service.ISysDictDataService;
import com.ruoyi.system.service.ISysDictTypeService;
import com.ruoyi.system.service.ITMatrixService;
import com.ruoyi.system.service.ValidateAllService;
import com.ruoyi.system.service.impl.DeviceExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Api("Matrix列表")
@RestController
@RequestMapping("/matrix")
public class MatrixController extends BaseController {
    @Autowired
    private ValidateAllService matrixService;
    @Autowired
    private ISysDictTypeService dictTypeService;
    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private ITMatrixService itMatrixService;

    @PostMapping("/importMatrix")
    @ApiOperation("Matrix导入")
    public AjaxResult importMatrix(MultipartFile file) throws Exception
    {
        DeviceExcelUtil<ImportMatrixDTO> util = new DeviceExcelUtil<ImportMatrixDTO>(ImportMatrixDTO.class);
        List<ImportMatrixDTO> tMatrixList = util.importExcel("MATRIX",file.getInputStream(),0);
        String operName = getUsername();
        String message = itMatrixService.importMatrix(tMatrixList, true, operName);
        return success(message);
    }

    @PostMapping("/imporValidate")
    @ApiOperation("Validate导入")
    public AjaxResult imporValidate(MultipartFile file) throws Exception
    {
        DeviceExcelUtil<ImportValidateDTO> util = new DeviceExcelUtil<ImportValidateDTO>(ImportValidateDTO.class);
        List<ImportValidateDTO> importValidateDTOS = util.importExcel("VALIDATEALL",file.getInputStream(),0);
        String operName = getUsername();
        String message = itMatrixService.imporValidate(importValidateDTOS, true, operName);
        return success(message);
    }

    @GetMapping("/listMatrix/{matrixType}")
    @ApiOperation("查询Matrix列表")
    @ApiImplicitParam(name = "matrixType", value = "matrix类型", dataType = "String", dataTypeClass = String.class)
    public TableDataInfo list(@PathVariable String matrixType) {
        startPage();
        List<TMatrix> list = itMatrixService.selectMatrixList(matrixType);
        return getDataTable(list);
    }

    @PostMapping("/mappingValue")
    @ApiOperation("查询Matrix多条件规则下唯一映射值")
    public AjaxResult selectMatrixMappingVlalue(@RequestBody MatrixMappingParam matrixListParam)
    {
        AjaxResult ajax = itMatrixService.selectMatrixMappingVlalue(matrixListParam);
        return ajax;
    }

    /**
     * 修改`BIGINT(32)`
     */
    @PutMapping("/editMatrix")
    @ApiOperation("修改Matrix列表")
    public R<String> edit(@RequestBody TMatrix tMatrix)
    {
        if (tMatrix == null || tMatrix.getUid() == null){
            return R.fail("id不得为空");
        }
        if (itMatrixService.updateTMatrix(tMatrix) > 0){
            return R.ok();
        }else {
            return R.fail("Id不存在或者系统里已存在该映射关系");
        }
    }

    /**
     * 删除`BIGINT(32)`
     */
    @DeleteMapping("/deleteMatrix/{uids}")
    @ApiOperation("删除Matrix列表")
    public AjaxResult removeMatrix(@PathVariable Long[] uids)
    {
        return toAjax(itMatrixService.deleteTMatrixByUids(uids));
    }


//    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/validate_all")
    @ApiOperation("查询validateAll列表")
    public R<Map<String,List<SysDictData>>> validateList(SysDictType dictType)
    {
        startPage();
        Map<String,List<SysDictData>> listMap = dictTypeService.selectValidateList();
        return R.ok(listMap);
    }

    @GetMapping("/dictdata/{dictType}")
    @ApiOperation("查询某字典类型下键值列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictType", value = "字典类型", dataType = "String", dataTypeClass = String.class)
    })
    public TableDataInfo listDatalist(@PathVariable String  dictType)
    {
        startPage();
        SysDictData dictData = new SysDictData();
        dictData.setDictType(dictType);
        List<SysDictData> list = dictDataService.selectMatrixDictDataList(dictData);
        return getDataTable(list);
    }


    /**
     * 新增字典类型
     */
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @ApiImplicitParams({
    @ApiImplicitParam(name = "dictLabel", value = "字典标签", dataType = "String", dataTypeClass = String.class),
    @ApiImplicitParam(name = "status", value = "0=正常,1=停用", dataType = "String", dataTypeClass = String.class),
    @ApiImplicitParam(name = "dictValue", value = "字典键值", dataType = "String", dataTypeClass = String.class),
    @ApiImplicitParam(name = "dictType", value = "字典类型", dataType = "String", dataTypeClass = String.class)
    })
    @PostMapping("/addDictdata")
    @ApiOperation("新增字典某类型下键值类型")
    public AjaxResult add(@RequestBody SysDictData dict)
    {
        int i = dictDataService.insertMatrixDictData(dict);
        if (i < 0){
            return AjaxResult.error("已存在");
        }else {
            return AjaxResult.success("success");
        }
    }

    /**
     * 删除字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/dictdata/{dictCodes}")
    @ApiOperation("批量删除字典数据类型")
    public AjaxResult remove(@PathVariable Long[] dictCodes)
    {
        dictDataService.deleteDictDataByIds(dictCodes);
        return success();
    }

    /**
     * 修改保存字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping("/dictedit")
    @ApiOperation("修改字典数据类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictCode", value = "字典编码", dataType = "Long", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "dictLabel", value = "字典标签", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "status", value = "0=正常,1=停用", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "dictValue", value = "字典键值", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "dictType", value = "字典类型", dataType = "String", dataTypeClass = String.class)
    })
    public AjaxResult edit(@RequestBody SysDictData dict)
    {
        return toAjax(dictDataService.updateDictData(dict));
    }

}
