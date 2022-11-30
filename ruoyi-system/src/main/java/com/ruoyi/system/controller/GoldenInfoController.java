package com.ruoyi.system.controller;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.system.domain.dto.ImportGoldenInfoDTO;
import com.ruoyi.system.domain.dto.ImportPartComponentDTO;
import com.ruoyi.system.domain.vo.GoldenInfoVO;
import com.ruoyi.system.service.GoldenInfoService;
import com.ruoyi.system.service.ITCarlineService;
import com.ruoyi.system.service.impl.DeviceExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * `devicelist`Controller
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@Api("版本信息列表")
@RestController
@RequestMapping("/goldeninfo")
public class GoldenInfoController extends BaseController
{
    @Autowired
    private ITCarlineService tCarlineService;
    @Autowired
    private GoldenInfoService goldenInfoService;


    @PostMapping("/importGoldenInfoData")
    @ApiOperation("导入GoldenInfo")
    public AjaxResult importGoldenInfoData(MultipartFile file) throws Exception
    {
        DeviceExcelUtil<ImportPartComponentDTO> util = new DeviceExcelUtil<ImportPartComponentDTO>(ImportPartComponentDTO.class);
        List<ImportGoldenInfoDTO> importGoldenInfoDTOS = new ArrayList<>();
        util.importGoldenInfoExcel(file.getInputStream(),importGoldenInfoDTOS);
        String originalFilename = file.getOriginalFilename();
        String message = goldenInfoService.importGoldenInfoDevice(importGoldenInfoDTOS, true, originalFilename);
        return success(message);
    }

    /**
     * 查询`车型基本数据`列表
     */
    @ApiOperation("查询车型基本信息列表")
//    @PreAuthorize("@ss.hasPermi('device:devicelist:list')")
    @GetMapping("/query")
    public R<Map<String, Object>> list()
    {
        startPage();
        Map<String, Object> goldenListVos = goldenInfoService.querygoldenList();
        return R.ok(goldenListVos);
    }

    /**
     * 获取`车型基本数据`详细信息
     */
//    @PreAuthorize("@ss.hasPermi('device:devicelist:query')")
    @ApiOperation("获取车型基本数据详细信息")
//    @ApiImplicitParam(name = "carlineId",value = "车型ID",required = true,dataType = "String",paramType = "path",dataTypeClass = String.class)
    @GetMapping("/Info")
    public R<List<GoldenInfoVO>> getInfo(@RequestParam("clusterName") String clusterName,@RequestParam("carlineModelType") String carlineModelType) {
        List<GoldenInfoVO> goldenInfoVOS = goldenInfoService.queryClusterInfo(clusterName,carlineModelType);
        if (goldenInfoVOS!=null){
            return R.ok(goldenInfoVOS);
        }else {
            return R.fail("该车型不存在");
        }
    }


    /**
     * 上传车型
     */
    @ApiOperation("上传基本数据")
//    @PreAuthorize("@ss.hasPermi('device:devicelist:remove')")
    @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "file", dataTypeClass = MultipartFile.class)
    @PostMapping("/upload")
    public AjaxResult uploadFile(MultipartFile file) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
//            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
//            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }



}
