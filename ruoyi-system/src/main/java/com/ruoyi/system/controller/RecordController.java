package com.ruoyi.system.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.param.DesktopSubmitParam;
import com.ruoyi.system.domain.param.DeviceListParam;
import com.ruoyi.system.domain.param.RecordListParam;
import com.ruoyi.system.service.DesktopService;
import com.ruoyi.system.service.DeviceListService;
import com.ruoyi.system.service.RecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * `devicelist`Controller
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@Api("记录相关")
@RestController
@RequestMapping("/record")
public class RecordController extends BaseController
{
    @Autowired
    private RecordService recordService;

    @ApiOperation("查询测试记录列表")
    @PostMapping("/list")
    public TableDataInfo list(@Validated @RequestBody RecordListParam deviceListParam) {
        startPage();
        List list = recordService.
                list(deviceListParam);
        return getDataTable(list);
    }

    @ApiOperation("查询记录历史列表")
    @GetMapping("/historyList/{operationUid}")
    public TableDataInfo historyList(@PathVariable("operationUid") Long operationUid) {
        startPage();
        List list = recordService.historyList(operationUid);
        return getDataTable(list);
    }



}
