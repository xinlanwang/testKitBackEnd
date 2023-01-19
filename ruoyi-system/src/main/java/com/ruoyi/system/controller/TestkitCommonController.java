package com.ruoyi.system.controller;

import cn.hutool.core.io.FileUtil;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.dto.ImportGoldenInfoDTO;
import com.ruoyi.system.domain.dto.ImportPartComponentDTO;
import com.ruoyi.system.domain.param.DesktopGetDBParam;
import com.ruoyi.system.service.DesktopService;
import com.ruoyi.system.service.DeviceListService;
import com.ruoyi.system.service.GoldenInfoService;
import com.ruoyi.system.service.impl.DeviceExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.ruoyi.common.constant.TestKitConstants.AUTO_IMPORT_DTC_PATH;
import static com.ruoyi.common.constant.TestKitConstants.AUTO_IMPORT_GOLDEN_PATH;


/**
 * `devicelist`Controller
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@Api("桌面端接口")
@RequestMapping("/")
@RestController
public class TestkitCommonController extends BaseController
{
    @Autowired
    private DeviceListService deviceListService;
    @Autowired
    private GoldenInfoService goldenInfoService;



    /**
     * 更新全部carlineInfo
     * @return
     * @throws Exception
     */
    @GetMapping("refresh/all")
    @ApiOperation("提交")
    public AjaxResult getdb() throws Exception {
        //golden
        Map<String, File> autoImportGoldenFildMap = goldenInfoService.getAutoImportGoldenFildMap();
        DeviceExcelUtil<ImportPartComponentDTO> util = new DeviceExcelUtil<ImportPartComponentDTO>(ImportPartComponentDTO.class);
        List<ImportGoldenInfoDTO> importGoldenInfoDTOS = new ArrayList<>();
        for (String str:autoImportGoldenFildMap.keySet()){
            File file = autoImportGoldenFildMap.get(str);
            util.importGoldenInfoExcel(new FileInputStream(file), importGoldenInfoDTOS);
            String originalFilename = file.getName();
            String message = goldenInfoService.importGoldenInfoDevice(importGoldenInfoDTOS, true, originalFilename);
        }
        //device
        try {
            List<File> files = FileUtil.loopFiles(AUTO_IMPORT_DTC_PATH);
            deviceListService.quarzImportDTCReport();
            System.out.println("总共大小为：" + files.size());
            for (File file:files){
                System.out.println(file.getName());
                long l = file.lastModified();
                Date date = new Date(l);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                System.out.println(simpleDateFormat.format(date));
                System.out.println(file.getParent());
                System.out.println(file.getCanonicalPath());
                System.out.println();
            }
            return AjaxResult.success("success");
        } catch (IOException e) {
            return AjaxResult.success("刷新失败");
        }
    }





    /**
     * 根据Id自动更新当前CarlineInfo
     */
    @ApiOperation("查询车型基本信息列表")
    @GetMapping("refresh/device/{carlineInfoUid}")
    public AjaxResult autoSaveVersionList(@PathVariable("carlineInfoUid") String carlineInfoUid) {
        return AjaxResult.success("device update success");
    }

    /**
     * 根据Id自动更新当前CarlineInfo
     */
    @ApiOperation("查询车型基本信息列表")
    @GetMapping("refresh/golden/{clusterName}")
    public AjaxResult autoSaveVersionList1(@PathVariable("clusterName") String clusterName) throws Exception {
        //删除开始
        Long[] strings = goldenInfoService.selectCarlineInfoIdsByClusterName(clusterName);
        deviceListService.deleteTCarlineByUids(strings);
        //删除结束
        String conrentClusterName = goldenInfoService.refreshGoldenByCarlineInfoUid(clusterName);
        Map<String, File> autoImportGoldenFildMap = goldenInfoService.getAutoImportGoldenFildMap();
        DeviceExcelUtil<ImportPartComponentDTO> util = new DeviceExcelUtil<ImportPartComponentDTO>(ImportPartComponentDTO.class);
        List<ImportGoldenInfoDTO> importGoldenInfoDTOS = new ArrayList<>();
        for (String str:autoImportGoldenFildMap.keySet()){
            if (!str.equals(conrentClusterName)){
                continue;
            }
            File file = autoImportGoldenFildMap.get(str);
            util.importGoldenInfoExcel(new FileInputStream(file), importGoldenInfoDTOS);
            String originalFilename = file.getName();
            String message = goldenInfoService.importGoldenInfoDevice(importGoldenInfoDTOS, true, originalFilename);
        }
        return AjaxResult.success("golden update success");
    }

    @ApiOperation("查询车型基本信息列表")
    @GetMapping("delete/golden/{clusterName}")
    public AjaxResult autoSaveVersionList2(@PathVariable("clusterName") String clusterName) throws Exception {
        Long[] strings = goldenInfoService.selectCarlineInfoIdsByClusterName(clusterName);
        deviceListService.deleteTCarlineByUids(strings);
        return AjaxResult.success("golden delete success");
    }

}
