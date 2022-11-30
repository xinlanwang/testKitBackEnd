package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.system.domain.dto.ImportDeviceDTO;
import com.ruoyi.system.domain.param.DeviceListParam;
import com.ruoyi.system.domain.vo.DeviceInfoVo;
import com.ruoyi.system.service.impl.DeviceExcelUtil;
import com.ruoyi.system.service.DeviceListService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ruoyi.common.utils.PageUtils.startPage;

/**
 * `devicelist`Controller
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@Api("设备信息列表")
@RestController
@RequestMapping("/devicelist")
public class DeviceListController extends BaseController
{
    @Autowired
    private DeviceListService deviceListService;

    @PostMapping("/upload")
    @ApiOperation("导入Device")
    public AjaxResult importDeviceData(MultipartFile file) throws Exception
    {
        DeviceExcelUtil<ImportDeviceDTO> util = new DeviceExcelUtil<ImportDeviceDTO>(ImportDeviceDTO.class);
        Map<String,List<ImportDeviceDTO>> importDeviceMap = new HashMap<>();
        util.importDeviceExcel(file.getInputStream(),importDeviceMap);
        String operName = "user";
        String message = deviceListService.importDevice(importDeviceMap, true, operName);
        return success(message);
    }


    /**
     * 查询`车型基本数据`列表
     */
    @ApiOperation("查询车型基本信息列表")
//    @PreAuthorize("@ss.hasPermi('device:devicelist:list')")
    @PostMapping("/query")
    public TableDataInfo list(@Validated @RequestBody DeviceListParam deviceListParam) {
        startPage();
        List list = deviceListService.queryDeviceList(deviceListParam);
        return getDataTable(list);
    }

    @PostMapping("/save")
    @ApiOperation("新增车型详细信息")
    public AjaxResult save(@Validated @RequestBody  DeviceInfoVo deviceInfoVo){
        return toAjax(deviceListService.insertDeviceInfo(deviceInfoVo));
    }

    @PutMapping("/update")
    @ApiOperation("修改车型详细信息")
    public R<String> update(@RequestBody DeviceInfoVo deviceInfoVo){
        int i = deviceListService.updateDeviceInfo(deviceInfoVo);
        if (i <= 0){
            return R.fail("该车型不存在");
        }
        return R.ok();
    }

    /**
     * 获取`车型基本数据`详细信息
     */
//    @PreAuthorize("@ss.hasPermi('device:devicelist:query')")
    @ApiOperation("获取车型基本数据详细信息")
//    @ApiImplicitParam(name = "carlineId",value = "车型ID",required = true,dataType = "String",paramType = "path",dataTypeClass = String.class)
    @GetMapping(value = "/{carlineInfoUid}")
    public R<DeviceInfoVo> getInfo(@PathVariable("carlineInfoUid") Long carlineInfoUid)
    {
        DeviceInfoVo deviceListVo = deviceListService.getInfo(carlineInfoUid);
        if (deviceListVo!=null){
            return R.ok(deviceListVo);
        }else {
            return R.fail("该车型不存在");
        }
    }

    /**
     * 批量删除`车型基本数据`
     */
    @ApiOperation("批量删除车型基本数据")
    @ApiImplicitParam(name = "carlineInfoUids", value = "删除的uid数组", required = true, dataType = "long", paramType = "path", dataTypeClass = Long.class)
//    @PreAuthorize("@ss.hasPermi('device:devicelist:remove')")
    @Log(title = "车型基本数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{carlineInfoUids}")
    public AjaxResult remove(@PathVariable Long[] carlineInfoUids)
    {
        return toAjax(deviceListService.deleteTCarlineByUids(carlineInfoUids));
    }

    /**
     * 上传车型
     */
//    @ApiOperation("上传基本数据")
////    @PreAuthorize("@ss.hasPermi('device:devicelist:remove')")
//    @ApiImplicitParam(name = "file", value = "文件", required = true, dataType = "file", dataTypeClass = MultipartFile.class)
//    @PostMapping("/upload")
//    public AjaxResult uploadFile(MultipartFile file) throws Exception
//    {
//        try
//        {
//            // 上传文件路径
//            String filePath = RuoYiConfig.getUploadPath();
//            // 上传并返回新文件名称
//            String fileName = FileUploadUtils.upload(filePath, file);
////            String url = serverConfig.getUrl() + fileName;
//            AjaxResult ajax = AjaxResult.success();
////            ajax.put("url", url);
//            ajax.put("fileName", fileName);
//            ajax.put("newFileName", FileUtils.getName(fileName));
//            ajax.put("originalFilename", file.getOriginalFilename());
//            return ajax;
//        }
//        catch (Exception e)
//        {
//            return AjaxResult.error(e.getMessage());
//        }
//    }



}
